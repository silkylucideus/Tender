package com.lucidlabs.tender.api;

public class Data_Account {

    String accountNumber,accountBalance, incomeTaxNumber, openDate, currency, fname, lname;
    String address, dob, mobileNo, email, aadharId, panCardId, walletId, countryId, userId;
    String userid;

    public Data_Account(){

    }

    public Data_Account(String accountNumber, String accountBalance, String incomeTaxNumber, String openDate, String currency, String fname, String lname, String address, String dob, String mobileNo, String email, String aadharId, String panCardId, String walletId, String countryId, String userId, String userid) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.incomeTaxNumber = incomeTaxNumber;
        this.openDate = openDate;
        this.currency = currency;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.dob = dob;
        this.mobileNo = mobileNo;
        this.email = email;
        this.aadharId = aadharId;
        this.panCardId = panCardId;
        this.walletId = walletId;
        this.countryId = countryId;
        this.userId = userId;
        this.userid = userid;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getIncomeTaxNumber() {
        return incomeTaxNumber;
    }

    public void setIncomeTaxNumber(String incomeTaxNumber) {
        this.incomeTaxNumber = incomeTaxNumber;
    }

    public String getOpenDate() {
        return openDate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadharId() {
        return aadharId;
    }

    public void setAadharId(String aadharId) {
        this.aadharId = aadharId;
    }

    public String getPanCardId() {
        return panCardId;
    }

    public void setPanCardId(String panCardId) {
        this.panCardId = panCardId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
