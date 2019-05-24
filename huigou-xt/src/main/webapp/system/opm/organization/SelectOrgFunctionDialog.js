var gridManager, operateCfg = {};

var pageParam = { rootId: 1, rootParentId: 0, parentId: 0 };

$(function () {
    bindEvents();
    initializeOperateCfg();
    loadOrgFunctionTree();
    initializeGrid();
    initializeUI();

    function initializeOperateCfg() {
        var path = web_app.name + '/org/';
        operateCfg.queryTreeAction = path + 'queryOrgFunctions.ajax';
        operateCfg.queryAction = path + 'slicedQueryOrgFunctions.ajax';
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
					{ display: "全名称", name: "fullName", width: 200, minWidth: 60, type: "string", align: "left" }
					],
            dataAction: "server",
            url: operateCfg.queryAction,
            parms: {
                parentId: pageParam.rootId
            },
            pageSize: 20,
            sortName: 'sequence',
            sortOrder: 'asc',
            width: "100%",
            height: "100%",
            heightDiff: -10,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true
        });
        UICtrl.setSearchAreaToggle(gridManager);
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
    $('.l-layout-center .l-layout-header').html(html.join(''));
    if (gridManager) {
        UICtrl.gridSearch(gridManager, { parentId: pageParam.parentId });
    }
}

function getOrgFunctionData(){
	var data = gridManager.getSelecteds();
    if (!data || data.length == 0) {
        Public.tip("请选择数据.");
        return;
    }

    return data;
}