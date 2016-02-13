package com.netloading.utils;

import android.util.Log;

/**
 * Created by Dandoh on 2/13/16.
 */
public class LogUtils {

    public static boolean isDev = true;

    public static void log(String tag, String message) {
        if (isDev) {
            Log.i(tag, message);
        }
    }
}
