var gridManager = null, refreshFlag = false, orgId = 0, managedOrgId, roleKindId, orgFullId;
$(document).ready(function () {
    roleKindId = $("#roleKindId").val();
    initializeUI();
    initializeTree();
    initializeGrid();
});

function initializeUI() {
    UICtrl.initDefaultLayout();
}

function initializeTree() {
    $('#maintree').commonTree({
        loadTreesAction: 'tmAuthorize/queryDelegationOrgs.ajax',
        parentId: 'orgRoot',
        getParam: function (e) {
            if (e) {
                return {showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt,pos"};
            }
            return {showDisabledOrg: 0};
        },
        changeNodeIcon: function (data) {
            data[this.options.iconFieldName] = OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
        },
        IsShowMenu: false,
        onClick: function (data) {
            //这里应该为超级管理员或者三员的根节点 superAdministrator common
            if (Public.isBlank(this.treeManager.getParent(data, 1)) || roleKindId == "superAdministrator") {
                orgId = data.id;
            } else {
                orgId = this.treeManager.getParent(data, 1).id;
            }
            orgFullId = data.fullId;
            managedOrgId = data.id;
            onFolderOrgTreeNodeClick(data.fullName, orgId, data.fullId);
        }
    });
}

function onFolderOrgTreeNodeClick(fullName, orgId, fullId) {
    if (fullName) {
    	fullName = "[" + fullName + "]";
    }
    $("#layout").layout("setCenterTitle", "<span class='tomato-color'>" + fullName + "</span>权限列表");
    var params = $("#queryMainForm").formToJSON();
    //最上级的节点
    params.subordinationId = orgId;
    //三员角色，实际点击的节点
    params.managedOrgId = managedOrgId;
    UICtrl.gridSearch(gridManager, params);
}

function initializeGrid() {
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {display: "系统", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "人员", name: "managerNames", width: 300, minWidth: 60, type: "string", align: "left"},
            {
                display: "操作", name: "oper", width: 80, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    if (item.id == "administrator" || item.id == "securityGuard" || item.id == "auditor") {
                        return "<center><a href='javascript:void(0);' onclick='allocate(\"" + item.id + "\",\"" + item.parentId + "\")'>分配</a></center>";
                    }
                }
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/tmAuthorize/queryTMAuthorizes.ajax',
        toolbar: null,
        width: '99.8%',
        height: '100%',
        heightDiff: -11,
        checkbox: false,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        tree: {
            columnName: 'name',
            idField: 'id',
            parentIDField: 'parentId'
        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

/**
 * 检查分配条件
 */
function checkAllocationCondition() {
    if (!managedOrgId) {
        Public.errorTip('请选择要分配的组织节点。');
        return false;
    }

    return true;
}

function allocate(baseFunctionTypeId, parentId) {
    if (!checkAllocationCondition()) {
        return;
    }

    //本地循环得到当前选择的管理人员
    var data = gridManager.getData(), managers = [];
    for (var i = 0; i < data.length; i++) {
        if (data[i].id == baseFunctionTypeId && parentId == data[i].parentId) {
            managers = data[i].managers;
            break;
        }
    }

    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();

    selectOrgParams = jQuery.extend(selectOrgParams, {selectableOrgKinds: "psm"});
    var options = {
        params: selectOrgParams, confirmHandler: function () {
            doSaveOnPosition.call(this);
        },
        closeHandler: onDialogCloseHandler, title: "选择办理人",
        initHandler: function () {
        	var _self=this;
            $.each(managers, function (i, o) {
            	_self.addDataOneNode.call(_self, o);
            });
        }
    };
    OpmUtil.showSelectOrgDialog(options);
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function doSaveOnPosition() {
    var data = this.getSelectedData();
    var _self = this;
    var selected = gridManager.getSelected();
    var params = {}, detailData = [];

    for (var i = 0; i < data.length; i++) {
        var baseObj = {};

        baseObj.subordinationId = managedOrgId;
        baseObj.subordinationFullId = orgFullId;
        baseObj.systemId = selected.parentId;
        baseObj.roleKindId = selected.id;
        baseObj.managerId = data[i].id;//OpmUtil.getPersonIdFromPsmId(data[i].id);

        detailData.push(baseObj);
    }

    params.detailData = $.toJSON(detailData);
    params.subordinationId = managedOrgId;
    params.systemId = selected.parentId;
    params.roleKindId = selected.id;

    Public.ajax(web_app.name + '/tmAuthorize/saveTMAuthorizes.ajax', params, function (data) {
        var selected = gridManager.getSelected();
        //gridManager.updateRow(selected, {handlerNames: data.handlerNames, handlers: data.handlers});
        gridManager.isDataChanged = false;
        refreshFlag = true;
        _self.close();
    });
}

function reloadGrid() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}