package com.netloading.presenter;

import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
import com.netloading.model.pojo.EmailSubmitPOJO;
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
 * Created by Dandoh on 2/16/16.
 */
public class ForgotpassPresenter implements ConfigurableOps<ForgotpassPresenter.View>{

    private static final String TAG = "ForgotpassPresenter";
    private WeakReference<View> mView;

    private boolean isProcessing = false;

    @Override
    public void onConfiguration(ForgotpassPresenter.View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public interface View extends ContextView{

        int STATUS_NETWORK_ERROR = 1;
        int STATUS_EMAIL_ERROR = 2;

        void onSucceed();
        void onError(int status);
    }


    public void submitEmail(String email) {

        Utils.log(TAG, email);
        AccountService accountService = ServiceGenerator.getAccountService();

        isProcessing = true;
        accountService.submitEmail(new EmailSubmitPOJO(email)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject responseBody = new JSONObject(response.body().string());
                    String message = responseBody.getString("status");

                    if (message.equals("success")) {
                        mView.get().onSucceed();
                    } else if (responseBody.getString("status").equals("error")) {
                        mView.get().onError(View.STATUS_EMAIL_ERROR);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    mView.get().onError(View.STATUS_NETWORK_ERROR);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.get().onError(View.STATUS_NETWORK_ERROR);
            }
        });
    }

    public boolean isProcessing() {
        return isProcessing;
    }
}
