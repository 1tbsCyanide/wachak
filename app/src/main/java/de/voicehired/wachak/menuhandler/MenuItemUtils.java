package de.voicehired.wachak.menuhandler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import de.voicehired.wachak.R;
import de.voicehired.wachak.core.preferences.UserPreferences;

/**
 * Utilities for menu items
 */
public class MenuItemUtils extends de.voicehired.wachak.core.menuhandler.MenuItemUtils {

    public static void adjustTextColor(Context context, SearchView sv) {
        if(Build.VERSION.SDK_INT < 14) {
            EditText searchEditText = (EditText) sv.findViewById(R.id.search_src_text);
            if(UserPreferences.getTheme() == de.voicehired.wachak.R.style.Theme_AntennaPod_Dark) {
                searchEditText.setTextColor(Color.WHITE);
            } else {
                searchEditText.setTextColor(Color.BLACK);
            }
        }
    }

    public static void refreshLockItem(Context context, Menu menu) {
        final MenuItem queueLock = menu.findItem(de.voicehired.wachak.R.id.queue_lock);
        int[] lockIcons = new int[] { de.voicehired.wachak.R.attr.ic_lock_open, de.voicehired.wachak.R.attr.ic_lock_closed };
        TypedArray ta = context.obtainStyledAttributes(lockIcons);
        if (UserPreferences.isQueueLocked()) {
            queueLock.setTitle(de.voicehired.wachak.R.string.unlock_queue);
            queueLock.setIcon(ta.getDrawable(0));
        } else {
            queueLock.setTitle(de.voicehired.wachak.R.string.lock_queue);
            queueLock.setIcon(ta.getDrawable(1));
        }
    }

}
