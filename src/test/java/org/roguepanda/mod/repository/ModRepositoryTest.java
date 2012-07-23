package org.roguepanda.mod.repository;

import static org.junit.Assert.assertEquals;
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
@ContextConfiguration("classpath:/WEB-INF/spring/*.xml")
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

}
