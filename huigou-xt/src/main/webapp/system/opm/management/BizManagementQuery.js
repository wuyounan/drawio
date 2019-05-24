var treeManager, gridManager, bizManagementTypeGridManager,  typeId,
 operateCfg = {}, manageTypeId = 0, orgId = "",  orgfullId,
 loadedBizManagementType = false,  loadedBizManagement = false;

$(function() {
	
	getQueryParameters();
	initializateUI();
	bindEvents();
	
	initializeOperateCfg();
	loadOrgTreeView();
	loadBizManagementType();
	loadBizManagement();
	
	function getQueryParameters() {
		typeId = Public.getQueryStringByName("typeId");
	}
	
	function bindEvents() {
		$("#showDisabledOrg,#showMasterPsm, #showVirtualOrg").click(function () {
			loadOrgTreeView();
        });
	}
	
	function initializeOperateCfg(){
		var  path = web_app.name + '/management/';
		operateCfg.queryOrgAction = '/org/queryOrgs.ajax';
		if (typeId == "manager"){
			operateCfg.queryBizManagementType = path +"slicedQueryOrgAllocatedBizManagementTypeForManager.ajax";
		    operateCfg.queryAction = path +'slicedQueryBizManagementForManager.ajax';	
		}
		else {
			operateCfg.queryBizManagementType = path + "slicedQueryOrgAllocatedBizManagementTypeForSubordination.ajax";
			operateCfg.queryAction = path +'slicedQueryBizManagementForSubordination.ajax';	
		}
	}

	/**
	 * 加载业务权限类别
	 */
	function loadBizManagementType() {
		bizManagementTypeGridManager = UICtrl.grid("#bizManagementType", {
			columns: [
					{ display: "编码", name: "code", width: "120", minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: "160", minWidth: 60, type: "string", align: "left" }
					],
			dataAction: "server",
			url: operateCfg.queryBizManagementType,
			title: '查询',
			pageSize: 20,
			width: '100%', 
			height: '100%', 
			sortName:'code',
			sortOrder:'asc',
			heightDiff: -8,
			checkbox: false,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			rownumbers: true,
			usePager: true,
			onDblClickRow: function (data, rowindex, rowobj) {
				doSearchBizManagement(data.id, data.name);
            },
            onSelectRow: function (data, rowindex, rowobj) {
            	doSearchBizManagement(data.id,data.name);
            },
            onLoadData: function () {
            	var loaded = loadedBizManagementType;
            	loadedBizManagementType = true;
            	return loaded;
            }
		});
		UICtrl.createGridQueryBtn('#bizManagementType', function(param){
			UICtrl.gridSearch(bizManagementTypeGridManager, { param: encodeURI(param) });
		});
	}

	function loadBizManagement() {
		gridManager = $("#maingrid").ligerGrid({
            columns: [
                     { display: "组织名称全路径", name: "fullName", width: "300", minWidth: 60, type: "string", align: "left" },
                     { display: "组织状态", name: "status", width: "100", minWidth: 60, type: "string", align: "left",
					   render: function(item) { return OpmUtil.getOrgStatusDisplay(item.status) } }
                     ],
                    dataAction: "server",
         			url: operateCfg.queryAction,
         			pageSize: 20,
         			usePager: true,
         			width: "100%",
         			height: "100%",
         			heightDiff: -8,
         			checkbox: true,
         			fixedCellHeight: true,
         			selectRowButtonOnly: true,
         			onLoadData: function(){
         				var loaded = loadedBizManagement;
         				loadedBizManagement = true;
         				return loaded;
         			}
        });
    }
	
	function initializateUI() {
		UICtrl.layout("#layout", {leftWidth: 3,rightWidth:6,onSizeChanged:function(){
			if (window['bizManagementTypeGridManager']) {
				var g=window['bizManagementTypeGridManager'];
				g._onResize.call(g);
		    }
		}});  
	}

	function doSearchBizManagement(id, name){
		if (manageTypeId == id) return;
	    manageTypeId = id;
	    $("#layout").layout("setRightTitle", "<span class='tomato-color' >[" + name + "]</span>权限列表");
	    searchBizManagement();
	}

	function searchBizManagement() {
		if (!orgId || !manageTypeId){
			return;
		}
	    gridManager.options.parms.selectedFullId = orgfullId;
	    gridManager.options.parms.manageTypeId = manageTypeId;
	    gridManager.options.newPage = 1;
	    reloadGrid();
	}
});

function loadBizManagementType() {
	if (!orgfullId){
		return;
	}
	bizManagementTypeGridManager.options.parms.typeId = typeId;
	bizManagementTypeGridManager.options.parms.selectedFullId = orgfullId;
	bizManagementTypeGridManager.options.newPage = 1;
    UICtrl.gridSearch(bizManagementTypeGridManager);
    manageTypeId = 0;
    //reloadGrid();
}

function loadOrgTreeView() {
	$("#orgTree").remove();
	$("#orgTreeWrapper").append("<ul id='orgTree'></ul>");
	
	$('#orgTree').commonTree({	
		loadTreesAction: operateCfg.queryOrgAction,
		parentId:'orgRoot',
		getParam: function (e) {
			return getOrgFilterCondition();
        },
		isLeaf: function(data) {
			data.nodeIcon = OpmUtil.getOrgImgUrl (data.orgKindId, data.status, false);
		},
		onClick: function(data) {
			if (data && orgId != data.id) {
			    $("#layout").layout("setCenterTitle", "<span class='tomato-color' >[" + data.name + "]</span>权限管理");
				orgId = data.id;
				orgfullId = data.fullId;
				loadBizManagementType();
			}
		},
		IsShowMenu: false
	});
}

function reloadGrid() {
	UICtrl.gridSearch(gridManager);
}