var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeGrid();
});

//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: addHandler,
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		enableHandler: enableHandler,
		disableHandler: disableHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 140, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 140, minWidth: 60, type: "string", align: "left" },		   
		{ display: "参数", name: "params", width: 300, minWidth: 60, type: "string", align: "left" },
	    { display: "序号", name: "sequence", width: 80, minWidth: 60, type: "string", align: "left"},		   
		{ display: "备注", name: "remark", width: 200, minWidth: 60, type: "string", align: "left" },		  
		{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "left",
			render: function (item) { 
				return UICtrl.getStatusInfo(item.status);
			}
		}
		],
		dataAction : 'server',
		url: web_app.name+'/parameter/slicedQueryMessageKind.ajax',
		pageSize : 20,
		width : '99.5%',
		height : '100%',
		heightDiff : -10,
		sortName:'sequence',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		fixedCellHeight : true,
		selectRowButtonOnly : true,
		onDblClickRow : function(data, rowindex, rowobj) {
			updateHandler(data.messageKindId);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

// 查询
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

//添加按钮 
function addHandler() {
	UICtrl.showAjaxDialog({
		url: web_app.name + '/parameter/showInsertMessageKind.load',
		title: "添加消息发送类别",
		width:400, 
		ok: saveMessageKind, 
		close: dialogClose
	});
}

//编辑按钮
function updateHandler(messageKindId){
	if(!messageKindId){
		var row = gridManager.getSelectedRow();
		if (!row) {Public.tip('请选择数据！'); return; }
		messageKindId=row.messageKindId;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/parameter/showUpdateMessageKind.load',
		title: "修改消息发送类别",
		width:400, 
		param:{messageKindId:messageKindId}, 
		ok: saveMessageKind, 
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'parameter/deleteMessageKind.ajax',
		gridManager:gridManager,idFieldName:'messageKindId',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function saveMessageKind() {
	$('#submitForm').ajaxSubmit({url: web_app.name + '/parameter/saveMessageKind.ajax',
		success : function(id) {
			$('#messageKindId').val(id);
			refreshFlag = true;
		}
	});
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}
//启用
function enableHandler(){
	DataUtil.updateById({ action: 'parameter/updateMessageKindStatus.ajax',
		gridManager: gridManager,idFieldName:'messageKindId', param:{status:1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'parameter/updateMessageKindStatus.ajax',
		gridManager: gridManager,idFieldName:'messageKindId',param:{status:-1},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
