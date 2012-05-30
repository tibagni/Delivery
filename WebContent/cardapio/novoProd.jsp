<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<script type="text/javascript" src="javascript/form.js"></script>

<h1>/Novo produto</h1>

<div class="tooltip"></div>
<div class="form">
	<form action="Menu">
		<fieldset>
			<legend>Informações gerais do produto</legend>
			<div>
				<label for="prod-nome">Nome do produto:</label>
				<input name="nome" id="prod-nome" type="text" title="Nome do produto"/>
			</div>
			<div>
				<label for="prod-desc">Descrição:</label>
				<input name="desc" id="prod-desc" type="text" title="Descrição do produto"/>
			</div>
			<div>
				<label for="prod-op">Opcionais por pedido:</label>
				<input name="op" id="prod-op" type="text" title="Quantidade máxima de opcionais que este produto pode ter em um pedido"/>
			</div>
			<div>
				<label for="prod-sab">Sabores por pedido:</label>
				<input name="sab" id="prod-sab" type="text" title="Quantidade de sabores que este produto pode ter em um pedido. Exemplo: Pizza pode ter 2 até sabores (Ex: metade calabresa e metade 4 queijos)"/>
			</div>
		</fieldset>
		<input type="hidden" name="catId" value="${param.cat}" />
		<input type="hidden" name="cmd" value="AddProduct" />
		<span class="ButtonInput"><span><input type="submit" value="Avançar >>" /></span></span>
	</form>
</div>
