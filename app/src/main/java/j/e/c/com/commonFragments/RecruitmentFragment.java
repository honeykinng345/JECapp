package j.e.c.com.commonFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import j.e.c.com.appConfig;

public class RecruitmentFragment extends Fragment {
    ProgressDialog progressDialog;
    Gson gson;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruitment, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this, view);
        gson = new Gson();
        return view;
    }

    @OnClick({R.id.backArrow, R.id.apply, R.id.hire})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.apply:
                TistecherJobExsist();
                break;
            case R.id.hire:
                isSchoolPositionExsisit();
                break;
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
                        //Toast.makeText(getActivity(),""+response,Toast.LENGTH_LONG).show();
                        //  istecher = obj.getBoolean("result");
                        // String check = obj.getString("result");
                        if (!obj.isNull("result")) {
                            JSONArray jba = obj.getJSONArray("result");
                            for (int i = 0; i < jba.length(); i++) {
                                Helper.setTeacher(gson.fromJson(String.valueOf(jba.getJSONObject(i)), Teacher.class));
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
                            Helper.setTeacher(null);
                        }
                        progressDialog.dismiss();
                        Helper.fragmentTransaction(this, new RecApplyFragment());

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
                params.put("tid", Helper.fetchUserId(getContext()));
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void isSchoolPositionExsisit() {
        progressDialog.show();
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_isSchoolIdExist,
                response -> {
                    try {


                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);

                        //Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                        if (!obj.isNull("result")) {
                            JSONArray jsonArray = obj.getJSONArray("result");
                            //   n=jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Helper.setSchool(gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), School.class));
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
                        }
                        Helper.fragmentTransaction(this, new RecHireFragment());
                        progressDialog.dismiss();

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
                params.put("sid", Helper.fetchUserId(getContext()));
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
}
