package com.lucidlabs.tender;

import android.app.Application;

import com.lucidlabs.tender.api.Device;

public class MyApplication extends Application {

    public Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
