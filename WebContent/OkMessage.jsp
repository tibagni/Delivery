<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Ok!</h1>

<c:if test="${not empty okMsg}">
    <p>${okMsg }</p>
</c:if>

<a href="#Home" class="AjaxLink">Voltar ao inicio.</a>