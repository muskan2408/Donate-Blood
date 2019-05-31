package com.muskankataria2408.blooddonationapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private TextInputEditText mDisplayName,mEmail,mPassword,mConfirmPassword,mAge,mAddress;
    private Button mSignup;
    private Toolbar mtoolbar;
    private Spinner mbloodGroup,mGender;
    String bloodgroup,gender,mobile;
    private DatabaseReference mDataBase;

    // Progress Dailog
    private ProgressDialog progressDialog;
    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        Intent i=getIntent();
        mobile=i.getStringExtra("mobile");
        //mobile="0000000000";
        progressDialog = new ProgressDialog(this);

        mDisplayName=(TextInputEditText)findViewById(R.id.name);
        mEmail=(TextInputEditText) findViewById(R.id.emailid);
        mPassword=(TextInputEditText) findViewById(R.id.pin1);
        mConfirmPassword=(TextInputEditText) findViewById(R.id.pin2);
        mAge=(TextInputEditText) findViewById(R.id.age);
        mAddress=(TextInputEditText) findViewById(R.id.address1);
        mbloodGroup=(Spinner)findViewById(R.id.qual_spinner);
        mGender=(Spinner)findViewById(R.id.gender_spinner);
        mSignup=(Button)findViewById(R.id.register);

        mbloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgroup=mbloodGroup.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                  bloodgroup="A+";
            }
        });
        mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender=mGender.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender="Male";
            }
        });

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=mEmail.getEditableText().toString();
                String name=mDisplayName.getEditableText().toString();
                String password=mPassword.getEditableText().toString();
                String confirmPassword=mConfirmPassword.getEditableText().toString();
                String address=mAddress.getEditableText().toString();
                String age=mAge.getEditableText().toString();
                progressDialog=new ProgressDialog(Register.this);
                progressDialog.setTitle("Registering User");
                progressDialog.setMessage("Please wait while we create your account");
                progressDialog.setCancelable(false);
                progressDialog.show();
                startRegister(name,email,password,confirmPassword,address,bloodgroup,gender,age,mobile);

            }
        });



    }

    private void startRegister(final String name, final String email, final String password, String confirmPassword, final String address, final String bloodgroup, final String gender, final String age,final String mobile) {

        if(name.isEmpty()){
            mDisplayName.setError("Name is Required");
            mDisplayName.requestFocus();
            progressDialog.cancel();
            return;
        }
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
        if(password.length()<=5){
            mPassword.setError("Password too short,enter minimum 6 characters");
            mPassword.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(confirmPassword.isEmpty()){
            mConfirmPassword.setError("ConfirmPassword is Required");
            mConfirmPassword.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(!confirmPassword.equals(password)){
            mConfirmPassword.setError("Password does not match");
            mConfirmPassword.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(age.isEmpty())
        {
            mAge.setError("Age is Required");
            mAge.requestFocus();
            progressDialog.cancel();
            return;
        }
         if(address.isEmpty())
         {
             mAddress.setError("Address is Required");
             mAddress.requestFocus();
             progressDialog.cancel();
             return;
         }

        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                boolean check= !task.getResult().getProviders().isEmpty();

                if(!check){
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                                String uid=current_user.getUid();
                                mDataBase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                String deviceToken= FirebaseInstanceId.getInstance().getToken();

                                HashMap<String,String> userMap=new HashMap<>();
                                userMap.put("name",name);
                                userMap.put("email", email);
                                userMap.put("mobile",mobile );
                                userMap.put("address", address);
                                userMap.put("BloodGroup",bloodgroup);
                                userMap.put("Gender",gender);
                                userMap.put("Age",age);
                                userMap.put("device_token",deviceToken);

                                mDataBase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            new AlertDialog.Builder(Register.this).setTitle("User Registered Successfully")
                                                    .setMessage("Please Login to Continue")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent=new Intent(Register.this,LoginActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                                        }
                                                    }).show();

                                        }
                                        else {

                                            String error="";
                                            try{
                                                throw task.getException();
                                            }
                                            catch(FirebaseAuthWeakPasswordException e)
                                            {
                                                error="Weak Password";
                                            }catch(FirebaseAuthInvalidCredentialsException e)
                                            {
                                                error="Invalid Email";
                                            }catch(FirebaseAuthUserCollisionException e)
                                            {
                                                error="Existing Account";
                                            }
                                            catch (Exception e) {
                                                error="Unknown Error";
                                                e.printStackTrace();
                                            }

                                            progressDialog.hide();
                                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });

                            }
                        }
                    });
                }
                else
                {
                    progressDialog.dismiss();

                    Toast.makeText(Register.this, "Email Already Exist! please enter another email address", Toast.LENGTH_SHORT).show();
                }
            }
        });



        }

}
