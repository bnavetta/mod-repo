<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<meta name="page" content="show-user"/>
		<title>User - ${u.name }</title>
	</head>
	<body>
		<h1 class="ui-widget-header">${u.name }</h1>
		<h2>Mods:</h2>
		<div id="mods">
			<c:forEach items="${u.mods }" var="m"><!-- mod is a keyword -->
				<a href="#" class="a-header" style="display:block;"><c:out value="${m.name }"/></a>
				<div>
					<app:link url="/mod/show/${m.id }">Page</app:link>
					<p style="border: 1px solid grey; white-space: pre-wrap;">
					<spring:escapeBody htmlEscape="true">
						${m.description }	
					</spring:escapeBody>
					</p>
				</div>
			</c:forEach>
		</div>
		<script type="text/javascript">
			$("#mods").accordion({header: '.a-header'});
		</script>
	</body>
</html>