package org.roguepanda.mod.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.roguepanda.mod.search.SearchResult;
import org.roguepanda.mod.search.SearchResult.Type;
import org.roguepanda.mod.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/search.json")
public class ApiSearchController
{
	@Autowired
	private SearchService searchService;
	
	public static class ModSearchResult
	{
		public String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long id;
	}
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Bad Query")
	public static class BadQueryException extends RuntimeException
	{
		
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public List<ModSearchResult> modSearch(@RequestParam("q") String q)
	{
		try
		{
			String query = searchService.escape(q);
			List<SearchResult> rawSearch = searchService.search(query);
			
			List<ModSearchResult> results = new ArrayList<ModSearchResult>();
			for(SearchResult raw : rawSearch)
			{
				if(raw.getType().equals(Type.MOD))
				{
					ModSearchResult res = new ModSearchResult();
					res.id = raw.getId();
					res.name = raw.getName();
					results.add(res);
				}
			}
			return results;
		}
		catch(QueryNodeException e)
		{
			throw new BadQueryException();
		}
	}
}
