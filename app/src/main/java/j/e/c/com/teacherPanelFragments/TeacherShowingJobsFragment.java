package j.e.c.com.teacherPanelFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.Models.School;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.teacherPanelFragments.TeacherAdapters.teacherShowingJobAdapter;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class TeacherShowingJobsFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<School> schools;
    private ArrayList<School> clonedList;
    private teacherShowingJobAdapter showingJobAdapter;
    private School school;
    Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_showing_jobs, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        schools = new ArrayList<>();
        showingJobAdapter = new teacherShowingJobAdapter(this, schools);
        recyclerView.setAdapter(showingJobAdapter);
        RecevingData();
        return view;
    }

    private void RecevingData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_tJobs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
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
                                schools.add(school);
                                showingJobAdapter.notifyDataSetChanged();
                                //progressBar.setVisibility(View.GONE);
                            }
                            //converting the string to json array object
                            //creating adapter object and setting it to recyclerview
                            // mainPageServicesAdapter adapter = new mainPageServicesAdapter(MainUserActivity.this, catList);
                            //serviceRV.setAdapter(adapter);
                        } catch (JSONException e) {
                            //progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.INVISIBLE);
                    }

                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @OnClick({R.id.backArrow, R.id.filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.filter:
                filterResult();
                break;
        }
    }

    private void filterResult() {

        clonedList = new ArrayList<>();
        for (School school : schools){
            clonedList.add(school);
        }

        View filterView = getLayoutInflater().inflate(R.layout.pop_search_filter, null);

        TextView clearAll = filterView.findViewById(R.id.clearAll);
        RadioGroup nationalityGroup = filterView.findViewById(R.id.nationalityRadioGroup);
        RadioGroup jobTypeGroup = filterView.findViewById(R.id.jobTypeRadioGroup);
        EditText minSalary = filterView.findViewById(R.id.salaryMin);
        EditText maxSalary = filterView.findViewById(R.id.salaryMax);
        EditText locationEditText = filterView.findViewById(R.id.location);

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nationalityGroup.clearCheck();
                jobTypeGroup.clearCheck();
                minSalary.setText(null);
                maxSalary.setText(null);
                locationEditText.setText(null);
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setView(filterView);

        alert.setPositiveButton("SEARCH", (dialog, which) -> {

            ArrayList<School> schoolArrayList = new ArrayList<>();

            String minS = minSalary.getText().toString().trim();
            String maxS = maxSalary.getText().toString().trim();
            String location = locationEditText.getText().toString().trim().toLowerCase();

            if(nationalityGroup.getCheckedRadioButtonId() == R.id.nativeRadioBtn){
                if (addSchoolToListByDemand(schoolArrayList, "Native")){
                    clonedList.clear();
                    for (School school: schoolArrayList){
                        clonedList.add(school);
                    }
                }
            }
            else if(nationalityGroup.getCheckedRadioButtonId() == R.id.nonNativeRadioBtn){
                if(addSchoolToListByDemand(schoolArrayList, "Non Native")){
                    clonedList.clear();
                    for (School school: schoolArrayList){
                        clonedList.add(school);
                    }
                }
            }
            if(jobTypeGroup.getCheckedRadioButtonId() == R.id.fullTimeRadioBtn){
                if(addSchoolToListByJobType(schoolArrayList, "Full Time")){
                    clonedList.clear();
                    for (School school: schoolArrayList){
                        clonedList.add(school);
                    }
                }
            }
            else if(jobTypeGroup.getCheckedRadioButtonId() == R.id.partTimeRadioBtn){
                if(addSchoolToListByJobType(schoolArrayList, "Part Time")){
                    clonedList.clear();
                    for (School school: schoolArrayList){
                        clonedList.add(school);
                    }
                }
            }
            if (!location.isEmpty()){
                if (addSchoolToListByLocation(schoolArrayList, location)){
                    clonedList.clear();
                    for (School school: schoolArrayList){
                        clonedList.add(school);
                    }
                }
            }

            if (!maxS.isEmpty() && !minS.isEmpty()){
                if (addSchoolToListBySalary(schoolArrayList, Integer.parseInt(minS), Integer.parseInt(maxS))){
                    clonedList.clear();
                    for (School school: schoolArrayList){
                        clonedList.add(school);
                    }
                }
            }

            //showingJobAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(new teacherShowingJobAdapter(this, clonedList));

        });

        alert.setNegativeButton("cancel", ((dialog, which) -> dialog.dismiss()));

        alert.show();
    }
    boolean addSchoolToListByDemand(ArrayList<School> schoolList, String condition){
        schoolList.clear();
        boolean i = false;
        for (School school: clonedList){
            if (school.getDemand().equals(condition)) {
                schoolList.add(school);
                i = true;
            }
        }
        return i;
    }
    boolean addSchoolToListByJobType(ArrayList<School> schoolList, String condition){
        schoolList.clear();
        boolean i = false;
        for (School school: clonedList){
            if (school.getJobTitle().equals(condition)) {
                schoolList.add(school);
                i = true;
            }
        }
        return i;
    }

    boolean addSchoolToListByLocation(ArrayList<School> schoolList, String condition){
        schoolList.clear();
        boolean i = false;
        for (School school: clonedList){
            if (school.getSchoolLocation().toLowerCase().contains(condition)) {
                schoolList.add(school);
                i = true;
            }
        }
        return i;
    }

    boolean addSchoolToListBySalary(ArrayList<School> schoolList, int min, int max){
        schoolList.clear();
        boolean i = false;
        for (School school: clonedList){
            int salary = Integer.parseInt(school.getSalary());
            if (salary >= min && salary <= max) {
                schoolList.add(school);
                i = true;
            }
        }
        return i;
    }
/*
    private void filterResult() {
        View filterView = getLayoutInflater().inflate(R.layout.pop_search_filter, null);

        TextInputLayout searchView = filterView.findViewById(R.id.search_bar);


        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setView(filterView);

        alert.setPositiveButton("SEARCH", (dialog, which) -> {

            String searchText = searchView.getEditText().getText().toString().toLowerCase();
            if (searchText.isEmpty()){
                return;
            }else {
                ArrayList<School> list = new ArrayList<>();
                for(School school : schoolArrayList){
                    if(
                            school.getDemand().toLowerCase().contains(searchText) || school.getSalary().toLowerCase().contains(searchText)
                            || school.getSchoolLocation().toLowerCase().contains(searchText) || school.getJobTitle().toLowerCase().contains(searchText)
                    )
                        list.add(school);
                }
                recyclerView.setAdapter(new teacherShowingJobAdapter(this, list));
            }
        });

        alert.setNegativeButton("cancel", ((dialog, which) -> dialog.dismiss()));

        alert.show();
    }
*/


}
