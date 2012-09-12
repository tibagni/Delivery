<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ambiente de testes P@gSeguro</title>
<link rel="stylesheet" href="../css/style.css" />
</head>
<body>
<h1>Pedido - ${paymentRequest.reference }</h1>
<table border="1">
	<tr>
		<td>Sender Name</td>
		<td>${paymentRequest.sender.name }</td>
	</tr>
	<tr>
        <td>Sender Email</td>
        <td>${paymentRequest.sender.email }</td>
	</tr>
    <tr>
        <td>Sender Phone (Area Code)</td>
        <td>${paymentRequest.sender.phone.areaCode }</td>
    </tr>
    <tr>
        <td>Sender Phone (Number)</td>
        <td>${paymentRequest.sender.phone.number }</td>
    </tr>
	<tr>
		<td>Currency</td>
		<td>${paymentRequest.currency }</td>
	</tr>
	<tr>
        <td>Redirect URL</td>
        <td>${paymentRequest.redirectURL }</td>
    </tr>
    <c:forEach var="item" items="${paymentRequest.items}">
    <tr>
        <td>Item ID</td>
        <td>${item.id }</td>
    </tr>
    <tr>
        <td>Item Description</td>
        <td>${item.description }</td>
    </tr>
    <tr>
        <td>Item Quantity</td>
        <td>${item.quantity }</td>
    </tr>
    <tr>
        <td>Item Amount</td>
        <td>${item.amount }</td>
    </tr>
    </c:forEach>
</table>
<p>Na table acima estao apenas alguns dados da requisicao de pagamento. Clique no link abaixo para vizualizar o xml completo</p>
<a href="${xmlFile }">Ver xml da requisição de pagamento</a>
<p><a href="/Delivery/PagSeguroTest/AutomaticReply?code=${code }">Enviar notificação de pagamento concluido!</a></p>
<p><a href="${paymentRequest.redirectURL }">Testar URL de retorno</a></p>
</body>
</html>