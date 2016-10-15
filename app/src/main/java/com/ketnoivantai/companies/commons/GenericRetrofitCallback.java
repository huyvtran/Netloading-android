package com.ketnoivantai.companies.commons;

import android.content.Context;

import com.ketnoivantai.NetloadingApplication;
import com.ketnoivantai.R;
import com.ketnoivantai.companies.models.webservice.json.GenericResultJSON;
import com.ketnoivantai.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dandoh on 3/23/16.
 */
public abstract class GenericRetrofitCallback<T extends GenericResultJSON> implements Callback<T> {


    private static final String TAG = "RESPONSE : ";
    private Context mContext = NetloadingApplication.getAppContext();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Utils.log(TAG, response.code() + " ");
        if (response.body() != null) {
            T result = response.body();
            Utils.log(TAG, result.toString());
            if (result.getStatus().equals("error")) {
                onError(result.getError());
            } else {
                onSucceed(result);
            }
        } else {
            onError(mContext.getString(R.string.network_error_string));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Utils.log(TAG, "onFailure");
        onError(mContext.getString(R.string.network_error_string));
    }

    protected abstract void onSucceed(T result);

    protected abstract void onError(String message);


}
