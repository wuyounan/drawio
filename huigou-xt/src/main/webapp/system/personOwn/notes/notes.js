var zIndex = 0;
$(document).ready(function(){
	$('html').addClass("overflow-x");
    var mainPanel=$('#notesPanel'),menu_bottom=$('#menu_bottom'),tmp,
    rootEl=document.compatMode=='CSS1Compat'?document.documentElement:document.body;
	//初始化显示面板高度
	function setHeight(){
		var height=rootEl.clientHeight-menu_bottom.outerHeight();
		if(height<400) height=400;
		mainPanel.height(height);
	}
	setHeight();
	setInterval(setHeight,10);
	//计算但前最大的z-index
	$('div.note').each(function(){
		tmp = $(this).css('z-index');
		if(tmp>zIndex) zIndex = tmp;
	});
	init_draggable();
	init_add_dialog();
	$('#doDelete').droppable({
		accept: "div.note",
		hoverClass: "op-state-delete-hover",
		drop: function( event, ui ) {
		    $.get(web_app.name+'/personOwn/delNote.ajax',{id:ui.draggable.attr('id')});
			ui.draggable.hide();
		}
	});
});
//初始化便签拖动事件
function init_draggable(){
	$('div.note').draggable({
		opacity: 0.6,
		containment:'parent',
		start:function(e,ui){ ui.helper.css('z-index',++zIndex);$('#doDelete').show();},
		stop:function(e,ui){
			if(ui.helper.is(':hidden')){
				ui.helper.remove();
				$('#doDelete').hide();
				return false;
			}
			$.get(web_app.name+'/personOwn/updateNote.ajax',{
				  offestx	: ui.position.left,
				  offesty	: ui.position.top,
				  zindex	: zIndex,
				  id		: parseInt(ui.helper.attr('id'))
			});
			$('#doDelete').hide();
		}
	});
}
function init_add_dialog(){
	$("#showColor").bind('click',function(e){
		var $clicked = $(e.target || e.srcElement);
		$(this).find('li').removeClass('cur');
		if($clicked.is('li')){
			var color = $clicked.attr("class");
			$("#add_color").val(color);
			$clicked.addClass('cur');
		}
	});
	$('#add_content').maxLength({maxlength:'300'});

}
function show_add_dialog(){
	UICtrl.showAjaxDialog({url:web_app.name+'/personOwn/toAddPage.load',title:'新增便签',ok: addNote,width:420,init:init_add_dialog});
}

function addNote(){
	var content = $("#add_content").val();
	var color = $("#add_color").val();
	if(content==""){
		$("#showmsg").html("内容不能为空!");
		$("#add_content").focus();
		return false;
	}
	var _self=this;
	var data ={zindex: ++zIndex,content:encodeURI(content),color:color,offestx:0,offesty:0};
	Public.ajax(web_app.name+'/personOwn/addNote.ajax', data, function(msg) {
		zIndex = zIndex++;
		var id=parseInt(msg);
		if(!isNaN(id)){
			var note=["<div class='note "+color+"' style='left:0px;top:0px;z-index:"+zIndex+"' id='"+id+"'>"];
			note.push("<div class='content'>"+content+"</div>");
			note.push("<div class='date'>--&nbsp;刚刚</div>");
			note.push("</div>");
			$("#notesPanel").append(note.join(''));
			init_draggable();
			_self.close();
		}else{
			$("#showmsg").html(msg);
		}	
	});
}