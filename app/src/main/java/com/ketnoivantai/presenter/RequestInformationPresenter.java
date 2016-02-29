package com.ketnoivantai.presenter;

import com.google.gson.Gson;
import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;
import com.ketnoivantai.model.pojo.RequestPOJO;
import com.ketnoivantai.model.webservice.ServiceGenerator;
import com.ketnoivantai.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AnhVu on 2/26/16.
 */
public class RequestInformationPresenter implements ConfigurableOps<RequestInformationPresenter.View> {

    private static final String TAG = "RequestInformationPresenter";
    private WeakReference<View> mView;
    private boolean processing;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {

        this.mView = new WeakReference<View>(view);

    }

    public void deleteRequest(int requestId) {
        ServiceGenerator.getNetloadingService()
                .deleteRequest(requestId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //                processing = false;

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    Utils.log(TAG, result.toString());

                    if (result.getString("status").equals("success")) {
                        mView.get().onDeleteSuccess();
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
                //                processing = false;

                mView.get().onError(View.STATUS_NETWORK_ERROR);
            }
        });
    }

    public void getRequestInfo(int requestId) {
        processing = true;
        ServiceGenerator.getNetloadingService()
                .getRequestDetailById(requestId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if (result.getString("status").equals("success")) {
                        RequestPOJO requestPOJO = new Gson().fromJson(result.getJSONObject("message").toString(),
                                RequestPOJO.class);

                        mView.get().updateRequestInformation(requestPOJO);

                    }

                } catch (JSONException e) {
                    processing = false;
                    mView.get().onError(View.STATUS_UNHANDLED_ERROR);
                    e.printStackTrace();
                } catch (IOException e) {
                    processing = false;
                    mView.get().onError(View.STATUS_NETWORK_ERROR);
                    e.printStackTrace();
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

        int STATUS_UNHANDLED_ERROR = 888;
        int STATUS_NETWORK_ERROR = 999;

        void updateRequestInformation(RequestPOJO requestInfo);

        void onDeleteSuccess();

        void onError(int status);

    }

}
