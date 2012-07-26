package org.roguepanda.mod.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.roguepanda.mod.domain.Mod;
import org.roguepanda.mod.repository.ModRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Component
public class ModFileService
{
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String ENCODING = "UTF-8"; //encoding to use for install.groovy
	
	public static final String FS_NAME = "mod-fs";
	
	public static final String MOD_DIR_NAME = "mods";
	
	private File MOD_DIR = new File(MOD_DIR_NAME);
	
	@PostConstruct
	public void createModDir() throws IOException
	{
		if(MOD_DIR.exists())
		{
			if(MOD_DIR.isFile())
			{
				MOD_DIR.delete();
			}
			else
			{
				FileUtils.deleteDirectory(MOD_DIR);
			}
		}
		assert MOD_DIR.mkdirs();
		assert MOD_DIR.exists();
		assert MOD_DIR.isDirectory();
		
		log.info("Mod Directory: " + MOD_DIR.getCanonicalPath());
		System.err.println("********************** MOD DIRECTORY: " + MOD_DIR.getCanonicalPath());
	}
	
	private GridFS gridfs;
	
	@Autowired
	public void setMongoDbFactory(MongoDbFactory factory)
	{
		this.gridfs = new GridFS(factory.getDb(), FS_NAME);
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
	
	public void writeFile(Mod mod, OutputStream out) throws IOException
	{
		GridFSDBFile file = gridfs.findOne(mod.getFileKey());
		file.writeTo(out);
	}
	
	public synchronized String generateKey() //synchronized to make sure the UUID is unique
	{
		return UUID.randomUUID().toString();
	}
	
	/*
	 * .mod file caching:
	 * 
	 * Use Spring to schedule a thread that empties MOD_DIR (w/ Commons FileUtils) every x minutes.
	 * When a file is requested, check if it is in the cache (file exists).
	 * If not, create it using ZipUtils.
	 * Serve the file (use streams w/ NIO transferTo)
	 */
	
	@SuppressWarnings("unused")
	public void downloadMod(Mod mod, OutputStream out) throws IOException
	{
		if(mod.getFileKey() == null)
		{
			throw new IllegalArgumentException("No content for mod " + mod);
		}
		
		if(!isInCache(mod))
		{
			buildModFile(mod);
		}
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(new File(MOD_DIR, mod.getFileKey()));
			FileChannel src = in.getChannel();
			byte[] buffer = new byte[8 * 1024];
			ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
			for(int length = 0; (length = src.read(byteBuffer)) != -1;)
			{
				out.write(buffer);
				byteBuffer.clear();
			}
		}
		finally
		{
			if(in != null)
				in.close();
		}	
	}
	
	/*
	 * .mod file format:
	 * 
	 * 		Zip file containing:
	 * 			- install.groovy
	 * 			- content/ (folder containing extract of uploaded zip)
	 */
	
	private void buildModFile(Mod mod) throws IOException
	{
		InputStream modStream = null;
		OutputStream zipStream = null;
		try
		{
			assert MOD_DIR.exists();
			assert MOD_DIR.isDirectory();
			File extractDir = createTempDir("mod-extract");
			modStream = gridfs.findOne(mod.getFileKey()).getInputStream();
			ZipUtils.unzip(modStream, new File(extractDir, "content"));
			File scriptFile = new File(extractDir, "install.groovy");
			scriptFile.createNewFile();
			FileUtils.write(scriptFile, mod.getInstallScript(), ENCODING);
			assert MOD_DIR.exists() && MOD_DIR.isDirectory();
			File zip = new File(MOD_DIR, mod.getFileKey());
			zip.getParentFile().mkdirs();
			zip.createNewFile();
			zipStream = new FileOutputStream(zip);
			ZipUtils.zip(extractDir, zipStream);
			FileUtils.deleteDirectory(extractDir);
		}
		finally
		{
			IOUtils.closeQuietly(modStream);
			IOUtils.closeQuietly(zipStream); //should already be closed by ZipUtils.zip()
		}
	}
	
	private File createTempDir(String prefix) throws IOException
	{
		File file = File.createTempFile(prefix, "");
		file.delete();
		file.mkdirs();
		return file;
	}

	private boolean isInCache(Mod mod)
	{
		return new File(MOD_DIR, mod.getFileKey()).exists();
	}
}
