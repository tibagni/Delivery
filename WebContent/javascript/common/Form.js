/*
 * Este arquivo define funcionalidades de formularios ajax
 */

$(document).ready(function() {
	// Hint dos campos do formul�rio
	$("input").live("focus", function() {
		var tip = $(this).attr("title");
		$(".tooltip").html(tip);
	});
	
	// Envio do formul�rio usando ajax
	$(".ajaxForm").submit(function() {
		var serializedForm = $(this).serialize();
		var url = $(this).attr( 'action' );
		startLoading();
		$.post(url, serializedForm, function(data) {
			$("div#MainArea").html(data);
			$.modal.close();
		}).error(function (jqXHR, textStatus, errorThrown) {
			if (textStatus == "error") {
				if (errorThrown == "Unauthorized") {
					handleUnauthorizedError();
				}
			}
			$.modal.close();
		});
		return false;
	});
});

function ajaxSubmit(form) {
	var serializedForm = form.serialize();
	var url = form.attr( 'action' );
	startLoading();
	$.post(url, serializedForm, function(data) {
		$("div#MainArea").html(data);
		$.modal.close();
	}).error(function (jqXHR, textStatus, errorThrown) {
		if (textStatus == "error") {
			if (errorThrown == "Unauthorized") {
				handleUnauthorizedError();
			}
		}
		$.modal.close();
	});
	return false;
}