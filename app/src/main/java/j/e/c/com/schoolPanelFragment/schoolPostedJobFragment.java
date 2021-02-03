package j.e.c.com.schoolPanelFragment;

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
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.SchoolAdapter.PostedJobAdapter;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class schoolPostedJobFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.nothingFound)
    LinearLayout nothingFound;
    private ArrayList<School> schoolArrayList;
    private PostedJobAdapter showingJobAdapter;
    private School school;
    Gson gson;
    SQLiteHandler sqLiteHandler;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posted_job_fragment, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();
        sqLiteHandler = new SQLiteHandler(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        schoolArrayList = new ArrayList<>();
        showingJobAdapter = new PostedJobAdapter(this, schoolArrayList);
        recyclerView.setAdapter(showingJobAdapter);
        fetchSchoolId();
        RecevingData();
        return view;
    }

    private void RecevingData() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_getAllSchoolUploadJobs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                           // Toast.makeText(getContext(),""+response,T)
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            if (!obj.isNull("result")) {
                                JSONArray jba = obj.getJSONArray("result");
                                //now looping through all the elements of the json array
                                for (int i = 0; i < jba.length(); i++) {
                                    school = gson.fromJson(String.valueOf(jba.getJSONObject(i)), School.class);

                                    if (school != null) {
                                        schoolArrayList.add(school);
                                        showingJobAdapter.notifyDataSetChanged();


                                    }

                                    else{
                                        nothingFound.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    //school.setLicenecePicture("url"+school.getLicenecePicture());
                                    //JSONObject sv = services.getJSONObject(i);
                                    //String Demand = sv.getString("Demand");
                                    //tring salary = sv.getString("salary");
                                    //school = new School(Sid,Sname,url);

                                    //getting product object from json array
                                    //  JSONObject categories = heroArray.getJSONObject(i);
                                    //adding the product to product list
                                }
                                //creating adapter object and setting it to recyclerview
                            }else{

                                    nothingFound.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                            catch (JSONException e) {
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
                params.put("sid", id);
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
            case R.id.addBtn:
                Helper.fragmentTransaction(this, new HireFormTwoFragment());
                break;

        }
    }

    private void fetchSchoolId() {
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            id = res.getString(3);
///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
        }
    }

}
