package com.ppal007.smartvoting.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.adapter.AdapterProgressBar;
import com.ppal007.smartvoting.model.ModelUser;
import com.ppal007.smartvoting.retrofit.ApiClient;
import com.ppal007.smartvoting.retrofit.ApiInterFace;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private void showToast(String msg){
        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
    }

    private EditText fullName,userName;
    private TextView textView_finger,msg_finger,textView_device_code;
    private Button button_signup;
    private AdapterProgressBar adapterProgressBar;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_sign_up);

        //init progress bar adapter
        adapterProgressBar = new AdapterProgressBar(SignUpActivity.this);

        //find xml
        fullName = findViewById(R.id.et_user_full_name);
        userName = findViewById(R.id.et_user_name);
        textView_finger = findViewById(R.id.tv_finger_signup);
        button_signup = findViewById(R.id.btn_sign_up);
        msg_finger = findViewById(R.id.tv_msg_signup);
        textView_device_code = findViewById(R.id.tv_device_code);

        //biometric
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                msg_finger.setText("Use scanner to login..");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msg_finger.setText("Device don't have fingerprint");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg_finger.setText("Biometric sensor unavailable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msg_finger.setText("Device have don't any fingerprint saved, please check your settings");
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                showToast("Error...");
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                @SuppressLint("HardwareIds")
                final String device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                textView_device_code.setText(device_id);
                textView_finger.setText("Done!");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToast("failed");
            }
        });
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("login")
                .setSubtitle("use fingerprint to login")
                .setNegativeButtonText("cancel")
                .build();

        //text view finger click listener
        textView_finger.setOnClickListener(v -> {
            textView_device_code.setText("");
            biometricPrompt.authenticate(promptInfo);
        });

        //sign up button click listener
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init_signUp();
            }
        });


    }

    private void init_signUp() {
        String _fullName = fullName.getText().toString();
        String _userName = userName.getText().toString();
        String _deviceId = textView_device_code.getText().toString();

        if (_fullName.isEmpty()){
            fullName.setError("Please Enter Full Name");
            fullName.requestFocus();
        }else if (_userName.isEmpty()){
            userName.setError("Please Enter User Name");
            userName.requestFocus();
        }else if (_deviceId.isEmpty()){
            showToast("Please initialize FingerPrint");
        }else {
            //insert data
            adapterProgressBar.startLoadingDialog();
            ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
            Call<ModelUser> call = apiInterFace.insert_user_data(_fullName,_userName,_deviceId);
            call.enqueue(new Callback<ModelUser>() {
                @Override
                public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
                    if (response.isSuccessful() && response.body()!=null){
                        ModelUser model = response.body();
                        if (model.isSuccess()){
                            showToast(model.getMessage());
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                            finish();
                            adapterProgressBar.dismissDialog();
                        }else {
                            showToast(model.getMessage());
                            adapterProgressBar.dismissDialog();
                        }
                    }else {
                        showToast("something wrong to insert");
                        adapterProgressBar.dismissDialog();
                    }
                }

                @Override
                public void onFailure(Call<ModelUser> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.toString());
                    adapterProgressBar.dismissDialog();
                }
            });
        }


    }
}