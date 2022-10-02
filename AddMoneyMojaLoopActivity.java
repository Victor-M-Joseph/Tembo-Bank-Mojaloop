package com.temboplus.temp;

import static android.content.Intent.EXTRA_UID;
import static com.temboplus.temp.GoalPlanActivity.EXTRA_DEPOSIT_AMOUNT;
import static com.temboplus.temp.MainActivity.SHARED_PREFS;
import static com.temboplus.temp.PhoneNumberActivity.PHONE_NUMBER;
import static com.temboplus.temp.PickCountryOfResidencyActivity.EXTRA_RESIDENCY;
import static com.temboplus.temp.PickLanguageActivity.EXTRA_LANGUAGE;
import static com.temboplus.temp.RecyclerView_Goals.EXTRA_ACCOUNT_NAME;
import static com.temboplus.temp.SavingsGoalActivity.EXTRA_GOAL_KEY;
import static com.temboplus.temp.SavingsGoalActivity.EXTRA_TARGET_AMOUNT;
import static com.temboplus.temp.SavingsGoalActivity.EXTRA_WITHDRAW_DATE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AddMoneyMojaLoopActivity extends AppCompatActivity {

    public static final String EXTRA_DEPOSIT_TRANSACTION_ID= "com.saveapp.saveapp.EXTRA_DEPOSIT_TRANSACTION_ID";
    public static final String EXTRA_GOAL_NAME = "com.saveapp.saveapp.EXTRA_GOAL_NAME";
    public static final String EXTRA_AMOUNT = "com.saveapp.saveapp.EXTRA_AMOUNT";
    public static final String EXTRA_GOAL_ID = "com.saveapp.saveapp.EXTRA_GOAL_ID";

    private TextView tvAccountName;
    private TextView tvTargetAmount;
    private EditText etAmount;
    private Button confirmBtn;
    private RadioButton cardRadioBtn;
    private TextView visaMastercard;
    private String accountName;
    private String targetAmount;
    private String savedAmount;
    private String withdrawDate;
    private int recommendedAmount;
    static boolean error;
    private DatabaseReference mDatabase;
    private String amount;
    private String goalFirebaseKey;

    private TextView tvMobileMoneyOperator;
    private String MMBalance;
    private ImageView MMLogo;
    private TextView MMOperator;
    private TextView tvMobileMoneyBalance;
    private String hoverId; // Hover Action ID for Making a deposit to the Bank

    String currency;
    TextView tvCurrency;
    TextView pickMethodNotice;

    LinearLayout cardDisplay;
    LinearLayout bankDisplay;
    LinearLayout momoDisplay;
    RadioButton cardNumber;
    RadioButton mobileMoneyOption;
    ImageView ivCardType;
    ImageView ivCardAvailable;
    ImageView editMomoNumber;
    ImageView addCard;
    LinearLayout mobileMoneyArea;
    LinearLayout backArrow;
    TextView noticeDisplay;
    LinearLayout loading;

    RadioButton bankAccountNumber;
    ImageView connectBank;
    ImageView bankLogo;

    //Card Data (Not all card data, we dont hold that, just the charge token and last 4 digits of the card & card type)
    String cardToken;
    String cardFirst6Digits;
    String cardLast4Digits;
    String cardType;
    String phoneNumber;
    String firstName;
    String lastName;
    String email;
    String XpressAccountNumber;

    String paymentMethod; //"card" OR "momo". Used to decide what to do when confirm button is clicked
    String mobileMoneyNumber;
    String mobileMoneyOperator;

    //Bank Payment
    String salaryAccount;
    String salaryAccountOTP;
    String salaryAccountProvider; //NMB, CRDB

    String token; //messaging token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_moja_loop);


        backArrow = findViewById(R.id.back_arrow);
        tvAccountName = findViewById(R.id.account_name);
        //tvTargetAmount = findViewById(R.id.target_amount);
        confirmBtn = findViewById(R.id.confirm);
        etAmount = findViewById(R.id.amount);
        MMOperator = findViewById(R.id.mobile_money_operator);
        tvMobileMoneyBalance = findViewById(R.id.mobile_money_balance);
        tvCurrency = findViewById(R.id.currency);
        mobileMoneyArea = findViewById(R.id.mobile_money_area);
        loading = findViewById(R.id.loading);

        bankAccountNumber=findViewById(R.id.bank_acc_number);
        connectBank = findViewById(R.id.connect_bank);
        bankLogo = findViewById(R.id.bank_logo);



        cardDisplay=findViewById(R.id.card_display);
        bankDisplay=findViewById(R.id.bank_display);
        momoDisplay=findViewById(R.id.momo_display);
        cardNumber=findViewById(R.id.card_number);
        addCard = findViewById(R.id.add_card);
        mobileMoneyOption = findViewById(R.id.mobile_money_number);
        ivCardType = findViewById(R.id.card_type);
        ivCardAvailable = findViewById(R.id.card_available);
        editMomoNumber = findViewById(R.id.edit_momo_number);
        noticeDisplay = findViewById(R.id.notice_display);
        pickMethodNotice=findViewById(R.id.pick_method_notice);

        //Set currency
        setCurrency();

        //display currency
        tvCurrency.setText(currency);


        //get Intent and all it brings with it!
        Intent i = getIntent();
        accountName = i.getStringExtra(EXTRA_ACCOUNT_NAME);
        targetAmount = i.getStringExtra(EXTRA_TARGET_AMOUNT); //TODO: Add saved amount field in the Firebase Database, update it with every transaction user makes to add savings. This info will be showed on recycler view at home page and here

        //savedAmount = i.getStringExtra(EXTRA_SAVED_AMOUNT);
        withdrawDate = "Matures on: " + i.getStringExtra(EXTRA_WITHDRAW_DATE);
        Log.d("Save Now", "onCreate: recommendedAmount is "+recommendedAmount);
        goalFirebaseKey = i.getStringExtra(EXTRA_GOAL_KEY);
        final String depositAmount = i.getStringExtra(EXTRA_DEPOSIT_AMOUNT);

        tvAccountName.setText(accountName);
        //tvAccountName.setPaintFlags(tvAccountName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        etAmount.setText(depositAmount);


        mobileMoneyNumber = loadPhoneNumber();


        //Get FCM Notification Token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                    token = task.getResult();

                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        //Check the Deposit Methods Radio Buttons
        //Go to Appropriate Page when Required
        //Set payment Method user clicks
        cardNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                paymentMethod="card";
                confirmBtn.setText(getResources().getString(R.string.add_money_deposit));
                cardDisplay.setBackgroundResource(R.drawable.picked_payment_method_bg);
                cardNumber.setChecked(true);
                mobileMoneyOption.setChecked(false);
                bankAccountNumber.setChecked(false);

                //Remove "picked_pament" bg
                bankDisplay.setBackgroundResource(R.drawable.unpicked_payment_method_bg);
                momoDisplay.setBackgroundResource(R.drawable.unpicked_payment_method_bg);



            }
        });

        bankAccountNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO: Implement Bank Deposit with Azam Pesa

                paymentMethod = "bank";
                confirmBtn.setText(getResources().getString(R.string.add_money_deposit));
                bankDisplay.setBackgroundResource(R.drawable.picked_payment_method_bg);
                bankAccountNumber.setChecked(true);
                cardNumber.setChecked(false);
                mobileMoneyOption.setChecked(false);

                //Remove "picked_pament" bg
                cardDisplay.setBackgroundResource(R.drawable.unpicked_payment_method_bg);
                momoDisplay.setBackgroundResource(R.drawable.unpicked_payment_method_bg);
            }

        });

        mobileMoneyOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //paymentMethod="momoAzamPay";
                paymentMethod="momo";
                confirmBtn.setText(getResources().getString(R.string.add_money_deposit));
                momoDisplay.setBackgroundResource(R.drawable.picked_payment_method_bg);
                mobileMoneyOption.setChecked(true);
                cardNumber.setChecked(false);
                bankAccountNumber.setChecked(false);

                //Remove "picked_pament" bg
                cardDisplay.setBackgroundResource(R.drawable.unpicked_payment_method_bg);
                bankDisplay.setBackgroundResource(R.drawable.unpicked_payment_method_bg);

            }
        });





        //Confirm Button INTENT
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                amount=etAmount.getText().toString();

                if (amount.isEmpty()|| amount.equals("TZS 0")) {
                    //noticeDisplay.setVisibility(View.VISIBLE);
                    noticeDisplay.setTextColor(Color.parseColor("#730344"));
                    //noticeDisplay.setText(getResources().getString(R.string.input_amount_notice));
                    etAmount.setError(getResources().getString(R.string.input_amount_notice));

                }
                else if (paymentMethod==null){
                    pickMethodNotice.setError("pick method first");
                }

                else {

                    //Switch Payment Method
                    switch (paymentMethod) {
                        case "card":
                            Intent intent = new Intent(AddMoneyMojaLoopActivity.this,
                                    AddCardActivity.class);
                            startActivity(intent);
                            break;

                        case "bank":
                            Intent intent_bank = new Intent(AddMoneyMojaLoopActivity.this,
                                    MojaloopBankInfoActivity.class);
                            startActivity(intent_bank);
                            break;

                        case "momo":
                            Intent intent_momo = new Intent(AddMoneyMojaLoopActivity.this,
                                    MojaloopMomoInfoActivity.class);
                            startActivity(intent_momo);
                            break;

                    }

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









    //Load Language
    public String loadLanguage(){
        SharedPreferences sharedPreferences = this.getSharedPreferences (PhoneNumberActivity.SHARED_PREFS, MODE_PRIVATE);
        String preferredLanguage = sharedPreferences.getString(EXTRA_LANGUAGE, "0");
        return preferredLanguage;
    }

    public String loadPhoneNumber(){
        Context context = AddMoneyMojaLoopActivity.this;
        String phoneNumber;
        SharedPreferences sharedPreferences = context.getSharedPreferences (SHARED_PREFS, MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString(PHONE_NUMBER, "0");
        return phoneNumber;
    }

    public String loadUserId(){
        Context context = AddMoneyMojaLoopActivity.this;
        String userID;
        SharedPreferences sharedPreferences = context.getSharedPreferences (SHARED_PREFS, MODE_PRIVATE);
        userID = sharedPreferences.getString(EXTRA_UID, "0");
        return userID;
    }

    public void setCurrency(){

        //load CountryOfResidency From Shared Preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences (SHARED_PREFS, MODE_PRIVATE);
        String residency = sharedPreferences.getString(EXTRA_RESIDENCY, null);

        switch (residency) {
            case "Tanzania":
                currency = "TZS";
                break;
            case "Kenya":
                currency = "KES";
                break;
            case "Uganda":
                currency = "UGX";
                break;
            case "United Kingdom":
                currency = "GBP";
                break;
            default:
                currency="TZS";

        }

    }


}