package com.ketnoivantai.presenter;

import com.ketnoivantai.common.ConfigurableOps;
import com.ketnoivantai.common.ContextView;

import java.lang.ref.WeakReference;

/**
 * Created by AnhVu on 2/26/16.
 */
public class OrderInformationPresenter implements ConfigurableOps<OrderInformationPresenter.View> {

    private WeakReference<View> mView;

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {
        mView = new WeakReference<View>(view);
    }

    public interface View extends ContextView {

    }

}
