package com.myhexaville.login.Customer;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myhexaville.login.CabHiring;
import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerEmergencyFragment extends Fragment {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase dbWrite,dbRead;
    private LinearLayout llEmergencyOld,llEmergencyNew;
    private TextView tvEmergency1,tvEmergency2;
    private EditText etvEmergency1,etvEmergency2;
    private Button btAddEmergency;
    private Cursor cursor;
    private String data;


    public CustomerEmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_customer_emergency, container, false);
        openHelper = new DatabaseHelper(getContext());
        dbWrite=openHelper.getWritableDatabase();
        dbRead=openHelper.getReadableDatabase();
        etvEmergency1=v.findViewById(R.id.etv_emergency_phone_1_customer);
        etvEmergency2=v.findViewById(R.id.etv_emergency_phone_2_customer);
        tvEmergency1=v.findViewById(R.id.tv_emergency_phone_1_customer_new);
        tvEmergency2=v.findViewById(R.id.tv_emergency_phone_2_customer_new);
        llEmergencyNew=v.findViewById(R.id.ll_new_emergency);
        llEmergencyOld=v.findViewById(R.id.ll_old_emergency);
        btAddEmergency=v.findViewById(R.id.bt_add_emergency_phone_number);

        data=((CabHiring) getActivity().getApplication()).getPhoneNumber();
        cursor=dbRead.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_6 + " WHERE " + DatabaseHelper.COL_61 + "=? ", new String[]{data});

        if(cursor.getCount()==0){
            llEmergencyOld.setVisibility(View.VISIBLE);
            llEmergencyNew.setVisibility(View.INVISIBLE);
            btAddEmergency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pushingEmergencyNumber(etvEmergency1.getText().toString());
                    pushingEmergencyNumber(etvEmergency2.getText().toString());
                    Toast.makeText(getContext(),"Emergency Numbers Added",Toast.LENGTH_SHORT).show();
                    tvEmergency1.setText(etvEmergency1.getText().toString());
                    tvEmergency2.setText(etvEmergency2.getText().toString());
                    displayingEmergencyPhone();
                }
            });

        }else{
            displayingEmergencyPhone();


        }


        cursor.close();



        return v;
    }
    private void pushingEmergencyNumber(String phone){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_61, data);
        contentValues.put(DatabaseHelper.COL_62, phone);
        long id = dbWrite.insert(DatabaseHelper.TABLE_NAME_6, null, contentValues);



    }
    private void displayingEmergencyPhone(){
        llEmergencyNew.setVisibility(View.VISIBLE);
        llEmergencyOld.setVisibility(View.INVISIBLE);


        if(cursor.moveToFirst()){

                String phoneNumber1=cursor.getString(cursor.getColumnIndex("emergencyPhoneNumber"));
                tvEmergency1.setText(phoneNumber1);
                if(cursor.moveToNext()){

                    String phoneNumber2=cursor.getString(cursor.getColumnIndex("emergencyPhoneNumber"));
                    tvEmergency2.setText(phoneNumber2);}
                    }


    }

}
