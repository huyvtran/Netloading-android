package com.netloading.model.webservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.netloading.NetloadingApplication;
import com.netloading.utils.Constants;
import com.netloading.utils.NotAuthenticatedException;
import com.netloading.utils.Utils;
import com.netloading.view.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

/**
 * Created by Dandoh on 2/10/16.
 */
public class ServiceGenerator {


    private static final String TAG = "ServiceGenerator";
    private static int id;

    /**
     * LOG all http request through a generic OkHttpClient
     */
    private static OkHttpClient httpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Utils.log("REQUEST", request.url() + " " + request.headers().toString());
                        return chain.proceed(request);
                    }
                })
                .build();

        Utils.log(TAG, "Size interceptor : " + okHttpClient.interceptors().size() + " ");

//        okHttpClient.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Utils.log("REQUEST", request.url() + " " + request.headers().toString());
//                return chain.proceed(request);
//            }
//        });

        return okHttpClient;
    }


    /**
     * Generic retrofit builder
     */
    private static Retrofit.Builder builder() {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
    }

    private static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, builder(), httpClient());
    }

    private static <S> S createService(Class<S> serviceClass, Retrofit.Builder builder, OkHttpClient httpClient) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }



    /*
        Authentication
     */

    private static boolean isLoggedIn = false;

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        ServiceGenerator.isLoggedIn = isLoggedIn;
    }

    public static void initialize(String accessToken, int id) {
        if (isLoggedIn) return;


        setAccessToken(accessToken);
        setId(id);
        isLoggedIn = true;
    }

    private static void setId(int id) {
        ServiceGenerator.id = id;
    }

    public static OkHttpClient getAuthenticatedHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Utils.log("REQUEST", request.url() + " " + request.headers().toString());
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        Request modifiedRequest = request.newBuilder()
                                .addHeader("token", getAccessToken())
                                .addHeader("customer_id", id + "")
                                .addHeader("account_type", "customer")
                                .build();


                        return chain.proceed(modifiedRequest);
                    }
                }).addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());

                        if (originalResponse.code() == 222) {
                            isLoggedIn = false;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    Utils.log(TAG, "222 Unauthenticated");
                                    Context context = NetloadingApplication.getAppContext();
                                    Intent intent = LoginActivity.makeIntent(context, LoginActivity.LOGIN_TOKEN_INVALID)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                    Utils.toast(context, "Phiên đăng nhập kết thúc hoặc tài khoản đang đăng nhập ở thiết bị khác," +
                                            "vui lòng đăng nhập lại");

                                    SharedPreferences sharedPreferences = PreferenceManager
                                            .getDefaultSharedPreferences(NetloadingApplication.getAppContext());
                                    sharedPreferences.edit().putInt(Constants.SHARED_PREFERENCE_ID_TAG, 0)
                                            .putString(Constants.SHARED_PREFERENCE_TOKEN_TAG, "NULL")
                                            .apply();


                                }
                            });



                        }

                        return originalResponse;
                    }
                })
                .build();

        return okHttpClient;
    }


    /**
     * Access token
     */
    private static String accessToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        ServiceGenerator.accessToken = accessToken;
    }


    /**
     * Services
     */

    private static AccountService mAccountService = createService(AccountService.class);

    public static AccountService getAccountService() {
        return mAccountService;
    }


    private static NetloadingService mNetloadingService;

    public static NetloadingService getNetloadingService() {

        if (!isLoggedIn()) {
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(NetloadingApplication.getAppContext());

            String token = sharedPreferences.getString(Constants.SHARED_PREFERENCE_TOKEN_TAG, "NULL");
            int customer_id = sharedPreferences.getInt(Constants.SHARED_PREFERENCE_ID_TAG, -1);

            Utils.log(TAG, "TOKEN : " + token);

            ServiceGenerator.initialize(token, customer_id);

        }

        if (mNetloadingService == null) {
            mNetloadingService = createService(NetloadingService.class, builder(), getAuthenticatedHttpClient());
        }
        return mNetloadingService;
    }


}
