package com.netloading.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.netloading.R;
import com.netloading.utils.Utils;

/**
 * Created by Dandoh on 2/25/16.
 */
public class NetloadingNavigationHandler {

    private static final String TAG = "NetloadingNavigationHandler";
    private Activity mActivity;

    public NetloadingNavigationHandler(Activity activity) {
        this.mActivity = activity;
    }

    public void onNavigationItemClick(View v) {

//        Utils.log(TAG, v.getId() + " clicked on navigation drawer");
        switch (v.getId()) {
            // TODO
        }

        if (v.getId() == R.id.navigation_manage_requests) {
            Utils.log(TAG, v.getId() + " clicked on request button");

            Intent intent = RequestListActivity.makeIntent(mActivity.getApplicationContext());
            mActivity.startActivity(intent);
        } else
        if (v.getId() == R.id.navigation_manage_orders) {
            Intent intent = OrderListActivity.makeIntent(mActivity.getApplicationContext());
            mActivity.startActivity(intent);
        }


    }
}
