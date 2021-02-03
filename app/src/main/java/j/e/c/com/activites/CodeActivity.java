package j.e.c.com.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import j.e.c.com.R;

public class CodeActivity extends AppCompatActivity {
String phoneCode ;
String otpId;
EditText codeText;
Button submitBtn;
FirebaseAuth mAuth;
TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        phoneCode = intent.getStringExtra("mobile");
        codeText = findViewById(R.id.codeText);
        tv2 = findViewById(R.id.tv2);
        tv2.setText(""+phoneCode);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> {
    SubmitMethod();
        });
        initiatiateOtp();

    }

    private void SubmitMethod() {


        if (codeText.getText().toString().isEmpty())
            Toast.makeText(CodeActivity.this, "Blank Filed Can Not Be Processed", Toast.LENGTH_SHORT).show();

        else if (codeText.getText().toString().length()!=6)
            Toast.makeText(CodeActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,codeText.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }


    }

    private void initiatiateOtp() {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneCode, 60, TimeUnit.SECONDS, CodeActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                otpId = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }



            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(CodeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // progressDialog.dismiss();
                           // Toast.makeText(CodeActivity.this, "Congruclation you are register ", Toast.LENGTH_SHORT).show();
                            sendUserMainActivity();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                          //  progressDialog.dismiss();
                            String e = Objects.requireNonNull(task.getException()).toString();
                            Toast.makeText(CodeActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();


                        }
                    }
                });

    }

    private void sendUserMainActivity() {
        Intent intent = new Intent(CodeActivity.this, ProfileActivity.class);
        intent.putExtra("phone",phoneCode);
        startActivity(intent);
        finish();
    }
    }

