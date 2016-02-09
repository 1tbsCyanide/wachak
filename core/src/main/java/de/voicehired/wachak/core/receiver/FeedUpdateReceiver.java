package de.voicehired.wachak.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.voicehired.wachak.core.preferences.UserPreferences;
import de.voicehired.wachak.core.storage.DBTasks;
import de.voicehired.wachak.core.util.NetworkUtils;

/**
 * Refreshes all feeds when it receives an intent
 */
public class FeedUpdateReceiver extends BroadcastReceiver {

    private static final String TAG = "FeedUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent");
        if (NetworkUtils.isDownloadAllowed()) {
            DBTasks.refreshAllFeeds(context, null);
        } else {
            Log.d(TAG, "Blocking automatic update: no wifi available / no mobile updates allowed");
        }
        UserPreferences.restartUpdateAlarm(false);
    }

}
