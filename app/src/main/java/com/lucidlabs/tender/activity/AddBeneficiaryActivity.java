package com.lucidlabs.tender.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.lucidlabs.tender.api.RequestBody_Beneficiary;
import com.lucidlabs.tender.api.Request_Beneficiary;
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

public class AddBeneficiaryActivity extends AppCompatActivity {
    private EditText Ben_account;
    private EditText ReBen_account;
    private EditText Nick_name;
    private EditText Email_id;
    private EditText IFSC_code;
    private ProgressDialog progressDialog;
    public static String otp_value;
    public static EditText editText;
    public static String ben_acc, nickName,  email_id, ifsc_code;

    public static String decrypted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beneficiary);
         Ben_account = findViewById(R.id.add_ben);
         ReBen_account = findViewById(R.id.readd_ben);
         Nick_name = findViewById(R.id.nick_name);
         Email_id = findViewById(R.id.email_id);
         IFSC_code = findViewById(R.id.ifsc_code);
         progressDialog      = new ProgressDialog(this);


        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setTitle("Add Beneficiary");
            actionbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.logout));
            actionbar.inflateMenu(R.menu.login_menu);
            setSupportActionBar(actionbar);
            actionbar.setTitle("Add Beneficiary");
        }

    }

    public void getBeneficiaryText(View view){
        ben_acc = Ben_account.getText().toString();
        if(ben_acc.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please mention account no.", Toast.LENGTH_SHORT).show();
        }
        String reben_acc = ReBen_account.getText().toString().trim();
        if(reben_acc.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please mention confirm account no.", Toast.LENGTH_SHORT).show();
        }
        if(!ben_acc.equals(reben_acc)){
            Toast.makeText(this,"Beneficiary Account Doesn't Match", Toast.LENGTH_LONG).show();
            return;
        }
        nickName = Nick_name.getText().toString().trim();
        if(nickName.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please mention nick name", Toast.LENGTH_SHORT).show();
        }
         email_id = Email_id.getText().toString().trim();
        if(email_id.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please mention email id", Toast.LENGTH_SHORT).show();
        }
         ifsc_code = IFSC_code.getText().toString().trim();
        if(ifsc_code.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please mention ifsc code", Toast.LENGTH_SHORT).show();
        }

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_verify_otp, null);

         editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);
        String tokyo = SharedPrefsUtils.getStringPreference(AddBeneficiaryActivity.this,Utils.level1_user_token);
        Toast.makeText(AddBeneficiaryActivity.this, "" + tokyo, Toast.LENGTH_SHORT).show();
        showProgress();

        final MyApplication myApplication = (MyApplication) getApplicationContext();
        Data userdata = new Data();

        // otp_type = 4 i.e. adding beneficiary
        userdata.setOtp_type("1");
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
                Toast.makeText(AddBeneficiaryActivity.this, "Button 2", Toast.LENGTH_SHORT).show();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
                otp_value = editText.getText().toString();
                if(otp_value.length() < 6) {
                    Toast.makeText(AddBeneficiaryActivity.this,"OTP should consist of only Six Character", Toast.LENGTH_LONG).show();
                    dialogBuilder.show();
                }
                else if (otp_value.length() > 6){
                    Toast.makeText(AddBeneficiaryActivity.this,"OTP should consist of only Six Character", Toast.LENGTH_LONG).show();
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
                    SharedPrefsUtils.setBooleanPreference(AddBeneficiaryActivity.this,Utils.level1_new_user_flag,false);
                    SharedPrefsUtils.setStringPreference(AddBeneficiaryActivity.this,Utils.level1_user_id,responseBody.getData().getUserid());
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
                    Toast.makeText(AddBeneficiaryActivity.this,"OTP Succesfully Created",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(AddBeneficiaryActivity.this, "" + decrypted, Toast.LENGTH_LONG).show();
                    addNotification(decrypted);

                }else{
                    Toast.makeText(AddBeneficiaryActivity.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(AddBeneficiaryActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void verify_otp(String otp_value){
        String tokyo = SharedPrefsUtils.getStringPreference(AddBeneficiaryActivity.this,Utils.level1_user_token);
        Toast.makeText(AddBeneficiaryActivity.this, "" + tokyo, Toast.LENGTH_SHORT).show();
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
                    SharedPrefsUtils.setBooleanPreference(AddBeneficiaryActivity.this, Utils.level1_new_user_flag, false);



                    SharedPrefsUtils.setStringPreference(AddBeneficiaryActivity.this,Utils.level1_otp_response,responseBody1.getData().get_response());


                    Toast.makeText(AddBeneficiaryActivity.this, "otp_response: " + responseBody1.getData().get_response() , Toast.LENGTH_LONG).show();
                    Toast.makeText(AddBeneficiaryActivity.this, "OTP Succesfully Verified", Toast.LENGTH_SHORT).show();

                    add_benef();

                } else {
                    Toast.makeText(AddBeneficiaryActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(AddBeneficiaryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void add_benef(){
        String tokeno = SharedPrefsUtils.getStringPreference(AddBeneficiaryActivity.this,Utils.level1_user_token);
        String respeno = SharedPrefsUtils.getStringPreference(AddBeneficiaryActivity.this,Utils.level1_otp_response);
        showProgress();

        final MyApplication myApplication = (MyApplication) getApplicationContext();

        Data_New userdata1 = new Data_New();

        userdata1.setAccount_number(ben_acc);
        userdata1.setIfsc_code(ifsc_code);

        //set Alias name

        userdata1.setAlias(nickName);




        userdata1.setOtp_response(respeno);
        userdata1.setEmail(email_id);
        final RequestBody_Beneficiary requestBodyBeneficiary = new RequestBody_Beneficiary();
        requestBodyBeneficiary.setTimestamp(Utils.getCurrentTimeStamp());
        requestBodyBeneficiary.setDevice(myApplication.getDevice());
        requestBodyBeneficiary.setToken(tokeno);

        requestBodyBeneficiary.setData(userdata1);
        set_add_benacc(requestBodyBeneficiary);

    }


    private void set_add_benacc(final RequestBody_Beneficiary requestBody) {
        showProgress();
        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device) requestBody.getDevice()).getDeviceid(), ((Device) requestBody.getDevice()).getHost());
        Call<ResponseBody> sigup1 = loginService.addbeneficiary(new Request_Beneficiary(requestBody));
        sigup1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody responseBody1 = response.body();
                dismissProgress();

                if (responseBody1.getStatus_code().equals("BNF001")) {

                    Toast.makeText(AddBeneficiaryActivity.this, "Beneficiary Successful Added", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(AddBeneficiaryActivity.this, DashboardActivity.class));

                } else {
                    Toast.makeText(AddBeneficiaryActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(AddBeneficiaryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
                            .setContentTitle("OTP Beneficiary Added") //set title of notification
                            .setContentText(decrypted)//this is notification message
                            .setAutoCancel(true) // makes auto cancel of notification
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification



            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }


