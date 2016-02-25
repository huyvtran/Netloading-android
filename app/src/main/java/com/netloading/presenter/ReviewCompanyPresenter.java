package com.netloading.presenter;

import com.google.gson.Gson;
import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
import com.netloading.model.pojo.CompanyPOJO;
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
 * Created by AnhVu on 2/25/16.
 */
public class ReviewCompanyPresenter implements ConfigurableOps<ReviewCompanyPresenter.View> {

    private WeakReference<View> mView;
    private boolean processing;


    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public void getCompanyInfo(int mCompanyId) {
        processing = true;

        ServiceGenerator.getNetloadingService()
                .getCompanyInfomation(mCompanyId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.getString("status").equals("success")) {
                        CompanyPOJO companyInfo = new Gson().fromJson(
                                result.getJSONObject("message").toString(),
                                CompanyPOJO.class
                        );

                        mView.get().updateCompanyInfo(companyInfo);

                    } else {
                        mView.get().onError(View.STATUS_UNHANDLED_ERROR);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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

        int STATUS_UNHANDLED_ERROR = 999;
        int STATUS_NETWORK_ERROR = 888;

        void onError(int status);

        void updateCompanyInfo(CompanyPOJO companyInfo);
    }


}
