package com.muskankataria2408.blooddonationapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.muskankataria2408.blooddonationapp.models.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    String setname ,setmobile, setbloodgroup, sethospitaladress,setlandmark;

    private List<String> values;
    private ArrayList<Model> a;
    private Context context;
    private Button details;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private String TAG="NotificationAdapter";

    public NotificationAdapter(Context context,List<String> values,ArrayList<Model> a) {

            this.context=context;
            this.values=values;
            this.a=a;

    }

    public NotificationAdapter(Context context, ArrayList<Model> a) {
        this.context=context;
        this.a=a;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_cards,parent,false);
       NotificationViewHolder notificationViewHolder=new NotificationViewHolder(itemView);
            return notificationViewHolder;

       // }


    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, final int position) {


        holder.name.setText("Requested By "+a.get(position).getName());
        holder.bloodgroup.setText("Blood Group "+a.get(position).getBloodGroup());
        holder.mobileno.setText("Mobile No. "+a.get(position).getMobile());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(view.getContext(),RequestProfile.class);
                i.putExtra("Name",a.get(position).getName());
                i.putExtra("Bloodgroup",a.get(position).getBloodGroup());
                i.putExtra("mobile",a.get(position).getMobile());
                i.putExtra("HospitalAddress",a.get(position).getHospitalAdress());
                i.putExtra("Landmark",a.get(position).getLandMark());
                view.getContext().startActivity(i);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete this Request")
                        .setMessage("Are you sure you want to delete this request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              final ProgressDialog progressDialog=new ProgressDialog(context);
                                progressDialog.setMessage("Deleting Request");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String current_user_id = mCurrentUser.getUid();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Requests").child(current_user_id);
                                //.orderByValue().equalTo(a.get(position).getUserid()).getRef();;

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       for(DataSnapshot snap: dataSnapshot.getChildren())
                                       {
                                           if(snap.getValue().equals(a.get(position).getUserid())){
                                               snap.getRef().removeValue();
                                               holder.cardView.setVisibility(View.GONE);
                                           }
                                       }

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {
                                       progressDialog.cancel();
                                       Toast.makeText(context,"Cannot Delete Request",Toast.LENGTH_LONG).show();
                                   }
                               });
                                progressDialog.cancel();



                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });


    }
//    public int getCount() {
//        return values.size();
//    }

    @Override
    public int getItemCount() {
        return a.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView name,bloodgroup,mobileno;
        Button button,delete;
        CardView cardView;
        public NotificationViewHolder(View itemView) {
            super(itemView);
//            mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Notification").child(a.get(i));
//            mUserDatabase.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    setname=dataSnapshot.child("name").getValue().toString();
//                    setmobile =dataSnapshot.child("mobile").getValue().toString();
//                    setbloodgroup=dataSnapshot.child("bloodgroup").getValue().toString();
//                    sethospitaladress=dataSnapshot.child("hospital").getValue().toString();
//                    setlandmark=dataSnapshot.child("landmark").getValue().toString();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });

            cardView=(CardView)itemView.findViewById(R.id.Notification_card);
            name=(TextView)itemView.findViewById(R.id.name);
            bloodgroup=(TextView)itemView.findViewById(R.id.bloodgroup);
            mobileno=(TextView)itemView.findViewById(R.id.mobile);
            button=(Button)itemView.findViewById(R.id.details);
            delete=(Button)itemView.findViewById(R.id.delete);
           // i++;
        }
    }
}