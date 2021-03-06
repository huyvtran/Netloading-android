package com.ketnoivantai.model.gcm;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.ketnoivantai.NetloadingApplication;
import com.ketnoivantai.R;
import com.ketnoivantai.model.pojo.OrderPOJO;
import com.ketnoivantai.model.pojo.RequestPOJO;
import com.ketnoivantai.model.webservice.ServiceGenerator;
import com.ketnoivantai.utils.Utils;
import com.ketnoivantai.view.OrderInformationActivity;
import com.ketnoivantai.view.PickCompanyActivity;
import com.ketnoivantai.view.PickLocationActivity;
import com.ketnoivantai.view.RequestInformationActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private static final String TAG = "GcmMessageHandler";
    private static final int STATUS_ADD_TRIP_AVAILABLE = 1;
    private static final int STATUS_ACCEPT_TRIP = 2;
    private static final int STATUS_DENY_TRIP = 3;
    private static final int STATUS_NOTIFICATION_TO_ALL = 4;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (data == null) return;


        int status = Integer.parseInt(data.getString("status"));
        Utils.log(TAG, "status received " + status);
        if (status == STATUS_ADD_TRIP_AVAILABLE) {
            tripAvailableNotification(data);
        } else if (status == STATUS_ACCEPT_TRIP) {
            companyAcceptNotification(data);
        } else if (status == STATUS_DENY_TRIP) {
            companyDenyNotification(data);
        } else if (status == STATUS_NOTIFICATION_TO_ALL) {
            showNotificationFromAdmins(data);
        }
    }

    private void showNotificationFromAdmins(Bundle data) {
        final String message = data.getString("message");
        final String title = data.getString("title");


        // start activity
        Intent intent = PickLocationActivity.makeIntent(getBaseContext(), 2)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Bundle b = new Bundle();
        b.putString("message", message);
        b.putString("title", title);
        intent.putExtras(b);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getBaseContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Context context = getBaseContext();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_netloading).setContentTitle(title)
                .setContentText("Bấm vào để xem chi tiết.")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }

    private void companyAcceptNotification(Bundle data) {
        final String message = data.getString("message");
        final int orderId = Integer.parseInt(data.getString("order_id"));

        ServiceGenerator.getNetloadingService()
                .getOrderDetailById(orderId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.getString("status").equals("success")) {
                        OrderPOJO orderPOJO = new Gson().fromJson(result.getJSONObject("message").toString(),
                                OrderPOJO.class);

                        Intent intent = OrderInformationActivity.makeIntent(
                                getBaseContext(),
                                orderPOJO.getRequest(),
                                orderPOJO.getOrder().getId(),
                                orderPOJO.getCompany_name(),
                                orderPOJO.getPrice(),
                                orderPOJO.getOrder().getStatus()
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        PendingIntent pendingIntent = PendingIntent.getActivity(
                                getBaseContext(),
                                0,
                                intent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );

                        Context context = getBaseContext();
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_notification_netloading).setContentTitle(message)
                                .setContentText("Ấn vào để xem đơn hàng")
                                .setContentIntent(pendingIntent)
                                .setSound(defaultSoundUri)
                                .setAutoCancel(true);

                        NotificationManager mNotificationManager = (NotificationManager) context
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    // Creates notification based on title and body received
    private void tripAvailableNotification(Bundle data) {

        Utils.log(TAG, (NetloadingApplication.getAppContext() == null) + " NULL OR NOT");

        final String message = data.getString("message");
        final int requestId = Integer.parseInt(data.getString("request_id"));

        // start activity
        Intent intent = PickCompanyActivity.makeIntent(getBaseContext(),
                requestId).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getBaseContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Context context = getBaseContext();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_netloading).setContentTitle(message)
                .setContentText("Bấm vào để tìm nhà xe.")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }

    private void companyDenyNotification(Bundle data) {
        //TODO - hehe
        Utils.log(TAG, "Den company deny noti roi");

        final String message = data.getString("message");
        final int requestId = Integer.parseInt(data.getString("request_id"));

        ServiceGenerator.getNetloadingService()
                .getRequestDetailById(requestId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());

                    if (result.getString("status").equals("success")) {
                        RequestPOJO requestPOJO = new Gson().fromJson(result.getJSONObject("message").toString(),
                                RequestPOJO.class);
                        requestPOJO.setStatus(1);

                        Intent intent = RequestInformationActivity.makeIntent(
                                getBaseContext(), requestPOJO.getId(), RequestInformationActivity.STATE_FROM_GCM)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        PendingIntent pendingIntent = PendingIntent.getActivity(
                                getBaseContext(),
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                        Context context = getBaseContext();
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_notification_netloading).setContentTitle(message)
                                .setContentText("Ấn vào để xem lại yêu cầu.")
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent).setAutoCancel(true);
                        NotificationManager mNotificationManager = (NotificationManager) context
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}