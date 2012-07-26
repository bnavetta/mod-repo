<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="file"required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/js/${file }" var="jsUrl"/>
<script type="text/javascript" src="${jsUrl }"></script>