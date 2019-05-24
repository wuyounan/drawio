var treeManager, roleGridManager, orgGridManager, 
roleId, functionId, inOrgFullId, filterByRoleId= false;

$(function () {
    getQueryParameters();
    loadFunctionTree();
    initializeUI();

    initializeRoleGrid();
    initializeOrgGrid();
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
        	reloadOrgGrid();
        }, "根据角色查询分配组织");   	
        
        UICtrl.createGridQueryBtn('#orgGrid', function (filter) {
        	reloadOrgGrid(filter);
        });
    }
    
    function loadFunctionTree() {
        if (treeManager)
            treeManager.clear();

        Public.ajax(web_app.name + "/sysFunction/queryFunctions.ajax", {
        	parentId: functionId,
        	id: functionId,
        	customDefineRoot: (functionId == 0) ? 0 : 1
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
                	data.children = [];
    				if (!data.parentId){
    					data.icon = web_app.name + "/themes/default/images/icons/function.gif";
    				}else {
    					data.icon= DataUtil.changeFunctionIcon(data.icon);
    				}
    				return data.hasChildren == 0;
                },
                onBeforeExpand: onBeforeExpand,
                onClick: function (node) {
                  if (!node || !node.data) {
                         return;
                     }
                	 if (data && functionId != data.id) {
                     	functionId = node.data.id;
                        reloadGrids();
                     }
                }
            }); // end of UICtrl.tree
        }); // end of function(data)
    }
   
    function onBeforeExpand(node) {
        if (node.data.hasChildren) {
            if (!node.data.children || node.data.children.length == 0) {
                Public.ajax(web_app + "/sysFunction/queryFunctions.ajax", {
                   parentId: node.data.id                  
                }, function (data) {
                    treeManager.append(node.target, data.Rows);
                });
            }
        }
    }
    
    function initializeRoleGrid() {
        roleGridManager = UICtrl.grid("#roleGrid", {
            columns: [{ display: '名称', name: 'roleName', width: "90%", minWidth: 60, type: "string", align: "left"}],
            dataAction: 'server',
            url: web_app.name
					+ "/access/queryRolesByFunctionId.ajax",
            usePager: false,
            title: "分配角色列表",
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
            		triggerReloadOrgGrid();
            	}
            }
        });
    }

    function initializeOrgGrid() {
        orgGridManager = UICtrl.grid("#orgGrid", {
            columns: [{ display: '组织全路径', name: 'fullName', width: "90%", minWidth: 60, type: "string", align: "left"}],
            dataAction: 'server',
            url: web_app.name
					+ "/access/queryOrgsByFunctionId.ajax",
            usePager: false,
            title: "分配组织列表",
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


function triggerReloadOrgGrid(){
	 var button=$("#orgGrid").find('span.ui-grid-query-button');
	 button.trigger("click");
}

function reloadGrids() {
    var param = {};
    param.functionId = functionId;
    UICtrl.gridSearch(roleGridManager, param);
    triggerReloadOrgGrid();
}

function reloadOrgGrid(filter) {
    var param = {};
    param.roleId = 0;
    if (filterByRoleId){
        param.roleId = roleId;
    }
    param.functionId = functionId;
    param.inOrgFullId = inOrgFullId;
    param.filter = filter; 
    UICtrl.gridSearch(orgGridManager, param);
}
