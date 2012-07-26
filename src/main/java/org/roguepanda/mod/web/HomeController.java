package org.roguepanda.mod.web;

import java.util.List;

import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.repository.ModRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	@Autowired
	private ModRepository modRepository;
	
	@RequestMapping("/")
	public String homePage(ModelMap model)
	{
		List<Mod> recent = modRepository.findRecent(10);
		model.addAttribute("recent", recent);
		return "index";
	}
}
