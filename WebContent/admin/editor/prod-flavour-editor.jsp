<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="../javascript/jquery.sheepItPlugin-1.0.0.js"></script>
<script type="text/javascript" src="../javascript/common/Form.js"></script>
<script type="text/javascript" src="../javascript/jquery.maskMoney.js"></script>
<script type="text/javascript" src="../javascript/back-end/price-mask.js"></script>
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
});
</script>

<h1>/Novo produto/Sabor</h1>
	<c:if test="${not empty finalMsg }">
		<blockquote>
			${finalMsg }
		</blockquote>
	</c:if>
<br />
<div class="tooltip"></div>
<div class="form">
	<form action="MenuEditor" class="ajaxForm">
		<fieldset>
			<legend>Sabor do produto</legend>
			<div>
				<label for="sab-nome">Nome do sabor:</label>
				<input name="nome" id="sab-nome" type="text" title="Nome do sabor (por exemplo: Calabresa, se o produto for uma pizza, ou maracujá, se for um suco...)"/>
			</div>
			<div>
				<label for="sab-desc">Descrição:</label>
				<input name="desc" id="sab-desc" type="text" title="Breve descrição do sabor (Ex: ingrediantes da pizza de calabbresa)"/>
			</div>
			<div>
				<label for="sab-foto">Foto:</label>
				<div id="upload-btn">
					<span class="ButtonInput"><span>
						<input id="sab-foto" type="button" value="Selecionar foto..." title="Foto que será exibida no cardápio (Se não for especificada, a foto do produto será usada)"/>
					</span></span>
					<div id="previewFoto" style="display:none; float:right;">
						<img src="../images/arrow-loading.gif" />
					</div>
				</div>
			</div>
		</fieldset>
		
		<fieldset>
			<legend>Preço</legend>
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
		
		
		<input type="hidden" name="prodId" value="${produto.id}" />
		<input type="hidden" name="foto" value="" id="fotoHidden" />
		<input type="hidden" name="cmd" value="AddFlavour" />
		<span class="ButtonInput"><span><input type="submit" value="Avançar >>" /></span></span>
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
	<!-- gambi -->
	<iframe id="upload_target" name="upload_target" src="#" style="display:none;"></iframe>
	<!-- gambi -->
</div>
