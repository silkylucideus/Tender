package com.lucidlabs.tender.api;

public class RequestBody_Beneficiary {
    Data_New data;
    String timestamp = "";
    String token;
    Device device;

    public RequestBody_Beneficiary() {

    }

   public RequestBody_Beneficiary(String timestamp, String token, Device device, Data_New data){
       this.timestamp = timestamp;
       this.token = token;
       this.device = device;
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
    public Data_New getData() {
        return data;
    }

    public void setData(Data_New data) {
        this.data = data;
    }
}
