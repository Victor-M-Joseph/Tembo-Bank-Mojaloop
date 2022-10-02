package com.temboplus.temp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.temboplus.temp.sendMoneyActivity.Constants.PERMISSION_REQUEST_CONTACT;

public class sendMoneyActivity extends AppCompatActivity {

    private LinearLayout pickContacts;
    private LinearLayout enterNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);



        pickContacts = findViewById(R.id.pick_contacts);
        enterNumber = findViewById(R.id.enter_number);

        //send money button Intent
        pickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askForContactPermission();

            }
        });

        //Enter phone number button intent
        enterNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sendMoneyActivity.this, AmountToSendActivity.class);
                intent.putExtra("enterNumber", true);
                startActivity(intent);

            }
        });
    }




    //Permission to access contacts
    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(sendMoneyActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(sendMoneyActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(sendMoneyActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(sendMoneyActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                Intent intent = new Intent(sendMoneyActivity.this, PickRecipientActivity.class);

                startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(sendMoneyActivity.this, PickRecipientActivity.class);

            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(sendMoneyActivity.this, PickRecipientActivity.class);
                    startActivity(intent);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(sendMoneyActivity.this, "No Permissions ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(sendMoneyActivity.this, sendMoneyActivity.class);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    class Constants{
        public static final int PERMISSION_REQUEST_CONTACT = 0;
    }

    //code to kill application if it ever went to the background
    @Override
    public void onResume(){
        super.onResume();

        StopApplication myApp = (StopApplication)this.getApplication();
        if (myApp.wasInBackground)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            this.finish();
        }

        myApp.stopActivityTransitionTimer();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        ((StopApplication)this.getApplication()).startActivityTransitionTimer();
    }
}
