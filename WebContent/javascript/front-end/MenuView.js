$(document).ready(function() {
	  $(".viewProd").live("click", function() {
		  var product = $(this).attr("href").split("-")[1];
		  $("div#MainArea").load('PageLoader?page=ProductView&prodId=' + product);
	  });
});