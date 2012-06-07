$(document).ready(function() {
	$("a.MenuButton, a.ActiveMenuButton").click(function() {
		// Workaround para poder setar quem é o item ativo no próprio html
		if ($(this).hasClass("ActiveMenuButton")) return;
		
		// Desativa o item ativo atual
		$("a.ActiveMenuButton").addClass("MenuButton");
		$("a.ActiveMenuButton").removeClass("ActiveMenuButton");

		// Ativa o novo item de menu
		$(this).removeClass("MenuButton");
		$(this).addClass("ActiveMenuButton");
		//startLoading();
		$("div#mainArea").html("<img src=\"images/loading-circle.gif\" />");
		var requestedPage = $(this).attr("href").substring(1);
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage);
		}
	});
	
	$("a.debugPageLoader").click(function() {
		$("div#mainArea").html("<img src=\"images/loading-circle.gif\" />");
		var requestedPage = $(this).attr("title");
		$("div#MainArea").load('PageLoader?debug=true&page=' + requestedPage);
	});
});