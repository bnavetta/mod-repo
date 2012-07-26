package org.roguepanda.mod.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

public class ZipUtils
{
	private ZipUtils() {}
	
	/**
	 * Recursively extract a zip file (as an {@link InputStream}) to the given directory. 
	 * @param is a stream containing zipped data
	 * @param destDir the root directory to which data will be extracted
	 * @throws IOException
	 */
	public static void unzip(InputStream is, File destDir) throws IOException
	{
		if(destDir.exists() && destDir.isFile())
		{
			throw new IllegalArgumentException("destDir is not a drectory");
		}
		else
		{
			if(!destDir.exists() && !destDir.mkdirs())
			{
				throw new IOException("Error creating destination directory");
			}
		}
		ZipInputStream in = null;
		try
		{
			in = new ZipInputStream(is);
			ZipEntry entry = null;
			while((entry = in.getNextEntry()) != null)
			{
				if(entry.isDirectory())
				{
					boolean success = new File(destDir, entry.getName()).mkdirs();
					if(!success)
					{
						throw new IOException("Error creating directory " + entry.getName());
					}
				}
				else
				{
					File file = new File(destDir, entry.getName());
					if(!file.getParentFile().exists() && !file.getParentFile().mkdirs())
					{
						throw new IOException("Error creating parent files for " + entry.getName());
					}
					if(!file.createNewFile())
					{
						throw new IOException("Error creating file " + entry.getName());
					}
					write(file, in);
				}
			}
		}
		finally
		{
			if(in != null)
			{
				in.close();
			}
		}
	}
	
	/**
	 * Recursively compress the contents of {@code file} in zip format to {@code dest}
	 * @param file the directory (or file) to zip
	 * @param dest the stream to compress to
	 * @throws IOException
	 */
	public static void zip(File file, OutputStream dest) throws IOException
	{
		ZipOutputStream zout = new ZipOutputStream(dest);
		zip(file.toURI(), file, zout);
		zout.close();
	}
	
	public static void zip(URI base, File file, ZipOutputStream dest) throws IOException
	{
		if(!file.exists())
		{
			throw new IOException(file.getPath() + " does not exist");
		}
		
		if(file.isDirectory())
		{
			if(file.list().length == 0) //empty directory
			{
				ZipEntry entry = new ZipEntry(base.relativize(new File(file, ".").toURI()).getPath());
				entry.setSize(0);
				entry.setTime(file.lastModified());
				dest.putNextEntry(entry);
				dest.closeEntry();
			}
			else
			{
				for(File f : file.listFiles())
				{
					zip(base, f, dest);
				}
			}
		}
		else
		{
			ZipEntry entry = new ZipEntry(base.relativize(file.toURI()).getPath());
			entry.setSize(file.length());
			entry.setTime(file.lastModified());
			dest.putNextEntry(entry);
			write(file, dest);
			dest.closeEntry();
		}
	}

	private static void write(File file, OutputStream out) throws IOException
	{
		byte[] buf = new byte[8 * 1024]; //8 KB
		int count;
		InputStream in = null;
		try
		{
			in = new FileInputStream(file);
			while((count = in.read(buf)) != -1)
			{
				out.write(buf, 0 , count);
			}
			out.flush();
		}
		finally
		{
			if(in != null)
				in.close();
		}
	}

	/**
	 * Write the contents of a stream to a file. This method does not close the {@link InputStream}.
	 * @param file the file to write to
	 * @param in the stream from which data will be read
	 * @throws IOException
	 */
	public static void write(File file, InputStream in) throws IOException 
	{
		byte[] buf = new byte[8 * 1024]; //8 KB
		int count;
		OutputStream out = null;
		try
		{
			out = new FileOutputStream(file);
			while((count = in.read(buf)) != -1)
			{
				out.write(buf, 0 , count);
			}
			out.flush();
		}
		finally
		{
			if(out != null)
			{
				out.close();
			}
		}
	}
	
	public static void copy(File src, File dest) throws IOException
	{
		if(!src.exists())
		{
			throw new IllegalArgumentException("src does not exist");
		}
		else if(src.isDirectory())
		{
			FileUtils.copyDirectory(src, dest);
		}
		else
		{
			if(dest.exists() && dest.isDirectory())
			{
				FileUtils.copyFileToDirectory(src, dest);
			}
			else if((dest.exists() && dest.isFile()) || !dest.exists())
			{
				FileUtils.copyFile(src, dest);
			}
		}
	}
}
