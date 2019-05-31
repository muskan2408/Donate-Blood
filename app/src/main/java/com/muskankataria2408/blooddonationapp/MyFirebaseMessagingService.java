//package com.muskankataria2408.blooddonationapp;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.google.android.gms.gcm.GcmListenerService;
//import com.muskankataria2408.blooddonationapp.NotificationFragment;
//
//public class MyFirebaseMessagingService extends GcmListenerService {
//
//    private static final String TAG = "MyGcmListenerService";
//
//    /**
//     * Called when message is received.
//     *
//     * @param from SenderID of the sender.
//     * @param data Data bundle containing message data as key/value pairs.
//     *             For Set of keys use data.keySet().
//     */
//    // [START receive_message]
//    @Override
//    public void onMessageReceived(String from, Bundle data) {
////        String message = data.getString("message");
////        Log.d(TAG, "From: " + from);
////        Log.d(TAG, "Message: " + message);
////
////        if (from.startsWith("/topics/")) {
////            // message received from some topic.
////        } else {
////            // normal downstream message.
////        }
////
////        // [START_EXCLUDE]
////        /**
////         * Production applications would usually process the message here.
////         * Eg: - Syncing with server.
////         *     - Store message in local database.
////         *     - Update UI.
////         */
////
////        /**
////         * In some cases it may be useful to show a notification indicating to the user
////         * that a message was received.
////         */
////        sendNotification(message);
////        // [END_EXCLUDE]
//
//    }
//    // [END receive_message]
//
//    /**
//     * Create and show a simple notification containing the received GCM message.
//     *
//     * @param message GCM message received.
//     */
//    private void sendNotification(String message) {
//        Intent intent = new Intent(this, NotificationFragment.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("GCM Message")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
//}


package com.muskankataria2408.blooddonationapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Jarvis on 15-07-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private DatabaseReference mUserDatabse;
    JSONObject jsonObj;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FirebasemessageService", "FROM:" + remoteMessage.getFrom());
        //Check if the message contains data
        if(remoteMessage.getData().size() > 0) {
            Log.d("FirebasemessageService", "Message data: " + remoteMessage.getData());
        }
        //Check if the message contains notification
        if(remoteMessage.getNotification() != null) {
            Log.d("FirebasemessageService", "Message body:" + remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),remoteMessage.getData());
        }
    }

//    @Override
//    public final void handleIntent(Intent intent) {
//
//        this.onMessageReceived(builder.build());
//
//    }

    private void sendNotification(String body, String title, Map<String, String> data) {

        Intent intent=new Intent(MyFirebaseMessagingService.this,Notification.class);
        Log.d("data", String.valueOf(data));
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Gson gsonObj = new Gson();
        String jsonStr = gsonObj.toJson(data);
        intent.putExtra("content", jsonStr);
//        if (data.containsKey("click_action")) {
//            ClickActionHelper.startActivity(data.get("click_action"), jsonStr, this);
//        }
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String id = null;
        try {
            id = jsonObj.get("UserId").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Requests").child(uid);
        mUserDatabse.push().setValue(id);

        final String finalId = id;
//        mUserDatabse.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // int a= (int) dataSnapshot.getChildrenCount();
//                mUserDatabse.push().setValue(finalId);
//
//                }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
        intent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        long[] v = {500,1000, 500, 1000};

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.blood))
                .setSmallIcon(R.mipmap.blood)
                .setColor(getResources().getColor(R.color.transperent))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setSound(notificationSound)
                .setVibrate(v)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001 , notifiBuilder.build());
//        Intent i=new Intent(MyFirebaseMessagingService.this,NotificationFragment.class);
//        startActivity(i);
    }
}




