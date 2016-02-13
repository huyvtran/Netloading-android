package com.netloading.presenter;

import com.google.gson.JsonElement;
import com.netloading.common.ConfigurableOps;
import com.netloading.model.pojo.LoginPOJO;
import com.netloading.model.pojo.RegisterPOJO;
import com.netloading.model.webservice.AccountService;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dandoh on 2/13/16.
 */
public class AccountPresenter implements ConfigurableOps<AccountPresenter.View> {
    private final String TAG = getClass().getSimpleName();
    private AccountService mAccountService;

    private WeakReference<View> mView;

    public interface View {

        // TODO - status failure
        int DUPLICATE_USERNAME = 1;
        int DUPLICATE_EMAIL = 2;
        int NETWORK_ERROR = 3;

        void registerSucceed();
        void registerError(int status);
        void loginSucceed(String token);
        void loginFailure(int status);
    }


    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
        if (firstTimeIn) {
            mAccountService = ServiceGenerator.createService(AccountService.class);
        } else {

        }

    }

    public void login(String username, String password) {

        LoginPOJO loginPOJO = new LoginPOJO(username, password);


        mAccountService.loginAndGetToken(loginPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String token = jsonObject.getJSONObject("message").getString("token");
                    LogUtils.log(TAG, status);
                    LogUtils.log(TAG, token);

                    //TODO - check message and status
                    mView.get().loginSucceed(token);


                } catch (JSONException | IOException e) {
                    e.printStackTrace();

                    // TODO - status login
                    mView.get().loginFailure(0);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void register(String username, String password,
                         String phone, String email, String address,
                         String socialId) {

        // TODO - validator


        // send register request
        RegisterPOJO registerPOJO = new RegisterPOJO(username, password, phone,
                email, address, socialId);

        mAccountService.registerAccount(registerPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    LogUtils.log(TAG, status);
                    LogUtils.log(TAG, message);


                    // TODO - check message and decide
                    mView.get().registerSucceed();


                } catch (JSONException | IOException e) {
                    e.printStackTrace();

                    mView.get().registerError(View.NETWORK_ERROR);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.get().registerError(View.NETWORK_ERROR);
            }
        });


    }


}
