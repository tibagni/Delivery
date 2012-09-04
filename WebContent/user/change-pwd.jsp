<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/common/Form.js"></script>
    
<h1>/Alterar senha</h1>

<c:if test="${not empty errorMsg }">
    <p style="color: red;">${errorMsg }</p>
</c:if>

<form class="ajaxForm" action="Account">
	<table>
		<tr>
			<td><label for="current_pwd">Senha atual</label></td>
			<td><input type="password" style="width: 120px" id="current_pwd"
				name="current" /></td>
		</tr>
		<tr>
			<td><label for="new_pwd">Nova senha</label></td>
			<td><input type="password" style="width: 120px" id="new_pwd"
				name="new" /></td>
		</tr>
	</table>
        <input type="hidden" name="cmd" value="ChangePassword" />
	<span class="ButtonInput"><span><input type="submit"
			value="Alterar" /></span></span>
</form>