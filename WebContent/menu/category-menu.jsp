<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p class="flip" id="catTitle-${categoria.categoryId}">${categoria.name}</p>
<div class="panel">				
	<c:forEach var="subcat" items="${categoria.subCategories}">
		<c:set var="categoria" value="${subcat}" scope="request"/>
		<%// Inclui esta pagina recursivamente para cada categoria %>
		<jsp:include page="category-menu.jsp"/>
	</c:forEach>
				
	<ul class="products">
	<c:forEach var="prod" items="${categoria.products}">
		<li>
			<a class="viewProd" href="#Prod-${prod.id}"> ${prod.name }</a>
			<div>${prod.description }</div>		
		</li>
	</c:forEach>
	</ul>
</div>