package j.e.c.com.chatFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
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

public class ChatFragment extends Fragment {

    ArrayList<Message> messageArrayList;
    ChatAdapter chatAdapter;
    Gson gson;
    Message msg;

    View mCustomBottomSheet;
    BottomSheetBehavior mBottomSheetBehavior;

    EditText message;
    ImageView sendBtn, otherBtn, acceptBtn, rejectBtn, reInterviewBtn, contractBtn;
    TextView interviewNotifiy, acceptNotifiy;
    View icons;
    TextView rejectText, contractText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.messageBar)
    CoordinatorLayout messageBar;


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

        interviewNotifiy = mCustomBottomSheet.findViewById(R.id.reInterviewNotifiy);
        acceptNotifiy = mCustomBottomSheet.findViewById(R.id.acceptNotifiy);

        icons = mCustomBottomSheet.findViewById(R.id.icons);
        rejectText = mCustomBottomSheet.findViewById(R.id.rejectText);
        contractText = mCustomBottomSheet.findViewById(R.id.contractText);

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
                case StaticVariables.BOTH_ACCEPTED:
                    acceptNotifiy.setVisibility(View.VISIBLE);
                    break;
            }
        }
        else
        {
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
                case "t not agree":
                    interviewNotifiy.setVisibility(View.VISIBLE);
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
                        if (Helper.areYouSure(getContext(), "School is agree. Do you want to get hired?"))
                            SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.BOTH_ACCEPTED);
                        break;
                    case StaticVariables.BOTH_ACCEPTED:
                        Helper.alert("Both parites are agree, wait for contract", getContext());
                        break;
                    default:
                        if (Helper.areYouSure(getContext(), "Are you agree for this job?"))
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
                        if (Helper.areYouSure(getContext(), "Teacher is agree. Do you want to hire him?"))
                            SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.BOTH_ACCEPTED);
                        break;
                    case StaticVariables.BOTH_ACCEPTED:
                        Helper.alert("Both parites are agree", getContext());
                        break;
                    default:
                        if (Helper.areYouSure(getContext(), "do you want to hire him?"))
                            SchoolAcceptTeacherAfterInterViewStatusUpdate(StaticVariables.SCHOOL_ACCEPTED);
                        break;
                }
            }
        });
        rejectBtn.setOnClickListener(v -> {
            if(Helper.areYouSure(getContext(), "Are you sure want to reject!"))
                openDialoug();
        });
        reInterviewBtn.setOnClickListener(v -> {
            if (Helper.isTeacherChating)
            {
                switch (Helper.getSchool().getStatus())
                {
                    case "School Interview":
                            if (Helper.areYouSure(getContext(), "School Wants to take Another Interview"))
                                SchoolAcceptTeacherAfterInterViewStatusUpdate("1");
                            else
                                SchoolAcceptTeacherAfterInterViewStatusUpdate("t not agree");
                        //acceptBtn.setClickable(false);
                        break;
                    default:
                        Helper.alert("Only School can take Interview again!", getContext());
                        break;
                }
            }
            else
            {
                switch (Helper.getSchool().getStatus())
                {
                    case "School Interview":
                        Helper.alert("Scheduled sent already!", getContext());
                        break;
                    case "t not agree":
                        Helper.alert("Teacher don't want re-interview!", getContext());
                        break;
                    default:
                        if(Helper.areYouSure(getContext(), "Do You Want to Interview Again!")) {
                            ScheduleHelper.scheduleInterview(Helper.getTeacher(), ChatFragment.this);
                        }
                        break;
                }
            }
        });
        contractBtn.setOnClickListener(v -> {
            if (Helper.isTeacherChating)
                Helper.alert("Contract Not Uploaded Yet!", getContext());
            else
                Helper.alert("Please Upload The Contrac", getContext());
        });

        messageArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, true);
        // linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getMessages();
    }

    private void updateBottomSheet(String text) {
        icons.setVisibility(View.GONE);
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
}
