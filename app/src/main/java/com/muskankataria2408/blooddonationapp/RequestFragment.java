package com.muskankataria2408.blooddonationapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.muskankataria2408.blooddonationapp.models.PlaceInfo;
import com.muskankataria2408.blooddonationapp.utils.SessionManagement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SNIMatcher;

import static android.support.constraint.Constraints.TAG;


public class RequestFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseUser mCurrentUser;
    private AutoCompleteTextView mSearchText;
    SessionManagement session;
    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    Button send;
    private TextInputEditText name,mobileno,landmark;
    private Spinner bloodgroup;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    private FirebaseFunctions mFunctions;
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    Address address;

   ArrayList<String> array=new ArrayList<>();
    String Sname,Smobile,Sbloodgroup,device_token,ShospitalAddress,searchString;

    private static final String TAG="RequestFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mNotificationReference;
    private static final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));
    String current_user_id;
    String mobile;
//    private String user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();

    public RequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(),  this)
                .build();

            View view=inflater.inflate(R.layout.fragment_request, container, false);
         send=(Button)view.findViewById(R.id.send);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        setHasOptionsMenu(true);
        session=new SessionManagement(getActivity());
        name=(TextInputEditText)view.findViewById(R.id.fullname);
        mobileno=(TextInputEditText)view.findViewById(R.id.mobileno);
        bloodgroup=(Spinner)view.findViewById(R.id.qual_spinner);
        landmark=(TextInputEditText)view.findViewById(R.id.landmark);
        mSearchText=(AutoCompleteTextView) view.findViewById(R.id.input_search);
        mNotificationReference= FirebaseDatabase.getInstance().getReference("Notification");
        mFunctions = FirebaseFunctions.getInstance();
        mAuth= FirebaseAuth.getInstance();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();

        current_user_id=mCurrentUser.getUid();

        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
        mUserDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                Sname=dataSnapshot.child("name").getValue().toString();
                Smobile =dataSnapshot.child("mobile").getValue().toString();
                Sbloodgroup=dataSnapshot.child("BloodGroup").getValue().toString();
                device_token=dataSnapshot.child("device_token").getValue().toString();
                name.setText(Sname.toUpperCase());
                mobileno.setText(Smobile);
                searchString=mSearchText.getText().toString();
                bloodgroup.setSelection(getIndex(bloodgroup,Sbloodgroup));
                mSearchText.setOnItemClickListener(mAutoCompleteListener);
                mplaceAutocompleteAdapter =new PlaceAutocompleteAdapter(getActivity(),
                        Places.getGeoDataClient(getActivity(),null),LAT_LNG_BOUNDS,null);
                mSearchText.setAdapter(mplaceAutocompleteAdapter);


                ShospitalAddress=mSearchText.getEditableText().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mplaceAutocompleteAdapter =new PlaceAutocompleteAdapter(getActivity(),
                Places.getGeoDataClient(getActivity(),null),LAT_LNG_BOUNDS,null);
         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                // Toast.makeText(getActivity(),ShospitalAddress,Toast.LENGTH_SHORT).show();
                 progressDialog.setTitle("Sending Request...");
               //  progressDialog.setMessage("Please wait while we creating your account");
                 progressDialog.setCancelable(false);
                 progressDialog.show();
                 ShospitalAddress=mSearchText.getEditableText().toString();
                 sendRequest(device_token,Sname,Smobile,landmark.getEditableText().toString(),Sbloodgroup,ShospitalAddress);

             }
         });

        return view;
    }

    private void sendRequest(final String device_token, String Sname, String Smobile, String landmark, String Sbloodgroup, String ShospitalAddress){

        if(Sname.isEmpty()){
           name.setError("Name is Required");
            name.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(Smobile.isEmpty())
        {
            mobileno.setError("Mobile is Required");
            mobileno.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(landmark.isEmpty())
        {
            this.landmark.setError("Landmark is Required");
            this.landmark.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if(ShospitalAddress.isEmpty())
        {
            mSearchText.setError("Address is Required");
            mSearchText.requestFocus();
            progressDialog.dismiss();
            return;
        }

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String,String>  notificationData=new HashMap<>();
        notificationData.put("token" , device_token);
        notificationData.put("name", Sname);
        notificationData.put("mobile",Smobile);
        notificationData.put("bloodgroup",Sbloodgroup);
        notificationData.put("hospital",ShospitalAddress);
        notificationData.put("landmark",landmark);
        mNotificationReference.child(mCurrentUser.getUid()).setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Requested Successfully!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);

        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if(item.getItemId()==R.id.main_logout_btn)
        {
            FirebaseAuth.getInstance().signOut();
            session.logoutUser();
            Intent startIntent=new Intent(getActivity(), LoginActivity.class) ;
            startActivity(startIntent);
            getActivity().finish();
        }


        if(item.getItemId()==R.id.main_rate)
        {
            final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        if(item.getItemId()==R.id.main_share)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=com.muskankataria2408.blooddonationapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        if(item.getItemId()==R.id.main_profile)
        {
            Intent startIntent=new Intent(getActivity(), MyProfile.class) ;
            startIntent.putExtra("mobile",mobile);
            startActivity(startIntent);
            getActivity().finish();
        }

        if(item.getItemId()==R.id.main_notification)
        {
            Intent i=new Intent(getActivity(),Notification.class);
            startActivity(i);
            //getActivity().finish();

        }


        return true;
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

    private Task<String> sendNotification(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }


    private ResultCallback<? super PlaceBuffer> mUpdatePlaceDetailsCallback=new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess())
            {
                Log.d(TAG,"places query did not complete successfully "+places.getStatus().toString());
                places.release();
                return;
            }
            final Place place=places.get(0);
            try{
                mPlace=new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                //   mPlace.setAttributions(place.getName().toString());
                mPlace.setId(place.getId().toString());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                mPlace.setRating(place.getRating());
                Log.d(TAG,"OnResult: place details"+mPlace.toString());

            }catch(NullPointerException e)
            {
                Log.e(TAG,"OnResult: NullPointerException"+e.getMessage());
            }
//            moveCamera(new LatLng(place.getViewport().getCenter().latitude,place.getViewport().getCenter().longitude),
//                    DEFAULT_ZOOM,mPlace);
//            Log.d(TAG,"OnResult:place details: "+place.getAttributions());
//            Log.d(TAG,"OnResult:place details: "+place.getViewport());
//            Log.d(TAG,"OnResult:place details: "+place.getAddress());
//            Log.d(TAG,"OnResult:place details: "+place.getPhoneNumber());
//            Log.d(TAG,"OnResult:place details: "+place.getLatLng());
//            Log.d(TAG,"OnResult:place details: "+place.getId());
//            Log.d(TAG,"OnResult:place details: "+place.getWebsiteUri());
//            Log.d(TAG,"OnResult:place details: "+place.getRating());
            places.release();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private AdapterView.OnItemClickListener mAutoCompleteListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // hideSoftKeyboard();
            final AutocompletePrediction item=mplaceAutocompleteAdapter.getItem(i);
            final String placeId=item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult=Places.GeoDataApi.getPlaceById(mGoogleApiClient,placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

}
