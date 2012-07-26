<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="name"required="true" %>
<%@ attribute name="editor" required="true" type="boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- http://code.google.com/p/pagedown/wiki/PageDown -->

<c:if test="${editor}">
	<div class="wmd-panel">
		<div id="wmd-button-bar-${name }"></div>
		<textarea class="wmd-input" id="wmd-input-${name }">
			<jsp:doBody/>
		</textarea>
	</div>
	<div id="wmd-preview-${name }" class="wmd-panem wmd-preview"></div>
	<script type="text/javascript">
		(function() {
			var converter = Markdown.getSanitizingConverter();
			var editor = new Markdown.Editor(converter, "-${name}");
			editor.run();
		})();
	</script>
</c:if>
<c:if test="${!editor}">
	<jsp:doBody var="markdown"/>
	<div id="markdown-${name }"></div>
	<script type="text/javascript">
		(function (){
			var converter = Markdown.getSanitizingConverter();
			$("markdown-${name}").innerHTML = converter.makeHtml("${markdown}");
		})();
	</script>
</c:if>