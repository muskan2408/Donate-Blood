package com.muskankataria2408.blooddonationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muskankataria2408.blooddonationapp.utils.SessionManagement;

import java.util.HashMap;

import static com.muskankataria2408.blooddonationapp.utils.SessionManagement.KEY_EMAIL;
import static com.muskankataria2408.blooddonationapp.utils.SessionManagement.KEY_MOBILE;

public class MyProfile extends AppCompatActivity {
    FloatingActionButton edit;
    TextView nameset, emailset,mobileset, addressset,bloodgroupset,ageset,genderset;
    String name,email,address,BloodGroup,age,mobile,gender;
    String user_id;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgressDailog;
    private FirebaseAuth mAuth;
    SessionManagement session;
    String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        session=new SessionManagement(this);
//
//        HashMap<String,String> usermap=session.getUserDetails();
//
//        mobile=(String)usermap.get(KEY_MOBILE);

//        ActionBar actionbar = getSupportActionBar();
////        actionbar.setDisplayHomeAsUpEnabled(true);
//       getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_user_id=mCurrentUser.getUid();
        mAuth= FirebaseAuth.getInstance();

           nameset=(TextView)findViewById(R.id.name);
           emailset=(TextView)findViewById(R.id.email);
           mobileset=(TextView)findViewById(R.id.mobile);
           addressset=(TextView)findViewById(R.id.address);
           bloodgroupset=(TextView)findViewById(R.id.bloodgroup);
           ageset=(TextView)findViewById(R.id.age);
           genderset=(TextView)findViewById(R.id.gender);

//        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
//        final String mobile = getIntent().getStringExtra("mobile");
//        final FirebaseUser[] user = {mAuth.getCurrentUser()};
//     String curent_user_id=mAuth.getCurrentUser().getUid();

//        mUserDatabase.child(curent_user_id).child("mobile").setValue(mobile[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.e("MyProfile","mobile added successfully");
//            }
//        });


        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 name=dataSnapshot.child("name").getValue().toString();
                 email=dataSnapshot.child("email").getValue().toString();
                mobile =dataSnapshot.child("mobile").getValue().toString();
                 BloodGroup=dataSnapshot.child("BloodGroup").getValue().toString();
                age=dataSnapshot.child("Age").getValue().toString();
                 address=dataSnapshot.child("address").getValue().toString();
                 gender=dataSnapshot.child("Gender").getValue().toString();
                 device_token=dataSnapshot.child("device_token").getValue().toString();


                nameset.setText(name.toUpperCase());
                emailset.setText(email);
                mobileset.setText("Mobile : "+ mobile);
                bloodgroupset.setText("Blood Group : "+BloodGroup);
                ageset.setText("Age : "+age+" years");
                addressset.setText("Address : "+address);
                genderset.setText("Gender : "+gender);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        edit=(FloatingActionButton) findViewById(R.id.floatingActionButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyProfile.this, EditProfile.class);
                i.putExtra("name",name);
                i.putExtra("email",email);
                i.putExtra("age",age);
                i.putExtra("address",address);
                i.putExtra("bloodgroup",BloodGroup);
                i.putExtra("mobile", mobile);
                i.putExtra("gender",gender);
                i.putExtra("device_token",device_token);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i =new Intent(MyProfile.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
