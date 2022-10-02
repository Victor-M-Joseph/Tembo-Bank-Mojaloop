package com.temboplus.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    String mobileMoneyNumber;

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

                    mojaloopGetLinkingProviders(pickedMomo, mobileMoneyNumber);


                }

            }
        });
    }


    public void mojaloopGetLinkingProviders(String Provider, String MSIDSN){


        OkHttpClient client1 = new OkHttpClient();

        String url = "http://localhost:6060/admin/apimgmt#tag/linking/operation/GetLinkingProviders";


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n" +
                "  \"consentRequestId\": \"f6ab43b0-71cc-49f9-b763-2ac3f05ac8c1\",\n" +
                "  \"toParticipantId\": \"dfspa\",\n" +
                "  \"accounts\": [\n" +
                "    {\n" +
                "      \"accountNickname\": \"SpeXXXXXXXXnt\",\n" +
                "      \"id\": \"dfspa.username.5678\",\n" +
                "      \"currency\": \"USD\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"actions\": [\n" +
                "    \"ACCOUNTS_TRANSFER\"\n" +
                "  ],\n" +
                "  \"userId\": \"username1234\",\n" +
                "  \"callbackUri\": \"pisp-app://callback\"\n" +
                "}");


        Request request1 = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();

        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //Notify user of failure

                    }
                });

                e.printStackTrace();

                Log.d("Make Deposit:", "getting azamToken Failed");

                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response0) throws IOException {

                String response01 = response0.body().string();

                final JSONObject[] JSONresponse01 = new JSONObject[1];
                try {
                    JSONresponse01[0] = new JSONObject(response01);
                    Boolean success = JSONresponse01[0].getBoolean("success");

                    if (success.equals(true)) {

                        //go to Linking

                        Intent intent = new Intent(PickMomoProviderMojaloop.this, EnterOtpMojaloopActivity.class);
                        startActivity(intent);


                    }
                    else {
                        //Notice user of failure
                    }


                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //Notice user of failure

                        }
                    });

                    e.printStackTrace();
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

                mobileMoneyNumber = "+255"+strNewMomoNumber;

                    //mobileMoneyOption.setTextColor(Color.parseColor("#21618C"));
                    mpesaNumber.setText(mobileMoneyNumber);


                    dialog.dismiss();

            }
        });

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_dialog_card);
        dialog.show();
    }
}
