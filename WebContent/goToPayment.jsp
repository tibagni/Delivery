<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/jquery.maskMoney.js"></script>
<script type="text/javascript" src="javascript/common/Form.js"></script>

<script type="text/javascript">
<!--
$(document).ready(function(){
	$(".manualPayment").click(function() {
		$(".manualPaymentPanel").show();
	});
	$("#value").maskMoney({symbol:'', showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
	
	$("#manualPaymentForm").submit(function () {
		var orderValue = parseFloat('${ValueOfOrderD}');
		var enteredValue = parseFloat($("#value").val());
		if (enteredValue < orderValue) {
			alert("Você não pode pagar menos pelo pedido!");
			return false;
		}
		
		if (confirm("Troco para R$:" + $("#value").val() + " \nEste valor está correto?")) {
	            ajaxSubmit($(this));
		}
		return false;
	});
});
//-->
</script>

<h1>/Pagamento</h1>
<p style="text-align: center;">
<a href="${paymentURL }">Pagar utilizando Pag Seguro</a>
<br /><br />
<a href="#" class="manualPayment">Pagar no momento da entrega</a>
</p>
<div class="manualPaymentPanel" style="display: none;">
    <h3>Pagamento em dinheiro</h3>
    Se preferir, você pode combinar o valor para realizar o pagamento em dinheiro, no momento da entrega.
    <form id="manualPaymentForm" action="Order">
        <fieldset>
            <p>Valor do pedido - ${ValueOfOrder }</p>
            <div>
                <label for="value">Troco para quanto? R$</label>
                <input name="value" id="value" type="text" />
            </div>
	        <input type="hidden" name="cmd" value="ManualPayment" />
            <input type="hidden" name="paymentId" value="${PaymentId }" />
            <input type="hidden" name="orderId" value="${ OrderId}" />
	        <span class="ButtonInput"><span><input type="submit" value="Combinar!" /></span></span>
        </fieldset>
    </form>
</div>
