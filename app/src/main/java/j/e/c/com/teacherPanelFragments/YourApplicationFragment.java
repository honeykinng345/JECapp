package j.e.c.com.teacherPanelFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;

public class YourApplicationFragment extends Fragment {

    Teacher teacher;
    Gson gson;

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
    @BindView(R.id.application)
    LinearLayout application;
    @BindView(R.id.nothingFound)
    LinearLayout nothingFound;
    @BindView(R.id.active)
    Button active;
    @BindView(R.id.inActive)
    Button inActive;
    @BindView(R.id.image)
    CircularImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_applications, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();
        TistecherJobExsist();
        return view;
    }

    @OnClick({R.id.backArrow, R.id.form, R.id.image, R.id.videoIcon, R.id.delete, R.id.addReccord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.form:
                break;
            case R.id.image:
                break;
            case R.id.videoIcon:
                break;
            case R.id.delete:
                break;
            case R.id.addReccord:
                Helper.fragmentTransaction(this, new TeacherApplyFragment());
                break;
        }
    }

    private void TistecherJobExsist() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_isTeacherPositionExist,
                response -> {
                    try {
                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);
                       // Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                        //  istecher = obj.getBoolean("result");
                        // String check = obj.getString("result");
                        if (!obj.isNull("result")) {
                            JSONArray jba = obj.getJSONArray("result");
                            for (int i = 0; i < jba.length(); i++) {
                                teacher = gson.fromJson(String.valueOf(jba.getJSONObject(i)), Teacher.class);
                                if (teacher != null) {
                                    application.setVisibility(View.VISIBLE);
                                    nothingFound.setVisibility(View.GONE);
                                    salary.setText(teacher.getSalary());
                                    location.setText(teacher.getCountry());
                                    gender.setText(teacher.getGender());
                                    nativ.setText(teacher.getNationality());
                                    fullTime.setText(teacher.getJobtitle());
                                    String imageUrl = "http://jeccompany.ml/bpic/" + teacher.getBpic();
                                    teacher.setBpic(imageUrl);
                                    try {
                                        Picasso.get().load(teacher.getBpic()).into(image);
                                    } catch (Exception e) {
                                    }
                                    if (teacher.getStatus().equals("0")) {
                                        active.setVisibility(View.GONE);
                                        inActive.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    application.setVisibility(View.GONE);
                                    nothingFound.setVisibility(View.VISIBLE);
                                }


                            }
                        }else {
                            nothingFound.setVisibility(View.VISIBLE);
                        }
                        //we have the array named hero inside the object
                        //so here we are getting that json array
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
                params.put("tid", Helper.fetchUserId(getContext()));
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

}
