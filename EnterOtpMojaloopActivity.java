package com.temboplus.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterOtpMojaloopActivity extends AppCompatActivity {

    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp_mojaloop);

        continueBtn=findViewById(R.id.continue_btn);

        continueBtn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                    /*saveAccountNumberToDatabase(accountNumber, pickedBank);*/

                Intent intent = new Intent(EnterOtpMojaloopActivity.this, MojaloopSuccessActivity.class);
                startActivity(intent);
            }
        });
    }
}