package com.lucidlabs.tender.api;

public class RequestBody {

    String timestamp = "";
    String token;
    Device device;
    Data data;
    String response;



    public RequestBody() {
    }

    public RequestBody(String timestamp, String token, Device device, Data data,String response) {
        this.timestamp = timestamp;
        this.token = token;
        this.device = device;
        this.data = data;
        this.response = response;


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

    public String getOtp_response() {
        return response;
    }

    public void setOtp_response(String response) {
        this.response = response;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


}
