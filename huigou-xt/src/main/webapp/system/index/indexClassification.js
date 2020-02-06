var gridManager = null, refreshFlag = false;
var pageParam = {rootId: "1", rootParentId: "0", currentId: "0"};
$(document).ready(function() {
	
	UICtrl.initDefaultLayout();
	
	initializeTree();
	initializeGrid();
	
    function initializeTree() {
        $('#indexClassificationTree').commonTree({
            loadTreesAction: "indexClassification/queryIndexClassifications.ajax",
            parentId: pageParam.rootParentId,
            isLeaf: function (data) {
                return parseInt(data.hasChildren) == 0;
            },
            onClick: function (data) {
                onTreeNodeClick(data);
            },
            getParam: function(){
            	return { isDefault: 0 };
            },
            IsShowMenu: false
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
    		enableHandler: enableHandler,
    		disableHandler: disableHandler,
    		saveSortIDHandler: saveSortIDHandler
    	});
    	gridManager = UICtrl.grid('#maingrid', {
    		columns: [
    			{ display: '图标', name: 'icon', width: 60, minWidth: 60, type: "string", align: "center", isAutoWidth: 0,
					render: function(item) {
						if (!Public.isBlank(item.icon)) {
							return '<span style="font-size:32px;"><i class="fa '+item.icon+'"></i></span>';
						}
						return '';
					}
	          },
    		{ display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },		   
    		{ display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },	
    		{ display: "名称全路径", name: "fullName", width: 300, minWidth: 60, type: "string", align: "left" },
    		{ display: "状态", name: "status", width: 100, minWidth: 60, type: "string", align: "left",
    			render: UICtrl.getStatusInfo 
    		},		   
    		{ display: "排序号", name: "sequene", width: 100, minWidth: 60, type: "string", align: "left",
    			render: function (item) {
    		    	return UICtrl.sequenceRender(item);
    		    }
    		}	   
    		],
    		dataAction: 'server',
    		url: web_app.name+'/indexClassification/slicedQueryIndexClassifications.ajax',
    		parms: { parentId: pageParam.rootId },
    		width: '100%',
    		height: '100%',
    		heightDiff: -5,
    		sortName:'sequence',
    		sortOrder:'asc',
    		toolbar: toolbarOptions,
    		checkbox: true,
    		selectRowButtonOnly: true,
    		onDblClickRow: function(data, rowindex, rowobj) {
    			updateHandler(data.id);
    		}
    	});
    	UICtrl.setSearchAreaToggle(gridManager);
    }
});

function onTreeNodeClick(data) {
	var html = [];
    if (data.id == pageParam.rootId) {
        html.push('指标分类列表');
    } else {
    	html.push('<span class="tomato-color">[', data.name, ']</span>指标分类列表');
    }
    
    pageParam.currentId = data.id;
    $("#layout").layout("setCenterTitle", html.join(''));
    if (gridManager) {
        UICtrl.gridSearch(gridManager, {parentId: pageParam.currentId});
    }
}

// 查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
	$("#indexClassificationTree").commonTree('refresh', pageParam.currentId);
	var params = $("#queryMainForm").formToJSON();
	UICtrl.gridSearch(gridManager, params);
} 

//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

//添加按钮 
function addHandler() {
	if (pageParam.currentId == pageParam.rootParentId) {
        Public.errorTip('请选择类别树。');
        return;
    }
	
	if (pageParam.currentId == pageParam.rootId){
		Public.errorTip('不能创建一级分类。');
        return;
	}
	
	var currentNodeData = $('#indexClassificationTree').commonTree("getSelected");
	
	UICtrl.showAjaxDialog({
		url: web_app.name + '/indexClassification/showInsertIndexClassification.load', 
		title: "添加指标分类",
		width: 400,
		param: {parentId: pageParam.currentId, dimId: currentNodeData.dimId},
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
	UICtrl.showAjaxDialog({
		url: web_app.name + '/indexClassification/showUpdateIndexClassification.load', 
		title: "修改指标分类",
		width: 400,
		param: {id: id}, 
		ok: update, 
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'indexClassification/deleteIndexClassifications.ajax',
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
	$('#submitForm').ajaxSubmit({url: web_app.name + '/indexClassification/insertIndexClassification.ajax',
		success: function(data) {
			refreshFlag = true;
			_self.close();
		}
	});
}

//编辑保存
function update(){
	var _self = this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/indexClassification/updateIndexClassification.ajax',
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
	var action = "indexClassification/updateIndexClassificationsSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager,idFieldName:'id', onSuccess: function(){
		reloadGrid(); 
	}});
}

//启用
function enableHandler(){
	DataUtil.updateById({ action: 'indexClassification/updateIndexClassificationsStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: 1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'indexClassification/updateIndexClassificationsStatus.ajax',
		gridManager: gridManager,idFieldName:'id',param:{status: 0},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
