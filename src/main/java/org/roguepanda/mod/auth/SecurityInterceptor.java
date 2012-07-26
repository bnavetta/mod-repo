package org.roguepanda.mod.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecurityInterceptor extends HandlerInterceptorAdapter
{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		if(handler instanceof HandlerMethod)
		{
			HandlerMethod method = (HandlerMethod) handler;
			if(method.getMethodAnnotation(RequiresLogin.class) != null)
			{
				if(getUser() != null)
				{
					return true;
				}
				else
				{
					response.sendRedirect(request.getContextPath() + "/auth/login?destination=" + request.getRequestURI());
					return false;
				}
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	private Object getUser()
	{
		return RequestContextHolder.getRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
	}
}
