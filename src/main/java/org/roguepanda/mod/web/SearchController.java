package org.roguepanda.mod.web;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.roguepanda.mod.search.SearchResult;
import org.roguepanda.mod.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/search")
public class SearchController
{
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String searchForm()
	{
		return "search";
	}
	
	@RequestMapping(method=RequestMethod.GET, produces="application/json", params="q")
	@ResponseBody
	public List<SearchResult> doSearch(@RequestParam("q") String query)
	{
		try
		{
			List<SearchResult> results = searchService.search(query);
			return results;
		}
		catch(QueryNodeException e)
		{
			String newQuery = searchService.escape(query);
			try
			{
				List<SearchResult> results = searchService.search(newQuery);
				return results;
			}
			catch(QueryNodeException ex)
			{
				return null;
			}
		}
	}
}
