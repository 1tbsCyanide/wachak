package de.voicehired.wachak.config;

import de.voicehired.wachak.core.DBTasksCallbacks;
import de.voicehired.wachak.core.preferences.UserPreferences;
import de.voicehired.wachak.core.storage.APDownloadAlgorithm;
import de.voicehired.wachak.core.storage.AutomaticDownloadAlgorithm;
import de.voicehired.wachak.core.storage.EpisodeCleanupAlgorithm;

public class DBTasksCallbacksImpl implements DBTasksCallbacks {

    @Override
    public AutomaticDownloadAlgorithm getAutomaticDownloadAlgorithm() {
        return new APDownloadAlgorithm();
    }

    @Override
    public EpisodeCleanupAlgorithm getEpisodeCacheCleanupAlgorithm() {
        return UserPreferences.getEpisodeCleanupAlgorithm();
    }
}
