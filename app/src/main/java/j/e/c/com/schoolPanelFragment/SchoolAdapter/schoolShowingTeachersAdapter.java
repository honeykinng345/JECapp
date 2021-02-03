package j.e.c.com.schoolPanelFragment.SchoolAdapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.AppController;
import j.e.c.com.Models.School;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.commonFragments.PlayVideoFragment;
import j.e.c.com.teacherPanelFragments.JobFormFragment;

public class schoolShowingTeachersAdapter extends RecyclerView.Adapter<schoolShowingTeachersAdapter.ViewHolder> {
    Fragment context;

    private ArrayList<School> schoolArrayList;
    private SchoolShowingJobsDialougAdapter jobsDialougAdapter;
    ArrayList<Teacher> teacherArrayList;
    private School school;
    Gson gson;

    public schoolShowingTeachersAdapter(Fragment context, ArrayList<Teacher> teacherArrayList) {
        this.context = context;
        this.teacherArrayList = teacherArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_applied_teachers, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Teacher teacher = teacherArrayList.get(position);
        holder.location.setText("" + teacher.getCountry());
        try {
            Picasso.get().load(teacher.getBpic()).fit().into(holder.image);

        } catch (Exception e) {
            Picasso.get().load(R.drawable.khabib).into(holder.image);
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context.getContext());

                View paymentItem = context.getLayoutInflater().inflate(R.layout.payment_item, null);

                ImageView imageView = paymentItem.findViewById(R.id.image);
                Picasso.get().load(teacher.getBpic()).into(imageView);
                alert.setView(paymentItem);

                alert.setPositiveButton("OK", (dialog, whichButton) -> {
                    //What ever you want to do with the value
                });

                alert.show();
            }
        });
        holder.form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Helper.setTeacher(teacherArrayList.get(position));
                Helper.isTeacherComeFromAdapter = true;
                Helper.fragmentTransaction(context,new JobFormFragment());
            }
        });

        holder.gender.setText("" + teacher.getGender());
        holder.salary.setText("" + teacher.getSalary());
        holder.fullTime.setText("" + teacher.getJobtitle());
        holder.nativ.setText("" + teacher.getJobtitle());
        holder.videoIcon.setOnClickListener(v -> {
            Helper.setTeacher(teacherArrayList.get(position));
            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(context, new PlayVideoFragment());

        });
        holder.hireBtn.setOnClickListener(v -> {
            Helper.setTeacher(teacherArrayList.get(position));
            OpenDialoug();
        });
    }

    private void OpenDialoug() {
        gson = new Gson();
        RecyclerView recyclerViewd;
        schoolArrayList = new ArrayList<>();
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.dialoug_showing_school_own_jobs, null);
        recyclerViewd = view.findViewById(R.id.drecyelerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewd.setLayoutManager(linearLayoutManager);

        recyclerViewd.setAdapter(jobsDialougAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getContext());
        builder.setView(view);
        AlertDialog dialog = builder.show();
        jobsDialougAdapter = new SchoolShowingJobsDialougAdapter(context,schoolArrayList, dialog);

        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_getAllSchoolUploadJobs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                          //  Toast.makeText(context.getContext(),""+response,Toast.LENGTH_LONG).show();
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jba = obj.getJSONArray("result");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jba.length(); i++) {
                                school = gson.fromJson(String.valueOf(jba.getJSONObject(i)), School.class);
                                //school.setLicenecePicture("url"+school.getLicenecePicture());
                                //JSONObject sv = services.getJSONObject(i);
                                //String Demand = sv.getString("Demand");
                                //tring salary = sv.getString("salary");
                                //school = new School(Sid,Sname,url);
                                schoolArrayList.add(school);
                                //jobsDialougAdapter.notifyDataSetChanged();
                                //getting product object from json array
                                //  JSONObject categories = heroArray.getJSONObject(i);
                                //adding the product to product list
                            }
                            recyclerViewd.setAdapter(new SchoolShowingJobsDialougAdapter(context, schoolArrayList, dialog));
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context.getContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        // spinKitView.setVisibility(View.GONE);
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("sid", Helper.fetchUserId(context.getContext()));
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);


        //AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return teacherArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.form)
        LinearLayout form;
        @BindView(R.id.image)
        CircularImageView image;
        @BindView(R.id.videoIcon)
        ImageView videoIcon;
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
        @BindView(R.id.hireBtn)
        Button hireBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
