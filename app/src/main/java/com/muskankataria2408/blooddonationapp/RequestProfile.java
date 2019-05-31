package com.muskankataria2408.blooddonationapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.transition.FragmentTransitionSupport;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.maps.model.ButtCap;

public class RequestProfile extends AppCompatActivity {
      private TextView name,bloodgroup,mobile,hospital,landmark;

      private Button accept,delete,showmaps;
      private String Sname,Sbloodgroup,Shopitaladdress,Smobile,Slandmark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_profile);
        name=(TextView)findViewById(R.id.name);
        bloodgroup=(TextView)findViewById(R.id.BloodGroup);
        mobile=(TextView)findViewById(R.id.Mobile);
        hospital=(TextView)findViewById(R.id.Hospital);
        landmark=(TextView)findViewById(R.id.landmark);
        accept=(Button)findViewById(R.id.accept);
        delete=(Button)findViewById(R.id.delete);
        showmaps=(Button)findViewById(R.id.showmaps);


        Intent i=getIntent();
        Sname=i.getStringExtra("Name");
        Sbloodgroup=i.getStringExtra("Bloodgroup");
        Smobile=i.getStringExtra("mobile");
        Shopitaladdress=i.getStringExtra("HospitalAddress");
        Slandmark=i.getStringExtra("Landmark");

        name.setText("Requested By : "+Sname);
        bloodgroup.setText("Blood Group : "+Sbloodgroup);
        mobile.setText("Mobile No. : "+Smobile);
        hospital.setText("Hospital Address : "+Shopitaladdress);
        landmark.setText("Landmark : "+Slandmark);

        showmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RequestProfile.this,MainActivity.class);
                i.putExtra("HospitalAddress",Shopitaladdress);
                startActivity(i);
//                Fragment TargetFragment= new MapsFragment();
//                android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(android.R.id.content,TargetFragment);
//                Bundle bundle = new Bundle();
//                bundle.putString("HospitalAddress", Shopitaladdress);
////              set Fragmentclass Arguments
//                MapsFragment fragobj = new MapsFragment();
//                fragobj.setArguments(bundle);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent i =new Intent(RequestProfile.this,Notification.class);
        startActivity(i);
        finish();
    }
}
