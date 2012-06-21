<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function(){
	var content = $("#shopCar").html();
	$("#userArea").html(content);
	$("#userAreaTitle").html(":: Carrinho de compras");
});

$(document).ready(function(){	
	$(".orderItem").easyTooltip({
		useElement: "detail"				   
	});
	
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		$("div#MainArea").load('PageLoader?page=Menu');		
});
</script>

<div id="shopCar">
	<ul id="shopList">
		<c:forEach var="item" items="${Order.items}">
			<li class="orderItem">${item.description } - ${item.cachedSizeName} - R$: ${item.price }
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
	</ul>
	<span class="ButtonInput"><span><input type="button" value="Cancelar" /></span></span>
	<span class="ButtonInput"><span><input type="button" value="Finalizar" /></span></span>
</div>