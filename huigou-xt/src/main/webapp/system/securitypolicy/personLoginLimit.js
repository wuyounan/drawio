var gridManager = null, refreshFlag = false, personId = "",fullId = "";
$(document).ready(function() {
	initializeUI();
	initializeTreeView();
	initializeGrid();
});

function initializeUI() {
	UICtrl.initDefaultLayout();
	addHandler();
}

function initializeTreeView() {
	$('#maintree').commonTree({
				loadTreesAction: 'org/queryOrgs.ajax',
				parentId: 'orgRoot',
				getParam: function(e) {
					if (e) {
						return { showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt,pos,psm", showMasterPsm: 1, showPosition: 0 };
					}
					return { showDisabledOrg: 0 };
				},
				changeNodeIcon: function(data) {
					data[this.options.iconFieldName] = OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
				},
				IsShowMenu: false,
				onClick: function(data) {
					personId = data.personId || "";
					fullId = data.fullId;
					onFolderOrgTreeNodeClick(data.fullName, personId,data.fullId);
				}
			});
}

function onFolderOrgTreeNodeClick(fullName, personId, fullId) {
	if (fullName){
		fullName = "[" + fullName + "]";
	}
	$("#layout").layout("setCenterTitle", "<span class=\"tomato-color\">" + fullName+ "</span>登录限制列表")
	var params = $("#queryMainForm").formToJSON();
	params.fullId = fullId;
	UICtrl.gridSearch(gridManager, params);
}

function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: addHandler,
		deleteHandler: deleteHandler,
		saveSortIDHandler: updateSequence,
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
				{display: "登录名",name: "loginName",width: 120,minWidth: 60,type: "string",align: "left"},
				{display: "机器编码",name: "code",width: 140,minWidth: 60,type: "string",align: "left"},
				{display: "机器名称",name: "name",width: 120,minWidth: 60,type: "string",align: "left"},
				{display: "MAC地址",name: "macAddress",width: 140,minWidth: 60,type: "string",align: "left"},
				{display: "IP地址",name: "ipAddress",width: 120,minWidth: 60,type: "string",align: "left"},
				{display: "排序号",name: "sequence",width: 60,minWidth: 60,type: "string",align: "center",
					render: function(item) {
						return "<input type='text' id='txtSequence_" + item.id
								+ "' class='textbox' value='" + item.sequence
								+ "' />";
					}
				} ],
		dataAction: 'server',
		url: web_app.name + '/personLoginLimit/sliceQueryPersonLoginLimits.ajax',
		parms: {
			fullId: fullId
		},
		toolbar: toolbarOptions,
		width: '99.8%',
		height: '100%',
		heightDiff: -5,
		sortName: 'sequence',
		sortOrder: 'asc',
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true
	});
}

function addHandler() {
	$("#toolbar_menuAdd").comboDialog({
		disabled: true,
		type: 'sys',
		name: 'machine',
		width: 433,
		dataIndex: 'id',
		getParam: function () {
			return {personId: personId};
		},
		checkbox: true,
		title: "选择机器",
		onShow: function() {
			if (personId=="") {
				Public.tip("请选择人员。");
				return false;
			}
			return true;
		},
		onChoose: function() {
			var rows = this.getSelectedRows();
			if (rows.length == 0) {
				Public.tip("请选择机器。");
				return;
			}
			doInsertPersonLoginLimit(rows);
			return true;
		}
	});
}

function doInsertPersonLoginLimit(data) {
	Public.ajax(web_app.name+ '/personLoginLimit/insertPersonLoginLimit.ajax', {
		personId: personId,fullId: fullId,data: $.toJSON(data)},
		function(){
			refreshFlag = true;
			onDialogCloseHandler();
		});
}

function updateSequence() {
	DataUtil.updateSequence({
				action: '/personLoginLimit/updatePersonLoginLimitsSequence.ajax',
				idFieldName: "id",
				gridManager: gridManager,
				onSuccess: reloadGrid
			});
}

function deleteHandler() {
	DataUtil.del({
		action: '/personLoginLimit/deletePersonLoginLimits.ajax',
		idFieldName: 'id',
		gridManager: gridManager,
		onSuccess: reloadGrid
	});
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