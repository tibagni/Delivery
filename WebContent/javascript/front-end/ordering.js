$(document).ready(function(){
	
	// ShopCar -------------------------------------
	$(".orderItem").easyTooltip({
		clickRemove: true,
		useElement: "detail"				   
	});

	$(".removeItem").live("click", function () {
		var id = $(this).attr("id").split("_")[1];
		$("div#shopCarArea").load('Order?cmd=RemoveItemFromOrder&itemTempId=' + id);
	});
	
	$("#finalize").click(function() {
		$("div#mainArea").load("Order?cmd=SelectOrderAddress", function(response, status, xhr) {
			if (status == "error") {
				handleUnauthorizedError();
			}
		}); 
	});
	
	// AddressSelector -----------------------------
	$(".addressBox").click(function () {
		var addressId = $(this).attr("id").split("-")[1];
		$("div#mainArea").load("Order?cmd=FinalizeOrder&selectedAddress=" + addressId, 
				function(response, status, xhr) {
			if (status == "error") {
				handleUnauthorizedError();
			}
		});  
	});
});