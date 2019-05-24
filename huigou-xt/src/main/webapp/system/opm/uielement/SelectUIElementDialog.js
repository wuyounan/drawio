var gridManager = null, resourceId ='', kindJSONData={}, uiElementOperationJSONData= {};;
$(document).ready(function() {
	resourceId = Public.getQueryStringByName("resourceId");
	kindJSONData = $("#kindId").combox("getJSONData");
	uiElementOperationJSONData = $("#uiElementOperation").combox("getJSONData");
	initializeGrid();
});
//初始化表格
function initializeGrid() {	
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addBatchHandler: function(){
			//parent.showAddPermissionField(gridManager);
		},
		//saveHandler: savePermissionField,
		deleteHandler: function(){
			DataUtil.delSelectedRows({action:'permissionField/deleteFunctionPermissionfield.ajax',
				gridManager:gridManager,idFieldName:'permissionFieldId',
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
	
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
			{ display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left"},		   	
			{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left"},		   	   
			{ display: "类别", name: "kindId", width: 80, minWidth: 60, type: "string", align: "left",
				render: function(item){
					return kindJSONData[item.kindId];
				}},		   
			{ display: "默认操作", name: "defaultOperationId", width:80, minWidth: 60, type: "string", align: "left",
				render: function(item){
					return uiElementOperationJSONData[item.defaultOperationId];
				}},	
		],
		dataAction: 'server',
		url: web_app.name+'/acess/slicedQueryUIElementPermissions.ajax',
		parms:{resourceId: resourceId},
		pageSize : 20,
		width: '100%',
		height: '100%',
		sortName:'sequence',
		sortOrder: 'asc',
		toolbar: toolbarOptions,
		heightDiff : -10,
		fixedCellHeight : true,
		selectRowButtonOnly : true
	});
}