var gridManager = null, refreshFlag = false, orgKindList;
$(document).ready(function() {
	initializeUI();
	initializeGrid();
	
	function initializeUI(){
		orgKindList = $("#orgKindId").combox("getJSONData");
	}
});

//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ addHandler: addHandler, 
		updateHandler: updateHandler, deleteHandler: deleteHandler,
		saveSortIDHandler: updateSequence });
	gridManager = UICtrl.grid('#maingrid', {
		columns: [		   
		{ display: "组织类别", name: "orgKindId", width: 120, minWidth: 60, type: "string", align: "left",
			render: function(item){
				return orgKindList[item.orgKindId];
			}
		},		   
		{ display: "属性名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" },		   
		{ display: "属性描述", name: "description", width: 150, minWidth: 60, type: "string", align: "left" },		   
		{ display: "数据源", name: "dataSource", width: 460, minWidth: 60, type: "string", align: "left",
			render:function(item){
				if (item.dataSource){
					return $.toJSON(item.dataSource);
				} 
			}},	
		{ display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left" ,		   
			render: function(item){
				return "<input type='text' id='txtSequence_" + item.id + "' class='textbox' value='" + item.sequence + "' />";
			}
		}  
		],
		dataAction: 'server',
		url: web_app.name+'/org/queryOrgPropertyDefinitions.ajax',
		pageSize: 20,
		width: '99.8%',
		height: '100%',
		sortName: 'org_kind_id,sequence',
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
		title: "添加组织属性定义",
		width: 400,
		url: web_app.name + '/org/showInsertOrgPropertyDefinition.load',
		ok: doSave,
		close: dialogClose
	});
}

function showUpdateHandler(id){
	UICtrl.showAjaxDialog({
		title: "修改组织属性定义",
		url: web_app.name + '/org/showUpdateOrgPropertyDefinition.load',
		width: 400, 
		param:{ id: id }, 
		ok: doSave, 
		close: dialogClose
	});
}

function updateHandler(){
	var id = DataUtil.getUpdateRowId(gridManager);
	if(!id){return;}
	showUpdateHandler(id);
}

function deleteHandler(){
	var action = '/org/deleteOrgPropertyDefinitions.ajax';
	DataUtil.del({ action: action, gridManager: gridManager, onSuccess: reloadGrid });
}

function doSave(){
	_self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/org/saveOrgPropertyDefinition.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

function updateSequence(){
	var action = "org/updateOrgPropertyDefinitionsSequence.ajax";
	DataUtil.updateSequence({ action: action, gridManager: gridManager,  onSuccess: reloadGrid });
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}
