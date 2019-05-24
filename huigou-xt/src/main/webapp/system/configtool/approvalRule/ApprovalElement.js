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
		{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" },		   
		{ display: "类别", name: "kindId", width: 60, minWidth: 60, type: "string", align: "center",
			render: function(item){
				if (parseInt(item.kindId) ==  1){
					return "系统";
				}
				return "业务";
			}
		},		   
		{ display: "数据源", name: "dataSourceConfig", width: 600, minWidth: 60, type: "string", align: "left",
			render:function(item){
				if (item.dataSourceConfig){
					return $.toJSON(item.dataSourceConfig);
				} 
		}},		
		{ display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left" ,		   
			render: function(item){
				return "<input type='text' id='txtSequence_" + item.id + "' class='textbox' value='" + item.sequence + "' />";
			}
		}  
		],
		dataAction: 'server',
		url: web_app.name+'/approvalRule/slicedQueryApprovalElements.ajax',
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
		title: "添加审批要素",
		width: 500,
		url: web_app.name + '/approvalRule/showInsertApprovalElement.load',
		ok: insert,
		close: dialogClose
	});
}

function showUpdateHandler(id){
	UICtrl.showAjaxDialog({
		title: "修改审批要素",
		url: web_app.name + '/approvalRule/showUpdateApprovalElement.load',
		width: 500, param:{ id: id }, ok: update, close: dialogClose});
}

function updateHandler(){
	var id = DataUtil.getUpdateRowId(gridManager);
	if (!id){return;}
	showUpdateHandler(id);
}

function deleteHandler(){
	var action = '/approvalRule/deleteApprovalElement.ajax';
	DataUtil.del({ action: action, gridManager: gridManager, onSuccess: reloadGrid });
}

//新增保存
function insert() {
	_self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/approvalRule/insertApprovalElement.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

//编辑保存
function update(){
	_self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/approvalRule/updateApprovalElement.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

function updateSequence(){
	var action = "approvalRule/updateApprovalElementsSequence.ajax";
	DataUtil.updateSequence({ action: action, gridManager: gridManager,  onSuccess: reloadGrid });
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}
