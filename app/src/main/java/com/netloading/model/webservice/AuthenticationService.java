package com.netloading.model.webservice;

import com.netloading.model.pojo.LoginPOJO;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Dandoh on 2/10/16.
 */
public interface AuthenticationService {

    @POST("/customers/sessions")
    Call<ResponseBody> loginAndGetToken(@Body LoginPOJO info);

}
