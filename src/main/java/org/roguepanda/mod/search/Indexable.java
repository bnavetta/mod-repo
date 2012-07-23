package org.roguepanda.mod.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

public interface Indexable
{
	public Term[] getIdTerms();
	
	public Document asDocument();
}
