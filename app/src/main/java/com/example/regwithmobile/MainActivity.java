package com.example.regwithmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    EditText phstrng, otpstrng;
    FirebaseAuth mAuth;
    String codeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// txt from ctrl to string
        phstrng = findViewById(R.id.phonenotxtctrl);
        otpstrng = findViewById(R.id.otptxtctrl);
        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.sndotpbtnctrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "initiated", Toast.LENGTH_SHORT).show();
                sendVerificationcode();


            }
        });

        findViewById(R.id.signinbtnctrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyotp();

            }
        });
    }

    private void verifyotp()
    {
        String code = otpstrng.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "otp correct ", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MainActivity.this, PROFILE.class);
//                            startActivity(intent);


                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(MainActivity.this, "invalid otp ", Toast.LENGTH_SHORT).show();

                            }


                        }
                    }
                });
    }


    private  void sendVerificationcode()
    {

        String phoneNumber = phstrng.getText().toString();
                if (phoneNumber.isEmpty())
                {
                    phstrng.setError("Phone No. is Required");
                    phstrng.requestFocus();
                    return;
                }

                if (phoneNumber.length()<10)
                {
                    phstrng.setError("Must be 10 Digitis ");
                    phstrng.requestFocus();
                    return;
                }


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)


                         .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;

        }
    };


}