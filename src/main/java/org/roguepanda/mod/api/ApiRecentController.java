package org.roguepanda.mod.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.repository.ModRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/recent.json")
public class ApiRecentController
{
	@Autowired
	private ModRepository modRepository;
	
	public static class ModSummary
	{
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		String name;
		Long id;
	}
	
	/*
	 * Use ModSummary so we don't send a lot of potentially unwanted data (i.e. descriptions) - the client can get those if requested
	 */
	@Transactional(readOnly=true)
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public List<ModSummary> getRecent(@RequestParam(required=false, value="count") Integer count)
	{
		if(count == null || count.intValue() == 0)
		{
			count = 10;
		}
		
		List<Mod> mods = modRepository.findRecent(count);
		mods.removeAll(Collections.singletonList(null));
		List<ModSummary> results = new ArrayList<ModSummary>();
		for(Mod mod : mods)
		{
			ModSummary summary = new ModSummary();
			summary.id = mod.getId();
			summary.name = mod.getName();
			results.add(summary);
		}
		return results;
	}
}
