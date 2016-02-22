package com.netloading.presenter;

import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;
import com.netloading.model.pojo.RequestPOJO;
import com.netloading.model.webservice.NetloadingService;
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
 * Created by Dandoh on 2/20/16.
 */
public class ReviewRequestPresenter implements ConfigurableOps<ReviewRequestPresenter.View> {

    private WeakReference<View> mView;

    @Override
    public void onConfiguration(ReviewRequestPresenter.View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public void sendRequest(String pickUpDate, String goodsWeightDimension,
                            String goodsWeightNumber, int startDistrictCode,
                            int arriveDistrictCode, String vehicleType,
                            String expectedPrice) {
        NetloadingService netloadingService = ServiceGenerator.getNetloadingService();
        RequestPOJO requestPOJO = new RequestPOJO(pickUpDate, goodsWeightDimension,
                goodsWeightNumber, startDistrictCode,
                arriveDistrictCode, vehicleType,
                expectedPrice);

        netloadingService.sendRequest(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if (result.getString("status").equals("success")) {

                    } else {

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
    }
}
