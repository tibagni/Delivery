<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/easyTooltip.js"></script>
<script type="text/javascript" src="javascript/front-end/ordering.js"></script>


<ul id="shopList">
<c:choose>
<c:when test="${not empty SessionOrder.items}">
	<c:forEach var="item" items="${SessionOrder.items}">
		<li class="orderItem">${item.description } - ${item.cachedSizeName} - R$: ${item.price } 
			<a class="removeItem" href="#-" id="#RemoveOrderItem_${item.temporaryId}" style="float: right;"> <img src="images/cross.gif" /></a>
			<div class="detail">
				<h3>Sabores:</h3>
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
</c:when>
<c:otherwise> 
<li>Nenhum item no pedido!</li>
</c:otherwise> 
</c:choose>
</ul>

<c:if test="${not empty SessionOrder.items}">
<span class="ButtonInput"><span><input class="removeItem" id="#RemoveOrderItem_-1" type="button" value="Cancelar" /></span></span>
<span class="ButtonInput"><span><input id="finalize" type="button" value="Finalizar" /></span></span>
</c:if>