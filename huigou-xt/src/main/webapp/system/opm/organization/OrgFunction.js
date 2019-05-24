var gridManager, orgFunBizManTypeAuthorizeGrid, refreshFlag, operateCfg = {};

var selectMoveDialog;

var pageParam = { rootId: 1, rootParentId: 0, parentId: 0, orgFunctionId: 0, organKindData: [] };

$(function () {
    pageParam.organKindData = $("#organKindId").combox("getFormattedData");
    bindEvents();
    initializeOperateCfg();
    loadOrgFunctionTree();
    initializeGrid();
    initializeOrgFunBizManTypeAuthorizeGrid();
    initializeUI();

    function initializeOperateCfg() {
        var path = web_app.name + '/org/';
        operateCfg.queryTreeAction = path + 'queryOrgFunctions.ajax';
        operateCfg.queryAction = path + 'slicedQueryOrgFunctions.ajax';
        operateCfg.showInsertAction = path + 'showOrgFunctionDetail.load';
        operateCfg.showUpdateAction = path + 'loadOrgFunction.load';
        operateCfg.insertAction = path + 'insertOrgFunction.ajax';
        operateCfg.updateAction = path + 'updateOrgFunction.ajax';
        operateCfg.deleteAction = path + 'deleteOrgFunction.ajax';
        operateCfg.updateSequenceAction = path + 'updateOrgFunctionSequence.ajax';
        operateCfg.moveAction = path + 'moveOrgFunction.ajax';

        operateCfg.insertOrgFunBizManTypeAuthorizeAction = path + 'insertOrgFunBizManTypeAuthorize.ajax';
        operateCfg.updateOrgFunBizManTypeAuthorizeAction = path + 'updateOrgFunBizManTypeAuthorize.ajax';
        operateCfg.deleteOrgFunBizManTypeAuthorizeAction = path + 'deleteOrgFunBizManTypeAuthorize.ajax';
        operateCfg.slicedQueryOrgFunBizManTypeAuthorizeAction = path + 'slicedQueryOrgFunBizManTypeAuthorizes.ajax';

        operateCfg.insertTitle = "添加组织职能类别";
        operateCfg.updateTitle = "修改组织职能类别";
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
        var toolbarparam = { addHandler: showInsertDialog, updateHandler: showUpdateDialog, deleteHandler: deleteOrgFunction, saveSortIDHandler: updateOrgFunctionSequence, moveHandler: moveHandler };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "全名称", name: "fullName", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "节点类别", name: "nodeKindId", width: 100, minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return item.nodeKindId == CommonNodeKind.Limb ? '分类' : '职能';
					    }
					},
					{ display: "排序号", name: "sequence", width: "60", minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return "<input type='text' id='txtSequence_"
									+ item.id + "' class='textbox' value='"
									+ item.sequence + "' />";
					    }
					}
					],
            dataAction: "server",
            url: operateCfg.queryAction,
            parms: {
                parentId: pageParam.rootId
            },
            pageSize: 20,
            sortName: 'sequence',
            sortOrder: 'asc',
            toolbar: toolbarOptions,
            width: "100%",
            height: "50%",
            heightDiff: -10,
            checkbox: false,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                doShowUpdateDialog(data.id);
            },
            onSelectRow: function (data, rowindex, rowobj) {
                if (pageParam.orgFunctionId != data.id) {
                    pageParam.orgFunctionId = data.id;
                    if (!pageParam.orgFunctionId) {
                        pageParam.orgFunctionId = 0;
                    }
                    searchOrgFunBizManTypeAuthorizeGrid();
                }
            }
        });
        UICtrl.setSearchAreaToggle(gridManager);
    }

    function initializeOrgFunBizManTypeAuthorizeGrid() {
        var toolbarparam = { addHandler: function () {
            if (pageParam.orgFunctionId == 0) {
                Public.errorTip("请选择组织职能。");
                return;
            }
            showSelectBizManagementTypeDialog();
        }, 
        saveHandler: saveOrgFunBizManTypeAuthorize,
        deleteHandler: deleteOrgFunBizManTypeAuthorize
        };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        orgFunBizManTypeAuthorizeGrid = UICtrl.grid("#orgFunBizManTypeAuthorizeGrid", {
            columns: [
					{ display: "公司类别", name: "organKindId", width: 100, minWidth: 60, type: "string", align: "left",
					    editor: { type: 'combobox', data: pageParam.organKindData, required: true },
					    render: function (item) {
					        for (var i = 0; i < pageParam.organKindData.length; i++) {
					            if (pageParam.organKindData[i].value == item.organKindId) {
					                return pageParam.organKindData[i].text;
					            }
					        }
					        return "";
					    }
					},
					{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "fullName", width: 200, minWidth: 60, type: "string", align: "left" }
					],
            dataAction: "server",
            url: operateCfg.slicedQueryOrgFunBizManTypeAuthorizeAction,
            parms: {
                orgFunctionId: pageParam.orgFunctionId
            },
            pageSize: 20,
            toolbar: toolbarOptions,
            width: "100%",
            height: "50%",
            heightDiff: -10,
            enabledEdit: true,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onLoadData: function () {
                return pageParam.orgFunctionId >= 0;
            }
        });
    }

    function initializeUI() {
        UICtrl.layout("#layout", { leftWidth: 200, heightDiff: -5 });
    }

    function loadOrgFunctionTree() {
        $('#maintree').commonTree({
            loadTreesAction: "/org/queryOrgFunctions.ajax",
            parentId: pageParam.rootParentId,
            isLeaf: function (data) {
                return parseInt(data.hasChildren) == 0;
            },
            onClick: function (data) {
                onTreeNodeClick(data);
            },
            IsShowMenu: false
        });
    }
});

function onTreeNodeClick(data) {
    var html = [];
    if (data.id == pageParam.rootId) {
        html.push('组织职能列表');
    } else {
        html.push('<font style="color:Tomato;font-size:14px;">[', data.name, ']</font>组织职能类别列表');
    }
    pageParam.parentId = data.id;
    pageParam.orgFunctionId = 0;
    $('.l-layout-center .l-layout-header').html(html.join(''));
    if (gridManager) {
        UICtrl.gridSearch(gridManager, { parentId: pageParam.parentId });
        searchOrgFunBizManTypeAuthorizeGrid();
    }
}

function getId() {
    return $("#id").val();
}

function showInsertDialog() {
    if (pageParam.parentId == pageParam.rootParentId) {
        Public.tip('请选择类别树！');
        return;
    }

    UICtrl.showAjaxDialog({
        url: operateCfg.showInsertAction,
        title: operateCfg.insertTitle,
        param: { parentId: pageParam.parentId },
        width: 340,
        ok: doSaveOrgFunction,
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
        width: 340,
        ok: doSaveOrgFunction,
        close: onDialogCloseHandler
    });
}

function showUpdateDialog() {
    var row = gridManager.getSelectedRow();
    if (!row) {
        Public.tip("请选择数据！");
        return;
    }
    doShowUpdateDialog(row.id);
}

function doSaveOrgFunction() {
    var _self = this;
    var id = getId();

    $('#submitForm').ajaxSubmit({
        url: web_app.name + (id ? operateCfg.updateAction : operateCfg.insertAction),
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function deleteOrgFunction() {
    DataUtil.del({ action: operateCfg.deleteAction, gridManager: gridManager, onSuccess: reloadGrid });
}

function updateOrgFunctionSequence() {
    DataUtil.updateSequence({ action: operateCfg.updateSequenceAction, gridManager: gridManager, onSuccess: reloadGrid });
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function doMoveOrgFunction(ids) {
    var newParentNode = $('#movetree').commonTree('getSelected');
    if (newParentNode == null) {
        Public.errorTip('请选择移动到的节点。');
        return false;
    }
    var newParentId = newParentNode.id;
    if (!newParentId) {
        Public.errorTip('请选择移动到的节点。');
        return false;
    }

    if (newParentId == pageParam.parentId) {
        Public.errorTip('您选择的目标节点和源节点一样，不能移动。');
        return false;
    }

    var params = {};
    params.parentId = newParentId;
    params.ids = $.toJSON(ids);

    Public.ajax(operateCfg.moveAction, params, function (data) {
        selectMoveDialog.hide();
        reloadGrid();
    });
}

function reloadGrid() {
    $("#maintree").commonTree('refresh', pageParam.parentId);
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

//移动
function moveHandler() {
    var ids = DataUtil.getSelectedIds({
        gridManager: gridManager,
        idFieldName: "id"
    });

    if (!ids) {
        Public.errorTip("请选择要移动的组织职能类别。");
        return;
    }

    if (!selectMoveDialog) {
        selectMoveDialog = UICtrl.showDialog({
            title: "移动到...",
            width: 350,
            content: '<div style="overflow-x: hidden; overflow-y: auto; width: 340px;height:250px;"><ul id="movetree"></ul></div>',
            init: function () {
                $('#movetree').commonTree({
                    loadTreesAction: "/org/queryOrgFunctions.ajax",
                    idFieldName: 'id',
                    IsShowMenu: false
                });
            },
            ok: function () {
                doMoveOrgFunction.call(this, ids);
            },
            close: function () {
                this.hide();
                return false;
            }
        });
    } else {
        $('#movetree').commonTree('refresh');
        selectMoveDialog.show().zindex();
    }
}

function saveOrgFunBizManTypeAuthorize() {
    var data = DataUtil.getGridData({ gridManager: orgFunBizManTypeAuthorizeGrid });
    if (!data) {
        return false;
    }

    Public.ajax(operateCfg.updateOrgFunBizManTypeAuthorizeAction,
        { data: Public.encodeJSONURI(data) }, function () {
            orgFunBizManTypeAuthorizeGrid.loadData();
        }
    );
}

function deleteOrgFunBizManTypeAuthorize() {
    DataUtil.delSelectedRows({ action: operateCfg.deleteOrgFunBizManTypeAuthorizeAction,
        idFieldName: "id",
        gridManager: orgFunBizManTypeAuthorizeGrid,
        onSuccess: function () {
            orgFunBizManTypeAuthorizeGrid.loadData();
        }
    });
}

function showSelectBizManagementTypeDialog() {
    var params = { confirmHandler: onSelectBizManagementTypeHandler, multiSelect: 1, closeHandler: function () { } };
    OpmUtil.showSelectBizManagementTypeDialog(params);
}

function onSelectBizManagementTypeHandler() {
    _self = this;
    var data = this.iframe.contentWindow.getBizManagementTypeData();
    //组装数据
    var bizManagementTypeIds = [];
    $.each(data, function (i, o) {
        bizManagementTypeIds.push(o.id);
    });
    //保存数据
    var params = {};
    params.orgFunctionId = pageParam.orgFunctionId;
    params.bizManagementTypeIds = $.toJSON(bizManagementTypeIds);
    Public.ajax(operateCfg.insertOrgFunBizManTypeAuthorizeAction, params, function () {
        searchOrgFunBizManTypeAuthorizeGrid();
    });

    _self.close();
}

function searchOrgFunBizManTypeAuthorizeGrid() {
    orgFunBizManTypeAuthorizeGrid.options.parms.orgFunctionId = this.pageParam.orgFunctionId;
    orgFunBizManTypeAuthorizeGrid.loadData();
}