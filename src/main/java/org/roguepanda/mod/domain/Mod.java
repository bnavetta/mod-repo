package org.roguepanda.mod.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableConstant;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.joda.time.DateTime;
import org.roguepanda.mod.util.CompassEntityListener;

@Entity
@Searchable
@EntityListeners({CompassEntityListener.class})
@SearchableConstant(name="type", values={"mod"})
public class Mod
{
	@Id
	@GeneratedValue
	private Long id;
	
	//@Column(unique=true)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="AUTHOR_ID", nullable=false)
	private User author;
	
	@Version
	private Timestamp version;
	
	//A brief (or not) description of the mod
	//TODO render this with Markdown/BBCode or something
	private String description;
	
	//The Groovy installation script that is combined with the download file to form the .mod file
	private String installScript;
	
	//A URL that links to the mod's homepage/documentation/etc.
	private String home;
	
	//Name of mod download file in MongoDB GridFS
	private String fileKey;
	
	@SearchableId
	public Long getId()
	{
		return this.id;
	}
	
	public DateTime getLastModified()
	{
		return new DateTime(version.getTime());
	}

	@SearchableProperty(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author)
	{
		this.author = author;
	}

	@SearchableProperty(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstallScript() {
		return installScript;
	}

	public void setInstallScript(String installScript) {
		this.installScript = installScript;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	public URL getHome()
	{
		try
		{
			return new URL(home);
		}
		catch (MalformedURLException e)
		{
			return null;
		}
	}

	public void setHome(String home) {
		this.home = home;
	}
	
	public void setHome(URL home)
	{
		this.home = home.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mod other = (Mod) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
