package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    EditText new_pass;
    Button btn_sub;
    private ProgressDialog progressDialog;
    String otp_response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog      = new ProgressDialog(this);

        new_pass = findViewById(R.id.newpass);
        btn_sub = findViewById(R.id.btn_submit);





    }



    public void onClickSubmit(View view){
        showProgress();
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        final Data userdata = new Data();


        String user_id = SharedPrefsUtils.getStringPreference(ResetPassword.this,Utils.level1_user_name);
        Toast.makeText(myApplication, "user_id *** " +user_id, Toast.LENGTH_SHORT).show();

         otp_response = SharedPrefsUtils.getStringPreference(ResetPassword.this,Utils.level1_otp_response);
        Toast.makeText(myApplication, "otp_response *** " +otp_response, Toast.LENGTH_SHORT).show();


            userdata.setOtp_response(otp_response);
            userdata.setNew_pass(new_pass.getText().toString().trim());
            userdata.setUserid(user_id);
            final RequestBody requestBody = new RequestBody();
            requestBody.setTimestamp(Utils.getCurrentTimeStamp());
            requestBody.setDevice(myApplication.getDevice());

            requestBody.setData(userdata);
            APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device) requestBody.getDevice()).getDeviceid(), ((Device) requestBody.getDevice()).getHost());
            Call<ResponseBody> sigup = loginService.resetPassword(new Request(requestBody));
            sigup.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    ResponseBody responseBody = response.body();
                    if (responseBody.getStatus_code().equals("PSW004")) {
                        dismissProgress();
                        Toast.makeText(myApplication, "Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPassword.this,LoginActivity2.class));

                    } else {

                        dismissProgress();
                        // Username Enumeration
                        Toast.makeText(ResetPassword.this, "" + responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgress();
                    Toast.makeText(ResetPassword.this, ""+ t.getMessage(), Toast.LENGTH_LONG).show();
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

        private void dismissProgress(){
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
        }

    }


