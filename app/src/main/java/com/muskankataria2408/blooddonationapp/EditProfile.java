package com.muskankataria2408.blooddonationapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
      TextInputEditText name,email,age,address,mobile;
      Spinner bloodgroup;
      Button update,updateMobile;
      ProgressDialog progressDialog;
    String Sgender,device_token;
    String current_user_id;
    String Smobile;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog = new ProgressDialog(this);

        name=(TextInputEditText)findViewById(R.id.name);
        email=(TextInputEditText)findViewById(R.id.email);
        age=(TextInputEditText)findViewById(R.id.age);
        address=(TextInputEditText)findViewById(R.id.address);
        bloodgroup=(Spinner)findViewById(R.id.spinner);
        updateMobile=(Button)findViewById(R.id.updatemobile);
    //    mobile=(TextInputEditText)findViewById(R.id.mobileno);
        update=(Button)findViewById(R.id.update);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        current_user_id=mCurrentUser.getUid();

        Intent i=getIntent();
         final String Sname=i.getStringExtra("name");
        final String Semail=i.getStringExtra("email");

        final String Sage=i.getStringExtra("age");
        final String Saddress=i.getStringExtra("address");
        final String Sbloodgroup=i.getStringExtra("bloodgroup");
      Smobile=i.getStringExtra("mobile");
       Sgender = i.getStringExtra("gender");
        device_token=i.getStringExtra("device_token");
        updateMobile.setVisibility(View.GONE);
       name.setText(Sname);
       email.setText(Semail);
       age.setText(Sage);
       address.setText(Saddress);

       bloodgroup.setSelection(getIndex(bloodgroup,Sbloodgroup));
//       mobile.setText(Smobile);

       update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               progressDialog=new ProgressDialog(EditProfile.this);
               progressDialog.setTitle("Updating Profile");
               progressDialog.setMessage("Please wait while we creating your account");
               progressDialog.setCancelable(false);
               progressDialog.show();
               startUpdate(Sname,Semail,Saddress,Sbloodgroup,Sage,Smobile);
           }
       });

//       updateMobile.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//                  Intent i=new Intent(EditProfile.this,EditMobile.class);
//                  i.putExtra("user_id",current_user_id);
//                  startActivity(i);
//           }
//       });

    }

    private void startUpdate(String sname, String semail, String saddress, String sbloodgroup, String sage, String smobile) {

        if(sname.isEmpty()){
           name.setError("Name is Required");
            name.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(semail.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(semail).matches()){
            email.setError("Please enter a valid email or password");
            email.requestFocus();
            progressDialog.cancel();
            return;
        }

        if(sage.isEmpty())
        {
            age.setError("Age is Required");
            age.requestFocus();
            progressDialog.cancel();
            return;
        }

        if(saddress.isEmpty())
        {
            address.setError("Address is Required");
            address.requestFocus();
            progressDialog.cancel();
            return;
        }

        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
        HashMap<String,String> userMap=new HashMap<>();
        userMap.put("name",name.getEditableText().toString());
        userMap.put("email", email.getEditableText().toString());
        userMap.put("mobile",Smobile );
        userMap.put("address", address.getEditableText().toString());
        userMap.put("BloodGroup",bloodgroup.getSelectedItem().toString());
        userMap.put("Age",age.getEditableText().toString());
        userMap.put("Gender",Sgender);
        userMap.put("device_token",device_token);

        mUserDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    progressDialog.dismiss();
                    new AlertDialog.Builder(EditProfile.this).setTitle("Profile Updated Successfully")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent=new Intent(EditProfile.this,MyProfile.class);
                                    startActivity(intent);
                                    finish();
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


    public int getIndex(Spinner bg,String Sbg){
             int index=0;
                  for(int i=0;i<bg.getCount();i++)
                  {
                      if(bg.getItemAtPosition(i).equals(Sbg))
                      {
                          index=i;
                      }
                  }
             return index;

         }

}
