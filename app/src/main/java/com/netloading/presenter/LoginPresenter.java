package com.netloading.presenter;

import com.netloading.common.ConfigurableOps;
import com.netloading.model.pojo.LoginPOJO;
import com.netloading.model.pojo.RegisterPOJO;
import com.netloading.model.webservice.AccountService;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.Utils;

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
public class LoginPresenter implements ConfigurableOps<LoginPresenter.View> {
    private final String TAG = getClass().getSimpleName();
    private AccountService mAccountService;

    private WeakReference<View> mView;

    public interface View {

        // TODO - status failure
        int NETWORK_ERROR = 3;
        void loginSucceed();
        void loginFailure(int status);
    }


    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
        if (firstTimeIn) {
            mAccountService = ServiceGenerator.getAccountService();
        } else {

        }

    }

    public void login(String username, String password) {

        final LoginPOJO loginPOJO = new LoginPOJO(username, password);


        mAccountService.loginAndSaveToken(loginPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    String token = jsonObject.getJSONObject("message").getString("token");
                    Utils.log(TAG, status);
                    Utils.log(TAG, token);
                    ServiceGenerator.initialize(token);

                    //TODO - check message and status
                    mView.get().loginSucceed();


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


}
