$(document).ready(function(){	
	$(".orderItem").easyTooltip({
		clickRemove: true,
		useElement: "detail"				   
	});

	$(".removeItem").live("click", function () {
		var id = $(this).attr("id").split("_")[1];
		$("div#userArea").load('Order?cmd=RemoveItemFromOrder&itemTempId=' + id);
	});
});