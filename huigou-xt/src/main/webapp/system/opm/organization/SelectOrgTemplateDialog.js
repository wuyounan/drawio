var treeManager, gridManager, parentId, refreshFlag = false, lastSelectedId = 0;
$(function() {
	InitializeUI();
	loadOrgTemplateTreeView();
	initializeGrid();	

	function InitializeUI() {
		UICtrl.layout("#pageLayout",{leftWidth: 4});
	}
	
	function initializeGrid() {
		gridManager = UICtrl.grid("#maingrid", {
			columns: [
					{ display: "编码", name: "code", width: "100", minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: "100", minWidth: 60, type: "string", align: "left" },
					{ display: "排序号", name: "sequence", width: "60", minWidth: 60, type: "string", align: "left"}
					],
			dataAction: "server",
			url: web_app.name + '/orgTemplate/queryOrgTemplates.ajax',
			parms: { parentId: OrgTemplate.ROOT_PARENT_ID_VALUE },
			pageSize: 20,
			width: "100%",
			height: "100%",
			heightDiff: -8,
			checkbox: true,
			fixedCellHeight: true,
			selectRowButtonOnly: true
		});
		UICtrl.setSearchAreaToggle(gridManager, false);
	}

	function loadOrgTemplateTreeView() {
		$('#maintree').commonTree({	
					loadTreesAction: "orgTemplate/queryOrgTemplates.ajax",
					parentId: OrgTemplate.ROOT_PARENT_ID_VALUE,
					isLeaf: function(data) {					
						data.nodeIcon = OpmUtil.getOrgImgUrl (data.orgKindId, true, false);
						return data.hasChildren == 0;
					},
					onClick: function(data) {
						if (data && lastSelectedId != data.id) {
							lastSelectedId = data.id;
							reloadGrid();
						}
					},
					IsShowMenu: false
				});
	}
});



function reloadGrid() {
	var params = {parentId: lastSelectedId};
	UICtrl.gridSearch(gridManager, params);
}

function getOrgTemplateData(){
	var data = gridManager.getSelecteds();
    if (!data || data.length == 0) {
        Public.tip("请选择数据。");
        return;
    }

    if (data.length > 1) {
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