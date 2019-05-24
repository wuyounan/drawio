var gridManager = null, refreshFlag = false, securityGradeList;

$(document).ready(function () {
    initializeUI();
    initializeGrid();
});

function initializeUI() {
    securityGradeList = $("#securityGrade").combox("getJSONData");
}

function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: addHandler,
        updateHandler: showUpdateDialog,
        deleteHandler: deleteHandler,
        enableHandler: enableHandler,
        disableHandler: disableHandler
    });

    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {
                display: "密级", name: "securityGrade", width: 100, minWidth: 40, type: "string", align: "left",
                render: function (item) {
                    return securityGradeList[item.securityGrade];
                }
            },
            {
                display: "启用", name: "status", width: 80, minWidth: 40, type: "string", align: "center",
                render: function (item) {
                    return UICtrl.getStatusInfo(item.status);
                }
            },
            { display: "密码错误次数锁定用户",
                name: "lockUserPasswordErrorTime",
                hide: false,
                width: 150,
                minWidth: 40,
                type: "string",
                align: "right"
            },
            {
                display: "密码最小长度",
                name: "passwordMinimumLength",
                hide: false,
                width: 120,
                minWidth: 40,
                type: "string",
                align: "right"
            },
            {display: "最少数字个数", name: "numberCount", width: 120, minWidth: 60, type: "string", align: "right"},
            {display: "最少大写字母个数", name: "uppercaseCount", width: 150, minWidth: 60, type: "string", align: "right"},
            {display: "最少小写字母个数", name: "lowercaseCount", width: 150, minWidth: 60, type: "string", align: "right"},
            {
                display: "最少特殊字符个数",
                name: "specialCharacterCount",
                width: 150,
                minWidth: 60,
                type: "string",
                align: "right"
            },
            {
                display: "密码有效天数",
                name: "passwordValidityInterval",
                width: 150,
                minWidth: 60,
                type: "string",
                align: "right"
            },
            {display: "内网网段", name: "intranetSegment", width: 150, minWidth: 60, type: "string", align: "right"},
            {
                display: "密码过期提前提醒天数",
                name: "passwordExpireGiveDays",
                width: 150,
                minWidth: 60,
                type: "string",
                align: "right"
            },
            {display: "自动解锁时间（分钟）", name: "autoUnlockTime", width: 150, minWidth: 60, type: "string", align: "right"},
            {
                display: "允许外网登录",
                name: "enableInternetLogin",
                width: 80,
                minWidth: 40,
                type: "string",
                align: "center",
                render: function (item) {
                    return UICtrl.getStatusInfo(item.enableInternetLogin);
                }
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/securityPolicy/sliceQuerySecurityPolicies.ajax',
        pageSize: 20,
        width: '100%',
        height: '100%',
        heightDiff: -8,
        sortName: 'status',
        sortOrder: 'desc',
        toolbar: toolbarOptions,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        checkbox: true,
        onDblClickRow: function (data, rowid, rowdata) {
            doShowUpdateDialog(data.id);
        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

function addHandler() {
    UICtrl.showAjaxDialog({
        title: "添加密码策略",
        width: 700,
        url: web_app.name + '/securityPolicy/showInsertSecurityPolicy.load',
        ok: doInsrtSecurityPolicy,
        close: onDialogCloseHandler
    });
}

function showUpdateDialog() {
	 var id = DataUtil.getUpdateRowId(gridManager);
	 if (!id){return;}
    doShowUpdateDialog(id);
}

function doShowUpdateDialog(id) {
    UICtrl.showAjaxDialog({
        title: "修改安全策略",
        param: { id: id },
        width: 700,
        url: web_app.name + '/securityPolicy/showUpdateSecurityPolicy.load',
        ok: doUpdateSecurityPolicy,
        close: onDialogCloseHandler
    });
}

function doInsrtSecurityPolicy() {
    var _self = this;
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/securityPolicy/insertSecurityPolicy.ajax',
        success: function (data) {
            refreshFlag = true;
            _self.close();
        }
    });
}

function doUpdateSecurityPolicy() {
    var _self = this;
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/securityPolicy/updateSecurityPolicy.ajax',
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function deleteHandler() {
    DataUtil.del({
        action: '/securityPolicy/deleteSecurityPolicies.ajax',
        idFieldName: 'id',
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}

function enableHandler() {
    updateStatus('确实要启用选中数据吗?', 1);
}

function disableHandler() {
    updateStatus('确实要禁用选中数据吗?', 0);
}

function updateStatus(message, status) {
    DataUtil.updateById({
        action: 'securityPolicy/updateSecurityPoliciesStatus.ajax',
        gridManager: gridManager, param: {status: status},
        message: message,
        onSuccess: function () {
            reloadGrid();
        }
    });
}

function query(obj) {
    var params = $(obj).formToJSON();
    UICtrl.gridSearch(gridManager, params);
}
function resetForm(obj) {
	$(obj).formClean();
}
function reloadGrid() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
} 