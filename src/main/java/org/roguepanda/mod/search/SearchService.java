package org.roguepanda.mod.search;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;

public interface SearchService
{
	public List<SearchResult> search(String query) throws QueryNodeException;
	
	public String escape(String query);
}
