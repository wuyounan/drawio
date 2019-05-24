var treeManager, gridManager, operateCfg = {}, orgId = "";

$(function () {
    initializeOperateCfg();
    initializateUI();

    loadOrgTreeView();
    loadOrgFunctionAuthoize();

    function initializeOperateCfg() {
        var path = web_app.name + '/org/';
        operateCfg.queryOrgAction = path + 'queryOrgs.ajax';

        operateCfg.queryOrgFunctionAuthorizesAction = path + 'slicedQueryOrgFunctionAuthorizes.ajax';
        operateCfg.saveOrgFunctionAuthorizeAction = path + 'saveOrgFunctionAuthorize.ajax';
        operateCfg.deleteOrgFunctionAuthorizeAction = path + 'deleteOrgFunctionAuthorize.ajax';
    }

    function loadOrgFunctionAuthoize() {
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(
	        { addHandler: function () {
	        	showSelectOrgFunctionDialog();
	        },
	            deleteHandler: function () {
	                DataUtil.delSelectedRows({ action: operateCfg.deleteOrgFunctionAuthorizeAction,
	                    idFieldName: "id",
	                    gridManager: gridManager,
	                    onSuccess: function () {
	                        reloadGrid();
	                    }
	                });
	            }
	        });

        gridManager = $("#maingrid").ligerGrid({
            columns: [
	            { display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
	            { display: "名称", name: "fullName", width: 300, minWidth: 60, type: "string", align: "left" }
	            ],
            dataAction: "server",
            url: operateCfg.queryOrgFunctionAuthorizesAction,
            toolbar: toolbarOptions,
            width: "100%",
            height: "100%",
            pageSize: 20,
            sortName: 'id',
             sortOrder: 'asc',
            heightDiff: -12,
            enabledEdit: true,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            rownumbers: true,
            usePager: true,
            onLoadData: function () {
                return (orgId) ;
            }
        });
    }

    function initializateUI() {
        UICtrl.layout("#layout", { leftWidth: 250,
            heightDiff: -5
        });
    }
});

function loadOrgTreeView() {
    $('#orgTree').commonTree({
        loadTreesAction: operateCfg.queryOrgAction,
        parentId: 'orgRoot',
        isLeaf: function (data) {
        	data.children = [];
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
            return parseInt(data.hasChildren) == 0;
        },
        getParam: function (record) {
            if (record) {
                return { showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt" };
            }
            return { showDisabledOrg: 0 };
        },
        onClick: function (data) {
            if (data && orgId != data.id) {
                $('#layout > div.l-layout-center > div.l-layout-header').html("<font style=\"color:Tomato;font-size:13px;\">[" + data.name + "]</font>组织职授权");
                orgId = data.id;
                reloadGrid();
            }
        },
        IsShowMenu: false
    });
}

function showSelectOrgFunctionDialog() {
	if (!orgId){
		Public.errorTip("请选择组织节点。");
		return;
	}
    var params = { confirmHandler: onSelectOrgFunctionHandler, closeHandler: function () { } };
    OpmUtil.showOrgFunctionDialog (params);
}

function onSelectOrgFunctionHandler() {
    _self = this;
    var data = this.iframe.contentWindow.getOrgFunctionData();
    //组装数据
    var orgFunctionIds = [];
    $.each(data, function (i, o) {
    	if (o.nodeKindId == CommonNodeKind.Leaf ){
    		orgFunctionIds.push(o.id);
    	}
    });
    if (orgFunctionIds.length == 0){
    	Public.errorTip("请选择组织职能。");
    	return;
    }
    //保存数据
    var params = {};
    params.orgId = orgId;
    params.orgFunctionIds = $.toJSON(orgFunctionIds);
    Public.ajax(operateCfg.saveOrgFunctionAuthorizeAction, params, function () {
    	_self.close();
        reloadGrid();
    });
}

function reloadGrid() {
	UICtrl.gridSearch(gridManager, { orgId: orgId });
}
