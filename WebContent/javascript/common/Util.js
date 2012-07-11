function startLoading() {	
	// loading
	$.modal('<p style=\"text-align:center;color:#FFF;\">Carregando...<p><img src=\"' + getLoadingImg() + '\" />', 
			{overlayCss: {backgroundColor:"#000"}});
};

$.fn.exists = function () {
    return this.length !== 0;
};

function isEmailValid(sEmail) {
    var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    if (filter.test(sEmail)) {
        return true;
    }
    else {
        return false;
    }
};