package j.e.c.com.teacherPanelFragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
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
import j.e.c.com.Others.ImageResizer;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;

import static android.app.Activity.RESULT_OK;

public class TeacherApplyFragment extends Fragment {

    @BindView(R.id.imageError)
    TextView imageError;
    @BindView(R.id.CVpicture)
    ImageView CVpicture;
    @BindView(R.id.snackbar_action)
    RelativeLayout snackbarAction;
    @BindView(R.id.beautyPic)
    Button beautyPic;
    @BindView(R.id.picture1)
    ImageView picture1;
    String filePath;
    @BindView(R.id.video)
    ImageView video;

    private Bitmap bitmap;
    String test;
    boolean seflToogle = false;
    @BindView(R.id.agentSpinner)
    AppCompatAutoCompleteTextView agentSpinner;
    @BindView(R.id.contact)
    TextInputLayout contact;
    @BindView(R.id.wechat)
    TextInputLayout wechat;
    @BindView(R.id.camera)
    ImageView camera;
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.uploadCVbtn)
    Button uploadCVbtn;
    @BindView(R.id.uploadVideoBtn)
    Button uploadVideoBtn;
    @BindView(R.id.fillBtn)
    Button fillBtn;
    @BindView(R.id.self)
    TextView self;
    @BindView(R.id.agentSpinerParent)
    TextInputLayout agentSpinerParent;

    Teacher teacher = Helper.getTeacher();
    Teacher2 t2 = Helper.getTeacher2();
    @BindView(R.id.cvTextView)
    TextView cvTextView;
    @BindView(R.id.videoTextView)
    TextView videoTextView;
    ProgressDialog progressDialog;
    public static int VIDEO_REQUEST_CODE = 200;
    SQLiteHandler sqLiteHandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_apply, container, false);
        ButterKnife.bind(this, view);
        sqLiteHandler = new SQLiteHandler(getContext());
        fetchTeacherId();
        // fillAll();
        return view;
    }

    private void fetchTeacherId() {
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            teacher.setTid(res.getString(3));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSpinners();
    }

    void updateSpinners() {
        //agent spinner
        agentSpinner.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.agentArray, getContext()));
    }

    /*@OnClick(R.id.fillBtn)
    public void onViewClicked() {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        // transaction.replace(R.id.fragment_container, new JobFormFragment()).addToBackStack(null).commit();
    }*/

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.backArrow, R.id.self, R.id.fillBtn, R.id.camera, R.id.uploadVideoBtn, R.id.uploadCVbtn, R.id.beautyPic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.self:
                if (!seflToogle) {
                    self.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_react01));
                    self.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    agentSpinner.setError(null);
                    seflToogle = !seflToogle;
                    agentSpinerParent.setEnabled(false);
                } else {
                    self.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_rect02));
                    self.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    agentSpinerParent.setEnabled(true);
                    seflToogle = !seflToogle;
                }
                break;
            case R.id.fillBtn:
                FeildVelidation();
                break;
            case R.id.camera:
                Helper.openCamera(this, Helper.IMAGE_REQUEST_CODE);
                break;
            case R.id.uploadVideoBtn:
               /* new MaterialFilePicker()
                        .withActivity(ge)
                        .withRequestCode(10)
                        .start();*/
                Helper.getFileFromStorage(this, Helper.VIDEO_REQUEST_CODE);
                break;
            case R.id.uploadCVbtn:
                Helper.getFileFromStorage(this, Helper.CV_REQUEST_CODE);
                break;
            case R.id.beautyPic:
                Helper.getFileFromStorage(this, Helper.BP_REQUEST_CODE);
                break;
        }
    }

    public void FeildVelidation() {
        // teacher.setAgent(agentSpinner.getText().toString().trim());
        if (!seflToogle) {
            if (TextUtils.isEmpty(agentSpinner.getText().toString().trim())) {
                agentSpinner.setError("");
                return;
            } else {
                teacher.setAgent(agentSpinner.getText().toString().trim());

            }
        } else {
            teacher.setAgent("Self");
        }
        // teacher.setAgent(agentSpinner.getText().toString().trim());
        if (TextUtils.isEmpty(contact.getEditText().getText().toString().trim())) {
            contact.setError(" ");
            return;
        } else {
            teacher.setPhone(contact.getEditText().getText().toString().trim());
        }
        if (TextUtils.isEmpty(wechat.getEditText().getText().toString().trim())) {
            wechat.setError(" ");
            return;
        }
        if (TextUtils.isEmpty(teacher.getPic())) {
            imageError.setVisibility(View.VISIBLE);
            return;

        } else {
            imageError.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(teacher.getCvpic())) {
            cvTextView.setVisibility(View.VISIBLE);
            return;

        } else {
            cvTextView.setVisibility(View.GONE);
        }
        teacher.setPhone(contact.getEditText().getText().toString().trim());
        teacher.setWechatid(wechat.getEditText().getText().toString().trim());
        teacher.setStatus("0");
        //Toast.makeText(getContext(), "" + teacher.getAgent(), Toast.LENGTH_LONG).show();
        // Tag used to cancel the request
        // SendData();
        Helper.isTeacherComeFromAdapter = false;
        Helper.fragmentTransaction(this, new JobFormFragment(), false);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case Helper.IMAGE_REQUEST_CODE:
                    try {
                        //Uri fiePath = data.getData();
                        //picture.setImageURI(fiePath);
                        bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                        // SiliCompressor.with(getContext()).getCompressBitmap(imageUriString);
                        picture.setImageBitmap(bitmap);
                        picture.setVisibility(View.VISIBLE);
                        Bitmap imageResize = ImageResizer.reduceBitmapSize(bitmap, 200000);
                        teacher.setPic(Helper.getStringImage(imageResize));
                        // teacher.setPic("oo");
                    } catch (Exception e) {
                        Helper.ShowSnackBar(snackbarAction, e.getMessage());
                    }
                    break;
                case Helper.VIDEO_REQUEST_CODE:
                    try {
                        Uri video1 = data.getData();
                        /*video.setVideoURI(video1);
                        video.setVisibility(View.VISIBLE);*/
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContext().getContentResolver().query(video1, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MICRO_KIND);

                        video.setImageBitmap(bitmap);


                        /* Uri videoPath = data.getData();*/
                        /*    uploadFileToFireBase(data.getData());*/
                        teacher.setVideo(data.getData().toString());

                        /*video.setVisibility(View.VISIBLE);
                        video.setVideoURI(videoPath);*/





                      /*  InputStream inputStream = getContext().getContentResolver().openInputStream(videoPath);

                        byte[] videofInBytes = new byte[inputStream.available()];
                        inputStream.read(videofInBytes);
                        t2.setVideo(Base64.encodeToString(videofInBytes, Base64.DEFAULT));

                       */
                    } catch (Exception e) {
                        Helper.ShowSnackBar(snackbarAction, e.getMessage());
                    }
                    break;
                case Helper.CV_REQUEST_CODE:
                    try {
                        Uri pdfPath = data.getData();
                        CVpicture.setImageURI(pdfPath);
                        CVpicture.setVisibility(View.VISIBLE);



                       /* InputStream inputStream = getContext().getContentResolver().openInputStream(pdfPath);
                        bitmap = BitmapFactory.decodeStream(inputStream);


                        Bitmap imageResize = ImageResizer.reduceBitmapSize(bitmap,200000);*/
                        teacher.setCvpic(Helper.getStringImage(Helper.getBitmapFromUri(pdfPath, getContext())));
                        //  Toast.makeText(getContext(),""+teacher.getCvpic().length(),Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Helper.ShowSnackBar(snackbarAction, e.getMessage());
                    }
                    break;
                case Helper.BP_REQUEST_CODE:
                    try {
                        Uri bp = data.getData();
                        picture1.setImageURI(bp);
                        picture1.setVisibility(View.VISIBLE);
                        InputStream inputStream = getContext().getContentResolver().openInputStream(bp);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        // CVpicture.setImageBitmap(bitmap);
                        Bitmap imageResize = ImageResizer.reduceBitmapSize(bitmap, 200000);
                        teacher.setBpic(Helper.getStringImage(imageResize));
                        // teacher.setBpic("ss");
                        //   Toast.makeText(getContext(),""+teacher.getFileEncodeUrl(),Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Helper.ShowSnackBar(snackbarAction, e.getMessage());
                    }
                    break;
            }
        }


    }


    public void fillAll() {
        Helper.fill(agentSpinner);
        Helper.fill(contact);
        Helper.fill(wechat);
    }

    private void SendData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Please Wait We Are Uploading Your Data");
        progressDialog.show();
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_TeacherData, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                Toast.makeText(getContext(), "" + response, Toast.LENGTH_LONG).show();
                boolean error = jObj.getBoolean("error");
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    progressDialog.dismiss();
                    // Now store the user in sqlite
                    String uid = jObj.getString("uid");
                    //  teacher.setUniqueId(uid);
                    Helper.isTeacherComeFromAdapter = false;
                    Helper.fragmentTransaction(this, new JobFormFragment(), false);


                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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



