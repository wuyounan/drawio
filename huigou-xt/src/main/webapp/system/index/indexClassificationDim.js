var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeGrid();
});

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
		saveSortIDHandler: saveSortIDHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [		   
		{ display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },		   
		{ display: "状态", name: "status", width: 100, minWidth: 60, type: "string", align: "left",
			render: UICtrl.getStatusInfo 
		},		   
		{ display: "默认", name: "isDefault", width: 100, minWidth: 60, type: "string", align: "left",
			render: function(item){
			    switch (parseInt(item.isDefault)) {
			        case 0:
			            return "<div class='status-disable' title='否'/>";
			            break;          
			        case 1:
			            return "<div class='status-enable' title='是'/>";
			            break;
			    }
			}
		},		   
		{ display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left",
		    render: function (item) {
		    	return UICtrl.sequenceRender(item);
		    }
		}
		],
		dataAction: 'server',
		checkbox: true,
		url: web_app.name+'/indexClassificationDim/slicedQueryIndexClassificationDims.ajax',
		width: '100%',
		height: '100%',
		heightDiff: -5,
		sortName: 'sequence',
		sortOrder: 'asc',
		toolbar: toolbarOptions,
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
	UICtrl.showAjaxDialog({url: web_app.name + '/indexClassificationDim/showInsertIndexClassificationDim.load', 
		title: "添加指标分类维度",
		width: 400,
		ok: insert, 
		close: dialogClose
	});
}

//编辑按钮
function updateHandler(id){
	if(!id){
		id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	//所需参数需要自己提取 {id:row.id}
	UICtrl.showAjaxDialog({url: web_app.name + '/indexClassificationDim/showUpdateIndexClassificationDim.load',
		param: {id: id},
		title: "修改指标分类维度",
		width: 400,
		ok: update, 
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action: 'indexClassificationDim/deleteIndexClassificationDims.ajax',
		gridManager:gridManager,idFieldName:'id',
		onCheck:function(data){
		},
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//新增保存
function insert() {
	var _self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/indexClassificationDim/insertIndexClassificationDim.ajax',
		success: function(data) {
			refreshFlag = true;
			_self.close();
		}
	});
}

//编辑保存
function update(){
	var _self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/indexClassificationDim/updateIndexClassificationDim.ajax',
		success: function() {
			refreshFlag = true;
			_self.close();
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

//保存扩展字段排序号
function saveSortIDHandler(){
	var action = "indexClassificationDim/updateIndexClassificationDimsSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager,idFieldName:'id', onSuccess: function(){
		reloadGrid(); 
	}});
}

//启用
function enableHandler(){
	DataUtil.updateById({ action: 'indexClassificationDim/updateIndexClassificationDimsStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status:1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'indexClassificationDim/updateIndexClassificationDimsStatus.ajax',
		gridManager: gridManager, idFieldName:'id',param: {status: 0},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
