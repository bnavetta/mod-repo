<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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
	
		<div id="content" class="ui-widget">
			<decorator:body/>
		</div>
	</body>
</html>