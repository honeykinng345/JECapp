package j.e.c.com.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
//import com.android.volley.toolbox.StringRequest;

public class NewPasswordActivity extends AppCompatActivity {
    String JecId;
    SQLiteHandler sqLiteHandler;
    private EditText password, confirmPassword;
    private Button submitBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> ValidateUser());
        loadUserInfo();


    }

    String pass, confirmPass;

    private void ValidateUser() {
        pass = password.getText().toString().trim();
        confirmPass = confirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            password.setError("Password Field Is Empty");
            return;
        }
        if (pass.length() < 6) {
            password.setError("Password Must Be Grater Than 6 Digits");
            return;
        }
        if (TextUtils.isEmpty(confirmPass)) {
            confirmPassword.setError(" Confirm Password Field Is Empty");
            return;
        }
        if (!pass.equals(confirmPass)) {
            confirmPassword.setError("Password Not Match");
            return;
        }
        UpdatePassword(JecId, pass);


    }

    private void UpdatePassword(String jecId, String pass) {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                appConfig.URL_UpdatePassword, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    Toast.makeText(getApplicationContext(), "Password Update Successful", Toast.LENGTH_LONG).show();
                    SendUserToLoginActivity();


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
                params.put("userId", jecId);
                params.put("password", pass);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadUserInfo() {
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            JecId = res.getString(3);


        }


    }

    @OnClick(R.id.backArrow)
    public void onViewClicked() {
        finish();
    }
}