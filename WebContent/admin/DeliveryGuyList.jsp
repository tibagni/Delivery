<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="../javascript/common/Form.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	// Dialog Link - Novo entregador
	$(".dialog_del").click(function() {
	    $("#dialogDeliveryGuy").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
	    // Antes de mais nada, È melhor esconder qualquer mensagem de erro que possa estar vsivel
	    $(".error").hide();
	    return false;
	});
	
	$("#insertForm").submit(function () {
		var submit = true;
		var name = $("#name").val();
		var pwd = $("#pwd").val();
		var errorMsg = "";
		
		if (name == null || name == '') {
			submit = false;
			errorMsg = errorMsg.concat("Preencha o nome do entregador<br />");
		}
		if (pwd == null || pwd == '') {
			submit = false;
			errorMsg = errorMsg.concat("Preencha a senha do entregador<br />");
		}
		
		if (submit) {
            ajaxSubmit($(this));
        } else {
        	$(".error").html(errorMsg);
        	$(".error").show();
        }
		
		return false;
	});
});
</script>

<h1>/Entregadores</h1>

<br />
<a href="#" class="dialog_del">[+] Entregador</a>
<br /><br />
<ul class="dGuyList">
<c:forEach var="deliveryGuy" items="${DeliveryGuyList}">
    <li>${deliveryGuy.code } - ${deliveryGuy.name }</li>
</c:forEach>
</ul>
<br />
<div class="dialog" id="dialogDeliveryGuy">
    <span class="BlockHeader"><span>:: Novo entregador</span></span>
    <div class="BlockContentBorder">
        <div class="error"></div>
        <form action="DeliveryGuyManager" id="insertForm">
            <label for="name">Nome do entregador</label> 
            <input type="text" name="name" id="name" />
            <br />
            <label for="pwd">Senha de acesso</label> 
            <input type="password" name="pwd" id="pwd" />
            <br />
            <input type="hidden" name="cmd" value="InsertDeliveryGuy" />
            <span class="ButtonInput"><span><input type="submit" value="Ok" /></span></span>
            <span class="ButtonInput simplemodal-close"><span><input type="button" value="Cancelar" /></span></span>
        </form>
    </div>
</div>