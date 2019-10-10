package com.lucidlabs.tender.api;

public class RequestBody_Alias {

    Data_Alias data;
    String timestamp = "";
    String token;
    Device device;

    public RequestBody_Alias() {

    }

    public RequestBody_Alias(Data_Alias data, String timestamp, String token, Device device) {
        this.data = data;
        this.timestamp = timestamp;
        this.token = token;
        this.device = device;
    }

    public Data_Alias getData() {
        return data;
    }

    public void setData(Data_Alias data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
