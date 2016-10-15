package com.ketnoivantai.customers.presenter;

import com.google.gson.Gson;
import com.ketnoivantai.customers.common.ConfigurableOps;
import com.ketnoivantai.customers.common.ContextView;
import com.ketnoivantai.customers.model.pojo.ProfilePOJO;
import com.ketnoivantai.customers.model.webservice.ServiceGenerator;

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
public class ProfilePresenter implements ConfigurableOps<ProfilePresenter.View> {

    private WeakReference<View> mView;
    private boolean processing;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        this.mView = new WeakReference<View>(view);
    }

    public void updateProfileInfo() {

        processing = true;

        ServiceGenerator.getNetloadingService().getProfileInfo().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if (result.getString("status").equals("success")) {
                        ProfilePOJO profile = new Gson().fromJson(
                                result.getJSONObject("message").toString(),
                                ProfilePOJO.class);

                        mView.get().onUpdateProfileSuccess(profile);


                    } else if (result.getString("status").equals("error")){
                        mView.get().onError(View.STATUS_UNHANDLED_ERROR);
                    }


                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    mView.get().onError(View.STATUS_NETWORK_ERROR);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                processing = false;
                mView.get().onError(View.STATUS_NETWORK_ERROR);
            }
        });


    }

    public boolean isProcessing() {
        return processing;
    }

    public interface View extends ContextView {

        int STATUS_NETWORK_ERROR = 999;
        int STATUS_UNHANDLED_ERROR = 888;

        void onError(int status);

        void onUpdateProfileSuccess(ProfilePOJO profile);
    }

}
