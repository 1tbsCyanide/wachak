package de.voicehired.wachak.fragment.gpodnet;

import de.voicehired.wachak.core.gpoddernet.GpodnetService;
import de.voicehired.wachak.core.gpoddernet.GpodnetServiceException;
import de.voicehired.wachak.core.gpoddernet.model.GpodnetPodcast;
import de.voicehired.wachak.core.preferences.GpodnetPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays suggestions from gpodder.net
 */
public class SuggestionListFragment extends PodcastListFragment {
    private static final int SUGGESTIONS_COUNT = 50;

    @Override
    protected List<GpodnetPodcast> loadPodcastData(GpodnetService service) throws GpodnetServiceException {
        if (GpodnetPreferences.loggedIn()) {
            service.authenticate(GpodnetPreferences.getUsername(), GpodnetPreferences.getPassword());
            return service.getSuggestions(SUGGESTIONS_COUNT);
        } else {
            return new ArrayList<GpodnetPodcast>();
        }
    }
}
