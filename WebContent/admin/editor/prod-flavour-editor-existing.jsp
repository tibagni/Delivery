<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="../javascript/jquery.sheepItPlugin-1.0.0.js"></script>
<script type="text/javascript" src="../javascript/common/Form.js"></script>
<script type="text/javascript">
$(document).ready(function() {     
    $('#sheepItForm').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        maxFormsCount: 3,
        minFormsCount: 1,
        iniFormsCount: 1,
        
        // Confirmations
        removeLastConfirmation: false,
        removeCurrentConfirmation: false,
        removeAllConfirmation: true,
        removeLastConfirmationMsg: 'Você tem certeza?',
        removeCurrentConfirmationMsg: 'Você tem certeza?',
        removeAllConfirmationMsg: 'Você tem certeza?'
        
    });
    
    
	// Dialog Link - Upload de foto
	$("#sab-foto").live("click", function() {
		$("#upload-dialog").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
		return false;
	});
	
	//
	$("form#upload-form").live("submit", function() {
		$("div#previewFoto").show();
		$("div#previewFoto").html("<img src=\"../images/arrow-loading.gif\" />");
		$.modal.close();
	});	
	
	  $(".editPriceLink").live("click", function() {
			$("#dialogEditPrice").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
			// Antes de mais nada, é melhor esconder qualquer mensagem de erro que possa estar vsivel
			$(".error").hide();
			var priceInfo = $(this).attr("href").split("-");
			var sizeId = priceInfo[3];
			var currentName = priceInfo[4];
			$("#editPriceLabel").html('Novo valor para o tamanho ' + currentName);
			$("#editHiddenSizeId").attr("value", sizeId);
			return false;
	  });

	  $(".editPrice").live("submit", function(event) {
		    /* stop form from submitting normally */
		    event.preventDefault();
		    /* pega os valores dos elementos do form: */
		    var $form = $( this ),
		        price = $form.find( 'input[name="price"]' ).val(),
		        sizeId = $form.find( '#editHiddenSizeId' ).val(),
		        flavourId = $form.find( '#editHiddenFlavourId' ).val(),
		        url = $form.attr( 'action' );

		    if (price == null || price == "") {
		    	$(".error").html("Preço inválido!");
		    	$(".error").show();
		    	return;
		    }

		    var callback = function() {
		        return function(data) {
		        	$("i#-priceValue-" + flavourId + "-" + sizeId).html(price);	
		        	$.modal.close();
		        };
		    };
		    
		    $.modal.close();
		    startLoading();

			$.post( url, { cmd : 'UpdatePrice', price: price, sizeId: sizeId, flavourId: flavourId }, callback());
		  });
	  
	  $('input[name="cancel"]').live("click", function() {
			$("div#MainArea").load('PageLoader?page=MenuEditor');
	  });
});
</script>

<h1>/Editar sabor/${flavour.name}</h1>
<br />
<div class="tooltip"></div>
<div class="form">
	<form action="MenuEditor" class="ajaxForm">
		<fieldset>
			<legend>Sabor do produto</legend>
			<div>
				<label for="sab-nome">Nome do sabor:</label>
				<input name="nome" id="sab-nome" value="${flavour.name}" type="text" title="Nome do sabor (por exemplo: Calabresa, se o produto for uma pizza, ou maracujá, se for um suco...)"/>
			</div>
			<div>
				<label for="sab-desc">Descrição:</label>
				<input name="desc" id="sab-desc" value="${flavour.description}" type="text" title="Breve descrição do sabor (Ex: ingrediantes da pizza de calabbresa)"/>
			</div>
			<div>
				<label for="sab-foto">Foto:</label>
				<div id="upload-btn">
					<span class="ButtonInput"><span>
						<input id="sab-foto" type="button" value="Selecionar foto..." title="Foto que será exibida no cardápio (Se não for especificada, a foto do produto será usada)"/>
					</span></span>
					<c:choose>
						<c:when test="${not empty product.picturePath}">
							<div id="previewFoto" style="float:right;">
								<img src="../${flavour.picturePath}" />
							</div>						
						</c:when>
						<c:otherwise>
							<div id="previewFoto" style="display:none; float:right;">
								<img src="../images/arrow-loading.gif" />
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Preço</legend>
				<ul>
					<c:forEach var="price" items="${flavour.prices}">
						<li><span id="-price-${price.flavourId}-${price.sizeId}">${price.cachedSizeName} - R$ <i id="-priceValue-${price.flavourId}-${price.sizeId}">${price.price}</i></span> 
						<a class="editPriceLink" href="#-editPrice-${price.flavourId}-${price.sizeId}-${price.cachedSizeName}">[Editar]</a></li>
					</c:forEach>	
				</ul>
				<h3>Adicionar Novos precos</h3>
				<!-- sheepIt Form -->
				<div id="sheepItForm">
				  <!-- Form template-->
				  <div id="sheepItForm_template">
				    <label for="sheepItForm_#index#_tam">Tamanho <span id="sheepItForm_label"></span></label>
				    <select id="sheepItForm_#index#_tam" name="produto[tamanhos][#index#][tamanho]" title="Tamanho do produto">
						<c:forEach var="tam" items="${produto.sizesAvailable}">
							<option value="${tam.id}">${tam.name}</option>
						</c:forEach>
					</select>
				    <br />
				    <label for="sheepItForm_#index#_preco">Preço <span id="sheepItForm_label"></span></label>
				    <input id="sheepItForm_#index#_preco" name="produto[precos][#index#][preco]" type="text" title="Preço do produto para o tamanho especificado" />
				    <a id="sheepItForm_remove_current">
				      <img class="delete" src="../images/cross.gif" border="0">
				    </a>
				    <hr>
				  </div>
				  <!-- /Form template-->
				   
				  <!-- No forms template - Nao deveria ser exbido! -->
				  <div id="sheepItForm_noforms_template">Nenhum Preco foi especificado</div>
				  <!-- /No forms template-->
				   
				  <!-- Controls -->
				  <div id="sheepItForm_controls">
				    <div id="sheepItForm_add"><a><span>Adicionar</span></a></div>
				    <div id="sheepItForm_remove_all"><a><span>Remover todos</span></a></div>
				  </div>
				  <!-- /Controls -->
				</div>
				<!-- /sheepIt Form -->
		</fieldset>
		
		
		<input type="hidden" name="flavId" value="${flavour.id}" />
		<input type="hidden" name="foto" value="" id="fotoHidden" />
		<input type="hidden" name="cmd" value="UpdateFlavour" />
		<span class="ButtonInput"><span><input type="submit" value="Atualizar" /></span></span>
		<span class="ButtonInput"><span><input type="button" name="cancel" value="Cancelar" /></span></span>
	</form>

	<div class="dialog" id="upload-dialog">
		<form action="MenuEditor" enctype="multipart/form-data" target="upload_target" method="post" id="upload-form">
			<span class="BlockHeader"><span>:: Selecione uma foto</span></span>
			<div>
				<label for="sab-foto-up">Foto:</label> 
				<input name="foto-up" id="sab-foto-up" type="file" title="Foto que será exibida no cardápio (Se não for especificada, a foto do produto será usada)" />
			</div>
			<span class="ButtonInput"><span><input type="submit" value="Fazer upload..." /></span></span>
		</form>
	</div>	
	<div class="dialog" id="dialogEditPrice">
		<span class="BlockHeader"><span>:: Alterar Preço</span></span>
		<div class="BlockContentBorder">
			<div class="error"></div>
			<form action="MenuEditor" class="editPrice">
				<label id="editPriceLabel" for="editPrice"></label> 
				<input type="text" name="price" id="editPrice" />
				<br />
				<input type="hidden" name="sizeId" id="editHiddenSizeId" />
				<input type="hidden" name="flavourId" id="editHiddenFlavourId" value="${flavour.id}" />
				<span class="ButtonInput"><span><input type="submit" value="Ok" /></span></span>
				<span class="ButtonInput simplemodal-close"><span><input type="button" value="Cancelar" /></span></span>
			</form>
		</div>
	</div>
	<!-- gambi -->
	<iframe id="upload_target" name="upload_target" src="#" style="display:none;"></iframe>
	<!-- gambi -->
</div>
