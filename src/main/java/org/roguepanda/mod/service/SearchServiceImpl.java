package org.roguepanda.mod.service;

import java.util.ArrayList;
import java.util.List;

import org.compass.core.Compass;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassHit;
import org.compass.core.CompassTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchServiceImpl implements SearchService
{
	private CompassTemplate template;
	
	@Autowired
	public void setCompass(Compass compass)
	{
		this.template = new CompassTemplate(compass);
	}
	
	@Override
	public SearchResult search(String query)
	{
		CompassDetachedHits hits = template.findWithDetach(query);
		List<SearchResultItem> items = new ArrayList<SearchResultItem>();
		for(CompassHit hit : hits.getHits())
			items.add(new CompassSearchResult(hit));
		if(hits.getSuggestedQuery().isSuggested())
			return new SearchResult(items, hits.getSuggestedQuery().toString()); //DefaultCompassQuery overrides toString()
		else
			return new SearchResult(items, null);
	}
	
	private static class CompassSearchResult implements SearchResultItem
	{
		private CompassHit hit;
		
		public CompassSearchResult(CompassHit hit)
		{
			this.hit = hit;
		}
		
		@Override
		public float getScore()
		{
			return hit.getScore();
		}

		@Override
		public Object getProperty(String name)
		{
			return hit.getResource().getObject(name);
		}

		@Override
		public String getPropertyString(String name)
		{
			return hit.getResource().getProperty(name).getStringValue();
		}

		@Override
		public Type getType()
		{
			String type = getPropertyString("type");
			if(type.equals("mod"))
				return Type.MOD;
			else if(type.equals("author"))
				return Type.AUTHOR;
			else
				throw new IllegalStateException("Type " + type + " is neither mod nor author");
		}

		@Override
		public Long getId()
		{
			Object obj = hit.getResource().getIdProperty();
			if(obj instanceof Long)
				return (Long) obj;
			else
				throw new IllegalStateException("Id " + obj + " is not a Long");
		}
		
	}
}
