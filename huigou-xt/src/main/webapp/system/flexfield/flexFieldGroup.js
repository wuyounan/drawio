var gridManager = null;

$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId : CommonTreeKind.FlexFieldGroup,
		onClick : onFolderTreeNodeClick
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
		saveSortIDHandler: saveSortIDHandler,
		moveHandler:moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		 columns: [
			{ display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" },		   
			{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" },
			{ display: "业务编码", name: "bizCode", width: 200, minWidth: 60, type: "string", align: "left" },
			{ display: "是否显示", name: "visible", width: 100, minWidth: 60, type: "string", align: "left",
				render: function (item) {
					return "<div class='"+(item.visible?"status-enable":"status-disable")+"'/>";
				} 
			},	
			{ display: "显示模式", name: "showModel", width: 100, minWidth: 60, type: "string", align: "left",
				render: function (item) {
					return item.showModel==1?'表格':'DIV';
				} 
			},
			{ display: "每行列数", name: "cols", width: 100, minWidth: 60, type: "string", align: "left" },
			{ display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left" ,
				render : function(item) {
					return UICtrl.sequenceRender(item);
				}  
			},
			{ display: "备注", name: "remark", width: 250, minWidth: 60, type: "string", align: "left" }	
		],
		dataAction : 'server',
		url: web_app.name+'/flexField/slicedQueryFlexFieldBizGroups.ajax',
		pageSize : 20,
		width : '100%',
		height : '100%',
		sortName:'sequence',
		sortOrder:'asc',
		heightDiff : -6,
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true, 
		onDblClickRow : function(data, rowindex, rowobj) {
			updateHandler(data.id);
		}
//	,
//		onAfterShowData:function(){
//			UICtrl.autoInitializeGridUI('#maingrid');
//		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.FlexFieldGroup){
		parentId="";
		html.push('扩展属性分组');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>扩展属性分组');
	}
	$("#layout").layout("setCenterTitle", html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId:parentId});
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
	var folderId=$('#treeParentId').val();
	if(folderId==''){
		Public.tip('请选择类别树！'); 
		return;
	}
	var url=web_app.name + '/flexField/showInsertFlexFieldBizGroup.do?folderId='+folderId;
	UICtrl.addTabItem({ tabid: 'addFlexField', text: '新增扩展属性分组', url:url});
}

//编辑按钮
function updateHandler(id){
	if(!id){
		id=DataUtil.getUpdateRowId(gridManager);
		if(!id){return;}
	}
	var url=web_app.name + '/flexField/loadFlexFieldBizGroup.do?id='+id;
	UICtrl.addTabItem({ tabid: 'loadFlexField'+id, text: '编辑扩展属性分组', url:url});
}


//删除按钮
function deleteHandler(){
	DataUtil.del({action:'flexField/deleteFlexFieldBizGroups.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//保存排序号
function saveSortIDHandler(){
	var action = "flexField/updateFlexFieldBizGroupSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager, onSuccess: function(){
		reloadGrid();
	}});
}

//移动
function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'移动到...',kindId:CommonTreeKind.FlexFieldGroup,
		save:function(parentId){
			DataUtil.updateById({action:'flexField/moveFlexFieldBizGroups.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
}

//复制新增
function copyHandler(){
	var row = gridManager.getSelectedRow();
	if (!row) {Public.tip('请选择数据。'); return; }
	var url = web_app.name + '/flexField/copyFlexFieldBizGroups.ajax';
    Public.ajax(url, {id:row.id}, function (data) {
    	updateHandler(data);
    });
}