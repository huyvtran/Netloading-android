package com.ketnoivantai.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;
import com.ketnoivantai.model.pojo.NewTripPOJO;
import com.ketnoivantai.model.webservice.ServiceGenerator;
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
        ServiceGenerator.getNetloadingService().getNewTrips().enqueue(new Callback<ResponseBody>() {
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

    public interface View extends ContextView {


        int STATUS_NETWORK_ERROR = 999;
        int STATUS_UNHANDLED_ERROR = 888;

        void onError(int status);
        void onGetTripsSuccess(ArrayList<NewTripPOJO> newTripPOJOs);
    }

}
