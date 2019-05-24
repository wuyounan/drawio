var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeGrid();
});

//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "类别", name: "suggestionKindTextView", width: 100, minWidth: 60, type: "string", align: "left" },		   
		{ display: "功能", name: "funName", width: 200, minWidth: 60, type: "string", align: "left" },
		{ display: "提交人", name: "personName", width: 100, minWidth: 60, type: "string", align: "left" },	
		{ display: "创建时间", name: "createTime", width: 130, minWidth: 60, type: "datetime", align: "left" },
		{ display: "内容", name: "content", width: 400, minWidth: 60, type: "string", align: "left" }
		],
		dataAction : 'server',
		url: web_app.name+'/suggestionBox/slicedQuery.ajax',
		pageSize : 20,
		width : '100%',
		height : '100%',
		heightDiff : -5,
		sortName:'createTime',
		sortOrder:'desc',
		toolbar: toolbarOptions,
		fixedCellHeight : true,
		selectRowButtonOnly : true,
		onDblClickRow : function(data, rowindex, rowobj) {
			updateHandler(data.boxId);
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


//编辑按钮
function updateHandler(boxId){
	if(!boxId){
		var row = gridManager.getSelectedRow();
		if (!row) {Public.tip('请选择数据！'); return; }
		boxId=row.boxId;
	}
	//所需参数需要自己提取 {id:row.id}
	UICtrl.showAjaxDialog({url: web_app.name + '/suggestionBox/showUpdate.load', param:{boxId:boxId},width:500, ok: update, close: dialogClose});
}

//删除按钮
function deleteHandler(){
	var row = gridManager.getSelectedRow();
	if (!row) {Public.tip('请选择数据！'); return; }
	UICtrl.confirm('确定删除吗?',function(){
		//所需参数需要自己提取 {id:row.id}
		Public.ajax(web_app.name + '/suggestionBox/delete.ajax', {boxId:row.boxId}, function(){
			reloadGrid();
		});
	});
}

//编辑保存
function update(){
	$('#suggestionBoxForm').ajaxSubmit({url: web_app.name + '/suggestionBox/update.ajax',
		success : function() {
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
