package org.roguepanda.mod.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.joda.time.DateTime;
import org.roguepanda.mod.file.ModDeleteListener;
import org.roguepanda.mod.search.IndexEntityListener;
import org.roguepanda.mod.search.Indexable;

@Entity
@EntityListeners({IndexEntityListener.class, ModDeleteListener.class})
public class Mod implements Indexable
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	
	public Long getId()
	{
		return this.id;
	}
	
	public DateTime getLastModified()
	{
		return new DateTime(version.getTime());
	}

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

	public Term[] getIdTerms()
	{
		return new Term[]{new Term("id", id.toString()), new Term("type", "mod")};
	}

	public Document asDocument()
	{
		Document doc = new Document();
		doc.add(new StoredField("mod-id", id.toString()));
		doc.add(new TextField("name", name, Field.Store.YES));
		doc.add(new TextField("description", description, Field.Store.NO));
		doc.add(new StringField("type", "mod", Field.Store.YES));
		return doc;
	}
}
