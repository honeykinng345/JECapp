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
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.SchoolAdapter.AppliedTeacherAdapter;
import j.e.c.com.schoolPanelFragment.SchoolAdapter.ReceivedOffersAdapter;

public class ReceivedOffers extends Fragment {
    private ArrayList<Teacher> teacherArrayList;
    private ReceivedOffersAdapter teacherAdapter;
    private  Teacher teacher;
    Gson gson;
    String tid;
    ArrayList<String> tids;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_received_offers, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        RecevingData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        teacherArrayList = new ArrayList<>();
        teacherAdapter = new ReceivedOffersAdapter(teacherArrayList,this);
        recyclerView.setAdapter(teacherAdapter);
    }

    @OnClick(R.id.backArrow)
    public void onViewClicked() {
        getFragmentManager().popBackStack();
    }

    private void RecevingData() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_fetchApplicedJobsData,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                           //Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jba = obj.getJSONArray("result");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jba.length(); i++) {

                                teacher = gson.fromJson(String.valueOf(jba.getJSONObject(i)), Teacher.class);
                                String url = "http://jeccompany.ml/bpic/"+teacher.getBpic();


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
            params.put("sid", Helper.fetchUserId(getContext()));


            return params;
        }

        };

        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
}
