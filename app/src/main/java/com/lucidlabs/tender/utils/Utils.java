 package com.lucidlabs.tender.utils;

import com.lucidlabs.tender.api.Device;



public class Utils {

    public static final boolean API_DEBUG              = true;
    public static final String MY_ENDPOINT             = "http://10.80.3.66/";
    public static final String BASE_URL                =  MY_ENDPOINT +"vulnbank/";
    public static final String API_LOGIN               =  BASE_URL+"login";
    public static final String API_SIGNUP              =  BASE_URL+"signup";
    public static final String API_GETACCOUNTDETAIL    =  BASE_URL+"account/details";
    public static final String API_GETOTP              =  BASE_URL+"otp/get";
    public static final String API_VERIFYOTP           =  BASE_URL+"otp/verify";
    public static final String API_ADDBENEFICIARY      =  BASE_URL+"beneficiary/add";
    public static final String API_PAY                 =  BASE_URL+"beneficiary/pay";
    public static final String API_VIEWBENEFICIARY     =  BASE_URL+"beneficiary/list";
    public static final String API_CHANEPASSWORD       =  BASE_URL+"password/change";
    public static final String API_LOGOUT              =  BASE_URL+"logout";
    public static final String API_FORGOTPASSWORD      =  BASE_URL+"password/forgot";
    public static final String API_VERIFYUSER          =  BASE_URL+"password/verifyuser";
    public static final String API_RESETPASSWORD       =  BASE_URL+"password/reset";
    public static final String API_ACCOUNTDETAILS      =  BASE_URL+"account/details";
    public static final String API_FETCHBENEFICIARY    =  BASE_URL+"beneficiary/fetch";
    public static final String API_DELETEBENEFICIARY   =  BASE_URL+"beneficiary/delete";
    public static final String API_GETBENEFICIARYFUND  =  BASE_URL+"beneficiary/get";

    public static final String level1_shared_firstname = "first_name";
    public static final String level1_shared_lastname  = "last_name";
    public static final String level1_shared_birthday  = "birthday";
    public static final String level1_shared_email     = "user_email";
    public static final String level1_shared_mobile    = "user_mobile";
    public static final String level1_shared_pin       = "user_pin";
    public static final String level1_new_user_flag    = "new_user";
    public static final String level1_device_id        = "imei";
    public static final String level1_shared_address   = "user_address";
    public static final String level1_user_id          = "user_id";
    public static final String level1_user_account     = "user_account_no";
    public static final String level1_user_token       = "user_token";
    public static final String level1_otp_response     = "otp_response";
    public static final String level1_password         = "user_passwd";
    public static final String level1_user_name        = "user_name";
    public static final String level1_alias_name       = "alias_name";



    //@alan hardcoded_encryption_key
    public static final String level1_key              = "user_mobile";
    //@alan no_random_iv
    public static final String level1_iv               = "user_pin";
    public static final String analyst_name            = "analyst_name";
    public static final String enviroment_url          = "enviroment_url";
    public static final String enviroment_port         = "enviroment_port";

    public static Device device                  = new Device();

    public static String getCurrentTimeStamp(){
        return ""+System.currentTimeMillis() / 1000L;
    }
}
