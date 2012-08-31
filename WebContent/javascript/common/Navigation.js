/*
 * Este arquivo define o funcionamento do menu de navegacao. Tanto para back-end quanto front-end
 * ja que o menu de navegacao e no mesmo igual
 */

var currentPage = "none";

$(document).ready(function() {
	$("a.MenuButton, a.ActiveMenuButton").click(function() {
		// Workaround para poder setar quem e o item ativo no proprio html
		//if ($(this).hasClass("ActiveMenuButton")) return;
		
		// Desativa o item ativo atual
		$("a.ActiveMenuButton").addClass("MenuButton");
		$("a.ActiveMenuButton").removeClass("ActiveMenuButton");

		// Ativa o novo item de menu
		$(this).removeClass("MenuButton");
		$(this).addClass("ActiveMenuButton");
		//startLoading();
		$("div#mainArea").html("<img src=\"" + getLoadingSpinnerImg() + "\" />");
		var requestedPage = $(this).attr("href").substring(1);
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage);
			// Identifica, na div, qual e a pagina ativa no momento
			$("div#mainArea").attr("title", requestedPage);
			
			if (requestedPage == "OrderManager") {
				// Limpa o contador de novos pedidos do order manager
				$("#orderManagerLink").html("");
			}
		}
	});
	
	$("a.AjaxLink").live("click", function() {
		$("div#mainArea").html("<img src=\"" + getLoadingSpinnerImg() + "\" />");
		var requestedPage = $(this).attr("href").substring(1);
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage);
			// Identifica, na div, qual e a pagina ativa no momento
			$("div#mainArea").attr("title", requestedPage);
			
			if (requestedPage == "OrderManager") {
				// Limpa o contador de novos pedidos do order manager
				$("#orderManagerLink").html("");
			}
		}
	});
});