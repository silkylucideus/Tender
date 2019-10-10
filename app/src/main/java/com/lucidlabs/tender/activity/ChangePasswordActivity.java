package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Context mContext;
    private EditText old_pass ;
    private EditText new_pass ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);

        old_pass = (EditText) findViewById(R.id.old_pass);
        new_pass = (EditText) findViewById(R.id.new_pass);

        progressDialog      = new ProgressDialog(this);
        if (null != actionbar) {
            actionbar.setTitle("Change Password");
            actionbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.logout));
            actionbar.inflateMenu(R.menu.login_menu);
            setSupportActionBar(actionbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }
    public void onClickSubmit(View view){



        final MyApplication myApplication = (MyApplication) getApplicationContext();

        final Data data =  new Data();
        String pass = SharedPrefsUtils.getStringPreference(ChangePasswordActivity.this,Utils.level1_password);

        if(pass.equals(old_pass.getText().toString().trim()))
        {
            Toast.makeText(myApplication, "yes", Toast.LENGTH_SHORT).show();
            showProgress();

            data.setNew_pass(new_pass.getText().toString().trim());
            data.setOld_pass(old_pass.getText().toString().trim());

            final RequestBody requestBody = new RequestBody();
            requestBody.setDevice(myApplication.getDevice());
            String tokeno = SharedPrefsUtils.getStringPreference(ChangePasswordActivity.this,Utils.level1_user_token);
            requestBody.setData(data);
            requestBody.setToken(tokeno);

            requestBody.setTimestamp(Utils.getCurrentTimeStamp());

            APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device)requestBody.getDevice()).getDeviceid(),((Device)requestBody.getDevice()).getHost());
            Call<ResponseBody> sigup = loginService.changePassword(new Request(requestBody));
            sigup.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    ResponseBody responseBody = response.body();
                    if(responseBody.getStatus_code().equals("PSW008")){
                        dismissProgress();

                        SharedPrefsUtils.setBooleanPreference(ChangePasswordActivity.this,Utils.level1_new_user_flag,false);





                        startActivity(new Intent(ChangePasswordActivity.this,DashboardActivity.class));
                    }else{
                        dismissProgress();
                        Toast.makeText(ChangePasswordActivity.this,"" + responseBody.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgress();
                    Toast.makeText(ChangePasswordActivity.this,"" +t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(myApplication, "Please Check Old Password", Toast.LENGTH_SHORT).show();
        }


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
