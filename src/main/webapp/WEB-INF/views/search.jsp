<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta name="page" content="search"/>
		<spring:url var="searchUrl" value="/search" />
		<spring:url var="modBase" value="/mod/show/"/>
		<spring:url var="userBase" value="/user/show/"/>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<title>Search</title>
		<script type="text/javascript">
			var runningRequest = false;
			var request;
		
			$(function(){
				$("#searchBox").keyup(function(e) {
					e.preventDefault();
					var term = $("#searchBox").val();
					if(term && term.length > 0) //make sure there is actually a query
					{
						$("#searchResults").empty();
						
						//Abort opened requests to speed it up
				        if(runningRequest){
				            request.abort();
				        }
						
						runningRequest = true;
						request = $.getJSON("${searchUrl}?q=" + term, function(data){
							var results = $("#searchResults");
							
							if(data == null)
							{
								results.text("Search error");
								return;
							}
							
							if(data.length == 0)
							{
								results.text("No results found");
								return;
							}
							
							var getLink = function(result) {
								if(result.type == "MOD")
								{
									return "${modBase}" + result.id; 
								}
								else if(result.type == "USER")
								{
									return "${userBase}" + result.id;
								}
								else
								{
									return "#";								
								}
							};
							
							$.each(data, function(index, result){
								results.append(
									$('<li/>', {
										'class': 'searchResult',
										html: $('<a/>', {
											href: getLink(result),
											text: escape(result.name),
										})
									})
								);
							});
							runningRequest = false;
						});
					}
				});		
			});
		</script>
	</head>
	<body>
		<h1 class="ui-widget-header">Search</h1>
		<div>
			<input type="text" id="searchBox" placeholder="Search" class="ui-corner-all"/>
		</div>
		<hr />
		<div>
			<ul id="searchResults">
				
			</ul>
		</div>
	</body>
</html>