package j.e.c.com.schoolPanelFragment.SchoolAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.AppController;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
import j.e.c.com.commonFragments.PaymentFragment;
import j.e.c.com.commonFragments.PlayVideoFragment;
import j.e.c.com.teacherPanelFragments.JobFormFragment;

public class AppliedTeacherAdapter extends RecyclerView.Adapter<AppliedTeacherAdapter.viewHolder> {
    SQLiteHandler sqLiteHandler;
    ProgressDialog progressDialog;


    private ArrayList<Teacher> teacherArrayList;
    private ArrayList<String> tids;
    private Fragment context;
    //private ArrayList<String> tids;


    public AppliedTeacherAdapter(ArrayList<Teacher> teacherArrayList, ArrayList tids, Fragment context) {
        this.teacherArrayList = teacherArrayList;
        this.tids = tids;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_applied_teachers, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Teacher teacher = teacherArrayList.get(position);
        //fetchSchoolId();
        for (String a : tids) {
            if (teacher.getTid().equals(a)) {
                //do what you want
                holder.other.setVisibility(View.VISIBLE);
                holder.hireBtn.setVisibility(View.GONE);
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rejectTeacher(teacher.getTid());
                    }
                });
            } else {
                holder.hireBtn.setVisibility(View.VISIBLE);
                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);

            }
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "" + position, Toast.LENGTH_LONG).show();
                    Helper.setTeacher(teacherArrayList.get(position));
                    Helper.isTeacherComeFromAdapter = true;
                    Helper.fragmentTransaction(context, new PaymentFragment());

                }
            });

        }
        holder.location.setText("" + teacher.getCountry());
        try {
            Picasso.get().load(teacher.getBpic()).fit().into(holder.image);

        } catch (Exception e) {
            Picasso.get().load(R.drawable.khabib).into(holder.image);
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context.getContext());
                View paymentItem = context.getLayoutInflater().inflate(R.layout.payment_item, null);
                ImageView imageView = paymentItem.findViewById(R.id.image);
                Picasso.get().load(teacher.getBpic()).into(imageView);
                alert.setView(paymentItem);
                alert.setPositiveButton("OK", (dialog, whichButton) -> {
                    //What ever you want to do with the value
                });
                alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
                    // what ever you want to do with No option.
                });
                alert.show();
            }
        });
        holder.gender.setText("" + teacher.getGender());
        holder.salary.setText("" + teacher.getSalary());
        holder.fullTime.setText("" + teacher.getJobtitle());
        holder.nativ.setText("" + teacher.getJobtitle());
        holder.form.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "" + position, Toast.LENGTH_LONG).show();
            Helper.setTeacher(teacherArrayList.get(position));
            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(context, new JobFormFragment());
        });
        holder.videoIcon.setOnClickListener(v -> {
            Helper.setTeacher(teacherArrayList.get(position));
            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(context, new PlayVideoFragment());

        });
        holder.hireBtn.setOnClickListener(v -> {
            scheduleInterview(teacher);

        });


    }

    private void rejectTeacher(String tid) {
        progressDialog = new ProgressDialog(context.getContext());
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_REJECTED_TEACER, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    // Toast.makeText(context.getContext(), "" + jObj, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(context.getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(context.getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }, error -> {
            Toast.makeText(context.getContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("id", tid);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    /*   private void fetchSchoolId() {

           sqLiteHandler = new SQLiteHandler(context.getContext());
           Cursor res = sqLiteHandler.getAllData();
           while (res.moveToNext()) {

               sid = res.getString(3);
   ///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
           }
       }*/
    @Override
    public int getItemCount() {
        return teacherArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.videoIcon)
        ImageView videoIcon;
        @BindView(R.id.nativ)
        TextView nativ;
        @BindView(R.id.salary)
        TextView salary;
        @BindView(R.id.fullTime)
        TextView fullTime;
        @BindView(R.id.gender)
        TextView gender;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.hireBtn)
        Button hireBtn;
        @BindView(R.id.image)
        CircularImageView image;
        @BindView(R.id.form)
        LinearLayout form;
        @BindView(R.id.other)
        LinearLayout other;
        @BindView(R.id.accept)
        Button accept;
        @BindView(R.id.reject)
        Button reject;
        @BindView(R.id.offer)
        TextView offer;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    void scheduleInterview(Teacher teacher) {
        View startTimeLayout;
        TextView dayView, monthView, yearView, startTimeView;
        View dateTimeView = context.getLayoutInflater().inflate(R.layout.date_time_dialog, null);
        dayView = dateTimeView.findViewById(R.id.day);
        monthView = dateTimeView.findViewById(R.id.month);
        yearView = dateTimeView.findViewById(R.id.year);
        startTimeView = dateTimeView.findViewById(R.id.startTime);
        startTimeLayout = dateTimeView.findViewById(R.id.starTimeLayout);
        AlertDialog.Builder alert = new AlertDialog.Builder(context.getContext());
        alert.setView(dateTimeView);
        dayView.setOnClickListener(v -> setDate(dayView, monthView, yearView));
        monthView.setOnClickListener(v -> setDate(dayView, monthView, yearView));
        yearView.setOnClickListener(v -> setDate(dayView, monthView, yearView));
        startTimeLayout.setOnClickListener(v -> Helper.setTime(startTimeView));
        alert.setPositiveButton("OK", (dialog, whichButton) -> {
            SendInterviewDateTimeScdule(teacher.getTid(), startTimeView.getText().toString(), dayView.getText().toString(), monthView.getText().toString(), yearView.getText().toString(), Helper.getSchool().getId(), Helper.getSchool().getStid());

        });
        alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });
        alert.show();
    }

    String tag_string_req = "req_login";

    private void SendInterviewDateTimeScdule(String tid, String startTimeView, String dayView, String monthView, String yearView, String jid, String stid) {
        progressDialog = new ProgressDialog(context.getContext());
        progressDialog.setMessage("We Are Processing On it");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_InterviewDateTimeSchdule, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    // Toast.makeText(context.getContext(), "" + jObj, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(context.getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(context.getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }, error -> {
            Toast.makeText(context.getContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("tid", tid);
                params.put("sid", stid);
                params.put("jid", jid);
                params.put("time", startTimeView);
                params.put("date", dayView);
                params.put("day", dayView);
                params.put("month", monthView);
                params.put("year", yearView);
                params.put("status", "0");
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    void setDate(TextView dayView, TextView monthView, TextView yearView) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
            int mMonth = month + 1;
            //String mDate = dayOfMonth + "-" + mMonth + "-" + year;
            //Objects.requireNonNull(dateView.getEditText()).setText(mDate);
            dayView.setText(String.valueOf(dayOfMonth));
            monthView.setText(theMonth(month));
            yearView.setText(String.valueOf(year));
        };
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context.getContext(), R.style.Theme_MaterialComponents_Dialog_MinWidth, onDateSetListener, year, month, day);
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    String theMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
}
