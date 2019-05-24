var gridManager = null, refreshFlag = false,dictDetalGridManager=null;

$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.Dictionary,
		onClick: onFolderTreeNodeClick
	});
}

function onFolderTreeNodeClick(data, folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.Dictionary){
		parentId="";
		html.push('系统字典列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>系统字典列表');
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
		enableHandler: enableHandler,
		disableHandler: disableHandler,
		moveHandler: moveHandler,
		syncCache:{id:'syncCache',text:'同步缓存',img:'fa-link',click:function(){
			Public.ajax(web_app.name + "/dictionary/syncCache.ajax");
		}}
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" },				   
		{ display: "类别", name: "kindId", width: 80, minWidth: 60, type: "string", align: "center",
			render: function (item) { 
				return item.kindId==1 ? '用户':'系统'; 
			} 
		},	
		{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.status);
			} 
		},
		{ display: "备注", name: "remark", width: 300, minWidth: 60, type: "string", align: "left" }	
		],
		dataAction: 'server',
		url: web_app.name+'/dictionary/slicedQuerySysDictionaries.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		sortName:'code',
		sortOrder:'asc',
		heightDiff: -8,
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		checkbox: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
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
		Public.tip('请选择类别树。');
		return;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/dictionary/showInsertSysDictionary.load',
		title: "添加系统字典",
		ok: insert, 
		width: 770, 
		init: initDialog,
		close: dialogClose
	});
}

//编辑按钮
function updateHandler(data){
	if(!data){
		var rows = gridManager.getSelectedRows();
		if(!rows.length){
			Public.tip('请选择数据。'); 
			return; 
	    }
		if(rows.length>1){
			Public.tip('只能选择一条记录编辑!');
			return;
		}
		data=rows[0];
	}
	if(parseInt(data.status) != -1){
		if(parseInt(data.kind) == -1){
			Public.errorTip('该类别的字典无法编辑。'); 
			return;
		}
	}

	UICtrl.showAjaxDialog({
		url: web_app.name + '/dictionary/showUpdateSysDictionary.load',
		title: "修改系统字典",
		param:{id: data.id},
		width: 770,
		ok: update, 
		init:initDialog,
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'dictionary/deleteSysDictionaries.ajax',
		gridManager: gridManager, idFieldName: 'id',
		onCheck:function(data){
			if(parseInt(data.status) != -1){
				Public.tip(data.name+'不是草稿状态,不能删除。');
				return false;
			}
		},
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function getId(){
	return $('#id').val();
}

function insert() {
	var id = getId();
	if(id!='') {
		return update();
	}
	var detailData = DataUtil.getGridData({gridManager:dictDetalGridManager});
	if(!detailData) {
		return false;
	}
	if(!checkDetailData()){
		return false;
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/dictionary/insertSysDictionary.ajax',
		param:{folderId:$('#treeParentId').val(),detailData:Public.encodeJSONURI(detailData)},
		success: function(id) {
			$('#id').val(id);
			dictDetalGridManager.options.parms['dictionaryId']=id;
			dictDetalGridManager.loadData();
			refreshFlag = true;
		}
	});
}

function checkDetailData(){
	//校验明细中是否存在重复的成员
	var detailData=DataUtil.getGridData({gridManager:dictDetalGridManager,isAllData:true});
	var flag=true,names={},values={};
	$.each(detailData,function(i,o){
		if(names[o.name]){
			Public.tip('成员名称['+o.name+']重复，请检查!');
			flag=false;
			return false;
		}else{
			names[o.name]=1;
		}
		if(values[o.value]){
			Public.tip('成员值['+o.value+']重复，请检查!');
			flag=false;
			return false;
		}else{
			values[o.value]=1;
		}
	});
	return flag;
}

//编辑保存
function update(){
	var detailData = DataUtil.getGridData({ gridManager: dictDetalGridManager, idFieldName: 'id' });
	if(!detailData) {
		return false;
	}
	if(!checkDetailData()){
		return false;
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/dictionary/updateSysDictionary.ajax',
		param:{ id: getId(), detailData: Public.encodeJSONURI(detailData)},
		success: function() {
			refreshFlag = true;
			dictDetalGridManager.loadData();
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
		gridManager:gridManager,title:'移动数据字典',kindId:CommonTreeKind.Dictionary,
		save:function(parentId){
			DataUtil.updateById({action:'dictionary/moveSysDictionaries.ajax',
				gridManager:gridManager,idFieldName:'id', param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();	
				}
			});
		}
	});
}
//启用
function enableHandler(){
	DataUtil.updateById({ action: 'dictionary/updateSysDictionariesStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: 1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'dictionary/updateSysDictionariesStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: 0},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

function initDialog(doc){
	var id=$('#id').length>0?$('#id').val():'';
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: function(){
			//新增时传入detailId -(new Date().getTime())临时主键
			UICtrl.addGridRow(dictDetalGridManager, {status: -1, sequence: dictDetalGridManager.getData().length + 1});
		},
		deleteHandler: function(){
			DataUtil.delSelectedRows({action:'dictionary/deleteSysDictionaryDetails.ajax',
				gridManager:dictDetalGridManager,idFieldName:'id',
				onCheck:function(data){
					if(parseInt(data.status) != -1){
						Public.tip(data.name+'不是草稿状态,不能删除!');
						return false;
					}
				},
				onSuccess:function(){
					dictDetalGridManager.loadData();
				},
				param: {
					dictionaryId: $('#id').val()
				}
			});
		},
		enableHandler: function(){
			enableOrDisableDictDetal(1);
		},
		disableHandler: function(){
			enableOrDisableDictDetal(0);
		}
	});
	var param={dictionaryId: id};
	param[gridManager.options['pagesizeParmName']]=1000;
	dictDetalGridManager=UICtrl.grid('#dictDetalGrid', {
		 columns: [
		        { display: "成员名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left",
		        	editor: { type: 'text',required:true}
		        },			        	
				{ display: "成员值", name: "value", width: 250, minWidth: 60, type: "string", align: "left",
					editor: { type: 'text',required:true}
				},
		        { display: "成员类别", name: "typeId", width: 84, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'text'}
				},
		        { display: "排序号", name: "sequence", width: 64, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'}
				},
				{ display: "状态", name: "status", width: 70, minWidth: 60, type: "string", align: "left",
					render: function (item) { 
						return UICtrl.getStatusInfo(item.status);
					}
				},
				{ display: "备注", name: "remark", width: 200, minWidth: 60, type: "string", align: "left",
					editor: { type:'text'}
				}
		],
		dataAction: 'server',
		url: web_app.name+'/dictionary/slicedQuerySysDictionaryDetails.ajax',
		parms:param,
		width: '100%', 
		height: '260',
		sortName:'sequence',
		sortOrder:'asc',
		heightDiff: -8,
		toolbar: toolbarOptions,
		enabledEdit: true,
		usePager: true,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onBeforeEdit:function(editParm){
			var c = editParm.column;
			if(c.name == 'value'){//启用的数据value 不能编辑
			   return editParm.record['status'] === -1;
			}
			return true;
		},
		onLoadData: function(){
			return $('#id').val()!='';
		}
	});
}
function enableOrDisableDictDetal(status){
	//判断是否存在新增数据,如果存在则先提示用户保存
	var data=DataUtil.getGridData({gridManager: dictDetalGridManager});
	if(data.length>0){
		Public.tip("数据已经改变,请执行保存后再执行该操作。");
		return false;
	}
	var message= status == 1 ? '确实要启用选中数据吗?':'确实要禁用选中数据吗?';
	DataUtil.updateById({ action: 'dictionary/updateSysDictionaryDetailsStatus.ajax',
		gridManager: dictDetalGridManager,idFieldName:'id', param:{status: status},
		message: message,
		onSuccess:function(){
			dictDetalGridManager.loadData();
		}
	});	
}
