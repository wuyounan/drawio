var gridManager, permissionGridManager,
   refreshFlag, tabHeader = "角色列表", operateCfg = {}, enableTspm, isUseTspm;
var pageParam = {rootId: 1, rootParentId: 0, parentId: 0, globalTenantId: "global"};
var tenantKindId;
$(function () {
	getQueryParameters();
    initializeOperateCfg();
    bindEvents();
    initializeUI();
    initializeTree();
    initializeGrid();
    initializePermissionGrid();
    
    function getQueryParameters() {
    	isUseTspm = $("#isUseTspm").val();
        enableTspm = $("#enableTspm").val();
    	tenantKindId = Public.getQueryStringByName("tenantKindId");
    }
    
    function bindEvents() {
        $("#btnQuery").click(function () {
            var params = $(this.form).formToJSON();
            UICtrl.gridSearch(gridManager, params);
        });
    }

    function initializeOperateCfg() {
        var actionPath = web_app.name + "/access/";
        operateCfg.slicedQueryAction = actionPath + 'slicedQueryRoles.ajax';
        operateCfg.showInsertAction = actionPath + 'showRoleDetail.load';
        operateCfg.showUpdateAction = actionPath + 'loadRole.load';

        operateCfg.insertAction = actionPath + 'insertRole.ajax';
        operateCfg.updateAction = actionPath + 'updateRole.ajax';
        operateCfg.deleteAction = '/access/deleteRole.ajax';
        operateCfg.updateSequenceAction = actionPath
				+ "updateRolesSequence.ajax";
        operateCfg.assignFunPermissionAction = actionPath
				+ "assignFunPermission.ajax";

        operateCfg.showAssignFunctionDialogAction = actionPath
				+ "showAssignFunctionDialog.do";
        operateCfg.moveRoleAction = actionPath + "moveRoles.ajax",
        operateCfg.moveTenantRoleAction = actionPath + "moveTenantRoles.ajax",

		operateCfg.showInsertTitle = "添加角色";
        operateCfg.showUpdateTitle = "修改角色";
    }

    function initializeUI() {
    	UICtrl.layout("#layout",{
    		leftWidth:2,
    		rightWidth:5,
    		allowRightCollapse: false,
    		allowRightResize: false
    	});
    }

    function initializeTree() {
        $('#maintree').commonTree({
        	loadTreesAction: 'access/queryRoles.ajax',
            parentId: pageParam.rootParentId,
            getParam: function(){
            	return {tenantKindId: tenantKindId}
            },
            isLeaf: function (data) {
                return parseInt(data.hasChildren) == 0;
            },
            onClick: function (data) {
                onTreeNodeClick(data);
            },
            dataRender: function (data) {
            	return data;
            },
            IsShowMenu: false
        });
    }
    

    function initializeGrid() {
        var toolbarparam = {
            addHandler: showInsertDialog,
            updateHandler: showUpdateDialog,
            deleteHandler: deleteRole,
            saveSortIDHandler: updateRoleSequence,
            moveHandler: moveHandler
        };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: 80, minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left",
						render: function (item) {
							var roleKindId = item.roleKindId;
					    	if ((enableTspm == "true") && (roleKindId != 'common')){
					    		return item.name;
					    	}
					    	
					    	if (item.nodeKindId == 1){
					    		return item.name;
					    	}
					    	
					    	if (tenantKindId != pageParam.globalTenantId && item.tenantId == pageParam.globalTenantId){
					    		return item.name;
					    	}
					    	
					        var html = ['<a href="javascript:void(null);" class="refreshPermission" '];
					        html.push('roleKindId="', roleKindId, '" ');
					        html.push('roleId="', item.id, '" ');
					        html.push('roleName="', item.name, '" ');
					        html.push('>');
					        html.push(item.name);
					        html.push('</a>');
					        return html.join('');
						}
					},
					{ display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return UICtrl.sequenceRender(item);
					    }
					},
					{ display: "类别", name: "roleKindIdTextView", width: 80, minWidth: 60, type: "string", align: "left" },
					{ display: '操作', name: "operation",width: 80, isAllowHide: false,isSort:false,
					    render: function (item) {
					    	var roleKindId = item.roleKindId;
					    	if ((enableTspm == "true") && (roleKindId != 'common')){
					    		return "";
					    	}
					    	
					    	if (item.nodeKindId == 1){
					    		return "";
					    	}
					    	
					    	if (tenantKindId != pageParam.globalTenantId && item.tenantId == pageParam.globalTenantId){
					    		return "";
					    	}
					    	
					        var html = ['<a href="javascript:void(null);" class="GridStyle" '];
					        html.push('roleKindId="', roleKindId, '" ');
					        html.push('roleId="', item.id, '" ');
					        html.push('roleName="', item.name, '" ');
					        html.push('>');
					        html.push('分配权限');
					        html.push('</a>');
					        return html.join('');
					    }
					},
					{ display:"角色人员类别", name: "rolePersonKindTextView", width: 100, minWidth: 60, type: "string", align: "left" },
					{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
					    render: function (item) {
					        return UICtrl.getStatusInfo(item);
					    }
					}
				],
            dataAction: "server",
            url: operateCfg.slicedQueryAction,
            parms: { parentId: pageParam.rootId, tenantKindId: tenantKindId },
            title: "角色列表",
            pageSize: 20,
            toolbar: toolbarOptions,
            width: '100%',
            height: "100%",
            sortName: 'sequence',
            sortOrder: 'asc',
            heightDiff: -10,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                doShowUpdateDialog(data);
            },
            onSelectRow: function (data, rowindex, rowobj) {
                refreshPermission(data.id, data.name, data.roleKindId);
            }
        });
        UICtrl.createGridQueryBtn('#maingrid', function (param) {
            UICtrl.gridSearch(gridManager, { keyword: encodeURI(param) });
        });
        $('#maingrid').on('click',function(e){
    		var $clicked = $(e.target || e.srcElement);
    		if($clicked.is('a.GridStyle')){
    			var roleKindId = $clicked.attr('roleKindId');
    	        var roleId = $clicked.attr('roleId');
    	        var roleName = $clicked.attr('roleName');
    	        assignFunction(roleId, roleName);
    			return false;
    		}
    		if($clicked.is('a.refreshPermission')){
    			var roleKindId = $clicked.attr('roleKindId');
    	        var roleId = $clicked.attr('roleId');
    	        var roleName = $clicked.attr('roleName');
    	        showRolePermission(roleId,roleName);
    	        refreshPermission(roleId, roleName, roleKindId);
    			return false;
    		}
    	});
    }

    function refreshPermission(roleId, name, roleKindId) {
        $('#permissiongrid').find(".l-panel-header-text").html("角色[<font color=Tomato>" + name + "</font>]对应权限");
        $('#mainRoleId').val(roleId);
        $('#mainrRoleKindId').val(roleKindId);
        permissionGridManager.options.parms.roleId = roleId;
        permissionGridManager.options.parms.roleKindId = roleKindId;
        permissionGridManager.loadData();
    }

    function initializePermissionGrid() {
        permissionGridManager = UICtrl.grid("#permissiongrid", {
            columns: [
						{ display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
 					    { display: "路径", name: "fullName", width: 400, minWidth: 60, type: "string", align: "left",
 					        render: function (item) {
 					            if (item.type == 'field') {//字段权限显示连接
 					                var html = ['<a href="javascript:void(null);" class="GridStyle" '];
 					                html.push('funId="', item.id, '" ');
 					                html.push('funName="', item.name, '" ');
 					                html.push('onclick="showPermissionField(this)" ');
 					                html.push('>');
 					                html.push(item.fullName);
 					                html.push('</a>');
 					                return html.join('');
 					            }
 					            return item.fullName;
 					        }
 					    },
 					   { display: "创建人", name: "createdByName", width: 60, minWidth: 60, type: "string", align: "left" },
 					   { display: "创建日期", name: "createdDate", width: 150, minWidth: 80, type: "time", align: "left" }
						],
            dataAction: "server",
            url: web_app.name + "/access/slicedQueryPermissionsByRoleId.ajax",
            pageSize: 20,
            title: "&nbsp;",
            width: '100%',
            height: "100%",
            heightDiff: -12,  
            sortName: 'fullName',
            sortOrder: 'asc',
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onLoadData: function () {
                return $('#mainRoleId').val() != '';
            }
        });
    }
    UICtrl.createGridQueryBtn('#permissiongrid', function (param) {
        UICtrl.gridSearch(permissionGridManager, { keyword: encodeURI(param) });
    });
});

function getId() {
    return $("#id").val();
}

function canEditRole(obj){
	return (tenantKindId == pageParam.globalTenantId || obj.tenantId != pageParam.globalTenantId) && pageParam.parentId != pageParam.rootParentId;
}

function isNotUseOrEnableTspm(){
	return (Public.isBlank(isUseTspm) || (isUseTspm == "false")) ||
    		(isUseTspm == "true" && enableTspm=="true");
}

function showInsertDialog() {	
	var node = $('#maintree').commonTree('getSelected');
    if (!node) {
        Public.tip('请选择左侧父节点。');
        return;
    }
    
    if (node.id != pageParam.rootId && !canEditRole(node)){
    	Public.tip('您不能在通用角色树下创建角色。');
        return;
    }
    
    UICtrl.showAjaxDialog({
        url: operateCfg.showInsertAction,
        title: operateCfg.showInsertTitle,
        param: {parentId: pageParam.parentId},
        width: 530,
        init: function(){
        	//if (Public.isBlank(enableTspm) || enableTspm == "false"){
        	if (isNotUseOrEnableTspm()){
        		$('#kindId').combox('setValue','common');
        		$('#kindId').parent().parent().hide();
        	}
        },
        ok: doSaveRole,
        close: onDialogCloseHandler
    });
}

function doShowUpdateDialog(row) {
    UICtrl.showAjaxDialog({
        url: operateCfg.showUpdateAction,
        title: operateCfg.showUpdateTitle,
        param: { id: row.id },
        width: 530,
        init: function(){
        	if (isNotUseOrEnableTspm()){
        		$('#kindId').parent().parent().hide();
        	}
        },
        ok: canEditRole(row) ? doSaveRole : false,
        close: onDialogCloseHandler
    });
}

function showUpdateDialog() {
	var row = DataUtil.getUpdateRow(gridManager);
	if (!row){ return; }
    doShowUpdateDialog(row);
}

function doSaveRole() {
    var _self = this;
    var id = getId();
    if (!id) {
        $("#tenantId").val(tenantKindId);
    }
    $('#submitForm').ajaxSubmit({
        url: (id ? operateCfg.updateAction : operateCfg.insertAction),
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function deleteRole() {
    DataUtil.del({ action: operateCfg.deleteAction, gridManager: gridManager, onSuccess: reloadGrid });
}

function updateRoleSequence() {
    DataUtil.updateSequence({action: "access/updateRolesSequence.ajax", gridManager: gridManager, onSuccess: reloadGrid});
}

function assignFunction(id, name) {
    UICtrl.addTabItem({ tabid: 'assignFunction' + id, text: "角色[" + name + "]分配权限", url: operateCfg.showAssignFunctionDialogAction + '?roleId=' + id });
}

//保存授权该方法已停用
function doAssignFunction() {
    var data = this.iframe.contentWindow.selectedData;
    if (!data)
        return;
    var _self = this;
    var functionIds = new Array();
    for (var i = 0; i < data.length; i++) {
        if (data[i].id)
            functionIds[functionIds.length] = data[i].id;
    }

    var params = {};
    params.roleId = roleId;
    params.functionIds = $.toJSON(functionIds);
    Public.ajax(operateCfg.assignFunPermissionAction, params, function () {
        _self.close();
    });
}

function setButtonsVisible(visible){
	if (visible){
		$("#toolbar_menuAdd").show();
		$("#toolbar_menuUpdate").show();
		$("#toolbar_menuDelete").show();
		$("#toolbar_menuSaveID").show();
		$("#toolbar_menuMove").show();
		
		$("#separator_lineAdd").show();
		$("#separator_lineUpdate").show();
		$("#separator_lineDelete").show();
		$("#separator_lineSaveID").show();
		$("#separator_lineMove").show();
	}else{
		$("#toolbar_menuAdd").hide();
		$("#toolbar_menuUpdate").hide();
		$("#toolbar_menuDelete").hide();
		$("#toolbar_menuSaveID").hide();
		$("#toolbar_menuMove").hide();
		
		$("#separator_lineAdd").hide();
		$("#separator_lineUpdate").hide();
		$("#separator_lineDelete").hide();
		$("#separator_lineSaveID").hide();
		$("#separator_lineMove").hide();
	}
}

function onTreeNodeClick(data) {
    if (data.id == pageParam.rootId) {
    	$('#maingrid').find(".l-panel-header-text").html(tabHeader)
    } else {
    	 $('#maingrid').find(".l-panel-header-text").html(
					"<span class=\"tomato-color\">["  + data.name + "]</span>" + tabHeader);
    }
    pageParam.parentId = data.id;
    if (gridManager) {
        UICtrl.gridSearch(gridManager, {parentId: pageParam.parentId});
    }
    if (data.id == pageParam.rootId){
    	setButtonsVisible(true);
    }else{
    	setButtonsVisible(canEditRole(data));
    }
}

function reloadGrid() {
	$("#maintree").commonTree('refresh', pageParam.parentId);
    UICtrl.gridSearch(gridManager, { parentId: pageParam.parentId });
}

function moveHandler() {
    var ids = DataUtil.getSelectedIds({
        gridManager: gridManager,
        idFieldName: "id"
    });

    if (!ids) {
        Public.errorTip("请选择要移动的角色。");
        return;
    }

    UICtrl.showDialog({
        title: "移动到...",
        width: 350,
        content: '<div style="overflow-x: hidden; overflow-y: auto; width: 340px;height:250px;"><ul id="movetree"></ul></div>',
        init: function () {
            $('#movetree').commonTree({
                loadTreesAction: "access/queryRoles.ajax",
                idFieldName: 'id',
                IsShowMenu: false,
                dataRender: function (data) {
                    return data;
                },
            });
        },
        ok: function () {
        	doMoveRoles.call(this, ids);
        },
        close: function () {
        	return true;
        }
    });
}

function doMoveRoles(ids) {
	var _self = this;
    var newParentNode = $('#movetree').commonTree('getSelected');
    if (newParentNode == null) {
        Public.errorTip('请选择移动到的节点。');
        return false;
    }
    var newParentId = newParentNode.id;
    if (!newParentId) {
        Public.errorTip('请选择移动到的节点。');
        return false;
    }
    
    if (newParentNode.nodeKindId == 2){
    	Public.errorTip('您不能移动角色到叶节点，请选择文件夹。');
        return false;
    }
    
    if (newParentId == pageParam.parentId) {
        Public.errorTip('您选择的目标节点和源节点一样，不能移动。');
        return false;
    }
    
    if (!canEditRole(newParentNode)){
    	Public.errorTip('您不能移动角色到通用角色树下。');
        return false;
    }

    var params = {};
    params.parentId = newParentId;
    params.ids = $.toJSON(ids);
    
    var url = tenantKindId == pageParam.globalTenantId ? operateCfg.moveRoleAction : operateCfg.moveTenantRoleAction
    
    Public.ajax(url, params, function (data) {
    	_self.close();
        reloadGrid();
        $("#maintree").commonTree('refresh', newParentId);
    });
}
function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function showPermissionField(obj) {
    var id, name;
    id = $(obj).attr('funId');
    name = $(obj).attr('funName');
    UICtrl.showFrameDialog({
        url: web_app.name + "/system/opm/permissionField/showPermissionField.jsp",
        param: { functionFieldGroupId: id },
        title: name,
        width: 650,
        height: 400,
        cancelVal: '关闭',
        ok: false,
        cancel: true
    });
}

function showRolePermission(id,name){
	UICtrl.showFrameDialog({
		url: web_app.name + "/access/forwardRolePermission.do",
		param: {roleId: id},
		title: name,
		width: getDefaultDialogWidth(),
		height: getDefaultDialogHeight(),
		cancelVal: '关闭',
		ok: false,
		cancel:true
	});
}