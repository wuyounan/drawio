var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeGrid();
	initializeUI();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId : CommonTreeKind.SerialNumber,
		onClick : onFolderTreeNodeClick
	});
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.SerialNumber){
		parentId="";
		html.push('单据编号列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>单据编号列表');
	}
	$('#layout').layout('setCenterTitle',html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId:parentId});
	}
}

function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: addHandler,
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		moveHandler:moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 120, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 140, minWidth: 60, type: "string", align: "left" },		   
		{ display: "编码规则", name: "rule", width: 200, minWidth: 60, type: "string", align: "left" },		   
		{ display: "当前值", name: "currentValue", width: 100, minWidth: 60, type: "string", align: "left" },
		{ display: "最后更新日期", name: "lastModifiedDate", width: 140, minWidth: 60, type: "datetime", align: "left" }
		],
		dataAction: 'server',		
		url: web_app.name+'/codeBuildRule/slicedQueryCodeBuildRules.ajax',
		pageSize : 20,
		width : '100%',
		height : '100%',
		heightDiff : -8,
		sortName:'code',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight : true,
		selectRowButtonOnly : true,
		onDblClickRow : function(data, rowindex, rowobj) {
			updateHandler(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
	gridManager.loadData();
} 

function resetForm(obj) {
	$(obj).formClean();
}

function getParentId(){
	return $('#treeParentId').val();
}

function addHandler() {
	if(Public.isBlank(getParentId())){
		Public.tip('请选择类别树。'); 
		return;
	}
	
	UICtrl.showAjaxDialog({
		url: web_app.name + '/codeBuildRule/showInsertCodeBuildRule.load',
		title: "添加单据编号规则",
		width: 340, 
		ok: insert, 
		close: dialogClose
		});
}

function updateHandler(id){
	if(!id){
		var id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/codeBuildRule/showUpdateCodeBuildRule.load',
		title: "修改单据编号规则",
		width: 340, 
		param:{id:id}, 
		ok: update, 
		close: dialogClose
		});
}

function deleteHandler(){
	DataUtil.del({action:'codeBuildRule/deleteCodeBuildRules.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function insert() {
	var codeBuildRuleId=$('#codeBuildRuleId').val();
	if(codeBuildRuleId!='') return update();
	$('#submitForm').ajaxSubmit({url: web_app.name + '/codeBuildRule/insertCodeBuildRule.ajax',
		param:{folderId:$('#treeParentId').val()},
		success : function(id) {
			$('#codeBuildRuleId').val(id);
			refreshFlag = true;
		}
	});
}

function update(){
	$('#submitForm').ajaxSubmit({url: web_app.name + '/codeBuildRule/updateCodeBuildRule.ajax',
		success : function() {
			refreshFlag = true;
		}
	});
}

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'移动到...',kindId:CommonTreeKind.SerialNumber,
		save:function(parentId){
			DataUtil.updateById({action:'codeBuildRule/moveCodeBuildRules.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
}

function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}