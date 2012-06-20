<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/front-end/MenuView.js"></script>

<h1>/Cardápio</h1>

<div class="LineHeader">
	<a href="#show" id="showHide">Exibir/Econder tudo</a>
</div>

<br />

<c:forEach var="cat" items="${MenuCategories}">
	<c:set var="categoria" value="${cat}" scope="request"/>
	<jsp:include page="menu/category-menu.jsp"/>
</c:forEach>