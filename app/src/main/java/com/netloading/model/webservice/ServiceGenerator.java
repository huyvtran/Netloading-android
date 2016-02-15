package com.netloading.model.webservice;

import com.netloading.utils.Constants;
import com.netloading.utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dandoh on 2/10/16.
 */
public class ServiceGenerator {


    private static final String TAG = "ServiceGenerator";

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


    public static void initialize(String accessToken) {
        setAccessToken(accessToken);
        isLoggedIn = true;
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
                                .build();

                        return chain.proceed(modifiedRequest);
                    }
                }).build();

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
}
