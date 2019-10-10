package com.lucidlabs.tender.api;

public class Data {

    String firstname;
    String lastname;
    String gndr;
    String mobile;
    String email;
    String passwd, old_pass, new_pass;
    String countryId;
    String address;
    String dob;
    String userid, userId;
    String token;
    String aadharId;
    String accNo;
    String otp_type, otp, response, otp_response;


    public Data() {
    }

    public Data(String firstname, String lastname, String gndr, String mobile, String email, String passwd, String countryId, String address, String dob, String userid, String token, String aadharNo, String accNo, String otp_type, String otp, String response, String old_pass, String new_pass, String otp_response, String userId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gndr = gndr;
        this.mobile = mobile;
        this.email = email;
        this.passwd = passwd;
        this.countryId = countryId;
        this.address = address;
        this.dob = dob;
        this.userid = userid;
        this.token = token;
        this.aadharId = aadharNo;
        this.accNo = accNo;
        this.otp_type = otp_type;
        this.otp = otp;
        this.response = response;
        this.old_pass = old_pass;
        this.new_pass = new_pass;
        this.otp_response = otp_response;
        this.userId = userId;
    }

    public String getNew_pass() {
        return new_pass;
    }

    public void setNew_pass(String new_pass) {
        this.new_pass = new_pass;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getOtp_type() {
        return otp_type;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOld_pass() {
        return old_pass;
    }

    public void setOld_pass(String old_passwd) {
        this.old_pass = old_passwd;
    }

    public String getGndr() {
        return gndr;
    }

    public void setGndr(String gndr) {
        this.gndr = gndr;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAadharNo() {
        return aadharId;
    }

    public void setOtp_type(String otp_type) {
        this.otp_type = otp_type;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharId = aadharNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String get_response() {
        return response;
    }

    public void set_response(String response) {
        this.response = response;
    }

    public String getOtp_response() {
        return otp_response;
    }

    public void setOtp_response(String otp_response) {
        this.otp_response = otp_response;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
