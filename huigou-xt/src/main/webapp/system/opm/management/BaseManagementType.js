var gridManager, refreshFlag, operateCfg = {};
$(function () {
    initializeOperateCfg();
    initializeUI();
    initializeGrid();
   
    function initializeOperateCfg() {
        var path = '/management/';
        operateCfg.queryAction = path + 'slicedQueryBaseManagementTypes.ajax';
        operateCfg.showInsertAction = path + 'showBaseManagementTypeDetail.load';
        operateCfg.showUpdateAction = path + 'loadBaseManagementType.load';
        operateCfg.insertAction = path + 'insertBaseManagementType.ajax';
        operateCfg.updateAction = path + 'updateBaseManagementType.ajax';
        operateCfg.deleteAction = path + 'deleteBaseManagementType.ajax';
        operateCfg.updateSequenceAction = path + 'updateBaseManagementTypeSequence.ajax';
        operateCfg.moveAction = path + 'updateBaseManagementTypeFolderId.ajax';

        operateCfg.insertTitle = "添加基础管理权限类别";
        operateCfg.updateTitle = "修改基础管理权限类别";
    }

    function initializeGrid() {
        var toolbarparam = { addHandler: showInsertDialog, 
        		             updateHandler: showUpdateDialog, 
        		             deleteHandler: deleteBaseManagementType, 
        		             saveSortIDHandler: updateBaseManagementTypeSequence, 
        		             moveHandler: moveHandler };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "业务管理权限编码", name: "bizManagementTypeCode", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "业务管理权限名称", name: "bizManagementTypeName", width: 160, minWidth: 60, type: "string", align: "left" },
					{ display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return "<input type='text' id='txtSequence_"
									+ item.id + "' class='textbox' value='"
									+ item.sequence + "' />";
					    }
					},
					{ display: "备注", name: "remark", width: 200, minWidth: 60, type: "string", align: "left" }
					],
            dataAction: "server",
            url: web_app.name + operateCfg.queryAction,
            pageSize: 20,
            toolbar: toolbarOptions,
            sortName: "sequence",
            sortOrder: "asc",
            width: "99.8%",
            height: "100%",
            heightDiff: -8,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                doShowUpdateDialog(data.id);
            }
        });
        UICtrl.setSearchAreaToggle(gridManager);
    }

    function initializeUI() {
    	UICtrl.initDefaultLayout();
        $('#maintree').commonTree({
            kindId: CommonTreeKind.BaseManagementType,
            onClick: onFolderTreeNodeClick
        });
    }
});

function onFolderTreeNodeClick(data, folderId) {
    var html = [];
    if (folderId == CommonTreeKind.BaseManagementType) {
        folderId = "";
        html.push('基础管理权限类别列表');
    } else {
        html.push('<span class="tomato-color">[', data.name, ']</span>基础管理权限类别列表');
    }
    $('#layout').layout('setCenterTitle',html.join(''));
    $('#treeFolderId').val(folderId);
    if (gridManager) {
        UICtrl.gridSearch(gridManager, { folderId: folderId });
    }
}

function getId() {
    return $("#id").val();
}

function showInsertDialog() {
    var folderId = $('#treeFolderId').val();
    if (folderId == '') {
        Public.tip('请选择类别树。');
        return;
    }
    UICtrl.showAjaxDialog({
        title: operateCfg.insertTitle,
        width: 400,
        param: {folderId: folderId},
        init: initDialog,
        url: web_app.name + operateCfg.showInsertAction,
        ok: doSaveBaseManagementType,
        close: onDialogCloseHandler
    });
}

function doShowUpdateDialog(id) {
    UICtrl.showAjaxDialog({
        url: web_app.name +  operateCfg.showUpdateAction,
        param: { id: id },
        title: operateCfg.updateTitle,
        width: 400,
        init: initDialog,
        ok: doSaveBaseManagementType,
        close: onDialogCloseHandler
    });
}

function showUpdateDialog() {
	var id = DataUtil.getUpdateRowId(gridManager);
	if (!id){ return; }
    doShowUpdateDialog(id);
}

function doSaveBaseManagementType() {
    var _self = this;
    var id = getId();
    var param = {};
    if (!id) {//新增时加入目录ID
        param['folderId'] = $('#treeFolderId').val();
    }
    $('#submitForm').ajaxSubmit({ url:web_app.name +  (id ? operateCfg.updateAction : operateCfg.insertAction),
        param: param,
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function deleteBaseManagementType() {
    DataUtil.del({ action: operateCfg.deleteAction, gridManager: gridManager, onSuccess: reloadGrid });
}

function updateBaseManagementTypeSequence() {
    DataUtil.updateSequence({ action: operateCfg.updateSequenceAction, gridManager: gridManager, onSuccess: reloadGrid });
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

function showSelectBizManagementTypeDialog() {
    var params = {parent:window['ajaxDialog'], confirmHandler: onSelectBizManagementTypeHandler, closeHandler: function () { } };
    OpmUtil.showSelectBizManagementTypeDialog(params);
}

function onSelectBizManagementTypeHandler() {
    _self = this;
    var data = this.iframe.contentWindow.getBizManagementTypeData();
    if (data == null) return;
    if (data.nodeKindId != CommonNodeKind.Leaf) {
        Public.errorTip("请选择业务权限类别。");
        return;
    }

    $("#bizManagementTypeId").val(data.id);
    $("#bizManagementTypeName").val(data.name);

    _self.close();
}

function initDialog(doc) {
    $("#btnSelectBizManagementType").click(
		function () {
		    showSelectBizManagementTypeDialog();
		}
	);
}

//移动
function moveHandler() {
    UICtrl.showMoveTreeDialog({
        gridManager: gridManager, title: '移动基础管理权限', kindId: CommonTreeKind.BaseManagementType,
        save: function (parentId) {
            DataUtil.updateById({ action: operateCfg.moveAction,
                gridManager: gridManager, param: { folderId: parentId },
                onSuccess: function () {
                    reloadGrid();
                }
            });
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