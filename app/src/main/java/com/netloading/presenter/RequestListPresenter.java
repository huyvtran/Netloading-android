package com.netloading.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
import com.netloading.model.pojo.CompanyTripPOJO;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.Utils;

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
 * Created by AnhVu on 2/25/16.
 */
public class RequestListPresenter implements ConfigurableOps<RequestListPresenter.View> {
    private static final String TAG = "RequestListPresenter";
    private WeakReference<View> mView;
    private boolean processing;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        this.mView = new WeakReference<View>(view);
    }

    public boolean isProcessing() {
        return processing;
    }

    public void getAllRequest() {
        processing = true;

        ServiceGenerator.getNetloadingService()
                .getAllRequestOfCustomer().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if (result.getString("status").equals("success")) {

                        Gson gson = new Gson();
                        JSONArray requestsArray = result.getJSONArray("message");

                        Type listType = new TypeToken<ArrayList<RequestPOJO>>() {
                        }.getType();

                        ArrayList<RequestPOJO> requestPOJOs = gson.fromJson(requestsArray.toString(), listType);

                        Utils.log(TAG, requestPOJOs.size() + " ");

                        mView.get().updateRequestList(requestPOJOs);

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

    public interface View extends ContextView {

        int STATUS_NETWORK_ERROR = 999;
        int STATUS_UNHANDLED_ERROR = 888;

        void onError(int status);

        void updateRequestList(ArrayList<RequestPOJO> requestPOJOs);
    }

}
