var treeManager, gridManager, refreshFlag,
 operateCfg = {},
 manageOrgId = "", //被管理的组织（managedOrgId） 
 hasPermission = false;
$(function () {
    initializateUI();
    initializeOperateCfg();
    loadOrgTreeView();
    loadBizManagement();

    function initializeOperateCfg() {
        var path = web_app.name + '/management/';
        operateCfg.queryOrgAction = '/org/queryOrgs.ajax';
        operateCfg.slicedQueryAction = path + 'slicedQueryBizManagementsByManageOrgId.ajax';
        operateCfg.insertByOrgAction = path + 'insertDelegationBizManagement.ajax';
    }

    function loadBizManagement() {
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
                     { display: "业务管理权限", name: "name", width: "30%", minWidth: 60, type: "string", align: "left" },
                     { display: "管理者", name: "managerNames", width: "50%", minWidth: 60, type: "string", align: "left" },
                     { display: "操作", name: "managerNames", width: "10%", minWidth: 50, type: "string", align: "left",
                         render: function (item) {
                             if (item.nodeKindId == CommonNodeKind.Leaf) {
                                 return "<center><a href='javascript:void(0);' onclick='doAuthorize(" + item.id + ")'>授权</a></center>";
                             }
                         }
                     }
                     ],
            dataAction: "server",
            url: operateCfg.slicedQueryAction,
            sortName: 'fullName',
            sortOrder: 'asc',
            usePager: false,
            width: "100%",
            height: "100%",
            heightDiff: -10,
            checkbox: false,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onLoadData: function () {
                return manageOrgId;
            },
            tree: {
                columnName: 'name',
                idField: 'id',
                parentIDField: 'parentId'
            }
        });
    }

    function initializateUI() {
    	UICtrl.initDefaultLayout();
    }
});

/**
* 检查分配条件
*/
function checkAuthorizeCondition() {
    if (!manageOrgId) {
        Public.errorTip('请选择要授权的组织节点。');
        return false;
    }
    if (hasPermission === false) {
        Public.errorTip('您没有操作该节点权限。');
        return false;
    }
    return true;
}

function doAuthorize(manageTypeId) {
    if (!checkAuthorizeCondition()) {
        return;
    }

    //本地循环得到当前选择的管理人员
    var data = gridManager.getData();
    var managers = [];
    for (var i = 0; i < data.length; i++) {
        if (data[i].id == manageTypeId) {
            managers = data[i].managers;
            break;
        }
    }

    var selectOrgparams = OpmUtil.getSelectOrgDefaultParams();
    selectOrgparams = jQuery.extend(selectOrgparams, { manageCodes: 'delegation', selectableOrgKinds: "ogn,dpt,pos,psm" });
    var options = {
        params: selectOrgparams, confirmHandler: function () {
            doSaveAuthorize.call(this, manageTypeId);
        },
        closeHandler: onDialogCloseHandler, title: "选择组织",
        initHandler: function () {
        	var _self=this;
            $.each(managers, function (i, o) {
            	_self.addDataOneNode.call(_self, o);
            });
        }
    };
    OpmUtil.showSelectOrgDialog(options);
}

function doSaveAuthorize(manageTypeId) {
	var data = this.getSelectedData();
    var _self = this;

    var orgIds = [];
    for (var i = 0; i < data.length; i++) {
        orgIds[orgIds.length] = data[i].id;
    }

    var params = {};
    params.kindId = 2;
    params.manageOrgId = manageOrgId;
    params.orgIds = $.toJSON(orgIds);
    params.manageTypeId = manageTypeId;
    Public.ajax(operateCfg.insertByOrgAction, params, function () {
        refreshFlag = true;
        _self.close();
    });
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function loadOrgTreeView() {
    $('#orgTree').commonTree({
        loadTreesAction: operateCfg.queryOrgAction,
        parentId: 'orgRoot',
        getParam: function (e) {
            if (e) {
                return { showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt,pos,fld,prj,grp" };
            }
            return { showDisabledOrg: 0 };
        },
        manageType: 'delegation',
        isLeaf: function (data) {
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
        },
        onClick: function (data) {
            if (data && manageOrgId != data.id) {
                $('#layout > div.l-layout-center > div.l-layout-header').html("<font style=\"color:Tomato;font-size:13px;\">[" + data.name + "]</font>权限管理");
                manageOrgId = data.id;
                hasPermission = data.managerPermissionFlag;
                reloadGrid();
            }
        },
        IsShowMenu: false
    });
}

function reloadGrid() {
    gridManager.options.parms.newPage = 1;
    //orgId 映射为manageOrgId
    if (hasPermission) {
        gridManager.options.parms.orgId = manageOrgId;
    } else {
        gridManager.options.parms.orgId = "@";
    }
    UICtrl.gridSearch(gridManager);
}