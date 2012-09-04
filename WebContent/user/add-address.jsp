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
	$("#form-addr").submit(function () {
		clearHighlights();
		var errorMsgs = "";
		var rua = $("#cli-end-rua");
		var bairro = $("#cli-end-bair");
        var uf = $("#cli-end-uf");
        var cidade = $("#cli-end-cidade");
        var cep = $("#cli-end-cep");
        var num = $("#cli-end-num");
        
        var submit = true;
        
        var focus = null;
        
        var form = $(this);
		
		if (isEmpty(rua.val())) {
			highlightField(rua);
			errorMsgs += "<li>O nome da rua é obrigatório</li>";
			focus = rua;
		}
		if (isEmpty(bairro.val())) {
			highlightField(bairro);
            errorMsgs += "<li>O bairro é obrigatório</li>";
            if (focus == null) focus = bairro;
		}
		if (isEmpty(uf.val())) {
			highlightField(uf);
            errorMsgs += "<li>O UF é obrigatório</li>";
            if (focus == null) focus = uf;
        }
        if (isEmpty(cidade.val())) {
            highlightField(cidade);
            errorMsgs += "<li>A cidade é obrigatória</li>";
            if (focus == null) focus = cidade;
        }
        if (isEmpty(cep.val())) {
            highlightField(cep);
            errorMsgs += "<li>O cep é obrigatório</li>";
            if (focus == null) focus = cep;
        }
        if (isEmpty(num.val())) {
            highlightField(num);
            errorMsgs += "<li>O número é obrigatório</li>";
            if (focus == null) focus = num;
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

<h1>/Cadastro de endereço</h1>

<div class="tooltip"></div>
<div class="form">
    <ul id="errorBox"></ul> 
    <form action="Account" id="form-addr" method="post">     
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

        <input type="hidden" name="cmd" value="AddAddress" />
        <span class="ButtonInput"><span><input type="submit" value="Ok" /></span></span>
    </form>
</div>
