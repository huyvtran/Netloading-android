package com.ketnoivantai.model.webservice;

import com.ketnoivantai.model.pojo.EmailSubmitPOJO;
import com.ketnoivantai.model.pojo.LoginPOJO;
import com.ketnoivantai.model.pojo.RegisterPOJO;

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
