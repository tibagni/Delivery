<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<h1>/Login</h1>

<c:if test="${not empty loginMsg}">
    <p>${loginMsg }</p>
    <c:remove var="loginMsg"/>
</c:if>
<div style="text-align: center;">
	<form method="post" action="Login">
		<table style="margin-left:auto; margin-right: auto;">
			<tr>
				<td><label for="login_user">Usuário</label></td>
				<td><input type="text" style="width: 120px" id="login_user"
					name="user" /></td>
			</tr>
			<tr>
				<td><label for="login_pwd">Senha</label></td>
				<td><input type="password" style="width: 120px" id="login_pwd"
					name="password" /></td>
			</tr>
		</table>
		<span class="ButtonInput"><span><input type="submit"
				value="Login" /></span></span>
	</form>
</div>