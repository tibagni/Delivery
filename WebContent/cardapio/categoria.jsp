<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p class="flip" id="catTitle-${categoria.categoryId}">
	${categoria.name}
</p>
<div class="panel">
	<!-- Dialogo para adicionar sub-categoria -->
	<a href="#" class="dialog_subCat" id="link-${categoria.name}-${categoria.categoryId}">
				[+] Sub-categoria em <i>${categoria.name}</i></a>
	<br />
	<!-- Abre página para adicionar novo produto -->
	<a href="#" class="page_Prod" id="linkProdCat-${categoria.categoryId}">
				[+] Produto</a>
				
	<!-- Dialogo para adicionar sub-categoria -->
	<c:forEach var="subcat" items="${categoria.subCategories}">
		<c:set var="categoria" value="${subcat}" scope="request"/>
		<%// Inclui esta pagina recursivamente para cada categoria %>
		<jsp:include page="categoria.jsp"/>
	</c:forEach>
</div>