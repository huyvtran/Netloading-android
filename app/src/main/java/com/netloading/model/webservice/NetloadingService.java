package com.netloading.model.webservice;

import com.netloading.model.pojo.GCMTokenPOJO;
import com.netloading.model.pojo.RequestPOJO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Dandoh on 2/15/16.
 */
public interface NetloadingService {
    @POST("/customers/notification")
    Call<ResponseBody> sendRegistrationTokenToServer(@Body GCMTokenPOJO gcmTokenPOJO);


    @POST("/requests")
    Call<ResponseBody> sendRequest(@Body RequestPOJO requestPOJO);

}
