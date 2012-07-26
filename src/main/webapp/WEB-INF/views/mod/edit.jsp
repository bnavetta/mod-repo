<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<meta name="page" content="${pageName}"/>
		<title>Edit Mod</title>
		
		<app:js file="jquery.validate.min.js"/>
		<app:css file="codemirror.css"/>
		<app:js file="codemirror.js"/>
		<app:js file="groovy.js"/>
		
		<script type="text/javascript">
			$(function() {
				CodeMirror.fromTextArea(document.getElementById("installScript"), {
					//value: {m.installScript},
					mode: 'groovy',
					lineNumbers: true
				});
				
				$("#uploadButton").button();
				$("#editAccordion").accordion();
				$("#tabRoot").tabs();
				<c:if test="${empty m.id }">
				$("#tabRoot").tabs("disable", 1);
				</c:if>
			});
		</script>
	</head>
	<body>
		<h1 class="ui-widget-header">Edit Mod</h1>
		<div>
			<c:if test="${hasErrors }">
				<app:errors errors="${errors }"/>
			</c:if>
		</div>
		<div class="ui-widget-content">
			<div id="tabRoot">
				<ul>
					<li><a href="#tab-1">Edit Mod</a></li>
					<li><a href="#tab-2">Upload</a></li>
				</ul>
				<div id="tab-1">
					<spring:url value="/mod/save" var="action"/>	
					<form:form action="${action}" commandName="m" method="post">
						<div id="editAccordion">
							<h3><a href="#">Information</a></h3>
							<div>
								<div>
									<label for="name">Name: </label><br/>
									<form:input path="name" id="name" cssClass="ui-corner-all"/>
								</div>
								<div>
									<label for="description">Description:</label><br/>
									<form:textarea path="description" id="description" cssClass="ui-corner-all"/>
								</div>
								<div>
									<label for="home">Home Page:</label><br/>
									<form:input path="home" id="home" cssClass="ui-corner-all" type="url"/>
								</div>
							</div>
							
							<h3><a href="#">Install Script</a></h3>
							<div>
								<form:textarea path="installScript" id="installScript"/>
							</div>
						</div>
						<c:if test="${not empty m.id}">
						<form:hidden path="id"/>
						</c:if>
						<div class="formControls">
							<input type="submit" value="Save"/>
						</div>
					</form:form>
				</div>
				<div id="tab-2">
					<spring:url var="uploadUrl" value="/file/upload"/>
					<form action="${uploadUrl }" method="post" enctype="multipart/form-data">
						<input type="hidden" name="id" value="${m.id }"/>
						<input type="file" name="file" accept="application/zip"/>
						<input type="submit" id="uploadButton" value="Upload"/>
					</form>
				</div>
			</div>
		</div>
	</body>
</html>