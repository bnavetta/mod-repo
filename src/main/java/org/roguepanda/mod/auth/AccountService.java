package org.roguepanda.mod.auth;

import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class AccountService
{
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public User createUser(String name, String password)
	{
		byte[] salt = PasswordHashGenerator.generateSalt();
		byte[] pwdHash = PasswordHashGenerator.getHash(password, salt);
			
		User user = new User();
		user.setName(name);
		user.setPassword(pwdHash);
		user.setSalt(salt);
		
		return userRepository.saveAndFlush(user);	
	}
	
	@Transactional(readOnly=true)
	public boolean authenticate(String username, String password)
	{
		User user = userRepository.findByName(username);
		if(user == null)
			return false;
		return authenticate(user, password);
	}
	
	public boolean authenticate(User user, String password)
	{
		return PasswordHashGenerator.authenticate(user, password);
	}
}
