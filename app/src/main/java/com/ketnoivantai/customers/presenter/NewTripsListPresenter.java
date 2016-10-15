package com.ketnoivantai.customers.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ketnoivantai.customers.common.ConfigurableOps;
import com.ketnoivantai.customers.common.ContextView;
import com.ketnoivantai.customers.model.pojo.NewTripPOJO;
import com.ketnoivantai.customers.model.webservice.ServiceGenerator;
import com.ketnoivantai.utils.AddressManager;
import com.ketnoivantai.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AnhVu on 9/14/16.
 */
public class NewTripsListPresenter implements ConfigurableOps<NewTripsListPresenter.View>{

    private static final String TAG = "NewTripsPresenter";
    private WeakReference<View> mView;
    private boolean processing;
    private Object newTrips;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public boolean isProcessing() {
        return processing;
    }

    public void getNewTrips() {
        ServiceGenerator.getAccountService().getNewTrips().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                processing = false;
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    Utils.log(TAG, result.toString());

                    if (result.getString("status").equals("success")) {

                        Gson gson = new Gson();
                        JSONArray newTripsArray = result.getJSONArray("message");

                        Type listType = new TypeToken<ArrayList<NewTripPOJO>>() {
                        }.getType();
                        ArrayList<NewTripPOJO> newTripPOJOs = gson.fromJson(newTripsArray.toString(), listType);
                        Collections.reverse(newTripPOJOs);

                        mView.get().onGetTripsSuccess(newTripPOJOs);

                    } else if (result.getString("status").equals("error")) {
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

    public void filterList(Context context, int mStartProvincePosition, int mArriveProvincePosition, ArrayList<NewTripPOJO> mNewTripsPOJOs) {

        ArrayList<NewTripPOJO> filterList = new ArrayList<NewTripPOJO>();

        for (NewTripPOJO item: mNewTripsPOJOs) {

            int startCode = Integer.parseInt(item.getStart_address());
            int arriveCode = Integer.parseInt(item.getArrive_address());

            int startProvincePositionOfItem = AddressManager.getInstance(context).getProvincePositionFromCode(startCode);
            int arriveProvincePositionOfItem = AddressManager.getInstance(context).getProvincePositionFromCode(arriveCode);

            boolean ok1 = false;
            boolean ok2 = false;

            if (mStartProvincePosition == -1) ok1 = true;
            if (mArriveProvincePosition == -1) ok2 = true;

            if (mStartProvincePosition == startProvincePositionOfItem) ok1 = true;
            if (mArriveProvincePosition == arriveProvincePositionOfItem) ok2 = true;

            if (ok1 && ok2) filterList.add(item);
        }

        mView.get().onFilterSuccess(filterList);

    }

    public interface View extends ContextView {


        int STATUS_NETWORK_ERROR = 999;
        int STATUS_UNHANDLED_ERROR = 888;

        void onError(int status);
        void onGetTripsSuccess(ArrayList<NewTripPOJO> newTripPOJOs);
        void onFilterSuccess(ArrayList<NewTripPOJO> filterList);

    }

}
