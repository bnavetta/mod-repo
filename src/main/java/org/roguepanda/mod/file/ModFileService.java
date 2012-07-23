package org.roguepanda.mod.file;

import java.io.InputStream;
import java.util.UUID;

import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.repository.ModRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

@Component
public class ModFileService
{
private GridFS gridfs;
	
	@Autowired
	public void setMongoDbFactory(MongoDbFactory factory)
	{
		this.gridfs = new GridFS(factory.getDb(), "mod-fs");
	}
	
	public void createFile(String name, InputStream in)
	{
		GridFSInputFile file = gridfs.createFile(in, name);
		file.save();
	}
	
	public void deleteFile(String name)
	{
		gridfs.remove(name);
	}
	
	@Autowired
	private ModRepository modRepository;
	
	public void updateContent(Mod mod, InputStream in)
	{
		String oldKey = mod.getFileKey();
		if(oldKey != null)
			deleteFile(oldKey);
		
		String newKey = generateKey();
		mod.setFileKey(newKey);
		modRepository.save(mod);
		createFile(newKey, in);
	}
	
	public String generateKey()
	{
		return UUID.randomUUID().toString();
	}	
}
