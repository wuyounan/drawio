var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.Parameter,
		onClick: onFolderTreeNodeClick
	});
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.Parameter){
		parentId="";
		html.push($.i18nProp('system.parameter.gridtitle'));
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>',$.i18nProp('system.parameter.gridtitle'));
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
			updateParameter();
		},
		deleteHandler: deleteHandler,
		moveHandler:moveHandler,
		syncCache:{id:'syncCache',text:'common.button.sync',img:'fa-link',click:function(){
			Public.ajax(web_app.name + "/parameter/syncCacheSysParameter.ajax");
		}},
		updateSysStartTime:{id:'updateSysStartTime',text:'刷新资源版本时间',img:'fa-telegram',click:function(){
			Public.ajax(web_app.name + "/updateSysStartTime.ajax");
		}}
	});
	
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "common.field.code", name: "code", width: 200, minWidth: 60, type: "string", align: "left" },		   
		{ display: "common.field.name", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },		   
		{ display: "common.field.value", name: "value", width: 220, minWidth: 60, type: "string", align: "left" },		   
		{ display: "common.field.remark", name: "remark", width: 300, minWidth: 60, type: "string", align: "left" }
		],
		dataAction: 'server',
		url: web_app.name+'/parameter/slicedQuerySysParameters.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -5,
		sortName:'code',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			updateParameter(data.id);
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

function addHandler() {
	var parentId=$('#treeParentId').val();
	if(parentId==''){
		Public.tip('common.warning.not.kindtree');
		return;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/parameter/showInsertSysParameter.load',
		title:$.i18nProp('common.field.add.title',$.i18nProp('system.parameter.title')),
		width: 500, 
		ok: insert,
		close: dialogClose
	});
}

function updateParameter(id){
	if(!id){
		var id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}

	UICtrl.showAjaxDialog({
		url: web_app.name + '/parameter/showUpdateSysParameter.load',
		title:$.i18nProp('common.field.modif.title',$.i18nProp('system.parameter.title')), 
		param:{id:id}, 
		width: 500,
		ok: update, 
		close: dialogClose
	});
}

function deleteHandler(){
	DataUtil.del({action:'parameter/deleteSysParameters.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function insert() {
	var parameterId=$('#parameterId').val();
	if(parameterId!='') {
		return update();
	}
	var _self=this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/parameter/insertSysParameter.ajax',
		param:{folderId:$('#treeParentId').val()},
		success: function(id) {
			$('#parameterId').val(id);
			refreshFlag = true;
			_self.close();
		}
	});
}

function update(){
	var _self=this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/parameter/updateSysParameter.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'common.button.move',kindId:CommonTreeKind.Parameter,
		save:function(parentId){
			DataUtil.updateById({action:'parameter/moveSysParameters.ajax',
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