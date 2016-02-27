package com.netloading.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
import com.netloading.model.pojo.OrderPOJO;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.NotAuthenticatedException;
import com.netloading.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AnhVu on 2/26/16.
 */
public class OrderListPresenter implements ConfigurableOps<OrderListPresenter.View> {

    private static final String TAG = "OrderListPresenter";
    private WeakReference<View> mView;
    private boolean processing;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        this.mView = new WeakReference<View>(view);
    }

    public void updateOrderInformation() {
        processing = true;
        ServiceGenerator.getNetloadingService().getAllOrdersOfCustomer().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    Utils.log(TAG, result.toString());

                    if (result.getString("status").equals("success")) {

                        Gson gson = new Gson();
                        JSONArray ordersArray = result.getJSONArray("message");

                        Type listType = new TypeToken<ArrayList<OrderPOJO>>() {
                        }.getType();

                        ArrayList<OrderPOJO> orderPOJOs = gson.fromJson(ordersArray.toString(), listType);

                        mView.get().updateOrderList(orderPOJOs);

                    } else if (result.getString("status").equals("error")){
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
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
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

        void onError(int statusUnhandledError);

        void updateOrderList(ArrayList<OrderPOJO> orderPOJOs);
    }
}
