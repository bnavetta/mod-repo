package org.roguepanda.mod.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.roguepanda.mod.auth.AccountService;
import org.roguepanda.mod.auth.RequiresLogin;
import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.UserRepository;
import org.roguepanda.mod.web.command.RegisterCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth/")
public class AuthController
{
	@Autowired
	private AccountService accountService;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value="register", method=RequestMethod.POST)
	public String register(@ModelAttribute RegisterCommand register, HttpSession session, ModelMap model)
	{
		//TODO use bean validation for nulls and check here for password match (put result in BindingResult)
		//TODO check that the user does not already exist
		List<String> errors = new ArrayList<String>();
		boolean hasErrors = false;
		if(register.getUsername() == null || register.getUsername().equals(""))
		{
			errors.add("A username must be specified");
			hasErrors = true;
		}
		if(register.getPassword() == null || register.getPassword().equals(""))
		{
			errors.add("A password must be specified");
			hasErrors = true;
		}
		if(register.getConfirm() == null || register.getConfirm().equals(""))
		{
			errors.add("The password must be confirmed");
			hasErrors = true;
		}
		if(register.getPassword() != null && register.getConfirm() != null && !register.getPassword().equals(register.getConfirm()))
		{
			errors.add("Passwords don't match");
			hasErrors = true;
		}

		if(hasErrors)
		{
			model.addAttribute("hasErrors", hasErrors);
			model.addAttribute("errors", errors);
			return "auth/register";
		}

		User user = accountService.createUser(register.getUsername(), register.getPassword());
		session.setAttribute("user", user);
		return "redirect:/";
	}

	@RequestMapping(value="register", method=RequestMethod.GET)
	public String registerForm(){ return "auth/register"; }

	@RequestMapping(value="login", method=RequestMethod.POST)
	public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password, ModelMap model, HttpSession session, @RequestParam(required=false, value="destination") String destination)
	{
		User user = userRepository.findByName(username);
		if(user != null)
		{
			if(accountService.authenticate(user, password))
			{
				session.setAttribute("user", user);
				if(destination == null || destination.equals(""))
				{
					return "redirect:/";
				}
				else
				{
					return "redirect:" + destination;
				}
			}
			else
			{
				model.addAttribute("hasErrors", true);
				model.addAttribute("errors", Collections.singletonList("Incorrect password"));
				return "auth/login";
			}
		}
		else
		{
			model.addAttribute("hasErrors", true);
			model.addAttribute("errors", Collections.singletonList("User not found"));
			return "auth/login";
		}
		
	}

	@RequestMapping(value="login", method=RequestMethod.GET)
	public String loginForm()
	{
		return "auth/login";
	}

	@RequestMapping("logout")
	public String logout(@RequestParam(required=false, value="destination", defaultValue="/") String destination, HttpSession session)
	{
		session.setAttribute("user", null);
		return "redirect:" + destination;
	}

	@RequiresLogin
	@RequestMapping("test")
	@ResponseBody
	public String testSecure()
	{
		return "Hello, World!";
	}
}
