package de.voicehired.wachak.config;

import android.content.Context;
import android.content.Intent;

import de.voicehired.wachak.R;
import de.voicehired.wachak.activity.AudioplayerActivity;
import de.voicehired.wachak.activity.VideoplayerActivity;
import de.voicehired.wachak.core.PlaybackServiceCallbacks;
import de.voicehired.wachak.core.feed.MediaType;


public class PlaybackServiceCallbacksImpl implements PlaybackServiceCallbacks {
    @Override
    public Intent getPlayerActivityIntent(Context context, MediaType mediaType) {
        if (mediaType == MediaType.VIDEO) {
            return new Intent(context, VideoplayerActivity.class);
        } else {
            return new Intent(context, AudioplayerActivity.class);
        }
    }

    @Override
    public boolean useQueue() {
        return true;
    }

    @Override
    public int getNotificationIconResource(Context context) {
        return R.drawable.ic_launcher;
    }
}
