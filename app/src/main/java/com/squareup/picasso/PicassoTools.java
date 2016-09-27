package com.squareup.picasso;

import android.util.Log;

/**
 * Created by AnhVu on 9/16/16.
 */
public class PicassoTools {
    public static void clearCache (Picasso p) {
        Log.v("PicassoTools", "clear cache");
        p.cache.clear();
    }
}
