<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/jquery.js"></script>
<c:choose>
	<c:when test="${not empty img}">
		<script type="text/javascript">
			// Imagem carregada, vamos mostra-la
			$("div#previewFoto", top.document).html("<img src=\"${img}\" />");
			$("#fotoHidden", top.document).attr("value", "${img}");
		</script>
	</c:when>
	<c:otherwise> 
		<script type="text/javascript">
			// Falha no processamento da imagem, avisar o usuário
			$("div#upload-btn", top.document).html("Não foi possível carregar a imagem");
			$("#fotoHidden", top.document).attr("value", "${img}");
		</script>
	</c:otherwise>
</c:choose> 