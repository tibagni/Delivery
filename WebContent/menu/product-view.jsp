<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
<!--
// Limita a quantidade de sabores que podem ser escolhidos
var maxFlav = ${product.flavoursPerOrder};
var maxOpt = ${product.optionalsPerOrder};
$(document).ready(function() {
	$('.flavourSelection').click(function() {
		if ($(".flavourSelection").filter(":checked").size() > maxFlav) {
			return false;
		}
	});
	
	//Limita a quantidade de opcionais que podem ser escolhidos
	$('.optionalSelection').click(function() {
		if ($(".optionalSelection").filter(":checked").size() > maxOpt) {
			return false;
		}
	});

	//Recarrega a pagina pra mostrar apenas os sabores que tem o tamanhos especificado
	$("#sizeSelect").change(function() {
		  var sizeId = this.value;
		  $("div#MainArea").load('PageLoader?page=ProductView&prodId=${product.id}&sizeId=' + sizeId);
	});
});
//-->
</script>

<c:set var="flavourInputType" scope="page" value="radio"/>
<c:set var="optionalInputType" scope="page" value="radio"/>
<h1>${product.name}</h1>
Tamanho: 
<select id="sizeSelect">
	<c:forEach var="size" items="${product.sizesAvailable}">
		<c:choose>
			<c:when test="${size.id == product.currentSizeId}">
  				<option value="${size.id}" selected="selected">${size.name}</option>
			</c:when>
			<c:otherwise>
  				<option value="${size.id}">${size.name}</option>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</select>
<form>
	<div class="content">
		<img src="${product.picturePath}" />
		<h3 style="border:none;">Sabores:</h3>
		<c:if test="${product.flavoursPerOrder > 1}">
			<c:set var="flavourInputType" scope="page" value="checkbox"/>
			<h4>Você pode escolher até ${product.flavoursPerOrder} sabores para este produto</h4>
		</c:if>
		<div class="elements">
			<ul>
				<c:forEach var="flavour" items="${product.flavours}">
					<li>
						 <input class="flavourSelection" value="-flavour-${flavour.id}" type="${flavourInputType}" name="flavour"/>${flavour.name}
					</li>
				</c:forEach>	
			</ul>
		</div>
		<h3 style="border:none;">Opcionais:</h3>
		<c:if test="${product.optionalsPerOrder > 1}">
			<c:set var="optionalInputType" scope="page" value="checkbox"/>
			<h4>Você pode escolher até ${product.optionalsPerOrder} opcionais para este produto</h4>
		</c:if>
		<div class="elements">
			<ul>
				<c:forEach var="optional" items="${product.optionals}">
					<li>
					<input class="optionalSelection" value="-optional-${optional.id}" type="${optionalInputType}" name="flavour"/>${optional.name}
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</form>