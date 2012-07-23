package org.roguepanda.mod.search;


public interface SearchResult
{
	public Long getId();
	public String getName();
	public Type getType();
	
	public boolean equals(Object obj);
	
	public int hashCode();
	
	public static enum Type
	{
		MOD, USER
	}
}