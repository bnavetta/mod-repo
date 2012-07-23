package org.roguepanda.mod.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.roguepanda.mod.auth.AccountService;
import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.UserRepository;
import org.roguepanda.mod.web.command.LoginCommand;
import org.roguepanda.mod.web.command.RegisterCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth/")
public class AuthController
{
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
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
	public String doLogin(@Valid @ModelAttribute LoginCommand cmd, BindingResult result,  ModelMap model, HttpSession session)
	{
		if(result.hasErrors())
		{
			model.addAttribute("hasErrors", true);
			model.addAttribute("errors", result.getAllErrors());
			return "auth/login";
		}
		else
		{
			User user = userRepository.findByName(cmd.getUsername());
			if(user != null)
			{
				if(accountService.authenticate(user, cmd.getPassword()))
				{
					session.setAttribute("user", user);
					return "redirect:/";
				}
				else
				{
					result.reject("password.incorrect", "Incorrect password");
					model.addAttribute("errors", result);
					model.addAttribute("hasErrors", true);
					return "auth/login";
				}
			}
			else
			{
				result.reject("user.not.found", "User not found");
				model.addAttribute("errors", result);
				model.addAttribute("hasErrors", true);
				return "auth/login";
			}
		}
	}
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String loginForm()
	{
		return "auth/login";
	}
}
