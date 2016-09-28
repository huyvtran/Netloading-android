package com.ketnoivantai.model.webservice;

import com.ketnoivantai.model.pojo.AcceptTripPOJO;
import com.ketnoivantai.model.pojo.ChangePasswordPOJO;
import com.ketnoivantai.model.pojo.GCMTokenPOJO;
import com.ketnoivantai.model.pojo.RequestPOJO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Dandoh on 2/15/16.
 */
public interface NetloadingService {

    @DELETE("/customers/sessions")
    Call<ResponseBody> logout();

    @GET("/customers/company_info/{id}")
    Call<ResponseBody> getCompanyInfomation(@Path("id") int companyId);

    @POST("/customers/notification")
    Call<ResponseBody> sendRegistrationTokenToServer(@Body GCMTokenPOJO gcmTokenPOJO);

    @POST("/customers/change_password")
    Call<ResponseBody> changePassword(@Body ChangePasswordPOJO changePasswordPOJO);

    @GET("/customers/info")
    Call<ResponseBody> getProfileInfo();


    @POST("/requests")
    Call<ResponseBody> sendRequest(@Body RequestPOJO requestPOJO);

    @POST("/requests/accept")
    Call<ResponseBody> acceptTrip(@Body AcceptTripPOJO acceptTripPOJO);

    @GET("/requests")
    Call<ResponseBody> getAllRequestOfCustomer();


    @DELETE("/requests/{id}")
    Call<ResponseBody> deleteRequest(@Path("id") int requestId);

    @GET("/requests/retry/{id}")
    Call<ResponseBody> retryRequest(@Path("id") int requestId);

    @GET("/orders")
    Call<ResponseBody> getAllOrdersOfCustomer();

    @GET("/customers/order_info/{id}")
    Call<ResponseBody> getOrderDetailById(@Path("id") int orderId);

    @GET("/customers/request_info/{id}")
    Call<ResponseBody> getRequestDetailById(@Path("id") int requestId);

}
