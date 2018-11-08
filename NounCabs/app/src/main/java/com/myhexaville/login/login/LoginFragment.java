package com.myhexaville.login.login;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myhexaville.login.CabHiring;
import com.myhexaville.login.Customer.CustomerActivity;
import com.myhexaville.login.DatabaseHelper;
import com.myhexaville.login.Driver.DriverActivity;
import com.myhexaville.login.R;

public class LoginFragment extends Fragment implements OnLoginListener{
    private static final String TAG = "LoginFragment";
    private TextView tvLogin;

    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Cursor cursor;
    TextView btLogin;
    private EditText etvPhoneNumber;
    private EditText etvPassword;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);
//        populateAutoComplete();
        etvPhoneNumber =(EditText)  inflate.findViewById(R.id.etv_phone_number_login);
        etvPassword =(EditText)inflate.findViewById(R.id.etv_password_login);
        btLogin =(TextView)inflate.findViewById(R.id.tv_login);
        openHelper=new DatabaseHelper(getContext());
        db = openHelper.getReadableDatabase();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = etvPhoneNumber.getText().toString();
                String pass = etvPassword.getText().toString();

                cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_1 + " WHERE " + DatabaseHelper.COL_11 + " =? AND " + DatabaseHelper.COL_12 + " =? ", new String[]{phone, pass});
                Log.d(TAG,"the value of cursor count  is "+cursor.getCount());
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        //attemptLogin();
                        String data;
                        Cursor newCursor;
                        String typeUser="";


                        if (cursor.moveToFirst()){
                            do{

                                data = cursor.getString(cursor.getColumnIndex("phoneNumber"));
                                // do what ever you want here
                                Log.d(TAG,"your phone number is "+data);
                                ((CabHiring) getActivity().getApplication()).setPhoneNumber(data);
                                newCursor=db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" WHERE "+DatabaseHelper.COL_11+" =? ",new String[]{data});
                                Log.d(TAG,"the value of new cursor count  is "+newCursor.getCount()+"   newCursor.getColumnIndex(\"typeOfUser\")  "+newCursor.getColumnIndex("typeOfUser"));
                                if(newCursor!=null){
                                    if(newCursor.getCount()>0){
                                        if(newCursor.moveToFirst()){
                                            do{

                                                Log.d(TAG,"newCursor.getString(0)"+newCursor.getString(0));
                                                Log.d(TAG,"newCursor.getString(1)"+newCursor.getString(1));
                                                Log.d(TAG,"newCursor.getString(2)"+newCursor.getString(2));
                                                Log.d(TAG,"newCursor.getString(3)"+newCursor.getString(3));
                                                Log.d(TAG,"newCursor.getString(4)"+newCursor.getString(4));
                                                Log.d(TAG,"newCursor.getString(5)"+newCursor.getString(5));

                                                if(newCursor!=null)
                                                    typeUser=newCursor.getString(newCursor.getColumnIndex("typeOfUser"));


                                            }while(newCursor.moveToNext());
                                        }
                                    }
                                }

                            }while(cursor.moveToNext());
                            newCursor.close();
                        }

                        cursor.close();

                        if(typeUser.matches("driver")){

                            Intent i=new Intent(getActivity(),DriverActivity.class);
                            startActivity(i);

                        }
                        if(typeUser.matches("customer")){

                            Intent i=new Intent(getActivity(),CustomerActivity.class);
                            startActivity(i);

                        }
                        if(!(typeUser.matches("driver")) && !(typeUser.matches("customer")))
                            Toast.makeText(getContext(),"user type doesnt match",Toast.LENGTH_SHORT).show();


                        Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Login error", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });







        return inflate;
    }

    @Override
    public void login() {
        Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();
    }

//
//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null,this );
//    }
//
//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (getActivity().checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(etvPhoneNumber, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }




//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }


//    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }
//
//        // Reset errors.
//        etvPhoneNumber.setError(null);
//        etvPassword.setError(null);
//
//        // Store values at the time of the login attempt.
//        String phoneNumber = etvPhoneNumber.getText().toString();
//        String password = etvPassword.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            etvPassword.setError(getString(R.string.error_invalid_password));
//            focusView = etvPassword;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(phoneNumber)) {
//            etvPhoneNumber.setError(getString(R.string.error_field_required));
//            focusView = etvPhoneNumber;
//            cancel = true;
//        } else if (!isPhoneNumberValid(phoneNumber)) {
//            etvPhoneNumber.setError(getString(R.string.error_invalid_email));
//            focusView = etvPhoneNumber;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(phoneNumber, password);
//            mAuthTask.execute((Void) null);
//        }
//    }
//
//
//    private boolean isPhoneNumberValid(String phoneNumber) {
//        //TODO: Replace this with your own logic
//        if(phoneNumber.length()==10)return true;
//        else return false;
//    }
//
//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 4;
//    }



//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(getContext(),//CHECK IF THERE IS ANY ERROR
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//
//        addEmailsToAutoComplete(emails);
//
//
//    }

//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }
//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(getContext(),//CHECK IF ANY ERROR
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        etvPhoneNumber.setAdapter(adapter);
//    }
//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                //CHECK FOR ERRORS
//                getActivity().finish();
//            } else {
//                etvPassword.setError(getString(R.string.error_incorrect_password));
//                etvPassword.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }


}
