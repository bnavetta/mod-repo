package org.roguepanda.mod.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

public interface Indexable
{
	public Query getQuery();
	
	public Document asDocument();
}
