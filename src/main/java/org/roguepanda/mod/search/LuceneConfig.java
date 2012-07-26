package org.roguepanda.mod.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LuceneConfig
{
	@Bean
	public Analyzer analyzer()
	{
		return new StandardAnalyzer(Version.LUCENE_40);
	}
	
	@Bean(destroyMethod="close")
	public Directory luceneDirectory() throws IOException
	{
		//TODO set up a GridFS-based directory (use files as locks)
		return FSDirectory.open(new File("mod-index"));
	}
	
	@Bean(initMethod="rebuildIndex")
	public IndexBuilder indexBuilder()
	{
		return new IndexBuilder();
	}
}
