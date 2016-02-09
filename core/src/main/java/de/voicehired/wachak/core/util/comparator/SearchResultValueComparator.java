package de.voicehired.wachak.core.util.comparator;

import de.voicehired.wachak.core.feed.SearchResult;

import java.util.Comparator;

public class SearchResultValueComparator implements Comparator<SearchResult> {

	@Override
	public int compare(SearchResult lhs, SearchResult rhs) {
		return rhs.getValue() - lhs.getValue();
	}

}
