package de.voicehired.wachak.adapter;

import de.voicehired.wachak.core.feed.FeedItem;

public interface ActionButtonCallback {
	/** Is called when the action button of a list item has been pressed. */
	abstract void onActionButtonPressed(FeedItem item);
}
