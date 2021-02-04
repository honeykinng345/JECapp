package j.e.c.com.schoolPanelFragment.SchoolAdapter;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.AppController;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.Others.ScheduleHelper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
import j.e.c.com.chatFragments.ChatFragment;
import j.e.c.com.commonFragments.NotificationFragment;
import j.e.c.com.commonFragments.PlayVideoFragment;
import j.e.c.com.commonFragments.ScheduledInterviewsFragment;
import j.e.c.com.teacherPanelFragments.JobFormFragment;

public class ReceivedOffersAdapter extends RecyclerView.Adapter<ReceivedOffersAdapter.viewHolder> {



    private ArrayList<Teacher> teacherArrayList;
    private Fragment context;

    public ReceivedOffersAdapter(ArrayList<Teacher> teacherArrayList, Fragment context) {
        this.teacherArrayList = teacherArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_applied_teachers, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Teacher teacher = teacherArrayList.get(position);
        holder.hireBtn.setVisibility(View.GONE);
        holder.other.setVisibility(View.VISIBLE);
        //status check
        switch (teacher.getStatus()) {
            case "0":
                holder.offer.setText("Offers : "+teacher.getWechatid()+" rmb");
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getPhone is a job id primary ker in this context
                        ScheduleHelper.scheduleInterview(teacher, context);
                        //updateJobStatus(teacher.getPhone(), "1");
                    }
                });
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.updateJobStatus(teacher.getPhone(), "2", context);
                    }
                });
                break;
            case "1":
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.GONE);
                holder.accept.setText("Schedule");
                holder.offer.setVisibility(View.GONE);

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.fragmentTransaction(context, new ScheduledInterviewsFragment(teacher.getAgent()));
                    }
                });

                break;
            case "2":
                holder.accept.setVisibility(View.GONE);
                holder.reject.setText("Rejected");
                holder.offer.setVisibility(View.GONE);
                break;
            case "3":
              /*  holder.accept.setVisibility(View.GONE);
                holder.reject.setText("Rejected");*/
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.GONE);
                holder.accept.setText("Schedule");
                holder.offer.setText("Interview link");
                holder.offer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.fragmentTransaction(context, new NotificationFragment());
                    }
                });

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.fragmentTransaction(context, new ScheduledInterviewsFragment(teacher.getAgent()));
                    }
                });
                break;

        /*    case "4":
              *//*  holder.accept.setVisibility(View.GONE);
                holder.reject.setText("Rejected");*//*
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.accept.setText("Accpet");
                holder.reject.setText("Reject");
                holder.offer.setText("Chat");
                holder.offer.setOnClickListener(v -> Helper.fragmentTransaction(context, new ChatFragment()));

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SchoolAcceptTeacherAfterInterViewStatusUpdate(teacher);

                    }
                });
                break;*/
            case "4":
                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
                holder.offer.setText(R.string.chat);
                holder.offer.setOnClickListener(v -> Helper.fragmentTransaction(context, new ChatFragment()));
                break;
        }

        holder.location.setText(" "+teacher.getCountry());

        try {
            Picasso.get().load(teacher.getBpic()).fit().into(holder.image);

        } catch (Exception e) {
            Picasso.get().load(R.drawable.khabib).into(holder.image);
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context.getContext());
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
    }
    String tag_string_req = "req_login";
    private void SchoolAcceptTeacherAfterInterViewStatusUpdate(Teacher teacher) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_SchoolAcceptTeacherAfterInterViewStatusUpdate, response -> {


            try {

                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                   // Helper.alert("Interview Schedule Has Been Changed",context.getContext());
                    context.onResume();
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("Filed required");
                    Toast.makeText(context.getContext(), errorMsg, Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(context.getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

            }

        }, error -> {

            Toast.makeText(context.getContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("id",teacher.getPhone());



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    private void updateJobStatus(String phone, String status) {



        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_updateApplayTableRow,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                          //  Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            Boolean result = obj.getBoolean("result");

                            if (result){
                                context.onResume();
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context.getContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        // spinKitView.setVisibility(View.GONE);


                    }

                }) { @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<>();
            params.put("id", phone);
            params.put("status", status);


            return params;
        }

        };

        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

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
}
