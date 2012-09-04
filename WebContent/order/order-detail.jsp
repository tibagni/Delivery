<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="orderInfo">
<p>
<b>Endereço de entrega: </b>${order.cachedAddress }
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
</div>