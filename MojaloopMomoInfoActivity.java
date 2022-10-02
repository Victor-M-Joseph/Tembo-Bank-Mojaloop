package com.temboplus.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MojaloopMomoInfoActivity extends AppCompatActivity {

    private LinearLayout backBtn;
    LinearLayout addCard;
    LinearLayout connectBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mojaloop_momo_info);

        addCard = findViewById(R.id.card);
        connectBank = findViewById(R.id.bank);
        backBtn =findViewById(R.id.back_arrow);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        connectBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddCardInfoActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MojaloopMomoInfoActivity.this, PickMomoProviderMojaloop.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }
}