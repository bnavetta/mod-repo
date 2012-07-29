package org.roguepanda.mod.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.ModRepository;
import org.roguepanda.mod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/*.xml")
public class IndexTest
{
	@Autowired
	private ModRepository modRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Directory directory;
	
	@Autowired
	private Analyzer analyzer;
	
	@Before
	@Transactional
	public void deleteIndex() throws CorruptIndexException, LockObtainFailedException, IOException
	{
		modRepository.deleteAll();
		userRepository.deleteAll();
		IndexWriter iw = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_40, analyzer));
		iw.deleteAll();
		iw.close();
	}
	
	@Test
	@Transactional
	public void testAdd() throws CorruptIndexException, IOException
	{
		User user = new User();
		user.setName("user");
		user.setPassword(new byte[]{1,2,3,4});
		user.setSalt(new byte[]{5,6,7,8});
		
		user = userRepository.saveAndFlush(user);
		
		IndexReader ir = DirectoryReader.open(directory);
		
		assertEquals(1, ir.numDocs());
		
		IndexSearcher is = new IndexSearcher(ir);
		
		Query q = new TermQuery(new Term("name", "user"));
		TopDocs docs = is.search(q, 100);
		assertEquals(1, docs.totalHits);
		Document doc = is.doc(docs.scoreDocs[0].doc);
		assertEquals("user", doc.getField("name").stringValue());
		assertEquals(user.getId().toString(), doc.getField("id").stringValue());
		assertEquals("user", doc.getField("type").stringValue());
		
		ir.close();
	}
	
	@Test
	@Transactional
	public void testRemove() throws CorruptIndexException, IOException
	{
		IndexReader ir = DirectoryReader.open(directory);
		assertEquals(0, ir.numDocs());
		ir.close();
		
		User user = new User();
		user.setName("user");
		user.setPassword(new byte[]{1,2,3,4});
		user.setSalt(new byte[]{5,6,7,8});
		
		user = userRepository.saveAndFlush(user);
		
		ir = DirectoryReader.open(directory);
		assertEquals(1, ir.numDocs());
		ir.close();
		
		userRepository.delete(user);
		
		ir = DirectoryReader.open(directory);
		assertEquals(0,ir.numDocs());
		ir.close();
	}
	
	
	@Test
	@Transactional
	public void testUpdate() throws CorruptIndexException, IOException
	{
		User user = new User();
		user.setName("user");
		user.setPassword(new byte[]{1,2,3,4});
		user.setSalt(new byte[]{5,6,7,8});
		user = userRepository.saveAndFlush(user);
		
		IndexReader ir = DirectoryReader.open(directory);
		assertEquals(1, ir.numDocs());
		IndexSearcher is = new IndexSearcher(ir);
		Query q = new TermQuery(new Term("name", "user"));
		TopDocs docs = is.search(q, 100);
		assertEquals(1, docs.totalHits);
		Document doc = is.doc(docs.scoreDocs[0].doc);
		assertEquals("user", doc.getField("name").stringValue());
		assertEquals(user.getId().toString(), doc.getField("id").stringValue());
		assertEquals("user", doc.getField("type").stringValue());
		ir.close();
		
		user.setName("theUser");
		user = userRepository.saveAndFlush(user);
		assertNotNull(user.getId());
		assert user.getId() != 0;
		
		ir = DirectoryReader.open(directory);
		assertEquals(1, ir.numDocs());
		
		is = new IndexSearcher(ir);
		q = new TermQuery(new Term("id", user.getId().toString()));
		docs = is.search(q, 100);
		assertEquals(1, docs.totalHits);
		doc = is.doc(docs.scoreDocs[0].doc);
		assertEquals("theUser", doc.getField("name").stringValue());
		assertEquals(user.getId().toString(), doc.getField("id").stringValue());
		assertEquals("user", doc.getField("type").stringValue());
		ir.close();
	}
}
