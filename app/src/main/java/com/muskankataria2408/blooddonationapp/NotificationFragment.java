package com.muskankataria2408.blooddonationapp;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.muskankataria2408.blooddonationapp.utils.SessionManagement;


public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SessionManagement session;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String mobile;


    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        // Inflate the layout for this fragment
//        ActionBar actionbar = getActivity().getActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
////        ActionBar actionbar = getSupportActionBar();
////        actionbar.setDisplayHomeAsUpEnabled(true);
//        getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        View view=inflater.inflate(R.layout.fragment_notification, container, false);
        setHasOptionsMenu(true);
        session=new SessionManagement(getActivity());
//        mobile=getArguments().getString("mobile");
        return view;
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
            Intent startIntent=new Intent(getActivity(), LoginActivity.class);
            session.logoutUser();
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
            Intent startIntent=new Intent(getActivity(), MyProfile.class);
            startIntent.putExtra("mobile",mobile);
            startActivity(startIntent);
            getActivity().finish();
        }


        return true;
    }

}
