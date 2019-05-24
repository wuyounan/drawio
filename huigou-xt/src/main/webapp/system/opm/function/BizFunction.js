var treeManager, gridManager, bizFunctionGridManager,
 operateCfg = {}, orgId = "", orgKindId = "", bizFunctionId = -1;

$(function () {
    initializeOperateCfg();
    initializateUI();

    loadOrgTreeView();
    loadBizFunction();
    loadBizFunctionOwnBase();
    initializateSelectBaseFunctionType();

    function initializeOperateCfg() {
        var path = web_app.name + '/fun/';
        operateCfg.queryOrgAction = '/org/queryOrgs.ajax';

        operateCfg.queryBizFunctionAction = path + 'slicedQueryBizFunctions.ajax';
        operateCfg.saveBizFunctionAction = path + 'saveBizFunction.ajax';
        operateCfg.deleteBizFunctionAction = 'fun/deleteBizFunction.ajax';

        operateCfg.queryBizFunctionOwnBaseAction = path + 'slicedQueryBizFunctionOwnBases.ajax';
        operateCfg.saveBizFunctionOwnBaseAction = path + 'saveBizFunctionOwnBase.ajax';
        operateCfg.deleteBizFunctionOwnBaseAction = 'fun/deleteBizFunctionOwnBase.ajax';
    }

    function initializateSelectBaseFunctionType() {
        $('#maingrid #toolbar_menuAdd').comboDialog({ type: 'opm', name: 'selectBaseFunctonType', width: 480,
            dataIndex: 'id',
            title: "基础职能角色",
            checkbox: true,
            onShow: function () {
                if (!beforeAddBizFunctionOwnBaseCheck()) {
                    return false;
                }
                return true;
            },
            onChoose: function () {
                var baseFunctionTypeData = this.getSelectedRows();

                if (baseFunctionTypeData == null) {
                    Public.errorTip("基础职能角色。");
                    return;
                }
                //组装基础职能角色
                var baseFunctionTypeIds = [];
                $.each(baseFunctionTypeData, function (i, o) {
                    baseFunctionTypeIds.push(o.id);
                });
                //保存数据
                var params = {};
                params.bizFunctionId = bizFunctionId;
                params.baseFunctionTypeIds = $.toJSON(baseFunctionTypeIds);
                var url = web_app.name + "/fun/saveBizFunctionOwnBase.ajax";
                Public.ajax(url, params, function () {
                    gridManager.loadData();
                });

                return true;
            }
        });
    }

    /**
    * 加载业务段类别
    */
    function loadBizFunction() {
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(
        { addHandler: function () {
            if (!beforeAddBizFunctionCheck()) {
                return;
            }
            UICtrl.addGridRow(bizFunctionGridManager,
                { orgId: orgId, sequence: bizFunctionGridManager.getData().length + 1 });
        },
            saveHandler: saveBizFunction,
            deleteHandler: function () {
                DataUtil.delSelectedRows({ action: operateCfg.deleteBizFunctionAction,
                    idFieldName: "id",
                    gridManager: bizFunctionGridManager,
                    onSuccess: function () {
                        bizFunctionGridManager.loadData();
                        gridManager.loadData();
                    }
                });
            }
        });
        bizFunctionGridManager = UICtrl.grid("#bizFunctionGrid", {
            columns: [
					{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left", editor: { type: 'text', required: true} },
					{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left", editor: { type: 'text', required: true} },
					{ display: "排序号", name: "sequence", width: "60", minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return "<input type='text' id='txtSequence_"
									+ item.id + "' class='textbox' value='"
									+ item.sequence + "' />";
					    }
					}
					],
            dataAction: "server",
            url: operateCfg.queryBizFunctionAction,
            toolbar: toolbarOptions,
            width: '100%',
            height: '50%',
            pageSize: 20,
            sortName: 'sequence',
            sortOrder: 'asc',
            //checkbox: true,
            heightDiff: -12,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            rownumbers: true,
            usePager: true,
            delayLoad: true,
            enabledEdit: true,
            onSelectRow: function (data, rowindex, rowobj) {
                if (bizFunctionId != data.id) {
                    bizFunctionId = data.id;
                    if (!bizFunctionId) {
                        bizFunctionId = 0;
                    }
                    searchBizFunctionOwnBase();
                }
            }
        });
    }

    function loadBizFunctionOwnBase() {
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(
	        { addHandler: function () {
	        },
	            deleteHandler: function () {
	                DataUtil.delSelectedRows({ action: operateCfg.deleteBizFunctionOwnBaseAction,
	                    idFieldName: "id",
	                    gridManager: gridManager,
	                    onSuccess: function () {
	                        gridManager.loadData();
	                    }
	                });
	            }
	        });

        gridManager = $("#maingrid").ligerGrid({
            columns: [
	            { display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
	            { display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" }
	            ],
            dataAction: "server",
            url: operateCfg.queryBizFunctionOwnBaseAction,
            toolbar: toolbarOptions,
            width: "100%",
            height: "50%",
            pageSize: 20,
            sortName: 'sequence',
            heightDiff: -12,
            enabledEdit: true,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            rownumbers: true,
            usePager: true,
            onLoadData: function () {
                return bizFunctionId >= 0;
            }
        });
    }

    function initializateUI() {
        UICtrl.layout("#layout", { leftWidth: 250,
            heightDiff: -5
        });
    }
});

function beforeAddBizFunctionCheck() {
    if (!orgId) {
        Public.errorTip("请选择组织节点。");
        return false;
    }
    if (orgKindId != OrgKind.Project) {
        Public.errorTip("请选择项目组织。");
        return false;
    }
    return true;
}

function beforeAddBizFunctionOwnBaseCheck() {
    if (!beforeAddBizFunctionCheck()) {

        return false;
    }
    if (!bizFunctionId) {
        Public.errorTip("请选业务职能角色。");
        return false;
    }
    return true;
}

function saveBizFunction() {
    var bizFunctiondata = DataUtil.getGridData({ gridManager: bizFunctionGridManager });
    if (!bizFunctiondata) {
        return false;
    }

    Public.ajax(operateCfg.saveBizFunctionAction,
        { data: Public.encodeJSONURI(bizFunctiondata) }, function () {
            bizFunctionGridManager.loadData();
        }
    );
}

function searchBizFunction(orgName) {
    $('#layout2').find('div.l-layout-center').find('div.l-layout-header').html("<font style=\"color:Tomato;font-size:13px;\">[" + orgName + "]</font>业务职能角色");
    bizFunctionGridManager.options.parms.orgId = orgId;
    bizFunctionGridManager.loadData();
}

function searchBizFunctionOwnBase() {
    gridManager.options.parms.bizFunctionId = bizFunctionId;
    gridManager.loadData();
}

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
                return { showDisabledOrg: 0, displayableOrgKinds: "ogn,fld,prj" };
            }
            return { showDisabledOrg: 0 };
        },
        onClick: function (data) {
            if (data && orgId != data.id) {
                $('#layout > div.l-layout-center > div.l-layout-header').html("<font style=\"color:Tomato;font-size:13px;\">[" + data.name + "]</font>业务职能角色");
                orgId = data.id;
                orgKindId = data.orgKindId;
                bizFunctionId = 0;
                searchBizFunction();
                searchBizFunctionOwnBase();
            }
        },
        IsShowMenu: false
    });
}