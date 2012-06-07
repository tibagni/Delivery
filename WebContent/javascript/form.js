$(document).ready(function(){
	$("input").focus(function() {
		var tip = $(this).attr("title");
		$(".tooltip").html(tip);
	});
	
	$(".ajaxForm").submit(function() {
		var serializedForm = $(this).serialize();
		var url = $(this).attr( 'action' );
		startLoading();
		$.post(url, serializedForm, function(data) {
			$("div#MainArea").html(data);
			$.modal.close();
		});
		return false;
	});
});