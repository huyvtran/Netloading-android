package com.netloading.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
}
