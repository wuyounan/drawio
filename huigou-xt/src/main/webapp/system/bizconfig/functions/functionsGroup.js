$(document).ready(function () {
    $('#functionGroupDiv').on('click',function(e){
    	var $clicked = $(e.target || e.srcElement);
    	if($clicked.is('i.fa')) $clicked=$clicked.parent();
    	if($clicked.is('button')){
    		if($clicked.hasClass('disabled')){
    			return false;
    		}
    		var url=$clicked.data('url'),code=$clicked.data('code'),name=$clicked.data('name');
    		if (Public.isBlank(url)) {
				return;
			}
    		if(!url.startsWith('http')){
    			url=web_app.name+ "/" +url;
    		}
    		url+=(/\?/.test(url) ? '&' : '?')+ $.param({functionCode:code});
			UICtrl.addTabItem({tabid: 'func' + code,text:name,url: url});
    	}
    });
});
