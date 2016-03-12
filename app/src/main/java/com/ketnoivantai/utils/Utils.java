package com.ketnoivantai.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ketnoivantai.view.PickLocationActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by Dandoh on 2/13/16.
 */
public class Utils {

    public static boolean isDev = true;
    public static DecimalFormatSymbols otherSymbols;
    public static DecimalFormat df;

    public static void log(String tag, String message) {
        if (isDev) {
            Log.i(tag, message);
        }
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void backToHome(Context context) {
        Intent intent = PickLocationActivity.makeIntent(context, 1);
        context.startActivity(intent);
    }

    public static String formatNumber(int num) {
        if (otherSymbols == null) {
            otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
            otherSymbols.setDecimalSeparator(',');
            otherSymbols.setGroupingSeparator('.');
        }

        if (df == null) df = new DecimalFormat("#,###.##", otherSymbols);

        return df.format(num);
    }


}
