package com.netloading.presenter;

import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;

import java.lang.ref.WeakReference;

/**
 * Created by AnhVu on 2/26/16.
 */
public class RequestInformationPresenter implements ConfigurableOps<RequestInformationPresenter.View>{

    private WeakReference<View> mView;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {

        this.mView = new WeakReference<View>(view);

    }

    public interface View extends ContextView {

    }

}
