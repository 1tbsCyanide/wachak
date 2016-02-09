package de.voicehired.wachak;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialModule;

import de.voicehired.wachak.core.feed.EventDistributor;
import de.voicehired.wachak.core.preferences.PlaybackPreferences;
import de.voicehired.wachak.core.preferences.UserPreferences;
import de.voicehired.wachak.core.storage.PodDBAdapter;
import de.voicehired.wachak.core.util.NetworkUtils;
import de.voicehired.wachak.spa.SPAUtil;

/** Main application class. */
public class PodcastApp extends Application {

    // make sure that ClientConfigurator executes its static code
    static {
        try {
            Class.forName("de.voicehired.wachak.config.ClientConfigurator");
        } catch (Exception e) {
            throw new RuntimeException("ClientConfigurator not found");
        }
    }

	private static PodcastApp singleton;

	public static PodcastApp getInstance() {
		return singleton;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler(new CrashReportWriter());

		if(BuildConfig.DEBUG) {
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.penaltyLog()
				.penaltyDropBox();
			if (Build.VERSION.SDK_INT >= 11) {
				builder.detectActivityLeaks();
				builder.detectLeakedClosableObjects();
			}
			if(Build.VERSION.SDK_INT >= 16) {
				builder.detectLeakedRegistrationObjects();
			}
			StrictMode.setVmPolicy(builder.build());
		}

		singleton = this;

		PodDBAdapter.init(this);
		UserPreferences.init(this);
		UpdateManager.init(this);
		PlaybackPreferences.init(this);
		NetworkUtils.init(this);
		EventDistributor.getInstance();
		Iconify.with(new FontAwesomeModule());
		Iconify.with(new MaterialModule());

        SPAUtil.sendSPAppsQueryFeedsIntent(this);
    }

}
