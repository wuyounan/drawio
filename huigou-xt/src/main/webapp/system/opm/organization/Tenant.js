var refreshFlag = false, gridManager = null, lastSelectedId = "";
var TENANT_KIND = 35
$(document).ready(function() {
	initUI();
	initTreeView();
	initGrid();
	
	function initUI(){
	    UICtrl.initDefaultLayout();
	}

	function initTreeView(){
		$('#maintree').commonTree({
			kindId: TENANT_KIND,
			onClick:  function (data) {
	            if (data && lastSelectedId != data.id) {
	            	onFolderTreeNodeClick(data, data.id);
	            	lastSelectedId = data.id;
	            }
	        },
		});
	}
	
	function initGrid() {
		var toolbarOptions=getToolbarOptions();
		var columns=getGridColumns();
		var manageType=getManageType();
		gridManager = UICtrl.grid('#maingrid', {
			columns:columns,
			dataAction: 'server',
			url: web_app.name+'/tenant/slicedQueryTenants.ajax',
			pageSize: 20,
			manageType:manageType,
			width:'100%',
			height:'100%',
			heightDiff: -8,
			sortOrder:'asc',
			checkbox: true,
			toolbar: toolbarOptions,
			fixedCellHeight:true,
			selectRowButtonOnly:true,
			onDblClickRow:function(data, rowindex, rowobj) {
				updateHandler(data.id);
			}
		});
		UICtrl.setSearchAreaToggle(gridManager);
	}
	
	afteInit();
});



function getGridColumns(){
	var columns=[{ display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" }];
	columns.push({ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" });
	columns.push({ display: "描述", name: "description", width: 250, minWidth: 60, type: "string", align: "left" });
	columns.push({ display: "联系人", name: "contacts", width: 100, minWidth: 60, type: "string", align: "left" });
	columns.push({ display: "联系电话", name: "contactNumber", width: 150, minWidth: 60, type: "string", align: "left" });
	columns.push({ display: "状态", name: "status", width: 60, minWidth: 30, type: "string", align: "center",
        render: UICtrl.getStatusInfo
    });
	var addColumns=getAddGridColumns();
	if($.isArray(addColumns)){
		columns.push.apply(columns,addColumns);
	}
	return columns;
}

function getToolbarOptions(){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		addHandler: addHandler, 
		updateHandler: function(){
			 var row = UICtrl.checkSelectedRows(gridManager);
			    if (row) {
			    	updateHandler(row.id);
			    }
		},
		deleteHandler: deleteHandler,
		enableHandler: enableHandler,
		disableHandler: disableHandler,
		buildOrgStructureByOrgIdHandler: { id: "buildOrgStructureByOrgId", text: "根据模板生成组织", click: buildOrgStructureByOrgIdHandler, img: "fa-cubes" },
		buildDefaultOrgStructureHandler: { id: "buildDefaultOrgStructure", text: "生成默认组织", click: buildDefaultOrgStructureHandler, img: "fa-cubes" }
	});
	var addButtons=getAddToolbarButton();
	if($.isArray(addButtons)){
		toolbarOptions.items.push.apply(toolbarOptions.items,addButtons);
	}
	if($.isPlainObject(addButtons)&&addButtons['items']){
		toolbarOptions.items.push.apply(toolbarOptions.items,addButtons['items']);
	}
	return toolbarOptions;
}


function onFolderTreeNodeClick(data,folderId) {
	var html=[],localFolderId=folderId;
	if(localFolderId==TENANT_KIND){
		localFolderId="";
		html.push('租户维护');
	}else{
		html.push('<span class=\"tomato-color\">[',data.name,']</span>租户维护');
	}
	$("#layout").layout("setCenterTitle", html.join(''));
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId: localFolderId});
	}
}

// 查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
} 

//得到Id
function getId() {
    return $("#id").val() || "";
}

//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

//添加按钮 
function addHandler() {
    var nodeData = $('#maintree').commonTree('getSelected');
    if (!nodeData || nodeData.id == TENANT_KIND) {
        Public.tip('请选择左侧父节点。');
        return;
    }
	UICtrl.showAjaxDialog({
		url: web_app.name + '/tenant/showInsertTenant.load',
		title:"添加租户",
		param: { folderId: nodeData.id },
		width: 700,
		ok: insert,
		close: dialogClose
	});
}

//编辑按钮
function updateHandler(id) {
    UICtrl.showAjaxDialog({
        url: web_app.name + '/tenant/showUpdateTenant.load',
        title: "修改租户",
        param: {id: id},
        width: 700,
        ok: update,
        close: dialogClose
    });
}

//关闭对话框
function dialogClose() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

//新增保存
function insert() {
    var _self=this;
	$('#submitForm').ajaxSubmit({url: web_app.name + '/tenant/insertTenant.ajax',
		success: function (id) {
			refreshFlag = true;
            _self.close();
        }
	});
}

//编辑保存
function update() {
    var _self = this;
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/tenant/updateTenant.ajax',
        param: {id: getId()},
        success: function () {
            _self.close();
            refreshFlag = true;
        }
    });
}

function deleteHandler() {
    var action = "tenant/deleteTenant.ajax";
    DataUtil.del({
        action: action,
        idFieldName: 'id',
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}

function enableHandler(){
	DataUtil.updateById({ action: 'tenant/updateTenantStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status:1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'tenant/updateTenantStatus.ajax',
		gridManager: gridManager,idFieldName:'id',param:{status:0},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

function getSelectedTenant(){
	var row = gridManager.getSelectedRows();
	if (row.length == 0){
		Public.errorTip("请选择租户数据。");
		return false;
	}
	if (row.length > 1){
		Public.errorTip("请选择一个租户数据。");
		return false;
	}
	return row[0];
}

function buildOrgStructureByOrgIdHandler(){
	var tenant = getSelectedTenant();
	if (!tenant){
		return;
	}
	var selectOrgparams = OpmUtil.getSelectOrgDefaultParams();
	selectOrgparams = jQuery.extend(selectOrgparams, {displayableOrgKinds: "ogn", selectableOrgKinds: "ogn"});
	var options = { 
			params: selectOrgparams, 
			confirmHandler: function (){
				var data = this.getSelectedData();
				if (data.length == 0) {
					Public.errorTip("请选择组织。");
					return;
				}
				if (data.length != 1) {
					Public.errorTip("请选择一个组织。");
					return;
				}
				var _self=this;
				Public.ajax(web_app.name + '/tenant/buildOrgStructureByOrgId.ajax', {tenantId: tenant.id, orgId: data[0].id},function () {
					_self.close();
					reloadGrid();
				});
			}, 
			closeHandler: dialogClose, 
			title : "选择组织"
	};
	OpmUtil.showSelectOrgDialog(options);
}

function buildDefaultOrgStructureHandler(){
	var tenant = getSelectedTenant();
	if (!tenant){
		return;
	}
	UICtrl.confirm('您确定需要生成默认组织吗?', function () {
		Public.ajax(web_app.name + '/tenant/buildDefaultOrgStructure.ajax', {tenantId: tenant.id},function () {
			reloadGrid();
		});
    });
}

