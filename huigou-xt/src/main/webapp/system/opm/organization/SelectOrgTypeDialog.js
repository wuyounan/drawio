var gridManager, orgKindId, isMultipleSelect, folderId;
$(function() {
	getQueryParameters();
	initializeUI();
	initializeTree();
	initializeGrid();
		
	
	function getQueryParameters() {
		orgKindId = $("#orgKindId").val();
		isMultipleSelect = $("#isMultipleSelect").val();
	}

	function initializeUI() {
		UICtrl.layout("#pageLayout",{leftWidth:4});
		var tabCaption = getTabCaption();
		$('#layout').layout('setLeftTitle', tabCaption + "树");
		$('#layout').layout('setCenterTitle',tabCaption + "列表");
	}
	
	function initializeGrid() {
		gridManager = UICtrl.grid("#orgTypeGrid", {
			columns: [{ display: "编码", name: "code", width: "100", minWidth: 60, type: "string", align: "left" },
						{ display: "名称", name: "name", width: "100", minWidth: 60, type: "string", align: "left" },
						{ display: "排序号", name: "sequence", width: "60", minWidth: 60, type: "string", align: "left" }, 
						{ display: "状态", name: "status", width: "60", minWidth: 60, type: "string", align: "center",
							render: function(item) {
								return UICtrl.getStatusInfo(item.status);						
							}
						}],
			dataAction: "server",
			url: web_app.name + '/orgType/slicedQueryOrgTypes.ajax',
			parms: {folderId: 0, orgKindId: orgKindId },
			pageSize: 20,
			sortName: "sequence",
			sortOrder: "asc",
			width: "100%",
			height: "100%",
			heightDiff: -8,
			checkbox: true,
			fixedCellHeight: true,
			selectRowButtonOnly: true
		});

		UICtrl.setSearchAreaToggle(gridManager, false);
	}
	
	function initializeTree() {
        $('#maintree').commonTree(
				{
				    kindId: getCommonTreeKindId(),
				    onClick: function (data) {
				        onFolderTreeNodeClick(data);
				    },
				    IsShowMenu: false
				});
    }
});

function onFolderTreeNodeClick(data) {
	if (folderId != data.id) {
		var html = [];
		if (!parseInt(data.parentId)) {
			html.push(getTabCaption() + "列表");
			folderId = 0;
		} else {
			html.push('<span class="tomato-color">[', data.name, ']</span>', getTabCaption() + "列表");
			folderId = data.id;
		}
		$('#pageLayout').layout('setCenterTitle',html.join(''));
		reloadGrid();
	}
}

/*
function refreshFolderTree() {
	loadFolderTree({
		kindId: getCommonTreeKindId(),
		onClick: onFolderTreeNodeClick,
		isExpand: false
	});
}*/

function reloadGrid() {
	UICtrl.gridSearch(gridManager,{ folderId:folderId });
}

function getTabCaption() {
	var result;
	switch (orgKindId) {
	case "ogn":
		result = "机构类型";
		break;
	case "dpt":
		result = "部门类型";
		break;
	case "pos":
		result = "岗位类型";
		break;
	case "prj":
		result = "项目组类型";
	default:
		result = "";
	}
	return result;
}

function getCommonTreeKindId() {
	switch (orgKindId) {
	case "ogn":
		result = 1;
		break;
	case "dpt":
		result = 2;
		break;
	case "pos":
		result = 3;
		break;
	case "prj":
		result = 4;
		break;
	default:
		result = -1;
	}
	return result;
}

function getOrgTypeData(){
	var data = gridManager.getSelecteds();
    if (!data || data.length == 0) {
        Public.tip("请选择数据 。");
        return;
    }

    if (isMultipleSelect == "false" && data.length > 1) {
    	Public.tip('请选择一条数据。');
        return;
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