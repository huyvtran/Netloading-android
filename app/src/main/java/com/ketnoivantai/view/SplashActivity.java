package com.ketnoivantai.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.ketnoivantai.NetloadingApplication;
import com.ketnoivantai.R;
import com.ketnoivantai.common.GenericActivity;
import com.ketnoivantai.presenter.SplashPresenter;
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