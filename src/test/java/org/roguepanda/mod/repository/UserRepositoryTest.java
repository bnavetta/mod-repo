package org.roguepanda.mod.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.roguepanda.mod.auth.PasswordHashGenerator;
import org.roguepanda.mod.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/*.xml")
public class UserRepositoryTest
{
	@Autowired
	private UserRepository repo;
	
	@Before
	@After
	@Transactional
	public void cleanDB()
	{
		repo.deleteAll();
	}
	
	@Test
	@Transactional
	public void testCRUD() throws Exception
	{
		User user = new User();
		user.setName("testCRUDUser");
		user.setSalt(PasswordHashGenerator.generateSalt());
		user.setPassword(PasswordHashGenerator.getHash("password", user.getSalt()));
		
		User saved = repo.saveAndFlush(user);
		Assert.assertNotNull(saved);
		Assert.assertEquals(user, saved);
		Assert.assertEquals("testCRUDUser", saved.getName());
		assertNotNull(saved.getId());
		
		Assert.assertNotNull(saved.getId());
		User found = repo.findOne(saved.getId());
		Assert.assertNotNull(found);
		Assert.assertEquals(saved, found);
		
		found.setName("TESTCRUDUSER");
		Assert.assertEquals("TESTCRUDUSER", repo.saveAndFlush(found).getName());
		
		repo.delete(found);
		Assert.assertTrue(repo.findAll().isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindByName() throws Exception
	{
		User user = new User();
		user.setName("testFindByNameUser");
		user.setSalt(PasswordHashGenerator.generateSalt());
		user.setPassword(PasswordHashGenerator.getHash("password", user.getSalt()));
		
		Assert.assertNotNull(repo.saveAndFlush(user));
		
		User found = repo.findByName("testFindByNameUser");
		Assert.assertNotNull(found);
		Assert.assertEquals("testFindByNameUser", found.getName());
		Assert.assertEquals(user, found);
	}

}
