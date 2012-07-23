package org.roguepanda.mod.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.ModRepository;
import org.roguepanda.mod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/WEB-INF/spring/*.xml")
public class SearchTest
{
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private ModRepository mRepo;
	
	@Transactional
	public void tearDown() throws Exception
	{
		mRepo.deleteAll();
		uRepo.deleteAll();
	}

	@Test
	@Transactional
	public void testSearch() throws Exception
	{
		createTestData();
		
		List<SearchResult> results = searchService.search("ben");
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("ben", results.get(0).getName());
		assertEquals(SearchResult.Type.USER, results.get(0).getType());
		
		results = searchService.search("Epic Mod 1");
		assertNotNull(results);
		assertEquals(2, results.size());
		
		for(SearchResult result : results)
		{
			if(!(result.getType().equals(SearchResult.Type.MOD)))
				fail("Result is not a mod");
			if(!(result.getName().equals("Epic Mod 1") || result.getName().equals("Epic Mod 2")))
				fail("Unknown name for result");
		}
		
		tearDown();
	}

	@Transactional
	private void createTestData()
	{
		User user = new User();
		user.setName("ben");
		user.setPassword(new byte[]{2,5,3,2,46});
		user.setSalt(new byte[]{3,6,31,5,35,54});
		
		Mod mod1 = new Mod();
		mod1.setName("Epic Mod 1");
		mod1.setDescription("An epic mod");
		mod1.setFileKey("abcde");
		mod1.setHome("http://www.example.com/epicMod1.html");
		mod1.setInstallScript("println 'Epic Mod 1'");
		user.addMod(mod1);
		
		Mod mod2 = new Mod();
		mod2.setName("Epic Mod 2");
		mod2.setDescription("Another epic mod");
		mod2.setFileKey("fghij");
		mod2.setHome("http://www.example.com/epicMod2.html");
		mod2.setInstallScript("println 'Epic Mod 2'");
		user.addMod(mod2);
		
		uRepo.saveAndFlush(user);
	}

}
