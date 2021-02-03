package j.e.c.com.teacherPanelFragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
import j.e.c.com.teacherPanelFragments.TeacherAdapters.teacherAppliedJobAdapter;
import j.e.c.com.teacherPanelFragments.TeacherAdapters.teacherShowingJobAdapter;

public class AppliedJobsFragment extends Fragment {
    SQLiteHandler sqLiteHandler;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.filter)
    LinearLayout filter;
    private ArrayList<School> schoolArrayList;
    private teacherAppliedJobAdapter showingJobAdapter;
    private School school;
    Gson gson;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_showing_jobs, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();
        sqLiteHandler = new SQLiteHandler(getActivity());
        fetchUserId();
        RecevingData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        schoolArrayList = new ArrayList<>();
        showingJobAdapter = new teacherAppliedJobAdapter(this, schoolArrayList);
        recyclerView.setAdapter(showingJobAdapter);
        filter.setVisibility(View.GONE);
        return view;
    }

    private void RecevingData() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_showApplaiedJobsToTeacher,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray services = obj.getJSONArray("result");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < services.length(); i++) {
                                school = gson.fromJson(String.valueOf(services.getJSONObject(i)), School.class);
                                //school.setLicenecePicture("url"+school.getLicenecePicture());
                                //JSONObject sv = services.getJSONObject(i);
                                //String Demand = sv.getString("Demand");
                                //tring salary = sv.getString("salary");
                                //school = new School(Sid,Sname,url);
                                schoolArrayList.add(school);
                                showingJobAdapter.notifyDataSetChanged();
                                //progressBar.setVisibility(View.GONE);
                            }
                            //creating adapter object and setting it to recyclerview
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

    private void fetchUserId() {
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            id = res.getString(3);
///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
        }

    }

    @OnClick(R.id.backArrow)
    public void onViewClicked() {
        getFragmentManager().popBackStack();
    }
}
