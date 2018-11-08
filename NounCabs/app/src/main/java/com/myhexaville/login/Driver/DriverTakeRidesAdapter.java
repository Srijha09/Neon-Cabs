package com.myhexaville.login.Driver;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myhexaville.login.CabHiring;
import com.myhexaville.login.R;
import com.myhexaville.login.RideRequests;
import com.myhexaville.login.Rides;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class DriverTakeRidesAdapter extends RecyclerView.Adapter<DriverTakeRidesAdapter.MyViewHolder> {

        private ArrayList<RideRequests> rideRequestsList;

        private DriverTakeRidesFragment context;
    private FirebaseFirestore db;

    public DriverTakeRidesAdapter(ArrayList<RideRequests> rideRequestsList, DriverTakeRidesFragment context) {
        this.rideRequestsList = rideRequestsList;
        this.context = context;
    }

    //This method inflates view present in the RecyclerView
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.card_driver_take_rides, parent, false);
            MyViewHolder holder=new MyViewHolder(view);
            return holder;
        }

        //Binding the data using get() method of POJO object
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.tvPickup.setText(rideRequestsList.get(position).getPickupPoint());
            holder.tvDrop.setText(rideRequestsList.get(position).getDropPoint());
            holder.tvEstFare.setText("Rs. "+rideRequestsList.get(position).getFare());
            holder.tvDist.setText(rideRequestsList.get(position).getDistance());
        }



        @Override
        public int getItemCount() {
        if(rideRequestsList==null){
            return 0;
        }
            return rideRequestsList.size();
        }


        //View holder class, where all view components are defined
        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

            public TextView tvPickup;
            public TextView tvDrop;
            public TextView tvEstFare;
            public TextView tvDist;
            public CircleImageView civAccept;
            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnLongClickListener(this);
                tvPickup=(TextView)itemView.findViewById(R.id.tv_pickup_take_rides_driver);
                tvDrop=(TextView)itemView.findViewById(R.id.tv_drop_take_rides_driver);
                tvEstFare=(TextView)itemView.findViewById(R.id.tv_estFare_take_rides_driver);
                tvDist=(TextView)itemView.findViewById(R.id.tv_dist_take_rides_driver);
                civAccept=(CircleImageView)itemView.findViewById(R.id.iv_accept_take_rides_driver);

            }


            @Override
            public boolean onLongClick(View v) {
                db= FirebaseFirestore.getInstance();
                addingToTheRides(getAdapterPosition());
                deletingRequest(getAdapterPosition());
                moveToDuringRideActivity(getAdapterPosition());
                return false;
            }
            public void addingToTheRides(int position){
                RideRequests temp=rideRequestsList.get(position);
                String data = ((CabHiring)context.getActivity().getApplication()).getPhoneNumber();

                Rides rides=new Rides(temp.getPickupPoint(),temp.getDropPoint(),temp.getDistance(),temp.getOtp(),temp.getTimeStamp(),temp.getFare(),temp.getCustomerPhoneNumber(),data);



                db.collection("Rides").document(rides.getTimeStamp()+" "+rides.getOtp()).set(rides);



            }
            public void deletingRequest(int position){



                    //deleting the document from the database
                    Log.d(TAG, "6TH ENTRY JUST BEFORE DELETING WITH DOCUMENT ID "+rideRequestsList.get(position));

                    db.collection("RideRequests").document(rideRequestsList.get(position).getTimeStamp()+" "+rideRequestsList.get(position).getDistance())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DELETED DOCUMENT SUCCESSFULLY");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                    return;


            }
            public void moveToDuringRideActivity(int position){
                //push to your rides
                Intent i = new Intent(context.getActivity(), DriverDuringRideActivity.class);
                i.putExtra("RequestObject",rideRequestsList.get(position));


                context.startActivity(i);
                ((Activity) context.getActivity()).overridePendingTransition(0,0);


            }
        }


    }


