package com.ketnoivantai.presenter;

import android.os.Handler;

import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;

import java.lang.ref.WeakReference;

/**
 * Created by AnhVu on 2/28/16.
 */
public class SplashPresenter implements ConfigurableOps<SplashPresenter.View> {

    private WeakReference<View> mView;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        this.mView = new WeakReference<View>(view);
    }

    public void setDelayTime(int i) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.get().finishSplash();
            }
        }, i);

    }

    public interface View extends ContextView {

        void finishSplash();
    }
}
