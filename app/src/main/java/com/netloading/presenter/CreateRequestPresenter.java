package com.netloading.presenter;

import com.netloading.common.ConfigurableOps;
import com.netloading.common.ContextView;

/**
 * Created by Dandoh on 2/20/16.
 */
public class CreateRequestPresenter implements ConfigurableOps<CreateRequestPresenter.View> {

    public interface View extends ContextView{
    }

    @Override
    public void onConfiguration(View view, boolean firstTimeIn) {

    }
}
