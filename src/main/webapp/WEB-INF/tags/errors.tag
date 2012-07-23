<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="errors" required="true" type="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="ui-state-error">
	<ul type="none">
	<c:forEach items="${errors }" var="error">
		<li><span class="ui-icon ui-icon-alert" style="display:inline-block"><!-- alert icon --></span>&nbsp;${error }</li>
	</c:forEach>
	</ul>
</div>