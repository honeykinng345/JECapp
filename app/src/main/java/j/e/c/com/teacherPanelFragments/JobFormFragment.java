package j.e.c.com.teacherPanelFragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Models.Teacher2;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.commonFragments.WelcomeFragment;

//import com.android.volley.toolbox.StringRequest;


public class JobFormFragment extends Fragment {

    Teacher2 t2 = Helper.getTeacher2();
    Teacher teacher = Helper.getTeacher();
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    private ProgressDialog progressDialog;

    @BindView(R.id.nationality)
    AppCompatAutoCompleteTextView nationality;
    @BindView(R.id.workPlace)
    AppCompatAutoCompleteTextView workPlace;
    @BindView(R.id.visa)
    AppCompatAutoCompleteTextView visa;
    @BindView(R.id.visaType)
    AppCompatAutoCompleteTextView visaType;
    @BindView(R.id.visaTypeParent)
    TextInputLayout visaTypeParent;
    @BindView(R.id.gender)
    AppCompatAutoCompleteTextView gender;
    @BindView(R.id.job)
    AppCompatAutoCompleteTextView job;
    @BindView(R.id.beenChina)
    AppCompatAutoCompleteTextView beenChina;
    @BindView(R.id.experience)
    AppCompatAutoCompleteTextView experience;
    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.toolBar)
    LinearLayout toolBar;
    @BindView(R.id.name)
    TextInputLayout name;
    @BindView(R.id.salary)
    TextInputLayout salary;
    @BindView(R.id.education)
    TextInputLayout education;
    @BindView(R.id.graduation)
    TextInputLayout graduation;
    @BindView(R.id.yearsInChina)
    AppCompatAutoCompleteTextView yearsInChina;
    @BindView(R.id.yearsInChinaparent)
    TextInputLayout yearsInChinaparent;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    @BindView(R.id.age)
    TextInputLayout age;
    @BindView(R.id.whenCanJoin)
    TextInputLayout whenCanJoin;
    @BindView(R.id.visaQualified)
    AppCompatAutoCompleteTextView visaQualified;
    @BindView(R.id.countrySpinner)
    CountryCodePicker countrySpinner;
    @BindView(R.id.residence)
    TextInputLayout residence;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_form, container, false);
        //  fillAll();
        progressDialog = new ProgressDialog(getContext());
        ButterKnife.bind(this, view);
        teacher = Helper.getTeacher();

        if (Helper.isTeacherComeFromAdapter) {
            checkbox.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            fillForm(teacher);
        }
        return view;
    }

    private void fillForm(Teacher teacher) {


        name.getEditText().setText(teacher.getName());
        age.getEditText().setText(teacher.getAge());
        nationality.setText(teacher.getNationality());
        countrySpinner.setCountryForNameCode(teacher.getCountry());
        gender.setText(teacher.getGender());
        salary.getEditText().setText(teacher.getSalary());
        education.getEditText().setText(teacher.getEducation());
        graduation.getEditText().setText(teacher.getGraduation());
        job.setText(teacher.getJobtitle());
        visa.setText(teacher.getVisa());
        visaType.setText(teacher.getVisatype());
        visaQualified.setText(teacher.getQFWV());
        workPlace.setText(teacher.getWorkplace());
        beenChina.setText(teacher.getFromchina());
        yearsInChina.setText(teacher.getYearinchina());
        experience.setText(teacher.getWorkexperince());
        whenCanJoin.getEditText().setText(teacher.getJoindate());
        residence.getEditText().setText(teacher.getCurrentworkplace());



        name.getEditText().setEnabled(false);
        age.getEditText().setEnabled(false);
        nationality.setEnabled(false);
        countrySpinner.setEnabled(false);
        gender.setEnabled(false);
        salary.getEditText().setEnabled(false);
        education.getEditText().setEnabled(false);
        graduation.getEditText().setEnabled(false);
        job.setEnabled(false);
        visaType.setEnabled(false);
        visa.setEnabled(false);
        visaQualified.setEnabled(false);
        workPlace.setEnabled(false);
        beenChina.setEnabled(false);
        yearsInChina.setEnabled(false);
        experience.setEnabled(false);
        whenCanJoin.getEditText().setEnabled(false);
        residence.getEditText().setEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();

        nationality.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.nationalityArray, getContext()));
        workPlace.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.chineseCities, getContext()));
        gender.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.genderArray, getContext()));
        job.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.jobArray, getContext()));
        beenChina.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.yesNoArray, getContext()));
        yearsInChina.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.yearsArray, getContext()));
        experience.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.yearsArray, getContext()));
        visaType.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.typeOfVisa, getContext()));
        visaQualified.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.yesNoArray, getContext()));
        visa.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.visa, getContext()));
        visa.setOnItemClickListener((parent, view, position, id) -> {
            //Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
            if (position == 0) {
                visaTypeParent.setVisibility(View.VISIBLE);
            } else {
                visaTypeParent.setVisibility(View.GONE);
            }
        });

        beenChina.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                yearsInChinaparent.setVisibility(View.VISIBLE);
            } else {
                yearsInChinaparent.setVisibility(View.GONE);
            }
        });

        String pattern = "dd-MM-yy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        Objects.requireNonNull(whenCanJoin.getEditText()).setText(simpleDateFormat.format(date));
    }

    @OnClick({R.id.backArrow, R.id.submitBtn, R.id.whenCanJoin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
                break;
            case R.id.submitBtn:

                if (validateFields()) {
                    teacher.setName(name.getEditText().getText().toString());
                    teacher.setAge(age.getEditText().getText().toString());
                    teacher.setNationality(nationality.getText().toString());
                    teacher.setCountry(countrySpinner.getSelectedCountryName().toString());
                    teacher.setGender(gender.getText().toString());
                    teacher.setSalary(salary.getEditText().getText().toString());
                    teacher.setEducation(education.getEditText().getText().toString());
                    teacher.setGraduation(graduation.getEditText().getText().toString());
                    teacher.setJobtitle(job.getText().toString());
                    teacher.setVisa(visa.getText().toString());
                    if (TextUtils.equals(visa.getText().toString(), "Have")) {
                        teacher.setVisatype(visaType.getText().toString());
                    } else {
                        teacher.setVisatype(" No Visa");

                    }
                    teacher.setQFWV(visaQualified.getText().toString());
                    teacher.setWorkplace(workPlace.getText().toString());
                    teacher.setFromchina(beenChina.getText().toString());
                    if (TextUtils.equals(beenChina.getText().toString(), "Yes")) {
                        teacher.setYearinchina(yearsInChina.getText().toString());
                    } else {
                        teacher.setYearinchina("NO Years");
                    }
                    teacher.setWorkexperince(experience.getText().toString());
                    teacher.setJoindate(whenCanJoin.getEditText().getText().toString());
                    teacher.setCurrentworkplace(residence.getEditText().getText().toString());
                    SendData();
                }
                break;
            case R.id.whenCanJoin:
                Helper.setDate(whenCanJoin);
                break;
        }

    }

    private void SendData() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Please Wait We Are Uploading Your Data");
        progressDialog.show();

        uploadFileToFireBase(Uri.parse(teacher.getVideo()));
        String tag_string_req = "req_login";
      /*  StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_t2, response -> {
            progressDialog.dismiss();

            Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
           // Log.d("Test", ""+response);
           //Helper.fragmentTransaction(this,new WelcomeFragment());
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Test", Objects.requireNonNull(error.getMessage()));
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("tuid", teacher.getUniqueId());
                params.put("name", t2.getName());
                params.put("age",t2.getAge());
                params.put("nationality", t2.getNationality());
                 params.put("country", t2.getCountry());
                 params.put("gender", t2.getGender());
                 params.put("salary", t2.getSalary());
                 params.put("education", t2.getEducation());
                 params.put("graduation", t2.getGraduation());
                 params.put("jobtitle", t2.getJobtitle());
                 params.put("visa", t2.getVisa());
                 params.put("visatype", t2.getVisatype());
                 params.put("QFWV", t2.getQFWV());
                 params.put("workplace", t2.getWorkplace());
                 params.put("fromchina", t2.getFromchina());
                 params.put("yearinchina", t2.getYearinchina());
                 params.put("workexperince", t2.getWorkexperince());
                 params.put("joindate", t2.getJoindate());
                 params.put("currentworkplace", t2.getCurrentworkplace());
                 params.put("video", t2.getVideo());
                 return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);*/
    }

    boolean validateFields() {
        boolean b = true;
        if (!Helper.validateField(name))
            b = false;
        if (!Helper.validateField(age))
            b = false;
        if (!Helper.validateField(nationality))
            b = false;
        if (!Helper.validateField(gender))
            b = false;
        if (!Helper.validateField(salary))
            b = false;
        if (!Helper.validateField(education))
            b = false;
        if (!Helper.validateField(graduation))
            b = false;
        if (!Helper.validateField(job))
            b = false;
        if (!Helper.validateField(visa))
            b = false;
        else if (TextUtils.equals(visa.getText().toString(), "Have")) {
            if (!Helper.validateField(visaType))
                b = false;
        }
        if (!Helper.validateField(visaQualified))
            b = false;
        if (!Helper.validateField(workPlace))
            b = false;
        if (!Helper.validateField(beenChina))
            b = false;
        else if (TextUtils.equals(beenChina.getText().toString(), "Yes")) {
            if (!Helper.validateField(yearsInChina))
                b = false;
        }
        if (!Helper.validateField(experience))
            b = false;
        if (!Helper.validateField(whenCanJoin))
            b = false;
        if (!Helper.validateField(residence))
            b = false;

        return b;
    }

    public void fillAll() {
        Helper.fill(name);
        Helper.fill(age);
        Helper.fill(nationality);

        Helper.fill(gender);
        Helper.fill(salary);
        Helper.fill(education);
        Helper.fill(graduation);
        Helper.fill(job);
        Helper.fill(visa);
        Helper.fill(visaType);
        Helper.fill(visaQualified);
        Helper.fill(workPlace);
        Helper.fill(beenChina);
        Helper.fill(experience);
        Helper.fill(residence);


    }

    private void uploadFileToFireBase(Uri data) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Video...");
        pd.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        StorageReference fileReference = storageReference.child("teacherID")
                .child(System.currentTimeMillis() + "." + Helper.getFileExtension(getActivity(), data));

        fileReference.putFile(data).addOnSuccessListener(taskSnapshot ->
                Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            //addRoomToDB(new Room(id, beds, wifi, rent, status, uri.toString()));
                            //saving video url
                            //Prefrence.saveVideoLink(uri.toString(), getContext());

                            teacher.setVideo(uri.toString());
                            sendApplicationData();
                            pd.dismiss();
                            //pd.dismiss();
                            //Helper.Toast(getContext(), uri.toString());
                        }))
                .addOnFailureListener(e -> Helper.Toast(getContext(), e.toString()))
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    pd.setMessage("Uploading " + (int) progress + "%");
                    //Helper.Toast(getContext(), "Uploading... "+ progress + "%");
                });
    }

    private void sendApplicationData() {

        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Please Wait We Are Uploading Your Data");
        progressDialog.show();
        String tag_string_req = "req_login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_TeacherData, response -> {


            //progressDialog.dismiss();


            try {


                JSONObject jObj = new JSONObject(response);
               // Toast.makeText(getContext(), "" + response, Toast.LENGTH_LONG).show();
                boolean error = jObj.getBoolean("error");

               Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    progressDialog.dismiss();
                   // Toast.makeText(getContext(), "" + response, Toast.LENGTH_LONG).show();
                    Helper.fragmentTransaction(this,new WelcomeFragment(), false);

                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }


            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }


            // Helper.fragmentTransaction(this, new JobFormFragment());

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Test", Objects.requireNonNull(error.getMessage()));
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("agent", teacher.getAgent());
                params.put("phone", teacher.getPhone());
                params.put("wechatid", teacher.getWechatid());
                params.put("image", teacher.getPic());
                params.put("bpic", teacher.getBpic());
                params.put("file", teacher.getCvpic());
                params.put("status", teacher.getStatus());
                params.put("tid", teacher.getTid());
                params.put("video", teacher.getVideo());
                params.put("name", teacher.getName());
                params.put("age", teacher.getAge());
                params.put("nationality", teacher.getNationality());
                params.put("country", teacher.getCountry());
                params.put("gender", teacher.getGender());
                params.put("salary", teacher.getSalary());
                params.put("education", teacher.getEducation());
                params.put("graduation", teacher.getGraduation());
                params.put("jobtitle", teacher.getJobtitle());
                params.put("visa", teacher.getVisa());
                params.put("visatype", teacher.getVisatype());
                params.put("QFWV", teacher.getQFWV());
                params.put("workplace", teacher.getWorkplace());
                params.put("fromchina", teacher.getFromchina());
                params.put("yearinchina", teacher.getYearinchina());
                params.put("workexperince", teacher.getWorkexperince());
                params.put("joindate", teacher.getJoindate());
                params.put("currentworkplace", teacher.getCurrentworkplace());

               /* params.put("jobtitle", teacher.getJobtitle());
                params.put("visa", teacher.getVisa());
                params.put("visatype", teacher.getVisatype());
                params.put("QFWV", teacher.getQFWV());
                params.put("workplace", teacher.getWorkplace());
                params.put("fromchina", teacher.getFromchina());
                params.put("yearinchina", teacher.getYearinchina());
                params.put("workexperince", teacher.getWorkexperince());
                params.put("joindate", teacher.getJoindate());
                params.put("currentworkplace", teacher.getCurrentworkplace());
                params.put("video", teacher.getVideo());*/
                //  params.put("name", teacher.getName());


/*     params.put("file", teacher.getFile());
                params.put("video", teacher.getVideo());
                params.put("name", teacher.getName());
                params.put("age", teacher.getAge());
                params.put("nationality", teacher.getNationality());
                params.put("country", teacher.getCountry());
                params.put("gender", teacher.getGender());
                params.put("salary", teacher.getSalary());
                params.put("education", teacher.getEducation());
                params.put("gradution", teacher.getGraduation());
                params.put("jobTitle", teacher.getJobtitile());
                params.put("visa", teacher.getVisa());
                params.put("visaType", teacher.getVisatype());
                params.put("ValidateForVisa", teacher.getValidateforvisa());
                params.put("CurrentWorkPlace", teacher.getCurrentworkplace());
                params.put("FromChina", teacher.getFromchina());
                params.put("DurationInChina", teacher.getDurationinchina());
                params.put("workExperince", teacher.getWorkexperience());
                params.put("FutherJoinDate", teacher.getFutherjoindate());
                params.put("currentResidence", teacher.getCurrentresidence());
                params.put("status", teacher.getStatus());
            */
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
}
