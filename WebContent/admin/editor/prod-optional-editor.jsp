<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="javascript/jquery.sheepItPlugin-1.0.0.js"></script>
<script type="text/javascript" src="javascript/common/Form.js"></script>
<script type="text/javascript">
$(document).ready(function() {     
    var sheepItForm = $('#sheepItForm').sheepIt({
        separator: '',
        allowRemoveLast: true,
        allowRemoveCurrent: true,
        allowRemoveAll: true,
        allowAdd: true,
        allowAddN: true,
        maxFormsCount: 10,
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
});
</script>

<h1>/Novo produto/Opcionais</h1>

<div class="tooltip"></div>
<div class="form">
	<form action="MenuEditor" class="ajaxForm">
		<fieldset>
			<legend>Opcionais do produto (${produto.name })</legend>
				<!-- sheepIt Form -->
				<div id="sheepItForm">
				  <!-- Form template-->
				  <div id="sheepItForm_template">
				    <label for="sheepItForm_#index#_nome_op">Opcional <span id="sheepItForm_label"></span></label>
				    <input id="sheepItForm_#index#_nome_op" name="produto[opcionais][#index#][nome]" type="text" title="Nome do ocpional" />
				    <br />
				    <label for="sheepItForm_#index#_preco_op">Preço do opcional <span id="sheepItForm_label"></span></label>
				    <input id="sheepItForm_#index#_preco_op" name="produto[opcionais][#index#][preco]" type="text" title="Preço do opcional" />
				    <a id="sheepItForm_remove_current">
				      <img class="delete" src="images/cross.gif" border="0">
				    </a>
				    <hr>
				  </div>
				  <!-- /Form template-->
				   
				  <!-- No forms template - Nao deveria ser exbido! -->
				  <div id="sheepItForm_noforms_template">Nenhum opcional foi especificado</div>
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
		<input type="hidden" name="cmd" value="AddOptional" />
		<span class="ButtonInput"><span><input type="submit" value="Avançar >>" /></span></span>
	</form>
</div>
    