var gridManager, refreshFlag, operateCfg = {}, currentFolderId;

$(function () {
    bindEvents();
    initializeOperateCfg();
    initializeGrid();
    initializeUI();

    function initializeOperateCfg() {
        var path = web_app.name + '/fun/';
        operateCfg.queryAction = path + 'slicedQueryBaseFunctionTypes.ajax';
        operateCfg.showInsertAction = path + 'showInsertBaseFunctionnType.load';
        operateCfg.showUpdateAction = path + 'showUpdateBaseFunctionType.load';
        operateCfg.insertAction = path + 'insertBaseFunctionType.ajax';
        operateCfg.updateAction = path + 'updateBaseFunctionType.ajax';
        operateCfg.deleteAction = path + 'deleteBaseFunctionType.ajax';
        operateCfg.updateSequenceAction = path + 'updateBaseFunctionTypeSequence.ajax';
        operateCfg.moveAction = path + 'updateBaseFunctionTypeFolderId.ajax';

        operateCfg.insertTitle = "添加基础职能角色";
        operateCfg.updateTitle = "修改基础职能角色";
    }

    function bindEvents() {
        $("#btnQuery").click(function () {
            var params = $(this.form).formToJSON();
            UICtrl.gridSearch(gridManager, params);
        });
        $("#btnReset").click(function () {
            $(this.form).formClean();
        });
    }

    function initializeGrid() {
        var toolbarparam = { addHandler: showInsertDialog, updateHandler: showUpdateDialog, deleteHandler: deleteBaseFunctionType, saveSortIDHandler: updateBaseFunctionTypeSequence, moveHandler: moveHandler };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return "<input type='text' id='txtSequence_"
									+ item.id + "' class='textbox' value='"
									+ item.sequence + "' />";
					    }
					}
					],
            dataAction: "server",
            url: operateCfg.queryAction,
            pageSize: 20,
            toolbar: toolbarOptions,
            sortName: "sequence",
            sortOrder: "asc",
            width: "99.8%",
            height: "100%",
            heightDiff: -10,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                doShowUpdateDialog(data[getIdFieldName()]);
            }
        });
        UICtrl.setSearchAreaToggle(gridManager);
    }

    function initializeUI() {
        UICtrl.initDefaultLayout();
        currentFolderId == CommonTreeKind.BaseFunctionType;
        $('#maintree').commonTree({
            kindId: CommonTreeKind.BaseFunctionType,
            onClick: onFolderTreeNodeClick
        });
    }
});

function onFolderTreeNodeClick(data, folderId) {
    if (folderId != currentFolderId) {
        currentFolderId = folderId;
        
        var html = [];
        if (currentFolderId == CommonTreeKind.BaseFunctionType) {
            html.push('基础职能角色');
        } else {
            html.push('<font style="color:Tomato;font-size:14px;">[', data.name, ']</font>基础职能角色');
        }
        $('.l-layout-center .l-layout-header').html(html.join(''));

        if (gridManager) {
            UICtrl.gridSearch(gridManager, { folderId: currentFolderId });
        }
    }
}

function getId() {
    return $("#id").val();
}

function showInsertDialog() {
    if (currentFolderId == CommonTreeKind.BaseFunctionType) {
        Public.tip('请选择类别树！');
        return;
    }

    UICtrl.showAjaxDialog({
        title: operateCfg.insertTitle,
        param: {
            folderId: currentFolderId
        },
        width: 300,
        url: operateCfg.showInsertAction,
        ok: doSaveBaseFunctionType,
        close: onDialogCloseHandler
    });
}

function doShowUpdateDialog(id) {
    UICtrl.showAjaxDialog({
        url: operateCfg.showUpdateAction,
        param: {
            id: id
        },
        title: operateCfg.updateTitle,
        width: 300,
        ok: doSaveBaseFunctionType,
        close: onDialogCloseHandler
    });
}

function showUpdateDialog() {
    var row = gridManager.getSelectedRow();
    if (!row) {
        Public.tip("请选择数据！");
        return;
    }
    doShowUpdateDialog(row[getIdFieldName()]);
}

function doSaveBaseFunctionType() {
    var _self = this;
    var id = getId();
    var params = {};
    if (!id) {//新增时加入目录ID
        params.folderId = currentFolderId;
    }

    $('#submitForm').ajaxSubmit({ url: web_app.name + (id ? operateCfg.updateAction : operateCfg.insertAction),
        param: params,
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function getIdFieldName() {
    return "id";
}

function deleteBaseFunctionType() {
    DataUtil.del({ action: operateCfg.deleteAction, idFieldName: getIdFieldName(), gridManager: gridManager, onSuccess: reloadGrid });
}

function updateBaseFunctionTypeSequence() {
    DataUtil.updateSequence({ action: operateCfg.updateSequenceAction, gridManager: gridManager, idFieldName: getIdFieldName(), onSuccess: reloadGrid });
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

//移动
function moveHandler() {
    UICtrl.showMoveTreeDialog({
        gridManager: gridManager, title: '移动基础职能角色', kindId: CommonTreeKind.BaseFunctionType,
        save: function (parentId) {
            DataUtil.updateById({ action: operateCfg.moveAction,
                gridManager: gridManager, param: { folderId: parentId },
                idFieldName: getIdFieldName(),
                onSuccess: function () {
                    reloadGrid();
                }
            });
        }
    });
}