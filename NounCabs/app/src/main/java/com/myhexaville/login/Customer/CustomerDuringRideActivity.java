package com.myhexaville.login.Customer;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myhexaville.login.CabHiring;
import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.Driver.DriverActivity;
import com.myhexaville.login.Driver.DriverDuringRideActivity;
import com.myhexaville.login.Manifest;
import com.myhexaville.login.R;
import com.myhexaville.login.RideRequests;
import com.myhexaville.login.Rides;
import com.myhexaville.login.WebViewMaps;

public class CustomerDuringRideActivity extends AppCompatActivity {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db,dbRead;
    private TextView tvFare;
    private Button btStopRide;
    private Rides rides;
    private Button btSendSmsEmergency;
    private String data;
    private static final String TAG = "TAG" ;
    private WebView wvMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInternetPermissions();
        setContentView(R.layout.activity_customer_during_ride);
        openHelper = new DatabaseHelper(this);
        db=openHelper.getWritableDatabase();
        dbRead=openHelper.getReadableDatabase();
        btStopRide=findViewById(R.id.bt_stop_ride_customer);
        btSendSmsEmergency=findViewById(R.id.bt_send_sms_emergency_during_ride);
        tvFare=findViewById(R.id.tv_riding_during_customer);
        btSendSmsEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defineEmergencyButton();

            }
        });
        wvMaps=findViewById(R.id.wv_maps_customer);

        rides=(Rides)getIntent().getSerializableExtra("RidesObject");

        String PickUpLatitude=Double.toString(getIntent().getDoubleExtra("PickUpLatitude",0.0));
        String PickUpLongitude=Double.toString(getIntent().getDoubleExtra("PickUpLongitude",0.0));
        String DropLatitude=Double.toString(getIntent().getDoubleExtra("DropLatitude",0.0));
        String DropLongitude=Double.toString(getIntent().getDoubleExtra("DropLongitude",0.0));
        Log.d(TAG," pickup point "+PickUpLatitude+" , "+PickUpLongitude+" and DROP point"+ DropLatitude+" , "+DropLongitude);

        WebSettings webSettings = wvMaps.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebViewMaps webViewClient = new WebViewMaps(this);
        wvMaps.setWebViewClient(webViewClient);
        wvMaps.loadUrl("https://www.google.com/maps/dir/"+PickUpLatitude+"%C2%B0+N,+"+PickUpLongitude+"%C2%B0+E/"+DropLatitude+"%C2%B0+N,+"+DropLongitude+"%C2%B0+E/data=!3m1!4b1!4m10!4m9!1m3!2m2!1d78.4867!2d17.385!1m3!2m2!1d80.2707!2d13.0827!3e0");


        checkFilePermissions();

        btStopRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defineFirstClick();
            }
        });


    }

    private void defineFirstClick(){
        tvFare.setText(" Estimated Fare = Rs. "+rides.getFare());
        btStopRide.setText(" Go To the Home Page");
        //pushing values to the ride history of that driver

        insertValuesToRidesTable(rides.getPickupPoint(),rides.getDropPoint(),rides.getDistance(),rides.getOtp(),rides.getTimeStamp(),rides.getFare(),rides.getDriverPhoneNumber(),rides.getCustomerPhoneNumber());
        btStopRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defineSecondClick();

            }
        });



    }
    private void insertValuesToRidesTable(String pickupPoint,String dropPoint, String distance,String otp,String timeStamp,String fare,String driverPhoneNumber, String customerPhoneNumber){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_52, pickupPoint);
        contentValues.put(DatabaseHelper.COL_53, dropPoint);
        contentValues.put(DatabaseHelper.COL_54, distance);
        contentValues.put(DatabaseHelper.COL_55, otp);
        contentValues.put(DatabaseHelper.COL_56, timeStamp);
        contentValues.put(DatabaseHelper.COL_57, fare);
        contentValues.put(DatabaseHelper.COL_58,driverPhoneNumber);
        contentValues.put(DatabaseHelper.COL_59,customerPhoneNumber);
        long id = db.insert(DatabaseHelper.TABLE_NAME_5, null, contentValues);
        Toast.makeText(this," Values inserted in the rides table ",Toast.LENGTH_SHORT).show();

    }
    private void defineSecondClick(){
        Intent i=new Intent(CustomerDuringRideActivity.this,CustomerActivity.class);
        startActivity(i);
    }
    private void defineEmergencyButton(){
        data=((CabHiring) this.getApplication()).getPhoneNumber();
        //openWhatsAppNew();
        Cursor cursor=dbRead.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_6 + " WHERE " + DatabaseHelper.COL_61 + "=? ", new String[]{data});
        if(cursor.getCount()==0){
            Toast.makeText(this,"Please Add Emergency Contacts",Toast.LENGTH_SHORT).show();

        }else{

            if(cursor.moveToFirst()){


                String phoneNumber1=cursor.getString(cursor.getColumnIndex("emergencyPhoneNumber"));
                Log.d(TAG,"phone number sending sms to "+phoneNumber1);

                sendSms(phoneNumber1);

                if(cursor.moveToNext()){

                    String phoneNumber2=cursor.getString(cursor.getColumnIndex("emergencyPhoneNumber"));
                    Log.d(TAG,"phone number sending sms to "+phoneNumber2);
                    sendSms(phoneNumber2);

                    Toast.makeText(this," Message Successfully Sent ",Toast.LENGTH_SHORT).show();

                }
            }

        }



    }
    private void sendSms(String phoneNumber){
        String messageToSend = "I need help. DRIVER: "+rides.getDriverPhoneNumber();
        String number = "+91"+phoneNumber;

        SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);
        Toast.makeText(this," Message sent by Sms Manager ",Toast.LENGTH_SHORT).show();

    }
    private void checkFilePermissions() {
        if (ContextCompat.checkSelfPermission(CustomerDuringRideActivity.this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.SEND_SMS");

            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{android.Manifest.permission.SEND_SMS}, 1001); //Any number
            }
        }else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
    private void checkInternetPermissions(){
        if (ContextCompat.checkSelfPermission(CustomerDuringRideActivity.this, android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.INTERNET");

            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{android.Manifest.permission.INTERNET}, 1001); //Any number
            }
        }else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

}
