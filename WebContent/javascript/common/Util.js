function startLoading() {	
	// loading
	$.modal('<p style=\"text-align:center;color:#FFF;\">Carregando...<p><img src=\"' + getLoadingImg() + '\" />', 
			{overlayCss: {backgroundColor:"#000"}});
};

$.fn.exists = function () {
    return this.length !== 0;
};