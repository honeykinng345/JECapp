package j.e.c.com.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.MainActivity;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.SessionManager;
import j.e.c.com.appConfig;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    TextView forgotPswrd;
    //private EditText phone, password;

    ProgressDialog progressDialog;
    CountryCodePicker ccp;
    @BindView(R.id.phone)
    TextInputLayout phone;
    @BindView(R.id.password)
    TextInputLayout password;

    private SessionManager sessionManager;
    double longitude = 0.0, latitude = 0.0;

    SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(getApplicationContext());
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait ");
        progressDialog.setCanceledOnTouchOutside(false);
        loginBtn = findViewById(R.id.loginBtn);
        ccp = findViewById(R.id.picPhone);
        forgotPswrd = findViewById(R.id.forgotPswrd);
       // phone = findViewById(R.id.phone);
       // ccp.registerCarrierNumberEditText(phone.getEditText());
        //password = findViewById(R.id.password);
       // ccp.registerCarrierNumberEditText(phone.getEditText());
        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        loginBtn.setOnClickListener(v -> Validate());

    }

    String phoneCOde, PasswordTxt;

    private void Validate() {
        phoneCOde = phone.getEditText().getText().toString().trim();
        PasswordTxt = password.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(phoneCOde)) {
            phone.setError("Enter Phone Number");
            return;
        }
        if (TextUtils.isEmpty(PasswordTxt)) {
            password.setError("Enter Password");
            return;
        }
        CheckUser(phoneCOde, PasswordTxt);


    }

    String checkCode;

    private void CheckUser(String phoneCOde, String passwordTxt) {
        ccp.registerCarrierNumberEditText(phone.getEditText());
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        if (phoneCOde.matches("[0-9]+")) {
            checkCode = ccp.getFullNumberWithPlus();
            progressDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    appConfig.URL_loginPhone, response -> {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String error = obj.getString("error");
                    JSONArray heroArray = obj.getJSONArray("result");
                    sessionManager.setLogin(true);
                    for (int i = 0; i < heroArray.length(); i++) {
                        //getting product object from json array
                        JSONObject categories = heroArray.getJSONObject(i);
                        String name = categories.getString("name");
                        String phone = categories.getString("phone");
                        String userId = categories.getString("id");
                        String jecId = categories.getString("UserId");
                        storeData(name, phone, userId, jecId);


                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Wrong Request Password: ", Toast.LENGTH_LONG).show();
                }

            }, error -> {
                Toast.makeText(getApplicationContext(),"errror"+
                        error.getMessage().toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("phone", checkCode);
                    params.put("password", passwordTxt);
                    return params;
                }

            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    appConfig.URL_loginUserId, response -> {
                progressDialog.dismiss();
                try {
                    sessionManager.setLogin(true);
                    JSONObject obj = new JSONObject(response);
                    String error = obj.getString("error");
                    JSONArray heroArray = obj.getJSONArray("result");
                    for (int i = 0; i < heroArray.length(); i++) {
                        //getting product object from json array
                        JSONObject categories = heroArray.getJSONObject(i);
                        String name = categories.getString("name");
                        String phone = categories.getString("phone");
                        String userId = categories.getString("id");
                        String jecId = categories.getString("UserId");
                        storeData(name, phone, userId, jecId);


                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Wrong Request Password: ", Toast.LENGTH_LONG).show();
                }

            }, error -> {
                Toast.makeText(getApplicationContext(),"errorr"+
                        error.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("UserId", phoneCOde);
                    params.put("password", passwordTxt);
                    return params;
                }

            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private void storeData(String name, String phone, String userId, String jecid) {
        //Toast.makeText(LoginActivity.this, ""+name+phone+userId, Toast.LENGTH_LONG).show();
        boolean t = sqLiteHandler.insertData(name, phone, userId, jecid);
        if (t) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "fail", Toast.LENGTH_LONG).show();
        }


    }


    public void foreget(View view) {
        startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
    }


    @OnClick({R.id.backArrow, R.id.tvRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                finish();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
