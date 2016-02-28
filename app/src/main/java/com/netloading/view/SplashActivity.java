package com.netloading.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.netloading.NetloadingApplication;
import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.presenter.SplashPresenter;
import com.netloading.utils.Constants;
import com.netloading.utils.Utils;

/**
 * Created by AnhVu on 2/28/16.
 */
public class SplashActivity extends GenericActivity<SplashPresenter.View, SplashPresenter>
    implements SplashPresenter.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        super.onCreate(savedInstanceState, SplashPresenter.class, this);
        getOps().setDelayTime(3000);

    }

    @Override
    public void finishSplash() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(NetloadingApplication.getAppContext());

        String token = sharedPreferences.getString(Constants.SHARED_PREFERENCE_TOKEN_TAG, "NULL");
        Utils.log(TAG, token);
        if (token.equals("NULL")) {
            startActivity(PickLocationActivity.makeIntent(this, false));
        } else {
            startActivity(PickLocationActivity.makeIntent(this, true));
        }
        finish();
    }
}
