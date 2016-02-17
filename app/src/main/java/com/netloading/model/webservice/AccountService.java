package com.netloading.model.webservice;

import com.netloading.model.pojo.EmailSubmitPOJO;
import com.netloading.model.pojo.LoginPOJO;
import com.netloading.model.pojo.RegisterPOJO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Dandoh on 2/13/16.
 */
public interface AccountService {

    @POST("/customers/sessions")
    Call<ResponseBody> loginAndSaveToken(@Body LoginPOJO info);

    @POST("/customers")
    Call<ResponseBody> registerAccount(@Body RegisterPOJO registerPOJO);

    @POST("/customers/forgot_password")
    Call<ResponseBody> submitEmail(@Body EmailSubmitPOJO emailSubmitPOJO);
}
