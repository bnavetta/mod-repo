package org.roguepanda.mod.auth;

import org.roguepanda.mod.domain.User;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class AuthUtils
{
	//TODO refactor code to use these methods
	
	public static void setUser(User user)
	{
		RequestContextHolder.getRequestAttributes().setAttribute("user", user, RequestAttributes.SCOPE_SESSION);
	}
	
	public static User getUser()
	{
		return (User) RequestContextHolder.getRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
	}
}
