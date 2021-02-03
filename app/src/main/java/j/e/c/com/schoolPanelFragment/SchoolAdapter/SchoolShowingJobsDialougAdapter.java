package j.e.c.com.schoolPanelFragment.SchoolAdapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.HireFormTwoFragment;

public class SchoolShowingJobsDialougAdapter extends RecyclerView.Adapter<SchoolShowingJobsDialougAdapter.ViewHolder> {
    Fragment context;

    private ArrayList<School> schoolArrayList;
    androidx.appcompat.app.AlertDialog dialog;

    public SchoolShowingJobsDialougAdapter(Fragment context, ArrayList<School> schoolArrayList, androidx.appcompat.app.AlertDialog dialog) {
        this.context = context;
        this.schoolArrayList = schoolArrayList;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.item_jobs_showing_to_school_when_applied_teacher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        School school = schoolArrayList.get(i);
        //jid= school.getId();
        holder.nativ.setText("" + school.getCity());
        holder.sallary.setText("" + school.getSalary());
        holder.time.setText("" + school.getJobTitle());
        holder.location.setText("" + school.getSchoolLocation());
        String check = school.getStatus();
        if (check.equals("0")) {
            holder.radioBtn.setVisibility(View.GONE);
            holder.Inactive.setVisibility(View.VISIBLE);
        }
        holder.form.setOnClickListener(v -> {
            // Toast.makeText(v.getContext(), ""+pos, Toast.LENGTH_LONG).show();
            Helper.setSchool(schoolArrayList.get(i));
            Helper.isTeacherComeFromAdapter = true;
            Helper.fragmentTransaction(context, new HireFormTwoFragment());
            dialog.dismiss();
        });
        holder.radioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context.getContext());
                alert.setMessage("Are You Sure You Want To Proceed");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendData(school, Helper.getTeacher());
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return schoolArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.radioBtn)
        RadioButton radioBtn;
        @BindView(R.id.Inactive)
        TextView Inactive;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    void SendData(School school, Teacher teacher) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.row_dialoug, null);
        EditText editText;
        Button btnsend;
        editText = (EditText) view.findViewById(R.id.offeredt);
        btnsend = (Button) view.findViewById(R.id.btnsend);
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getContext());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        btnsend.setOnClickListener(v -> {
            String offer = editText.getText().toString().trim();
            sendData(offer, school.getId(), school.getStid(), teacher.getTid());
            dialog.dismiss();


        });


    }

    String tag_string_req = "req_login";

    private void sendData(String offer, String jid, String stid, String tid) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context.getContext());
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
                    View view = LayoutInflater.from(context.getContext()).inflate(R.layout.row_dialoug, null);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(context.getContext());
                    builder.setView(view);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(context.getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(context.getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }, error -> {
            Toast.makeText(context.getContext(),
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
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}
