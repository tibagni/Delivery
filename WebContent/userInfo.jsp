<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
<!--
$(document).ready(function(){
	$("#userAreaTitle").html(":: Dados do usu�rio");
});
//-->
</script>

<h4>Bem vindo, ${UserSession.name }</h4>
<ul class="userInfoList">
	<li>${UserSession.email }</li>
	<li>${UserSession.tel } <a href="#" class="AjaxLink">[Alterar]</a></li>
	<li><a href="#" class="AjaxLink">[Alterar senha]</a></li>
</ul>

<a href="Login?action=logout">Sair</a>