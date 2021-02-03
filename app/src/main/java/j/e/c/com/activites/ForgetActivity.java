package j.e.c.com.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.R;
import j.e.c.com.appConfig;
//import com.android.volley.toolbox.StringRequest;

public class ForgetActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private CountryCodePicker ccp;
    private Button forgetBtn;
    private EditText phonetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        progressDialog = new ProgressDialog(this);
        ButterKnife.bind(this);
        progressDialog.setTitle("Please Wait ");
        progressDialog.setCancelable(false);
        forgetBtn = findViewById(R.id.forgetBtn);
        ccp = findViewById(R.id.ccp);
        phonetext = findViewById(R.id.phone);
        ccp.registerCarrierNumberEditText(phonetext);
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
            }
        });
    }

    String phone;

    private void validateUser() {
        phone = phonetext.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            phonetext.setError("Please Enter Phone Number");
            return;
        }
        CheckPhoneNumIsExisitOrNot(ccp.getFullNumberWithPlus().replace("", ""));


    }

    String tag_string_req = "req_login";

    private void CheckPhoneNumIsExisitOrNot(String replace) {
        progressDialog.setMessage("We Are Processing On it");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_PhoneValidate, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                // Check for error node in json
                if (error) {
                    // user successfully logged in
                    // Create login session
                    SendUserToForgetCodeActivity();


                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                   /* Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();*/
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "Wrong Request", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }, error -> {
           /* Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();*/
            progressDialog.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("phone", replace);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void SendUserToForgetCodeActivity() {
        Intent intent = new Intent(ForgetActivity.this, ForgetCodeActivity.class);
        intent.putExtra("mobile", ccp.getFullNumberWithPlus().replace("", ""));
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.backArrow)
    public void onViewClicked() {
        finish();
    }
}