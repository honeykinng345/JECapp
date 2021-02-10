package j.e.c.com.Others;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import j.e.c.com.AppController;
import j.e.c.com.Models.Teacher;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.chatFragments.ChatFragment;

public class ScheduleHelper {
    public static void scheduleInterview(Teacher teacher, Fragment context) {
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
        dayView.setOnClickListener(v -> setDate(dayView, monthView, yearView, context.getContext()));
        monthView.setOnClickListener(v -> setDate(dayView, monthView, yearView, context.getContext()));
        yearView.setOnClickListener(v -> setDate(dayView, monthView, yearView, context.getContext()));
        startTimeLayout.setOnClickListener(v -> Helper.setTime(startTimeView));
        alert.setPositiveButton("OK", (dialog, whichButton) -> {
            SendInterviewDateTimeScdule(context, teacher, startTimeView.getText().toString(), dayView.getText().toString(), monthView.getText().toString(), yearView.getText().toString(), Helper.getSchool().getId(), Helper.getSchool().getStid());

        });
        alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });
        alert.show();
    }

    static void setDate(TextView dayView, TextView monthView, TextView yearView, Context context) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.Theme_MaterialComponents_Dialog_MinWidth, onDateSetListener, year, month, day);
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    static String theMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    static void SendInterviewDateTimeScdule(Fragment context, Teacher teacher, String startTimeView, String dayView, String monthView, String yearView, String jid, String stid) {
        ProgressDialog progressDialog = new ProgressDialog(context.getContext());
        progressDialog.setMessage("We Are Processing On it");
        progressDialog.show();
        ProgressDialog finalProgressDialog = progressDialog;
        ProgressDialog finalProgressDialog1 = progressDialog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_InterviewDateTimeSchdule, response -> {
            finalProgressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    // Toast.makeText(context.getContext(), "" + jObj, Toast.LENGTH_LONG).show();
                    finalProgressDialog.dismiss();
                    String status;
                    if (context instanceof ChatFragment)
                        status = "School Interview";
                    else
                        status = "1";
                    Helper.updateJobStatus(teacher.getPhone(), status, context);
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(context.getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    finalProgressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(context.getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finalProgressDialog.dismiss();
            }

        }, error -> {
            Toast.makeText(context.getContext(),"errro"+
                    error.toString(), Toast.LENGTH_LONG).show();
            finalProgressDialog1.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("tid", teacher.getTid());
                params.put("sid", stid);
                params.put("jid", jid);
                params.put("time", startTimeView);
                params.put("date", dayView);
                params.put("day", dayView);
                params.put("month", monthView);
                params.put("year", yearView);
                params.put("status", "0");
                params.put("sender", Helper.fetchUserId(context.getContext()));
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "req_login");


    }

}
