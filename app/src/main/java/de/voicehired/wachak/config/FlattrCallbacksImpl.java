package de.voicehired.wachak.config;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.shredzone.flattr4j.oauth.AccessToken;

import de.voicehired.wachak.BuildConfig;
import de.voicehired.wachak.activity.FlattrAuthActivity;
import de.voicehired.wachak.activity.MainActivity;
import de.voicehired.wachak.core.FlattrCallbacks;

public class FlattrCallbacksImpl implements FlattrCallbacks {
    private static final String TAG = "FlattrCallbacksImpl";

    @Override
    public boolean flattrEnabled() {
        return true;
    }

    @Override
    public Intent getFlattrAuthenticationActivityIntent(Context context) {
        return new Intent(context, FlattrAuthActivity.class);
    }

    @Override
    public PendingIntent getFlattrFailedNotificationContentIntent(Context context) {
        return PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
    }

    @Override
    public String getFlattrAppKey() {
        return BuildConfig.FLATTR_APP_KEY;
    }

    @Override
    public String getFlattrAppSecret() {
        return BuildConfig.FLATTR_APP_SECRET;
    }

    @Override
    public void handleFlattrAuthenticationSuccess(AccessToken token) {
        FlattrAuthActivity instance = FlattrAuthActivity.getInstance();
        if (instance != null) {
            instance.handleAuthenticationSuccess();
        } else {
            Log.e(TAG, "FlattrAuthActivity instance was null");
        }
    }
}
