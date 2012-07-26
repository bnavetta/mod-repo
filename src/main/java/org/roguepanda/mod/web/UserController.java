package org.roguepanda.mod.web;

import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/user")
public class UserController
{
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class UserNotFoundException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;	
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value="/show/{id}", method=RequestMethod.GET)
	public String showUser(@PathVariable("id") Long id, ModelMap model)
	{
		User user = userRepository.findOne(id);
		if(user != null)
		{
			model.addAttribute("u", user);
			return "user/show";
		}
		else
		{
			throw new UserNotFoundException();
		}
	}
}
