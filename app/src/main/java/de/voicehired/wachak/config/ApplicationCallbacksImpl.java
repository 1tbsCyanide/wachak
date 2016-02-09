package de.voicehired.wachak.config;


import android.app.Application;
import android.content.Context;
import android.content.Intent;

import de.voicehired.wachak.PodcastApp;
import de.voicehired.wachak.activity.StorageErrorActivity;
import de.voicehired.wachak.core.ApplicationCallbacks;

public class ApplicationCallbacksImpl implements ApplicationCallbacks {

    @Override
    public Application getApplicationInstance() {
        return PodcastApp.getInstance();
    }

    @Override
    public Intent getStorageErrorActivity(Context context) {
        return new Intent(context, StorageErrorActivity.class);
    }

}
