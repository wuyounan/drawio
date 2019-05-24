var gridManager = null,roleAuthorizationGridManager=null,personGridManager=null;
$(document).ready(function() {
	initUI();
	initializePermissionGrid();
	initializRoleAuthorizationGrid();
	initializPersonGrid();
});

function initUI(){
	$('#tabPage').tab();
	$("#queryPermissionMainBtn").click(function() {
		var params = $(this.form).formToJSON();
		UICtrl.gridSearch(gridManager, params);
	});
	$("#queryRoleAuthorizationMainBtn").click(function() {
		var params = $(this.form).formToJSON();
		UICtrl.gridSearch(roleAuthorizationGridManager, params);
	});
	$("#queryPersonMainBtn").click(function() {
		var params = $(this.form).formToJSON();
		UICtrl.gridSearch(personGridManager, params);
	});
	$("#resetPermissionMainBtn").click(function() {
		$(this.form).formClean();
	});
	$("#resetRoleAuthorizationMainBtn").click(function() {
		$(this.form).formClean();
	});
	$("#resetPersonMainBtn").click(function() {
		$(this.form).formClean();
	});
	$('li.tabli').on('click',function(){
		setTimeout(function(){reRenderGrid();},0);
	});
	setTimeout(function(){$("#menuPermission").trigger('click');},500);
}

function reRenderGrid() {
	if ($("#menuPermission").hasClass("active")){
		gridManager.reRender();
	}else if ($("#menuRoleAuthorization").hasClass("active")){
		roleAuthorizationGridManager.reRender();
	}else{
		personGridManager.reRender();
	}
}

function getRoleId(){
	return $('#roleId').val();
}

function initializePermissionGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        exportExcelHandler: function(){
        	UICtrl.gridExport(gridManager);
        }
    });
	gridManager = UICtrl.grid("#permissiongrid", {
			columns: [
				{ display: "编码", name: "code", width: "200", minWidth: 60, type: "string", align: "left" },
 				{ display: "名称", name: "name", width: "250", minWidth: 60, type: "string", align: "left" },
				{ display: "路径", name: "fullName", width: "500", minWidth: 60, type: "string", align: "left" }
			],
			dataAction: "server",
			url: web_app.name  + "/access/slicedQueryPermissionsByRoleId.ajax",
			parms:{roleId:getRoleId()},
			pageSize: 20,
			width: '99%',
			height: "100%",
			sortName: 'fullId',
            sortOrder: 'asc',
			heightDiff: -12,
			toolbar: toolbarOptions,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return !(Public.isBlank(getRoleId()));
			}
	});
	UICtrl.setSearchAreaToggle(gridManager, $("#navTitle01"));
}


function initializRoleAuthorizationGrid() {
	roleAuthorizationGridManager = UICtrl.grid("#roleAuthorizationQuerygrid", {
			columns: [
 				{ display: "组织名称", name: "name", width: "300", minWidth: 60, type: "string", align: "left" },
				{ display: "组织路径", name: "fullName", width: "400", minWidth: 60, type: "string", align: "left" }
			],
			dataAction: "server",
			url: web_app.name  + "/access/slicedQueryRoleByAuthorize.ajax",
			parms:{roleId:getRoleId()},
			pageSize: 20,
			width: '99%',
			height: "100%",
			sortName: 'fullSequence',
            sortOrder: 'asc',
			heightDiff: -12,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return !(Public.isBlank(getRoleId()));
			}
	});
 	UICtrl.setSearchAreaToggle(roleAuthorizationGridManager, $("#navTitle02"));
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
			url: web_app.name  + "/access/slicedQueryPersonAsRoleAuthorize.ajax",
			parms:{roleId:getRoleId(),singlePerson:'1'},
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
				return !(Public.isBlank(getRoleId()));
			}
	});
 	UICtrl.setSearchAreaToggle(personGridManager, $("#navTitle03"));
}

