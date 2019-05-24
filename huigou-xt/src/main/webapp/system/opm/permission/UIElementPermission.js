var gridManager = null, refreshFlag = false, permissionId, kindJSONData={}, uiElementOperationJSONData= {};
Public.tip.topDiff=7;
$(document).ready(function() {
	permissionId = Public.getQueryStringByName("permissionId");
	kindJSONData = $("#kindId").combox("getJSONData");
	uiElementOperationJSONData = $("#uiElementOperation").combox("getJSONData");

	initializeGrid();
});

//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addBatchHandler: function(){
			parent.showSelectUIElementDialog(gridManager, permissionId, gridManager.getData().length);
		},
		addHandler: addHandler,
		saveHandler: saveUIElementPermission,
		deleteHandler: function(){
			DataUtil.delSelectedRows({action:'access/deleteUIElementPermissions.ajax',
				gridManager:gridManager, idFieldName:'id',
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
	
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
			{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left",
				editor: { type: 'text',required: true}
			},		   	
			{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left",
				editor: { type: 'text',required: true}
			},		   	   
			{ display: "类别", name: "kindId", width: 80, minWidth: 60, type: "string", align: "left",
				editor: { type:'combobox', data: kindJSONData, required: true},
				render: function (item) { 
					return kindJSONData[item.kindId];
				}
			},		   
			{ display: "权限", name: "operationId", width:80, minWidth: 60, type: "string", align: "left",
				editor: { type:'combobox', data: uiElementOperationJSONData, required: true},
				render: function (item) { 
					return uiElementOperationJSONData[item.operationId];
				}
			},
			{ display: "排序号", name: "sequence", width: 150, minWidth: 60, type: "string", align: "left",
				editor: { type: 'text',required: true}
			}
		],
		dataAction : 'server',
		url: web_app.name+'/access/slicedQueryUIElementPermissions.ajax',
		parms: { permissionId: permissionId },
		pageSize : 20,
		width: '100%',
		height: '100%',
		sortName: 'sequence',
		sortOrder:'asc',
		heightDiff : -10,
		toolbar: toolbarOptions,
		fixedCellHeight : true,
		enabledEdit: true,
		checkbox: true,
		selectRowButtonOnly: true
	});
}

function addHandler(){
	var sequence = gridManager.getData().length;
	UICtrl.addGridRow(gridManager,{ permissionId: permissionId, sequence: ++sequence });
}

function saveUIElementPermission(){
	var detailData = DataUtil.getGridData({ gridManager: gridManager });
	if(!detailData) {
		return;
	}
	
	if(detailData.length==0){
		Public.tip('数据没有被修改。');
		return;
	}
	
	var data = gridManager.getData();
	
	for (var i = 0; i < data.length; i++){
		for (var j = i + 1; j < data.length; j++){
			if (data[i].code == data[j].code){
				Public.errorTip("编码“"+ data[i].code +"”重复，不能保存。");
				return;
			}
		}
	}
	
	Public.ajax(web_app.name + '/access/saveUIElementPermissions.ajax', 
		{ detailData: Public.encodeJSONURI(detailData) },
		function(data) {
			reloadGrid();
		}
	);
}

// 查询
function query() {
	UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
}