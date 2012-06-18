<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function() {
	$("a.addNew").click(function() {
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		var requestedPage = $(this).attr("href").substring(1);
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage + '&prodId=${product.id}');
		}
	});
	
	$("a.backToEditor").click(function() {
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		$("div#MainArea").load('PageLoader?page=MenuEditor');
	});
	
	$("a.editExisting").click(function() {
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		var linkParts = $(this).attr("href").split("-");
		var requestedPage = linkParts[1];
		var editingId = linkParts[2];
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage + '&prodId=${product.id}&editing=' + editingId);
		}
	});
});
</script>

<h1>${product.name}</h1>
<a class="editExisting" href="#-ProductEditor-${product.id}">Editar este produto</a>
<h3>Sabores:</h3>
<ul>
	<c:forEach var="flavour" items="${product.flavours}">
		<li><a class="editExisting" href="#-FlavourEditor-${flavour.id}">${flavour.name}</a></li>
	</c:forEach>	
</ul>
<a class="addNew" href="#FlavourEditor">[+] Adicionar outro sabor</a>
<h3>Opcionais:</h3>
<ul>
	<c:forEach var="optional" items="${product.optionals}">
		<li>${optional.name} 
			<a class="editExisting" href="#-OptionalEditor-${optional.id}">[Editar]</a> 
			<a class="removeExisting" href="#-OptionalRemover-${optional.id}"> [Remover]</a></li>
	</c:forEach>
</ul>
<a class="addNew" href="#OptionalEditor">[+] Adicionar outro opcional</a>
<p>
<a class="backToEditor" href="#MenuEditor">Voltar ao editor</a></p>