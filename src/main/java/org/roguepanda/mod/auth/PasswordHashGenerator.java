package org.roguepanda.mod.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.roguepanda.mod.domain.User;

//Same as VoiceOver
public class PasswordHashGenerator
{
	private static SecureRandom rand;
	private static MessageDigest digest;
	
	static{
		try
		{
			rand = SecureRandom.getInstance("SHA1PRNG");
			digest = MessageDigest.getInstance("SHA-256");
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new IllegalStateException("SHA1PRNG and SHA-256 algorithms must be supported by all JVMs");
		}
	}
	
	public static byte[] getHash(String password, byte[] salt)
	{
		digest.reset();
		digest.update(salt);
		try
		{
			byte[] input = digest.digest(password.getBytes("UTF-8"));
			for(int i = 0; i < 1000; i++)
			{
				digest.reset();
				input = digest.digest(input);
			}
			return input;
		}
		catch(UnsupportedEncodingException e)
		{
			throw new IllegalStateException("UTF-8 encoding must be supported by all JVMs");
		}
	}
	
	public static byte[] generateSalt()
	{
		byte[] salt = new byte[20];
		rand.nextBytes(salt);
		return salt;
	}
	
	public static boolean authenticate(User user, String pwdAttempt)
	{
		byte[] given = getHash(pwdAttempt, user.getSalt());
		return Arrays.equals(user.getPassword(), given);
	}
}
