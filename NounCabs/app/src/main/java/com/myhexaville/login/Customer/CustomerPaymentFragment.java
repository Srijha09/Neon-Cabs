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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myhexaville.login.CabHiring;
import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.R;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerPaymentFragment extends Fragment {
    private String moneyToBeAdded;
    private Button btAddMoney;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase dbWrite,dbRead;
    private Cursor cursor;
    private String data;
    private TextView tvWalletValue;


    public CustomerPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_customer_payment, container, false);
        tvWalletValue=v.findViewById(R.id.tv_wallet_value_customer);

        openHelper = new DatabaseHelper(getContext());
        dbWrite=openHelper.getWritableDatabase();
        dbRead=openHelper.getReadableDatabase();

        btAddMoney=v.findViewById(R.id.bt_add_money_wallet_customer);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_add_money_wallet_customer);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.add_money_values, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                moneyToBeAdded=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                moneyToBeAdded="100";

            }
        });
        spinner.setAdapter(adapter);
        displayingTheWalletContent();
        btAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementingTheWallet();

            }
        });



        return  v;

    }

    public void incrementingTheWallet(){


        data=((CabHiring) getActivity().getApplication()).getPhoneNumber();
        cursor=dbRead.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_3 + " WHERE " + DatabaseHelper.COL_31 + "=? ", new String[]{data});

        if(cursor.moveToFirst()){

            do{

                String BeforeAdding=cursor.getString(cursor.getColumnIndex("wallet"));
                int beforeAdding=Integer.parseInt(BeforeAdding);
                int afterAdding=beforeAdding+Integer.parseInt(moneyToBeAdded);
                String AfterAdding=""+afterAdding;
                updatingWallet(AfterAdding);

            } while(cursor.moveToNext());
        }
        cursor.close();

    }
    public void updatingWallet(String AfterAdding){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_32, AfterAdding);
        dbWrite.update(DatabaseHelper.TABLE_NAME_3,contentValues,DatabaseHelper.COL_31+" =? ",new String[]{data});
        Toast.makeText(getContext(),"Rs. "+AfterAdding+" added to your wallet ",Toast.LENGTH_SHORT).show();
        displayingTheWalletContent();

    }
    private void displayingTheWalletContent(){

        data=((CabHiring) getActivity().getApplication()).getPhoneNumber();
        cursor=dbRead.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_3 + " WHERE " + DatabaseHelper.COL_31 + "=? ", new String[]{data});

        if(cursor.moveToFirst()){

            do{

                String valueWallet=cursor.getString(cursor.getColumnIndex("wallet"));
                tvWalletValue.setText(valueWallet);

            } while(cursor.moveToNext());
        }
        cursor.close();
    }

}
