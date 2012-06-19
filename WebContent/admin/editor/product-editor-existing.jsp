<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="../javascript/jquery.sheepItPlugin-1.0.0.js"></script>
<script type="text/javascript" src="../javascript/common/Form.js"></script>
<script type="text/javascript" src="../javascript/masked-Input.js"></script>
<script type="text/javascript" src="../javascript/back-end/productForm-mask.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#sheepItForm').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        maxFormsCount: <%= request.getAttribute("sizesLeft")%>,
        minFormsCount: 0,
        iniFormsCount: 0,
        
        // Confirmations
        removeLastConfirmation: false,
        removeCurrentConfirmation: false,
        removeAllConfirmation: true,
        removeLastConfirmationMsg: 'Você tem certeza?',
        removeCurrentConfirmationMsg: 'Você tem certeza?',
        removeAllConfirmationMsg: 'Você tem certeza?'
        
    });
	
	// Dialog Link - Upload de foto
	$("#prod-foto").live("click", function() {
		$("#upload-dialog").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
		return false;
	});
	
	//
	$("form#upload-form").live("submit", function() {
		$("div#previewFoto").show();
		$("div#previewFoto").html("<img src=\"../images/arrow-loading.gif\" />");
		$.modal.close();
	});
	
	  $(".editSize").live("submit", function(event) {
		    /* stop form from submitting normally */
		    event.preventDefault();
		    /* pega os valores dos elementos do form: */
		    var $form = $( this ),
		        nome = $form.find( 'input[name="name"]' ).val(),
		        sizeId = $form.find( 'input[name="id"]' ).val(),
		        url = $form.attr( 'action' );

		    if (nome == null || nome == "") {
		    	$(".error").html("Tamanho inválido!");
		    	$(".error").show();
		    	return;
		    }

		    var callback = function() {
		        return function(data) {
		        	$("span#-size-" + sizeId).html(nome);	
		        	$.modal.close();
		        };
		    };
		    
		    $.modal.close();
		    startLoading();

			$.post( url, { cmd : 'UpdateProductSize', name: nome, id: sizeId }, callback());
		  });
	  
	  $('input[name="cancel"]').live("click", function() {
			$("div#MainArea").load('PageLoader?page=MenuEditor');
	  });
	  
	  $(".editSizeLink").live("click", function() {
			$("#dialogEditSize").modal({overlayClose:true, overlayCss: {backgroundColor:"#000"}});
			// Antes de mais nada, é melhor esconder qualquer mensagem de erro que possa estar vsivel
			$(".error").hide();
			var sizeInfo = $(this).attr("href").split("-");
			var id = sizeInfo[2];
			var currentName = $("span#-size-" + id).html();
			
			$("#editSizeName").attr("value", currentName);
			$("#editHiddenId").attr("value", id);
			return false;
	  });
});
</script>

<h1>/Editar produto/${product.name}</h1>

<div class="tooltip"></div>
<div class="form">
	<form action="MenuEditor" class="ajaxForm">
		<fieldset>
			<legend>Informações gerais do produto</legend>
			<div>
				<label for="prod-nome">Nome do produto:</label>
				<input name="nome" id="prod-nome" type="text" value="${product.name}" title="Nome do produto"/>
			</div>
			<div>
				<label for="prod-desc">Descrição:</label>
				<input name="desc" id="prod-desc" type="text" value="${product.description}" title="Breve descrição do produto"/>
			</div>
			<div>
				<label for="prod-op">Opcionais por pedido:</label>
				<input name="op" id="prod-op" type="text" value="${product.optionalsPerOrder}" title="Quantidade máxima de opcionais que este produto pode ter em um pedido"/>
			</div>
			<div>
				<label for="prod-sab">Sabores por pedido:</label>
				<input name="sab" id="prod-sab" type="text" value="${product.flavoursPerOrder}" title="Quantidade de sabores que este produto pode ter em um pedido. Exemplo: Pizza pode ter 2 até sabores (Ex: metade calabresa e metade 4 queijos)"/>
			</div>
			<div>
				<label for="prod-foto">Foto:</label>
				<div id="upload-btn">
					<span class="ButtonInput"><span>
						<input id="prod-foto" type="button" value="Selecionar foto..." title="Foto do produto que será exibida no cardápio"/>
					</span></span>
					<c:choose>
						<c:when test="${not empty product.picturePath}">
							<div id="previewFoto" style="float:right;">
								<img src="../${product.picturePath}" />
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
			<legend>Tamanho</legend>
				<ul>
					<c:forEach var="size" items="${product.sizesAvailable}">
						<li><span id="-size-${size.id}">${size.name}</span> <a class="editSizeLink" href="#-editSize-${size.id}">[Editar]</a></li>
					</c:forEach>	
				</ul>
				<h3>Adicionar Novos tamanhos</h3>
				<!-- sheepIt Form -->
				<div id="sheepItForm">
				  <!-- Form template-->
				  <div id="sheepItForm_template">
				    <label for="sheepItForm_#index#_tamanho">Tamanho <span id="sheepItForm_label"></span></label>
				    <input id="sheepItForm_#index#_tamanho" name="produto[tamanhos][#index#][tamanho]" type="text" title="Os tamanhos que um produto pode assumir (pequeno, médio, grande...)" />
				    <a id="sheepItForm_remove_current">
				      <img class="delete" src="../images/cross.gif" border="0">
				    </a>
				    <hr>
				  </div>
				  <!-- /Form template-->
				   
				  <!-- No forms template -->
				  <div id="sheepItForm_noforms_template">Nenhum tamanho foi especificado</div>
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
		
		<input type="hidden" name="prodId" value="${product.id}" />
		<input type="hidden" name="foto" id="fotoHidden" value="${product.picturePath}" />
		<input type="hidden" name="cmd" value="UpdateProduct" />
		<span class="ButtonInput"><span><input type="submit" value="Atualizar" /></span></span>
		<span class="ButtonInput"><span><input type="button" name="cancel" value="Cancelar" /></span></span>
	</form>

	<div class="dialog" id="upload-dialog">
		<form action="MenuEditor" enctype="multipart/form-data" target="upload_target" method="post" id="upload-form">
			<span class="BlockHeader"><span>:: Selecione uma foto</span></span>
			<div>
				<label for="prod-foto-up">Foto do produto:</label> 
				<input name="foto-up" id="prod-foto-up" type="file" title="Foto do produto" />
			</div>
			<span class="ButtonInput"><span><input type="submit" value="Fazer upload..." /></span></span>
		</form>
	</div>
	
	<div class="dialog" id="dialogEditSize">
		<span class="BlockHeader"><span>:: Editar tamanho</span></span>
		<div class="BlockContentBorder">
			<div class="error"></div>
			<form action="MenuEditor" class="editSize">
				<label for="editSizeName">Tamanho</label> 
				<input type="text" name="name" id="editSizeName" />
				<br />
				<input type="hidden" name="id" id="editHiddenId" />
				<span class="ButtonInput"><span><input type="submit" value="Ok" /></span></span>
				<span class="ButtonInput simplemodal-close"><span><input type="button" value="Cancelar" /></span></span>
			</form>
		</div>
	</div>
	<!-- gambi -->
	<iframe id="upload_target" name="upload_target" src="#" style="display:none;"></iframe>
	<!-- gambi -->
</div>
