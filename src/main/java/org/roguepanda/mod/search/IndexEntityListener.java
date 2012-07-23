package org.roguepanda.mod.search;

import java.io.IOException;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class IndexEntityListener
{
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Directory directory;
	
	@Autowired
	private Analyzer analyzer;
	
	private IndexWriter getIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException
	{
		assert analyzer != null;
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		assert directory != null;
		return new IndexWriter(directory, config);
	}
	
	@PostPersist //Only index if things went well
	public void addToIndex(Indexable indexable)
	{
		IndexWriter iw = null;
		try
		{
			log.trace("Adding {} to index", indexable);
			iw = getIndexWriter();
			Document doc = indexable.asDocument();
			iw.addDocument(doc);
		}
		catch (CorruptIndexException e)
		{
			log.error("Error adding object to search index", e);
		}
		catch (LockObtainFailedException e)
		{
			log.error("Error adding object to search index", e);
		}
		catch (IOException e)
		{
			log.error("Error adding object to search index", e);
		}
		finally
		{
			if(iw != null)
			{
				try
				{
					iw.close();
				}
				catch (CorruptIndexException e)
				{
					log.error("Error adding object to search index", e);
				}
				catch (IOException e)
				{
					log.error("Error adding object to search index", e);
				}
			}
		}
	}
	
	@PostUpdate //Only index if things went well
	public void updateIndex(Indexable indexable)
	{
		IndexWriter iw = null;
		try
		{
			log.trace("Updating {} in index", indexable);
			iw = getIndexWriter();
			Term[] terms = indexable.getIdTerms();
			Document doc = indexable.asDocument();
			iw.deleteDocuments(terms);
			iw.addDocument(doc);
		}
		catch (CorruptIndexException e)
		{
			log.error("Error updating search index", e);
		}
		catch (LockObtainFailedException e)
		{
			log.error("Error updating search index", e);
		}
		catch (IOException e)
		{
			log.error("Error updating search index", e);
		}
		finally
		{
			if(iw != null)
			{
				try
				{
					iw.close();
				}
				catch (CorruptIndexException e)
				{
					log.error("Error updating search index", e);
				}
				catch (IOException e)
				{
					log.error("Error updating search index", e);
				}
			}
		}
	}
	
	@PreRemove
	public void deleteFromIndex(Indexable indexable)
	{
		IndexWriter iw = null;
		try
		{
			log.trace("Deleting {} from index", indexable);
			iw = getIndexWriter();
			Term[] terms = indexable.getIdTerms();
			iw.deleteDocuments(terms);
		}
		catch (CorruptIndexException e)
		{
			log.error("Error deleting object from search index", e);
		}
		catch (LockObtainFailedException e)
		{
			log.error("Error deleting object from search index", e);
		}
		catch (IOException e)
		{
			log.error("Error deleting object from search index", e);
		}
		finally
		{
			if(iw != null)
			{
				try
				{
					iw.close();
				}
				catch (CorruptIndexException e)
				{
					log.error("Error updating search index", e);
				}
				catch (IOException e)
				{
					log.error("Error updating search index", e);
				}
			}
		}
	}
}
