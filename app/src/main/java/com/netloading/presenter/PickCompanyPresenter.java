package com.netloading.presenter;

import android.app.Service;

import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
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
 * Created by Dandoh on 2/24/16.
 */
public class PickCompanyPresenter implements ConfigurableOps<PickCompanyPresenter.View> {
    private WeakReference<View> mView;
    private boolean processing;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        this.mView = new WeakReference<View>(view);
    }

    public void deleteRequest(int requestId) {
        processing = true;

        ServiceGenerator.getNetloadingService()
                .deleteRequest(requestId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if (result.getString("message").equals("success")) {
                        mView.get().onDeleteSuccess();
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
        int STATUS_NETWORK_ERROR =  234;

        void onDeleteSuccess();

        void onError(int status);
    }


}
