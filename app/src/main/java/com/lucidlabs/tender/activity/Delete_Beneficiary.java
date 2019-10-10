package com.lucidlabs.tender.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lucidlabs.tender.MyApplication;
import com.lucidlabs.tender.R;
import com.lucidlabs.tender.api.APIInterface;
import com.lucidlabs.tender.api.Data;
import com.lucidlabs.tender.api.Data_Alias;
import com.lucidlabs.tender.api.Data_New;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.api.Request;
import com.lucidlabs.tender.api.RequestBody;
import com.lucidlabs.tender.api.RequestBody_Alias;
import com.lucidlabs.tender.api.RequestBody_Beneficiary;
import com.lucidlabs.tender.api.Request_Alias;
import com.lucidlabs.tender.api.Request_Beneficiary;
import com.lucidlabs.tender.api.ResponseBody;
import com.lucidlabs.tender.api.ResponseBody_Alias;
import com.lucidlabs.tender.api.ResponseBody_Beneficiary;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delete_Beneficiary extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListView mainListView;

    private ProgressDialog progressDialog;
    private Context mContext;
    public static String otp_value;
    public static EditText editText;
    public static String decrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__beneficiary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext =  Delete_Beneficiary.this;
        mainListView = findViewById(R.id.mainListView);

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setTitle("Delete Beneficiaries");
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

        progressDialog      = new ProgressDialog(this);
        fetchBeneficiary();

    }
    private void fetchBeneficiary(){
        Data_New userdata1 = new Data_New();
        String tokeno = SharedPrefsUtils.getStringPreference(Delete_Beneficiary.this, Utils.level1_user_token);
        String alias = SharedPrefsUtils.getStringPreference(Delete_Beneficiary.this, Utils.level1_alias_name);
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        final RequestBody_Beneficiary requestBodyBeneficiary = new RequestBody_Beneficiary();
        requestBodyBeneficiary.setTimestamp(Utils.getCurrentTimeStamp());
        requestBodyBeneficiary.setDevice(myApplication.getDevice());
        requestBodyBeneficiary.setToken(tokeno);
        userdata1.setAlias(alias);

        requestBodyBeneficiary.setData(userdata1);





        showProgress();
        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device)requestBodyBeneficiary.getDevice()).getDeviceid(),((Device)requestBodyBeneficiary.getDevice()).getHost());
        Call<ResponseBody_Beneficiary> sigup = loginService.fetchBenificiary(new Request_Beneficiary(requestBodyBeneficiary));
        sigup.enqueue(new Callback<ResponseBody_Beneficiary>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ResponseBody_Beneficiary> call, Response<ResponseBody_Beneficiary> response) {

                ResponseBody_Beneficiary responseBody = response.body();
                dismissProgress();
                if(responseBody.getStatus_code().equals("BNF012")){


                    try {

                        ArrayList<String> modelRecyclerArrayList = new ArrayList<>();


                        modelRecyclerArrayList.add("ALIAS: " +responseBody.getData().getAlias());
                        modelRecyclerArrayList.add("Email: " +responseBody.getData().getEmail());
                        modelRecyclerArrayList.add("ACCOUNT NO.: " +responseBody.getData().getAccountNumber());
                        modelRecyclerArrayList.add("IFSC code : " +responseBody.getData().getIfscCode());
                        modelRecyclerArrayList.add("Creation DateTime : " +responseBody.getData().getCreationDateTime());
                        ArrayAdapter<String> itemsAdapter =
                                new ArrayAdapter<String>(Delete_Beneficiary.this, android.R.layout.simple_list_item_1, modelRecyclerArrayList);

                        mainListView.setAdapter(itemsAdapter);

//



                    }
                    catch(Exception e){
                        Toast.makeText(myApplication, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                    } }else{
                    Toast.makeText(mContext,responseBody.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody_Beneficiary> call, Throwable t) {
                dismissProgress();
                Toast.makeText(mContext,"Check "+ t.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

    }

    public void onDelete(View view) {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_verify_otp, null);

        editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);
        String tokyo = SharedPrefsUtils.getStringPreference(Delete_Beneficiary.this,Utils.level1_user_token);
        Toast.makeText(Delete_Beneficiary.this, "" + tokyo, Toast.LENGTH_SHORT).show();
        showProgress();

        final MyApplication myApplication = (MyApplication) getApplicationContext();
        Data userdata = new Data();

        // otp_type = 2 i.e. deleting beneficiary
        userdata.setOtp_type("2");
        final RequestBody requestBody = new RequestBody();
        requestBody.setTimestamp(Utils.getCurrentTimeStamp());
        requestBody.setDevice(myApplication.getDevice());
        requestBody.setToken(tokyo);
        requestBody.setData(userdata);
        getotp(requestBody);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
                otp_value = editText.getText().toString();
                if(otp_value.length() < 6) {
                    Toast.makeText(Delete_Beneficiary.this,"OTP should consist of only Six Character", Toast.LENGTH_LONG).show();
                    dialogBuilder.show();
                }
                else if (otp_value.length() > 6){
                    Toast.makeText(Delete_Beneficiary.this,"OTP should consist of only Six Character", Toast.LENGTH_LONG).show();
                    dialogBuilder.show();
                }
                else {
                    verify_otp(otp_value);
                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void getotp(final RequestBody requestBody){
        showProgress();

        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device)requestBody.getDevice()).getDeviceid(),((Device)requestBody.getDevice()).getHost());
        Call<ResponseBody> sigup = loginService.getotp(new Request(requestBody));
        sigup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody responseBody = response.body();
                dismissProgress();
                if(responseBody.getStatus_code().equals("OTP001")){
                    SharedPrefsUtils.setBooleanPreference(Delete_Beneficiary.this,Utils.level1_new_user_flag,false);
                    SharedPrefsUtils.setStringPreference(Delete_Beneficiary.this,Utils.level1_user_id,responseBody.getData().getUserid());
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
                    Toast.makeText(Delete_Beneficiary.this,"OTP Succesfully Created",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(AddBeneficiaryActivity.this, "" + decrypted, Toast.LENGTH_LONG).show();
                    addNotification(decrypted);

                }else{
                    Toast.makeText(Delete_Beneficiary.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(Delete_Beneficiary.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void verify_otp(String otp_value){
        String tokyo = SharedPrefsUtils.getStringPreference(Delete_Beneficiary.this,Utils.level1_user_token);
        //Toast.makeText(Delete_Beneficiary.this, "" + tokyo, Toast.LENGTH_SHORT).show();
        showProgress();

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
                    SharedPrefsUtils.setBooleanPreference(Delete_Beneficiary.this, Utils.level1_new_user_flag, false);



                    SharedPrefsUtils.setStringPreference(Delete_Beneficiary.this,Utils.level1_otp_response,responseBody1.getData().get_response());


                    //Toast.makeText(Delete_Beneficiary.this, "otp_response: " + responseBody1.getData().get_response() , Toast.LENGTH_LONG).show();
                    Toast.makeText(Delete_Beneficiary.this, "OTP Succesfully Verified", Toast.LENGTH_SHORT).show();

                    delete_benef();

                } else {
                    Toast.makeText(Delete_Beneficiary.this, responseBody1.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(Delete_Beneficiary.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



public void delete_benef() {

    Data_New userdata1 = new Data_New();
    String tokeno = SharedPrefsUtils.getStringPreference(Delete_Beneficiary.this, Utils.level1_user_token);
    String alias = SharedPrefsUtils.getStringPreference(Delete_Beneficiary.this, Utils.level1_alias_name);
    String otp_response = SharedPrefsUtils.getStringPreference(Delete_Beneficiary.this, Utils.level1_otp_response);
    final MyApplication myApplication = (MyApplication) getApplicationContext();
    final RequestBody_Beneficiary requestBodyBeneficiary = new RequestBody_Beneficiary();
    requestBodyBeneficiary.setTimestamp(Utils.getCurrentTimeStamp());
    requestBodyBeneficiary.setDevice(myApplication.getDevice());
    requestBodyBeneficiary.setToken(tokeno);
    userdata1.setAlias(alias);
    userdata1.setOtp_response(otp_response);
    requestBodyBeneficiary.setData(userdata1);


    showProgress();
    APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device) requestBodyBeneficiary.getDevice()).getDeviceid(), ((Device) requestBodyBeneficiary.getDevice()).getHost());
    Call<ResponseBody_Beneficiary> sigup = loginService.deleteBenificiary(new Request_Beneficiary(requestBodyBeneficiary));
    sigup.enqueue(new Callback<ResponseBody_Beneficiary>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onResponse(Call<ResponseBody_Beneficiary> call, Response<ResponseBody_Beneficiary> response) {

            ResponseBody_Beneficiary responseBody = response.body();
            dismissProgress();
            if (responseBody.getStatus_code().equals("BNF014")) {
                Toast.makeText(myApplication, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Delete_Beneficiary.this, BeneficiaryActivity.class));
                finish();

            } else {
                Toast.makeText(mContext, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
        @Override
        public void onFailure(Call<ResponseBody_Beneficiary> call, Throwable t) {
            dismissProgress();
            Toast.makeText(mContext, "Check " + t.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(t.getMessage());
        }
    });

}

    private void addNotification(String decrypted) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.btn_star) //set icon for notification
                        .setContentTitle("OTP Beneficiary Deletion") //set title of notification
                        .setContentText(decrypted)//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification



        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
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
