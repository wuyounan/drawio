var gridManager = null, refreshFlag = false,securityGradeList;
$(document).ready(function () {
    initUI();
    initGrid();
});

function initUI() {
	securityGradeList = $('#securityGrade').combox('getJSONData');
}

function initGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
    	addHandler: addHandler,
		updateHandler:showUpdateDialog,
		deleteHandler: deleteHandler,
		enableHandler: enableHandler,
        disableHandler: disableHandler
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            { display: "编码", name: "code", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "名称", name: "name", width: 160, minWidth: 60, type: "string", align: "left" },
            { display: "IP地址", name: "ip", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "MAC地址", name: "mac", width: 160, minWidth: 60, type: "string", align: "left" },
            { display: "密级", name: "securityGrade", width: 80, minWidth: 60, type: "string", align: "center", 
            	render: function (item) {
            		return securityGradeList[item.securityGrade];
	            }
            },
            { display: "状态", name: "status", width: 80, minWidth: 40, type: "string", align: "center",
                render: function (item) {
                    return UICtrl.getStatusInfo(item.status);
                }
            },
            { display: "备注", name: "remark", width: 250, minWidth: 60, type: "string", align: "left" }
			],
        dataAction: 'server',
        url: web_app.name + '/machine/sliceQueryMachines.ajax',
        toolbar: toolbarOptions,
        width: '99.8%',
        height: '100%',
        heightDiff: -3,
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
    	title: "添加机器",
    	width: 400,
    	url: web_app.name + '/machine/showInsertMachine.load',
    	ok: doInsert,
    	close: onDialogCloseHandler
    });
}
function doInsert(){
	var _self = this;
	$('#submitForm').ajaxSubmit({
		url: web_app.name + '/machine/insertMachine.ajax',
		success: function(){
			refreshFlag = true;
			_self.close();
		}
	});
}

function showUpdateDialog(){
	var row = gridManager.getSelectedRow();
	if (!row) {
		Public.tip("请选择数据。");
		return;
	}
    doShowUpdateDialog(row.id);
}

function doShowUpdateDialog(id){
	UICtrl.showAjaxDialog({
    	title: "修改机器",
    	width: 400,
    	url: web_app.name + '/machine/loadMachine.load',
    	param:{ id: id },
    	ok: doUpdate,
    	close: onDialogCloseHandler
    });
}

function doUpdate(){
	var _self = this;
	$('#submitForm').ajaxSubmit({
		url: web_app.name + '/machine/updateMachine.ajax',
		success:function(){
			refreshFlag = true;
			_self.close();
		}
	});
}

function deleteHandler(){
	DataUtil.del({
        action: '/machine/deleteMachine.ajax',
        idFieldName: 'id',
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}

function enableHandler() {
    updateStatus('您要启用选中数据吗?', 1);
}

function disableHandler() {
    updateStatus('您要禁用选中数据吗?', 0);
}

function updateStatus(message, status) {
    DataUtil.updateById({ action: 'machine/updateMachinesStatus.ajax',
        gridManager: gridManager, param: { status: status },
        message: message,
        onSuccess: function () {
            reloadGrid();
        }
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