var gridManager, refreshFlag, operateCfg = {};

var pageParam = {rootId: 1, rootParentId: 0, parentId: 0};

$(function () {
    initializeOperateCfg();
    initializeGrid();
    loadBizManagementTypeTree();
    initializeUI();

    function initializeOperateCfg() {
        var path = '/management/';
        operateCfg.queryTreeAction = path + 'queryBizManagementTypes.ajax';
        operateCfg.queryAction = path + 'slicedQueryBizManagementTypes.ajax';
        operateCfg.showInsertAction = path + 'showBizManagementTypeDetail.load';
        operateCfg.showUpdateAction = path + 'loadBizManagementType.load';
        operateCfg.insertAction = path + 'insertBizManagementType.ajax';
        operateCfg.updateAction = path + 'updateBizManagementType.ajax';
        operateCfg.deleteAction = path + 'deleteBizManagementType.ajax';
        operateCfg.updateSequenceAction = path + 'updateBizManagementTypeSequence.ajax';
        operateCfg.moveAction = path + 'moveBizManagementType.ajax';

        operateCfg.insertTitle = "添加业务管理权限类别";
        operateCfg.updateTitle = "修改业务管理权限类别";
    }

    function initializeGrid() {
        var toolbarparam = {
            addHandler: showInsertDialog,
            updateHandler: showUpdateDialog,
            deleteHandler: deleteBizManagementType,
            saveSortIDHandler: updateBizManagementTypeSequence,
            moveHandler: moveHandler,
            permissionBizManagementQuery:{id:'permissionBizManagementQuery',text:'查询业务权限授权',img:'fa-link',click:function(){
            	var url=DataUtil.composeURLByParam('management/forwardPermissionBizManagementQuery.do');
            	UICtrl.addTabItem({tabid:'permissionBizManagementQuery', text:'查询业务权限授权',url:url}); 
    		}}
        };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
                {display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
                {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
                {
                    display: "类别", name: "kindId", width: 100, minWidth: 60, type: "string", align: "left",
                    render: function (item) {
                        return item.kindId == 0 ? '系统' : '自定义';
                    }
                },
                {
                    display: "节点类别", name: "nodeKindId", width: 100, minWidth: 60, type: "string", align: "left",
                    render: function (item) {
                        return item.nodeKindId == CommonNodeKind.Limb ? '分类' : '权限类别';
                    }
                },
                {
                    display: "排序号", name: "sequence", width: "60", minWidth: 60, type: "string", align: "left",
                    render: function (item) {
                        return "<input type='text' id='txtSequence_"
                            + item.id + "' class='textbox' value='"
                            + item.sequence + "' />";
                    }
                },
                {display: "备注", name: "remark", width: 200, minWidth: 60, type: "string", align: "left"},
                {
                    display: "授权查看", name: "view", width: 100, minWidth: 60, type: "string", align: "center",isSort:false,
                    render: function (item) {
                    	if(item.nodeKindId == CommonNodeKind.Limb){
                    		return '';
                    	}else{
                    		var html = ['<a href="javascript:void(null);" class="view" '];
					        html.push('id="', item.id, '" ');
					        html.push('name="', item.name, '" ');
					        html.push('>');
					        html.push('授权查看');
					        html.push('</a>');
					        return html.join('');
                    	}
                    }
                }
            ],
            dataAction: "server",
            url: web_app.name + operateCfg.queryAction,
            parms: { parentId: pageParam.rootId },
            pageSize: 20,
            sortName: 'sequence',
            sortOrder: 'asc',
            toolbar: toolbarOptions,
            width: "100%",
            height: "100%",
            heightDiff: -8,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                doShowUpdateDialog(data.id);
            }
        });
        UICtrl.setSearchAreaToggle(gridManager);
        $('#maingrid').on('click',function(e){
    		var $clicked = $(e.target || e.srcElement);
    		if($clicked.is('a.view')){
    			var id = $clicked.attr('id');
    	        var name = $clicked.attr('name');
    	        viewManagementType(id, name);
    			return false;
    		}
    	});
    }

    function initializeUI() {
    	UICtrl.initDefaultLayout();
    }

    function loadBizManagementTypeTree() {
        $('#maintree').commonTree({
            loadTreesAction: "management/queryBizManagementTypes.ajax",
            parentId: pageParam.rootParentId,
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
});

function onTreeNodeClick(data) {
    var html = [];
    if (data.id == pageParam.rootId) {
        html.push('业务管理权限类别列表');
    } else {
        html.push('<span class="tomato-color">[', data.name, ']</span>业务管理权限类别列表');
    }
    pageParam.parentId = data.id;
    $("#layout").layout("setCenterTitle", html.join(''));
    if (gridManager) {
        UICtrl.gridSearch(gridManager, {parentId: pageParam.parentId});
    }
}

function getId() {
    return $("#id").val();
}

function showInsertDialog() {
    if (pageParam.parentId == pageParam.rootParentId) {
        Public.tip('请选择类别树。');
        return;
    }

    UICtrl.showAjaxDialog({
        url: web_app.name + operateCfg.showInsertAction,
        title: operateCfg.insertTitle,
        param: {parentId: pageParam.parentId},
        width: 340,
        ok: doSaveBizManagementType,
        close: onDialogCloseHandler
    });
}

function doShowUpdateDialog(id) {
    UICtrl.showAjaxDialog({
        url: web_app.name + operateCfg.showUpdateAction,
        param: { id: id },
        title: operateCfg.updateTitle,
        width: 340,
        ok: doSaveBizManagementType,
        close: onDialogCloseHandler
    });
}

function showUpdateDialog() {
	var id = DataUtil.getUpdateRowId(gridManager);
	if (!id){ return; }
    doShowUpdateDialog(id);
}

function doSaveBizManagementType() {
    var _self = this;
    var id = getId();

    $('#submitForm').ajaxSubmit({
        url: web_app.name + (id ? operateCfg.updateAction : operateCfg.insertAction),
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function deleteBizManagementType() {
    DataUtil.del({action: operateCfg.deleteAction, gridManager: gridManager, onSuccess: reloadGrid});
}

function updateBizManagementTypeSequence() {
    DataUtil.updateSequence({action: operateCfg.updateSequenceAction, gridManager: gridManager, onSuccess: reloadGrid});
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function doMoveBizManagementType(ids) {
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

    if (newParentId == pageParam.parentId) {
        Public.errorTip('您选择的目标节点和源节点一样，不能移动。');
        return false;
    }

    var params = {};
    params.parentId = newParentId;
    params.ids = $.toJSON(ids);

    Public.ajax(web_app.name + operateCfg.moveAction, params, function (data) {
        reloadGrid();
    });
    return true;
}

function reloadGrid() {
    $("#maintree").commonTree('refresh');
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

//移动
function moveHandler() {
    var ids = DataUtil.getSelectedIds({
        gridManager: gridManager,
        idFieldName: "id"
    });

    if (!ids) {
        Public.errorTip("请选择要移动的业务管理权限类别。");
        return;
    }

    //if (!selectMoveDialog) {
        //selectMoveDialog = 
        UICtrl.showDialog({
            title: "移动到...",
            width: 350,
            content: '<div style="overflow-x: hidden; overflow-y: auto; width: 340px;height:250px;"><ul id="movetree"></ul></div>',
            init: function () {
                $('#movetree').commonTree({
                    loadTreesAction: "management/queryBizManagementTypes.ajax",
                    idFieldName: 'id',
                    IsShowMenu: false,
                    dataRender: function (data) {
                        return data;
                    },
                });
            },
            ok: function () {
               return doMoveBizManagementType.call(this, ids);
            }
        });
    //} else {
    //    $('#movetree').commonTree('refresh');
    //    selectMoveDialog.show().zindex();
    //}
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}

function viewManagementType(id,name){
	 UICtrl.showAjaxDialog({
         title: "查询授权["+name+"]",
         url: web_app.name +'/management/forwardQueryManagementByTypeId.load',
         param:{parentId:id},
         width: getDefaultDialogWidth(),
         height: getDefaultDialogHeight(),
         init: function (div) {
        	 initializeManagementGrid(div,id);
         },
         ok:false
     });
}

function initializeManagementGrid(div) {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		deleteOrg:{ id: "deleteOrg", text: "删除", img:  "fa-trash-o" , click: function(){
			DataUtil.del({ action: '/management/deleteBizManagement.ajax', gridManager: managementGridManager, onSuccess: function(){
				managementGridManager.loadData();
			}});
		}},
		romoveCache:{ id: "romoveCache", text: "清除权限缓存", img: "fa-files-o", click: function(){
			Public.ajax(web_app.name + '/management/removePermissionCache.ajax');
	    }},
		exportExcelHandler: function(){
        	UICtrl.gridExport(managementGridManager);
        }
	});  
	
	managementGridManager=UICtrl.grid($('div.grid',div), {
		columns: [
		{ display: "管理者", name: "managerName", width: 120, minWidth: 60, type: "string", align: "left" },		   
		{ display: "管理者路径", name: "managerFullName", width: 250, minWidth: 60, type: "string", align: "left" },		   
		{ display: "管理者状态", name: "managerStatus", width: 80, minWidth: 60, type: "string", align: "left",
			render: function (item){
		   	   	return OpmUtil.getOrgStatusDisplay(item.managerStatus) ; 
		   	} 
		},		   
		{ display: "被管理者", name: "subName", width: 150, minWidth: 60, type: "string", align: "left" },
		{ display: "被管理者路径", name: "subFullName", width: 220, minWidth: 60, type: "string", align: "left" },
		{ display: "创建人", name: "createdByName", width: 100, minWidth: 60, type: "string", align: "left" },
		{ display: "创建时间", name: "createdDate", width: 120, minWidth: 60, type: "date", align: "left" }
		],
		dataAction: 'server',
		url: web_app.name+'/management/slicedQueryManagementByTypeId.ajax',
		parms:{parentId:$('#typeParentId').val()},
		pageSize: 20,
		width: '100%',
		height: getDefaultDialogHeight()-90,
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true
	});
	
	$("#queryMainBtn",div).click(function() {
		var params = $(this.form).formToJSON();
		UICtrl.gridSearch(managementGridManager, params);
	});
	$("#resetMainBtn").click(function() {
		$(this.form).formClean();
	});
}