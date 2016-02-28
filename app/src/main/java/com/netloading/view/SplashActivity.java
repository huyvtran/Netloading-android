package com.netloading.view;

import android.content.Intent;
import android.os.Bundle;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.presenter.SplashPresenter;

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
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
