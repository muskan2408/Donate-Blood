package com.muskankataria2408.blooddonationapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ActionBar toolbar;
    private static final String TAG="MainActivity";
    private static final int ERROR_DAILOG_REQUEST=9001;
    BottomNavigationView bottomNavigationView;
    private DatabaseReference mUserDatabse;
    String mobile;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = getSupportActionBar();
        mobile=getIntent().getStringExtra("mobile");
        bottomNavigationView = findViewById(R.id.navigation);
//        mUserDatabse= FirebaseDatabase.getInstance().getReference();
//        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
//        String current_user_id=mCurrentUser.getUid();
//        mUserDatabse.child("Users").child(current_user_id).child("mobile").setValue(mobile);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        toolbar.setTitle("Maps");
        if(isServiceOk()){
            loadFragment(new MapsFragment());
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch(item.getItemId()){
                        case R.id.navigation_maps:
                            //  Toast.makeText(MainActivity.this, "Hello profile", Toast.LENGTH_SHORT).show();
                            toolbar.setTitle("Maps");
                            fragment = new MapsFragment();
                            loadFragment(fragment);
                            return true;
                        // Toast.makeText(MainActivity.this, "Hello QR", Toast.LENGTH_SHORT).show();
                        case R.id.navigation_request_Blood:
                            // Toast.makeText(MainActivity.this, "Hello Maps", Toast.LENGTH_SHORT).show();
                            toolbar.setTitle("Request Blood");
                            fragment = new RequestFragment();
                            loadFragment(fragment);
                            return true;
                        case R.id.navigation_history:
                            toolbar.setTitle("Donation History");
                            fragment = new DonationHistoryFragment();
                            loadFragment(fragment);
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        int count=getSupportFragmentManager().getBackStackEntryCount();
        if(count==0)
        {
            super.onBackPressed();

        }
        else{
            getSupportFragmentManager().popBackStack();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle=new Bundle();
        bundle.putString("mobile",mobile);
// set Fragmentclass Arguments
        NotificationFragment fragobj = new NotificationFragment();
        RequestFragment req = new RequestFragment();
        DonationHistoryFragment donate=new DonationHistoryFragment();
        req.setArguments(bundle);
        donate.setArguments(bundle);
        fragobj.setArguments(bundle);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser==null)
//        {
//            Intent startIntent=new Intent(MainActivity.this, LoginActivity.class) ;
//            startActivity(startIntent);
//            finish();
//        }
//    }
    public boolean isServiceOk()
    {
        Log.d(TAG,"isServiceOk: checking google services version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS)
        {
            //everything is fine
            Log.d(TAG,"Google play services working properly");
            return true;
        }
        else
        {
            if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){

                Log.d(TAG,"isServiceOk: an error occured but we can fix it");
                Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DAILOG_REQUEST);
                dialog.show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "You can't connect to internet", Toast.LENGTH_SHORT).show();
            }
            return false;

        }
    }
}