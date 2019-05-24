var gridManager = null, refreshFlag = false, kindJSONData={}, uiElementOperationJSONData= {};
$(document).ready(function() {
	initializeGrid();
	initializeUI();
	
	kindJSONData = $("#kindId").combox("getJSONData");
	uiElementOperationJSONData = $("#uiElementOperation").combox("getJSONData");
});
function initializeUI(){
	UICtrl.initDefaultLayout();
	
	$('#maintree').commonTree({
		kindId: CommonTreeKind.UIElement,
		onClick: onFolderTreeNodeClick
	});
}
function onFolderTreeNodeClick(data,folderId) {
	var html=[], tempFolderId = folderId;
	if(folderId == CommonTreeKind.UIElement){
		tempFolderId = "";
		html.push('界面元素列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>界面元素列表');
	}
	$('#layout').layout('setCenterTitle',html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId: tempFolderId});
	}
}
//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: addHandler,
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		enableHandler: enableHandler,
		disableHandler: disableHandler,
		saveSortIDHandler: updateSequence,
		moveHandler:moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" },		   	
		{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" },		   	   
		{ display: "类别", name: "kindId", width: 80, minWidth: 60, type: "string", align: "left",
			render: function(item){
				return kindJSONData[item.kindId];
			}},		   
		{ display: "默认操作", name: "defaultOperationId", width:80, minWidth: 60, type: "string", align: "left",
			render: function(item){
				return uiElementOperationJSONData[item.defaultOperationId];
			}},	
		 { display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
				render: function (item) { 
					return UICtrl.getStatusInfo(item.status);
				} 
		},
		{ display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
		    render: function (item) {
		        return "<input type='text' id='txtSequence_" + item.id + "' class='textbox' value='" + item.sequence + "' />";
		    }
		}
		],
		dataAction: 'server',
		url: web_app.name+'/uiElement/slicedQueryUIElements.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		sortName: 'sequence',
		sortOrder:'asc',
		heightDiff: -8,
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		checkbox: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			updateHandler(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

// 查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}
//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
} 

function initDetailPage(){
	var defaultOperationId = $("#defaultOperationId").val();
	defaultOperationId = defaultOperationId ? defaultOperationId: "readonly";
	$("#defaultOperationId").combox();
	$("#defaultOperationId").combox("setData", uiElementOperationJSONData);
	$("#defaultOperationId").combox("setValue", defaultOperationId);
}

//添加按钮 
function addHandler() {
	var folderId=$('#treeParentId').val();
	if(folderId==''){
		Public.tip('请选择类别树。'); 
		return;
	}
	UICtrl.showAjaxDialog({
		title:'添加界面元素',
		url: web_app.name + '/uiElement/showUIElementDetail.load',
		param:{folderId: folderId},
		init: initDetailPage,
		width:320,
		ok: insert, 
		close: dialogClose,
	});
}

//编辑按钮
function updateHandler(id){
	if(!id){
		id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	UICtrl.showAjaxDialog({
		title:'修改界面元素',
		url: web_app.name + '/uiElement/loadUIElement.load', 
		param:{id:id},
		init: initDetailPage,
		width:320,
		ok: update, 
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'uiElement/deleteUIElements.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//新增保存
function insert() {
	var id=$('#id').val();
	if(id !='') {
		return update();
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/uiElement/insertUIElement.ajax',
		param:{foderId: $('#treeParentId').val()},
		success: function(id) {
			$('#id').val(id);
			refreshFlag = true;
		}
	});
}

function update(){
	$('#submitForm').ajaxSubmit({url: web_app.name + '/uiElement/updateUIElement.ajax',
		success: function() {
			refreshFlag = true;
			gridManager.loadData();
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

function updateSequence(){
	var action = "uiElement/updateUIElementsSequence.ajax";
    DataUtil.updateSequence({ action: action, gridManager: gridManager, onSuccess: reloadGrid });
}

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager, kindId: CommonTreeKind.UIElement,
		save:function(folderId){
			DataUtil.updateById({action:'uiElement/moveUIElements.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId: folderId},
				onSuccess:function(){
					reloadGrid();	
				}
			});
		}
	});
}

function updateUIElementsStatus(message, status){
	DataUtil.updateById({ action: 'uiElement/updateUIElementsStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{ status: status },
		message: message,
		onSuccess:function(){
			reloadGrid();	
		}
	});	
}

function enableHandler(){
	updateUIElementsStatus('确实要启用选中数据吗?', 1);
}

function disableHandler(){
	updateUIElementsStatus('确实要禁用选中数据吗?', 0);
}