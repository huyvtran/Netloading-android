package com.netloading.model.gcm;

import com.google.android.gms.gcm.GcmListenerService;
import com.netloading.R;
import com.netloading.utils.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private static final String TAG = "GcmMessageHandler";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("body");
        Utils.log(TAG, message);
        createNotification(from, message);
    }

        // Creates notification based on title and body received
    private void createNotification(String title, String body) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_netloading).setContentTitle(title)
                .setContentText(body);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

}