package j.e.c.com.teacherPanelFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.School;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.SessionManager;
import j.e.c.com.activites.LoginActivity;
import j.e.c.com.appConfig;
import j.e.c.com.commonFragments.DummyListingFragment;
import j.e.c.com.commonFragments.WelcomeFragment;
import j.e.c.com.schoolPanelFragment.DummyTeacherFragment;
import j.e.c.com.schoolPanelFragment.HireFormOneFragment;
import j.e.c.com.schoolPanelFragment.schoolPostedJobFragment;
import j.e.c.com.schoolPanelFragment.schoolShowingTeachersFragment;

public class TeacherFragment extends Fragment {
    SQLiteHandler sqLiteHandler;
ProgressDialog progressDialog;

SessionManager sessionManager ;
    private int n=0;
    private String id;
    boolean istecher;
  Teacher teacher;
  School school;
  Gson gson;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        sqLiteHandler = new SQLiteHandler(getActivity());
        sessionManager  = new SessionManager(getActivity());

        fetchUserId();


        return view;
    }


    @OnClick({R.id.backArrow, R.id.applyBtn, R.id.hireBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.applyBtn:
       if (sessionManager.isLoggedIn()){
           TistecherJobExsist();
       }else{
           Helper.fragmentTransaction(this, new DummyListingFragment());
       }
                break;
            case R.id.hireBtn:
                if (sessionManager.isLoggedIn()){
                   // TistecherJobExsist();
                    isSchoolJobExsist();
                }else{
                    Helper.fragmentTransaction(this, new DummyTeacherFragment());
                }





                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
    private void isSchoolJobExsist() {

        progressDialog.show();
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_isSchoolIdExist,
                response -> {
                    try {
                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);

                        if (!obj.isNull("result")) {
                            JSONArray jsonArray = obj.getJSONArray("result");
                            //   n=jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                school = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), School.class);
                                //Toast.makeText(getActivity(),""+jba,Toast.LENGTH_LONG).show();
                                //school.setLicenecePicture("url"+school.getLicenecePicture());
                                //JSONObject sv = services.getJSONObject(i);
                                //String Demand = sv.getString("Demand");
                                //tring salary = sv.getString("salary");
                                //showingJobAdapter.notifyDataSetChanged();
                                //getting product object from json array
                                //  JSONObject categories = heroArray.getJSONObject(i);
                                //adding the product to product list
                            }
                        }else {
                            school = null;
                        }
                        progressDialog.dismiss();
                        if (school!= null) {

                            if (school.getStatus().equals("1")){
                                Helper.fragmentTransaction(this, new schoolShowingTeachersFragment());
                            }else{
                                Helper.fragmentTransaction(this, new WelcomeFragment());
                            }

                        } else {
                            Helper.fragmentTransaction(this, new DummyTeacherFragment());

                        }
                        //we have the array named hero inside the object
                        //so here we are getting that json array
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    // spinKitView.setVisibility(View.GONE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("sid", id);
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    private void fetchUserId() {
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            id = res.getString(3);
///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
        }
    }
    private void TistecherJobExsist() {
        progressDialog.show();
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_isTeacherPositionExist,
                response -> {
                    try {
                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);

                      //  istecher = obj.getBoolean("result");
                       // String check = obj.getString("result");
                        if (!obj.isNull("result")) {
                            JSONArray jba = obj.getJSONArray("result");
                            for (int i = 0; i < jba.length(); i++) {
                                teacher = gson.fromJson(String.valueOf(jba.getJSONObject(i)), Teacher.class);
                                //Toast.makeText(getActivity(),""+jba,Toast.LENGTH_LONG).show();
                                //school.setLicenecePicture("url"+school.getLicenecePicture());
                                //JSONObject sv = services.getJSONObject(i);
                                //String Demand = sv.getString("Demand");
                                //tring salary = sv.getString("salary");
                                //showingJobAdapter.notifyDataSetChanged();
                                //getting product object from json array
                                //  JSONObject categories = heroArray.getJSONObject(i);
                                //adding the product to product list
                            }
                        }else {
                            teacher = null;

                        }
                        progressDialog.dismiss();
                        if (teacher!= null) {
                            if (teacher.getStatus().equals("1")){
                                Helper.fragmentTransaction(this, new TeacherShowingJobsFragment());
                            }else{
                                Helper.fragmentTransaction(this, new WelcomeFragment());
                            }

                        }else {
                            Helper.fragmentTransaction(this, new DummyListingFragment());

                        }


                        //we have the array named hero inside the object
                        //so here we are getting that json array
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ///Log.e("TistecherJobExsist()", e.toString());

                        progressDialog.dismiss();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    // spinKitView.setVisibility(View.GONE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("tid", id);
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

}
