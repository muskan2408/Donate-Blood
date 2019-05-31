package com.muskankataria2408.blooddonationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Object;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

public class MobileAuthActivity extends AppCompatActivity {


    private static final String TAG = "MobileAuthActivity";



    private ConstraintLayout phoneLayout,codeLayout;
    private TextInputLayout phoneET,codeET;
    private ProgressBar phoneProgress,codeProgress;
    private Button sendCodeBT;
    private TextView errorText;
    String phoneNumber;
    private DatabaseReference mDataBase;

    private DatabaseReference mUserDatabse;

    private FirebaseAuth mAuth;
    DatabaseReference mFirebaseDatabaseReference;

    private int btnType = 0;
    private boolean isExist=false;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    FirebaseUser mfirebaseUser;
    protected PhoneAuthProvider.ForceResendingToken mResendToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_auth);


        mfirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        phoneLayout = findViewById(R.id.phoneLayout);
        codeLayout  = findViewById(R.id.codeLayout);

        phoneET = findViewById(R.id.phoneET);
        codeET = findViewById(R.id.codeET);

        phoneProgress = findViewById(R.id.phoneProgress);
        codeProgress = findViewById(R.id.codeProgress);
        mUserDatabse= FirebaseDatabase.getInstance().getReference().child("Users");

        sendCodeBT = findViewById(R.id.sendCodeBT);

        errorText = findViewById(R.id.errorText);
        FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance();


        sendCodeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





        sendCodeBT.setError(null);

        if (btnType==0) {
            phoneNumber = phoneET.getEditText().getText().toString();
            if(phoneNumber.length()==10){
                phoneProgress.setVisibility(View.VISIBLE);
                phoneET.setEnabled(false);
                //sendCodeBT.setVisibility(View.GONE);

                mFirebaseDatabaseReference=FirebaseDatabase.getInstance().getReference("Users");
                Log.e(TAG,String.valueOf(mFirebaseDatabaseReference));
                mFirebaseDatabaseReference.orderByChild("mobile").equalTo(phoneNumber).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(TAG,"OnDATACHANGE");
                            if (dataSnapshot.exists()) {

                                Log.d(TAG, "DataSnap Shots: " + dataSnapshot.getValue());
                                /* if alredy exist and check for first time, second time isExist=true*/
                                phoneET.setError("Mobile no. already exists enter different mobile number");
                                phoneET.setEnabled(true);
                               phoneET.setError(null);
                            }
                            else {


                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        "+91" + phoneNumber,
                                        30,
                                        TimeUnit.SECONDS,
                                        MobileAuthActivity.this,
                                        mCallbacks);
                            }

//                        if(dataSnapshot.getValue()!=null) {
//
//                            Log.d(TAG, "DataSnap Shots: " + dataSnapshot.getValue());
////                                /* if alredy exist and check for first time, second time isExist=true*/
//                            phoneET.setError("Mobile no. already exists enter different mobile number");
//                        }else{

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG,"databseError : OnCancelled");
                    }

                });

//                UserRecord userRecord = FirebaseAuth.getInstance().getUserByPhoneNumber(phoneNumber);
//// See the UserRecord reference doc for the contents of userRecord.
//                System.out.println("Successfully fetched user data: " + userRecord.getPhoneNumber());

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+phoneNumber,
                        30,
                        TimeUnit.SECONDS,
                        MobileAuthActivity.this,
                        mCallbacks);


            }else {
                phoneET.setError("Please enter a valid number...");
            }
        }else {

            String code = codeET.getEditText().getText().toString();

            if(code.length()!=6){
                codeET.setError("Please enter a valid code");

            }else {
                sendCodeBT.setEnabled(false);
                codeProgress.setVisibility(View.VISIBLE);
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId,code);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    errorText.setText(e.getMessage());
                    Log.d("error",e.getLocalizedMessage());
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    errorText.setText("Invalid mobile number");
                    Log.d("error",e.getLocalizedMessage());
                } else {
                    errorText.setText(e.getMessage());
                    Log.d("error",e.getLocalizedMessage());
                }
                errorText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
                phoneProgress.setVisibility(View.INVISIBLE);
                codeLayout.setVisibility(View.VISIBLE);
                btnType = 1;
                sendCodeBT.setEnabled(true);
                sendCodeBT.setText("Verify Code");

            }
        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            final String contactNo = user.getPhoneNumber();
                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();
                            mDataBase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            FirebaseUser user1 = mAuth.getCurrentUser();
                            String curent_user_id=mAuth.getCurrentUser().getUid();
                            String deviceToken= FirebaseInstanceId.getInstance().getToken();
                            //  mNotificationReference.child("token").setValue(FirebaseInstanceId.getInstance().getToken());

                                                    Intent intent=new Intent(MobileAuthActivity.this,Register.class);
                                                    intent.putExtra("mobile",contactNo);
                                                    startActivity(intent);
                                                    finishAffinity();


                           // Toast.makeText(getApplicationContext(),"valid",Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MobileAuthActivity.this,"Code sent", Toast.LENGTH_SHORT).show();

                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                codeProgress.setVisibility(View.INVISIBLE);
                                errorText.setText("The sms verification code is invalid. Please check the code");
                                Log.d(TAG,task.getException().getLocalizedMessage());
                                errorText.setVisibility(View.VISIBLE);
                                sendCodeBT.setEnabled(true);
                            }
                        }
                    }
                });
    }

}
