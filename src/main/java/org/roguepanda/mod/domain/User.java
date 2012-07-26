package org.roguepanda.mod.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.roguepanda.mod.search.IndexEntityListener;
import org.roguepanda.mod.search.Indexable;

@Entity
@EntityListeners({IndexEntityListener.class})
public class User implements Indexable
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	@NotNull
	private byte[] password;
	
	//TODO use bean validation
	@NotNull
	private byte[] salt;
	
	@OneToMany(cascade=ALL, mappedBy="author", orphanRemoval=true, fetch=EAGER)
	private List<Mod> mods;
	
	public Long getId()
	{
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
	public List<Mod> getMods() {
		return mods;
	}

	public void setMods(List<Mod> mods)
	{
		this.mods = mods;
		for(Mod m : mods)
		{
			m.setAuthor(this);
		}
	}
	
	public void addMod(Mod mod)
	{
		if(mods == null)
		{
			mods = new ArrayList<Mod>();
		}
		mods.add(mod);
		mod.setAuthor(this);
	}
	
	public void removeMod(Mod mod)
	{
		if(mods != null)
		{
			mods.remove(mod);
			mod.setAuthor(null);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Query getQuery()
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(new TermQuery(new Term("id", id.toString())), Occur.MUST);
		bq.add(new TermQuery(new Term("type", "user")), Occur.MUST);
		return bq;
	}

	public Document asDocument()
	{
		Document doc = new Document();
		//doc.add(new StoredField("id", id.toString()));
		//doc.add(new LongField("id", id, Field.Store.YES));
		doc.add(new StringField("id", id.toString(), Field.Store.YES));
		doc.add(new StringField("name", name, Field.Store.YES));
		doc.add(new StringField("type", "user", Field.Store.YES));
		return doc;
	}
}
