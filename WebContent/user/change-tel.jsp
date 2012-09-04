<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/common/Form.js"></script>
<script type="text/javascript" src="javascript/masked-Input.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    $("#new_tel").mask("(99) 9999-9999");
});
</script>
    
<h1>/Alterar telefone para contato</h1>

<c:if test="${not empty errorMsg }">
    <p style="color: red;">${errorMsg }</p>
</c:if>

<form class="ajaxForm" action="Account">
	<table>
		<tr>
			<td><label for="new_tel">Novo Telefone:</label></td>
			<td><input type="text" style="width: 120px" id="new_tel"
				name="tel" /></td>
		</tr>
	</table>
        <input type="hidden" name="cmd" value="ChangeTel" />
	<span class="ButtonInput"><span><input type="submit"
			value="Alterar" /></span></span>
</form>