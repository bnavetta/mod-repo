package org.roguepanda.mod.file;

import javax.persistence.PreRemove;

import org.roguepanda.mod.domain.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class ModDeleteListener
{
	@Autowired
	private ModFileService fs;
	
	@PreRemove
	public void deleteFile(Mod mod)
	{
		assert fs != null;
		fs.deleteFile(mod.getFileKey());
	}
}
