package org.roguepanda.mod.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableConstant;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.roguepanda.mod.util.CompassEntityListener;

@Entity
@Searchable
@EntityListeners({CompassEntityListener.class})
@SearchableConstant(name="type", values={"author"})
public class User
{
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true, nullable=false)
	private String name;
	
	@Column(nullable=false)
	private byte[] password;
	
	@Column(nullable=false)
	private byte[] salt;
	
	@OneToMany(cascade=ALL, mappedBy="author", orphanRemoval=true, fetch=EAGER)
	private List<Mod> mods;
	
	@SearchableId
	public Long getId()
	{
		return this.id;
	}

	@SearchableProperty(name="name")
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
}
