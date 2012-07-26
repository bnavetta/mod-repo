package org.roguepanda.mod.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
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
	
	@NotNull
	private String name;
	
	@ManyToOne
	@JoinColumn(name="AUTHOR_ID", nullable=false)
	private User author;
	
	@Version
	private Timestamp version;
	
	private Date created;
	
	//A brief (or not) description of the mod
	//TODO render this with Markdown/BBCode or something
	private String description;
	
	//The Groovy installation script that is combined with the download file to form the .mod file
	@NotNull
	@Lob
	private String installScript;
	
	//A URL that links to the mod's homepage/documentation/etc.
	@NotNull
	@org.hibernate.validator.constraints.URL
	private String home;
	
	//Name of mod download file in MongoDB GridFS
	//Can't be @NotNull because mod could be created and then have the file uploaded
	private String fileKey;
	
	public Long getId()
	{
		return this.id;
	}
	
	public DateTime getLastModified()
	{
		return new DateTime(version.getTime());
	}
	
	public DateTime getCreated()
	{
		return new DateTime(created);
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
		if(home != null)
		{
			this.home = home.toString();
		}
		else
		{
			this.home = null;
		}
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
	
	@PrePersist
	protected void initDateCreated()
	{
		if(created == null)
		{
			created = new Date();
		}
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	public Query getQuery()
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(new TermQuery(new Term("id", id.toString())), Occur.MUST);
		bq.add(new TermQuery(new Term("type", "mod")), Occur.MUST);
		return bq;
	}

	public Document asDocument()
	{
		Document doc = new Document();
		//doc.add(new StoredField("id", id.toString()));
		//doc.add(new LongField("id", id, Field.Store.YES));
		doc.add(new StringField("id", id.toString(), Field.Store.YES));
		doc.add(new TextField("name", name, Field.Store.YES));
		doc.add(new TextField("description", description, Field.Store.NO));
		doc.add(new StringField("type", "mod", Field.Store.YES));
		return doc;
	}
}
