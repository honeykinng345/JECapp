package j.e.c.com.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.R;
import j.e.c.com.appConfig;


public class ProfileActivity extends AppCompatActivity {

    String phone;
    EditText name, email, password;
    Button register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ");
        progressDialog.setCancelable(false);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        register.setOnClickListener(v -> validateUser());
    }

    String fullName, UserId, PasswordCHeck;

    private void validateUser() {
        fullName = name.getText().toString().trim();
        UserId = email.getText().toString().trim();
        PasswordCHeck = password.getText().toString().trim();
        if (TextUtils.isEmpty(fullName)) {
            name.setError("Enter Your Name Please");
            return;
        }
        if (TextUtils.isEmpty(UserId)) {
            email.setError("ID Field Is Empty");
            return;
        }
        if (TextUtils.isEmpty(PasswordCHeck)) {
            Toast.makeText(this, "password Field Is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (PasswordCHeck.length() < 6) {
            Toast.makeText(this, "password must be 6 Digitise", Toast.LENGTH_SHORT).show();
            return;

        }
        //  Toast.makeText(ProfileActivity.this,""+phone+code,Toast.LENGTH_LONG).show();
        SendData();
    }

    private void SendData() {
        progressDialog.setMessage("Registration In Process");
        progressDialog.show();
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_regsiter, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Toast.makeText(ProfileActivity.this,""+response,Toast.LENGTH_LONG).show();
                        // Now store the user in sqlite
                        // Now store the user in sqlite
                      /*String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");*/
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ProfileActivity.this, "JEC ID Already Taken" + UserId, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "" + e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("hh", e.toString());
                }

            }
        }, error -> {
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String, String> params = new HashMap<>();
                params.put("name", fullName);
                params.put("phone", phone);
                params.put("UserId", UserId);
                params.put("password", PasswordCHeck);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @OnClick(R.id.backBtn)
    public void onViewClicked() {
        finish();
    }
}
