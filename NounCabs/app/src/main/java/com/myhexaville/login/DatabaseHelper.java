package com.myhexaville.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="NounCabsDatabase.db";

    //User table number 1
    public static final String TABLE_NAME_1 ="User";
    public static final String COL_11 ="phoneNumber";
    public static final String COL_12 ="password";
    public static final String COL_13="firstName";
    public static final String COL_14="lastName";
    public static final String COL_15="dob";
    public static final String COL_16="typeOfUser";

    //Driver table number 2
    public static final String TABLE_NAME_2 ="Driver";
    public static final String COL_21 ="driverPhoneNumber";
    public static final String COL_22 ="address";
    public static final String COL_23 ="licenseNumber";
    public static final String COL_24 ="availability";
    public static final String COL_25 ="AmountEarned";

    //Customer table number 3
    public static final String TABLE_NAME_3 ="Customer";
    public static final String COL_31 ="customerPhoneNumber";
    public static final String COL_32 ="Wallet";

    //Car table number 4

    public static final String TABLE_NAME_4 ="Car";
    public static final String COL_41 ="carNumber";
    public static final String COL_42 ="driverPhoneNumber";
    public static final String COL_43 ="carType";
    public static final String COL_44 ="carName";

    //Rides table number 5
    public static final String TABLE_NAME_5 ="Rides";
    public static final String COL_51 ="rideId";
    public static final String COL_52 ="pickUpPoint";
    public static final String COL_53 ="dropPoint";
    public static final String COL_54 ="distance";
    public static final String COL_55 ="otp";
    public static final String COL_56 ="timeStamp";
    public static final String COL_57 ="fare";
    public static final String COL_58 ="driverPhoneNumber";
    public static final String COL_59 ="customerPhoneNumber";

    //Emergency table number 6
    public static final String TABLE_NAME_6 ="Emergency";
    public static final String COL_61 ="customerPhoneNumber";
    public static final String COL_62 ="emergencyPhoneNumber";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_1 + " (phoneNumber TEXT PRIMARY KEY,password TEXT,firstName TEXT,lastName TEXT,dob TEXT,typeOfUser TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + " (driverPhoneNumber TEXT PRIMARY KEY,address TEXT,licenseNumber TEXT,availability TEXT,amountEarned TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_3 + " (customerPhoneNumber TEXT PRIMARY KEY,wallet TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_4 + " (carNumber TEXT PRIMARY KEY ,driverPhoneNumber TEXT,carType TEXT,carName TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_5 + " (rideId INTEGER PRIMARY KEY AUTOINCREMENT ,pickUpPoint TEXT,dropPoint TEXT,distance TEXT,otp TEXT," +
                "timeStamp TEXT,fare TEXT,driverPhoneNumber TEXT,customerPhoneNumber TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_6 + " (customerPhoneNumber TEXT,emergencyPhoneNumber TEXT,PRIMARY KEY(customerPhoneNumber,emergencyPhoneNumber))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1); //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_6);
        onCreate(db);
    }
}
