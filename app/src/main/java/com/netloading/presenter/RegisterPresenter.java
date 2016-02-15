package com.netloading.presenter;

import com.netloading.common.ConfigurableOps;
import com.netloading.model.pojo.RegisterPOJO;
import com.netloading.model.webservice.AccountService;
import com.netloading.model.webservice.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dandoh on 2/14/16.
 */
public class RegisterPresenter implements ConfigurableOps<RegisterPresenter.View> {

    private static final String TAG = RegisterPresenter.class.getSimpleName();
    private AccountService mAccountService = ServiceGenerator.getAccountService();
    private WeakReference<View> mView;

    public interface View {
        int STATUS_NETWORK_ERROR = 243;
        int STATUS_UNHANDLED_ERROR = 15;
        int STATUS_DUPLICATE_USERNAME = 1;
        int STATUS_DUPLICATE_EMAIL = 2;
        int STATUS_DUPLICATE_PHONE = 3;

        void registerSucceed();

        void registerError(int errorCode);
    }


    @Override
    public void onConfiguration(RegisterPresenter.View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }


    public void register(String username, String password,
                         String phone, String email, String address,
                         String socialId) {


        // send register request
        RegisterPOJO registerPOJO = new RegisterPOJO(username, password, phone,
                email, address, socialId);

        mAccountService.registerAccount(registerPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
//                    String message = jsonObject.getString("message");
//                    Utils.log(TAG, status);
//                    Utils.log(TAG, message);
                    // TODO - check message and decide
                    if (status.equals("success")) {
                        mView.get().registerSucceed();
                    } else {
                        int errorCode = jsonObject.getJSONObject("message").getInt("errorCode");
                        mView.get().registerError(errorCode);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO - error
                    mView.get().registerError(View.STATUS_UNHANDLED_ERROR);

                } catch (IOException e) {
                    e.printStackTrace();
                    mView.get().registerError(View.STATUS_NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.get().registerError(View.STATUS_NETWORK_ERROR);
            }
        });


    }






}
