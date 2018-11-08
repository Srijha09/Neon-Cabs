package com.myhexaville.login.Driver;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.myhexaville.login.R;
import com.myhexaville.login.RideRequests;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverTakeRidesFragment extends Fragment {
    private RecyclerView rv;
    DriverTakeRidesAdapter adapter;
    public ArrayList<RideRequests> rideRequestsList;
    private FirebaseFirestore db;




    public DriverTakeRidesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        database();

    }
    public void database(){

        db= FirebaseFirestore.getInstance();

        db.collection("RideRequests")
                .addSnapshotListener(new EventListener<QuerySnapshot>()
                {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        accessDetails(value);





                    }
                });

    }

    private void accessDetails(QuerySnapshot value)
    {
        rideRequestsList=new ArrayList<>();

        for (DocumentSnapshot document : value)
        {

            RideRequests rideRequests = document.toObject(RideRequests.class);
            //adding the documentid which is used in deletion
            Log.d(TAG, document.getId() + "  2ND ENTRY DOCUMENT DETAILS" + document.getData());

            //requestsDetails.add(swapRequesttemp);

            accessNames(rideRequests,value);
        }

            Log.d(TAG, " FOR LOOP NEVER ENTERED" );
        adapter=new DriverTakeRidesAdapter(rideRequestsList,DriverTakeRidesFragment.this);
        rv.setAdapter(adapter);

        return;

    }

    private void accessNames(RideRequests rideRequests,QuerySnapshot value)
    {
        rideRequestsList.add(rideRequests);

        if(rideRequestsList.size()==value.size()) {
            fillRequests();

        }
        return;

    }
    private void fillRequests()
    {




        Log.d(TAG, "4TH ENTRY parameters FROM THE LISTENER TO THE ADAPTER  ");

        adapter=new DriverTakeRidesAdapter(rideRequestsList,DriverTakeRidesFragment.this);
        rv.setAdapter(adapter);

        return;




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_driver_take_rides, container, false);
        rv=(RecyclerView)v.findViewById(R.id.rv_take_rides_driver);




        adapter=new DriverTakeRidesAdapter(rideRequestsList,DriverTakeRidesFragment.this);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);


        return v;
    }

}
