package com.netloading.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.view.PickLocationActivity;

/**
 * Created by Dandoh on 2/13/16.
 */
public class Utils {

    public static boolean isDev = true;

    public static void log(String tag, String message) {
        if (isDev) {
            Log.i(tag, message);
        }
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void backToHome(Context context) {
        Intent intent = PickLocationActivity.makeIntent(context, true);
        context.startActivity(intent);
    }


}
