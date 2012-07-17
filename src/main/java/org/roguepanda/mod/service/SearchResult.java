package org.roguepanda.mod.service;

import java.util.Iterator;
import java.util.List;

public class SearchResult implements Iterable<SearchResultItem>
{
	private List<SearchResultItem> items;
	
	private String suggestedQuery;
	
	@Override
	public Iterator<SearchResultItem> iterator()
	{
		return items.iterator();
	}

	public SearchResult(List<SearchResultItem> items, String suggestedQuery) {
		super();
		this.items = items;
		this.suggestedQuery = suggestedQuery;
	}

	public List<SearchResultItem> getItems() {
		return items;
	}

	public String getSuggestedQuery() {
		return suggestedQuery;
	}

}
