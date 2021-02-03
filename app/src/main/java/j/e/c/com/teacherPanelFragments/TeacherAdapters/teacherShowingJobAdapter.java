package j.e.c.com.teacherPanelFragments.TeacherAdapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.e.c.com.AppController;
import j.e.c.com.Models.School;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.HireFormTwoFragment;
import j.e.c.com.teacherPanelFragments.AppliedJobsFragment;
//import com.android.volley.toolbox.StringRequest;


public class teacherShowingJobAdapter extends RecyclerView.Adapter<teacherShowingJobAdapter.HolderJobs> {


    private Fragment currentFragment;
    private ArrayList<School> schoolArrayList;
    String tid;
    SQLiteHandler sqLiteHandler;
    ProgressDialog progressDialog;
    private boolean isTeacherAppplayOnJobs;

    public teacherShowingJobAdapter(Fragment fragment, ArrayList<School> schoolArrayList) {
        this.currentFragment = fragment;
        this.schoolArrayList = schoolArrayList;
    }

    @NonNull
    @Override
    public HolderJobs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentFragment.getContext()).inflate(R.layout.row_jobs, parent, false);
        fetchSchoolId();
        return new HolderJobs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderJobs holder, int position) {
        if (currentFragment instanceof AppliedJobsFragment) {
            holder.chat.setVisibility(View.GONE);
            holder.btnApplay.setVisibility(View.GONE);
        }
        School school = schoolArrayList.get(position);

       // isTeacherAlreadyApplyOnJob(school);
        //jid= school.getId();
        holder.nativ.setText("" + school.getDemand());
        holder.sallary.setText("" + school.getSalary());
        holder.time.setText("" + school.getJobTitle());
        holder.location.setText("" + school.getSchoolLocation());
      if (isTeacherAppplayOnJobs){
            holder.applied.setVisibility(View.VISIBLE);
            holder.btnApplay.setVisibility(View.GONE);
            holder.chat.setVisibility(View.GONE);
        }

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Helper.fragmentTransaction(currentFragment, new );
            }
        });
        holder.form.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "" + position, Toast.LENGTH_LONG).show();
            Helper.setSchool(schoolArrayList.get(position));
            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(currentFragment, new HireFormTwoFragment());

        });
        holder.btnApplay.setOnClickListener(v -> {
         checkValidate(school);
               // openDialoug(school);


        });
    }

    private void checkValidate(School school) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_isTeacherAlreadyApplyOnJob, response -> {

            try {

                JSONObject jObj = new JSONObject(response);
              //  Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();

                isTeacherAppplayOnJobs = jObj.getBoolean("result");
                if (isTeacherAppplayOnJobs){
                    Helper.alert("Already Applied",currentFragment.getContext());

                }else {
                    openDialoug(school);
                }
                // Toast.makeText(currentFragment.getContext(),""+isTeacherAppplayOnJobs,Toast.LENGTH_LONG).show();
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(currentFragment.getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

            }

        }, error -> {
            Toast.makeText(currentFragment.getContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("jid", school.getId());
                params.put("tid", tid);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }



    private void openDialoug(School school) {
        View view = LayoutInflater.from(currentFragment.getContext()).inflate(R.layout.row_dialoug, null);
        EditText editText;
        Button btnsend;
        editText = (EditText) view.findViewById(R.id.offeredt);
        btnsend = (Button) view.findViewById(R.id.btnsend);
        AlertDialog.Builder builder = new AlertDialog.Builder(currentFragment.getContext());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        btnsend.setOnClickListener(v -> {
            String offer = editText.getText().toString().trim();
            sendData(offer, school.getId(), school.getStid(), tid);
            dialog.dismiss();


        });


    }

    String tag_string_req = "req_login";

    private void sendData(String offer, String jid, String stid, String tid) {
//Toast.makeText(currentFragment.getContext(),""+offer+jid+stid+tid,Toast.LENGTH_LONG).show();
        progressDialog = new ProgressDialog(currentFragment.getContext());
        progressDialog.setMessage("We Are Processing On it");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_applyJobs, response -> {
            progressDialog.dismiss();
            try {
                // Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    //  Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                    View view = LayoutInflater.from(currentFragment.getContext()).inflate(R.layout.row_dialoug, null);
                    EditText editText;
                    LottieAnimationView lottieAnimationView;
                    Button btnsend;
                    TextView tv1;
                    editText = (EditText) view.findViewById(R.id.offeredt);
                    btnsend = (Button) view.findViewById(R.id.btnsend);
                    lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.congDialoug);
                    tv1 = (TextView) view.findViewById(R.id.tv1);
                    editText.setVisibility(View.GONE);
                    btnsend.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(currentFragment.getContext());
                    builder.setView(view);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(currentFragment.getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(currentFragment.getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }, error -> {
            Toast.makeText(currentFragment.getContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("tid", tid);
                params.put("sid", stid);
                params.put("jid", jid);
                params.put("Toffer", offer);
                params.put("status", "0");
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    @Override
    public int getItemCount() {
        return schoolArrayList.size();
    }

    public class HolderJobs extends RecyclerView.ViewHolder {
        @BindView(R.id.form)
        ImageView form;
        @BindView(R.id.nativ)
        TextView nativ;
        @BindView(R.id.sallary)
        TextView sallary;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.btnApplay)
        TextView btnApplay;
        @BindView(R.id.chat)
        TextView chat;

        @BindView(R.id.applied)
        LinearLayout applied;

        public HolderJobs(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void fetchSchoolId() {
        sqLiteHandler = new SQLiteHandler(currentFragment.getContext());
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            tid = res.getString(3);
///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
        }
    }

}
