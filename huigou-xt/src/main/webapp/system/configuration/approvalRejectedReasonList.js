var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function getApprovalRejectedReasonKind(){
	var kind=$('#approvalRejectedReasonKind').val();
	if (Public.isBlank(kind)) {
		return 'common';
	}
	return kind;
}

function initializeUI(){
	UICtrl.initDefaultLayout();
	var kind=getApprovalRejectedReasonKind();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.ApprovalRejectedReason,
		onClick: onFolderTreeNodeClick,
		parentId:kind
	});
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[],treeFolderId=folderId;
	if(folderId==CommonTreeKind.ApprovalRejectedReason){
		treeFolderId="";
		html.push($.i18nProp('job.approval.rejected.reason'));
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>',$.i18nProp('job.approval.rejected.reason'));
	}
	$('#layout').layout('setCenterTitle',html.join(''));
	$('#treeFolderId').val(treeFolderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId:treeFolderId});
	}
}

function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: addHandler,
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		saveSortIDHandler: saveSortIDHandler,
		moveHandler:moveHandler
	});
	var kind=getApprovalRejectedReasonKind();
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		//驳回理由类别
		{ display: "common.field.kind", name: "folderName", width: 200, minWidth: 60, type: "string", align: "left" },		   
		{ display: "common.field.rejectedReason", name: "content", width: 300, minWidth: 60, type: "string", align: "left" },
		{ display: "common.field.sequence", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left" ,
			render : function(item) {
				return UICtrl.sequenceRender(item);
			}  
		},
		],
		dataAction: 'server',
		url: web_app.name+'/approvalRejectedReason/slicedQueryApprovalRejectedReason.ajax',
		parms:{rejectedReasonKind:kind},
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -5,
		sortName:'sequence',
		sortOrder:'asc',
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
	var folderId=$('#treeFolderId').val();
	if(folderId==''){
		Public.tip('common.warning.not.kindtree');
		return;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/approvalRejectedReason/showInsertApprovalRejectedReason.load',
		title:$.i18nProp('common.field.add.title',$.i18nProp('job.approval.rejected.reason')),
		width: 410, 
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
		url: web_app.name + '/approvalRejectedReason/showLoadApprovalRejectedReason.load',
		title:$.i18nProp('common.field.modif.title',$.i18nProp('job.approval.rejected.reason')), 
		param:{id:id}, 
		width: 410,
		ok: update, 
		close: dialogClose
	});
}

function deleteHandler(){
	DataUtil.del({action:'approvalRejectedReason/deleteApprovalRejectedReason.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function insert() {
	var kind=getApprovalRejectedReasonKind();
	var _self=this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/approvalRejectedReason/insertApprovalRejectedReason.ajax',
		param:{folderId:$('#treeFolderId').val(),rejectedReasonKind:kind},
		success: function(id) {
			refreshFlag = true;
			_self.close();
		}
	});
}

function update(){
	var _self=this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/approvalRejectedReason/updateApprovalRejectedReason.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

function moveHandler(){
	var kind=getApprovalRejectedReasonKind();
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'common.button.move',kindId:CommonTreeKind.ApprovalRejectedReason,parentId:kind,
		save:function(parentId){
			DataUtil.updateById({action:'approvalRejectedReason/moveApprovalRejectedReason.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
}

//保存排序号
function saveSortIDHandler(){
	var action = "approvalRejectedReason/updateApprovalRejectedReasonSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager, onSuccess: function(){
		reloadGrid();
	}});
}

function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}