package org.roguepanda.mod.web;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.roguepanda.mod.auth.AuthUtils;
import org.roguepanda.mod.auth.RequiresLogin;
import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.domain.User;
import org.roguepanda.mod.repository.ModRepository;
import org.roguepanda.mod.repository.UserRepository;
import org.roguepanda.mod.web.command.EditModCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/mod")
public class ModController
{
	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public static class ModNotFoundException extends RuntimeException {}
	
	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public static class IllegalModEditException extends RuntimeException {} //use when someone tries to edit another person's mod
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ModRepository modRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping("/show/{id}")
	public String showMod(@PathVariable("id") Long id, ModelMap model)
	{
		Mod mod = modRepository.findOne(id);
		if(mod != null)
		{
			model.addAttribute("m", mod);
			return "mod/show";
		}
		else
		{
			throw new ModNotFoundException();
		}
	}
	
	@RequiresLogin
	@RequestMapping("/create")
	public String createMod(ModelMap model)
	{
		log.trace("Mod being created");
		model.addAttribute("pageName", "create");
		model.addAttribute("m", new Mod());
		
		return "mod/edit";
	}
	
	@RequiresLogin
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap model)
	{	
		log.trace("Editing mod with id " + id);
		Mod mod = modRepository.findOne(id);
		if(mod != null)
		{
			if(!mod.getAuthor().equals(AuthUtils.getUser()))
			{
				throw new IllegalModEditException();
			}
			model.addAttribute("pageName", "edit");
			model.addAttribute("m", mod);
			return "mod/edit";
		}
		else
		{
			throw new ModNotFoundException();
		}
	}
	
	@RequiresLogin
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(EditModCommand cmd, @RequestParam(required=false, value="id") Long id, ModelMap model)
	{
		System.err.println("ID: " + id);
		if(id != null && id != 0) //existing, test for 0 because Spring seems to set it to "" or 0 if it isn't actually there
		{
			System.err.println("EDITING MOD WITH ID " + id);
			Mod mod = modRepository.findOne(id);
			cmd.applyTo(mod);
			Set<ConstraintViolation<Mod>> violations = validator.validate(mod);
			if(violations.isEmpty()) //valid
			{
				modRepository.save(mod);
				return "redirect:/mod/show/" + mod.getId();
			}
			else
			{
				model.addAttribute("hasErrors", true);
				model.addAttribute("errors", violations);
				return "mod/edit";
			}
		}
		else
		{
			System.err.println("CREATING NEW MOD");
			Mod mod = new Mod();
			User user = AuthUtils.getUser();
			user.addMod(mod);
			cmd.applyTo(mod);
			Set<ConstraintViolation<Mod>> violations = validator.validate(mod);
			if(violations.isEmpty()) //valid
			{
				userRepository.saveAndFlush(user);
				return "redirect:/user/show/" + user.getId();
			}
			else
			{
				model.addAttribute("hasErrors", true);
				model.addAttribute("errors", violations);
				return "mod/edit";
			}
		}
	}
	
//	@ModelAttribute("user")
//	public User lookupUser(@RequestParam(value="uid", required=false) Long uid)
//	{
//		//retrieve from session or request attributes
//		if(AuthUtils.getUser() != null)
//		{
//			return AuthUtils.getUser();
//		}
//		return userRepository.findOne(uid);
//	}
}
