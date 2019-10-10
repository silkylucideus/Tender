package com.lucidlabs.tender.api;

import com.lucidlabs.tender.utils.Utils;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by alanabraham on 2/10/18.
 */

public interface APIInterface {

    @FormUrlEncoded
    @POST(Utils.API_LOGIN)
    Call<BaseResponse> login(@Field("login") String login, @Field("password") String password);





    @POST(Utils.API_LOGIN)
    Call<ResponseBody> login(@Body Request requestBody);

    @POST(Utils.API_GETOTP)
    Call<ResponseBody> getotp(@Body Request requestBody);

    @POST(Utils.API_VERIFYOTP)
    Call<ResponseBody> verifyotp(@Body Request requestBody);



    @POST(Utils.API_SIGNUP)
    Call<ResponseBody> signup(@Body Request requestBody);

    @POST(Utils.API_ADDBENEFICIARY)
    Call<ResponseBody> addbeneficiary(@Body Request_Beneficiary requestBody);


    @POST(Utils.API_CHANEPASSWORD)
    Call<ResponseBody> changePassword(@Body Request requestBody);

    @POST(Utils.API_FORGOTPASSWORD)
    Call<ResponseBody> forgotPassword(@Body Request requestBody);

    @POST(Utils.API_VERIFYUSER)
    Call<ResponseBody> verifyUser(@Body Request requestBody);

    @POST(Utils.API_RESETPASSWORD)
    Call<ResponseBody> resetPassword(@Body Request requestBody);

    @POST(Utils.API_LOGOUT)
    Call<ResponseBody> logout(@Body Request requestBody);


    @POST(Utils.API_ACCOUNTDETAILS)
    Call<ResponseBody_Account> getJSON(@Body Request_Account requestBody);


    @POST(Utils.API_PAY)
    Call<ResponseBody_Beneficiary> transferFund(@Body Request_Beneficiary requestBody);


    @POST(Utils.API_VIEWBENEFICIARY)
    Call<ResponseBody_Alias> viewBenificiary(@Body Request_Alias requestBody);

    @POST(Utils.API_GETBENEFICIARYFUND)
    Call<ResponseBody_Alias> getBenificiary(@Body Request_Alias requestBody);

    @POST(Utils.API_FETCHBENEFICIARY)
    Call<ResponseBody_Beneficiary> fetchBenificiary(@Body Request_Beneficiary requestBody);

    @POST(Utils.API_DELETEBENEFICIARY)
    Call<ResponseBody_Beneficiary> deleteBenificiary(@Body Request_Beneficiary requestBody);

    @POST(Utils.API_GETACCOUNTDETAIL)
    Call<ResponseBody> getAccountDetails(@Body Request requestBody);






}



