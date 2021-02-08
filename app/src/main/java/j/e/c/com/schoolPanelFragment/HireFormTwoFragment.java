package j.e.c.com.schoolPanelFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.School;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.SessionManager;
import j.e.c.com.activites.LoginActivity;
import j.e.c.com.activites.ProfileActivity;
import j.e.c.com.appConfig;
import j.e.c.com.commonFragments.WelcomeFragment;

public class HireFormTwoFragment extends Fragment {

    private  int n=0;

    @BindView(R.id.kpm)
    AppCompatAutoCompleteTextView kpm;
    @BindView(R.id.arrivalDate)
    TextInputLayout arrivalDate;
    @BindView(R.id.jobTitle)
    AppCompatAutoCompleteTextView jobTitle;
    @BindView(R.id.demand)
    AppCompatAutoCompleteTextView demand;
    @BindView(R.id.city)
    TextInputLayout city;
    @BindView(R.id.workingTime)
    TextInputLayout workingTime;
    @BindView(R.id.kidsAge)
    TextInputLayout kidsAge;
    @BindView(R.id.noOfTeachers)
    TextInputLayout noOfTeachers;
    @BindView(R.id.requirements)
    TextInputLayout requirements;
    @BindView(R.id.salary)
    TextInputLayout salary;
    @BindView(R.id.housing)
    TextInputLayout housing;
    @BindView(R.id.advantages)
    TextInputLayout advantages;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    School school;
    ProgressDialog progressDialog;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    SQLiteHandler sqLiteHandler;
private  String id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_hire_form_two, container, false);
        ButterKnife.bind(this, view);
        sqLiteHandler = new SQLiteHandler(getContext());
        fetchSchoolId();
        isSchoolJobExsist();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        kpm.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.kpmArray, getContext()));
        jobTitle.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.jobArray, getContext()));
        demand.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.demandArray, getContext()));

        school = Helper.getSchool();

        if (Helper.isTeacherComeFromAdapter) {
            checkbox.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            fillForm(school);
        }
    }

    private void fetchSchoolId() {


        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            id = res.getString(3);
///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.backArrow, R.id.submitBtn, R.id.arrivalDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                Helper.goBackFromFragment(this);
                break;
            case R.id.submitBtn:
                if (validateFields())
                    setValues();
                break;
            case R.id.arrivalDate:
                Helper.setDate(arrivalDate);
                break;
        }
    }

    private void setValues() {
        school.setCity(Objects.requireNonNull(city.getEditText()).getText().toString());
        school.setJobTitle(jobTitle.getText().toString());
        school.setWorkTime(Objects.requireNonNull(workingTime.getEditText()).getText().toString());
        school.setKidsAge(Objects.requireNonNull(kidsAge.getEditText()).getText().toString());
        school.setKPMUT(kpm.getText().toString());
        school.setArrivalDate(Objects.requireNonNull(arrivalDate.getEditText()).getText().toString());
        school.setDemand(demand.getText().toString());
        school.setNumberOfTeaches(Objects.requireNonNull(noOfTeachers.getEditText()).getText().toString());
        school.setRequimentsscholl(Objects.requireNonNull(requirements.getEditText()).getText().toString());
        school.setSalary(Objects.requireNonNull(salary.getEditText()).getText().toString());
        school.setHousing(Objects.requireNonNull(housing.getEditText()).getText().toString());
        school.setAdvantage(Objects.requireNonNull(advantages.getEditText()).getText().toString());
        school.setStatus("0");
        school.setStid(id);
        sendData();
    }

    private void sendData() {
        if (n!=0)
            ProccedNext(new schoolPostedJobFragment());
        else
            ProccedNext(new WelcomeFragment());
    }

    boolean validateFields() {
        boolean b = true;
        if (!Helper.validateField(city))
            b = false;
        if (!Helper.validateField(jobTitle))
            b = false;
        if (!Helper.validateField(workingTime))
            b = false;
        if (!Helper.validateField(kidsAge))
            b = false;
        if (!Helper.validateField(kpm))
            b = false;
        if (!Helper.validateField(arrivalDate))
            b = false;
        if (!Helper.validateField(demand))
            b = false;
        if (!Helper.validateField(noOfTeachers))
            b = false;
        if (!Helper.validateField(requirements))
            b = false;
        if (!Helper.validateField(salary))
            b = false;
        if (!Helper.validateField(housing))
            b = false;
        if (!Helper.validateField(advantages))
            b = false;
        if (!checkbox.isChecked()) {
            checkbox.setError("You need to agree with us");
            b = false;
        } else checkbox.setError(null);
        return b;
    }

    void fillForm(School school){
city.getEditText().setText(school.getCity());
jobTitle.setText(school.getJobTitle());
workingTime.getEditText().setText(school.getWorkTime());
kidsAge.getEditText().setText(school.getKidsAge());
kpm.setText(school.getKPMUT());
arrivalDate.getEditText().setText(school.getArrivalDate());
demand.setText(school.getDemand());
noOfTeachers.getEditText().setText(school.getNumberOfTeaches());
requirements.getEditText().setText(school.getRequimentsscholl());
salary.getEditText().setText(school.getSalary());
housing.getEditText().setText(school.getHousing());
advantages.getEditText().setText(school.getAdvantage());

city.getEditText().setEnabled(false);
        city.getEditText().setEnabled(false);
        jobTitle.setEnabled(false);
        workingTime.getEditText().setEnabled(false);
        kidsAge.getEditText().setEnabled(false);
        kpm.setEnabled(false);
        arrivalDate.getEditText().setEnabled(false);
        demand.setEnabled(false);
        noOfTeachers.getEditText().setEnabled(false);
        requirements.getEditText().setEnabled(false);
        salary.getEditText().setEnabled(false);
        housing.getEditText().setEnabled(false);
        advantages.getEditText().setEnabled(false);
    }

    private void isSchoolJobExsist() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_isSchoolIdExist,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("result");
                            n = jsonArray.length();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        // spinKitView.setVisibility(View.GONE);


                    }

                }) { @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<>();
            params.put("sid",id);


            return params;
        }

        };

        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public  void  ProccedNext(Fragment fragment){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Please Wait We Are Uploading Your Information");
        progressDialog.show();
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_School, response -> {
            progressDialog.dismiss();
             //Toast.makeText(getActivity(),""+response,Toast.LENGTH_LONG).show();
            // Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();

          //  Helper.fragmentTransaction(this, fragment);

            try {
                JSONObject jObj = new JSONObject(response);
               Toast.makeText(getActivity(),""+jObj,Toast.LENGTH_LONG).show();
                boolean error = jObj.getBoolean("error");
                if (!error) {

                    Helper.fragmentTransaction(this,fragment, false);

                  //  Toast.makeText(getActivity(),""+error,Toast.LENGTH_LONG).show();
                    // User successfully stored in MySQL
                    // Toast.makeText(ProfileActivity.this,""+response,Toast.LENGTH_LONG).show();
                    // Now store the user in sqlite
                    // Now store the user in sqlite
                      /*String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");*/
                   // startActivity(new Intent(, LoginActivity.class));
                   // finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_LONG).show();
                Log.d("hh", e.toString());
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("agentReference", school.getAgentReference());
                params.put("schoolLocation", school.getSchoolLocation());
                params.put("ApplicantName", school.getApplicantName());
                params.put("postionInSchool", school.getPostionInSchool());
                params.put("ContactNumber", school.getContactNumber());
                params.put("weChatId", school.getWeChatId());
                params.put("licenecePicture", school.getLicenecePicture());
                params.put("licenseLiveImage", school.getLicenseLiveImage());
                params.put("city", school.getCity());
                params.put("jobTitle", school.getJobTitle());
                params.put("workTime", school.getWorkTime());
                params.put("kidsAge", school.getKidsAge());
                params.put("KPMUT", school.getKPMUT());
                params.put("ArrivalDate", school.getArrivalDate());
                params.put("Demand", school.getDemand());
                params.put("numberOfTeaches", school.getNumberOfTeaches());
                params.put("requimentsscholl", school.getRequimentsscholl());
                params.put("salary", school.getSalary());
                params.put("housing", school.getHousing());
                params.put("advantage", school.getAdvantage());
                params.put("status", school.getStatus());
                params.put("stid", school.getStid());

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }
}
