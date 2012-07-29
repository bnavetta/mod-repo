package org.roguepanda.mod.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.ModRepository;
import org.roguepanda.mod.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class IndexBuilder
{
	private Logger log = LoggerFactory.getLogger(getClass());
	
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
		log.info("Rebuilding index");
		IndexWriter writer = null;
		try
		{
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_40, analyzer));
			log.trace("Wiping existing index");
			writer.deleteAll();
			log.trace("Index wiped");
			log.trace("Adding users");
			for(User user : userRepository.findAll())
			{
				writer.addDocument(user.asDocument());
			}
			log.trace("Users added");
			log.trace("Adding mods");
			for(Mod mod : modRepository.findAll())
			{
				writer.addDocument(mod.asDocument());
			}
			log.trace("Mods added");
			writer.commit();
			log.info("Index rebuilt");
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