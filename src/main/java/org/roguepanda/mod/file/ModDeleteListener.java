package org.roguepanda.mod.file;

import javax.persistence.PreRemove;

import org.roguepanda.mod.domain.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.MongoDbFactory;

import com.mongodb.gridfs.GridFS;

@Configurable
public class ModDeleteListener
{
	private GridFS gridfs;
	
	@Autowired
	public void setMongoDbFactory(MongoDbFactory factory)
	{
		gridfs = new GridFS(factory.getDb(), ModFileService.FS_NAME);
	}
	
	@PreRemove
	public void deleteFile(Mod mod)
	{
		gridfs.remove(mod.getFileKey());
	}
}
