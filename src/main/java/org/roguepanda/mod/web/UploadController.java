package org.roguepanda.mod.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.file.ModFileService;
import org.roguepanda.mod.repository.ModRepository;
import org.roguepanda.mod.web.ModController.ModNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class UploadController
{
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ModRepository modRepository;
	
	@Autowired
	private ModFileService fileService;
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String uploadModFile(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file) throws IOException
	{
		Mod mod = modRepository.findOne(id);
		if(mod == null)
		{
			throw new ModNotFoundException();
		}
		else
		{
			fileService.updateContent(mod, file.getInputStream());
			return "redirect:/mod/show/" + mod.getId();
		}
	}
	
	@RequestMapping("/raw") //raw because it's not in the packaged format that the client installs with
	public void downloadRaw(@RequestParam("id") Long id, HttpServletResponse response)
	{
		try
		{
			Mod mod = modRepository.findOne(id);
			if(mod == null)
			{
				response.sendError(HttpStatus.NOT_FOUND.value(), "Requested mod does not exist");
				return;
			}
			if(mod.getFileKey() == null)
			{
				response.sendError(HttpStatus.NOT_FOUND.value(), "No file has been uploaded for this mod");
				return;
			}
			fileService.writeFile(mod, response.getOutputStream());
		}
		catch(IOException e)
		{
			log.warn("Error serving raw mod file", e);
			throw new RuntimeException("IOException serving raw mod file", e);
		}
	}
	
	@RequestMapping("/download/{id}")
	public void download(@PathVariable("id") Long id, HttpServletResponse response)
	{
		try
		{
			Mod mod = modRepository.findOne(id);
			if(mod == null)
			{
				response.sendError(HttpStatus.NOT_FOUND.value(), "Requested mod does not exist");
				return;
			}
			if(mod.getFileKey() == null)
			{
				response.sendError(HttpStatus.NOT_FOUND.value(), "No file has been uploaded for this mod");
				return;
			}
			fileService.downloadMod(mod, response.getOutputStream());
		}
		catch(IOException e)
		{
			log.warn("Error serving mod file", e);
			throw new RuntimeException("IOException serving mod file", e);
		}
	}
}
