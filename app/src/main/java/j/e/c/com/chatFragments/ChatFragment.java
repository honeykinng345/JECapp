package j.e.c.com.chatFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Others.Helper;
import j.e.c.com.Others.ScheduleHelper;
import j.e.c.com.Others.StaticVariables;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.chatFragments.models.Message;
import j.e.c.com.commonFragments.PaymentFragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.DOWNLOAD_SERVICE;

public class ChatFragment extends Fragment {

    ArrayList<Message> messageArrayList;
    ChatAdapter chatAdapter;
    Gson gson;
    Message msg;

    View mCustomBottomSheet;
    BottomSheetBehavior mBottomSheetBehavior;

    EditText message;
    ImageView sendBtn, otherBtn, acceptBtn, rejectBtn, reInterviewBtn, contractBtn, filledContractBtn;
    TextView interviewNotifiy, acceptNotifiy, contractNotifiy, filledContractNotifiy;
    View icons, filledContractView;
    TextView rejectText, contractText, filledContractText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.messageBar)
    CoordinatorLayout messageBar;
    private DownloadManager dm;
    private long enqueue;
    private Uri a;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        ButterKnife.bind(this, view);
        gson = new Gson();

        mCustomBottomSheet = view.findViewById(R.id.custom_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mCustomBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        return view;
    }


    private void getMessages() {
        messageArrayList.clear();
        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_getAllMessages,
                response -> {
                    try {
                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);

                        if (!obj.isNull("result")) {
                            JSONArray jsonArray = obj.getJSONArray("result");
                            //   n=jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                msg = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Message.class);
                                messageArrayList.add(msg);
                                //Toast.makeText(getActivity(),""+jba,Toast.LENGTH_LONG).show();
                                //school.setLicenecePicture("url"+school.getLicenecePicture());
                                //JSONObject sv = services.getJSONObject(i);
                                //String Demand = sv.getString("Demand");
                                //tring salary = sv.getString("salary");
                                //showingJobAdapter.notifyDataSetChanged();
                                //getting product object from json array
                                //  JSONObject categories = heroArray.getJSONObject(i);
                                //adding the product to product list
                            }
                            Collections.reverse(messageArrayList);
                            chatAdapter = new ChatAdapter(getContext(), messageArrayList);
                            recyclerView.setAdapter(chatAdapter);
                        }
                        //we have the array named hero inside the object
                        //so here we are getting that json array
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // progressDialog.dismiss();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    // progressDialog.dismiss();
                    // spinKitView.setVisibility(View.GONE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("id", Helper.fetchUserId(getContext()));
                return params;
            }

        };
        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    String tag_string_req = "req_login";

    private void SendData() {
        String m = message.getText().toString().trim();

        if (m.isEmpty()) {
            Toast.makeText(getActivity(), "Not Send Empty Message", Toast.LENGTH_LONG).show();
        }
        else
            {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    appConfig.URL_chat_message, response -> {
                // Toast.makeText(getActivity(),"ok"+response,Toast.LENGTH_LONG).show();
                try {
                    // Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                    // Check for error node in json
                    if (!error) {
                        getMessages();

                        message.setText(null);
                        // user successfully logged in
                        Toast.makeText(getActivity(), "ok", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }, error -> {
                Toast.makeText(getContext(),
                        error.toString(), Toast.LENGTH_LONG).show();

            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("id", Helper.fetchUserId(getContext()));
                    params.put("message", m);
                    params.put("admin", Helper.AdminId);
                    params.put("frm", "u");
                    return params;
                }

            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
//Toast.makeText(currentFragment.getContext(),""+offer+jid+stid+tid,Toast.LENGTH_LONG).show();


    }

    @Override
    public void onResume() {
        super.onResume();

        message = mCustomBottomSheet.findViewById(R.id.message);
        sendBtn = mCustomBottomSheet.findViewById(R.id.sendBtn);
        otherBtn = mCustomBottomSheet.findViewById(R.id.other_options);
        acceptBtn = mCustomBottomSheet.findViewById(R.id.acceptBtn);
        rejectBtn = mCustomBottomSheet.findViewById(R.id.rejectBtn);
        reInterviewBtn = mCustomBottomSheet.findViewById(R.id.reInterview);
        contractBtn = mCustomBottomSheet.findViewById(R.id.contractBtn);
        filledContractBtn = mCustomBottomSheet.findViewById(R.id.uploadFilledContractBtn);

        interviewNotifiy = mCustomBottomSheet.findViewById(R.id.reInterviewNotifiy);
        acceptNotifiy = mCustomBottomSheet.findViewById(R.id.acceptNotifiy);
        contractNotifiy = mCustomBottomSheet.findViewById(R.id.contractNotifiy);
        filledContractNotifiy = mCustomBottomSheet.findViewById(R.id.filledContractNotifiy);

        icons = mCustomBottomSheet.findViewById(R.id.icons);
        rejectText = mCustomBottomSheet.findViewById(R.id.rejectText);
        contractText = mCustomBottomSheet.findViewById(R.id.contractText);
        filledContractText = mCustomBottomSheet.findViewById(R.id.filledContractText);
        filledContractView = mCustomBottomSheet.findViewById(R.id.uploadFilledContractView);


        if (Helper.isTeacherChating)
        {

            contractBtn.setImageResource(R.drawable.ic_bsheet_download);
            contractText.setText("Download Contract");

            switch (Helper.getSchool().getStatus())
            {
                case "Teacher Rejected":
                    updateBottomSheet("You have rejected the school");
                    break;
                case "School Rejected":
                    updateBottomSheet(null);
                    break;
                case "School Interview":
                    interviewNotifiy.setVisibility(View.VISIBLE);
                    break;
                case StaticVariables.SCHOOL_ACCEPTED:
                    acceptNotifiy.setVisibility(View.VISIBLE);
                    break;
                case StaticVariables.CONTRACT_UPLOADED:
                    contractNotifiy.setVisibility(View.VISIBLE);
                    break;
            }
        }
        else
        {
            filledContractBtn.setImageResource(R.drawable.ic_bsheet_download);
            filledContractText.setText("Filled Contract");
            switch (Helper.getTeacher().getStatus())
            {
                case "Teacher Rejected":
                    updateBottomSheet(null);
                    break;
                case "School Rejected":
                    updateBottomSheet("You have rejected the teacher");
                    break;
                case StaticVariables.TEACHER_ACCEPTED:
                case StaticVariables.BOTH_ACCEPTED:
                    acceptNotifiy.setVisibility(View.VISIBLE);
                    break;
                case StaticVariables.TEACHER_REJECTED_REINTERVIEW:
                    interviewNotifiy.setVisibility(View.VISIBLE);
                    break;
                case StaticVariables.CONTRACT_FILLED_TEACHER:
                    filledContractNotifiy.setVisibility(View.VISIBLE);
                    break;
            }
        }

        sendBtn.setOnClickListener(v -> SendData());
        otherBtn.setOnClickListener(v -> {
            if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        acceptBtn.setOnClickListener(v -> {

            if (Helper.isTeacherChating)
            {
                switch (Helper.getSchool().getStatus())
                {
                    case StaticVariables.TEACHER_ACCEPTED:
                        Helper.alert("You accepted already! Plz wait for response", getContext());
                        break;
                    case StaticVariables.SCHOOL_ACCEPTED:
                        if (Helper.areYouSure(getContext(), "School is agree. Do you want to get hired?", "YES", "NO")) {
                            SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.BOTH_ACCEPTED);
                        }
                       /* else {
                            if (Helper.areYouSure(getContext(), "Are You Sure You Want To Reject This Job", "YES", "NO"))
                                SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.TEACHER_REJECTED_JOB);
                        }*/
                        break;
                    case StaticVariables.BOTH_ACCEPTED:
                        Helper.alert("Both parites are agree, wait for contract", getContext());
                        break;
                    default:
                        if (Helper.areYouSure(getContext(), "Are you agree for this job?", "YES", "NO"))
                            SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.TEACHER_ACCEPTED);
                        break;
                }
            }
            else
            {
                switch (Helper.getTeacher().getStatus())
                {
                    case StaticVariables.SCHOOL_ACCEPTED:
                        Helper.alert("You accepted already! Plz wait for response", getContext());
                        break;
                    case StaticVariables.TEACHER_ACCEPTED:
                        if (Helper.areYouSure(getContext(), "Teacher is agree. Do you want to hire him?", "YES", "NO"))
                            SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.BOTH_ACCEPTED);
                        break;
                    case StaticVariables.BOTH_ACCEPTED:
                        Helper.alert("Both parites are agreed, Plz upload contract", getContext());
                        break;
                    default:
                        if (Helper.areYouSure(getContext(), "do you want to hire him?", "YES", "NO"))
                            SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.SCHOOL_ACCEPTED);
                        break;
                }
            }
        });
        rejectBtn.setOnClickListener(v -> {
            if(Helper.areYouSure(getContext(), "Are you sure want to reject!", "YES", "NO"))
                openDialoug();
        });
        reInterviewBtn.setOnClickListener(v -> {
            if (Helper.isTeacherChating)
            {
                switch (Helper.getSchool().getStatus())
                {
                    case "School Interview":
                            if (Helper.areYouSure(getContext(), "School Wants to take Another Interview", "ACCEPT", "REJECT"))
                                SchoolAcceptTeacherAfterInterViewStatusUpdate("1");
                            else
                                SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.TEACHER_REJECTED_REINTERVIEW);
                        break;
                    default:
                        Helper.alert("Only School can take Interview again!", getContext());
                        break;
                }
            }
            else
            {
                switch (Helper.getTeacher().getStatus())
                {
                    case "School Interview":
                        Helper.alert("Scheduled sent already!", getContext());
                        break;
                    case StaticVariables.TEACHER_REJECTED_REINTERVIEW:
                        Helper.alert("Teacher don't want re-interview!", getContext());
                        break;
                    default:
                        if(Helper.areYouSure(getContext(), "Do You Want to Interview Again!", "YES", "NO")) {
                            ScheduleHelper.scheduleInterview(Helper.getTeacher(), ChatFragment.this);
                        }
                        break;
                }
            }
        });
        contractBtn.setOnClickListener(v -> {
            if (Helper.isTeacherChating)
            {
                if (Helper.getSchool().getStatus().equals(StaticVariables.CONTRACT_UPLOADED))
                {
                    Dexter.withContext(getContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            getContractFromDB(
                                    Helper.getSchool().getId(), null, Helper.getTeacher().getTid(),
                                    appConfig.URL_DownloadContract);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    }).withErrorListener(dexterError -> Helper.alert(dexterError.toString(), getContext())).check();

                }
                else
                {
                    Helper.alert("Contract Not Uploaded Yet!", getContext());
                }
            }
            else{
                if (Helper.getTeacher().getStatus().equals(StaticVariables.BOTH_ACCEPTED))
                    checkSchoolContractWithJEC(Helper.getSchool().getId(), Helper.getTeacher().getTid());
                else
                    Helper.alert("Both Parties Need to be agree", getContext());
            }
        });
        filledContractBtn.setOnClickListener(v -> {
            if (Helper.isTeacherChating) {
                Helper.getFileFromStorage(ChatFragment.this, Helper.CV_REQUEST_CODE);
            }
            else{
                if (Helper.getTeacher().getStatus().equals(StaticVariables.CONTRACT_FILLED_TEACHER)){
                    Dexter.withContext(getContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            getContractFromDB(Helper.getSchool().getId(), Helper.getSchool().getStid(),
                                    Helper.getTeacher().getTid(), appConfig.URL_DownloadFilledContract);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    }).withErrorListener(dexterError -> Helper.alert(dexterError.toString(), getContext())).check();

                }
                else Helper.alert("Filled Contract Not Uploaded Yet!", getContext());
            }
        });

        messageArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, true);
        // linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getMessages();
    }

    private void downloadContract(String url) {

       /* BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            //ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            a = Uri.parse(uriString);
                            File d = new File(a.getPath());
                            // copy file from external to internal will esaily avalible on net use google.
                            //view.setImageURI(a);
                        }
                    }
                }
            }
        };

        getActivity().registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));*/


        String fileName = "Contract" + Helper.getSchool().getStid() + System.currentTimeMillis() + ".jpg";

        dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url))
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setTitle(fileName);
        request.setDescription(fileName);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        enqueue = dm.enqueue(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri filePath = data.getData();
                String url = null;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                if (requestCode == Helper.IMAGE_REQUEST_CODE)
                    url = appConfig.URL_UploadContractForTeacher;
                else if (requestCode == Helper.CV_REQUEST_CODE)
                    url = appConfig.URL_UpoloadFilledContract;
                uploadContract(
                        Helper.getSchool().getStid(), Helper.getSchool().getId(),
                        Helper.getTeacher().getTid(), Helper.getStringImage(bitmap),
                        url);
            }catch (Exception e){
                Log.d("contractImg", e.toString());
            }
        }
    }

    private void updateBottomSheet(String text) {
        icons.setVisibility(View.GONE);
        contractBtn.setVisibility(View.GONE);
        contractText.setVisibility(View.GONE);
        rejectText.setVisibility(View.VISIBLE);
        if (text != null)
            rejectText.setText(text);
    }

    private void openDialoug() {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_dialoug, null);
            EditText editText;
            Button btnsend;
            editText = (EditText) view.findViewById(R.id.offeredt);
            editText.setHint("Please Write Reason");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(70) });
            btnsend = (Button) view.findViewById(R.id.btnsend);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
            btnsend.setOnClickListener(v -> {
                String whyReject = editText.getText().toString().trim();
                if (whyReject.isEmpty()) {
                    Toast.makeText(getActivity(), "Not Send Empty Message", Toast.LENGTH_LONG).show();
                    return;
                }
               // sendData(offer, school.getId(), school.getStid(), tid);
                SendResonForRejection(whyReject);
                dialog.dismiss();
            });
        }

    private void SchoolAcceptTeacherAfterInterViewStatusUpdate(String status) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_SchoolAcceptTeacherAfterInterViewStatusUpdate, response -> {

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                // Check for error node in json
                if (!error) {
                    onResume();
                }
            } catch (JSONException e) {
                // JSON error
                Helper.Toast(getContext(), "SchoolAcceptTeacherAfterInterViewStatusUpdate catch" + e.toString());
            }

        }, error -> {

       Helper.Toast(getContext(),"Error");

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                if (!Helper.isTeacherChating)
                    params.put("id", Helper.getTeacher().getPhone());
                else
                    params.put("id", Helper.getSchool().getContactNumber());
                params.put("status", status);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }

    void OpenBottomNav() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        //infilate view For Bottom Sheet
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_navigsation_chat, null);
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();
    }

    @OnClick({R.id.backArrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                Helper.goBackFromFragment(this);
                break;
        }
    }
    private void SendResonForRejection(String RejectionMessage) {


        if (RejectionMessage.isEmpty()) {
            Toast.makeText(getActivity(), "Not Send Empty Message", Toast.LENGTH_LONG).show();
            return;
        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    appConfig.URL_chat_message, response -> {
                // Toast.makeText(getActivity(),"ok"+response,Toast.LENGTH_LONG).show();
                try {
                    // Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                    // Check for error node in json
                    if (!error) {
                        if (Helper.isTeacherChating)
                            SchoolAcceptTeacherAfterInterViewStatusUpdate("Teacher Rejected");
                        else SchoolAcceptTeacherAfterInterViewStatusUpdate("School Rejected");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }, error -> {
                Helper.Toast(getContext(), "SendResonForRejection error" + error.toString());
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("id", Helper.fetchUserId(getContext()));
                    params.put("message", RejectionMessage);
                    params.put("admin", Helper.AdminId);
                    params.put("frm", "u");
                    return params;
                }

            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
//Toast.makeText(currentFragment.getContext(),""+offer+jid+stid+tid,Toast.LENGTH_LONG).show();


    }

    void checkSchoolContractWithJEC(String jid, String tid){
        StringRequest strReq = new StringRequest(Request.Method.POST, appConfig.URL_checkSchoolContractWithJec, response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                boolean result = jObj.getBoolean("result");
                if (result) {
                    Helper.getFileFromStorage(ChatFragment.this, Helper.IMAGE_REQUEST_CODE);
                }else {
                    if (Helper.areYouSure(getContext(), "First You need to make contract with JEC", "OK", "CANCEL"))
                        Helper.fragmentTransaction(ChatFragment.this, new PaymentFragment());
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getContext(), "checkSchoolContractWithJEC catch: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, error -> {
            Toast.makeText(getContext(),
                    "checkSchoolContractWithJEC error: " + error.toString(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("jid", jid);
                params.put("tid", tid);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    void uploadContract(String sid, String jid, String tid, String image, String apiUrl){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait..");
        //progressDialog.setMessage("Please Wait We Are Uploading Your Information");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, apiUrl, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean result = jObj.getBoolean("result");
                if (result) {
                    Helper.alert( "Contract Uploaded Successfully", getContext());
                    if (!Helper.isTeacherChating)
                        SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.CONTRACT_UPLOADED);
                    else SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.CONTRACT_FILLED_TEACHER);
                }else {
                   Helper.alert("Some error occurred!", getContext());
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getContext(), "uploadContract catch: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(),
                    "uploadContract error: " + error.toString(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("sid", sid);
                params.put("jid", jid);
                params.put("tid", tid);
                params.put("image", image);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    void getContractFromDB(String jid, String sid, String tid, String apiUrl){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait..");
        //progressDialog.setMessage("Please Wait We Are Uploading Your Information");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, apiUrl, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                String result = jObj.getString("result");
                if (result != null) {
                   downloadContract(appConfig.TEACHER_CONTRACT_IMGAES + result);
                }else {
                    Helper.alert("null", getContext());
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getContext(), "getContractFromDB catch: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(),
                    "getContractFromDB error: " + error.toString(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("jid", jid);
                if (sid != null)
                    params.put("sid", sid);
                params.put("tid", tid);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
