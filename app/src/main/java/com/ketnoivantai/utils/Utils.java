package com.ketnoivantai.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.ketnoivantai.customers.view.PickLocationActivity;

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


    public static Pair<String, String> ConvertToDimension(String vehicleType) {
        if (vehicleType.equals("xeTai") || vehicleType.equals("xeDongLanh")) {
            return new Pair<>("kg", "kg");
        } else if (vehicleType.equals("xeBon")) {
            return new Pair<>("m3", "m³");
        }

        return new Pair<>("kg", "kg");
    }

    public static String nameFromVehicleTypeString(String vehicleType) {
        if (vehicleType.equals("xeTai")) return "Xe tải";
        if (vehicleType.equals("xeDongLanh")) return "Xe đông lạnh";
        if (vehicleType.equals("xeBon")) return "Xe bồn";
        return null;
    }


}
