var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeGrid();
	initializeUI();
});
function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId : CommonTreeKind.MessageRemind,
		onClick : onFolderTreeNodeClick
	});
}
function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.MessageRemind){
		parentId="";
		html.push('消息提醒列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>消息提醒列表');
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
		moveHandler:moveHandler,
		saveSortIDHandler : saveSortIDHandler,
		testParseRemindFun:{id:'testParseRemindFun',text:'测试',img:'page_dynamic.gif',click:testParseRemindFun}
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },		   	
		{ display: "标题", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },		   	   
		{ display: "提示文本", name: "remindTitle", width: 200, minWidth: 60, type: "string", align: "left" },		   
		{ display: "连接地址", name: "remindUrl", width: 200, minWidth: 60, type: "string", align: "left" },		   
		{ display: "函数", name: "executeFunc", width: 200, minWidth: 60, type: "string", align: "left" },
		{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "left",
			render: function (item) { 
				return UICtrl.getStatusInfo(item.status);
			} 
		},	   
		{ display: "页面打开方式", name: "openKindTextView", width: 80, minWidth: 60, type: "string", align: "left"},
		{ display: "排序号", name: "sequence", width: 80, minWidth: 60, type: "string", align: "left" ,
			render : function(item) {
				return "<input type='text' mask='nnn' id='txtSequence_" +item.id+ "' class='textbox' value='" + item.sequence + "' />";
			}  
		}
		],
		dataAction : 'server',
		url: web_app.name+'/messageRemind/slicedQuery.ajax',
		pageSize : 20,
		width : '100%',
		height : '100%',
		sortName:'sequence',
		sortOrder:'asc',
		heightDiff : -5,
		toolbar: toolbarOptions,
		checkbox: true,
		onDblClickRow : function(data, rowindex, rowobj) {
			updateHandler(data);
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
	var parentId=$('#treeParentId').val();
	if(parentId==''){
		Public.tip('请选择类别树！'); 
		return;
	}
	UICtrl.showAjaxDialog({
		title:'添加消息提醒',
		url: web_app.name + '/messageRemind/showInsert.load',
		param:{folderId:parentId},
		ok: insert, 
		close: dialogClose,
		width:510
	});
}

//编辑按钮
function updateHandler(data){
	if(!data){
		data = DataUtil.getUpdateRow(gridManager);
		if (!data) { return; }
	}
	var id=data.id;
	UICtrl.showAjaxDialog({
		title:'修改消息提醒',
		url: web_app.name + '/messageRemind/showLoad.load', 
		param:{id:id},
		width:510,
		ok: update, 
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'messageRemind/delete.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//新增保存
function insert() {
	var remindId=$('#remindId').val();
	if(remindId!='') return update();
	$('#submitForm').ajaxSubmit({url: web_app.name + '/messageRemind/save.ajax',
		param:{folderId:$('#treeParentId').val()},
		success : function(id) {
			$('#remindId').val(id);
			refreshFlag = true;
		}
	});
}

//编辑保存
function update(){
	$('#submitForm').ajaxSubmit({url: web_app.name + '/messageRemind/save.ajax',
		success : function() {
			refreshFlag = true;
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

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,kindId:CommonTreeKind.MessageRemind,
		save:function(parentId){
			DataUtil.updateById({action:'messageRemind/updateFolderId.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();	
				}
			});
		}
	});
}


//启用
function enableHandler(){
	DataUtil.updateById({ action: 'messageRemind/updateStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status:1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'messageRemind/updateStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status:0},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});			
}

function saveSortIDHandler(){
	var action = "messageRemind/updateSequence.ajax";
	DataUtil.updateSequence({action : action,gridManager : gridManager,idFieldName:'id',onSuccess : reloadGrid});
}

function testParseRemindFun(){
	var row = gridManager.getSelectedRow();
	if (!row) {Public.tip('请选择数据！'); return; }
	Public.ajax(web_app.name + "/messageRemind/testParseRemindFun.ajax",{id:row.id},function(data){
		var html=['<div style="text-align:left;">'];
		$.each(data, function(i, o){
			html.push('内容:',o.remindTitle,'</br>');
			html.push('连接:',o.remindUrl,'</br>');
		});
		html.push('</div>');
		var options={title:'测试结果',content:html.join(''),width: 400,opacity:0.1};
		Public.dialog(options);
	});
}