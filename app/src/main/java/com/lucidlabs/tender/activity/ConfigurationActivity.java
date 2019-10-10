package com.lucidlabs.tender.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lucidlabs.tender.R;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

public class ConfigurationActivity extends AppCompatActivity {


    private EditText e_name;
    private EditText e_url;
    private EditText e_port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Configuration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        e_name   = (EditText)findViewById(R.id.e_name);
        e_url    = (EditText)findViewById(R.id.e_url);
        e_port   = (EditText)findViewById(R.id.e_port);
        e_name.setText(SharedPrefsUtils.getStringPreference(this,Utils.analyst_name));
        e_url.setText(SharedPrefsUtils.getStringPreference(this,Utils.enviroment_url));
        e_port.setText(SharedPrefsUtils.getStringPreference(this,Utils.enviroment_port));

    }


    public void saveOptions(View view){
        String s_url            = e_url.getText().toString();
        String s_port           = e_port.getText().toString();
        String s_analyst        = e_name.getText().toString();
        if(s_analyst.isEmpty() || s_url.isEmpty() || s_analyst.isEmpty()){
            Toast.makeText(this,"All Fields Are Necessary",Toast.LENGTH_SHORT).show();
        }else {
            SharedPrefsUtils.setStringPreference(this,Utils.analyst_name,s_analyst);
            SharedPrefsUtils.setStringPreference(this,Utils.enviroment_url,s_url);
            SharedPrefsUtils.setStringPreference(this,Utils.enviroment_port,s_port);
            Toast.makeText(this,"Succefully Saved",Toast.LENGTH_SHORT).show();
        }
    }



}
