<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/common/Util.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(".orderBox").click(function() {
	    var orderId = $(this).attr("id").split("-")[1];
	    $("#orderDetail").load("PageLoader?page=OrderDetail&orderId=" + orderId, function() {
	        $("#orderDetailDialog").modal({overlayClose:true, overlayCss: {backgroundColor:"#000", 'height': 'auto !important'}});
	    });
	    return false;
	});
});
</script>

<h1>/Histórico de pedidos</h1>

<c:choose>
	<c:when test="${empty orders }">
	   <p style="text-align: center;">Não há pedidos no seu histórico</p>
	</c:when>
	<c:otherwise>
		<c:forEach var="order" items="${orders}">
	        <div id="order-${order.id}" class="orderBox">
	            <p><b>${order.statusText }</b> - Realizado em <b>${order.timestampAsText }</b></p>
	        </div>
		</c:forEach>
	</c:otherwise>
</c:choose>

    
    
<div class="dialog" id="orderDetailDialog" style="width: 500px">
    <!-- Detalhes do pedido -->
    <span class="BlockHeader"><span>:: Detalhes do pedido</span></span>
    <div id="orderDetail">
    <p></p>
    </div>
</div>