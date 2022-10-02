package com.temboplus.temp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.hover.sdk.api.HoverParameters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.temboplus.temp.MainActivity.MM_BALANCE;
import static com.temboplus.temp.MainActivity.SHARED_PREFS;
import static com.temboplus.temp.PickRecipientActivity.EXTRA_RECIPIENT_NAME;
import static com.temboplus.temp.PickRecipientActivity.EXTRA_RECIPIENT_NUMBER;

public class AmountToSendActivity extends AppCompatActivity {

    private String recipientName;
    private String recipientNumber;
    static boolean error;
    static boolean enterNumber;


    private String MobileOperator;
    String MobileMoneyOperator;
    private String MMBalance;
    private TextView MMOperator;
    private TextView tvMobileMoneyBalance;
    private String hoverId; // Hover Action ID for Sending money, for each Mobile Money service

    public static final String EXTRA_AMOUNT_TO_SEND = "com.saveapp.saveapp.EXTRA_AMOUNT_TO_SEND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_amount);


        final TextView tvRecipientName = findViewById(R.id.name);
        final TextView tvRecipientNumber = findViewById(R.id.number);
        final EditText etAmount = findViewById(R.id.amount);
        Button confirm = findViewById(R.id.confirm);



        ////////////////////////////////////////////////////////////////

        //Get mobile money balance from shared preferences and set it.
        //Get Mobile Money from Shared Preferences and show it
        SharedPreferences sharedPreferences = this.getSharedPreferences (SHARED_PREFS, MODE_PRIVATE);
        String balance = sharedPreferences.getString(MM_BALANCE, "Show Balance");



        //get Intent and all it brings with it!
        final Intent i = getIntent();
        recipientNumber = i.getStringExtra(EXTRA_RECIPIENT_NUMBER); //all the values from the intent
        recipientName = i.getStringExtra(EXTRA_RECIPIENT_NAME);
        enterNumber = getIntent().getExtras().getBoolean("enterNumber");
        error = getIntent().hasExtra("error");
        Log.d("Enter Amount Activity", " Boolean Enter Number: "+enterNumber);



            tvRecipientName.setText(recipientName);
            tvRecipientNumber.setText(recipientNumber);


            //Confirm Button Intent
            confirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String amount = etAmount.getText().toString();

                    //format recipient number
                    String formatedRecipientNumber = formatRecipientNumber(recipientNumber);

                    if (amount.isEmpty()){
                       etAmount.setError("Enter Amount");
                    }
                    else {

                        Intent intent = new Intent(AmountToSendActivity.this, ConfirmTransferMojaloop.class);
                        intent.putExtra("error",true);
                        intent.putExtra("enterNumber", false);
                        intent.putExtra(EXTRA_RECIPIENT_NAME, recipientName);
                        intent.putExtra(EXTRA_RECIPIENT_NUMBER, recipientNumber);
                        startActivity(intent);

                    }

                }
            });



        //Comma separator when entering amount.
        etAmount.addTextChangedListener( new TextWatcher() {
            boolean isEdiging;
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void afterTextChanged(Editable s) {
                if(isEdiging) return;
                isEdiging = true;

                if(s.length()!=0) {
                    String str = s.toString().replaceAll("[^\\d]", "");

                    double s1 = 0;
                    if (!str.isEmpty()) {
                        s1 = Double.parseDouble(str);
                    }

                    NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
                    ((DecimalFormat) nf2).applyPattern("###,###.###");
                    s.replace(0, s.length(), nf2.format(s1));

                    //etAmount.setText("TZS " + etAmount.getText().toString());
                    etAmount.setText("TZS " + etAmount.getText().toString().replace("TZS ", ""));
                    etAmount.setSelection(etAmount.getText().length());

                }

                isEdiging = false;
            }
        });


        etAmount.requestFocus();
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

    public String formatRecipientNumber(String recipientNumber){

        //Format recipient number, remove all unwanted characters

        //1. remove all and any dashes (-) then spaces, then country codes for Tanzania, Uganda, Kenya, Rwanda, Ghana & Nigeria
        String recipientNumberNoDashes = recipientNumber.replace("-","");
        String recipientNumberNoDashesOrSpaces = recipientNumberNoDashes.replace(" ", "");
        String recipientNumberNoDashesOrSpacesOr255 = recipientNumberNoDashesOrSpaces.replace("+255","0");
        String recipientNumberNoDashesOrSpacesOr255Or256 = recipientNumberNoDashesOrSpacesOr255.replace("+256","0");
        String recipientNumberNoDashesOrSpacesOr255Or256Or254 = recipientNumberNoDashesOrSpacesOr255Or256.replace("+254","0");
        String recipientNumberNoDashesOrSpacesOr255Or256Or254Or250 =  recipientNumberNoDashesOrSpacesOr255Or256Or254.replace("+250","0");
        String recipientNumberNoDashesOrSpacesOr255Or256Or254Or250Or233 = recipientNumberNoDashesOrSpacesOr255Or256Or254Or250.replace("+233","0");
        String recipientNumberNoDashesOrSpacesOr255Or256Or254Or250Or233Or234 = recipientNumberNoDashesOrSpacesOr255Or256Or254Or250Or233.replace("+234","0");
        final String formatedRecipientNumber = recipientNumberNoDashesOrSpacesOr255Or256Or254Or250Or233Or234;


        return formatedRecipientNumber;

    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {


            Intent intent = new Intent(AmountToSendActivity.this, MainActivity.class);
            startActivity(intent);


        } else if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Error: " + data.getStringExtra("error"), Toast.LENGTH_LONG).show();
            //TODO: Send this to Transaction Database
        }
    }
}
