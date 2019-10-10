package com.lucidlabs.tender.api;

public class Request {

    public RequestBody requestBody;


    public Request(RequestBody requestBody) {
        this.requestBody = requestBody;

    }



    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }


}
