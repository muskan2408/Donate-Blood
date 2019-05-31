//package com.muskankataria2408.blooddonationapp;
//
//import android.content.Intent;
//import android.util.Log;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
//public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
//      private static final String REG_TOKEN="REG_TOKEN";
////    @Override
////    public void onTokenRefresh() {
////        String recent_token=FirebaseInstanceId.getInstance().getToken();
////
////        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Notification");
////        reference.child("token").setValue(recent_token);
////        Log.d(REG_TOKEN,recent_token);
//////        sendRegistrationToServer(recent_token);
////
////}
//    @Override
//    public void onTokenRefresh() {
//        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);
//    }
//}

package com.muskankataria2408.blooddonationapp;

import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Jarvis on 15-07-2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("FirebaseInstanceService", "Firebase New Token: " + refreshedToken);
        //addfirebasetoken(refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }


    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.




    }
}