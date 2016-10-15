package com.ketnoivantai;

import android.app.Application;
import android.content.Context;

import com.ketnoivantai.utils.Utils;

/**
 * Created by Dandoh on 2/27/16.
 */
public class NetloadingApplication extends Application {

    private static final String TAG = "NetloadingApplication";
    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.log(TAG, "Application started");
        appContext = this;

    }
    public static Context getAppContext() {
        return appContext;
    }

}
