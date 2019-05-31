package com.muskankataria2408.blooddonationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.muskankataria2408.blooddonationapp.utils.SessionManagement;

public class LoginActivity extends AppCompatActivity {
    Button login, signup;
    private  TextInputLayout mEmail,mPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabse;
    SessionManagement session;
    String mobile;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.update);
        signup=(Button)findViewById(R.id.create);
        mEmail=(TextInputLayout)findViewById(R.id.email);
        mPassword=(TextInputLayout)findViewById(R.id.pin);
        progressDialog = new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
       session=new SessionManagement(getApplicationContext());
        mUserDatabse= FirebaseDatabase.getInstance().getReference().child("Users");

//          Intent i=getIntent();
//          mobile=i.getStringExtra("mobile");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();

                   progressDialog.setTitle("LogIn");
                   progressDialog.setMessage("Please wait while we check your credentials");
                   progressDialog.setCanceledOnTouchOutside(false);
                   progressDialog.show();
                   loginUser(email,password);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,MobileAuthActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
    }

    private void loginUser(final String email, final String password) {
        if(email.isEmpty()){
            mEmail.setError("Email is Required");
            mEmail.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please enter a valid email or password");
            mEmail.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(password.isEmpty()){
            mPassword.setError("Password is Required");
            mPassword.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(password.length()<=5)
        {
            mPassword.setError("Password must be of 6 characters");
            mPassword.requestFocus();
            progressDialog.cancel();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                     final String curent_user_id=mAuth.getCurrentUser().getUid();
                    String deviceToken= FirebaseInstanceId.getInstance().getToken();


                    mUserDatabse.child(curent_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              session.createLoginSession(email, password);
                              Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                              mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(mainIntent);
                              finish();
                }
                      });
//                    mUserDatabse.child(curent_user_id).child("mobile").setValue(mobile).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                        }
//                    });

                }
                else
                {
                    //  Toast.makeText(getApplicationContext(),"Task is starting...",Toast.LENGTH_LONG).show();
                    String error = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        error = "Invalid Email!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Invalid Password!";
                    } catch (Exception e) {
                        error = "Default error!";
                        e.printStackTrace();
                    }
                 progressDialog.hide();
                    Toast.makeText(LoginActivity.this,error, Toast.LENGTH_LONG).show();
                }

            }
        });
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//      if(currentUser!=null){
//            Intent startIntent=new Intent(LoginActivity.this, MainActivity.class) ;
//            startActivity(startIntent);
//            finish();
//        }
//    }

}
