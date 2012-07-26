<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="app" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		
		<c:url value="/js/jquery-1.7.2.min.js" var="jqueryUrl"/>
		<c:url value="/js/jquery-ui-1.8.21.custom.min.js" var="jqueryUiJs"/>
		<c:url value="/css/main.css" var="mainCssUrl"/>
		<c:url value="/css/smoothness/jquery-ui-1.8.21.custom.css" var="jqueryUiCss"/>
		<c:url value="/js/modrepo.js" var="mainJsUrl"/>
		
		<link rel="stylesheet" type="text/css" href="${jqueryUiCss }"/>
		<link rel="stylesheet" type="text/css" href="${mainCssUrl }"/>
		
		<script type="text/javascript" src="${jqueryUrl }"></script>
		<script type="text/javascript" src="${jqueryUiJs }"></script>
		<script type="text/javascript" src="${mainJsUrl }"></script>
		
		<title><decorator:title default="Minecraft Mod Repository"/></title>
		<decorator:head/>
	</head>
	<body>
		<div id="_left"></div>
		<div id="_right"></div>
		<div id="_top"></div>
		<div id="_bottom"></div>
		
		<!--[if lte IE 6]>
			<style>#_top, #_bottom, #_left, #_right { display: none; }</style>
		<![endif]-->
		<div class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="margin-left: 20px; margin-top: 30px;">
			<decorator:usePage id="contentPage"/>
			<%
				pageContext.setAttribute("pg", contentPage.getProperty("meta.page"));
			%>
			<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<li class="ui-state-default ui-corner-top ui-state-active${pg eq 'index' ? ' ui-tabs-selected' : ''}"><app:link url="/">Home</app:link></li>
				<li class="ui-state-default ui-corner-top ui-state-active${pg eq 'search' ? ' ui-tabs-selected' : ''}"><app:link url="/search">Search</app:link></li>
				<app:loggedIn>
				<li class="ui-state-default ui-corner-top ui-state-active${pg eq 'create' ? ' ui-tabs-selected' : '' }"><app:link url="/mod/create">Create Mod</app:link></li>
				</app:loggedIn>
				<c:choose>
				<c:when test="${not empty sessionScope.user }">
				<li class="ui-state-default ui-corner-top ui-state-active"><app:link url="/auth/logout">Logout</app:link></li>
				</c:when>
				<c:otherwise>
				<li class="ui-state-default ui-corner-top ui-state-active${pg eq 'login' ? ' ui-tabs-selected' : '' }"><app:link url="/auth/login">Login</app:link></li>
				<li class="ui-state-default ui-corner-top ui-state-active${pg eq 'register' ? ' ui-tabs-selected' : '' }"><app:link url="/auth/register">Create Account</app:link></li>
				</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<div id="content" class="ui-widget">
			<decorator:body/>
		</div>
	</body>
</html>