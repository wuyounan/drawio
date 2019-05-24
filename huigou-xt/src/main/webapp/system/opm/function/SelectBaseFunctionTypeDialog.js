var gridManager, refreshFlag, operateCfg = {}, currentFolderId;

$(function () {
    bindEvents();
    initializeOperateCfg();
    initializeGrid();
    initializeUI();

    function initializeOperateCfg() {
        var path = web_app.name + '/fun/';
        operateCfg.queryAction = path + 'slicedQueryBaseFunctionTypes.ajax';
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

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left" }
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
            selectRowButtonOnly: true
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

function getSelectedData() {
    var data = gridManager.getSelecteds();
    if (!data || data.length == 0) {
        Public.tip("请选择数据.");
        return;
    }

    return data;
}