var gridManager = null, refreshFlag = false;
$(document).ready(function () {
    initializeGrid();
});

function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
    	addHandler: addHandler,
		updateHandler:showUpdateDialog,
		deleteHandler: deleteHandler,
		saveSortIDHandler: updateSequence,
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            { display: "编码", name: "code", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "名称", name: "name", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "类名前缀", name: "classPrefix", width: 200, minWidth: 60, type: "string", align: "left" },
            { display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "center",render: function (item) {
                return "<input type='text' id='txtSequence_"
                + item.id
                + "' class='textbox' value='"
                + item.sequence + "' />";
    		}}
			],
        dataAction: 'server',
        url: web_app.name + '/applicationSystem/sliceQueryApplicationSystems.ajax',
        toolbar: toolbarOptions,
        width: '99.8%',
        height: '100%',
        heightDiff: -3,
        sortName:'sequence',
		sortOrder:'asc',
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {
        	doShowUpdateDialog(data.id);
        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

function addHandler(){
	UICtrl.showAjaxDialog({
    	title: "添加应用系统",
    	width: 400,
    	url: web_app.name + '/applicationSystem/showInsertApplicationSystem.load',
    	ok: doInsert,
    	close: onDialogCloseHandler
    });
}

function doInsert(){
	var _self = this;
	$('#submitForm').ajaxSubmit({
		url: web_app.name + '/applicationSystem/insertApplicationSystem.ajax',
		success:function(){
			refreshFlag = true;
			_self.close();
		}
	});
}

function showUpdateDialog(){
	 var id = DataUtil.getUpdateRowId(gridManager);
	 if (!id){return;}
	 doShowUpdateDialog(id);
}

function doShowUpdateDialog(id){
	UICtrl.showAjaxDialog({
    	title: "修改应用系统",
    	width: 400,
    	url: web_app.name + '/applicationSystem/loadApplicationSystem.load',
    	param:{ id:id },
    	ok: doUpdate,
    	close: onDialogCloseHandler
    });
}

function doUpdate(){
	var _self = this;
	$('#submitForm').ajaxSubmit({
		url: web_app.name + '/applicationSystem/updateApplicationSystem.ajax',
		success:function(){
			refreshFlag = true;
			_self.close();
		}
	});
}

function updateSequence(){
	DataUtil.updateSequence({
        action:'/applicationSystem/updateApplicationSystemsSequence.ajax',
        idFieldName: "id",
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}
function deleteHandler(){
	DataUtil.del({
        action: '/applicationSystem/deleteApplicationSystems.ajax',
        idFieldName: 'id',
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}
 
function query(obj) {
    var param = $(obj).formToJSON();
    UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}

function reloadGrid() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}