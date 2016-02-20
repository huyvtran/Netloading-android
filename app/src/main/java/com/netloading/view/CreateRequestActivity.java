package com.netloading.view;

import android.os.Bundle;

import com.netloading.R;
import com.netloading.common.GenericActivity;
import com.netloading.presenter.CreateRequestPresenter;

/**
 * Created by Dandoh on 2/20/16.
 */
public class CreateRequestActivity extends GenericActivity<CreateRequestPresenter.View, CreateRequestPresenter>
        implements CreateRequestPresenter.View {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_request_activity);

        super.onCreate(savedInstanceState, CreateRequestPresenter.class, this);
    }
}
