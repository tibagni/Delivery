<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="../javascript/jquery.maskMoney.js"></script>
<script type="text/javascript" src="../javascript/back-end/price-mask.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("a.addNew").click(function() {
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		var requestedPage = $(this).attr("href").substring(1);
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage + '&prodId=${product.id}');
		}
	});
	
	$("a.backToEditor").click(function() {
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		$("div#MainArea").load('PageLoader?page=MenuEditor');
	});
	
	$("a.editExisting").click(function() {
		$("div#mainArea").html("<img src=\"../images/loading-circle.gif\" />");
		var linkParts = $(this).attr("href").split("-");
		var requestedPage = linkParts[1];
		var editingId = linkParts[2];
		
		if (requestedPage != null && requestedPage.length > 0) {
			$("div#MainArea").load('PageLoader?page=' + requestedPage + '&prodId=${product.id}&editing=' + editingId);
		}
	});
	
	  $(".editOptional").live("submit", function(event) {
		    /* stop form from submitting normally */
		    event.preventDefault();
		    /* pega os valores dos elementos do form: */
		    var $form = $( this ),
		        price = $form.find( 'input[name="optionalPrice"]' ).val(),
		        id = $form.find( '#editHiddenId' ).val(),
		        url = $form.attr( 'action' );

		    if (price == null || price == "") {
		    	$(".error").html("Preço inválido!");
		    	$(".error").show();
		    	return;
		    }

		    var callback = function() {
		        return function(data) {
		        	$("i#-optionalPrice-" + id).html(price);	
		        	$.modal.close();
		        };
		    };
		    
		    $.modal.close();
		    startLoading();

			$.post( url, { cmd : 'UpdateOptional', optionalPrice: price, id: id }, callback());
		  });
	  
	  $(".editExistingOptional").live("click", function() {
			$("#dialogEditOptional").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
			// Antes de mais nada, é melhor esconder qualquer mensagem de erro que possa estar vsivel
			$(".error").hide();
			var optionalInfo = $(this).attr("href").split("-");
			var id = optionalInfo[2];
			var name = optionalInfo[3];
			$("#editHiddenId").attr("value", id);
			$("#editOptionalLabel").html(name + ": ");
			return false;
	  });
});
</script>

<h1>${product.name}</h1>
<a class="editExisting" href="#-ProductEditor-${product.id}">Editar este produto</a>
<h3>Sabores:</h3>
<ul>
	<c:forEach var="flavour" items="${product.flavours}">
		<li><a class="editExisting" href="#-FlavourEditor-${flavour.id}">${flavour.name}</a></li>
	</c:forEach>	
</ul>
<a class="addNew" href="#FlavourEditor">[+] Adicionar outro sabor</a>
<h3>Opcionais:</h3>
<ul>
	<c:forEach var="optional" items="${product.optionals}">
		<li>${optional.name} - R$: <i id="-optionalPrice-${optional.id}">${optional.price}</i>
			<a class="editExistingOptional" href="#-OptionalEditor-${optional.id}-${optional.name}">[Editar]</a></li>
	</c:forEach>
</ul>
<a class="addNew" href="#OptionalEditor">[+] Adicionar outro opcional</a>
<p>
<a class="backToEditor" href="#MenuEditor">Voltar ao editor</a></p>


<div class="dialog" id="dialogEditOptional">
	<span class="BlockHeader"><span>:: Alterar Preço</span></span>
	<div class="BlockContentBorder">
		<div class="error"></div>
		<form action="MenuEditor" class="editOptional">
			<label id="editOptionalLabel" for="editOptional">aaaaaaaaaaaaaaaaaaaaaaaaaa</label> 
			<input class="price" type="text" name="optionalPrice" id="editOptional" />
			<br />
			<input type="hidden" name="id" id="editHiddenId" />
			<span class="ButtonInput"><span><input type="submit" value="Ok" /></span></span>
			<span class="ButtonInput simplemodal-close"><span><input type="button" value="Cancelar" /></span></span>
		</form>
	</div>
</div>