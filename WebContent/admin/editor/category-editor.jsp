<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p class="flip" id="catTitle-${categoria.categoryId}">
	${categoria.name} <a class="dialog_editCat" href="#" id="edit-${categoria.name}-${categoria.categoryId}">[Editar]</a>
</p>
<div class="panel">
	<!-- Dialogo para adicionar sub-categoria -->
	<a href="#" class="dialog_subCat" id="link-${categoria.name}-${categoria.categoryId}">
				[+] Sub-categoria em <i>${categoria.name}</i></a>
	<br />
	<!-- Abre página para adicionar novo produto -->
	<a href="#" class="page_Prod" id="linkProdCat-${categoria.categoryId}">
				[+] Produto</a>
				
	<c:forEach var="subcat" items="${categoria.subCategories}">
		<c:set var="categoria" value="${subcat}" scope="request"/>
		<%// Inclui esta pagina recursivamente para cada categoria %>
		<jsp:include page="category-editor.jsp"/>
	</c:forEach>
				
	<ul class="products">
	<c:forEach var="prod" items="${categoria.products}">
		<li>
			<a class="page_edit_Prod" href="#Prod-${prod.id}"> ${prod.name }</a>
			<div>${prod.description }</div>		
		</li>
	</c:forEach>
	</ul>
</div>