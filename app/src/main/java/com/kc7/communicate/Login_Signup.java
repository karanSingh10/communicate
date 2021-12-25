package com.kc7.communicate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kc7.communicate.userlist.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Login_Signup extends AppCompatActivity {

    EditText communicatenumber, communicateotp, communicatename;
    Button otp, signin;

    FirebaseAuth mAuth;
FirebaseUser mUser;
FirebaseFirestore Db;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
String code;
String name, number;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){

            startActivity(new Intent(Login_Signup.this, MainActivity.class));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__signup);


        communicatenumber = findViewById(R.id.communicatenumber);
        otp = findViewById(R.id.getotp);
        signin = findViewById(R.id.signin);
communicateotp = findViewById(R.id.communicateotp);
communicatename = findViewById(R.id.communicatename);

        signin.setVisibility(View.GONE);
        otp.setVisibility(View.VISIBLE);


        mAuth = FirebaseAuth.getInstance();


        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signin.setVisibility(View.VISIBLE);
                otp.setVisibility(View.GONE);


                if (communicatenumber.getText().toString().trim().isEmpty() ||
                        communicatenumber.getText().toString().length() != 10 ||
                communicatename.getText().toString().trim().isEmpty()) {

                    communicatenumber.setError("Please enter a valid number and name");



                } else {

                    name = communicatename.getText().toString().trim();
                    number = communicatenumber.getText().toString().trim();


                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+91" +
                                            communicatenumber.getText().toString().trim())       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(Login_Signup.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);


                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(Login_Signup.this, "" +
                        "Inside onverificationcomplete", Toast.LENGTH_SHORT).show();
                signIION(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Login_Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                otp.setVisibility(View.VISIBLE);
                signin.setVisibility(View.GONE);


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(Login_Signup.this, "" +
                            "Invalid Request", Toast.LENGTH_LONG).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(Login_Signup.this, "" +
                            "Quota exceeded", Toast.LENGTH_LONG).show();

                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                //sometimes the code is not detected automatically
                //so user has to manually enter the code
                Toast.makeText(Login_Signup.this, "OTP has been sent, " +
                        "please enter OTP and press signin", Toast.LENGTH_SHORT).show();


                otp.setVisibility(View.GONE);
                signin.setVisibility(View.VISIBLE);
code = s;

/*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (communicateotp.getText().toString().trim().isEmpty()) {
                            Toast.makeText(Login_Signup.this, "OTP=", Toast.LENGTH_SHORT).show();

                        } else {
                        }


                    }
                }, 10000);

  */          }
        };

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(communicateotp.getText().toString().trim().isEmpty()){

                    Toast.makeText(Login_Signup.this, "" +
                            "Enter a valid otp", Toast.LENGTH_SHORT).show();


                }
                else{

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, communicateotp.getText().toString().trim());

                    signIION(credential);


                }

            }
        });

    }


    private void signIION(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login_Signup.this, "" +
                            "Sign in done", Toast.LENGTH_SHORT).show();

                    mAuth = FirebaseAuth.getInstance();
                    mUser = mAuth.getCurrentUser();
                    Db = FirebaseFirestore.getInstance();

                    DocumentReference docref = Db.collection("UserChat")
                            .document(mUser.getUid());

                    Map<String, Object> newdoc = new HashMap<>();
                    newdoc.put("name", name);
                    newdoc.put("number", number);
newdoc.put("latestchat", "");
newdoc.put("uid", mUser.getUid());

                    docref.set(newdoc).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(Login_Signup.this, "" +
                                        "Ready to communicate now", Toast.LENGTH_SHORT).show();

                            }
                            else{

                                Toast.makeText(Login_Signup.this, "" +
                                        "Unable to add details", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Login_Signup.this, "" +
                                    e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                    startActivity(new Intent(Login_Signup.this, MainActivity.class));
finish();


                } else {
                    Toast.makeText(Login_Signup.this, "No sign in 1", Toast.LENGTH_SHORT).show();

                    Toast.makeText(Login_Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login_Signup.this, "No sign in 2", Toast.LENGTH_SHORT).show();

                Toast.makeText(Login_Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}