package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lucidlabs.tender.MyApplication;
import com.lucidlabs.tender.R;
import com.lucidlabs.tender.adapter.MyListAdapter;
import com.lucidlabs.tender.api.APIInterface;
import com.lucidlabs.tender.api.Data;
import com.lucidlabs.tender.api.Data_Account;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.api.Request;
import com.lucidlabs.tender.api.RequestBody;
import com.lucidlabs.tender.api.RequestBody_Account;
import com.lucidlabs.tender.api.Request_Account;
import com.lucidlabs.tender.api.ResponseBody;
import com.lucidlabs.tender.api.ResponseBody_Account;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Account_Details extends AppCompatActivity {

    private ListView mainListView;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;



    //String[] maintitle;



    Integer[] imgid={
           R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,
            R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,
            R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,R.drawable.view_b,
            R.drawable.view_b
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //recyclerView = findViewById(R.id.card_recycler_view);
        progressDialog      = new ProgressDialog(this);
        mainListView = findViewById(R.id.mainListView);

        fetchAccountDetails();


    }


    private void fetchAccountDetails() {
        Data_Account userdata1 = new Data_Account();
        String tokeno = SharedPrefsUtils.getStringPreference(Account_Details.this, Utils.level1_user_token);
        String user_id = SharedPrefsUtils.getStringPreference(Account_Details.this, Utils.level1_user_id);
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
                    ArrayList<String> modelRecyclerArrayList = new ArrayList<>();


                    modelRecyclerArrayList.add("UserId: " +responseBody.getData().getUserId());
                    modelRecyclerArrayList.add("Account No.: " +responseBody.getData().getAccountNumber());
                    modelRecyclerArrayList.add("Account Balance: " +responseBody.getData().getAccountBalance());
                    modelRecyclerArrayList.add("Income Tax Number: " +responseBody.getData().getIncomeTaxNumber());
                    modelRecyclerArrayList.add("Open Date: " +responseBody.getData().getOpenDate());
                    modelRecyclerArrayList.add("Currency: " +responseBody.getData().getCurrency());
                    modelRecyclerArrayList.add("First Name: " +responseBody.getData().getFname());
                    modelRecyclerArrayList.add("Last Name: " +responseBody.getData().getLname());
                    modelRecyclerArrayList.add("Address: " +responseBody.getData().getAddress());
                    modelRecyclerArrayList.add("DOB: " +responseBody.getData().getDob());
                    modelRecyclerArrayList.add("Mobile No.: " +responseBody.getData().getMobileNo());
                    modelRecyclerArrayList.add("Email: " +responseBody.getData().getEmail());
                    modelRecyclerArrayList.add("AadharId: " +responseBody.getData().getAadharId());
                    modelRecyclerArrayList.add("PancardId: " +responseBody.getData().getPanCardId());
                    modelRecyclerArrayList.add("WalletId: " +responseBody.getData().getWalletId());
                    modelRecyclerArrayList.add("CountryId: " +responseBody.getData().getCountryId());


                    //ArrayAdapter<String> itemsAdapter =
                           // new ArrayAdapter<String>(Account_Details.this, android.R.layout.simple_list_item_1, modelRecyclerArrayList);

                   //mainListView.setAdapter(itemsAdapter);


                    MyListAdapter adapter= new MyListAdapter(modelRecyclerArrayList,imgid,getApplicationContext());

                    mainListView.setAdapter(adapter);

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

//
private void writeRecycler(String response) {

//    try {
//        //getting the whole json object from the response
//        JSONObject obj = new JSONObject(response);
//        Toast.makeText(this, "Object "+ obj, Toast.LENGTH_SHORT).show();
//
//
//            ArrayList<Data> modelRecyclerArrayList = new ArrayList<>();
//            JSONArray dataArray = obj.getJSONArray("data");
//
//            for (int i = 0; i < dataArray.length(); i++) {
//
//                Data modelRecycler = new Data();
//                JSONObject dataobj = dataArray.getJSONObject(i);
//
//                modelRecycler.setAadharNo(dataobj.getString("aadharId"));
//                modelRecycler.setAccNo(dataobj.getString("accountNumber"));
//                modelRecycler.setFirstname(dataobj.getString("fname"));
//
//
//                modelRecyclerArrayList.add(modelRecycler);
//            }
//
//                retrofitAdapter = new DataAdapter(modelRecyclerArrayList);
//                recyclerView.setAdapter(retrofitAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
//
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }

}
}
