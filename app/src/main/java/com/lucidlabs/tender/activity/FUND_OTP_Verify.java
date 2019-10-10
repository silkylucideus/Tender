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

public class FUND_OTP_Verify extends AppCompatActivity {


    EditText otp;
    Button btn_sub;
    private ProgressDialog progressDialog;
    String otp_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund__otp__verify);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("OTP Verification");
        progressDialog = new ProgressDialog(this);
        otp = findViewById(R.id.otp);
        btn_sub = findViewById(R.id.btn_submit);

    }

    public void onClickSubmit(View view) {
        showProgress();

        String otp_value = otp.getText().toString().trim();
        if (otp_value.length() < 6) {
            dismissProgress();

            Toast.makeText(FUND_OTP_Verify.this, "OTP should consist of only Six Numeric Value", Toast.LENGTH_LONG).show();


        } else if (otp_value.length() > 6) {
            dismissProgress();

            Toast.makeText(FUND_OTP_Verify.this, "OTP should consist of only Six Numeric Value", Toast.LENGTH_LONG).show();

        } else {
            String tokyo = SharedPrefsUtils.getStringPreference(FUND_OTP_Verify.this, Utils.level1_user_token);
            Toast.makeText(FUND_OTP_Verify.this, "" + tokyo, Toast.LENGTH_SHORT).show();


            final MyApplication myApplication = (MyApplication) getApplicationContext();
            Data userdata = new Data();
            userdata.setOtp(otp_value);
            final RequestBody requestBody1 = new RequestBody();
            requestBody1.setTimestamp(Utils.getCurrentTimeStamp());
            requestBody1.setDevice(myApplication.getDevice());
            requestBody1.setToken(tokyo);
            requestBody1.setData(userdata);
            set_otp(requestBody1);

        }
    }


    private void set_otp(final RequestBody requestBody) {
        showProgress();
        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device) requestBody.getDevice()).getDeviceid(), ((Device) requestBody.getDevice()).getHost());
        Call<ResponseBody> sigup1 = loginService.verifyotp(new Request(requestBody));
        sigup1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody responseBody1 = response.body();
                dismissProgress();

                if (responseBody1.getStatus_code().equals("OTP003")) {
                    SharedPrefsUtils.setBooleanPreference(FUND_OTP_Verify.this, Utils.level1_new_user_flag, false);



                    SharedPrefsUtils.setStringPreference(FUND_OTP_Verify.this,Utils.level1_otp_response,responseBody1.getData().get_response());


                    Toast.makeText(FUND_OTP_Verify.this, "otp_response: " + responseBody1.getData().get_response() , Toast.LENGTH_LONG).show();
                    Toast.makeText(FUND_OTP_Verify.this, "OTP Succesfully Verified", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(FUND_OTP_Verify.this,TransferActivity.class));

                } else {
                    Toast.makeText(FUND_OTP_Verify.this, ""+ responseBody1.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(FUND_OTP_Verify.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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

    }
