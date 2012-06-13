<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="../javascript/back-end/MenuEditor.js"></script>

<h1>/Cardápio</h1>

<div class="LineHeader">
	<a href="#show" id="showHide">Exibir/Econder tudo</a>
</div>

<br />
<a href="#" class="dialog_cat">[+] Categoria</a>
<br /><br />

<c:forEach var="cat" items="${MenuCategories}">
	<c:set var="categoria" value="${cat}" scope="request"/>
	<jsp:include page="editor/category-editor.jsp"/>
</c:forEach>
<br />
<div class="dialog" id="dialogCat">
	<span class="BlockHeader"><span>:: Nova categoria</span></span>
	<div class="BlockContentBorder">
		<div class="error"></div>
		<form action="MenuEditor" class="newCat">
			<label for="name">Nome da categoria</label> 
			<input type="text" name="catName" id="name" />
			<br />
			<span class="ButtonInput"><span><input type="submit" value="Ok" /></span></span>
			<span class="ButtonInput simplemodal-close"><span><input type="button" value="Cancelar" /></span></span>
		</form>
	</div>
</div>
<div class="dialog" id="dialogSubCat">
		<span class="BlockHeader"><span>:: Nova sub-categoria de<i class="subCat_title"></i></span></span>
		<div class="BlockContentBorder">
			<div class="error"></div>
			<form action="MenuEditor" class="newCat">
				<label for="name">Nome da sub-categoria</label> 
				<input type="text" name="catName" id="name" />
				<input type="hidden" name="parentId" id="subCat_parentIdHidden" value="" />
				<br />
				<span class="ButtonInput"><span><input type="button" value="Ok" /></span></span>
				<span class="ButtonInput simplemodal-close"><span><input type="button" value="Cancelar" /></span></span>
			</form>
		</div>
	</div>