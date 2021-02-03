package j.e.c.com.chatFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
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
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.chatFragments.models.Message;

public class ChatFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
   Message msg;
   ArrayList<Message> messageArrayList;
   ChatAdapter chatAdapter;
   Gson  gson;

    @BindView(R.id.sendBtn)
    ImageView sendBtn;
    @BindView(R.id.message)
    EditText message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        ButterKnife.bind(this, view);
        gson= new Gson();

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

        if (m.isEmpty()){
            Toast.makeText(getActivity(),"Not Send Empty Message",Toast.LENGTH_LONG).show();
            return;
        }else{
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
                        Toast.makeText(getActivity(),"ok",Toast.LENGTH_LONG).show();


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
                    params.put("id",Helper.fetchUserId(getContext()));
                    params.put("message", m);
                    params.put("admin",Helper.AdminId);
                    params.put("frm","u");
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
        messageArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, true);
       // linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        sendBtn.setOnClickListener(v -> {

            SendData();


        });

        getMessages();
    }

    @OnClick(R.id.backArrow)
    public void onViewClicked() {
        getFragmentManager().popBackStack();
    }


}
