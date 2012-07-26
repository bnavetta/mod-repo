<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="file"required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/css/${file }" var="cssUrl"/>
<link rel="stylesheet" type="text/css" href="${cssUrl }"/>