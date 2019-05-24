var treeManager, gridManager, refreshFlag,
 operateCfg = {},
 managedOrgId = "", //被管理的组织（下属组织）
 hasPermission = false; //是否有权限
$(function () {
    initializateUI();
    initializeOperateCfg();
    loadOrgTreeView();
    loadPersonOnPosition();

    function initializeOperateCfg() {
        var path = web_app.name + '/projectOrgPersonOnPosition/';
        operateCfg.queryOrgAction = '/org/queryOrgs.ajax';
        operateCfg.queryAction = path + 'queryOnPosition.ajax';
        operateCfg.saveOnPosition = path + 'saveOnPosition.ajax';
    }

    function loadPersonOnPosition() {
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
                     { display: "业务岗位", name: "name", width: "30%", minWidth: 60, type: "string", align: "left" },
                     { display: "办理人", name: "handlerNames", width: "50%", minWidth: 60, type: "string", align: "left" },
                     { display: "操作", width: "10%", minWidth: 50, type: "string", align: "left",
                         render: function (item) {
                             if (item.nodeKindId == "BaseFunType") {
                                 return "<center><a href='javascript:void(0);' onclick='allocate(" + item.id + ")'>分配</a></center>";
                             }
                         }
                     }
                     ],
            dataAction: "server",
            url: operateCfg.queryAction,
            sortName: 'sequence',
            sortOrder: 'asc',
            usePager: false,
            width: "100%",
            height: "100%",
            heightDiff: -10,
            checkbox: false,
            fixedCellHeight: true,
            enabledSort: false,
            selectRowButtonOnly: true,
            onLoadData: function () {
                return managedOrgId;
            },
            tree: {
                columnName: 'name',
                idField: 'id',
                parentIDField: 'parentId'
            }
        });
    }

    function initializateUI() {
        UICtrl.layout("#layout", { leftWidth: 250, heightDiff: -5 });
    }
});

/**
* 检查分配条件
*/
function checkAllocationCondition() {
    if (!managedOrgId) {
        Public.errorTip('请选择要分配的组织节点。');
        return false;
    }
    if (hasPermission === false) {
        Public.errorTip('您没有操作该节点权限。');
        return false;
    }
    return true;
}

function allocate(baseFunctionTypeId) {
    if (!checkAllocationCondition()) {
        return;
    }

    var selected = gridManager.getSelected();
    if (!selected) {
        Public.errorTip('请选择数据。');
        return;
    }
    
    //本地循环得到当前选择的管理人员
    var data = gridManager.getData();
    var handlers = [];
    for (var i = 0; i < data.length; i++) {
        if (data[i].id == baseFunctionTypeId) {
            handlers = data[i].handlers;
            break;
        }
    }

    var selectOrgparams = OpmUtil.getSelectOrgDefaultParams();
    selectOrgparams = jQuery.extend(selectOrgparams, { selectableOrgKinds: "psm" });
    var options = {
        params: selectOrgparams, confirmHandler: function () {
            doSaveOnPosition.call(this, baseFunctionTypeId);
        },
        closeHandler: onDialogCloseHandler, title: "选择办理人",
        initHandler: function () {
        	var _self=this;
            $.each(handlers, function (i, o) {
            	_self.addDataOneNode.call(_self, o);
            });
        }
    };
    OpmUtil.showSelectOrgDialog(options);
}

function doSaveOnPosition(baseFunctionTypeId) {
	var data = this.getSelectedData();
    var _self = this;

    var personIds = [];
    for (var i = 0; i < data.length; i++) {
        personIds[personIds.length] = OpmUtil.getPersonIdFromPsmId(data[i].id);
    }

    var params = {};
    params.orgId = managedOrgId;
    params.baseFunctionTypeId = baseFunctionTypeId;
    params.personIds = $.toJSON(personIds);

    Public.ajax(operateCfg.saveOnPosition, params, function (data) {
        var selected = gridManager.getSelected();
        gridManager.updateRow(selected, { handlerNames: data.handlerNames, handlers: data.handlers });
        gridManager.isDataChanged = false;
        refreshFlag = false;
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
                return { showDisabledOrg: 0, displayableOrgKinds: "ogn,fld,prj" };
            }
            return { showDisabledOrg: 0 };
        },
        manageType: 'onPosition',
        isLeaf: function (data) {
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
        },
        onClick: function (data) {
            if (data && managedOrgId != data.id) {
                $('#layout > div.l-layout-center > div.l-layout-header').html("<font style=\"color:Tomato;font-size:13px;\">[" + data.name + "]</font>人员落位");
                managedOrgId = data.id;
                hasPermission = data.managerPermissionFlag;
                reloadGrid();
            }
        },
        IsShowMenu: false
    });
}

function reloadGrid() {
    gridManager.options.parms.newPage = 1;
    if (hasPermission) {
        gridManager.options.parms.orgId = managedOrgId;
    } else {
    	//gridManager.setData({ status: 0, data: {Rows:[]}});
       gridManager.options.parms.orgId = "@";
    }
    UICtrl.gridSearch(gridManager);    
}