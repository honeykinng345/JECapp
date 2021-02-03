package j.e.c.com.commonFragments.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;

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
import j.e.c.com.Models.ScheduleInterview;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;

public class ScheduleInterviewAdapter extends RecyclerView.Adapter<ScheduleInterviewAdapter.ViewHolder> {

    //model class needed for schedule
    //List<>

    ArrayList<ScheduleInterview> scheduleInterviewArrayList;
    Fragment context;
    ProgressDialog progressDialog;

    String jid;



    public ScheduleInterviewAdapter(ArrayList<ScheduleInterview> scheduleInterviewArrayList, Fragment context ,String jid) {
        this.scheduleInterviewArrayList = scheduleInterviewArrayList;
        this.context = context;
        this.jid = jid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_schedule, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ScheduleInterview scheduleInterviews = scheduleInterviewArrayList.get(i);
        if (scheduleInterviews.getSender().equals(Helper.fetchUserId(context.getContext()))){
            viewHolder.confirmButton.setVisibility(View.GONE);
            viewHolder.changeBtn.setVisibility(View.GONE);
        }
        if (scheduleInterviews.getStatus().equals("1")){
            viewHolder.confirmButton.setVisibility(View.GONE);
            viewHolder.changeBtn.setVisibility(View.GONE);
        }
     /*   if (scheduleInterviews.getStatus().equals("1")) {
            viewHolder.wait.setVisibility(View.VISIBLE);
            viewHolder.confirmButton.setVisibility(View.GONE);
            viewHolder.changeBtn.setVisibility(View.GONE);


        }else{
            viewHolder.wait.setVisibility(View.GONE);
        }*/
        viewHolder.date.setText("" + scheduleInterviews.getDate() + scheduleInterviews.getMonth() + scheduleInterviews.getYear());
        viewHolder.time.setText("" + scheduleInterviews.getTime());
        viewHolder.changeBtn.setOnClickListener(v -> changeTimeDateSchedule(scheduleInterviews));
        viewHolder.confirmButton.setOnClickListener(v -> ConfrimDateTimeSchedule(scheduleInterviews));

    }

    String tag_string_req = "req_login";

    private void changeTimeDateSchedule(ScheduleInterview scheduleInterviews) {

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

        alert.setPositiveButton("OK", (dialog, whichButton) -> SendInterviewDateTimeScdule(scheduleInterviews.getId(), startTimeView.getText().toString(), dayView.getText().toString(), monthView.getText().toString(), yearView.getText().toString()));

        alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });

        alert.show();


    }

    private void SendInterviewDateTimeScdule(String id, String startTimeView, String dayView, String monthView, String yearView) {
        progressDialog = new ProgressDialog(context.getContext());
        progressDialog.setMessage("We Are Processing On it");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_updateTimeSchduleDate, response -> {
            progressDialog.dismiss();

            try {

                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                Helper.alert("Interview Schedule Has Been Changed",context.getContext());
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
                params.put("id", id);
                params.put("time", startTimeView);
                params.put("date", dayView);
                params.put("day", dayView);
                params.put("month", monthView);
                params.put("year", yearView);
                params.put("uid", Helper.fetchUserId(context.getContext()));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private void ConfrimDateTimeSchedule(ScheduleInterview scheduleInterviews) {
        progressDialog = new ProgressDialog(context.getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_confirmTimeSchduleDate, response -> {
            progressDialog.dismiss();

            try {

                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                   // Toast.makeText(context.getContext(), "" + response, Toast.LENGTH_LONG).show();


                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(context.getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(context.getContext(), "Json error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }, error -> {

            Toast.makeText(context.getContext(),
                    error.getMessage().toString(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("id", scheduleInterviews.getId());
                params.put("status", "3");
                params.put("jid",jid);
                params.put("uid",Helper.fetchUserId(context.getContext()));

                //

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    @Override
    public int getItemCount() {
        return scheduleInterviewArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.changeBtn)
        TextView changeBtn;
        @BindView(R.id.confirm_button)
        TextView confirmButton;
        @BindView(R.id.wait)
        TextView wait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(context.getContext()), R.style.Theme_MaterialComponents_Dialog_MinWidth, onDateSetListener, year, month, day);
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    String theMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
}

