package org.roguepanda.mod.file;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class FileUtilsTest
{	
	@Test
	public void testZipUnzip() throws IOException
	{
		File dest = new File("test-files.zip");
		dest.createNewFile();
		FileOutputStream out = new FileOutputStream(dest);
		ZipUtils.zip(new File("test-files"), out);
		
		assertTrue(dest.exists());
		
		FileInputStream fis = new FileInputStream(dest);
		File destDir = new File("test-destination");
		ZipUtils.unzip(fis, destDir);
		
		assertTrue(new File(destDir, "foo.txt").exists());
		assertTrue(new File(destDir, "important").exists());
		assertTrue(new File(destDir, "important/data.txt").exists());
		assertTrue(new File(destDir, "myFolder").exists());
		
		new File(destDir, "foo.txt").delete();
		new File(destDir, "myFolder").delete();
		new File(destDir, "important/data.txt").delete();
		new File(destDir, "important").delete();
		destDir.delete();
		dest.delete();
	}
	
	@Test
	public void testCopy() throws IOException
	{
		//file to file
		File src1 = new File("test-files/foo.txt");
		File dest1 = new File("bar.txt");
		ZipUtils.copy(src1, dest1);
		assertTrue(dest1.exists());
		dest1.delete();
		
		//file to directory
		File src2 = new File("test-files/foo.txt");
		File dest2 = new File(".");
		ZipUtils.copy(src2, dest2);
		assertTrue(new File("foo.txt").exists());
		new File("foo.txt").delete();
		
		//directory to directory
		File src3 = new File("test-files/important");
		File dest3 = new File("dest");
		ZipUtils.copy(src3, dest3);
		assertTrue(dest3.exists());
		assertTrue(new File(dest3, "data.txt").exists());
		
		FileUtils.deleteDirectory(dest3);
	}

}
