package com.ketnoivantai.customers.model.webservice;

import com.ketnoivantai.customers.model.pojo.LoginPOJO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Dandoh on 2/10/16.
 */
public interface AuthenticationServiceDemo {

    @POST("/customers/sessions")
    Call<ResponseBody> loginAndGetToken(@Body LoginPOJO info);

}
