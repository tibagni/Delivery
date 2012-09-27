<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
<!--
$(document).ready(function() {
	$(".changeStatus").click(function() {
		var orderParts = $(this).attr("href").split("-");
		var orderId = orderParts[1];
		var newStatus = orderParts[3];
		
		if (newStatus == 5) {
			$("#deliveryGuyListElement").show();
			$("#moveOrderElement").hide();
			return;
		}

		$.post( "Order", { cmd : 'ChangeOrderStatus', orderId: orderId, newStatus: newStatus }, function() {
			$.modal.close();
		});
	});
	   $(".setDeliveryGuy").click(function() {
	        var deliveryGuyParts = $(this).attr("href").split("-");
	        var orderId = ${order.id};
	        var newStatus = ${order.nextAllowedStatus};
	        var deliveryId = deliveryGuyParts[1];
	        
	        $.post( "Order", { cmd : 'ChangeOrderStatus', orderId: orderId, newStatus: newStatus, deliveryGuyId: deliveryId }, function() {
	            $.modal.close();
	        });
	    });
});
//-->
</script>
<c:if test="${order.payment.paymentStatus == 3 }">
    <p style="color: red; text-decoration: blink;">O pagamento ser� realizado no momento da entrega - 
     Valor combinado com o cliente R$: ${order.payment.manualPaymentValue }</p>
</c:if>
<div class="orderInfo">
<p>
<b>Endere�o de entrega: </b>${order.cachedAddress }
</p><p>
<b>Cliente:</b> ${order.cachedUserName }
</p>
</div>
<ul class="orderDetail">
<c:forEach var="item" items="${order.items}">
        <li><h4>${item.description } - R$: ${item.price } (${item.cachedSizeName})</h4>
            <div>
                <ul>
                    <c:forEach items="${item.flavours}" var="flavour">
                        <li>${flavour.value}</li>
                    </c:forEach>
                </ul>
                <c:if test="${not empty item.optionals }">
                <h3>Opcionais:</h3>
                    <ul>
                        <c:forEach items="${item.optionals}" var="optional">
                            <li>${optional.value}</li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>
        </li>
    </c:forEach>
</ul>
<div style="border-top: solid 1px #000; padding:3px">
    Total - R$: ${order.price}
</div>
<div class="orderInfo">
<p>Status atual:<b> ${order.statusText }</b></p>
<c:if test="${order.canChangeStatusManually }">
<p id="moveOrderElement"><a href="#-${order.id }-newStatus-${order.nextAllowedStatus }" class="changeStatus">Mover para: <b>${order.nextStatusText }</b></a></p>
	<div id="deliveryGuyListElement" style="display: none;">
	<p>Quem dever� realizar esta entrega?</p>
	<p><c:if test="${not empty deliveryGuyList }">
	    <ul>
	    <c:forEach var="deliveryGuy" items="${deliveryGuyList }">
	        <li><a href="#-${deliveryGuy.code }" class="setDeliveryGuy">${deliveryGuy.name }</a></li>
	    </c:forEach>
	    </ul>
	</c:if> </p>
	</div>
</c:if>
</div>