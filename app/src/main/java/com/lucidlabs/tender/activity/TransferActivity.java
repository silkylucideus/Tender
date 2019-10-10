package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.lucidlabs.tender.api.Data_New;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.api.Request;
import com.lucidlabs.tender.api.RequestBody;
import com.lucidlabs.tender.api.RequestBody_Beneficiary;
import com.lucidlabs.tender.api.Request_Beneficiary;
import com.lucidlabs.tender.api.ResponseBody;
import com.lucidlabs.tender.api.ResponseBody_Beneficiary;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferActivity extends AppCompatActivity {

    EditText description, amount;
    private ProgressDialog progressDialog;
    String otp_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setTitle("Fund Transfer");
            actionbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.logout));
            description = findViewById(R.id.description);
            amount = findViewById(R.id.amount);
            progressDialog      = new ProgressDialog(this);
        }
    }
    public void onClickSubmit(View view){
        showProgress();
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        final Data_New userdata = new Data_New();

        String tokeno = SharedPrefsUtils.getStringPreference(TransferActivity.this,Utils.level1_user_token);
        String user_id = SharedPrefsUtils.getStringPreference(TransferActivity.this, Utils.level1_alias_name);
        Toast.makeText(myApplication, "aliasname *** " +user_id, Toast.LENGTH_SHORT).show();

        otp_response = SharedPrefsUtils.getStringPreference(TransferActivity.this,Utils.level1_otp_response);
        Toast.makeText(myApplication, "otp_response *** " +otp_response, Toast.LENGTH_SHORT).show();



        userdata.setOtp_response(otp_response);
        userdata.setAmount(amount.getText().toString().trim());
        userdata.setRemarks(description.getText().toString().trim());
        userdata.setAlias(user_id);




        final RequestBody_Beneficiary requestBody = new RequestBody_Beneficiary();
        requestBody.setTimestamp(Utils.getCurrentTimeStamp());
        requestBody.setDevice(myApplication.getDevice());
        requestBody.setToken(tokeno);
        requestBody.setData(userdata);


        //add beneficiary such as alias


        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device) requestBody.getDevice()).getDeviceid(), ((Device) requestBody.getDevice()).getHost());
        Call<ResponseBody_Beneficiary> sigup = loginService.transferFund(new Request_Beneficiary(requestBody));
        sigup.enqueue(new Callback<ResponseBody_Beneficiary>() {
            @Override
            public void onResponse(Call<ResponseBody_Beneficiary> call, Response<ResponseBody_Beneficiary> response) {

                ResponseBody_Beneficiary responseBody = response.body();
                if (responseBody.getStatus_code().equals("BNF014")) {
                    dismissProgress();
                    Toast.makeText(myApplication, "Success", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(TransferActivity.this,LoginActivity2.class));

                } else {

                    dismissProgress();
                    // Username Enumeration
                    Toast.makeText(TransferActivity.this, "" + responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody_Beneficiary> call, Throwable t) {
                dismissProgress();
                Toast.makeText(TransferActivity.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();
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
