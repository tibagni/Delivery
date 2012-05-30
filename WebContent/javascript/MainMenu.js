$(document).ready(function() {
	$("a.MenuButton, a.ActiveMenuButton").click(function() {
		// Workaround para poder setar quem � o item ativo no pr�prio html
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
		$("div#MainArea").load('PageLoader?page=' + requestedPage);
	});
});