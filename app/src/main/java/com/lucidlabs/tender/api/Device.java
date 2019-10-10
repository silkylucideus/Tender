package com.lucidlabs.tender.api;

public class Device {

    String deviceid;
    String os;
    String host;

    public Device() {
    }

    public Device(String deviceid, String os, String host) {
        this.deviceid = deviceid;
        this.os = os;
        this.host = host;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
