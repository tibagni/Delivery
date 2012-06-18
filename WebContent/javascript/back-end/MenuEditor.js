/*
 * Este arquivo define funcionalidades do editor de cardapio (back-end)
 */

$(document).ready(function(){
	//Envio de formulario de nova categoria/sub-categoria
	  $(".newCat").live("submit", function(event) {
	    /* stop form from submitting normally */
	    event.preventDefault();
	    /* pega os valores dos elementos do form: */
	    var $form = $( this ),
	        nome = $form.find( 'input[name="catName"]' ).val(),
	        parentIdField = $form.find( 'input[name="parentId"]' ),
	        url = $form.attr( 'action' ),
	        parent = "";

	    if (nome == null || nome == "") {
	    	$(".error").html("Nome da categoria inv�lido!");
	    	$(".error").show();
	    	return;
	    }

	    var callback = function() {
	        return function(data) {
				$(".panel:last").after(data);
		    	$.modal.close();
	        };
	    };
	    
	    if (parentIdField.exists()) {
	    	parent = parentIdField.val();
	    	sel = "[id='catTitle-" + parent + "']";
	    	// Muda o callback!
	    	callback = function() {
		        return function(data) {
		        	// Coloca a nova categoria no fim
					$(sel).next().append(data);
			    	$.modal.close();
		        };
		    };
	    }
	    
	    $.modal.close();
	    startLoading();

		$.post( url, { cmd : 'AddCategory', catName: nome, parentId: parent }, callback());
	  });
	  
		//Envio de formulario de edicao de categoria/sub-categoria
	  $(".editCat").live("submit", function(event) {
	    /* stop form from submitting normally */
	    event.preventDefault();
	    /* pega os valores dos elementos do form: */
	    var $form = $( this ),
	        nome = $form.find( 'input[name="catName"]' ).val(),
	        catId = $form.find( 'input[name="catId"]' ).val(),
	        url = $form.attr( 'action' );

	    if (nome == null || nome == "") {
	    	$(".error").html("Nome da categoria inv�lido!");
	    	$(".error").show();
	    	return;
	    }

	    var callback = function() {
	        return function(data) {
	        	// E melhor carregar o cardapio de novo para que as alteracoes tenham efeito
				$("div#MainArea").load('PageLoader?page=MenuEditor');
				$.modal.close();
	        };
	    };
	    
	    $.modal.close();
	    startLoading();

		$.post( url, { cmd : 'UpdateCategory', catName: nome, catId: catId }, callback());
	  });
	  
	  $(function() {	
			// Dialog Link - Nova categoria
			$(".dialog_cat").live("click", function() {
				$("#dialogCat").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
				// Antes de mais nada, � melhor esconder qualquer mensagem de erro que possa estar vsivel
				$(".error").hide();
				return false;
			});
			// Dialog Link - Nova sub-ategoria
			$(".dialog_subCat").live("click", function() {
				// Antes de mais nada, � melhor esconder qualquer mensagem de erro que possa estar vsivel
				$(".error").hide();
				var catInfo = $(this).attr("id").split("-");
				var parentName = catInfo[1];
				var parentId = catInfo[2];
				// Define o titulo do dialogo
				$("i.subCat_title").html(" " + parentName);
				// Adiciona o id da categoria como parentId no campo hidden
				$("#subCat_parentIdHidden").attr("value", parentId);
				
				$("#dialogSubCat").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
				return false;
			});	
			// Dialog Link - editar categoria
			$(".dialog_editCat").live("click", function() {
				$("#dialogEditCat").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
				// Antes de mais nada, � melhor esconder qualquer mensagem de erro que possa estar vsivel
				$(".error").hide();
				
				var catInfo = $(this).attr("id").split("-");
				var catName = catInfo[1];
				var catId = catInfo[2];
				
				$("#editCatName").attr("value", catName);
				$("#editHiddenId").attr("value", catId);
				return false;
			});
		});
	  
	  $(".page_Prod").live("click", function() {
		  // Pega a categoria em que o produto ser� inserido
		  var category = $(this).attr("id").split("-")[1];
		  $("div#MainArea").load('PageLoader?page=ProductEditor&cat=' + category);
	  });
	  
	  $(".page_edit_Prod").live("click", function() {
		  var product = $(this).attr("href").split("-")[1];
		  $("div#MainArea").load('PageLoader?page=ProductOverviewEditor&prodId=' + product);
	  });
});