package org.roguepanda.mod.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.roguepanda.mod.domain.User;

//Same as VoiceOver
public class PasswordHashGenerator
{
	public static byte[] getHash(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(password.getBytes("UTF-8"));
		for(int i = 0; i < 1000; i++)
		{
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}
	
	public static byte[] generateSalt() throws NoSuchAlgorithmException
	{
		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[20];
		rand.nextBytes(salt);
		return salt;
	}
	
	public static boolean authenticate(User user, String pwdAttempt) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		byte[] given = getHash(pwdAttempt, user.getSalt());
		return Arrays.equals(user.getPassword(), given);
	}
}
