<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="url" description="The href of the link" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="${url }" var="_newUrl"/>
<a href="${_newUrl }"><jsp:doBody/></a>
