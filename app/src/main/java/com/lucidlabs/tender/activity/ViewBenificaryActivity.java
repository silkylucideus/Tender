package com.lucidlabs.tender.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucidlabs.tender.MyApplication;
import com.lucidlabs.tender.R;
import com.lucidlabs.tender.adapter.BenficiaryAdapter;
import com.lucidlabs.tender.adapter.MyAdapter;
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
import com.lucidlabs.tender.api.ResponseBody_Account;
import com.lucidlabs.tender.api.ResponseBody_Alias;
import com.lucidlabs.tender.api.ResponseBody_Beneficiary;
import com.lucidlabs.tender.api.ServiceGenerator;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBenificaryActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListView mainListView;

    private ProgressDialog progressDialog;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        mContext =  ViewBenificaryActivity.this;
        mainListView = findViewById(R.id.mainListView);

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setTitle("View Beneficiaries");
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
        String tokeno = SharedPrefsUtils.getStringPreference(ViewBenificaryActivity.this,Utils.level1_user_token);
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
        //JSONArray jsonArray = new JSONArray(responseBody.getData().getResult()); getBenificiary
        JSONArray jsonArray = new JSONArray(responseBody.getData().getAlias());
        String[] strArr = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            strArr[i] = jsonArray.getString(i);
            modelRecyclerArrayList.add(strArr[i]);
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(ViewBenificaryActivity.this, android.R.layout.simple_list_item_1, modelRecyclerArrayList);

            mainListView.setAdapter(itemsAdapter);

            mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Toast.makeText(myApplication, "hello " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    SharedPrefsUtils.setStringPreference(ViewBenificaryActivity.this,Utils.level1_alias_name, parent.getItemAtPosition(position).toString());
                    startActivity(new Intent(ViewBenificaryActivity.this, Delete_Beneficiary.class));
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

    private void writeListView(String response){


        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);


                ArrayList<Data_New> modelRecyclerArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    Data_New modelRecycler = new Data_New();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    modelRecycler.setAccount_number(dataobj.getString("account_number"));
                    modelRecycler.setEmail(dataobj.getString("email"));
                    modelRecycler.setIfsc_code(dataobj.getString("ifsc_code"));
                    //modelRecycler.setAlias(dataobj.getString("alias"));

                    modelRecyclerArrayList.add(modelRecycler);

                }

                mAdapter = new BenficiaryAdapter(modelRecyclerArrayList);
                recyclerView.setAdapter(mAdapter);
                //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
