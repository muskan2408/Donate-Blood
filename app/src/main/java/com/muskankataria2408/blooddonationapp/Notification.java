package com.muskankataria2408.blooddonationapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.muskankataria2408.blooddonationapp.models.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextView empty;
    JSONObject jsonObj;
    private FirebaseUser mCurrentUser;
    RequestUserDetail r;
    private DatabaseReference mUserDatabse;
    private DatabaseReference mDatabase;
    Model model;

    JSONArray json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Intent intent=getIntent();
        String body= intent.getStringExtra("content");
        String id=intent.getStringExtra("id");
//        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = current_user.getUid();
//        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Requests").child(uid);
//        mUserDatabse.push().setValue(id);
////        //Toast.makeText(Notification.this,body,Toast.LENGTH_SHORT).show();
//        if(getIntent().getExtras()==null && r==null || arr.size()>0)
//        {
//        if(id!=null)

//        {
//            arr.add(id);
//        }

//        Toast.makeText(Notification.this, "Ready to do something" +
//                " Notification", Toast.LENGTH_SHORT).show();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(mCurrentUser!=null) {
                String current_user_id = mCurrentUser.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Requests").child(current_user_id);
            }
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // int a= (int) dataSnapshot.getChildrenCount();
                    final ArrayList<Model>  arr = new ArrayList<Model>();
                    if (!dataSnapshot.hasChildren()) {
                        if (gridLayoutManager.getItemCount() == 0) {
                            progressDialog.dismiss();
                            empty = (TextView) findViewById(R.id.empty);
                            empty.setText("You Don't have any Notification");
                        } else {
                            progressDialog.dismiss();

                            View b = findViewById(R.id.empty);
                            b.setVisibility(View.GONE);

                        }
                    } else {



                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            String id = (String) snap.getValue();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Notification").child(id);
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String setname = dataSnapshot.child("name").getValue().toString();
                                    String setmobile = dataSnapshot.child("mobile").getValue().toString();
                                    String setbloodgroup = dataSnapshot.child("bloodgroup").getValue().toString();
                                    String sethospitaladress = dataSnapshot.child("hospital").getValue().toString();
                                    String setlandmark = dataSnapshot.child("landmark").getValue().toString();
                                    String settoken = dataSnapshot.child("token").getValue().toString();
                                    String uid=dataSnapshot.getKey();
                                    String userid=dataSnapshot.getKey();
                                    model = new Model(setname, setbloodgroup, setmobile, sethospitaladress, setlandmark, settoken,userid);

                                    arr.add(model);

                                   // Toast.makeText(Notification.this, arr.toString(), Toast.LENGTH_SHORT).show();

                                    NotificationAdapter notificationAdapter = new NotificationAdapter(Notification.this, arr);
                                    recyclerView.setAdapter(notificationAdapter);
                                  //  Toast.makeText(Notification.this, "All Goes well set Adapter! Notification", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(Notification.this, "Something went worng! Notification", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Notification.this, "Something went worng! Requests", Toast.LENGTH_SHORT).show();

                }
            });

       // }

//if(getIntent().getExtras()!=null && arr.size()>0 ) {
//    try {
//        if (body != null) {
//            jsonObj = new JSONObject(body);
//        } else {
//            Toast.makeText(Notification.this, "Notification Error!", Toast.LENGTH_SHORT).show();
//        }
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//    try {
//        r = new RequestUserDetail(jsonObj.get("From").toString(), jsonObj.get("BloodGroup").toString(), jsonObj.get("Phone").toString(), jsonObj.get("HospitalAddress").toString(), jsonObj.get("Landmark").toString(), jsonObj.get("UserId").toString());
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
////    String id = null;
////    try {
////        id = jsonObj.get("UserId").toString();
////    } catch (JSONException e) {
////        e.printStackTrace();
////    }
//
//    Log.d("Notification", body);
////    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
////    String uid = current_user.getUid();
////    mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Requests").child(uid);
////    mUserDatabse.push().setValue(id);
////    final List<String> a = new ArrayList<>();
////    a.add(r.getName());
////    a.add(r.getBloodgroup());
////    a.add(r.getMobile());
////    a.add(r.getHospitalAddress());
////    a.add(r.getLandmark());
//
//    progressDialog.dismiss();
////    mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
////   // String current_user_id = mCurrentUser.getUid();
////    mDatabase = FirebaseDatabase.getInstance().getReference().child("Requests").child(current_user_id);
////    mDatabase.addValueEventListener(new ValueEventListener() {
////        @Override
////        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////            // int a= (int) dataSnapshot.getChildrenCount();
////
////            for (DataSnapshot snap : dataSnapshot.getChildren()) {
////                arr.add((String) snap.getValue());
////
////            }
////
////            NotificationAdapter notificationAdapter = new NotificationAdapter(Notification.this, arr);
////            recyclerView.setAdapter(notificationAdapter);
////        }
////
////        @Override
////        public void onCancelled(@NonNull DatabaseError databaseError) {
////
////        }
////    });
////    if (gridLayoutManager.getItemCount() == 0 && arr.size() == 0) {
////        progressDialog.dismiss();
////        empty = (TextView) findViewById(R.id.empty);
////        empty.setText("You Don't have any Notification");
////    } else {
////        View b = findViewById(R.id.empty);
////        b.setVisibility(View.GONE);
////
////    }
////}
////else
////{
////    if (gridLayoutManager.getItemCount() == 0 && arr.size() == 0) {
////        progressDialog.dismiss();
////        empty = (TextView) findViewById(R.id.empty);
////        empty.setText("You Don't have any Notification");
////    }
////    else {
////        progressDialog.dismiss();
////
////        View b = findViewById(R.id.empty);
////        b.setVisibility(View.GONE);
////
////    }
////    if(arr.size()!=0) {
////        progressDialog.dismiss();
////
////        NotificationAdapter notificationAdapter = new NotificationAdapter(Notification.this, arr);
////        recyclerView.setAdapter(notificationAdapter);
////    }
//}

    }




    @Override
    public void onBackPressed() {
        Intent i =new Intent(Notification.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}

