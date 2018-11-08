package com.myhexaville.login;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class CabHiring extends Application {
    private String PhoneNumber;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
