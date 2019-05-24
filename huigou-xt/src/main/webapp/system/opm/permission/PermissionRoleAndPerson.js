var gridManager = null,roleAuthorizationGridManager=null,personGridManager=null;
$(document).ready(function() {
	initUI();
	initializRoleGrid();
	initializRoleAuthorizationGrid();
	initializPersonGrid();
});

function initUI(){
	$('#tabPage').tab();
	$("#queryRoleMainBtn").click(function() {
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
	$("#resetRoleMainBtn").click(function() {
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
	setTimeout(function(){$("#menuRole").trigger('click');},500);
}


function reRenderGrid() {
	if ($("#menuRole").hasClass("active")){
		gridManager.reRender();
	}else if ($("#menuRoleAuthorization").hasClass("active")){
		roleAuthorizationGridManager.reRender();
	}else{
		personGridManager.reRender();
	}
}

function getPermissionId(){
	return $('#permissionId').val();
}

function initializRoleGrid() {
	gridManager = UICtrl.grid("#roleQuerygrid", {
			columns: [
				{ display: "编码", name: "code", width: "150", minWidth: 60, type: "string", align: "left" },
 				{ display: "名称", name: "name", width: "200", minWidth: 60, type: "string", align: "left" },
				{ display: "路径", name: "fullName", width: "300", minWidth: 60, type: "string", align: "left" }
			],
			dataAction: "server",
			url: web_app.name  + "/access/slicedQueryRolesByPermission.ajax",
			parms:{permissionId:getPermissionId()},
			pageSize: 20,
			width: '99%',
			height: "100%",
			sortName: 'fullId',
            sortOrder: 'asc',
			heightDiff: -12,
			checkbox: true,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return !(Public.isBlank(getPermissionId()));
			}
	});
 	UICtrl.setSearchAreaToggle(gridManager, $("#navTitle01"));
}


function initializRoleAuthorizationGrid() {
	roleAuthorizationGridManager = UICtrl.grid("#roleAuthorizationQuerygrid", {
			columns: [
 				{ display: "组织名称", name: "name", width: "200", minWidth: 60, type: "string", align: "left" },
				{ display: "组织路径", name: "fullName", width: "300", minWidth: 60, type: "string", align: "left" },
				{ display: "角色编码", name: "roleCode", width: "150", minWidth: 60, type: "string", align: "left" },
 				{ display: "角色名称", name: "roleName", width: "200", minWidth: 60, type: "string", align: "left" },
				{ display: "角色路径", name: "roleFullName", width: "300", minWidth: 60, type: "string", align: "left" }
			],
			dataAction: "server",
			url: web_app.name  + "/access/slicedQueryRoleAuthorizeByPermission.ajax",
			parms:{permissionId:getPermissionId()},
			pageSize: 20,
			width: '99%',
			height: "100%",
			sortName: 'fullSequence',
            sortOrder: 'asc',
			heightDiff: -12,
			checkbox: true,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return !(Public.isBlank(getPermissionId()));
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
				{ display: "组织路径", name: "orgFullName", width: "300", minWidth: 60, type: "string", align: "left" },
				{ display: "角色编码", name: "roleCode", width: "150", minWidth: 60, type: "string", align: "left" },
 				{ display: "角色名称", name: "roleName", width: "200", minWidth: 60, type: "string", align: "left" },
				{ display: "角色路径", name: "roleFullName", width: "300", minWidth: 60, type: "string", align: "left" }
			],
			dataAction: "server",
			url: web_app.name  + "/access/slicedQueryPersonsByPermission.ajax",
			parms:{permissionId:getPermissionId(),singlePerson:'1'},
			pageSize: 20,
			width: '99%',
			height: "100%",
			sortName: 'fullSequence',
            sortOrder: 'asc',
			heightDiff: -12,
			toolbar: toolbarOptions,
			checkbox: true,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return !(Public.isBlank(getPermissionId()));
			}
	});
 	UICtrl.setSearchAreaToggle(personGridManager, $("#navTitle03"));
}