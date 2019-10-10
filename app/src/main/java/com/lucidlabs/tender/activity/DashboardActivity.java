package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lucidlabs.tender.MyApplication;
import com.lucidlabs.tender.R;
import com.lucidlabs.tender.adapter.MyListAdapter;
import com.lucidlabs.tender.api.APIInterface;
import com.lucidlabs.tender.api.Data;
import com.lucidlabs.tender.api.Data_Account;
import com.lucidlabs.tender.api.Data_New;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.api.Request;
import com.lucidlabs.tender.api.RequestBody;
import com.lucidlabs.tender.api.RequestBody_Account;
import com.lucidlabs.tender.api.RequestBody_Beneficiary;
import com.lucidlabs.tender.api.Request_Account;
import com.lucidlabs.tender.api.Request_Beneficiary;
import com.lucidlabs.tender.api.ResponseBody;
import com.lucidlabs.tender.api.ResponseBody_Account;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Context mContext;
    TextView acc_no, acc_bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        progressDialog    = new ProgressDialog(this);
        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        acc_no = findViewById(R.id.acc_no);
        acc_bal = findViewById(R.id.acc_bal);


        if (null != actionbar) {
            actionbar.setTitle("Welcome");
            actionbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.logout));
            actionbar.inflateMenu(R.menu.login_menu);
            setSupportActionBar(actionbar);
            actionbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(item.getItemId()==R.id.menu_configuration)
                    {
                        Toast.makeText(mContext, "hu lalala", Toast.LENGTH_SHORT).show();
                    }


                    return false;
                }
            });



        }
        fetchAccountDetails();

    }



    public void onClickAccountStatement(View view){
        startActivity(new Intent(this, StatementActivity.class));
    }

    public void onClickBeneficiaries(View view){
        startActivity(new Intent(this, BeneficiaryActivity.class));
    }

    public void onClickFundTransfer(View view){
        startActivity(new Intent(this, BENEFICIARY_FUND.class));

    }

    public void getAccountDetails(View view){
        startActivity(new Intent(DashboardActivity.this,Account_Details.class));
    }

    public void onClickWallet(View view){
        startActivity(new Intent(this, WalletActivity.class));
    }



    public void onChangePassword(View view){
        startActivity(new Intent(this, ChangePasswordActivity.class)); }





    public void onClickLogout(View view){
        Data userdata1 = new Data();
        String tokeno = SharedPrefsUtils.getStringPreference(DashboardActivity.this, Utils.level1_user_token);
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        final RequestBody requestBodyBeneficiary = new RequestBody();
        requestBodyBeneficiary.setTimestamp(Utils.getCurrentTimeStamp());
        requestBodyBeneficiary.setDevice(myApplication.getDevice());
        requestBodyBeneficiary.setToken(tokeno);

        requestBodyBeneficiary.setData(userdata1);


        showProgress();
        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device)requestBodyBeneficiary.getDevice()).getDeviceid(),((Device)requestBodyBeneficiary.getDevice()).getHost());
        Call<ResponseBody> sigup = loginService.logout(new Request(requestBodyBeneficiary));
        sigup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody responseBody = response.body();
                dismissProgress();
                if(responseBody.getStatus_code().equals("LGT001")){
                    Toast.makeText(myApplication, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    finish();


                }else{
                    Toast.makeText(mContext,""+responseBody.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(mContext,""+ t.getMessage(),Toast.LENGTH_SHORT).show();
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

    private void fetchAccountDetails() {
        Data_Account userdata1 = new Data_Account();
        String tokeno = SharedPrefsUtils.getStringPreference(DashboardActivity.this, Utils.level1_user_token);
        String user_id = SharedPrefsUtils.getStringPreference(DashboardActivity.this, Utils.level1_user_id);
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        final RequestBody_Account requestBody = new RequestBody_Account();
        requestBody.setTimestamp(Utils.getCurrentTimeStamp());
        requestBody.setDevice(myApplication.getDevice());
        requestBody.setToken(tokeno);
        //IDOR

        userdata1.setUserid(user_id);

        requestBody.setData(userdata1);


        showProgress();
        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device) requestBody.getDevice()).getDeviceid(), ((Device) requestBody.getDevice()).getHost());
        Call<ResponseBody_Account> sigup = loginService.getJSON(new Request_Account(requestBody));
        sigup.enqueue(new Callback<ResponseBody_Account>() {
            @Override
            public void onResponse(Call<ResponseBody_Account> call, Response<ResponseBody_Account> response) {
                ResponseBody_Account responseBody = response.body();
                if (responseBody.getStatus_code().equals("ACT002")) {
                    //Toast.makeText(myApplication, ""+ responseBody.getData().getAadharNo(), Toast.LENGTH_SHORT).show();
                    dismissProgress();


                    acc_no.setText("Account No: " + responseBody.getData().getAccountNumber());
                    acc_bal.setText("Balance: "+ responseBody.getData().getAccountBalance());



                } else {
                    Toast.makeText(myApplication, "" + responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                    dismissProgress();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody_Account> call, Throwable t) {
                Toast.makeText(myApplication, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgress();
            }


        });

}
}
