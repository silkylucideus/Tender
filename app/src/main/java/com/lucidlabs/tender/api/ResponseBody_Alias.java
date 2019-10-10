package com.lucidlabs.tender.api;

public class ResponseBody_Alias {
    String status;
    String status_code;
    String message;
    String time_stamp;
    Data_Alias data;

    public ResponseBody_Alias(String status, String status_code, String message, String time_stamp, Data_Alias data) {
        this.status = status;
        this.status_code = status_code;
        this.message = message;
        this.time_stamp = time_stamp;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public Data_Alias getData() {
        return data;
    }

    public void setData(Data_Alias data) {
        this.data = data;
    }
}
