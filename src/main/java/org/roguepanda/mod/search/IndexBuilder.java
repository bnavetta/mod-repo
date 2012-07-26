package org.roguepanda.mod.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;
import org.springframework.transaction.annotation.Transactional;
import org.roguepanda.mod.repository.*;
import org.roguepanda.mod.domain.*;

public class IndexBuilder
{
	@Autowired
	private Analyzer analyzer;
	
	@Autowired
	private Directory directory;
	
	@Autowired
	private ModRepository modRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public void rebuildIndex() throws Exception
	{
		IndexWriter writer = null;
		try
		{
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_40, analyzer));
			writer.deleteAll();
			for(User user : userRepository.findAll())
			{
				writer.addDocument(user.asDocument());
			}
			for(Mod mod : modRepository.findAll())
			{
				writer.addDocument(mod.asDocument());
			}
			writer.commit();
		}
		finally
		{
			if(writer != null)
			{
				writer.close();
			}
		}
	}
}