package org.roguepanda.mod.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.ModRepository;
import org.roguepanda.mod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/WEB-INF/spring/*.xml")
public class SearchServiceImplTest
{
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private ModRepository mRepo;
	
	@After
	public void cleanDB()
	{
		mRepo.deleteAll();
		uRepo.deleteAll();
	}
	
	@Test
	public void testType() //Is this a bad idea?
	{
		assertThat(searchService, is(SearchServiceImpl.class));
	}
	
	@Test
	public void testSearch()
	{
		//remember to create test data
		User user = new User();
		user.setName("testUser");
		user.setPassword(new byte[]{1,4,2,5});
		user.setSalt(new byte[]{4,5,4,14,63});
		
		Mod mod = new Mod();
		mod.setName("epicMod");
		mod.setDescription("an epic mod");
		mod.setFileKey("abcde");
		mod.setHome("http://www.example.com");
		mod.setInstallScript("println 'Hello, World!'");
		
		user.addMod(mod);
		assertNotNull(uRepo.saveAndFlush(user));
		
		SearchResult res = searchService.search("epic");
		System.err.println(res.getSuggestedQuery());
		System.err.println(res.getItems());
	}

}
