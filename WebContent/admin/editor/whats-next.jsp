<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function() {
	$("a.whatsNext").click(function() {
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		var requestedPage = $(this).attr("href").substring(1);
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage + '&prodId=${prodId}');
		}
	});
});
</script>

<h1>/Novo produto/Ok!</h1>
<blockquote>
	<p>${finalMsg }</p>
	<c:if test="${not empty finalPicture }">
		<img src="${finalPicture }" />
	</c:if>
</blockquote>
<h3>O que deseja fazer agora?</h3>
<ul>
	<li><a class="whatsNext" href="#FlavourEditor">Adicionar novo sabor ao produto</a></li>
	<li><a class="whatsNext" href="#OptionalEditor">Adicionar opcionais ao produto</a></li>
	<li><a class="whatsNext" href="#Menu">Voltar ao editor de cardapio</a></li>	
</ul>