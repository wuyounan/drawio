var gridManager, refreshFlag, operateCfg = {};

var pageParam = { rootId: 1, rootParentId: 0, parentId: 0, multiSelect: 0 };

$(function () {
    pageParam.multiSelect = Public.getQueryStringByName("multiSelect") || 0;

    initializeOperateCfg();
    initializeUI();
    initializeGrid();
    loadBizManagementTypeTree();

    function initializeOperateCfg() {
        var path = web_app.name + '/management/';
        operateCfg.queryAction = path + 'slicedQueryBizManagementTypes.ajax';
    }

    function initializeUI() {
    	UICtrl.layout("#pageLayout",{leftWidth: 4});
    }

    function initializeGrid() {
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: "100", minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: "100", minWidth: 60, type: "string", align: "left" },
					{ display: "节点类别", name: "nodeKindId", width: 100, minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return item.nodeKindId == CommonNodeKind.Limb ? '分类' : '权限类别';
					    }
					}
					],
            dataAction: "server",
            url: operateCfg.queryAction,
            parms: { parentId: 0 },
            pageSize: 20,
            sortName: 'sequence',
            sortOrder: 'asc',
            width: "99.9%",
            height: "100%",
            heightDiff: -8,
            checkbox: pageParam.multiSelect == 1,
            fixedCellHeight: true,
            selectRowButtonOnly: true
        });
        UICtrl.setSearchAreaToggle(gridManager);
    }

    function loadBizManagementTypeTree() {
        $('#maintree').commonTree({
            loadTreesAction: "/management/queryBizManagementTypes.ajax",
            parentId: 0,
            isLeaf: function (data) {
                return parseInt(data.hasChildren) == 0;
            },
            dataRender: function (data) {
            	return data;
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
        html.push('业务管理权限类别列表');
    } else {
        html.push('<span class="tomato-color">[', data.name, ']</span>业务管理权限类别列');
    }
    pageParam.parentId = data.id;
    $('#layout').layout('setCenterTitle',html.join(''));
    if (gridManager) {
        UICtrl.gridSearch(gridManager, { parentId: pageParam.parentId });
    }
}

function getBizManagementTypeData() {
    var data;
    if (pageParam.multiSelect == 0) {
        data = gridManager.getSelected();
        if (!data) {
            Public.errorTip("请选择数据。");
            return;
        }
    } else {
        data = gridManager.getSelecteds();
        if (!data || data.length == 0) {
            Public.errorTip("请选择数据。");
            return;
        }
    }

    return data;
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}