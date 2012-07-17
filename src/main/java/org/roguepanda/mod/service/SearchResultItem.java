package org.roguepanda.mod.service;

public interface SearchResultItem
{
	public float getScore();
	
	public Object getProperty(String name);
	
	public String getPropertyString(String name);
	
	public Type getType();
	
	public Long getId();
	
	public static enum Type
	{
		MOD, AUTHOR
	}
}
