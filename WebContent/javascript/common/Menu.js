/*
 * Este arquivo define funcionalidades do cardapio comuns ao editor (back-end)
 * e ao visualizador (front-end)
 */

$(document).ready(function(){
	//Efeito para mostrar e esconder o conudo de uma categoria no cardapio
	$(".flip").live("click", function(){
	    $(this).next().slideToggle("slow");
	  });
	
	//Aplica o efeito a todas as categorias (incluindo sub-categorias)
	$("#showHide").live("click", function() {
		var action = $(this).attr("href");

		if (action == "#show") {
			$(".panel").slideDown();
			$(this).attr("href", "#hide");
		} else {
			$(".panel").slideUp();
			$(this).attr("href", "#show");			
		}
	});
});
