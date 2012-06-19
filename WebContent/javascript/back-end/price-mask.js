$(document).ready(function(){
		$(".price").focus(function() {
			$(this).maskMoney({symbol:'', showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
		});
	});