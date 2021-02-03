package j.e.c.com.schoolPanelFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.School;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.SchoolAdapter.schoolShowingTeachersAdapter;
import j.e.c.com.teacherPanelFragments.TeacherAdapters.teacherShowingJobAdapter;

public class schoolShowingTeachersFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<Teacher> teacherArrayList;
    private ArrayList<Teacher> clonedList;
    private Teacher teacher;
    private schoolShowingTeachersAdapter schoolShowingTeachersAdapter;
    Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.school_showing_teachers_fragment, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();
        recieveData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        teacherArrayList = new ArrayList<>();
        schoolShowingTeachersAdapter = new schoolShowingTeachersAdapter(this, teacherArrayList);
        recyclerView.setAdapter(schoolShowingTeachersAdapter);
        return view;
    }

    private void recieveData() {
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_getAllTeachers,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //  Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jba = obj.getJSONArray("result");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jba.length(); i++) {
                                teacher = gson.fromJson(String.valueOf(jba.getJSONObject(i)), Teacher.class);
                                String url = appConfig.URL_TEACHERS_IMGAES+teacher.getBpic();
                                teacher.setBpic(url);
                                //school.setLicenecePicture("url"+school.getLicenecePicture());
                                //JSONObject sv = services.getJSONObject(i);
                                //String Demand = sv.getString("Demand");
                                //tring salary = sv.getString("salary");
                                //school = new School(Sid,Sname,url);
                                teacherArrayList.add(teacher);
                                schoolShowingTeachersAdapter.notifyDataSetChanged();
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

                });
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @OnClick({R.id.backArrow, R.id.addBtn, R.id.filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.addBtn:
                Helper.fragmentTransaction(this,new HireFormTwoFragment());
                break;
            case R.id.filter:
                filterResult();
                break;
        }
    }

    private void filterResult() {

        clonedList = new ArrayList<>();
        for (Teacher teacher : teacherArrayList){
            clonedList.add(teacher);
        }

        View filterView = getLayoutInflater().inflate(R.layout.pop_search_filter, null);

        RelativeLayout genderLayout = filterView.findViewById(R.id.genderLayout);
        genderLayout.setVisibility(View.VISIBLE);

        TextView clearAll = filterView.findViewById(R.id.clearAll);
        RadioGroup nationalityGroup = filterView.findViewById(R.id.nationalityRadioGroup);
        RadioGroup jobTypeGroup = filterView.findViewById(R.id.jobTypeRadioGroup);
        RadioGroup genderGroup = filterView.findViewById(R.id.genderRadioGroup);
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

            ArrayList<Teacher> teachers = new ArrayList<>();

            String minS = minSalary.getText().toString().trim();
            String maxS = maxSalary.getText().toString().trim();
            String location = locationEditText.getText().toString().trim().toLowerCase();

            if(nationalityGroup.getCheckedRadioButtonId() == R.id.nativeRadioBtn){
                if (addTeacherToListByDemand(teachers, "Native")){
                    doFurther(teachers);
                }
            }
            else if(nationalityGroup.getCheckedRadioButtonId() == R.id.nonNativeRadioBtn){
                if(addTeacherToListByDemand(teachers, "Non Native")){
                    doFurther(teachers);
                }
            }
            if(jobTypeGroup.getCheckedRadioButtonId() == R.id.fullTimeRadioBtn){
                if(addTeacherToListByJobType(teachers, "Full Time")){
                    doFurther(teachers);
                }
            }
            else if(jobTypeGroup.getCheckedRadioButtonId() == R.id.partTimeRadioBtn){
                if(addTeacherToListByJobType(teachers, "Part Time")){
                    doFurther(teachers);
                }
            }
            if(genderGroup.getCheckedRadioButtonId() == R.id.maleRadioBtn){
                if(addTeacherToListByGender(teachers, "Boy")){
                    doFurther(teachers);
                }
            }
            else if(genderGroup.getCheckedRadioButtonId() == R.id.FemaleRadioBtn){
                if(addTeacherToListByGender(teachers, "Girl")){
                    doFurther(teachers);
                }
            }
            if (!location.isEmpty()){
                if (addTeacherToListByLocation(teachers, location)){
                    doFurther(teachers);
                }
            }

            if (!maxS.isEmpty() && !minS.isEmpty()){
                if (addTeacherToListBySalary(teachers, Integer.parseInt(minS), Integer.parseInt(maxS))){
                    doFurther(teachers);
                }
            }

            //showingJobAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(new schoolShowingTeachersAdapter(this, clonedList));

        });

        alert.setNegativeButton("cancel", ((dialog, which) -> dialog.dismiss()));

        alert.show();
    }


    boolean addTeacherToListByDemand(ArrayList<Teacher> teacherList, String condition){
        teacherList.clear();
        boolean i = false;
        for (Teacher teacher: clonedList){
            if (teacher.getNationality().equals(condition)) {
                teacherList.add(teacher);
                i = true;
            }
        }
        return i;
    }
    boolean addTeacherToListByJobType(ArrayList<Teacher> teacherList, String condition){
        teacherList.clear();
        boolean i = false;
        for (Teacher teacher: clonedList){
            if (teacher.getJobtitle().equals(condition)) {
                teacherList.add(teacher);
                i = true;
            }
        }
        return i;
    }

    boolean addTeacherToListByGender(ArrayList<Teacher> teacherList, String condition){
        teacherList.clear();
        boolean i = false;
        for (Teacher teacher: clonedList){
            if (teacher.getGender().equals(condition)) {
                teacherList.add(teacher);
                i = true;
            }
        }
        return i;
    }

    boolean addTeacherToListByLocation(ArrayList<Teacher> teacherList, String condition){
        teacherList.clear();
        boolean i = false;
        for (Teacher teacher: clonedList){
            if (teacher.getCountry().toLowerCase().contains(condition)) {
                teacherList.add(teacher);
                i = true;
            }
        }
        return i;
    }

    boolean addTeacherToListBySalary(ArrayList<Teacher> teacherList, int min, int max){
        teacherList.clear();
        boolean i = false;
        for (Teacher teacher: clonedList){
            int salary = Integer.parseInt(teacher.getSalary());
            if (salary >= min && salary <= max) {
                teacherList.add(teacher);
                i = true;
            }
        }
        return i;
    }

    void doFurther(ArrayList<Teacher> teachers){
        clonedList.clear();
        for (Teacher teacher: teachers){
            clonedList.add(teacher);
        }
    }
}
