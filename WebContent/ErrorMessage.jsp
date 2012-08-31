<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Ooops, aconteceu algum erro!</h1>

<c:if test="${not empty errorMsg}">
    <p>${errorMsg }</p>
</c:if>

<a href="#Home" class="AjaxLink">Voltar ao inicio.</a>