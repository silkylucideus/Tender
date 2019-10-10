package com.lucidlabs.tender.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lucidlabs.tender.MyApplication;
import com.lucidlabs.tender.R;
import com.lucidlabs.tender.api.APIInterface;
import com.lucidlabs.tender.api.Data;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.api.Request;
import com.lucidlabs.tender.api.RequestBody;
import com.lucidlabs.tender.api.ResponseBody;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.EncryptionUtil;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText e_firstname;
    private EditText e_lastname;
    private EditText e_brithday;
    private EditText e_email;
    private EditText e_mobile;

    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_signup);
        initalize();
    }

    private void initalize(){
        toolbar         = (Toolbar) findViewById(R.id.toolbar);
        e_firstname     = (EditText) findViewById(R.id.e_firstname);
        e_lastname      = (EditText) findViewById(R.id.e_lastname);
        e_brithday      = (EditText) findViewById(R.id.e_birthday);
        e_email         = (EditText) findViewById(R.id.e_email);
        e_mobile        = (EditText) findViewById(R.id.e_mobile);

        progressDialog      = new ProgressDialog(this);
        toolbar.setTitle("Tender");
    }

    public void getText(View view){
        final String s_firstname  = e_firstname.getText().toString().trim();
        final String s_lastname   = e_lastname.getText().toString().trim();
        final String s_brithday   = e_brithday.getText().toString().trim();
        final String s_email      = e_email.getText().toString().trim();
        final String s_mobile     = e_mobile.getText().toString().trim();
        //@alan usages of string class for storing sensitive Data
//        final String s_pin        = e_pin.getText().toString();
//        final String s_pin_2      = e_pin2.getText().toString();
//
//        if(s_pin.length() < 4 ){
//            Toast.makeText(this,"Pin Should Consist of atleast Four Character", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if(!s_pin.equals(s_pin_2)){
//            Toast.makeText(this,"Pin Doesnt Match", Toast.LENGTH_LONG).show();
//            return;
//        }

       // Log.d("Application","Proccess: "+s_pin_2+": com.lucidlabs.tender");
        SharedPrefsUtils.setStringPreference(this,Utils.level1_shared_firstname,s_firstname);
        SharedPrefsUtils.setStringPreference(this,Utils.level1_shared_lastname,s_lastname);
        SharedPrefsUtils.setStringPreference(this,Utils.level1_shared_birthday,s_brithday);
        SharedPrefsUtils.setStringPreference(this,Utils.level1_shared_email,s_email);
        SharedPrefsUtils.setStringPreference(this,Utils.level1_shared_mobile,s_mobile);
        SharedPrefsUtils.setStringPreference(this,Utils.level1_shared_address,s_mobile);
//        try {
//            SharedPrefsUtils.setStringPreference(this,Utils.level1_shared_pin,EncryptionUtil.SHA1(s_pin));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


        Data userdata = new Data();
        userdata.setFirstname(s_firstname);
        userdata.setLastname(s_lastname);
        userdata.setGndr("1");
        userdata.setMobile(s_mobile);
        userdata.setPasswd("Dvia@123");
        userdata.setEmail(s_email);
        userdata.setCountryId("IND");
        userdata.setAddress("New Delhi, Delhi 110097");
        userdata.setDob("1992-03-08");

        final MyApplication myApplication = (MyApplication) getApplicationContext();
        RequestBody requestBody =  new RequestBody();
        requestBody.setTimestamp(Utils.getCurrentTimeStamp());
        requestBody.setData(userdata);
        requestBody.setDevice(myApplication.getDevice());

        signupApi(requestBody);
    }

    private void signupApi(final RequestBody requestBody){
        showProgress();
        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device)requestBody.getDevice()).getDeviceid(),((Device)requestBody.getDevice()).getHost());
        Call<ResponseBody> sigup = loginService.signup(new Request(requestBody));
        sigup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody responseBody = response.body();
                dismissProgress();
                if(responseBody.getStatus_code().equals("SNUP02")){
                    SharedPrefsUtils.setBooleanPreference(SignupActivity.this,Utils.level1_new_user_flag,false);
                    SharedPrefsUtils.setStringPreference(SignupActivity.this,Utils.level1_user_id,responseBody.getData().getUserid());
                    //SharedPrefsUtils.setStringPreference(SignupActivity.this,Utils.level1_user_account,responseBody.getData().getAccNo());
                    Toast.makeText(SignupActivity.this,"Account Succesfully Created",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SignupActivity.this,LoginActivity2.class));
                    addNotification(responseBody.getData().getUserId());
                }else{
                    Toast.makeText(SignupActivity.this,""+ responseBody.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(SignupActivity.this,"" + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void showProgress(){
        if(progressDialog!=null){
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
                            .setContentTitle("UserId & Password") //set title of notification
                            .setContentText("UserId: "+ decrypted + "& Password is: " + "Dvia@123")//this is notification message
                            .setAutoCancel(true) // makes auto cancel of notification
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification



            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }





