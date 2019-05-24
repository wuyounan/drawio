var gridManager = null, refreshFlag = false;
$(document).ready(function () {
    initializeGrid();
});

//初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
    	addHandler: addHandler,
        updateHandler: function () {
            updateHandler();
        },
        deleteHandler: deleteHandler,
        saveSortIDHandler: saveSortIDHandler
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "数据类型", name: "dataKindTextView", width: 100, minWidth: 60, type: "string", align: "left"},
            {display: "数据源", name: "dataSource", width: 400, minWidth: 60, type: "string", align: "left"},
            {display: "备注", name: "remark", width: 200, minWidth: 60, type: "string", align: "left"},
            {
                display: "序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return UICtrl.sequenceRender(item);
                }
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/opDataKind/slicedQueryOpdatakind.ajax',
        checkbox: false,
        usePager: true,
        width: '100%',
        height: '100%',
        heightDiff: -5,
        sortName: 'sequence',
        sortOrder: 'asc',
        toolbar: toolbarOptions,
        onDblClickRow: function (data, rowindex, rowobj) {
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

function addHandler() {
	UICtrl.showAjaxDialog({
		title: '添加权限资料类型',
        width: 600,
        url: web_app.name + '/opDataKind/showInsertOpdatakind.load',
        ok: doSave
    });
}

//编辑按钮
function updateHandler(id) {
    if (!id) {
        var id = DataUtil.getUpdateRowId(gridManager);
        if (!id) {
            return;
        }
    }
    UICtrl.showAjaxDialog({
        title: '修改',
        width: 600,
        url: web_app.name + '/opDataKind/showLoadOpdatakind.load',
        param: {
            id: id
        },
        ok: doSave
    });
}

//编辑保存
function doSave() {
    var _self = this;
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/opDataKind/saveOpdatakind.ajax',
        success: function () {
        	reloadGrid();
            _self.close();
        }
    });
}

function deleteHandler(){
	DataUtil.del({action:'opDataKind/deleteOpdatakind.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();
		}
	});
}

//保存扩展字段排序号
function saveSortIDHandler() {
    var action = "opDataKind/updateOpdatakindSequence.ajax";
    DataUtil.updateSequence({
        action: action, gridManager: gridManager, idFieldName: 'id', onSuccess: function () {
            reloadGrid();
        }
    });
    return false;
}