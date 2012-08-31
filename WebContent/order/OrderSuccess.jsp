<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function(){
	var content = $("#shopCar").html();
	$("#shopCarArea").html(content);
	$("#shopCarArea").show(200);
	$("div#MainArea").html("<img src=\"../images/loading-circle.gif\" />");
	$("div#MainArea").load('PageLoader?page=Menu');	
});
</script>

<div id="shopCar">
	<jsp:include page="ShopCar.jsp"/>
</div>