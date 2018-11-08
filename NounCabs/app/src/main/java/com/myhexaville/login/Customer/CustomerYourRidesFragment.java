package com.myhexaville.login.Customer;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myhexaville.login.CabHiring;
import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


    public class CustomerYourRidesFragment extends Fragment {
        private ArrayList<String> pickup;
        private ArrayList<String> drop;
        private ArrayList<String> time;
        private ArrayList<String> estFare;
        private CustomerYourRidesAdapter adapter;
        private RecyclerView recyclerView;

        SQLiteDatabase db;
        SQLiteOpenHelper openHelper;
        Cursor cursor;

        public CustomerYourRidesFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v= inflater.inflate(R.layout.fragment_customer_your_rides, container, false);
            recyclerView=v.findViewById(R.id.rv_your_rides_customer);

            openHelper=new DatabaseHelper(getContext());
            db = openHelper.getReadableDatabase();

            pickup=new ArrayList<>();
            drop=new ArrayList<>();
            estFare=new ArrayList<>();
            time=new ArrayList<>();


            String data = ((CabHiring)getActivity().getApplication()).getPhoneNumber();

            cursor=db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_5 + " WHERE " + DatabaseHelper.COL_59 + "=? ", new String[]{data});
            if(cursor.moveToFirst()){

                do{
                    String Pickup = cursor.getString(cursor.getColumnIndex("pickUpPoint"));
                    String Drop=cursor.getString(cursor.getColumnIndex("dropPoint"));
                    String EstFare=cursor.getString(cursor.getColumnIndex("fare"));
                    String Time=cursor.getString(cursor.getColumnIndex("timeStamp"));

                    pickup.add(Pickup);
                    drop.add(Drop);
                    estFare.add(EstFare);
                    time.add(Time);

                } while(cursor.moveToNext());
            }
            cursor.close();



            adapter=new CustomerYourRidesAdapter(CustomerYourRidesFragment.this,pickup,drop,time,estFare);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager llm=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(llm);


            return v;
        }


    }



