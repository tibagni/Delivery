<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function() {
	$("a.whatsNext").click(function() {
		$("div#mainArea").html("<img src=\"images/loading-circle.gif\" />");
		var requestedPage = $(this).attr("href").substring(1);
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage + '&prodId=${produto.id}');
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
	<li><a class="whatsNext" href="#NewFlavour">Adicionar novo sabor ao produto (${produto.name})</a></li>
	<li><a class="whatsNext" href="#">Adicionar opcionais ao produto (${produto.name})</a></li>
	<li><a class="whatsNext" href="#Menu">Voltar ao editor de cardapio</a></li>	
</ul>