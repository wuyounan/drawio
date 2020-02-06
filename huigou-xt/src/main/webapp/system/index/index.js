var gridManager = null, refreshFlag = false;
var pageParam = {rootId: "1", rootParentId: "0", currentId: "0"};
$(document).ready(function() {
	UICtrl.initDefaultLayout();
	initTree();
	initGrid();
});

function initTree() {
    $('#mainTree').commonTree({
        loadTreesAction: "indexClassification/queryIndexClassifications.ajax",
        parentId: pageParam.rootParentId,
        isLeaf: function (data) {
            return parseInt(data.hasChildren) == 0;
        },
        onClick: function (data) {
            onTreeNodeClick(data);
        },
        getParam: function(){
        	return { isDefault: 1 };
        },
        IsShowMenu: false
    });
}

function onTreeNodeClick(data) {
	var html = [];
    if (data.id == pageParam.rootId) {
        html.push('指标列表');
    } else {
    	html.push('<span class="tomato-color">[', data.name, ']</span>指标列表');
    }
    
    pageParam.currentId = data.id;
    
    $("#layout").layout("setCenterTitle", html.join(''));
    if (gridManager) {
        UICtrl.gridSearch(gridManager, { fullId: data.fullId });
    }
}

//初始化表格
function initGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		addHandler: addHandler, 
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		saveSortIDHandler: saveSortIDHandler,
		enableHandler: enableHandler,
		disableHandler: disableHandler,
		moveHandler: moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
			{ display: "ID", name: "id", width: 240, minWidth: 60, type: "string", align: "left" },		
			{ display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" },		   
			{ display: "名称", name: "name", width: 250, minWidth: 60, type: "string", align: "left" },		   
			{ display: "流程主键", name: "procKey", width: 100, minWidth: 60, type: "string", align: "left" },		   
			{ display: "周期类型", name: "indexPeriodKindTextView", width: 70, minWidth: 60, type: "string", align: "center" },		   
			{ display: "指标级别", name: "indexGradeTextView", width: 70, minWidth: 60, type: "string", align: "center" },		   
			{ display: "状态", name: "status", width: 60, minWidth: 30, type: "string", align: "center",
                render: UICtrl.getStatusInfo
            },	   
			{ display: "排序号", name: "sequence", width: 60, minWidth: 30, type: "int", align: "left",
                render: function (item) {                    
                    return UICtrl.sequenceRender(item);
                }
            },		   
			{ display: "描述", name: "description", width: 350, minWidth: 60, type: "string", align: "left" }
		],
		dataAction : 'server',
		url: web_app.name+'/index/slicedQueryIndexes.ajax',
		width: '100%',
		height: '100%',
		heightDiff : -5,
		sortName:'sequence',
		sortOrder:'asc',
		checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
		toolbar: toolbarOptions,
		onDblClickRow : function(data, rowindex, rowobj) {
			doShowUpdate(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

function addHandler() {
	if(pageParam.currentId == "0" || pageParam.currentId == "1"){
        Public.errorTip('请选择指标分类树。');
        return;		
	}
    parent.addTabItem({
        tabid: 'MCSIndexDetail',
        text: "添加指标",
        url: web_app.name + '/index/showInsertIndex.do?classificationId=' + pageParam.currentId
    });
}

function updateHandler(){
    var row = UICtrl.checkSelectedRows(gridManager);
    if (row) {
    	doShowUpdate(row.id);
    }	
}

function doShowUpdate(id){
    parent.addTabItem({
        tabid: 'MCSIndexDetail' + id,
        text: "修改指标",
        url: web_app.name + '/index/showUpdateIndex.do?id=' + id
    });	
}

//删除
function deleteHandler() {
  var action = "index/deleteIndexes.ajax";
  DataUtil.del({
      action: action,
      idFieldName: 'id',
      gridManager: gridManager,
      onSuccess: reloadGrid
  });
}

//保存扩展字段排序号
function saveSortIDHandler(){
	var action = "index/updateIndexsSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager,idFieldName:'id', onSuccess: function(){
		reloadGrid(); 
	}});
	return false;
}

//启用
function enableHandler() {
 updateStatus('确实要启用选中数据吗?', 1);
}

//禁用
function disableHandler() {
 updateStatus('确实要禁用选中数据吗?', 0);
}

//修改状态
function updateStatus(message, status) {
 DataUtil.updateById({ action: 'index/updateIndexsStatus.ajax',
     gridManager: gridManager, param: { status: status },
     message: message,
     onSuccess: function () {
         reloadGrid();
     }
 });
}

//移动
function moveHandler(){
	UICtrl.showDialog({title:'移动指标'||'common.button.move',width:300,
		content:'<div style="overflow-x: hidden; overflow-y: auto; width:280px;height:250px;"><ul id="dialogMoveTree"></ul></div>',
		init:function(){
			$('#dialogMoveTree').commonTree({loadTreesAction: "indexClassification/queryIndexClassifications.ajax",
				parentId: pageParam.rootParentId,
		        getParam: function(){
		        	return { isDefault: 1 };
		        },IsShowMenu: false
			});
		},
		ok:function(){
			var parentId=$('#dialogMoveTree').commonTree('getSelectedId');
			if(!parentId){
				Public.tip('common.warning.not.nodetree');
				return false;
			}
			DataUtil.updateById({action:'index/moveIndexs.ajax',
				gridManager:gridManager,idFieldName:'id',param:{classificationId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
			
			this.close();
		}
	});
}

//查询
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

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}
