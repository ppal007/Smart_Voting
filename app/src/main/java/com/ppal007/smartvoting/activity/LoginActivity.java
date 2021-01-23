package com.ppal007.smartvoting.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private void showToast(String msg){
        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
    }

    private TextView textView_msg,textView_signUp,textView_device_code;
    private ImageView imageView_finger;
    private AdapterProgressBar adapterProgressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_login);

        //init progress bar
        adapterProgressBar = new AdapterProgressBar(LoginActivity.this);

        //find xml
        textView_msg = findViewById(R.id.tv_login_msg);
        imageView_finger = findViewById(R.id.img_finger_print);
        textView_signUp = findViewById(R.id.tvRegister);
        textView_device_code = findViewById(R.id.tv_device_code_login);

        //init finger print
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                textView_msg.setText("Use scanner to login..");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                textView_msg.setText("Device don't have fingerprint");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                textView_msg.setText("Biometric sensor unavailable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                textView_msg.setText("Device have don't any fingerprint saved, please check your settings");
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
                //check device id
                chk_login(device_id);

//                showToast("success");

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
        biometricPrompt.authenticate(promptInfo);

        //img view finger click listener
        imageView_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });


        //tv signup click listener
        textView_signUp.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
            startActivity(intent);
        });



    }

    private void chk_login(String device_id) {
        adapterProgressBar.startLoadingDialog();
        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
        Call<ModelUser> call = apiInterFace.login_user(device_id);
        call.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelUser model = response.body();
                    if (model.isSuccess()){
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        intent.putExtra("ex_full_name",model.getFullName());
                        intent.putExtra("ex_user_name",model.getUserName());
                        startActivity(intent);

                    }
                    showToast(model.getMessage());
                }else {
                    showToast("something wrong to login");
                }
                adapterProgressBar.dismissDialog();
            }

            @Override
            public void onFailure(Call<ModelUser> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                adapterProgressBar.dismissDialog();
            }
        });
    }
}