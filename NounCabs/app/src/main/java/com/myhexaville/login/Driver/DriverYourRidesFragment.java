package com.myhexaville.login.Driver;


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
public class DriverYourRidesFragment extends Fragment {
    RecyclerView recyclerView;
    DriverYourRidesAdapter adapter;


    private ArrayList<String> pickup;
    private ArrayList<String> drop;
    private ArrayList<String> time;
    private ArrayList<String> estFare;
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Cursor cursor;



    public DriverYourRidesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_driver_your_rides, container, false);
        recyclerView=view.findViewById(R.id.rv_your_rides_driver);

        openHelper=new DatabaseHelper(getContext());
        db = openHelper.getReadableDatabase();

        pickup=new ArrayList<>();
        drop=new ArrayList<>();
        estFare=new ArrayList<>();
        time=new ArrayList<>();


        String data = ((CabHiring)getActivity().getApplication()).getPhoneNumber();

        cursor=db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_5 + " WHERE " + DatabaseHelper.COL_58 + "=? ", new String[]{data});
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

        adapter=new DriverYourRidesAdapter(DriverYourRidesFragment.this,pickup,drop,time,estFare);
        recyclerView.setAdapter(adapter);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        return view;
    }

}
