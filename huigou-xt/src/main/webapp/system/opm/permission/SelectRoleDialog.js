var gridManager, parentId = "0";

$(function () {
	initializeUI();
    initializeTree();
    initializeGrid();

    function initializeUI() {
    	UICtrl.layout("#pageLayout",{leftWidth: 3});
    }

    function initializeTree() {
        $('#maintree').commonTree({
        	loadTreesAction : "access/queryRoles.ajax",
            isLeaf: function (data) {
                return data.hasChildren == 0;
            },
            dataRender: function (data) {
            	return data;
            },
            onClick: onTreeNodeClick,
            IsShowMenu: false
        });
    }

    function initializeGrid() {
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: "100", minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: "100", minWidth: 60, type: "string", align: "left" },
					{ display: "类别", name: "roleKindIdTextView", width: "80", minWidth: 60, type: "string", align: "center" },
                    { display: "状态", name: "status", width: "60", minWidth: 60, type: "string", align: "center",
                        render: function (item) {
                        	return UICtrl.getStatusInfo(item.status);
                        }
                    }
                     ],
            dataAction: "server",
            url: web_app.name + '/access/slicedQueryRoles.ajax',
            pageSize: 20,
            width: "100%",
            height: "100%",
            sortName: 'sequence',
            sortOrder: 'asc',
            heightDiff: -7,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true
        });

        UICtrl.setSearchAreaToggle(gridManager, false);
    }
});

function onTreeNodeClick(data) {
	var title = "角色列表";
    if (data.id == '1') {
        parentId = "0";
    }else{
    	title = "<span class=\"tomato-color\">["  + data.name + "]</span>角色列表";
        parentId = data.id;
    }
    $("pageLayout").layout("setCenterTitle", title);
    if (gridManager) {
        UICtrl.gridSearch(gridManager, { parentId: parentId, nodeKindId: 2 });
    }
}

function reloadGrid() {
    UICtrl.gridSearch(gridManager, { folderId: folderId });
}

function getRoleData() {
    var data = gridManager.getSelecteds();
    var roles = [];
    $.each(data, function(i, o){
    	if (o.nodeKindId == 2){
    		roles.push(o);
    	}
    })
    if (roles.length == 0) {
        Public.tip("请选择角色数据。");
        return;
    }
    
    return roles;
}

function query(obj) {
	var params = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, params);
}

function resetForm(obj) {
	$(obj).formClean();
}