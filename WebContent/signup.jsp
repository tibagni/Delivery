<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<script type="text/javascript" src="javascript/common/Form.js"></script>
<script type="text/javascript" src="javascript/common/Util.js"></script>
<script type="text/javascript" src="javascript/jquery.validate.js"></script>
<script type="text/javascript" src="javascript/masked-Input.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	
	function highlightField(field) {
		field.addClass('highlightedWrong');
	};
	
	function clearHighlights() {
		$(":input").removeClass('highlightedWrong');
	};
	
	//validacao do form
	$("#form-cli").submit(function () {
		clearHighlights();
		var errorMsgs = "";
		var name = $("#cli-nome");
		var cpf = $("#cli-cpf");
        var tel = $("#cli-tel");
        var email = $("#cli-email");
        var pwd = $("#cli-pwd");
        var pwd2 = $("#cli-pwd2");
        
        var submit = true;
        
        var focus = null;
        
        var form = $(this);
		
		if (isEmpty(name.val())) {
			highlightField(name);
			errorMsgs += "<li>O nome é obrigatório</li>";
			focus = name;
		}
		if (isEmpty(cpf.val())) {
			highlightField(cpf);
            errorMsgs += "<li>O CPF é obrigatório</li>";
            if (focus == null) focus = cpf;
		} else if (!isCPFValid(cpf.val())) {
			highlightField(cpf);
            errorMsgs += "<li>Insira um CPF válido!</li>";
            if (focus == null) focus = cpf;
        }
        if (isEmpty(tel.val())) {
            highlightField(tel);
            errorMsgs += "<li>O Telefone é obrigatório</li>";
            if (focus == null) focus = tel;
        }
        if (isEmpty(email.val())) {
            highlightField(email);
            errorMsgs += "<li>O Email é obrigatório</li>";
            if (focus == null) focus = email;
        } else if (!isEmailValid(email.val())) {
            highlightField(email);
            errorMsgs += "<li>Insira um Email válido!</li>";
            if (focus == null) focus = email;
        }
        
        //Validacao de senha
        if (isEmpty(pwd.val())) {
            highlightField(pwd);
            highlightField(pwd2);
            pwd2.val("");
            errorMsgs += "<li>Insira uma senha válida!</li>";
            if (focus == null) focus = pwd;
        } else {
        	if (pwd.val().length < 4) {
                pwd2.val("");
                pwd.val("");
                highlightField(pwd);
                highlightField(pwd2);
                errorMsgs += "<li>A senha deve ter, no mínimo, 4 caracteres!</li>";
                if (focus == null) focus = pwd;
        	} else if (pwd.val() != pwd2.val()) {
                pwd2.val("");
                pwd.val("");
                highlightField(pwd);
                highlightField(pwd2);
                errorMsgs += "<li>A senha e a confirmação não batem!</li>";
                if (focus == null) focus = pwd;
        	}
        }
        
        if (!isEmpty(errorMsgs)) {
        	$("#errorBox").show();
            $("#errorBox").html(errorMsgs);
            submit = false;
        }
        if (focus != null) {
        	focus.focus();
        }
        
        if (submit) {
        	ajaxSubmit(form);
        }
        
		return false;
		
	});
       
    // Aplicar mascara nos campos de formulario 
    $("#cli-cpf").mask("999.999.999-99");
    $("#cli-tel").mask("(99) 9999-9999");
    

    $("#cli-end-cep").mask("99999-999");
    $("#cli-end-uf").mask("aa");
    
    $("#addressLookup").click(function() {
    	var cep = $("#cli-end-cep").attr("value");
    	if (cep == null) {
    		return false;
    	}
    	$("#addressLookup").hide();
    	$("#addressLookingUp").show();
    	$.get('AddressLookup?formato=javascript&cep='+ cep, function(data) {
    	    $("#addressLookingUp").hide();
            $("#addressLookup").show();
            eval(data);
            // Agora (depois do eval) temos uma variavel para o cep, resultadoCEP
            if (resultadoCEP['resultado'] == 1) {
            	$("#cli-end-rua").attr("value", unescape(resultadoCEP['logradouro']));
                $("#cli-end-bair").attr("value", unescape(resultadoCEP['bairro']));
                $("#cli-end-cidade").attr("value", unescape(resultadoCEP['cidade']));
                $("#cli-end-uf").attr("value", unescape(resultadoCEP['uf']));
            } else {
                $("#cli-end-rua").attr("value", "");
                $("#cli-end-bair").attr("value", "");
                $("#cli-end-cidade").attr("value", "");
                $("#cli-end-uf").attr("value", "");
            	alert("cep não encontrado!");
            }
    	});
    });
});
</script>

<h1>/Cadastro</h1>

<div class="tooltip"></div>
<div class="form">
    <ul id="errorBox"></ul> 
    <form action="Account" id="form-cli">
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
                <label for="cli-tel">Telefone:</label>
                <input name="tel" id="cli-tel" type="text" title="Telefone para contato"/>
            </div>
            <div>
                <label for="cli-email">Email:</label>
                <input name="email" id="cli-email" type="text" title="Email. Será usado como Login no site!"/>
            </div>
            <div>
                <label for="cli-pwd">Senha:</label>
                <input name="pwd" id="cli-pwd" type="password" title="Senha para acesso ao site"/>
            </div>
            <div>
                <label for="cli-pwd2">Confirme a senha:</label>
                <input name="pwd2" id="cli-pwd2" type="password" title="Senha para acesso ao site"/>
            </div>
        </fieldset>
        
        <fieldset>
            <legend>Endereço</legend>
             <div>        
	            <label for="cli-end-cep">Cep </label>
	            <input id="cli-end-cep" size="10" name="cep" type="text" title="CEP da rua" />
	            <img id="addressLookup" src="images/lookup.gif" title="auto-preencher endereço"/>
                <img id="addressLookingUp" src="images/arrow-loading.gif" style="display: none;"/>
            </div>
            <div>          
               <label for="cli-end-rua">Rua </label>
               <input id="cli-end-rua" name="rua" type="text" title="Nome da ruda" />
            </div>
            <div>
               <label for="cli-end-num">Número </label>
               <input id="cli-end-num" size="4" name="num" type="text" title="Número..." />
            </div>
            <div>
               <label for="cli-end-comp">Complemento </label>
               <input id="cli-end-comp" size="4" name="comp" type="text" title="Complemento" />
            </div>
            <div>
               <label for="cli-end-bair">Bairro </label>
               <input id="cli-end-bair" name="bair" type="text" title="Bairro" />
            </div>
            <div>
               <label for="cli-end-cidade">Cidade </label>
               <input id="cli-end-cidade" name="cidade" type="text" title="Cidade" />
            </div>
            <div>
               <label for="cli-end-uf">UF </label>
               <input id="cli-end-uf" name="uf" size="2" type="text" title="UF" />
            </div>
        </fieldset>

        <input type="hidden" name="cmd" value="AddNewUser" />
        <span class="ButtonInput"><span><input type="submit" value="Avançar >>" /></span></span>
    </form>
</div>
