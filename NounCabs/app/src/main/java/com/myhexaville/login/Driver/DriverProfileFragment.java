package com.myhexaville.login.Driver;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myhexaville.login.CabHiring;
import com.myhexaville.login.R;
import com.myhexaville.login.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverProfileFragment extends Fragment {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Cursor cursor;
    private TextView tvName,tvBirthday,tvPhoneNumber,tvLicenseNumber,tvCarDetails,tvAddress,tvAmountEarned;

    public DriverProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_driver_profile, container, false);
        tvName =(TextView)view.findViewById(R.id.tv_name_driver);
        tvBirthday=(TextView)view.findViewById(R.id.tv_birthday_driver);
        tvPhoneNumber=(TextView)view.findViewById(R.id.tv_phone_number_driver);
        tvLicenseNumber=(TextView)view.findViewById(R.id.tv_licenseNo_driver);
        tvCarDetails=(TextView)view.findViewById(R.id.tv_car_details_driver);
        tvAddress=(TextView)view.findViewById(R.id.tv_address_driver);
        tvAmountEarned=(TextView)view.findViewById(R.id.tv_wallet_driver);
        openHelper=new DatabaseHelper(getContext());
        db = openHelper.getReadableDatabase();
        // do what ever you want here
                String data=((CabHiring) getActivity().getApplication()).getPhoneNumber();
                tvPhoneNumber.setText(data);
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_1 + " WHERE " + DatabaseHelper.COL_11 + "=? ", new String[]{data});

        if (cursor.moveToFirst()){
            do{
                String dataName = cursor.getString(cursor.getColumnIndex("firstName"))+" "+cursor.getString(cursor.getColumnIndex("lastName"));
                String Birthday=cursor.getString(cursor.getColumnIndex("dob"));
                // do what ever you want here
                tvName.setText(dataName);
                tvBirthday.setText(Birthday);

                }while(cursor.moveToNext());
        }
        cursor.close();

        Cursor newCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_2 + " WHERE " + DatabaseHelper.COL_21 + "=? ", new String[]{data});

        if (newCursor.moveToFirst()){
            do{
                String Address = newCursor.getString(newCursor.getColumnIndex("address"));
                String LicenseNumber=newCursor.getString(newCursor.getColumnIndex("licenseNumber"));
                String AmountEarned=newCursor.getString(newCursor.getColumnIndex("amountEarned"));
                // do what ever you want here
                tvAddress.setText(Address);
                tvLicenseNumber.setText(LicenseNumber);
                tvAmountEarned.setText(AmountEarned);

            }while(newCursor.moveToNext());
        }
        newCursor.close();


        Cursor carCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_4 + " WHERE " + DatabaseHelper.COL_42 + "=? ", new String[]{data});

        if (carCursor.moveToFirst()){
            do{
                String CarNumber = carCursor.getString(carCursor.getColumnIndex("carNumber"));
                String CarType=carCursor.getString(carCursor.getColumnIndex("carType"));
                String CarName=carCursor.getString(carCursor.getColumnIndex("carName"));
                // do what ever you want here
                tvCarDetails.setText(CarNumber+" - "+CarName+" - "+CarType);

            }while(carCursor.moveToNext());
        }
        carCursor.close();




        return view;
    }

}
