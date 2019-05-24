var gridManager = null;
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
        deleteHandler: deleteHandler
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {display: "common.field.code", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "common.field.name", name: "name", width: 300, minWidth: 60, type: "string", align: "left"},
            {display: "无权限时隐藏", name: "isToHideTextView", width: 100, minWidth: 60, type: "string", align: "left"},
            {display: "权限校验Bean", name: "checkBeanName", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "common.field.remark", name: "remark", width: 300, minWidth: 60, type: "string", align: "left"}
        ],
        dataAction: 'server',
        url: web_app.name + '/groupFunction/slicedQueryFunctions.ajax',
        pageSize: 20,
        width: '100%',
        height: '100%',
        heightDiff: -5,
        sortName: 'code',
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

//添加按钮 
function addHandler() {
    UICtrl.showAjaxDialog({
        title: '新增',
        width: 400,
        url: web_app.name + '/groupFunction/showInsertFunctions.load',
        ok: insert
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
    var url = web_app.name + '/groupFunction/showLoadFunctions.load';
    BPMCUtil.createEditAttributePage($('#mainInfoDiv'), url, {id: id}, function (div) {
        initializeEditPageUI(div);
    });
}

//删除按钮
function deleteHandler() {
	var id = DataUtil.getUpdateRowId(gridManager);
	if (!id) {
        return;
    }
	UICtrl.confirm($.i18nProp('common.confirm.delete'), function () {
		Public.ajax(web_app.name + '/groupFunction/deleteFunctions.ajax', {id:id}, function (data) {
			reloadGrid();
        });
    });
}

//新增保存
function insert(div) {
    var _self = this;
    $('#submitForm',div).ajaxSubmit({
        url: web_app.name + '/groupFunction/insertFunctions.ajax',
        success: function (data) {
            reloadGrid();
            _self.close();
            updateHandler(data);
        }
    });
}