package com.netloading.model.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.netloading.R;
import com.netloading.model.pojo.GCMTokenPOJO;
import com.netloading.model.webservice.NetloadingService;
import com.netloading.model.webservice.ServiceGenerator;
import com.netloading.utils.Constants;
import com.netloading.utils.NotAuthenticatedException;
import com.netloading.utils.Utils;

import java.io.IOException;

/**
 * Created by Dandoh on 2/15/16.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String GCM_TOKEN = "gcmToken";


    public static Intent makeIntent(Context context) {
        return new Intent(context, RegistrationIntentService.class);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(TAG);
    }

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Make a call to Instance API
        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Utils.log(TAG, "GCM Registration Token: " + token);

            sharedPreferences.edit().putString(GCM_TOKEN, token).apply();


            // pass along this data
            sendRegistrationToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.log(TAG, "Failed to complete token refresh");

            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // send network request, send to server
        NetloadingService netloadingService = null;
        try {
            netloadingService = ServiceGenerator.getNetloadingService();
        } catch (NotAuthenticatedException e) {
            e.printStackTrace();
        }
        int id = sharedPreferences.getInt(Constants.SHARED_PREFERENCE_ID_TAG, 0);


        try {
            netloadingService.sendRegistrationTokenToServer(new GCMTokenPOJO(id, token))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    }

}


