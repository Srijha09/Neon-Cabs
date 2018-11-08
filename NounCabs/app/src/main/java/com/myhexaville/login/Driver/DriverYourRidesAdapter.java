package com.myhexaville.login.Driver;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhexaville.login.R;

import java.util.ArrayList;

public class DriverYourRidesAdapter extends RecyclerView.Adapter<DriverYourRidesAdapter.MyViewHolder> {

        private ArrayList<String> pickup;
        private ArrayList<String> drop;
        private ArrayList<String> time;
        private ArrayList<String> estFare;

        public DriverYourRidesFragment context;


        public DriverYourRidesAdapter(DriverYourRidesFragment context, ArrayList<String> pickup, ArrayList<String> drop, ArrayList<String> time, ArrayList<String> estFare){
            this.context=context;
            this.pickup=pickup;
            this.drop=drop;
            this.time=time;
            this.estFare=estFare;
        }


        //This method inflates view present in the RecyclerView
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.card_driver_your_rides, parent, false);
            MyViewHolder holder=new MyViewHolder(v);
            return holder;
        }

        //Binding the data using get() method of POJO object
        @Override
        public void onBindViewHolder(final DriverYourRidesAdapter.MyViewHolder holder, int position) {
            holder.tvPickup.setText(pickup.get(position));
            holder.tvDrop.setText(drop.get(position));
            holder.tvEstFare.setText("Rs. "+estFare.get(position));
            holder.tvTime.setText(time.get(position));

        }



        @Override
        public int getItemCount() {
            return pickup.size();
        }


        //View holder class, where all view components are defined
        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView tvPickup;
            public TextView tvDrop;
            public TextView tvEstFare;
            public TextView tvTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                tvPickup=(TextView)itemView.findViewById(R.id.tv_pickup_your_rides_driver);
                tvDrop=(TextView)itemView.findViewById(R.id.tv_drop_your_rides_driver);
                tvEstFare=(TextView)itemView.findViewById(R.id.tv_estFare_your_rides_driver);
                tvTime=(TextView)itemView.findViewById(R.id.tv_time_your_rides_driver);

            }

            @Override
            public void onClick(View v) {

            }

        }


    }



