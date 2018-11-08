package com.myhexaville.login.Driver;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.myhexaville.login.Customer.CustomerDuringRideActivity;
import com.myhexaville.login.R;
import com.myhexaville.login.login.MainActivity;

public class DriverActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean viewIsAtHome;
    private static final String TAG = DriverActivity.class.getSimpleName();



//    private static final String SQL_CREATE_ENTRIES =
//            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME_1 + " (" +
//                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
//                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
//                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";
//
//    private static final String SQL_DELETE_ENTRIES =
//            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME_1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_app_bar_driver);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_driver);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_driver);
        navigationView.setNavigationItemSelectedListener(this);
        displayView(R.id.nav_take_rides_driver);
//        checkFilePermissions();
    }

    @Override
    public void onBackPressed() {DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_driver);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displayView(R.id.nav_take_rides_driver); //display the News fragment
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_driver) {
            return true;
        }
        if(id==R.id.action_logout_driver){
            Intent intent=new Intent(DriverActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displayView(id);
//
//        if (id == R.id.nav_profile) {
//            // Handle the profile action
//            setContentView(R.layout.fragment_driver_profile);
//
//
//        } else if (id == R.id.nav_YourRides) {
//            setContentView(R.layout.fragment_driver_your_rides);
//
//        } else if (id == R.id.nav_MyReview) {
//
//        } else if (id == R.id.nav_wallet) {
//
//        } else if (id == R.id.nav_about) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_driver);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_take_rides_driver:
                fragment = new DriverTakeRidesFragment();
                title = "Take Rides";
                viewIsAtHome=true;
                break;
            case R.id.nav_profile_driver:
                fragment = new DriverProfileFragment();
                title  = "Profile";
                viewIsAtHome=false;

                break;
            case R.id.nav_your_rides_driver:
                fragment = new DriverYourRidesFragment();
                title = "Your Rides";
                viewIsAtHome=false;
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame_driver, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_driver);
        drawer.closeDrawer(GravityCompat.START);

    }
//    private void checkFilePermissions() {
//        if (ContextCompat.checkSelfPermission(DriverActivity.this, android.Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//        }
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
//        {
//            int permissionCheck = this.checkSelfPermission("Manifest.permission.SEND_SMS");
//
//            if (permissionCheck != 0) {
//
//                this.requestPermissions(new String[]{android.Manifest.permission.SEND_SMS}, 1001); //Any number
//            }
//        }else {
//            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
//        }
//    }
}
