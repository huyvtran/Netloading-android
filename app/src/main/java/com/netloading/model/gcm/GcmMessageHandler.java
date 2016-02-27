package com.netloading.model.gcm;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netloading.R;
import com.netloading.model.pojo.CompanyTripPOJO;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.Constants;
import com.netloading.utils.NotAuthenticatedException;
import com.netloading.utils.Utils;
import com.netloading.view.PickCompanyActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private static final String TAG = "GcmMessageHandler";
    private static final int STATUS_ADD_TRIP_AVAILABLE = 1;
    private static final int STATUS_ACCEPT_TRIP = 2;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (data == null) return;

        if (!ServiceGenerator.isLoggedIn()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            String token = sharedPreferences.getString(Constants.SHARED_PREFERENCE_TOKEN_TAG, " ");
            int customer_id = sharedPreferences.getInt(Constants.SHARED_PREFERENCE_ID_TAG, 0);

            ServiceGenerator.initialize(token, customer_id);
        }

        int status = Integer.parseInt(data.getString("status"));
        Utils.log(TAG, "status received " + status);
        if (status == STATUS_ADD_TRIP_AVAILABLE) {
            tripAvailableNotification(data);
        } else if (status == STATUS_ACCEPT_TRIP) {
            companyAcceptNotification(data);
        }
    }

    private void companyAcceptNotification(Bundle data) {

    }

    // Creates notification based on title and body received
    private void tripAvailableNotification(Bundle data) {
        final String message = data.getString("message");
        final int requestId = Integer.parseInt(data.getString("request_id"));

        try {
            ServiceGenerator.getNetloadingService()
                    .retryRequest(requestId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Utils.log(TAG, result.toString());
                        if (result.getString("status").equals("success")) {

                            // Get company list
                            Gson gson = new Gson();
                            JSONArray companiesArray = result.getJSONObject("message").getJSONArray("trips");
                            Type listType = new TypeToken<ArrayList<CompanyTripPOJO>>() {}.getType();
                            ArrayList<CompanyTripPOJO> companyTripPOJOs = gson.fromJson(companiesArray.toString(), listType);

                            // start activity
                            Intent intent = PickCompanyActivity.makeIntent(getBaseContext(),
                                    companyTripPOJOs, requestId);

                            PendingIntent pendingIntent = PendingIntent.getActivity(
                                    getBaseContext(),
                                    0,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                            Context context = getBaseContext();
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_notification_netloading).setContentTitle(message)
                                    .setContentText("Ấn vào ...")
                                    .setContentIntent(pendingIntent);
                            NotificationManager mNotificationManager = (NotificationManager) context
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
                        } else {
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (NotAuthenticatedException e) {
            e.printStackTrace();
        }

    }

}