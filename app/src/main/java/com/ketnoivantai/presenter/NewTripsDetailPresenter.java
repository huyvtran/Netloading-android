package com.ketnoivantai.presenter;

import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;

import java.lang.ref.WeakReference;

/**
 * Created by AnhVu on 9/15/16.
 */
public class NewTripsDetailPresenter implements ConfigurableOps<NewTripsDetailPresenter.View> {

    private static final String TAG = "NewTripsDetailPresenter";
    private WeakReference<View> mView;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public interface View extends ContextView {

    }
}
