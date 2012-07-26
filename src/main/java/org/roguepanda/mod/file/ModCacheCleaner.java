package org.roguepanda.mod.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ModCacheCleaner
{
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Async //in case you want to manually clean the cache
	public void cleanCache() throws IOException
	{
		File dir = new File(ModFileService.MOD_DIR_NAME);
		if(dir.exists())
			FileUtils.cleanDirectory(dir);
	}
	
	@Scheduled(fixedDelay = 5 * 60000) //every 5 minutes measured from the previous completion
	public void repeatCleanCache()
	{
		log.trace("Clearing mod cache");
		try
		{
			cleanCache();
		}
		catch (IOException e) 
		{
			log.error("Error clearing mod cache", e);
		}
	}
}
