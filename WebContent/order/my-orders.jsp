<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1>/Meus pedidos</h1>
<c:choose>
    <c:when test="${empty orders }">
       <p style="text-align: center;">Não há pedidos abertos</p>
    </c:when>
    <c:otherwise>
    <c:forEach var="order" items="${orders }">
    <div class="orderInfo">
    <h6>${order.timestampAsText }</h6>
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
        <p>Status atual:<b> ${order.statusText }</b>
        <c:if test="${order.waitingPaymentStatus }">
            <c:if test="${not order.payment.manualPayment }">
               - <a href="${order.payment.URL }" target="_blank">Pagar</a>
            </c:if>
        </c:if>
        </p>
    </div>
</c:forEach>
    </c:otherwise>
</c:choose>

<p><a href="#OrderHistory" class="AjaxLink">Histórico de pedidos</a></p>
