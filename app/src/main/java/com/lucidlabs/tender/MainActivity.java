package com.lucidlabs.tender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lucidlabs.tender.activity.LoginActivity2;
import com.lucidlabs.tender.activity.SignupActivity;
import com.lucidlabs.tender.api.Device;
import com.lucidlabs.tender.utils.SharedPrefsUtils;
import com.lucidlabs.tender.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPrefsUtils.setStringPreference(this,Utils.level1_device_id,"I12121212122121");

        String id   =   SharedPrefsUtils.getStringPreference(this,Utils.level1_device_id);

        final MyApplication myApplication = (MyApplication) getApplicationContext();
        Device device = new Device(id,"Android","lucideustech.com");
        myApplication.setDevice(device);

        if(SharedPrefsUtils.getBooleanPreference(this, Utils.level1_new_user_flag,true)){
            startActivity(new Intent(this, LoginActivity2.class));
        }else{
            startActivity(new Intent(this, LoginActivity2.class));
        }
        finish();

    }
}
