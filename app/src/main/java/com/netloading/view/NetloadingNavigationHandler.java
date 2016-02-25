package com.netloading.view;

import android.app.Activity;
import android.view.View;

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

        Utils.log(TAG, v.getId() + " clicked on navigation drawer");
        switch (v.getId()) {
            // TODO
        }
    }
}
