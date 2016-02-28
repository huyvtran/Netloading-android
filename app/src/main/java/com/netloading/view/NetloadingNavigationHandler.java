package com.netloading.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.netloading.NetloadingApplication;
import com.netloading.R;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.Constants;
import com.netloading.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dandoh on 2/25/16.
 */
public class NetloadingNavigationHandler {

    private static final String TAG = "NetloadingNavigationHandler";
    private Activity mActivity;

    public NetloadingNavigationHandler(Activity activity) {
        this.mActivity = activity;
    }

    public void onNavigationItemClick(View v) {

        if (!ServiceGenerator.isLoggedIn()) {
            Intent intent = LoginActivity.makeIntent(mActivity, LoginActivity.LOGIN_AFTER_LOGOUT);
            mActivity.startActivity(intent);
            return;
        }

//        Utils.log(TAG, v.getId() + " clicked on navigation drawer");
        switch (v.getId()) {
            // TODO
        }

        if (v.getId() == R.id.navigation_manage_requests) {
            Utils.log(TAG, v.getId() + " clicked on request button");

            Intent intent = RequestListActivity.makeIntent(mActivity.getApplicationContext());
            mActivity.startActivity(intent);
        } else
        if (v.getId() == R.id.navigation_manage_orders) {
            Intent intent = OrderListActivity.makeIntent(mActivity.getApplicationContext());
            mActivity.startActivity(intent);
        } else
        if (v.getId() == R.id.navigation_sign_out) {
            ServiceGenerator.getNetloadingService().logout().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());

                        if (result.getString("status").equals("success")) {
                            ServiceGenerator.setIsLoggedIn(false);
//                            System.exit(0);
                            Intent intent = LoginActivity.makeIntent(mActivity, LoginActivity.LOGIN_AFTER_LOGOUT).setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            mActivity.startActivity(intent);


                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(NetloadingApplication.getAppContext());
                            sharedPreferences.edit().putInt(Constants.SHARED_PREFERENCE_ID_TAG, 0)
                                    .putString(Constants.SHARED_PREFERENCE_TOKEN_TAG, "NULL")
                                    .apply();

                        } else {
                            Utils.log(TAG, "Error when logout");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ServiceGenerator.setIsLoggedIn(false);
//                            System.exit(0);
                    Intent intent = LoginActivity.makeIntent(mActivity, LoginActivity.LOGIN_AFTER_LOGOUT).setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    mActivity.startActivity(intent);


                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(NetloadingApplication.getAppContext());
                    sharedPreferences.edit().putInt(Constants.SHARED_PREFERENCE_ID_TAG, 0)
                            .putString(Constants.SHARED_PREFERENCE_TOKEN_TAG, "NULL")
                            .apply();
                    Utils.log(TAG, "Error when logout");
                }
            });
        }


    }
}
