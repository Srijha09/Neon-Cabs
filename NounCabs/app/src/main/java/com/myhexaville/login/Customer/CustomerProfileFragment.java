package com.myhexaville.login.Customer;


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
import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class CustomerProfileFragment extends Fragment {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    private TextView tvName,tvPhoneNumber,tvBirthday;
    Cursor cursor;

    public CustomerProfileFragment() {
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            openHelper=new DatabaseHelper(getContext());
            db = openHelper.getReadableDatabase();

            // Inflate the layout for this fragment
            View v= inflater.inflate(R.layout.fragment_customer_profile, container, false);

            tvName =v.findViewById(R.id.tv_name_profile_customer);
            tvPhoneNumber=v.findViewById(R.id.tv_phone_number_customer);
            tvBirthday=v.findViewById(R.id.tv_birthday_customer);
            String data = ((CabHiring)getActivity().getApplication()).getPhoneNumber();
            tvPhoneNumber.setText(data);
            cursor=db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_1 + " WHERE " + DatabaseHelper.COL_11 + "=? ", new String[]{data});
            if(cursor.moveToFirst()){

                do{
                    String dataName = cursor.getString(cursor.getColumnIndex("firstName"))+" "+cursor.getString(cursor.getColumnIndex("lastName"));
                    String Birthday=cursor.getString(cursor.getColumnIndex("dob"));
                    tvBirthday.setText(Birthday);
                    tvName.setText(dataName);
                } while(cursor.moveToNext());
            }
            cursor.close();
            return v;

        }


    }



//cursor=db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_1 + " WHERE " + DatabaseHelper.COL_11 + "=? ", new String[]{data});
//            if(cursor.moveToFirst()){
//
//                do{
//                    String dataName = cursor.getString(cursor.getColumnIndex("firstName"));
//                    tvName.setText(dataName);
//                } while(cursor.moveToNext());
//            }