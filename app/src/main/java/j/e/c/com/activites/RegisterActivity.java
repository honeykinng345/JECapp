package j.e.c.com.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class RegisterActivity extends AppCompatActivity {
    private EditText phonetext, Codetext;
    private Button conitonANdNextButton;
    ProgressDialog progressDialog;
    private CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ");
        progressDialog.setCancelable(false);
        Codetext = findViewById(R.id.codeText);
        phonetext = findViewById(R.id.phone);
        conitonANdNextButton = findViewById(R.id.continueBtn);
        ccp = findViewById(R.id.picPhone);
        ccp.registerCarrierNumberEditText(phonetext);
        conitonANdNextButton.setOnClickListener(v -> ValidateData());
    }

    String phone;

    private void ValidateData() {
        phone = phonetext.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            phonetext.setError("Please Enter Phone Number");
            return;
        }
        CheckPhoneNumIsExisitOrNot(ccp.getFullNumberWithPlus().replace("", ""));


    }

    // Tag used to cancel the request
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
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    SendUserToCodeActivity();


                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }, error -> {
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void SendUserToCodeActivity() {
        Intent intent = new Intent(RegisterActivity.this, CodeActivity.class);
        intent.putExtra("mobile", ccp.getFullNumberWithPlus().replace("", ""));
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.backArrow)
    public void onViewClicked() {
        finish();
    }
}



