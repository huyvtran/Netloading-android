package com.ketnoivantai.presenter;

import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;
import com.ketnoivantai.model.pojo.RequestPOJO;
import com.ketnoivantai.model.webservice.NetloadingService;
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
 * Created by Dandoh on 2/20/16.
 */
public class ReviewRequestPresenter implements ConfigurableOps<ReviewRequestPresenter.View> {

    private static final String TAG = "ReviewRequestPresenter";
    private WeakReference<View> mView;
    private boolean processing;

    @Override
    public void onConfiguration(ReviewRequestPresenter.View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public void sendRequest(String pickUpDate, String goodsWeightDimension,
                            int goodsWeightNumber, int startDistrictCode,
                            int arriveDistrictCode, String vehicleType,
                            String goodsDescription, String goodsName,
                            String startProvinceName, String arriveProvinceName,
                            String startDistrictName, String arriveDistrictName) {

        // set state
        processing = true;

        NetloadingService netloadingService = null;
        netloadingService = ServiceGenerator.getNetloadingService();

        // TODO - test here
        final RequestPOJO requestPOJO = new RequestPOJO(pickUpDate, goodsWeightDimension,
                goodsWeightNumber, startDistrictCode,
                arriveDistrictCode, vehicleType,
                goodsDescription, goodsName,
                startProvinceName, arriveProvinceName,
                startDistrictName, arriveDistrictName
        );

        Utils.log(TAG, requestPOJO.toString());

        netloadingService.sendRequest(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;
                try {
                    Utils.log(TAG, "status = " + response.code());
                    JSONObject result = new JSONObject(response.body().string());

                    Utils.log(TAG, result.toString());
                    if (result.getString("status").equals("success")) {

//                        // Get company list
//                        Gson gson = new Gson();
//                        JSONArray companiesArray = result.getJSONObject("message").getJSONArray("trips");
//                        Type listType = new TypeToken<ArrayList<CompanyTripPOJO>>() {
//                        }.getType();
//                        ArrayList<CompanyTripPOJO> companyPOJOs = gson.fromJson(companiesArray.toString(), listType);
//
//                        // Get request id
                        int id = result.getJSONObject("message").getInt("insertId");
//                        Utils.log(TAG, companyPOJOs.size() + " ");

                        /// TODO - on result
                        mView.get().onRequestResult(id);

                    } else if (result.getString("status").equals("error")){
                        mView.get().onError(View.STATUS_ERROR_UNHANDLE);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    mView.get().onError(View.STATUS_ERROR_NETWORK);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                processing = false;
                mView.get().onError(View.STATUS_ERROR_NETWORK);
            }
        });


    }

    public boolean isProcessing() {
        return processing;
    }

    public interface View extends ContextView {
        int STATUS_ERROR_NETWORK = 999;
        int STATUS_ERROR_UNHANDLE = 888;

        void onError(int status);

        void onRequestResult(int requestId);
    }
}
