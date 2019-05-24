$(function(){ 
	initEvent();
	initPasswordEvent();
	var codeId=Public.getQueryStringByName("codeId");
	if(!Public.isBlank(codeId)){
		showPanel(codeId);
	}
});

function initEvent(){
	//导航条
	$('#userPanelNavbar').on('click',function(e){
		var $clicked = $(e.target || e.srcElement);
		if($clicked.is('i.fa')) $clicked=$clicked.parent();
		if($clicked.is('a')){
			showPanel($clicked.data('id'));
		}
	});
	
	//操作员照片
	var _img=$('#showPersonPicture');
	$('#addPicture').uploadButton({
		filetype: ['jpg','gif','jpeg','png','bmp'],
		afterUpload: function(data){
			_img[0].src = $.getCSRFUrl('attachmentDownFile.ajax',{id:data.id});
		},
		param: function(){
			var id = $('#id').val();
			if(id == ''){
				return false;
			}
			return {bizCode: 'PersonPicture', bizId: id, flag: 'false', deleteOld: 'true', returnPath: 'true'};
		}
	});
	
}


function showPanel(divId){
	var div=$('#'+divId);
	if(div.is(':visible')){
		return;
	}
	$('div.panel').addClass('ui-hide');
	div.removeClass('ui-hide');
	$('#userPanelNavbar').find('li').removeClass('active');
	$('#userPanelNavbar').find('a').each(function(){
		if($(this).data('id')==divId){
			$(this).parent().addClass('active');
		}
	});
	if($.isFunction(window['init'+divId])){
		window['init'+divId].call(window);
	}
}

function saveUserInfo(){
	$('#updatePersonForm').ajaxSubmit({url : web_app.name + '/personOwn/saveUsercontrolInfo.ajax'});
}