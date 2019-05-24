var treeManager, gridManager, parentId = 'root', refreshFlag = false, toolbar;

$(function() {
	loadOrgTemplateTreeView();
	initializeGrid();
	InitializeUI();
	
	function initializeGrid() {
		var toolbarOptions = {
			items: [
			         { id: "addOrg", text: "添加机构", click: addOrgTemplate,  img: "fa-plus" }, 
			         { id: "addDept", text: "添加部门", click: addOrgTemplate,  img: "fa-plus" },
			         { id: "addPosition", text: "添加岗位", click: addOrgTemplate,  img: "fa-plus" }, 
			         { id: "delete", text: "删除", click: deleteOrgTemplate, img: "fa-trash-o" },
			         {id: "saveSequence", text: '保存排序号',click: updateOrgTemplateSequence, img:"fa-floppy-o" }
			]
		};

		gridManager = UICtrl.grid("#maingrid", {
			columns: [
					{ display: "编码", name: "code", width: "120", minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: "180", minWidth: 60, type: "string", align: "left" },
					{ display: "排序号", name: "sequence", width: "60", minWidth: 60, type: "string", align: "left",
						render: function(item) {
							return "<input type='text' id='txtSequence_" + item.id  + "' class='textbox' value='" + item.sequence + "' />";
						}
					} ],
			dataAction: "server",
			url: web_app.name + '/orgTemplate/queryOrgTemplates.ajax',
			parms: { parentId: parentId },
			usePager: false,
			sortName: "sequence", 
			sortOrder: "asc",
			toolbar: toolbarOptions,
			width: "99.8%",  
			height: "100%",	
			heightDiff: -8,
			checkbox: true, 
			fixedCellHeight: true, 
			selectRowButtonOnly: true
		});
		toolbar = $('#maingrid').prev('div.ui-toolbar').data('ui_tool_bar');
		toolbar.disable("toolbar_addDept", "toolbar_addPosition");
		UICtrl.setSearchAreaToggle(gridManager);
	}

	function InitializeUI() {
		UICtrl.initDefaultLayout();
	}

	function addOrgTemplate(id) {
		var kindId = "";
		switch (id) {
		case "toolbar_addOrg":
			kindId = "ogn";
			break;
		case "toolbar_addDept":
			kindId = "dpt";
			break;
		case "toolbar_addPosition":
			kindId = "pos";
			break;
		}
		var params = { orgKindId: kindId, confirmHandler: doSaveOrgTemplate, closeHandler: onDialogCloseHandler, isMultipleSelect: "true" };
		OpmUtil.showSelectOrgTypeDialog(params);
	}

	function deleteOrgTemplate() {
		var action = "orgTemplate/deleteOrgTemplates.ajax";
		DataUtil.del({ action: action,  gridManager: gridManager, onSuccess: reloadGrid });
	}
});

function disableToolButtons(){
	toolbar.disable("toolbar_addOrg","toolbar_addDept", "toolbar_addPosition", "toolbar_delete", "toolbar_saveSequence");
}

function loadOrgTemplateTreeView() {
	if (treeManager)
		treeManager.clear();
	Public.ajax(web_app.name + "/orgTemplate/queryOrgTemplates.ajax", {
		parentId: parentId
	}, function(data) {
		treeManager = UICtrl.tree("#maintree", {
			data: data.Rows,
			idFieldName: "id",
			parentIDFieldName: "parentId",
			textFieldName: "name",
			checkbox: false,
			iconFieldName: "icon",
			btnClickToToggleOnly: true,
			nodeWidth: 180,
			isLeaf: function(data) {
				data.children = [];
				data.icon = OpmUtil.getOrgImgUrl (data.orgKindId, true);
				return data.hasChildren==0;
			},
			onBeforeExpand: onBeforeExpand,
			onClick: function(node) {
				if (!node || !node.data)
					return;
				if (parentId != node.data.id) {
					var caption = "机构模板列表";
					if (node.data.parentId == 'root') {
						$("#layout").layout("setCenterTitle", caption);
					} else {
						$("#layout").layout("setCenterTitle", "<span class=\"tomato-color\">["
								+ node.data.name + "]</span>"  + caption);
					}
					parentId = node.data.id;
					parentOrgKindId = node.data.orgKindId;

					gridManager.options.parms.parentId = parentId;
					gridManager.options.newPage = 1;
					
					disableToolButtons();
					switch (parentOrgKindId) {
					case "root":
						toolbar.enable("toolbar_addOrg", "toolbar_delete", "toolbar_saveSequence");
						break;
					case "ogn":
						toolbar.enable("toolbar_addOrg", "toolbar_addDept", "toolbar_delete", "toolbar_saveSequence");
						break;
					case "dpt":
						toolbar.enable("toolbar_addPosition", "toolbar_addDept", "toolbar_delete", "toolbar_saveSequence");
					case "pos":
						break;
					}
					reloadGrid2();
				}
			}
		});
	});
}

function doSaveOrgTemplate() {
	var data = this.iframe.contentWindow.getOrgTypeData();
	if (!data)
		return;
	
	var orgTypeIds = new Array();
	for (var i = 0; i < data.length; i++) {
		orgTypeIds[i] = data[i].id;
	}
	
	var _self = this;

	var params = {};
	params.parentId = parentId;
	params.orgTypeIds = $.toJSON(orgTypeIds);
	Public.ajax(web_app.name + "/orgTemplate/insertOrgTemplates.ajax",
			params, function() {
				refreshFlag = true;
				//_self.close();
			});
}

function onDialogCloseHandler() {
	if (refreshFlag) {
		reloadGrid();
		refreshFlag = false;
	}
}

function updateOrgTemplateSequence() {
	var action = "orgTemplate/updateOrgTemplateSequence.ajax";
	DataUtil.updateSequence({action: action,  gridManager: gridManager, onSuccess: reloadGrid});
}

function reloadGrid() {
	refreshNode();
	reloadGrid2();
}

function reloadGrid2() {
	var params = $(this.form).formToJSON();
	UICtrl.gridSearch(gridManager, params);
}

function onBeforeExpand(node) {
	if (node.data.hasChildren) {
		if (!node.data.children || node.data.children.length == 0) {
			Public.ajax(web_app.name + "/orgTemplate/queryOrgTemplates.ajax",
					{
						parentId: node.data.id
					}, function(data) {
						treeManager.append(node.target, data.Rows);
					});
		}
	}
}

function refreshNode(pNodeData) {
	var parentData;
	if (pNodeData)
		parentData = pNodeData;
	else
		parentData = treeManager.getDataByID(parentId);
	if (parentData) {
		if (parentData.children && parentData.children.length > 0) {
			for (var i = 0; i < parentData.children.length; i++) {
				treeManager.remove(parentData.children[i].treedataindex);
			}
		}
		Public.ajax(web_app.name + "/orgTemplate/queryOrgTemplates.ajax",
				{
					parentId: parentData.id
				}, function(data) {
					if (!data.Rows || data.Rows.length == 0) {
						var pn = treeManager.getParent(parentData.treedataindex);
						if (pn)
							refreshNode(pn);
					} else {
						treeManager.append(parentData, data.Rows);
					}
				});
	}
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}