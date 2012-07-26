<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="page" content="login"/>
		<title>Login</title>
	</head>
	<body>
		<h1 class="ui-widget-header">Login</h1>
		<div class="ui-widget-content">
			<c:if test="${hasErrors }">
				<app:errors errors="${errors }"></app:errors>
			</c:if>
		</div>
		<form method="post" action="login" id="loginForm">
			<div class="formComponent">
				<label for="username">Username:</label><br/>
				<input type="text" name="username" id="username"/>
			</div>
			<div class="formComponent">
				<label for="password">Password:</label><br />
				<input type="password" name="password" id="password"/>
			</div>
			<div class="formControls">
				<input type="submit" value="Login"/>
			</div>
		</form>
		<p>
			Don't have an account? <app:link url="/auth/register">Register now!</app:link>
		</p>
	</body>
</html>