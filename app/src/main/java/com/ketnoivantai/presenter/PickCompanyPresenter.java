package com.ketnoivantai.presenter;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;
import com.ketnoivantai.model.pojo.CompanyTripPOJO;
import com.ketnoivantai.model.pojo.RequestPOJO;
import com.ketnoivantai.model.webservice.ServiceGenerator;
import com.ketnoivantai.utils.Utils;

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

    public void retry(final int requestId) {
        processing = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                retryAfterWait(requestId);
                processing = false;
            }
        }, 2000);
    }

    private void retryAfterWait(int requestId) {
        processing = true;

        ServiceGenerator.getNetloadingService().retryRequest(requestId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;
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
                processing = false;
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
