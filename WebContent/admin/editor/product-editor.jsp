<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
});
</script>

<h1>/Novo produto</h1>

<div class="tooltip"></div>
<div class="form">
	<form action="MenuEditor" class="ajaxForm">
		<fieldset>
			<legend>Informações gerais do produto</legend>
			<div>
				<label for="prod-nome">Nome do produto:</label>
				<input name="nome" id="prod-nome" type="text" title="Nome do produto"/>
			</div>
			<div>
				<label for="prod-desc">Descrição:</label>
				<input name="desc" id="prod-desc" type="text" title="Breve descrição do produto"/>
			</div>
			<div>
				<label for="prod-op">Opcionais por pedido:</label>
				<input name="op" id="prod-op" type="text" title="Quantidade máxima de opcionais que este produto pode ter em um pedido"/>
			</div>
			<div>
				<label for="prod-sab">Sabores por pedido:</label>
				<input name="sab" id="prod-sab" type="text" title="Quantidade de sabores que este produto pode ter em um pedido. Exemplo: Pizza pode ter 2 até sabores (Ex: metade calabresa e metade 4 queijos)"/>
			</div>
			<div>
				<label for="prod-foto">Foto:</label>
				<div id="upload-btn">
					<span class="ButtonInput"><span>
						<input id="prod-foto" type="button" value="Selecionar foto..." title="Foto do produto que será exibida no cardápio"/>
					</span></span>
					<div id="previewFoto" style="display:none; float:right;">
						<img src="images/arrow-loading.gif" />
					</div>
				</div>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Tamanho</legend>
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
		
		<input type="hidden" name="catId" value="${param.cat}" />
		<input type="hidden" name="foto" value="" id="fotoHidden" />
		<input type="hidden" name="cmd" value="AddProduct" />
		<span class="ButtonInput"><span><input type="submit" value="Avançar >>" /></span></span>
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
	<!-- gambi -->
	<iframe id="upload_target" name="upload_target" src="#" style="display:none;"></iframe>
	<!-- gambi -->
</div>
