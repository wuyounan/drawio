var treeManager, gridManager, bizManagementGridManager, 
   refreshFlag, operateCfg = {}, manageTypeId = 0, 
   subordinationId = "",  //下属组织ID
   managerId = "",  //管理组织ID
   typeId; //授权方式ID

$(function() {	
	getQueryParameters();
	
	bindEvents();
	initializateUI();
	
	initializeOperateCfg();
	loadOrgTreeView();
	loadBizManagementType();
	loadBizManagement();
	
    function getQueryParameters() {
		typeId = $('#typeIdParam').val();
	}
	
	function initializeOperateCfg(){
		var  path =  '/management/';
		operateCfg.queryOrgAction ='/org/queryOrgs.ajax';
		operateCfg.queryAction = path +'slicedQueryBizManagements.ajax';		
		operateCfg.allocateManagers = path + 'allocateManagers.ajax';
		operateCfg.allocateSubordinations = path + 'allocateSubordinations.ajax';
		operateCfg.deleteAction = path +'deleteBizManagement.ajax';	
		operateCfg.romoveCache = path +'removePermissionCache.ajax';	
	}

	function bindEvents() {
		$("#showDisabledOrg,#showMasterPsm, #showVirtualOrg").click(function () {
			loadOrgTreeView();
        });
	}
	
	/**
	 * 加载业务权限类别
	 */
	function loadBizManagementType() {
		var toolbarOptions={items:[]};
		var queryManageCodes=$('#queryManageCodes').val();
		if (Public.isBlank(queryManageCodes)) {
			toolbarOptions = {
		        items: [
		           { id: "queryThis", text: "选择查询", click: function(){
		        	   OpmUtil.showSelectBizManagementTypeDialog({
		        		   confirmHandler:function(){
			        		   _self = this;
			        		    var data = this.iframe.contentWindow.getBizManagementTypeData();
			        		    if (data == null) return;
			        		    UICtrl.gridSearch(bizManagementGridManager, {param:'',fullId:data.fullId});
			        		    _self.close();
			        	   }
		        	   });	
		           }, img: "fa-search" }
		        ]
		    };
		}
		bizManagementGridManager = UICtrl.grid("#bizManagementType", {
			columns: [
					{ display: "编码", name: "code", width: "120", minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: "150", minWidth: 60, type: "string", align: "left" }
					],
			dataAction: "server",
			url: web_app.name + "/management/slicedQueryBizManagementTypesByKeyWord.ajax",
			pageSize: 20,
			parms: { nodeKindId: 2,queryManageCodes:queryManageCodes},
			width: '100%', 
			height: "100%",
			sortName: 'code',
			sortOrder: 'asc',
			heightDiff: -8,
			checkbox: false,
			toolbar: toolbarOptions,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			rownumbers: true,
			usePager: true,
			onDblClickRow: function (data, rowindex, rowobj) {
				doSearchBizManagement(data.id, data.name);
            },
            onSelectRow: function (data, rowindex, rowobj) {
            	doSearchBizManagement(data.id, data.name);
            }
		});
		UICtrl.createGridToolBarQueryBtn('#bizManagementType',function(param){
			UICtrl.gridSearch(bizManagementGridManager, {param:encodeURI(param),fullId:''});
		});
	}

	
	function loadBizManagement() {
        var toolbarOptions = {
            items: [
                { id: "addOrg", text: "授权", click: allocateOrg, img: "fa-plus" },
                { id: "deleteOrg", text: "删除", click: deleteBizManagement, img:  "fa-trash-o" },
		        { id: "romoveCache", text: "清除权限缓存", click: romoveCache, img: "fa-files-o" },
		        { id: 'doQuery',text:'查询业务权限授权',img:'fa-link',click:function(){
		        	var manageType=$('#delegationManageType').val();
		        	if (Public.isBlank(manageType)) {
		        		manageType='';
		        	}
	            	var url=DataUtil.composeURLByParam('management/forwardPermissionBizManagementQuery.do',{manageType:manageType});
	            	UICtrl.addTabItem({tabid:'permissionBizManagementQuery', text:'业务权限授权查询',url:url}); 
	    		}}
              ]
        };

        gridManager =  UICtrl.grid("#maingrid", {
            columns: [
                     { display: "名称", name: "name", width: 80, minWidth: 60, type: "string", align: "left" },
                     { display: "路径", name: "fullName", width: 300, minWidth: 60, type: "string", align: "left" },
                     { display: "组织状态", name: "status", width: 80, minWidth: 60, type: "string", align: "left",
					   render: function (item){
					   	   	return OpmUtil.getOrgStatusDisplay(item.status) ; 
					   	} 
                     },
					 { display: "创建人", name: "createdByName", width: 60, minWidth: 60, type: "string", align: "left" },
	 				 { display: "创建日期", name: "createdDate", width: 140, minWidth: 60, type: "datetime", align: "left" }							  	
                     ],
                    dataAction: "server",
         			url: web_app.name + operateCfg.queryAction,
         			pageSize: 20,
         			usePager: true,
         			toolbar: toolbarOptions,
         			width: "100%",
         			height: "100%",
         			heightDiff: -8,
         			checkbox: true,
         			fixedCellHeight: true,
         			selectRowButtonOnly: true,
         			onLoadData: function(){
         				return  isAllocateManager() ?  subordinationId: managerId && manageTypeId;
         			}
        });
    }
	
	function initializateUI() {
		UICtrl.layout("#layout", {leftWidth: 3,rightWidth:6});  
	}

	function deleteBizManagement() {
		DataUtil.del({ action: operateCfg.deleteAction, gridManager: gridManager, onSuccess: reloadGrid });
	}
});

/**
 * 是否分配管理者
 */
function isAllocateManager(){
	return typeId != "subordination";	
}

function doSearchBizManagement(id, name){
	if (manageTypeId != id) {
	    manageTypeId = id;
	    $("#layout").layout('setRightTitle',"<span class=\"tomato-color\">[" + name + "]</span>权限列表");
	    searchBizManagement();
	}
}
function searchBizManagement() {
	if (!manageTypeId){
		return;	
	}
	if (isAllocateManager() ? !subordinationId: !managerId){
		return;
	}
	
    if (isAllocateManager()){
       gridManager.options.parms.subordinationId = subordinationId;
    }else{
       gridManager.options.parms.managerId = managerId;
    }
    
    //gridManager.options.parms.typeId = typeId;
    
    gridManager.options.parms.manageTypeId = manageTypeId;
    gridManager.options.newPage = 1;
    reloadGrid();
}


/**
 * 检查分配条件
 */
function checkAllocateCondition(){
	 if (isAllocateManager() ? !subordinationId: !managerId) {
	        Public.tip('请选择要授权的机构节点。'); 
	        return false;
	 }

	 if (!manageTypeId) {
	    	Public.tip('请选择业务权限类别。'); 
	    	return false;
	 }
	 
	 return true;
}

function allocateOrg() {
	if (!checkAllocateCondition()){
    	return;
    }
	var selectOrgparams = OpmUtil.getSelectOrgDefaultParams();
	selectOrgparams = jQuery.extend(selectOrgparams, {selectableOrgKinds: "ogn,dpt,pos,psm"});
	var options = { 
			params: selectOrgparams, confirmHandler: doSaveAllocateOrg, 
			closeHandler: onDialogCloseHandler, title: "选择组织"
	};
	OpmUtil.showSelectOrgDialog(options);
}

function doSaveAllocateOrg(){
	var data = this.getSelectedData();
	if (data.length == 0) {
		Public.errorTip("请选择组织。");
		return;
	}
	
	var _self = this;
	
	var bizOrgIds = [];

    for (var i = 0; i < data.length; i++) {
    	bizOrgIds[bizOrgIds.length] = data[i].id;
    }
    
	var params = {};
	params.kindId = 2;

	params.manageTypeId  = manageTypeId;

	if (isAllocateManager()){
		params.subordinationId = subordinationId;
		params.managerIds = $.toJSON(bizOrgIds);
	}else{
		params.managerId = managerId;
		params.subordinationIds = $.toJSON(bizOrgIds);
	}
	
	var url = web_app.name + (isAllocateManager() ? operateCfg.allocateManagers: operateCfg.allocateSubordinations);
	
	Public.ajax(url, params, function() {
		refreshFlag = true;
		_self.close();
	});
}

function onDialogCloseHandler(){
	if (refreshFlag) {
		reloadGrid();
		refreshFlag = false;
	}
}

function loadOrgTreeView() {
	$("#orgTree").remove();
	$("#orgTreeWrapper").append("<ul id='orgTree'></ul>");
	var manageType=$('#delegationManageType').val();
	if (Public.isBlank(manageType)) {
		manageType=null;
	}
	var orgRootId=$('#delegationOrgRootId').val();
	if (Public.isBlank(orgRootId)) {
		orgRootId='orgRoot';
	}
	$('#orgTree').commonTree({	
		loadTreesAction: operateCfg.queryOrgAction,
		parentId: orgRootId,
		getParam: function (e) {
			return getOrgFilterCondition();
        },
		isLeaf: function(data) {
			data.nodeIcon = OpmUtil.getOrgImgUrl (data.orgKindId, data.status, false);
		},
		manageType: manageType,
		onClick: function(data,node) {
			var managerPermissionFlag=node['managerPermissionFlag'];
			if(managerPermissionFlag===false){
				return false;
			}
			var changeOrg = false;
			if (isAllocateManager()){
				if (data && subordinationId != data.id) {
					subordinationId = data.id;
					changeOrg = true;
				}
			}else{
				managerId = data.id;
				changeOrg = true;
			}
			if(changeOrg){
				$("#layout").layout("setCenterTitle", "<span class='tomato-color'>[" + data.name + "]</span>业务权限管理");
				searchBizManagement();
			}
		},
		IsShowMenu: false
	});
}


function reloadGrid() {
	UICtrl.gridSearch(gridManager);
}

function romoveCache(){
	Public.ajax(web_app.name + operateCfg.romoveCache);
}