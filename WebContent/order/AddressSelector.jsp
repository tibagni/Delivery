<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/front-end/ordering.js"></script>

<h1>/Endereço de entrega</h1>
<p>Selecione o endereço de onde deseja receber o pedido!</p>
<p>
	<c:forEach var="addr" items="${AddressList}">
		<div id="address-${addr.id}" class="addressBox">
		    <p>${addr.street }, ${addr.number } - ${addr.neighborhood }</p>
		    <p>${addr.zipCode } - ${addr.city } - ${addr.UF }</p>
		</div>
	</c:forEach>
</p>
<a href="#">[+] Adicionar outro endereço</a>