package com.ketnoivantai.customers.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.ketnoivantai.NetloadingApplication;
import com.ketnoivantai.R;
import com.ketnoivantai.customers.common.GenericActivity;
import com.ketnoivantai.customers.presenter.SplashPresenter;
import com.ketnoivantai.utils.Constants;
import com.ketnoivantai.utils.Utils;

/**
 * Created by AnhVu on 2/28/16.
 */
public class SplashActivity extends GenericActivity<SplashPresenter.View, SplashPresenter>
    implements SplashPresenter.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState, SplashPresenter.class, this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        resetDefaultValue();

        // clear picasso cache
//        PicassoTools.clearCache(Picasso.with(this));

        getOps().setDelayTime(3000);

    }

    private void resetDefaultValue() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit()
                .putString(Constants.GOODS_NAME, "")
                .putInt(Constants.GOODS_WEIGHT_NUMBER, 0)
                .putString(Constants.GOODS_PICKUP_DATE, "")
                .putString(Constants.GOODS_DESCRIPTION, "0").apply();
    }

    @Override
    public void finishSplash() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(NetloadingApplication.getAppContext());

        String token = sharedPreferences.getString(Constants.SHARED_PREFERENCE_TOKEN_TAG, "NULL");
        Utils.log(TAG, token);
        if (token.equals("NULL")) {
            startActivity(PickLocationActivity.makeIntent(this, 0));
        } else {
            startActivity(PickLocationActivity.makeIntent(this, 1));
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
