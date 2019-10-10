package com.lucidlabs.tender.activity;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lucidlabs.tender.MyApplication;
import com.lucidlabs.tender.R;
import com.lucidlabs.tender.adapter.BenficiaryAdapter;
import com.lucidlabs.tender.api.APIInterface;
import com.lucidlabs.tender.api.Data;
import com.lucidlabs.tender.api.Data_Alias;
import com.lucidlabs.tender.api.Data_New;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.api.Request;
import com.lucidlabs.tender.api.RequestBody;
import com.lucidlabs.tender.api.RequestBody_Alias;
import com.lucidlabs.tender.api.Request_Alias;
import com.lucidlabs.tender.api.ResponseBody;
import com.lucidlabs.tender.api.ResponseBody_Alias;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BENEFICIARY_FUND extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListView mainListView;

    private ProgressDialog progressDialog;
    private Context mContext;
    public static String decrypted;
    public static String otp_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary__fund);

        mContext =  BENEFICIARY_FUND.this;
        mainListView = findViewById(R.id.mainListView);

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setTitle("Beneficiaries Fund Transfer");
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
        // recyclerView        = (RecyclerView) findViewById(R.id.my_recycler_view);



        fetchBeneficiary();

    }


    private void fetchBeneficiary(){
        Data_Alias userdata1 = new Data_Alias();
        String tokeno = SharedPrefsUtils.getStringPreference(BENEFICIARY_FUND.this,Utils.level1_user_token);
        final MyApplication myApplication = (MyApplication) getApplicationContext();
        final RequestBody_Alias requestBodyBeneficiary = new RequestBody_Alias();
        requestBodyBeneficiary.setTimestamp(Utils.getCurrentTimeStamp());
        requestBodyBeneficiary.setDevice(myApplication.getDevice());
        requestBodyBeneficiary.setToken(tokeno);

        requestBodyBeneficiary.setData(userdata1);





        showProgress();
        APIInterface loginService = ServiceGenerator.createService(APIInterface.class, ((Device)requestBodyBeneficiary.getDevice()).getDeviceid(),((Device)requestBodyBeneficiary.getDevice()).getHost());
        Call<ResponseBody_Alias> sigup = loginService.viewBenificiary(new Request_Alias(requestBodyBeneficiary));
        sigup.enqueue(new Callback<ResponseBody_Alias>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ResponseBody_Alias> call, Response<ResponseBody_Alias> response) {

                ResponseBody_Alias responseBody = response.body();
                dismissProgress();
                if(responseBody.getStatus_code().equals("BNF011")){


                    try {
                        ArrayList<String> modelRecyclerArrayList = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(responseBody.getData().getAlias());
                        String[] strArr = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            strArr[i] = jsonArray.getString(i);
                            modelRecyclerArrayList.add(strArr[i]);
                            ArrayAdapter<String> itemsAdapter =
                                    new ArrayAdapter<String>(BENEFICIARY_FUND.this, android.R.layout.simple_list_item_1, modelRecyclerArrayList);

                            mainListView.setAdapter(itemsAdapter);

                            mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position,
                                                        long id) {
                                    Toast.makeText(myApplication, "hello " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                                    SharedPrefsUtils.setStringPreference(BENEFICIARY_FUND.this,Utils.level1_alias_name, parent.getItemAtPosition(position).toString());
                                    generateOTP();
                                    startActivity(new Intent(BENEFICIARY_FUND.this, FUND_OTP_Verify.class));
                                }
                            });

                        }

                    }
                    catch(JSONException e){
                        Toast.makeText(myApplication, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                    } }else{
                    Toast.makeText(mContext,responseBody.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody_Alias> call, Throwable t) {
                dismissProgress();
                Toast.makeText(mContext,"Check "+ t.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

    }

    public void generateOTP()
    {
        String tokyo = SharedPrefsUtils.getStringPreference(BENEFICIARY_FUND.this,Utils.level1_user_token);
        Toast.makeText(BENEFICIARY_FUND.this, "" + tokyo, Toast.LENGTH_SHORT).show();
        showProgress();

        final MyApplication myApplication = (MyApplication) getApplicationContext();
        Data userdata = new Data();

        // otp_type = 2 i.e. fund-transfer
        userdata.setOtp_type("3");
        final RequestBody requestBody = new RequestBody();
        requestBody.setTimestamp(Utils.getCurrentTimeStamp());
        requestBody.setDevice(myApplication.getDevice());
        requestBody.setToken(tokyo);
        requestBody.setData(userdata);
        getotp(requestBody);



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
                    SharedPrefsUtils.setBooleanPreference(BENEFICIARY_FUND.this,Utils.level1_new_user_flag,false);

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
                    Toast.makeText(BENEFICIARY_FUND.this,"OTP Succesfully Created",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(AddBeneficiaryActivity.this, "" + decrypted, Toast.LENGTH_LONG).show();
                    addNotification(decrypted);

                }else{
                    Toast.makeText(BENEFICIARY_FUND.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgress();
                Toast.makeText(BENEFICIARY_FUND.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
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


