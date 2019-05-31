package com.muskankataria2408.blooddonationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muskankataria2408.blooddonationapp.utils.SessionManagement;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=6000;
    Context context;
    boolean gps_enabled=false;
   private FirebaseAuth mAuth;
    SessionManagement session;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS=1;
    private static final String TAG="SplashActivity";


//    public SplashScreen(Context context){
//        this.context=context;
//
//    }
//    public SplashScreen(){
//
//    }
//public void checkIntent(Intent intent) {
//    if (intent.hasExtra("click_action")) {
//        ClickActionHelper.startActivity(intent.getStringExtra("click_action"),, this);
//    }
//}
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        checkIntent(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        gps_enabled=checkLocation();
      //  checkIntent(getIntent());
        FirebaseApp.initializeApp(this);
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

//        if(getIntent().getExtras()!=null)
//        {
//            Intent i=new Intent(SplashScreen.this,Notification.class);
//
//            startActivity(i);
////            MyFirebaseMessagingService my=new MyFirebaseMessagingService();
////            my.onMessageReceived();
//        }
//        permissionManager=new PermissionManager() {};
//           permissionManager.checkAndRequestPermissions(this);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
//            dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(myIntent);
//                    //get gps
//                }
//            });
//            dialog.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
//            dialog.show();
        }

    }

//    private void sendToStart() {
//        Intent startIntent=new Intent(SplashScreen.this, LoginActivity.class) ;
//        startActivity(startIntent);
//        finish();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (!checkInternet()) {
//            new AlertDialog.Builder(SplashScreen.this)
//                    .setTitle("No Internet Connection")
//                    .setMessage("Your device is not connected to Internet")
//                    .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//
//                        }
//
//                    })
//                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
//                            System.exit(0);
//                        }
//                    })
//                    .show();
//
//
//        } else   if(!checkLocation()){
//            new AlertDialog.Builder(SplashScreen.this)
//                    .setTitle("Unable to Access Location")
//                    .setMessage("Turn on your location to move further")
//                    .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//
//                        }
//
//                    })
//                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
//                            System.exit(0);
//                        }
//                    })
//                    .show();
//
//
//        }else{
//            if(checkLocation()&& checkInternet()){
//            Splash();
//        }}
//    }
//
//    private boolean checkLocation() {
//        boolean gps_enabled = false;
////        if(context!=null) {
//
//            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//            boolean network_enabled = false;
//
//            try {
//                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            } catch (Exception ex) {
//            }
//
//            try {
//                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//            } catch (Exception ex) {
//            }
//
//            if (!gps_enabled && !network_enabled) {
//                // notify user
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
//                dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        // TODO Auto-generated method stub
//                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(myIntent);
//                        //get gps
//                    }
//                });
//                dialog.setNegativeButton(this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        // TODO Auto-generated method stub
//                        finish();
//                        System.exit(0);
//
//
//                    }
//                });
//                dialog.show();
//            }
//            Log.e("Check gps_enabled", String.valueOf(gps_enabled));
//
////        }
//        return gps_enabled;
//    }
//
//
//    private void Splash() {
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
//                startActivity(i);
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
//
//    }
//    public boolean checkInternet() {
//        boolean mobileNwInfo;
//        ConnectivityManager conxMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        try {
//            mobileNwInfo = conxMgr.getActiveNetworkInfo().isConnected();
//        } catch (NullPointerException e) {
//            mobileNwInfo = false;
//        }
//        return mobileNwInfo;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(!(currentUser==null))
//        {
//            Intent startIntent=new Intent(SplashScreen.this, LoginActivity.class) ;
//            startActivity(startIntent);
//            finish();
//            // sendToStart();
//        }
//
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions

                perms.put(FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (
                            perms.get(FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            ) {
                        Log.d(TAG, "location services permission granted");
                        // process the normal flow
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, FINE_LOCATION)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    private void explain(String msg){
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!checkInternet()) {
            new AlertDialog.Builder(SplashScreen.this)
                    .setTitle("No Internet Connection")
                    .setCancelable(false)
                    .setMessage("Your device is not connected to Internet")
                    .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                        }

                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .show();


        } else   if(!checkLocation()){
            new AlertDialog.Builder(SplashScreen.this)
                    .setTitle("Unable to Access Location")
                    .setCancelable(false)
                    .setMessage("Turn on your location to move further")
                    .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);

                        }

                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .show();


        }else if(checkLocation()&& checkInternet()){
            Splash();
        }
    }



    private boolean checkLocation() {
        boolean gps_enabled = false;
//        if(context!=null) {

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
//                dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        // TODO Auto-generated method stub
//                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(myIntent);
//                        //get gps
//                    }
//                });
//                dialog.setNegativeButton(this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        // TODO Auto-generated method stub
//                        finish();
//                        System.exit(0);
//
//
//                    }
//                });
//                dialog.show();
        }
        Log.e("Check gps_enabled", String.valueOf(gps_enabled));

//        }
        return gps_enabled;
    }
    private void Splash() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                session = new SessionManagement(getApplicationContext());
                // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

                if(!SplashScreen.this.session.isLoggedIn())
                {
                    Log.e("TAG", "execterd");
                    Intent i=new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(i);
                    finish();


                }
             else{
                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }


    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        super.onBackPressed();
    }

    public boolean checkInternet() {
        boolean mobileNwInfo;
        ConnectivityManager conxMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        try {
            mobileNwInfo = conxMgr.getActiveNetworkInfo().isConnected();
        } catch (NullPointerException e) {
            mobileNwInfo = false;
        }
        return mobileNwInfo;
    }



}
