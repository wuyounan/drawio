var treeManager, functionGridManager, orgGridManager, 
roleId, functionId, orgFullId, filterByRoleId= false;

$(function () {
    getQueryParameters();
    loadRoleTreeView();
    initializeUI();

    initializeOrgGrid();
    initializeFunctionGrid();
    
    function getQueryParameters() {
    	functionId = Public.getQueryStringByName("funcId") || 0;
        orgFullId = Public.getQueryStringByName("orgFullId") || "";
    }

    function initializeUI() {
        UICtrl.initDefaultLayout();
    }

    function loadRoleTreeView() {
    	$('#maintree').commonTree({
    		loadTreesAction:  web_app.name + '/access/queryRoles.ajax',
    		parentId:  0,
            onClick: function(data){
            	if (roleId != data.id){
            		roleId = data.id;
            		reloadGrids();
            	}            	
            },
            IsShowMenu: false
        });
    }
    
   function initializeOrgGrid() {
        orgGridManager = UICtrl.grid("#orgGrid", {
            columns: [{ display: '组织全路径', name: 'fullName', width: "90%", minWidth: 60, type: "string", align: "left"}],
            dataAction: 'server',
            url: web_app.name
					+ "/access/queryOrgsByRoleId.ajax",
            usePager: false,
            title: "分配组织列表",
            width: '49%',
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
            		reloadOrgGrid();
            	}
            }
        });
    }

    function initializeFunctionGrid() {
        functionGridManager = UICtrl.grid("#functionGrid", {
            columns: [{ display: '功能全路径', name: 'fullName', width: "90%", minWidth: 60, type: "string", align: "left"}],
            dataAction: 'server',
            url: web_app.name
					+ "/access/queryFunctionsByRoleId.ajax",
            usePager: false,
            title: "分配功能列表",
            width: '49%',
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

function reloadGrids() {
    var param = {};
    param.roleId = roleId;
    UICtrl.gridSearch(orgGridManager, param);
    UICtrl.gridSearch(functionGridManager, param);    
}