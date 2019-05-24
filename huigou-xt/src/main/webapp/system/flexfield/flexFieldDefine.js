var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.FlexFieldDefine,
		onClick: onFolderTreeNodeClick
	});
}
//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		addHandler: addHandler, 
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		copyHandler: copyHandler,
		moveHandler:moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		 columns: [
			{ display: "英文名称", name: "fieldName", width: 200, minWidth: 60, type: "string", align: "left" },
			{ display: "中文名称", name: "description", width: 200, minWidth: 60, type: "string", align: "left" },
			{ display: "字段类型", name: "fieldTypeTextView", width: 100, minWidth: 60, type: "string", align: "left"},
			{ display: "字段长度", name: "fieldLength", width: 100, minWidth: 60, type: "string", align: "left" },
			{ display: "字段精度", name: "fieldPrecision", width: 100, minWidth: 60, type: "string", align: "left" },
			{ display: "控件类型", name: "controlTypeTextView", width: 100, minWidth: 60, type: "string", align: "left"}
		],
		dataAction: 'server',
		url: web_app.name+'/flexField/slicedQueryFlexFieldDefinitions.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -8,
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true, 
		onDblClickRow: function(data, rowindex, rowobj) {
			updateHandler(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}
function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.FlexFieldDefine){
		parentId="";
		html.push('扩展字段列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>扩展字段列表');
	}
	$('.l-layout-center .l-layout-header').html(html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId: parentId});
	}
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
	var parentId=$('#treeParentId').val();
	if(parentId==''){
		Public.tip('请选择类别树。'); 
		return;
	}
	UICtrl.showAjaxDialog({title:'新增扩展字段',
		 url: web_app.name + '/flexField/showInsertFlexFieldDefinition.load',
		 width:880, 
		 init:initDialog, 
		 ok: insert, 
		 close: dialogClose});
}

//编辑按钮
function updateHandler(id){
	if(!id){
		id=DataUtil.getUpdateRowId(gridManager);
	}
	//所需参数需要自己提取 {id:row.id}
	UICtrl.showAjaxDialog({title:'编辑扩展字段',
		url: web_app.name + '/flexField/loadFlexFieldDefinition.load',
		width:880, 
		param:{ id: id }, 
		init:initDialog,
		ok: update, 
		close: dialogClose});
}

function initDialog(doc){
	var controlType=$('#editControlType').val();
	if(controlType==2||controlType==6||controlType==7){
		$('#editDataSourceKindId').combox('enable');
	}
	$('#editControlType').combox({onChange:function(v){
		if(v.value==2||v.value==6||v.value==7){
			$('#editDataSourceKindId').combox('enable');
		}else{
			$('#editDataSourceKindId').combox('setValue','1');
			$('#editDataSourceKindId').combox('disable');
		}
		if(v.value==8||v.value==9||v.value==10){
			$('#editDataSourceKindId').combox('setValue','4');
		}
	}});
}
//删除按钮
function deleteHandler(){
	DataUtil.del({action:'flexField/deleteFlexFieldDefinitions.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//新增保存
function insert() {
	var id=$('#id').val();
	if(id!='') {
		return update();
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/flexField/insertFlexFieldDefinition.ajax',
		param:{folderId: $('#treeParentId').val()},
		success: function(data) {
			$('#id').val(data);
			refreshFlag = true;
		}
	});
}

//编辑保存
function update(){
	$('#submitForm').ajaxSubmit({url: web_app.name + '/flexField/updateFlexFieldDefinition.ajax',
		success: function() {
			refreshFlag = true;
		}
	});
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag = false;
	}
}
//复制新增
function copyHandler(){
	var row = gridManager.getSelectedRow();
	if (!row) {Public.tip('请选择数据。'); return; }
	//所需参数需要自己提取 {id:row.id}
	UICtrl.showAjaxDialog({
		title:'编辑扩展字段',url: web_app.name + '/flexField/loadFlexFieldDefinition.load',
		width:880, 
		param:{id: row.id},
		init:function(doc){
			$('#id').val('');
			initDialog(doc);
		},
		ok: insert, 
		close: dialogClose
	});
}

//移动
function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'移动扩展字段',kindId:CommonTreeKind.FlexFieldDefine,
		save:function(parentId){
			DataUtil.updateById({action:'flexField/moveFlexFieldDefinitions.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
}
