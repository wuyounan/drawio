var iframeloading=null;
$(document).ready(function() {
	Public.isReadOnly=$('#isReadOnly').val()==='true';
	
	initializeUI();
	bindEvent();
});
function initializeUI(){
	 UICtrl.layout("#layout", {leftWidth : 250,heightDiff : -5});
	 iframeloading=$('#loadingDic');
	 var scrolldeltaHeight=25;
	 /*if(Public.isReadOnly){
	 	$('#divScreenOver').show().mousewheel(function(e,delta){
				var convertViewIFrame = $("iframe:first", $('#convertViewCenter'));
				if(convertViewIFrame.length>0){
					try{
						var doc=convertViewIFrame[0].contentWindow.document;
						var top=doc.body.scrollTop;
						doc.body.scrollTop=top-scrolldeltaHeight*delta;
					}catch(e){}
				}
	 	});
	 }*/
	 var attachmentId=$('#attachmentId').val();
	 if(attachmentId!=''){
	 	viewAttachment(attachmentId,null,$('#attachmentKind').val());
	 }
}

function bindEvent(){
	$('#divTreeArea').bind('click',function(e){
		var $clicked = $(e.target || e.srcElement);
		if($clicked.is('a.GridStyle')){
			$('div.divChoose',this).removeClass('divChoose');
			var id=$clicked.attr('id'),name=$.trim($clicked.text()),fileKind=$clicked.attr('fileKind');
			viewAttachment(id,name,fileKind);
			$clicked.parent().addClass('divChoose');
		}
	});
}
function viewAttachment(id,name,fileKind){
	if(name){
		$('#fileNameView').html(name);
	}
	var div=$('#convertViewCenter');
	AttachmentUtil.clearView(div);
	if(AttachmentUtil.isImgForThickbox(fileKind)){
		var src=web_app.name+'/attachment/downFile.ajax?id='+id;
		var pic=$("<img src='"+src+"'  border=0 />").appendTo(div);
		var maxWidth=div.width();
		UICtrl.autoResizeImage(pic,maxWidth*0.98);
	}else{
		var convertUrl=$('#convertUrl').val();
		var method='/attachment.do?method=convertAttachment&attachmentId='+id+'&a='+new Date().getTime();
		method+="&isReadOnly="+(Public.isReadOnly?"true":"false");//是否只读
		//增加手机访问支持
		var convertForPhone=$('#convertForPhoneId').val();
		if(convertForPhone==='true'){
			method+="&convertForPhone=true";
		}
		var showUrl=convertUrl+method;
		AttachmentUtil.addConvertPreviewFileIFrame(div,showUrl);
	}
}


function downFile(){
	var file=$('#divTreeArea').find('div.divChoose');
	var id = null;
	if(file.length>0){
		id =file.find('a.GridStyle').attr('id');
	}else{
	   id=$('#attachmentId').val();
	}
	AttachmentUtil.downFileByAttachmentId(id);
}