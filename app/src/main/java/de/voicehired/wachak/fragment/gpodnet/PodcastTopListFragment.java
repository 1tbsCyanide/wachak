package de.voicehired.wachak.fragment.gpodnet;

import de.voicehired.wachak.core.gpoddernet.GpodnetService;
import de.voicehired.wachak.core.gpoddernet.GpodnetServiceException;
import de.voicehired.wachak.core.gpoddernet.model.GpodnetPodcast;

import java.util.List;

/**
 *
 */
public class PodcastTopListFragment extends PodcastListFragment {
    private static final String TAG = "PodcastTopListFragment";
    private static final int PODCAST_COUNT = 50;

    @Override
    protected List<GpodnetPodcast> loadPodcastData(GpodnetService service) throws GpodnetServiceException {
        return service.getPodcastToplist(PODCAST_COUNT);
    }
}
