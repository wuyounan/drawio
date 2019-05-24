var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeUI();
	initializeGrid();
});


function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		loadTreesAction:'orgfun/queryOrgfunByParentId.ajax',
		parentId :'root',
		IsShowMenu:false,
		onClick : onTreeNodeClick,
		dataRender:function(data){
			return data['Rows']||data;
		}
    });
}


function onTreeNodeClick(data) {
	var html=[],parentId=data.id;
	html.push('<span class="tomato-color">[',data.name,']</span>','列表');
	$('#layout').layout('setCenterTitle',html.join(''));
	$('#treeParentId').val(data.id);
	UICtrl.gridSearch(gridManager, {parentId:data.id});
}

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
		{ display: "函数", name: "code", width: 300, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },	
		{ display: "末级", name: "isLastTextView", width: 100, minWidth: 60, type: "string", align: "left" },
		{ display: "备注", name: "remark", width: 300, minWidth: 60, type: "string", align: "left" },
		{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.status);
			} 
		},
		{ display: "序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left" ,
			render : function(item) {
				return UICtrl.sequenceRender(item);
			}  
		}
		], 
		dataAction: 'server',
		url: web_app.name+'/orgfun/slicedQueryOrgfun.ajax',
		parms:{parentId:'0'},
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
	var parentId=$('#treeParentId').val();
	$("#maintree").commonTree('refresh', parentId);
	gridManager.loadData();
} 

function resetForm(obj) {
	$(obj).formClean();
}

function addHandler() {
	var node=$("#maintree").commonTree('getSelected');
	if(!node){
		Public.tip('请选择类别树。');
		return;
	}
	var isLast=node.isLast,parentId=node.id;
	if(isLast=='1'){
		Public.tip('末级不允许添加子记录。');
		return;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/orgfun/showInsertOrgfun.load',
		title:'新增['+node.name+']子级',
		width: 500, 
		ok: insert
	});
}

function updateHandler(id){
	if(!id){
		var id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	
	UICtrl.showAjaxDialog({
		url: web_app.name + '/orgfun/showLoadOrgfun.load',
		title:'编辑', 
		param:{id:id}, 
		width: 500,
		ok: update
	});
}

function deleteHandler(){
	DataUtil.del({action:'orgfun/deleteOrgfun.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function insert() {
	var parentId=$('#treeParentId').val();
	var _self=this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/orgfun/insertOrgfun.ajax',
		param:{parentId:parentId},
		success: function(id) {
			reloadGrid();
			_self.close();
		}
	});
}

function update(){
	var _self=this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/orgfun/updateOrgfun.ajax',
		success: function() {
			reloadGrid();
			_self.close();
		}
	});
}

function moveHandler(){
	var rows = gridManager.getSelectedRows();
	if (!rows || rows.length < 1) {
		Public.tip('common.warning.nochoose');
		return;
	}
	UICtrl.showDialog({title:'common.button.move',width:300,
		content:'<div style="overflow-x: hidden; overflow-y: auto; width:280px;height:250px;"><ul class="dialogMoveTree"></ul></div>',
		init:function(div){
			$('ul.dialogMoveTree',div).commonTree({
				loadTreesAction:'orgfun/queryOrgfunByParentId.ajax',
				parentId :'root',
				IsShowMenu:false,
				dataRender:function(data){
					return data['Rows']||data;
				}
		    });
		},
		ok:function(div){
			var parentId=$('ul.dialogMoveTree',div).commonTree('getSelectedId');
			if(!parentId){
				Public.tip('common.warning.not.nodetree');
				return false;
			}
			DataUtil.updateById({action:'orgfun/moveOrgfun.ajax',
				gridManager:gridManager,idFieldName:'id',param:{parentId:parentId},
				onSuccess:function(){
					$('#treeParentId').val(0);
					reloadGrid();
				}
			});
			this.close();
		}
	});
}

//保存排序号
function saveSortIDHandler(){
	var action = "orgfun/updateOrgfunSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager, onSuccess: function(){
		reloadGrid();
	}});
}

//启用
function enableHandler(){
	DataUtil.updateById({ action: 'orgfun/updateOrgfunStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: 1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'orgfun/updateOrgfunStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: 0},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
