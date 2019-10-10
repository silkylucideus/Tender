package com.lucidlabs.tender.api;

public class Data_New {
    String account_number, ifsc_code, email, otp_response;
    String amount, remarks ;
    String alias;
    String accountNumber, ifscCode, creationDateTime ;


    public Data_New(){

    }

    public Data_New(String account_number, String ifsc_code, String alias, String otp_response){
        this.account_number   = account_number;
        this.ifsc_code    = ifsc_code;
        this.alias        = alias;
        this.otp_response = otp_response;

    }

    // ****** Fetch Beneficiary for deleting

    public Data_New(String accountNumber, String ifscCode, String creationDateTime) {
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.creationDateTime = creationDateTime;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }



    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }


    // Fetch Beneficiary for deleting *************


    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number (String account_no) {
        this.account_number = account_no;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {


        this.alias = alias;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp_response() {
        return otp_response;
    }

    public void setOtp_response(String otp_response) {
        this.otp_response = otp_response;
    }



    public Data_New(String amount, String remarks) {
        this.amount = amount;
        this.remarks = remarks;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
