package com.netloading.presenter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netloading.R;
import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
import com.netloading.model.pojo.CompanyTripPOJO;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.NotAuthenticatedException;
import com.netloading.utils.Utils;
import com.netloading.view.RequestInformationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dandoh on 2/24/16.
 */
public class PickCompanyPresenter implements ConfigurableOps<PickCompanyPresenter.View> {
    private static final String TAG = "PickCompanyPresenter";
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
                processing = false;

                mView.get().onError(View.STATUS_NETWORK_ERROR);
            }
        });
    }

    public boolean isProcessing() {
        return processing;
    }

    public void retry(int requestId) {

        ServiceGenerator.getNetloadingService().retryRequest(requestId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONObject result = new JSONObject(response.body().string());

                    Utils.log(TAG, result.toString());
                    if (result.getString("status").equals("success")) {

                        // Get company list
                        Gson gson = new Gson();
                        JSONArray companiesArray = result.getJSONObject("message").getJSONArray("trips");
                        Type listType = new TypeToken<ArrayList<CompanyTripPOJO>>() {
                        }.getType();
                        ArrayList<CompanyTripPOJO> companyTripPOJOs = gson.fromJson(companiesArray.toString(), listType);

                        // Get request id
                        Utils.log(TAG, companyTripPOJOs.size() + " ");

                        /// TODO - on result
                        mView.get().onRetrySuccess(companyTripPOJOs);

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
                mView.get().onError(View.STATUS_NETWORK_ERROR);
            }
        });

    }

    public void getRequestInfo(int requestId) {
        ServiceGenerator.getNetloadingService().getRequestDetailById(requestId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if (result.getString("status").equals("success")) {
                        RequestPOJO requestPOJO = new Gson().fromJson(result.getJSONObject("message").toString(),
                                RequestPOJO.class);

                        mView.get().onGetRequestDetailSuccess(requestPOJO);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public interface View extends ContextView {
        int STATUS_NETWORK_ERROR = 999;
        int STATUS_UNHANDLED_ERROR = 888;

        void onDeleteSuccess();

        void onError(int status);

        void onRetrySuccess(ArrayList<CompanyTripPOJO> companyTripPOJOs);

        void onGetRequestDetailSuccess(RequestPOJO requestPOJO);
    }


}
