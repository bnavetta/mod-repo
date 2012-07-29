package org.roguepanda.mod.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.roguepanda.mod.auth.PasswordHashGenerator;
import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/*.xml")
public class ModRepositoryTest
{
	@Autowired
	private ModRepository repo;
	
	@Autowired
	private UserRepository userRepository;
	
	private User user;
	
	@Before
	@Transactional
	public void createTestUser() throws Exception
	{
		repo.deleteAll();
		userRepository.deleteAll();
		
		User u = new User();
		u.setName("modTestUser");
		u.setSalt(PasswordHashGenerator.generateSalt());
		u.setPassword(PasswordHashGenerator.getHash("password", u.getSalt()));
		
		user = userRepository.saveAndFlush(u);
	}
	
	@After
	@Transactional
	public void cleanDB()
	{
		repo.deleteAll();
		userRepository.delete(user);
	}
	
	@Test
	@Transactional
	public void testCRUD()
	{
		Mod mod = new Mod();
		mod.setName("myAwesomeMod");
		mod.setHome("http://www.example.com");
		mod.setDescription("An amazing mod in which things explode and stuff");
		mod.setFileKey("asfdaasdfasd");
		mod.setInstallScript("println 'Hello, World!\n'");
		
		user.addMod(mod);
		mod = repo.saveAndFlush(mod);
		assertNotNull(mod.getId());
		
		User u = userRepository.findOne(user.getId());
		assertTrue(u.getMods().contains(mod));
		
		Mod found = repo.findOne(mod.getId());
		assertNotNull(found);
		assertEquals(mod.getName(), found.getName());
		assertEquals(mod.getAuthor(), mod.getAuthor());
		assertEquals(mod.getHome(), found.getHome());
		assertEquals(mod.getDescription(), found.getDescription());
		assertEquals(mod.getFileKey(), found.getFileKey());
		assertEquals(mod.getInstallScript(), found.getInstallScript());
		
		found.setDescription("The greatest mod ever!");
		assertEquals("The greatest mod ever!", repo.saveAndFlush(found).getDescription());
		
		assertNotNull(found.getId());
		user.removeMod(found);
		userRepository.saveAndFlush(user);
		assertTrue(repo.findAll().isEmpty());
	}
	
	@Test
	public void testFindByName()
	{
		Mod mod = new Mod();
		mod.setName("amazingMod2");
		mod.setDescription("An amazing mod");
		mod.setFileKey("abc");
		mod.setHome("http://www.foo.com");
		mod.setInstallScript("10.times{println it}");
		
		user.addMod(mod);
		assertNotNull(mod.getAuthor());
		assertEquals(user, mod.getAuthor());
		assertNotNull(userRepository.saveAndFlush(user));
		
		List<Mod> found = repo.findByName("amazingMod2");
		assertNotNull(found);
		assertThat(found, hasItem(mod));
	}
	
	
	@Test
	@Transactional
	public void testFindRecent()
	{
		Mod mod = new Mod();
		mod.setName("amazingMod1");
		mod.setDescription("An amazing mod");
		mod.setFileKey("abc");
		mod.setHome("http://www.foo.com");
		mod.setInstallScript("10.times{println it}");
		user.addMod(mod);
		
		Mod mod2 = new Mod();
		mod2.setName("amazingMod2");
		mod2.setDescription("asf ");
		mod2.setFileKey("12345");
		mod2.setHome("http://www.google.com");
		mod2.setInstallScript("1");
		user.addMod(mod2);
		
		assertNotNull(mod.getAuthor());
		assertEquals(user, mod.getAuthor());
		assertNotNull(mod2.getAuthor());
		assertEquals(user, mod2.getAuthor());
		assertNotNull(userRepository.saveAndFlush(user));
		
		List<Mod> recent = repo.findRecent(5);
		assertNotNull(recent);
		assertFalse(recent.isEmpty());
		assertEquals(2, recent.size());
		assertEquals(mod2, recent.get(0));
		assertEquals(mod, recent.get(1));
		
		System.err.println(recent);
	}
}
