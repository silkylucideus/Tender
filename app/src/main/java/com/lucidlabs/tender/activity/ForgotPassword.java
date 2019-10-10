package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;

import com.lucidlabs.tender.MyApplication;
import com.lucidlabs.tender.R;
import com.lucidlabs.tender.api.APIInterface;
import com.lucidlabs.tender.api.Data;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.api.Request;
import com.lucidlabs.tender.api.RequestBody;
import com.lucidlabs.tender.api.ResponseBody;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    EditText user_id;
    Button btn_sub1;
    private ProgressDialog progressDialog;
    public static String decrypted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_sub1 = findViewById(R.id.btn_submit);
        progressDialog = new ProgressDialog(this);
        user_id = findViewById(R.id.user_id);

    }

    public void onClickSubmit(View view) {

        showProgress();
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        final Data userdata = new Data();

        // otp_type = 4 i.e. forgotPassword
        userdata.setOtp_type("4");
        userdata.setUserid(user_id.getText().toString().trim());
        final RequestBody requestBody = new RequestBody();
        requestBody.setTimestamp(Utils.getCurrentTimeStamp());
        requestBody.setDevice(myApplication.getDevice());

        requestBody.setData(userdata);


        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device) requestBody.getDevice()).getDeviceid(), ((Device) requestBody.getDevice()).getHost());
        Call<ResponseBody> sigup = loginService.forgotPassword(new Request(requestBody));
        sigup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody responseBody = response.body();
                if (responseBody.getStatus_code().equals("OTP001")) {
                    dismissProgress();
                    Toast.makeText(myApplication, "Success", Toast.LENGTH_SHORT).show();
                    SharedPrefsUtils.setStringPreference(ForgotPassword.this, Utils.level1_user_name, requestBody.getData().getUserid());
                    String respo = responseBody.getData().get_response();
                    try {
                        String key = "9bbc0d79e686e847bc305c9bd4cc2ea6";
                        String iv_value = "0123456789abcdef";


                        byte[] data = Base64.decode(respo, Base64.DEFAULT);


                        IvParameterSpec iv = new IvParameterSpec(iv_value.getBytes("UTF-8"));
                        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                        Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");

                        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                        decrypted = new String(cipher.doFinal(data));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    addNotification(decrypted);
                   // Toast.makeText(myApplication, "" + decrypted, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this, OTP_Verify.class));


                } else {

                    dismissProgress();
                    // Username Enumeration
                    Toast.makeText(ForgotPassword.this, "" + responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(ForgotPassword.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showProgress() {
        if (progressDialog != null) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    private void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void addNotification(String decrypted) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.btn_star) //set icon for notification
                        .setContentTitle("OTP Reset Password") //set title of notification
                        .setContentText(decrypted)//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification



    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
}
}