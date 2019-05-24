var gridManager = null,authorizationGridManager=null,personGridManager=null;
$(document).ready(function() {
	initUI();
	initializeGrid();
	initializAuthorizationGrid();
	//initializPersonGrid();
});

function initUI(){
	$('#tabPage').tab();
	$("#queryAuthorizationFormBtn").click(function() {
		var params = $(this.form).formToJSON();
		UICtrl.gridSearch(authorizationGridManager, params);
	});
	$("#resetAuthorizationFormBtn").click(function() {
		$(this.form).formClean();
	});
	/*$("#queryPersonMainBtn").click(function() {
		var params = $(this.form).formToJSON();
		UICtrl.gridSearch(personGridManager, params);
	});
	$("#resetPersonMainBtn").click(function() {
		$(this.form).formClean();
	});*/
	$('li.tabli').on('click',function(){
		setTimeout(function(){reRenderGrid();},0);
	});
	setTimeout(function(){$("#menuResources").trigger('click');},500);
}

function reRenderGrid() {
	if ($("#menuResources").hasClass("active")){
		gridManager.reRender();
	}else if ($("#menuAuthorization").hasClass("active")){
		authorizationGridManager.reRender();
	}else{
		//personGridManager.reRender();
	}
}

function getDataManagedetalId(){
	return $('#dataManagedetalId').val();
}

function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        exportExcelHandler: function(){
        	UICtrl.gridExport(gridManager);
        }
    });
	gridManager = UICtrl.grid("#resourcesgrid", {
		columns: [
		    {display: "资源名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left"},
			{display: "资源KEY", name: "resourceKey", width: 120, minWidth: 60, type: "string", align: "left"},
			{display: "资源值", name: "resourceValue", width: 200, minWidth: 60, type: "string", align: "left"},
			{display: "资源路径", name: "fullName", width: 200, minWidth: 60, type: "string", align: "left"},
			{display: "资源类型", name: "dataKindTextView", width: 80, minWidth: 60, type: "string", align: "left"},
			{display: "资源编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left"}
		],
		dataAction: 'server',
		url: web_app.name+'/dataManagement/queryOpdatamanagedetailresource.ajax',
		parms:{dataManagedetalId: getDataManagedetalId()},
		width: '99%',
		height: "100%",
		heightDiff: -15,
		usePager: false,
		onLoadData:function(){
			return !(Public.isBlank(getDataManagedetalId()));
		}
	});
}

function initializAuthorizationGrid() {
	authorizationGridManager = UICtrl.grid("#dataAuthorizationQuerygrid", {
		columns: [
 			{ display: "组织名称", name: "orgName", width: "300", minWidth: 60, type: "string", align: "left" },
			{ display: "组织路径", name: "orgFullName", width: "400", minWidth: 60, type: "string", align: "left" }
		],
		dataAction: "server",
		url: web_app.name  + "/dataManagement/slicedQueryDataManageByDetailId.ajax",
		parms:{dataManagedetalId:getDataManagedetalId()},
		pageSize: 20,
		width: '99%',
		height: "100%",
		sortName: 'fullSequence',
        sortOrder: 'asc',
		heightDiff: -12,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onLoadData:function(){
			return !(Public.isBlank(getDataManagedetalId()));
		}
	});
 	UICtrl.setSearchAreaToggle(authorizationGridManager, $("#navTitle02"));
}

function initializPersonGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        exportExcelHandler: function(){
        	UICtrl.gridExport(personGridManager);
        }
    });
	personGridManager = UICtrl.grid("#persongrid", {
		columns: [
			{ display: "姓名", name: "name", width: "150", minWidth: 60, type: "string", align: "left" },
			{ display: "组织名称", name: "orgName", width: "200", minWidth: 60, type: "string", align: "left" },
			{ display: "组织路径", name: "orgFullName", width: "400", minWidth: 60, type: "string", align: "left" }
		],
		dataAction: "server",
		url: web_app.name  + "/dataManagement/slicedQueryPersonAsDataManage.ajax",
		parms:{dataManagedetalId:getDataManagedetalId(),singlePerson:'1'},
		pageSize: 20,
		width: '99%',
		height: "100%",
		sortName: 'fullSequence',
        sortOrder: 'asc',
		heightDiff: -12,
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onLoadData:function(){
			return !(Public.isBlank(getDataManagedetalId()));
		}
	});
 	UICtrl.setSearchAreaToggle(personGridManager, $("#navTitle03"));
}