var treeManager, roleGridManager, functionGridManager, 
roleId, functionId, inOrgFullId, filterByRoleId= false, orgFullId;

$(function () {
    getQueryParameters();
    loadOrgTreeView();
    initializeUI();

    initializeRoleGrid();
    initializeFunctionGrid();
    createAdditionalCtrls();

    function getQueryParameters() {
    	functionId = Public.getQueryStringByName("funId") || 0;
    	inOrgFullId = Public.getQueryStringByName("inOrgFullId") || "";
    }

    function initializeUI() {
        UICtrl.initDefaultLayout();
    }
    
    function createAdditionalCtrls(){
    	var el = $("#roleGrid").find('div.l-panel-header');
        OpmUtil.createCheckBoxToGridTile(el, function(checked){
        	filterByRoleId = checked;
        	reloadFunctionGrid();
        }, "根据角色查询分配功能");
        
        UICtrl.createGridQueryBtn('#functionGrid', function (filter) {
        	reloadFunctionGrid(filter);
        });
    }
    
    function loadOrgTreeView() {
        if (treeManager)
            treeManager.clear();

        Public.ajax(web_app.name + "/org/queryOrgs.ajax", {
        	parentId: "orgRoot",
        	id: inOrgFullId,
        	customDefineRoot: (!inOrgFullId) ? 0 : 1,
        	showDisabledOrg: 0,
        	displayableOrgKinds: "ogn,dpt,pos,psm",
        	showVirtualOrg: 1,
        	showPosition: 1
        }, function (data) {
            treeManager = UICtrl.tree("#maintree", {
                data: data.Rows,
                idFieldName: "id",
                parentIDFieldName: "parentId",
                textFieldName: "name",
                checkbox: false,
                iconFieldName: "icon",
                btnClickToToggleOnly: true,
                nodeWidth: 180,
                isLeaf: function (data) {
                    data.children = [];
                    data.icon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
                    return data.hasChildren == 0;
                },
                onBeforeExpand: onBeforeExpand,
                onClick: function (node) {
                	if (!node || !node.data) {
                         return;
                     }
                	if (orgFullId != node.data.fullId) {
                    	orgFullId = node.data.fullId;
                    	reloadGrids();    
                    }
                } // end of onClick
            }); // end of UICtrl.tree
        }); // end of function(data)
    }

    function onBeforeExpand(node) {
        if (node.data.hasChildren) {
            if (!node.data.children || node.data.children.length == 0) {
                Public.ajax(web_app + "/org/queryOrgs.ajax", {
                   parentId: node.data.id,
                   showDisabledOrg: 0,
                   displayableOrgKinds: "ogn,dpt,pos,psm",
                   showVirtualOrg: 1,
                   showPosition: 1
                }, function (data) {
                    treeManager.append(node.target, data.Rows);
                });
            }
        }
    }
    
    function initializeRoleGrid() {
        roleGridManager = UICtrl.grid("#roleGrid", {
            columns: [{ display: '组织', name: 'fullName', width: "60%", minWidth: 60, type: "string", align: "left"},
                      { display: '角色', name: 'roleName', width: "30%", minWidth: 60, type: "string", align: "left"}],
            dataAction: 'server',
            url: web_app.name
					+ "/access/queryRolesByOrgFullId.ajax",
            usePager: false,
            title: "分配角色列表",
            width: '54%',
            height: '99%',
            heightDiff: -13,
            rownumbers: true, 
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            enabledSort: false,
            delayLoad: true,
            onSelectRow: function (data, rowindex, rowobj) {
            	roleId = data.id;
            	if (filterByRoleId){
            		triggerReloadFunctionGrid();
            	}
            }
        });
    }

    function initializeFunctionGrid() {
        functionGridManager = UICtrl.grid("#functionGrid", {
            columns: [{ display: '功能全路径', name: 'fullName', width: "90%", minWidth: 60, type: "string", align: "left"}],
            dataAction: 'server',
            url: web_app.name
					+ "/access/queryFunctionsByOrgFullId.ajax",
            usePager: false,
            title: "分配功能列表",
            width: '44%',
            height: '99%',
            heightDiff: -13,
            enabledSort: false,
            rownumbers: true,   
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            delayLoad: true
        });
    }
});

function triggerReloadFunctionGrid(){
	 var button=$("#functionGrid").find('span.ui-grid-query-button');
	 button.trigger("click");
}

function reloadGrids() {
    var param = {};
    param.orgFullId = orgFullId;
    UICtrl.gridSearch(roleGridManager, param);
    triggerReloadFunctionGrid();
}

function reloadFunctionGrid(filter) {
    var param = {};
    param.roleId = 0;
    if (filterByRoleId){
        param.roleId = roleId;
    }
    param.functionId = functionId;
    param.orgFullId = orgFullId;
    param.inOrgFullId = inOrgFullId;
    param.filter = filter;
    UICtrl.gridSearch(functionGridManager, param);
}
