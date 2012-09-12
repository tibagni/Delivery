<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="../javascript/common/Util.js"></script>
<script type="text/javascript">

// Long Polling

var lpOnComplete = function(response) {
    eval(response);
    var notify = !isCurrentMainPage("OrderManager");
    if (shouldLoad) {
    	if (notify) {
    		notifyNewOrder();
    	} else {
    	    $("div#MainArea").load('PageLoader?page=OrderManager');
        }
    }
    lpStart();
};
 
var lpStart = function() {
    $.post('LongPollingHandler', lpOnComplete);
};

$(document).ready(function() {
	lpStart();
	
	$("#statusSelection :checkbox").click(function () {
	    var status = $(this).attr("id").split("-")[1];
	    if ($(this).attr("checked")) {
	        $(".orderStatus-" + status).show(300);
	    } else {
	        $(".orderStatus-" + status).hide(300);
	    }
	});
	
	$(".orderBox").click(function() {
		var orderId = $(this).attr("id").split("-")[1];
		$("#orderDetail").load("PageLoader?page=OrderDetail&orderId=" + orderId, function() {
	        $("#orderDetailDialog").modal({overlayClose:true, overlayCss: {backgroundColor:"#000", 'height': 'auto !important'}});
		});
        return false;
	});
});
</script>

<h1>/Gerenciador de pedidos</h1>
<h3>Gerencie todos os pedidos em tempo real!</h3>

<table class="orderDashboard">
<tr>
<td>Aguardando pagamento</td>
<td>Na fila para preparo</td>
<td>Sendo preparado</td>
<td>Pronto para ser entregue</td>
<td>Saiu para entrega</td>
</tr>
<tr>
<td width="20%">
<p>
    <c:forEach var="order" items="${waitingPayment}">
        <c:if test="${order.status == 1 }">
            <div id="order-${order.id}" class="orderBox orderStatus-${order.status }">
                <p>#<b><i>${order.id }</i></b> - ${order.cachedUserName }</p>
                <p>Pedido realizado em <b>${order.timestampAsText }</b> </p>
                <c:if test="${order.payment.paymentStatus == 3 }">
                    <p style="color: red">O pagamento será realizado no momento da entrega</p>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</p>
</td>
<td width="20%">
<p>
    <c:forEach var="order" items="${readyToPrepare}">
        <c:if test="${order.status == 2 }">
            <div id="order-${order.id}" class="orderBox orderStatus-${order.status }">
                <p>#<b><i>${order.id }</i></b> - ${order.cachedUserName }</p>
                <p>Pedido realizado em <b>${order.timestampAsText }</b> </p>
                <c:if test="${order.payment.paymentStatus == 3 }">
                    <p style="color: red">O pagamento será realizado no momento da entrega</p>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</p>
</td>
<td width="20%">
<p>
    <c:forEach var="order" items="${preparing}">
        <c:if test="${order.status == 3 }">
            <div id="order-${order.id}" class="orderBox orderStatus-${order.status }">
                <p>#<b><i>${order.id }</i></b> - ${order.cachedUserName }</p>
                <p>Pedido realizado em <b>${order.timestampAsText }</b> </p>
                <c:if test="${order.payment.paymentStatus == 3 }">
                    <p style="color: red">O pagamento será realizado no momento da entrega</p>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</p>
</td>
<td width="20%">
<p>
    <c:forEach var="order" items="${readyToDeliver}">
        <c:if test="${order.status == 4 }">
            <div id="order-${order.id}" class="orderBox orderStatus-${order.status }">
                <p>#<b><i>${order.id }</i></b> - ${order.cachedUserName }</p>
                <p>Pedido realizado em <b>${order.timestampAsText }</b> </p>
                <c:if test="${order.payment.paymentStatus == 3 }">
                    <p style="color: red">O pagamento será realizado no momento da entrega</p>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</p>
</td>
<td width="20%">
<p>
    <c:forEach var="order" items="${delivering}">
	    <c:if test="${order.status == 5 }">
	        <div id="order-${order.id}" class="orderBox orderStatus-${order.status }">
	            <p>#<b><i>${order.id }</i></b> - ${order.cachedUserName }</p>
	            <p>Pedido realizado em <b>${order.timestampAsText }</b> </p>
                <c:if test="${order.payment.paymentStatus == 3 }">
                    <p style="color: red">O pagamento será realizado no momento da entrega</p>
                </c:if>
	        </div>
        </c:if>
    </c:forEach>
</p>
</td>
</tr>
</table>

<div class="dialog" id="orderDetailDialog" style="width: 500px">
    <!-- Detalhes do pedido -->
    <span class="BlockHeader"><span>:: Detalhes do pedido</span></span>
    <div id="orderDetail">
    <p></p>
    </div>
</div>