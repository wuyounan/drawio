var gridManager = null, refreshFlag = false;
var userGroupDetailGridManager=null;

$(document).ready(function() {
	initializeGrid();
	initializeUI();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.UserGroupKind,
		onClick: onFolderTreeNodeClick
	});
}
function onFolderTreeNodeClick(data, folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.UserGroupKind){
		parentId="";
		html.push('分组列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>分组列表');
	}
	$("#layout").layout("setCenterTitle", html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId:parentId});
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
		saveSortIDHandler: saveSortIDHandler,
		moveHandler:moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left" },		   	   	   
		{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },			
		{ display: "序号", name: "sequence", width: 80, minWidth: 60, type: "string", align: "left",
			render: function (item) { 
				return UICtrl.sequenceRender(item,'id');
			}
		},		   
		{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "left",
			render: function (item) { 
				return UICtrl.getStatusInfo(item.status);
			}
		},
		{ display: "备注", name: "remark", width: 350, minWidth: 60, type: "string", align: "left" }
		],
		dataAction: 'server',
		url: web_app.name+'/userGroup/slicedQueryUserGroups.ajax',
		parms:{kindId:'SYSTEM'},
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -8,
		sortName:'sequence',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		checkbox:true,
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
	var folderId=$('#treeParentId').val();
	if(folderId==''){
		Public.tip('请选择类别树。'); 
		return;
	}
	UICtrl.showAjaxDialog({
		title: '新增分组',
		url: web_app.name + '/userGroup/showInsertUserGroup.load',
		id:'editAjaxDialog',
		param: {folderId: folderId},
		width:800,
		init:initDetailPage,
		ok: save,
		close: dialogClose
	});
}

//编辑按钮
function updateHandler(id){
	if(!id){
		id = DataUtil.getUpdateRowId(gridManager);
		if(!id){return;}
	}
	UICtrl.showAjaxDialog({
		title:'编辑分组',
		url: web_app.name + '/userGroup/showUpdateUserGroup.load', 
		id:'editAjaxDialog',
		param:{id: id},
		width:800,
		init:initDetailPage,
		ok: save,
		close: dialogClose
	});
}

function initDetailPage(){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		addHandler: addUserGroupDetail, 
		deleteHandler: deleteDetailHandler
	});
	userGroupDetailGridManager = UICtrl.grid('#userGroupDetailGrid', {
		columns: [
		{ display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },		   	   	   
		{ display: "路径", name: "fullName", width: 300, minWidth: 60, type: "string", align: "left" },			   
		{ display: "类别", name: "orgKindId", width: 60, minWidth: 60, type: "string", align: "left",
			 render: function (item) {
                   return OpmUtil.getOrgKindDisplay(item.orgKindId);
             }
		},
		{ display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
			editor: { type:'spinner',required:true,min:1,max:100,mask:'nnn'}
		}, 
		{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.status);
			} 
		}
		],
		dataAction: 'server',
		url: web_app.name+'/userGroup/slicedQueryUserGroupDetails.ajax',
		parms:{groupId: $('#id').val()},
		pageSize: 20,
		enabledEdit: true,
		width: '99%',
		height: '100%',
		heightDiff: -100,
		sortName:'sequence',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		checkbox:true,
		onLoadData:function(){
			return $('#id').val()!='';
		}
	});
	UICtrl.createQueryBtn($('.ui-toolbar',$('#userGroupDetailGrid').parent()), function (value) {
        UICtrl.gridSearch(userGroupDetailGridManager, { name: Public.encodeURI(value)});
    });
}

function deleteHandler(){
	DataUtil.delSelectedRows({action:'userGroup/deleteUserGroups.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function save() {
	var param={};
	var insert = false;
	if($('#id').val()==''){
		insert = true;
		param['folderId']=$('#treeParentId').val();
	}
	var detailData = DataUtil.getGridData({gridManager: userGroupDetailGridManager});
	if(!detailData){
		return false;
	}
	param['detailData']=Public.encodeJSONURI(detailData);
	param['userGroupKind'] = "SYSTEM";
	$('#submitForm').ajaxSubmit({ url: web_app.name +(insert ? '/userGroup/insertUserGroup.ajax': '/userGroup/updateUserGroup.ajax'),
		param:param,
		success: function(data) {
			if($('#id').val()==''){
				$('#id').val(data);
				UICtrl.gridSearch(userGroupDetailGridManager, {groupId:$('#id').val()});
			}else{
				userGroupDetailGridManager.loadData();
			}
			refreshFlag = true;
		}
	});
}

function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}

function saveSortIDHandler(){
	var action = "userGroup/updateUserGroupsSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager,idFieldName:'id', onSuccess: function(){
		reloadGrid(); 
	}});
}

function updateUserGroupStatus(status){
	var message = "您要启用选中数据吗?";
	if (status == 0){
		 message = "您要禁用选中数据吗?";	
	}
	DataUtil.updateById({ action: 'userGroup/updateUserGroupsStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: status},
		message: message,
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

function enableHandler(){
	updateUserGroupStatus(1);
}

function disableHandler(){
	updateUserGroupStatus(0);
}

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'移动类别',kindId:CommonTreeKind.UserGroupKind,
		save:function(parentId){
			DataUtil.updateById({action:'userGroup/moveUserGroups.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();	
				}
			});
		}
	});
}

function addUserGroupDetail(){
	var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
	selectOrgParams['selectableOrgKinds']='dpt,pos,psm';
	var options = { params: selectOrgParams,title: "选择组织",
		parent:window['editAjaxDialog'],
		confirmHandler: function(){
			var data = this.getSelectedData();
			if (data.length == 0) {
				Public.errorTip("请选择数据。");
				return;
			}
			var addRows=[];
			var length = userGroupDetailGridManager.getData().length;
			$.each(data,function(i,o){
				var row = $.extend({},{ orgId: o.id, name: o.name, fullName: o.fullName, orgKindId: o.orgKindId, fullId: o.fullId });
				row.sequence = length + i + 1; 
				addRows.push(row);
			});
			userGroupDetailGridManager.addRows(addRows);
			this.close();
		}
	};
	OpmUtil.showSelectOrgDialog(options);
}

function deleteDetailHandler(){
	DataUtil.delSelectedRows({action:'userGroup/deleteUserGroupDetails.ajax',
		param: { groupId: $("#id").val() },
		gridManager:userGroupDetailGridManager, idFieldName:'id', 
		onSuccess:function(){
			userGroupDetailGridManager.loadData();
		}
	});
}