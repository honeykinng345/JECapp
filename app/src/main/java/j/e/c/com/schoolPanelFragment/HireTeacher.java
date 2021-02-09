package j.e.c.com.schoolPanelFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.School;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.SchoolAdapter.AppliedTeacherAdapter;
import j.e.c.com.schoolPanelFragment.SchoolAdapter.PostedJobAdapter;

public class HireTeacher extends Fragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<Teacher> teacherArrayList;
    private AppliedTeacherAdapter teacherAdapter;
    private  Teacher teacher;
    Gson gson;
    String tid;
    ArrayList<String> tids;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_hire_teacher, container, false);
        gson = new Gson();

        ButterKnife.bind(this, view);

        tids = new ArrayList<>();

        Tids();
        RecevingData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        teacherArrayList = new ArrayList<>();
        teacherAdapter = new AppliedTeacherAdapter(teacherArrayList, tids,this);
        recyclerView.setAdapter(teacherAdapter);


        //Toast.makeText(getContext(),""+Helper.getSchool().getId(),Toast.LENGTH_LONG).show();
        return view;
    }

    private void isSchoolJobExsist() {

        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_isSchoolIdExist,
                response -> {
                    try {
                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);

                        Toast.makeText(getContext(),""+obj,Toast.LENGTH_LONG).show();
                        //we have the array named hero inside the object
                        //so here we are getting that json array
                        JSONArray jba = obj.getJSONArray("result");

                        if(jba == null){
                            Helper.fragmentTransaction(HireTeacher.this, new HireFormOneFragment());
                        }

                        //now looping through all the elements of the json array
                        else  {
                            Helper.setSchool( gson.fromJson(String.valueOf(jba.getJSONObject(0)), School.class));
                            Helper.fragmentTransaction(HireTeacher.this, new HireFormTwoFragment());
                            //school.setLicenecePicture("url"+school.getLicenecePicture());
                            //JSONObject sv = services.getJSONObject(i);
                            //String Demand = sv.getString("Demand");
                            //tring salary = sv.getString("salary");
                            //school = new School(Sid,Sname,url);
                           // schoolArrayList.add(school);
                           // showingJobAdapter.notifyDataSetChanged();
                            //getting product object from json array
                            //  JSONObject categories = heroArray.getJSONObject(i);
                            //adding the product to product list
                        }
                        //creating adapter object and setting it to recyclerview
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    // spinKitView.setVisibility(View.GONE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("sid", Helper.fetchUserId(getContext()));
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    private void RecevingData() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_fetchApplicedJobsData,
                response -> {
                    try {


                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);

                        //we have the array named hero inside the object
                        //so here we are getting that json array
                        JSONArray jba = obj.getJSONArray("result");

                        //now looping through all the elements of the json array
                        for (int i = 0; i < jba.length(); i++) {

                            teacher = gson.fromJson(String.valueOf(jba.getJSONObject(i)), Teacher.class);
                            String url = "https://websitejec1.000webhostapp.com/bpic/"+teacher.getBpic();


                            teacher.setBpic(url);

                            //school.setLicenecePicture("url"+school.getLicenecePicture());

                            //JSONObject sv = services.getJSONObject(i);

                            //String Demand = sv.getString("Demand");
                            //tring salary = sv.getString("salary");


                            //school = new School(Sid,Sname,url);
                            teacherArrayList.add(teacher);
                          teacherAdapter.notifyDataSetChanged();

                            //showingJobAdapter.notifyDataSetChanged();
                            //getting product object from json array
                            //  JSONObject categories = heroArray.getJSONObject(i);

                            //adding the product to product list

                        }
                        //creating adapter object and setting it to recyclerview

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                },
                error -> {

                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    // spinKitView.setVisibility(View.GONE);


                }) { @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<>();
            params.put("jid", Helper.getSchool().getId());


            return params;
        }

        };

        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    private void Tids() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_getTidsFromScheludeTable,
                response -> {
                    try {


                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);

                        //we have the array named hero inside the object
                        //so here we are getting that json array
                        JSONArray jba = obj.getJSONArray("result");
                        Toast.makeText(getContext(),""+jba.getJSONObject(0),Toast.LENGTH_LONG).show();

                        //now looping through all the elements of the json array
                        /*for (JSONObject js: jba
                             ) {

                        }*/
                        for (int i = 0; i < jba.length(); i++) {
                            JSONObject sv = jba.getJSONObject(i);
                            tid =  sv.getString("tid");
                            Toast.makeText(getContext(),""+tid,Toast.LENGTH_LONG).show();

                            tids.add(tid);

//                                teacherAdapter = new AppliedTeacherAdapter(teacherArrayList, HireTeacher.this);
                            //recyclerView.setAdapter(teacherAdapter);
                            //teacherAdapter.notifyDataSetChanged();


                            //school.setLicenecePicture("url"+school.getLicenecePicture());

                            //JSONObject sv = services.getJSONObject(i);

                            //String Demand = sv.getString("Demand");
                            //tring salary = sv.getString("salary");


                            //school = new School(Sid,Sname,url);

                            //showingJobAdapter.notifyDataSetChanged();
                            //getting product object from json array
                            //  JSONObject categories = heroArray.getJSONObject(i);

                            //adding the product to product list

                        }
                        //creating adapter object and setting it to recyclerview

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                },
                error -> {

                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    // spinKitView.setVisibility(View.GONE);


                }) { @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<>();
            params.put("jid", Helper.getSchool().getId());


            return params;
        }

        };

        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    @OnClick({R.id.backArrow, R.id.addBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case  R.id.addBtn:
                isSchoolJobExsist();

                break;
        }
    }



/*    void scheduleInterview() {

        View startTimeLayout;
        TextView dayView, monthView, yearView, startTimeView;

        View dateTimeView = getLayoutInflater().inflate(R.layout.date_time_dialog, null);

        dayView = dateTimeView.findViewById(R.id.day);
        monthView = dateTimeView.findViewById(R.id.month);
        yearView = dateTimeView.findViewById(R.id.year);
        startTimeView = dateTimeView.findViewById(R.id.startTime);

        startTimeLayout = dateTimeView.findViewById(R.id.starTimeLayout);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(dateTimeView);

        dayView.setOnClickListener(v -> setDate(dayView, monthView, yearView));
        monthView.setOnClickListener(v -> setDate(dayView, monthView, yearView));
        yearView.setOnClickListener(v -> setDate(dayView, monthView, yearView));
        startTimeLayout.setOnClickListener(v -> Helper.setTime(startTimeView));

        alert.setPositiveButton("OK", (dialog, whichButton) -> {

        });

        alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });

        alert.show();
    }

    void setDate(TextView dayView, TextView monthView, TextView yearView){

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Dialog_MinWidth, onDateSetListener, year, month, day);
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }*/

}
