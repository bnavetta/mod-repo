<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<meta name="page" content="show-mod"/>
		<title>Mod - ${m.name }</title>
	</head>
	<body>
		<h1 class="ui-widget-header">${m.name }</h1>
		<div>
			<strong>Homepage:<strong>&nbsp;<a href="${m.home }">${m.home }</a><br/>
			<strong>Date Created:</strong>&nbsp;<joda:format value="${m.created }" style="L-"/><br/>
			<strong>Last Modified:</strong>&nbsp;<joda:format value="${m.lastModified }" style="LM"/><br/>
			<strong>Author:</strong>&nbsp;<app:link url="/user/show/${m.author.id }"><c:out value="${m.author.name }"/></app:link>
		</div>
		<div class="ui-widget-content" style="white-space: pre-wrap;">
			<spring:escapeBody htmlEscape="true">
				${m.description }
			</spring:escapeBody>
		</div>
		<div>
			<spring:url var="downloadUrl" value="/file/download/${m.id }"/>
			<a id="downloadButton" href="${downloadUrl }">Download!</a>
			<script type="text/javascript">$('#downloadButton').button({icons: {primary:'ui-icon-circle-arrow-s'}});</script>
		</div>
		<c:if test='${m.author eq sessionScope.user }'>
			<spring:url var="editUrl" value="/mod/edit/${m.id}"/>
			<a href="${editUrl }" id="editButton">Edit</a>
			<script type="text/javascript">$("#editButton").button({icons: {primary:"ui-icon-pencil"}});</script>
		</c:if>
	</body>
</html>