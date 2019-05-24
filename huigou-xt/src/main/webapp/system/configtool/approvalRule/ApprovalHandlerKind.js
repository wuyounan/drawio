var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeGrid();
});

//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ addHandler: addHandler, 
		updateHandler: updateHandler, deleteHandler: deleteHandler,
		saveSortIDHandler: updateSequence });
	gridManager = UICtrl.grid('#maingrid', {
		columns: [		   
		{ display: "编码", name: "code", width: 120, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 120, minWidth: 60, type: "string", align: "left" },
		{ display: "数据源类型", name: "dataSourceIdTextView", width: 120, minWidth: 60, type: "string", align: "left" },	
		{ display: "数据来源", name: "dataSourceConfig", width: 600, minWidth: 60, type: "string", align: "left",
			render:function(item){
				if(item.dataSourceConfig){
					return $.toJSON(item.dataSourceConfig);
				}
			}},	
		{ display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left" ,		   
			render: function(item){
				return UICtrl.sequenceRender(item);
			}
		}  
		],
		dataAction: 'server',
		url: web_app.name+'/approvalRule/slicedQueryApprovalHandlerKinds.ajax',
		pageSize: 20,
		width: '99.8%',
		height: '100%',
		sortName: 'sequence',
		sortOrder: 'asc',
		heightDiff: -8,
		checkbox: true,
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			showUpdateHandler(data.id);
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
		title: "添加审批处理人类别",
		width: 500,
		url: web_app.name + '/approvalRule/showInsertApprovalHandlerKind.load',
		ok: insert,
		close: dialogClose
	});
}

function updateHandler(){
	var id=DataUtil.getUpdateRowId(gridManager);
	if(!id){return;}
	showUpdateHandler(id);
}

function showUpdateHandler(id){
	UICtrl.showAjaxDialog({
		title: "修改审批处理人类别",
		url: web_app.name + '/approvalRule/showUpdateApprovalHandlerKind.load',
		width: 500, param:{ id: id }, ok: update, close: dialogClose});
}

function deleteHandler(){
	var action = '/approvalRule/deleteApprovalHandlerKind.ajax';
	DataUtil.del({ action: action, gridManager: gridManager, onSuccess: reloadGrid });
}

//新增保存
function insert() {
	_self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/approvalRule/insertApprovalHandlerKind.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

//编辑保存
function update(){
	_self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/approvalRule/updateApprovalHandlerKind.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

function updateSequence(){
	var action = "approvalRule/updateApprovalHandlerKindsSequence.ajax";
	DataUtil.updateSequence({ action: action, gridManager: gridManager,  onSuccess: reloadGrid });
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}
