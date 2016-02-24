package com.netloading.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
import com.netloading.model.pojo.CompanyPOJO;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.model.webservice.NetloadingService;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dandoh on 2/20/16.
 */
public class ReviewRequestPresenter implements ConfigurableOps<ReviewRequestPresenter.View> {

    private static final String TAG = "ReviewRequestPresenter";
    private WeakReference<View> mView;

    @Override
    public void onConfiguration(ReviewRequestPresenter.View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public void sendRequest(String pickUpDate, String goodsWeightDimension,
                            int goodsWeightNumber, int startDistrictCode,
                            int arriveDistrictCode, String vehicleType,
                            String expectedPrice, String goodsName) {
        NetloadingService netloadingService = ServiceGenerator.getNetloadingService();

        // TODO - test here
        RequestPOJO requestPOJO = new RequestPOJO(pickUpDate, goodsWeightDimension,
                goodsWeightNumber, 861,
                848, vehicleType,
                expectedPrice, goodsName);

        Utils.log(TAG, requestPOJO.toString());

        netloadingService.sendRequest(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    Utils.log(TAG, result.toString());
                    if (result.getString("status").equals("success")) {
                        Gson gson = new Gson();
                        JSONArray companiesArray = result.getJSONObject("message").getJSONArray("trips");
                        Type listType = new TypeToken<ArrayList<CompanyPOJO>>() {
                        }.getType();
                        ArrayList<CompanyPOJO> companyPOJOs = gson.fromJson(companiesArray.toString(), listType);

                        Utils.log(TAG, companyPOJOs.size() + " ");


                        /// TODO - on result
                        mView.get().onRequestResult(companyPOJOs);

                    } else {
                        mView.get().onError(View.STATUS_ERROR_NETWORK);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    public interface View extends ContextView {
        int STATUS_ERROR_NETWORK = 123;

        void onError(int status);

        void onRequestResult(ArrayList<CompanyPOJO> companyPOJOs);
    }
}
