package com.myhexaville.login.Customer;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.myhexaville.login.CabHiring;
import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.R;
import com.myhexaville.login.RideRequests;
import com.myhexaville.login.Rides;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by User on 10/2/2017.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{


    private Button btRideNow;
    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db;
    private String pickupPoint;
    private String dropPoint;
    private String distance;
    private String otp;
    private String timeStamp;
    private String fare;
    private String customerPhoneNumber;
    private ProgressDialog progress;

    SQLiteOpenHelper openHelper;
    SQLiteDatabase dbRead,dbWrite;
    Cursor cursor;

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    //widgets
    private AutoCompleteTextView mSearchTextDrop;
    private AutoCompleteTextView mSearchTextPickUp;


    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapterDrop mPlaceAutocompleteAdapterDrop;
    private PlaceAutocompleteAdapterPickUp mPlaceAutocompleteAdapterPickUp;
    private GoogleApiClient mGoogleApiClient;
    private PlacesInfo mPlace;
    private Marker mMarker;
    private ImageView mGps,mInfo;
    private Location pickup,drop;




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);
        mSearchTextDrop =  findViewById(R.id.input_search);
        mSearchTextPickUp=findViewById(R.id.input_search_pickup);
        mGps =  findViewById(R.id.ic_gps);
        btRideNow=findViewById(R.id.bt_ride_now_customer);
        db=FirebaseFirestore.getInstance();
        customerPhoneNumber=((CabHiring)this.getApplication()).getPhoneNumber();
        databaseResponding();
        btRideNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rideNowButton();
            }
        });
        getLocationPermission();




    }
    private void rideNowButton(){




        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");
        String formattedTime = tf.format(c.getTime());
        pickupPoint=mSearchTextPickUp.getText().toString();
        dropPoint= mSearchTextDrop.getText().toString();

        //hardCoding
        //double distanceValue = (pickup.distanceTo(drop))* 0.000621371 ;
        if(pickup==null){
            Log.d(TAG,"pickup is null ERROR");
        }
        float[] results=new float[3];
        Location.distanceBetween(pickup.getLatitude(),pickup.getLongitude(),drop.getLatitude(),drop.getLongitude(),results);

        distance= String.format("%d",(int)(results[0]/1000));
        Random r = new Random();
        int i = r.nextInt(9999 - 1000) + 1000;
        otp=""+i;
        timeStamp=formattedTime+" "+formattedDate;
        fare=""+fare(Integer.parseInt(distance));
        Log.d(TAG," the value of fare is ######################## "+fare);




        //checking the fare


        openHelper=new DatabaseHelper(this);
        dbRead=openHelper.getReadableDatabase();
        dbWrite=openHelper.getWritableDatabase();
        String data=((CabHiring) this.getApplication()).getPhoneNumber();
        cursor=dbRead.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_3 + " WHERE " + DatabaseHelper.COL_31 + "=? ", new String[]{data});

        if(cursor.moveToFirst()){

            do{

                String wallet=cursor.getString(cursor.getColumnIndex("wallet"));
                int WalletValue=Integer.parseInt(wallet);
                if(Integer.parseInt(fare)>WalletValue){
                    //insufficient funds
                    Toast.makeText(this,"Insufficient Cash in the Wallet",Toast.LENGTH_SHORT).show();

                }
                else{
                    int newValue=WalletValue-Integer.parseInt(fare);
                    decrementingWallet(""+newValue);
                    sufficientFunds();

                }

            } while(cursor.moveToNext());
        }
        cursor.close();




    }
    public int fare(int dist)  {
        int fare;
        if(dist>5){
            fare=dist-5;
            fare*=15;
            fare+=40;
        }
        else{
            fare =40;
        }

        return fare;
    }
    private void sufficientFunds(){

        RideRequests rideRequests=new RideRequests(pickupPoint,dropPoint,distance,otp,timeStamp,fare,customerPhoneNumber);

        db.collection("RideRequests").document(timeStamp+" "+distance).set(rideRequests);
        progress = new ProgressDialog(this);
        progress.setTitle("Searching for Drivers");
        progress.setMessage("Please Wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    private void decrementingWallet(String newValue){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_32, newValue);
        String data=((CabHiring) this.getApplication()).getPhoneNumber();
        dbWrite.update(DatabaseHelper.TABLE_NAME_3,contentValues,DatabaseHelper.COL_31+" =? ",new String[]{data});

    }

    private void databaseResponding(){
        db.collection("Rides")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    Rides rides=dc.getDocument().toObject(Rides.class);
                                    if(customerPhoneNumber.equals(rides.getCustomerPhoneNumber()))
                                        passingRidesObject(rides);

                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });
    }
    private void passingRidesObject(Rides rides){
        if(progress!=null) {
            progress.dismiss();
            Intent intent = new Intent(MapsActivity.this, CustomerDuringRideActivity.class);
            intent.putExtra("RidesObject", rides);
            Log.d(TAG," Pickup and drop "+pickup.getLatitude());
            intent.putExtra("PickUpLatitude",pickup.getLatitude());
            intent.putExtra("PickUpLongitude",pickup.getLongitude());
            intent.putExtra("DropLatitude",drop.getLatitude());
            intent.putExtra("DropLongitude",drop.getLongitude());


            startActivity(intent);

        }


    }


    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchTextDrop.setOnItemClickListener(mAutocompleteClickListenerDrop);
        mSearchTextPickUp.setOnItemClickListener(mAutocompleteClickListenerPickUp);

        mPlaceAutocompleteAdapterDrop = new PlaceAutocompleteAdapterDrop(this, mGoogleApiClient,LAT_LNG_BOUNDS, null);
        mPlaceAutocompleteAdapterPickUp =new PlaceAutocompleteAdapterPickUp(this,mGoogleApiClient,LAT_LNG_BOUNDS,null);

        mSearchTextDrop.setAdapter(mPlaceAutocompleteAdapterDrop);
        mSearchTextPickUp.setAdapter(mPlaceAutocompleteAdapterPickUp);

        mSearchTextDrop.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocateDrop();
                }

                return false;
            }
        });
        mSearchTextPickUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocatePickup();
                }

                return false;
            }
        });









        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void geoLocatePickup(){
        Log.d(TAG, "geoLocateDrop: geolocating");


        String searchStringPickup=mSearchTextPickUp.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchStringPickup, 1);

        }catch (IOException e){
            Log.e(TAG, "geoLocateDrop: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocatePickup: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));


        }
    }






    private void geoLocateDrop(){
        Log.d(TAG, "geoLocateDrop: geolocating");

        String searchString = mSearchTextDrop.getText().toString();


        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocateDrop: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocateDrop: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));




        }
    }

    private void settingPickup(LatLng latLng){
        pickup = new Location("Delhi");
        pickup.setLatitude(latLng.latitude);
        pickup.setLongitude(latLng.longitude);
        Log.d(TAG,"THE VALUE OF PICKUP IS "+pickup.getLatitude()+" and "+pickup.getLongitude());
    }
    private void settingDrop(LatLng latLng){
        drop = new Location("Chandigarh");
        drop.setLatitude(latLng.latitude);
        drop.setLongitude(latLng.longitude);
        Log.d(TAG,"THE VALUE OF DROP IS "+drop.getLatitude()+" and "+drop.getLongitude());

    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
//
//    private void moveCamera(LatLng latLng, float zoom, PlacesInfo placeInfo){
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//
//        mMap.clear();
//
//        if(placeInfo != null){
//            try{
//                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
//                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
//                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
//                        "Price Rating: " + placeInfo.getRating() + "\n";
//
//                MarkerOptions options = new MarkerOptions()
//                        .position(latLng)
//                        .title(placeInfo.getName())
//                        .snippet(snippet);
//                mMarker = mMap.addMarker(options);
//
//            }catch (NullPointerException e){
//                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
//            }
//        }else{
//            mMap.addMarker(new MarkerOptions().position(latLng));
//        }
//
//        hideSoftKeyboard();
//
//    }




    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
               android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListenerDrop = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapterDrop.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackDrop);
        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListenerPickUp = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapterPickUp.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackPickUp);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallbackDrop = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlacesInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                //passing it to global

                settingDrop(place.getLatLng());


                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());

            places.release();
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallbackPickUp = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlacesInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());


                settingPickup(place.getLatLng());


                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());

            places.release();
        }
    };

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.addMarker(new MarkerOptions().position(chandigarh).title("Chandigarh"));
//        mMap.addMarker(new MarkerOptions().position(delhi).title("Delhi"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
//
//        String str_origin = "origin=" + delhi.latitude + "," + delhi.longitude;
//        String str_dest = "destination=" + chandigarh.latitude + "," + chandigarh.longitude;
//        String sensor = "sensor=false";
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//        String output = "json";
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        Log.d("onMapClick", url.toString());
//        FetchUrl FetchUrl = new FetchUrl();
//        FetchUrl.execute(url);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(7));
//
//        Location delhi_location = new Location("Delhi");
//        delhi_location.setLatitude(delhi.latitude);
//        delhi_location.setLongitude(delhi.longitude);
//
//        Location chandigarh_location = new Location("Chandigarh");
//        chandigarh_location.setLatitude(chandigarh.latitude);
//        chandigarh_location.setLongitude(chandigarh.longitude);
//
//        double distance = (delhi_location.distanceTo(chandigarh_location))* 0.000621371 ;
//
//        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
//        alertDialog.setTitle("Info");
//        alertDialog.setMessage("Distance between these two location is : "+distance +" miles");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
//    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                JSONParserTask parser = new JSONParserTask();
                Log.d("ParserTask", parser.toString());
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }


}
