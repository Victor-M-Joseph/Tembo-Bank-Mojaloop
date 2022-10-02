package com.temboplus.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class PickMomoProviderMojaloop extends AppCompatActivity {

    LinearLayout mpesa;
    LinearLayout tigopesa;
    LinearLayout airtelMoney;
    LinearLayout ttclPesa;
    Button continueBtn;
    TextView activityTitle;
    LinearLayout backButton;
    TextView mpesaNumber;

    String pickedMomo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_momo_provider_mojaloop);


        mpesa = findViewById(R.id.mpesa);
        tigopesa = findViewById(R.id.tigo_pesa);
        airtelMoney = findViewById(R.id.airtel_money);
        ttclPesa= findViewById(R.id.ttcl_pesa);
        backButton = findViewById(R.id.back_arrow);
        continueBtn = findViewById(R.id.continue_btn);
        mpesaNumber = findViewById(R.id.mpesa_number);

        mpesa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pickedMomo = "mpesa";
                mpesa.setBackgroundResource(R.drawable.picked_goal_card);
                airtelMoney.setBackgroundResource(R.drawable.white_card);
                ttclPesa.setBackgroundResource(R.drawable.white_card);
                tigopesa.setBackgroundResource(R.drawable.white_card);

                editAccountNumberMojaloopDialog();

            }
        });


        tigopesa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pickedMomo = "tigopesa";
                tigopesa.setBackgroundResource(R.drawable.picked_goal_card);
                mpesa.setBackgroundResource(R.drawable.white_card);
                airtelMoney.setBackgroundResource(R.drawable.white_card);
                ttclPesa.setBackgroundResource(R.drawable.white_card);
            }
        });

        airtelMoney.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pickedMomo = "airtelMoney";
                airtelMoney.setBackgroundResource(R.drawable.picked_goal_card);
                tigopesa.setBackgroundResource(R.drawable.white_card);
                mpesa.setBackgroundResource(R.drawable.white_card);
                ttclPesa.setBackgroundResource(R.drawable.white_card);
            }
        });

        ttclPesa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pickedMomo = "airtelMoney";
                ttclPesa.setBackgroundResource(R.drawable.picked_goal_card);
                tigopesa.setBackgroundResource(R.drawable.white_card);
                mpesa.setBackgroundResource(R.drawable.white_card);
                airtelMoney.setBackgroundResource(R.drawable.white_card);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                if (pickedMomo!=null) {

                    Intent intent = new Intent(PickMomoProviderMojaloop.this, EnterOtpMojaloopActivity.class);
                    startActivity(intent);
                }

            }
        });
    }


    private void  editAccountNumberMojaloopDialog(){

        Dialog dialog = new Dialog(PickMomoProviderMojaloop.this);
        dialog.setContentView(R.layout.edit_account_number_mojaloop);

        EditText newMomoNumber = dialog.findViewById(R.id.new_momo_number);

        //ConfirmWithdrawContent
        Button confirmBtn = dialog.findViewById(R.id.confirm_btn);

        //User confirrms the withdraw
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strNewMomoNumber = newMomoNumber.getText().toString().trim();

                String mobileMoneyNumber = "+255"+strNewMomoNumber;

                    //mobileMoneyOption.setTextColor(Color.parseColor("#21618C"));
                    mpesaNumber.setText(mobileMoneyNumber);


                    dialog.dismiss();

            }
        });

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_dialog_card);
        dialog.show();
    }
}