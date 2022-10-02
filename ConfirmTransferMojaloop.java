package com.temboplus.temp;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import static com.temboplus.temp.PickRecipientActivity.EXTRA_RECIPIENT_NAME;
import static com.temboplus.temp.PickRecipientActivity.EXTRA_RECIPIENT_NUMBER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class ConfirmTransferMojaloop extends AppCompatActivity {

    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_transfer_mojaloop);

        confirmBtn = findViewById(R.id.confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Ask for fingerprint authentication
                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Approve with Finger Print")
                        .setDescription("Approve with Finger Print")
                        .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL | BIOMETRIC_WEAK)
                        .build();

                getPrompt().authenticate(promptInfo);


            }

        });
    }

    private BiometricPrompt getPrompt(){
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);


                int time = 2000;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {

                    }
                }, time);

                Toast.makeText(ConfirmTransferMojaloop.this, "You will be notified momentarily" , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ConfirmTransferMojaloop.this, MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notifyUser("Failed");
            }
        };

        return new BiometricPrompt(this, executor,callback);
    }

    private void notifyUser(String message){
        Toast.makeText(this
                ,message, Toast.LENGTH_LONG).show();
    }
}