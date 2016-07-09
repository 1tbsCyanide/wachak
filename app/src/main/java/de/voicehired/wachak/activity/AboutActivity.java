package de.voicehired.wachak.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;

import de.voicehired.wachak.R;
import de.voicehired.wachak.core.preferences.UserPreferences;
import rx.Subscription;

/**
 * Displays the 'about' screen
 */
public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(UserPreferences.getTheme());
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.about);
    }
}