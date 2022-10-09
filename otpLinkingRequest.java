package com.temboplus.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

                mojaloopPostLinkingRequestConsent(Provider, MSIDSN);

            }
        });
    }



    public void mojaloopPostLinkingRequestConsent(String Provider, String MSIDSN) {


        OkHttpClient client1 = new OkHttpClient();

        String url = "http://localhost:6060/admin/apimgmt#tag/linking/operation/PostLinkingRequestConsent";


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

                Log.d("Make Deposit:", "getting token Failed");

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

                        Intent intent = new Intent(EnterOtpMojaloopActivity.this, MojaloopSuccessActivity.class);
                        startActivity(intent);


                    } else {
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
