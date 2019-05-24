var treeManager, gridManager, refreshFlag = false, selectDialog, lastSelectedId = 0;

$(function () {
    bindEvents();
    bindUIElementPermissionBtnEvent();

    loadPermissionTree();
    initializeUI();
    initializeGrid();

    function initializeUI() {
        UICtrl.initDefaultLayout();
    }

    function bindEvents() {
        $("#name").live("blur", function () {
            if (!$("#fullName").val()) {
                $("#fullName").val($("#name").val());
            }
        });
    }
    
    function initializeGrid() {
        var toolbarparam = {
            addHandler: showInsertDialog,
            updateHandler: showUpdateDialog,
            uiElementPermissionHandler:{id: 'uiElementPermissionHandler', text:'界面元素设置', img:'fa-bars', click: uiElementPermissionHandler },
            enableHandler: enableHandler,
			disableHandler: disableHandler,
            deleteHandler: deletePermission,
            saveSortIDHandler: updatePermissionSequence
        };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
                  { display: '编码', name: 'code', width: 200, minWidth: 60, type: "string", align: "left" },
				  { display: '名称', name: 'name', width: 150, minWidth: 60, type: "string", align: "left" },
				  { display: '状态', name: 'status', width: 60, minWidth: 60, type: "int", align: "left",
						 render: function (item) {
							return UICtrl.getStatusInfo(item.status);
						 }
				  },
				  { display: '类型', name: 'nodeKindIdTextView', width: 80, minWidth: 60, type: "string", align: "left" },
				  { display: '全名称', name: 'fullName', width: 350, minWidth: 60, type: "string", align: "left" },
				  { display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "int", align: "left",
					     render: function (item) {
					         return "<input type='text' id='txtSequence_"+ item.id+ "' class='textbox' value='"+ item.sequence + "' />";
					     }
				  },
				  { display: "授权情况", name: "name", width: "100", minWidth: 60, type: "string", align: "center",isSort:false,
						render: function (item) { 
							var html=['<a href="javascript:void(null);" class="GridStyle" '];
							html.push('id="',item.id,'" ');
							html.push('name="',item.name,'" ');
							html.push('onclick="showRoleList(this)" ');
							html.push('>');
							html.push('授权情况');
							html.push('</a>');
							return html.join('');
						}
				  }
			],
            dataAction: 'server',
            url: web_app.name + "/access/queryPermissions.ajax",
            parms: { parentId: "0" },
            usePager: false,
            sortName: "sequence",
            SortOrder: "asc",
            toolbar: toolbarOptions,
            width: '100%',
            height: '100%',
            heightDiff: -13,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowindex, rowobj) {
                doShowUpdateDialog(data.id);
            }
        });

        UICtrl.setSearchAreaToggle(gridManager);
    }

    function loadPermissionTree() {
        $('#maintree').commonTree({
            loadTreesAction: "/access/queryPermissions.ajax",
            isLeaf: function (data) {
                return data.hasChildren == 0;
            },
            onClick: function (data) {
                if (data && lastSelectedId != data.id) {
                    reloadGrid3(data.id, data.name);
                }
            },
            IsShowMenu: false
        });
    }
});

//查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

function getId() {
    return $("#id").val();
}

function reloadGrid() {
    $("#maintree").commonTree('refresh', lastSelectedId);
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

function reloadGrid2() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

function reloadGrid3(id, name) {
	$('#layout').layout('setCenterTitle',"<span class=\"tomato-color\">[" + name + "]</span>子权限列表");
    lastSelectedId = id;
    var params = $("#queryMainForm").formToJSON();
    params.parentId = id;
    UICtrl.gridSearch(gridManager, params);
}

function showInsertDialog() {
    if (!$('#maintree').commonTree('getSelectedId')) {
        Public.tip('请选择左侧父权限。');
        return;
    }
    var parent =  $("#maintree").commonTree('getSelected');
    UICtrl.showAjaxDialog({
        title: "添加权限",
        param: {
            parentId: parent.id,
            resourceKindId:parent.resourceKindId
        },
        init: function(){
        	initDetailPageCtls.call(this, "add");  
        },
        width: 420,
        url: web_app.name + '/access/showPermissionDetail.load',
        ok: doSavePermission,
        close: onDialogCloseHandler
    });
}

function doShowUpdateDialog(id) {
    UICtrl.showAjaxDialog({
    	title: "修改权限",
    	url: web_app.name + '/access/loadPermission.load',
        param: { id: id },
        init: function(){
        	initDetailPageCtls.call(this, "update");        
        },
        width: 420,
        ok: doSavePermission,
        close: onDialogCloseHandler
    });
}

function initDetailPageCtls(operation){	
	
	if (operation == "add"){
		var parent =  $("#maintree").commonTree('getSelected');
		$('#resourceKindId').val(parent.resourceKindId);
		if (parent.resourceKindId == ResourceKind.FUN && parent.nodeKindId == PermissionNodeKind.FUN){
			 $('#resourceId').val(parent.resourceId);
			 $('#resourceName').val(parent.name);
			 $('#nodeKindId').combox('setValue',PermissionNodeKind.PERMISSION);
		}else if(parent.resourceKindId == ResourceKind.REMIND){
			 $('#nodeKindId').combox('setValue', PermissionNodeKind.REMIND);
		}else if(parent.resourceKindId == ResourceKind.BUSINESSCLASS){
			 $('#nodeKindId').combox('setValue', PermissionNodeKind.BUSINESSCLASS);
		}
	}
	
	if ($("#nodeKindId").val() == PermissionNodeKind.PERMISSION)  {
		UICtrl.disable("#resourceName");
		$('#operationName').searchbox({
	 		type: "sys", name: "funOperation",
			back: { id: '#operationId', name: '#operationName'},
			onChange: function(values, data) {
				if ($('#detailCode').val() == ""){
					$('#detailCode').val(parent.code + ":" + data.code);
					$('#detailName').val(parent.name + data.name);
				}
			}
	   });
	}else if($("#nodeKindId").val() == PermissionNodeKind.REMIND){
		$('#operationDiv').hide();
		UICtrl.disable("#operationName");
		$('#resourceName').searchbox({
	 		type: "sys", name: "messageRemind",
	 		getParam:function(){
	 			var nodeKindId=$("#nodeKindId").val();
	 			if(nodeKindId!= PermissionNodeKind.REMIND){
	 				Public.tip('错误的节点类型，请选择[提醒]。');
	 				return false;
	 			}
	 		},
			back: { id: '#resourceId', name: '#resourceName'},
			onChange: function(values, data) {
				if ($('#detailCode').val() == ""){
					$('#detailCode').val(data.code);
					$('#detailName').val(data.name);
				}
			}
	   });
	}else{
		if(!Public.isBlank($("#nodeKindId").val())){
			UICtrl.disable("#nodeKindId");
		}
		$("#operationName").searchbox("setValue", "");
		UICtrl.disable("#operationName");
		UICtrl.disable("#resourceName");
		$('#operationDiv').hide();
	}
}

function showUpdateDialog() {
	var id = DataUtil.getUpdateRowId(gridManager);
	if (!id){ return; }
    doShowUpdateDialog(id);
}

function doSavePermission() {
    var _self = this;
    var id = getId();
    $('#submitForm').ajaxSubmit({
        url: web_app.name
				+ (id ? '/access/updatePermission.ajax': '/access/insertPermission.ajax'),
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function bindUIElementPermissionBtnEvent(){
	$('#uiElementPermissionBtn').comboDialog({type:'opm', 
		name: 'uiElement', 
		width:400, lock:false, 
		checkbox:true,
		dataIndex:'id'
	});
}


function showSelectUIElementDialog(gm, permissionId, sequence){
	$('#uiElementPermissionBtn').comboDialog({
		width:500,
		onChoose:function(){
		    var rows = this.getSelectedRows();
		    var addRows = [];
		    var oldData = gm.getData();
		    var added;
		    $.each(rows, function(i, o){
		    	added = false;		    	
		    	$.each(oldData, function(index, obj){
		    		if (obj.code == o.code){
		    			added = true;
		    			return false;
		    		}
		    	});
		    	
		    	if (!added){
			    	o.resourceId = o.id;
			    	o.sequence = ++sequence;
			    	o.permissionId = permissionId;
			    	delete o.id;
			    	addRows.push( o );
		    	}
		    	
		    });
		    
		    if (addRows.length > 0){
		    	gm.addRows(addRows);
		    }
		    return true;
	    }	
	}).trigger('click');
}

function deletePermission() {
    var action = "access/deletePermissions.ajax";
    DataUtil.del({ action: action, gridManager: gridManager, onSuccess: reloadGrid });
}

function updateStatus(message, status){
	DataUtil.updateById({ action: 'access/updatePermissionsStatus.ajax',
		gridManager: gridManager,  param:{status: status},
		message: message,
		onSuccess:function(){
			reloadGrid();	
		}
	});	
}

function uiElementPermissionHandler(){
	var row = gridManager.getSelectedRow();
	if (!row) {
		Public.tip("请选择数据。");
		return;
	}
	var nodeKindId=row.nodeKindId;
	if(nodeKindId!='uiElement'){
		Public.tip("请选择界面元素类型数据。");
		return;
	}
	UICtrl.showFrameDialog({
		url: web_app.name + "/access/showUIElementPermission.do",
		param: { permissionId: row.id },
		title: "界面元素权限",
		width: 730,
		height: 350,
		cancelVal: '关闭',
		ok: false,
		cancel: true
	});
}

function enableHandler(){
	updateStatus('确实要启用选中数据吗?', 1);
}

//禁用
function disableHandler(){
	updateStatus('确实要禁用选中数据吗?', 0);
}

function updatePermissionSequence() {
    var action = "access/updatePermissionsSequence.ajax";
    DataUtil.updateSequence({
        action: action,
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}

function movePermission() {
    var ids = DataUtil.getSelectedIds({ gridManager: gridManager });
    if (!ids) {
        return;
    }
    if (!selectDialog) {
        selectDialog = UICtrl.showDialog({
            title: "移动到...",
            width: 350,
            content: '<div style="overflow-x: hidden; overflow-y: auto; width: 340px;height:250px;"><ul id="movetree"></ul></div>',
            init: function () {
                $('#movetree').commonTree({
                    loadTreesAction: "access/queryFunctions.ajax",
                    IsShowMenu: false
                });
            },
            ok: function() {
            	doMovePermission.call(window, ids);
            },
            close: function () {
                this.hide();
                return false;
            }
        });
    } else {
        $('#movetree').commonTree('refresh');
        selectDialog.show().zindex();
    }
}

function doMovePermission(ids) {
    var newParentId = $('#movetree').commonTree('getSelectedId');
    if (!newParentId) {
        Public.tip('请选择移动到的节点。');
        return false;
    }
    var params = {};
    params.parentId = newParentId;
    params.ids = $.toJSON(ids);
    Public.ajax(web_app.name + "/access/movePermissions.ajax", params, function (data) {
        reloadGrid();
        selectDialog.hide();
    });
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function showRoleList(obj){
	var id=$(obj).attr('id'),name=$(obj).attr('name');
	UICtrl.showFrameDialog({
		url: web_app.name + "/access/forwardPermissionRoleAndPerson.do",
		param: {permissionId: id},
		title: name,
		width: getDefaultDialogWidth(),
		height: getDefaultDialogHeight(),
		cancelVal: '关闭',
		ok: false,
		cancel:true
	});
}