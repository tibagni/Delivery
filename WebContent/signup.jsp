<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<script type="text/javascript" src="javascript/jquery.sheepItPlugin-1.0.0.js"></script>
<script type="text/javascript" src="javascript/common/Form.js"></script>
<script type="text/javascript" src="javascript/common/Util.js"></script>
<script type="text/javascript" src="javascript/masked-Input.js"></script>
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
        
    // Aplicar mascara nos campos de formulario 
    $("#cli-cpf").mask("999.999.999-99");
    $("#cli-tel").mask("(99) 9999-9999");
    

    $(".cep").live("focusin", function() {
    	$(this).mask("99999-999");
    });
    $(".uf").live("focusin", function() {
        $(this).mask("aa");
    });
    
    // Validacao do email
    $("#cli-email").focusout(function() {
    	var sEmail = $(this).val();
        if ($.trim(sEmail).length == 0 || !isEmailValid(sEmail)) {
            alert('Email inválido!');
            e.preventDefault();
        }
    });
});
</script>

<h1>/Cadastro</h1>

<div class="tooltip"></div>
<div class="form">
    <form action="MenuEditor" class="ajaxForm">
        <fieldset>
            <legend>Informações pessoais</legend>
            <div>
                <label for="cli-nome">Nome:</label>
                <input name="nome" id="cli-nome" type="text" title="Nome completo"/>
            </div>
            <div>
                <label for="cli-cpf">CPF:</label>
                <input name="cpf" id="cli-cpf" type="text" title="Número do Cpf"/>
            </div>
            <div>
                <label for="cli-email">Email:</label>
                <input name="email" id="cli-email" type="text" title="Email"/>
            </div>
            <div>
                <label for="cli-tel">Telefone:</label>
                <input name="tel" id="cli-tel" type="text" title="Telefone para contato"/>
            </div>
        </fieldset>
        
        <fieldset>
            <legend>Endereço</legend>
                <!-- sheepIt Form -->
                <div id="sheepItForm">
                  <!-- Form template-->
                  <div id="sheepItForm_template">
                    <label for="sheepItForm_#index#_cep">Cep <span id="sheepItForm_label"></span></label>
                    <input class="cep" id="sheepItForm_#index#_cep" size="10" name="cliente[enderecos][#index#][cep]" type="text" title="" />
                    <br />                    
                    <label for="sheepItForm_#index#_rua">Rua <span id="sheepItForm_label"></span></label>
                    <input id="sheepItForm_#index#_rua" name="cliente[enderecos][#index#][rua]" type="text" title="" />
                    <br />
                    <label for="sheepItForm_#index#_num">Número <span id="sheepItForm_label"></span></label>
                    <input id="sheepItForm_#index#_num" size="4" name="cliente[enderecos][#index#][num]" type="text" title="" />
                    <br />
                    <label for="sheepItForm_#index#_comp">Complemento <span id="sheepItForm_label"></span></label>
                    <input id="sheepItForm_#index#_comp" size="4" name="cliente[enderecos][#index#][comp]" type="text" title="" />
                    <br />
                    <label for="sheepItForm_#index#_bair">Bairro <span id="sheepItForm_label"></span></label>
                    <input id="sheepItForm_#index#_bair" name="cliente[enderecos][#index#][bair]" type="text" title="" />
                    <br />
                    <label for="sheepItForm_#index#_cidade">Cidade <span id="sheepItForm_label"></span></label>
                    <input id="sheepItForm_#index#_cidade" name="cliente[enderecos][#index#][cidade]" type="text" title="" />
                    <br />
                    <label for="sheepItForm_#index#_uf">UF <span id="sheepItForm_label"></span></label>
                    <input class="uf" id="sheepItForm_#index#_uf" name="cliente[enderecos][#index#][uf]" size="2" type="text" title="" />

                    <a id="sheepItForm_remove_current">
                      <img class="delete" src="images/cross.gif" border="0">
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

        <input type="hidden" name="cmd" value="AddProduct" />
        <span class="ButtonInput"><span><input type="submit" value="Avançar >>" /></span></span>
    </form>
</div>
