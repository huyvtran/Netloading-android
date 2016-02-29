package com.ketnoivantai.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.ketnoivantai.NetloadingApplication;
import com.ketnoivantai.R;
import com.ketnoivantai.model.webservice.ServiceGenerator;
import com.ketnoivantai.utils.Constants;
import com.ketnoivantai.utils.Utils;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String token = sharedPreferences.getString(Constants.SHARED_PREFERENCE_TOKEN_TAG, "NULL");

        if (token.equals("NULL")) {
            Intent intent = LoginActivity.makeIntent(mActivity, LoginActivity.LOGIN_NULL_TOKEN)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mActivity.startActivity(intent);

            return;
        }
//        Utils.log(TAG, v.getId() + " clicked on navigation drawer");

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

            Utils.log(TAG, "on navation sign out");
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
