package org.roguepanda.mod.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SearchServiceImpl implements SearchService
{
	public static final int MAX_RESULTS = 50;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Directory directory;
	
	private StandardQueryParser parser;
	
	@Autowired
	public void setAnalyzer(Analyzer analyzer)
	{
		parser = new StandardQueryParser(analyzer);
		parser.setAllowLeadingWildcard(true);
		//parser.setDefaultOperator(Operator.OR);
	}
	
	public String escape(String query)
	{
		return QueryParserUtil.escape(query);
	}
	
	public List<SearchResult> search(String query) throws QueryNodeException
	{
		DirectoryReader reader = null;
		try
		{
			log.trace("Searching for query '{}'", query);
			reader = DirectoryReader.open(directory);
			
			IndexSearcher searcher = new IndexSearcher(reader);
			Query q = parser.parse(query, "name"); //search name field by default
			TopDocs hits = searcher.search(q, MAX_RESULTS);
			
			List<SearchResult> results = new ArrayList<SearchResult>(hits.totalHits);
			for(ScoreDoc sd : hits.scoreDocs)
			{
				Document doc = searcher.doc(sd.doc);
				results.add(new LuceneSearchResult(doc));
			}
			return results;
		}
		catch(IOException e)
		{
			log.error("Error executing search", e);
		}
		finally
		{
			try
			{
				if(reader != null)
					reader.close();
			}
			catch (IOException e)
			{
				log.error("Error closing Lucene DirectoryReader", e);
			}
		}
		return null;
	}
	
	
	private static class LuceneSearchResult implements SearchResult
	{
		private Document doc;
		
		public LuceneSearchResult(Document doc)
		{
			assert doc != null;
			this.doc = doc;
		}
		
		public Long getId()
		{
			IndexableField id =  doc.getField("id");
			assert id != null;
			return Long.valueOf(id.stringValue());
		}

		public String getName()
		{
			return doc.getField("name").stringValue();
		}

		public Type getType()
		{
			String type = doc.getField("type").stringValue();
			Assert.notNull(type, "Type field of indexed document is null");
			if(type.equals("mod"))
				return Type.MOD;
			else if(type.equals("user"))
				return Type.USER;
			else
				throw new IllegalStateException("'" + type + "' is neither mod nor user");
		}
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append(getId());
			sb.append(": ");
			sb.append(getName());
			sb.append(" (");
			sb.append(getType());
			sb.append(")");
			return sb.toString();
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 19;
			int result = 1;
			result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
			result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
			result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj instanceof SearchResult)
			{
				SearchResult other = (SearchResult) obj;
				if(! (this.getType().equals(other.getType())))
				{
					return false;
				}
				else
				{
					if(!(this.getId().equals(other.getId())))
						return false;
					else
					{
						if(!this.getName().equals(other.getName()))
							return false;
						else
							return true;
					}
				}
			}
			else
			{
				return false;
			}
		}
	}
}
