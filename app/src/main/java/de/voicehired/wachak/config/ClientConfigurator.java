package de.voicehired.wachak.config;

import de.voicehired.wachak.BuildConfig;
import de.voicehired.wachak.core.ClientConfig;

/**
 * Configures the ClientConfig class of the core package.
 */
public class ClientConfigurator {

    static {
        ClientConfig.USER_AGENT = "AntennaPod/" + BuildConfig.VERSION_NAME;
        ClientConfig.applicationCallbacks = new ApplicationCallbacksImpl();
        ClientConfig.downloadServiceCallbacks = new DownloadServiceCallbacksImpl();
        ClientConfig.gpodnetCallbacks = new GpodnetCallbacksImpl();
        ClientConfig.playbackServiceCallbacks = new PlaybackServiceCallbacksImpl();
        ClientConfig.flattrCallbacks = new FlattrCallbacksImpl();
        ClientConfig.dbTasksCallbacks = new DBTasksCallbacksImpl();
    }
}
