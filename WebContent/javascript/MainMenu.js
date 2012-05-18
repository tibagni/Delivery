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
		
		var requestedPage = $(this).attr("href");
		$("div#MainArea").load('PageLoader', {page:requestedPage});
	});
});