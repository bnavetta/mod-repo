package org.roguepanda.mod.api;

import java.net.URL;
import java.util.Date;

import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.repository.ModRepository;
import org.roguepanda.mod.web.ModController.ModNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/details/{id}.json")
public class ApiDetailsController
{
	@Autowired
	private ModRepository modRepository;
	
	public static class ModDetails
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
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Date getCreated() {
			return created;
		}
		public void setCreated(Date created) {
			this.created = created;
		}
		public Date getLastModified() {
			return lastModified;
		}
		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}
		public URL getHome() {
			return home;
		}
		public void setHome(URL home) {
			this.home = home;
		}
		public String getAuthorName() {
			return authorName;
		}
		public void setAuthorName(String authorName) {
			this.authorName = authorName;
		}
		public Long getAuthorId() {
			return authorId;
		}
		public void setAuthorId(Long authorId) {
			this.authorId = authorId;
		}
		String name;
		Long id;
		String description;
		
		Date created;
		Date lastModified;
		
		URL home;
		
		String authorName;
		Long authorId;
	}
	
	@Transactional
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public ModDetails getDetails(@PathVariable("id") Long id)
	{
		Mod mod = modRepository.findOne(id);
		if(mod != null)
		{
			ModDetails details = new ModDetails();
			details.authorId = mod.getAuthor().getId();
			details.authorName = mod.getAuthor().getName();
			details.created = mod.getCreated().toDate(); //I don't know how Jackson would handle Joda Time
			details.description = mod.getDescription();
			details.id = mod.getId();
			details.lastModified = mod.getLastModified().toDate();
			details.name = mod.getName();
			details.home = mod.getHome();
			return details;
		}
		else
		{
			throw new ModNotFoundException();
		}
	}
}
