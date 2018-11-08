package com.myhexaville.login.login;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.R;

public class SignUpFragment extends Fragment implements OnSignUpListener {
    private static final String TAG = "SignUpFragment";
    RadioButton rbCustomer;
    RadioButton rbDriver;
    RadioGroup rgTypeOfUser;
    private Cursor cursor;


    SQLiteOpenHelper openHelper;
    SQLiteDatabase db,dbRead;
    TextView btSignUp;
    EditText etvFirstName,etvLastName, etvPassword,etvConfirmPassword, etvPhoneNumber,
            etvLicenseNo,etvCarNumber,etvCarType,etvCarName,etvAddress;
    Spinner spinnerDate,spinnerMonth,spinnerYear;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_signup, container, false);

        openHelper = new DatabaseHelper(getContext());

        rgTypeOfUser=(RadioGroup)inflate.findViewById(R.id.rg_typeOfUser);
        etvFirstName = (EditText)inflate.findViewById(R.id.etv_first_name_register);
        etvLastName = (EditText)inflate.findViewById(R.id.etv_last_name_register);
        etvPassword = (EditText)inflate.findViewById(R.id.etv_password_register);
        etvConfirmPassword = (EditText)inflate.findViewById(R.id.etv_confirm_password_register);
        etvPhoneNumber = (EditText)inflate.findViewById(R.id.etv__phonenumber_register);
        etvLicenseNo = (EditText)inflate.findViewById(R.id.etv_licenseNo_register);
        etvCarNumber = (EditText)inflate.findViewById(R.id.etv_car_no_register);
        etvCarName = (EditText)inflate.findViewById(R.id.etv_car_name_register);
        etvCarType = (EditText)inflate.findViewById(R.id.etv_car_type_register);
        etvAddress = (EditText)inflate.findViewById(R.id.etv_address_register);
        btSignUp =(TextView)inflate.findViewById(R.id.tv_signup_register);
        spinnerDate = (Spinner) inflate.findViewById(R.id.spinner_date_birth_register);
        spinnerMonth = (Spinner) inflate.findViewById(R.id.spinner_month_birth_register);
        spinnerYear = (Spinner) inflate.findViewById(R.id.spinner_year_birth_register);
        ArrayAdapter<CharSequence> adapterDate = ArrayAdapter.createFromResource(getContext(),
                R.array.dateOfBirth, android.R.layout.simple_spinner_item);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapterDate);

        ArrayAdapter<CharSequence> adapterMonth = ArrayAdapter.createFromResource(getContext(),
                R.array.monthOfBirth, android.R.layout.simple_spinner_item);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);

        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(getContext(),
                R.array.yearOfBirth, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);




        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=openHelper.getWritableDatabase();
                int SelectedId=rgTypeOfUser.getCheckedRadioButtonId();
                String FirstName= etvFirstName.getText().toString();
                String LastName= etvLastName.getText().toString();
                String Password= etvPassword.getText().toString();
                String ConfirmPassword= etvConfirmPassword.getText().toString();
                String PhoneNumber= etvPhoneNumber.getText().toString();
                String LicenseNumber= etvLicenseNo.getText().toString();
                String CarNumber= etvCarNumber.getText().toString();
                String CarName= etvCarName.getText().toString();
                String CarType= etvCarType.getText().toString();
                String Address= etvAddress.getText().toString();
                String date=spinnerDate.getSelectedItem().toString();
                String month=spinnerMonth.getSelectedItem().toString();
                String year=spinnerYear.getSelectedItem().toString();

                if(!checkForNewCustomer(PhoneNumber))
                    Toast.makeText(getContext()," User Already Exists ",Toast.LENGTH_SHORT).show();
                else{

                    switch (SelectedId){
                        case -1:
                            Toast.makeText(getContext(),"Type of User not selected",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.rb_customer:


                            if(FirstName.matches("")|| LastName.matches("") || Password.matches("") || ConfirmPassword.matches("")
                                    ||PhoneNumber.matches("") || !(Password.matches(ConfirmPassword)) || !(isPhoneNumberValid(PhoneNumber)))
                            {
                                Toast.makeText(getContext()," Entries are inappropriate ",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                insertDataToUser(PhoneNumber,Password,FirstName,LastName,date+"-"+month+"-"+year,"customer");
                                insertDataToCustomer(PhoneNumber,"0");
                                Toast.makeText(getContext(), "register successfully", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.rb_driver:

                            if(FirstName.matches("")|| LastName.matches("") || Password.matches("") || ConfirmPassword.matches("")
                                    ||PhoneNumber.matches("") || LicenseNumber.matches("")|| CarName.matches("")|| CarNumber.matches("")|| CarType.matches("")
                                    || Address.matches("")|| !(Password.matches(ConfirmPassword)) || !(isPhoneNumberValid(PhoneNumber)) || !(isLicenseNumberValid(LicenseNumber)))
                            {

                                Toast.makeText(getContext()," Entries are inappropriate ",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                insertDataToUser(PhoneNumber,Password,FirstName,LastName,date+"-"+month+"-"+year,"driver");
                                insertDataToDriver(PhoneNumber,Address,LicenseNumber,"yes","0",CarNumber,CarType,CarName);
                                Toast.makeText(getContext(), "register successfully", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }

                }










            }
        });


        rbCustomer=inflate.findViewById(R.id.rb_customer);
        rbDriver=inflate.findViewById(R.id.rb_driver);
        rbCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etvLicenseNo.setVisibility(View.INVISIBLE);
                etvCarType.setVisibility(View.INVISIBLE);
                etvCarName.setVisibility(View.INVISIBLE);
                etvCarNumber.setVisibility(View.INVISIBLE);
                etvAddress.setVisibility(View.INVISIBLE);

            }
        });
        rbDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etvLicenseNo.setVisibility(View.VISIBLE);
                etvCarType.setVisibility(View.VISIBLE);
                etvCarName.setVisibility(View.VISIBLE);
                etvCarNumber.setVisibility(View.VISIBLE);
                etvAddress.setVisibility(View.VISIBLE);

            }
        });



        return inflate;
    }

    public void insertDataToUser(String PhoneNumber,String Password,String FirstName,String LastName,String dob,String typeOfUser){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_11, PhoneNumber);
        contentValues.put(DatabaseHelper.COL_12, Password);
        contentValues.put(DatabaseHelper.COL_13, FirstName);
        contentValues.put(DatabaseHelper.COL_14, LastName);
        contentValues.put(DatabaseHelper.COL_15, dob);
        contentValues.put(DatabaseHelper.COL_16, typeOfUser);
        long id = db.insert(DatabaseHelper.TABLE_NAME_1, null, contentValues);



    }
    public void insertDataToDriver(String driverPhoneNumber,String address,String licenseNumber,String availability,String amountEarned,String carNumber,String carType,String carName){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_21, driverPhoneNumber);
        contentValues.put(DatabaseHelper.COL_22,address);
        contentValues.put(DatabaseHelper.COL_23, licenseNumber);
        contentValues.put(DatabaseHelper.COL_24, availability);
        contentValues.put(DatabaseHelper.COL_25, amountEarned);
        long id = db.insert(DatabaseHelper.TABLE_NAME_2, null, contentValues);
        ContentValues contentValuesCar = new ContentValues();
        contentValuesCar.put(DatabaseHelper.COL_41,carNumber);
        contentValuesCar.put(DatabaseHelper.COL_42,driverPhoneNumber);
        contentValuesCar.put(DatabaseHelper.COL_43,carType);
        contentValuesCar.put(DatabaseHelper.COL_44,carName);
        long idCar=db.insert(DatabaseHelper.TABLE_NAME_4,null,contentValuesCar);

    }
    public void insertDataToCustomer(String customerPhoneNumber,String wallet){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_31, customerPhoneNumber);
        contentValues.put(DatabaseHelper.COL_32, wallet);
        long id = db.insert(DatabaseHelper.TABLE_NAME_3, null, contentValues);


    }
    private boolean checkForNewCustomer(String phoneNumber){
        openHelper = new DatabaseHelper(getContext());
        dbRead=openHelper.getReadableDatabase();

        cursor=dbRead.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_1 + " WHERE " + DatabaseHelper.COL_11 + "=? ", new String[]{phoneNumber});

        if(cursor.getCount()==0){
            cursor.close();

            return true;
        }else{
            cursor.close();

            return false;
        }



    }
    private boolean isLicenseNumberValid(String licenseNumber){
        return (licenseNumber.length()==13);
    }

    private boolean isPhoneNumberValid(String phoneNumber){
        return (phoneNumber.length()==10);
    }
    @Override
    public void signUp() {
        Toast.makeText(getContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }
}
