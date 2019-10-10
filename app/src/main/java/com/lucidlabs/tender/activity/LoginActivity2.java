package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity2 extends AppCompatActivity {

    private EditText e_username ;
    private EditText e_password ;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        e_username = (EditText) findViewById(R.id.e_username);
        e_password = (EditText) findViewById(R.id.e_password);

        progressDialog      = new ProgressDialog(this);
        String userid =  getIntent().getStringExtra("userid");

        e_username.setText(userid);
    }



    public void onSignup(View view){
        startActivity(new Intent(this,SignupActivity.class));
    }

    public void onClicklogin(View view){

        showProgress();

        final MyApplication myApplication = (MyApplication) getApplicationContext();

        final Data data =  new Data();
        data.setUserid(e_username.getText().toString().trim());
        data.setPasswd(e_password.getText().toString().trim());




        final RequestBody requestBody = new RequestBody();
        requestBody.setDevice(myApplication.getDevice());
        requestBody.setData(data);
        requestBody.setTimestamp(Utils.getCurrentTimeStamp());

        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device)requestBody.getDevice()).getDeviceid(),((Device)requestBody.getDevice()).getHost());
        Call<ResponseBody> sigup = loginService.login(new Request(requestBody));
        sigup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody responseBody = response.body();
                if(responseBody.getStatus_code().equals("LGN002")){
                    dismissProgress();

                    SharedPrefsUtils.setBooleanPreference(LoginActivity2.this,Utils.level1_new_user_flag,false);

                    SharedPrefsUtils.setStringPreference(LoginActivity2.this,Utils.level1_user_id,data.getUserid());

                    SharedPrefsUtils.setStringPreference(LoginActivity2.this,Utils.level1_user_token,responseBody.getData().getToken());
                    SharedPrefsUtils.setStringPreference(LoginActivity2.this,Utils.level1_password,e_password.getText().toString().trim());

                    Toast.makeText(LoginActivity2.this, "token: " + responseBody.getData().getToken(), Toast.LENGTH_LONG).show();

                    startActivity(new Intent(LoginActivity2.this,DashboardActivity.class));
                }else{
                    dismissProgress();
                    Toast.makeText(LoginActivity2.this,responseBody.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(LoginActivity2.this,"Check "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });




    }

    public void onResetPassword(View view){
        startActivity(new Intent(this, ForgotPassword.class));
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
