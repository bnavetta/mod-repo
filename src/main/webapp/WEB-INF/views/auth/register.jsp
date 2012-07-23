<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page contentType="text/html; charset=ISO-8859-1"
		pageEncoding="ISO-8859-1" session="false" />
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="true" />
	<html xmlns="http://www.w3.org/1999/xhtml"
		xmlns:c="http://java.sun.com/jsp/jstl/core"
		xmlns:spring="http://www.springframework.org/tags"
		xmlns:app="urn:jsptagdir:/WEB-INF/tags/">
		<head>
			<title>Create Account</title>
			<spring:url value="/css/pwdwidget.css" var="pwdCss" />
			<link rel="stylesheet" type="text/css" href="${pwdCss }" />
			<spring:url value="/js/pwdwidget.js" var="pwdJs" />
			<script type="text/javascript" src="${pwdJs }"> <!-- Prevent self-closing --> </script>
		</head>
		<body>
			<h1 class="ui-widget-header">Create Account</h1>
			<div class="ui-widget-content" style="padding: 10px;">
				<c:if test="${hasErrors }">
					<app:errors errors="${errors }"></app:errors>
				</c:if>
			<form method="post" id="registerForm" action="register">
				<div class="formComponent">
					<label for="username">Username:</label><br />
					<input type="text" name="username" id="username" />
				</div>
				<div class="formComponent">
					<label for="password">Password:</label><br />
					<div class="pwdwidgetdiv" id="passwordContainer"></div>
					<script type="text/javascript">
						var pwdWidget = new PasswordWidget('passwordContainer', 'password');
						pwdWidget.MakePWDWidget();
					</script>
					<noscript>
						<input type="password" id="password" name="password" />
					</noscript>
				</div>
				<div class="formComponent">
					<label for="confirm">Confirm Password:</label><br />
					<input type="password" name="confirm" id="confirm" />
				</div>
				<div class="formControls">
					<input type="submit" value="Create Account" /><br />
					<input type="reset" value="Reset Form" />
				</div>
			</form>
		</div>
	</body>
</html>

</jsp:root>