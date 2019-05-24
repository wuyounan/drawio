var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeGrid();
});

//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ addHandler:add, deleteHandler: deleteHandler,delAll:{
		id: 'delAll', text:'删除全部', click:delAll, img:'page_cross.gif'
	}});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		   	{ display: "背景颜色", name: "color", width: 100, minWidth: 60, type: "string", align: "center",
		   		render: function (item) {
		   			return '<span class="color '+item['color']+'">&nbsp;</span>';
				}
		   	},		   
		   	{ display: "内容", name: "content", width: 500, minWidth: 60, type: "string", align: "left" },
		   	{ display: "创建时间", name: "createDate", width: 100, minWidth: 60, type: "string", align: "left" }
		],
		dataAction : 'server',
		url: web_app.name+'/personOwn/slicedQueryPersonNotes.ajax',
		pageSize : 20,
		sortName:'id',
		sortOrder:'desc',
		width : '100%',
		height : '100%',
		heightDiff : -5,
		toolbar: toolbarOptions,
		fixedCellHeight : true,
		selectRowButtonOnly : true,
		checkbox:true,
		onDblClickRow : function(data, rowindex, rowobj) {
			
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

//查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
} 

//重置表单
function resetForm(obj) {
	$(obj).formClean();
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
function add(){
	UICtrl.showAjaxDialog({url:web_app.name+'/personOwn/toAddPage.load',title:'新增便签',ok: addNote,width:420,init:init_add_dialog});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'personOwn/delNote.ajax',
		gridManager:gridManager,
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}
//删除全部
function delAll() {
	UICtrl.confirm("此操作不能回退，确信要删除全部数据吗？", function() {
		Public.ajax(web_app.name+'/personOwn/doDeleteAll.ajax', {}, function() {
			reloadGrid();
		});
	});
}
//新增保存
function addNote(){
	var content = $("#add_content").val();
	var color = $("#add_color").val();
	if(content==""){
		$("#showmsg").html("内容不能为空!");
		$("#add_content").focus();
		return false;
	}
	var _self=this;
	var data ={content:encodeURI(content),color:color,offestx:0,offesty:0};
	Public.ajax(web_app.name+'/personOwn/addNote.ajax', data, function(msg) {
		reloadGrid();
		_self.close();
	});
	
}
//返回图形显示页面
function goPicView(){
	window.location.href=web_app.name+'/personOwn/toMainPage.do?timeStamp='+ new Date().getTime();
}