<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<html>
	<head>
		<meta name="page" content="index"/>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
		<title>Home - Minecraft Mod Repository</title>
	</head>
	<body>
		<h1 class="ui-widget-header">New Mods:</h1>
		<ul type="none">
		<c:forEach items="${recent }" var="m">
			<li class="ui-state-default ui-corner-all" style="max-width:500px;">
				<span class="ui-icon ui-icon-plusthick" style="display:inline-block;"><!-- This should be a block --></span>
				<app:link url="/mod/show/${m.id}"><c:out value="${m.name}"/> - <joda:format value="${m.created}" style="LS"/> </app:link>
			</li>
		</c:forEach>
		</ul>
	</body>
</html>