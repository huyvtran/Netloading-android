package com.ketnoivantai.presenter;

import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;
import com.ketnoivantai.model.pojo.ChangePasswordPOJO;
import com.ketnoivantai.model.webservice.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AnhVu on 3/12/16.
 */
public class ChangePasswordPresenter implements ConfigurableOps<ChangePasswordPresenter.View> {

    private WeakReference<View> mView;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public void changeCustomerPassword(ChangePasswordPOJO changePasswordPOJO) {

        ServiceGenerator.getNetloadingService().changePassword(changePasswordPOJO)
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());

                        if (result.getString("status").equals("success")) {

                            mView.get().onChangPasswordSuccess();

                        } else if (result.getString("status").equals("error")) {

                            if (result.getInt("message") == 2) {
                                mView.get().onWrongPasswordError();
                            } else {
                                mView.get().onUnhandledError();
                            }
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

    public interface View extends ContextView {

        int STATUS_NETWORK_ERROR = 999;

        void onError(int status);

        void onWrongPasswordError();

        void onUnhandledError();

        void onChangPasswordSuccess();
    }

}
