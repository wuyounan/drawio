var treeManager, gridManager, refreshFlag = false, selectFunctionDialog, lastSelectedId = 0;

$(function() {
	bindEvents();

	loadFunctionTree();
	initializeUI();
	initializeGrid();

	function initializeUI() {
		UICtrl.layout("#layout", { leftWidth: 3 });
	}

	function bindEvents() {
		$("#name").live("blur", function() {
			if (!$("#fullName").val()) {
				$("#fullName").val($("#name").val());
			}			
		});
	}

	function initializeGrid() {
		var toolbarparam = {
			addHandler: showInsertDialog,
			updateHandler: showUpdateDialog,
			deleteHandler: deleteFunction,
			saveSortIDHandler: updateFunctionSequence,
			enableHandler: enableHandler,
			disableHandler: disableHandler,
			moveHandler:moveHandler,
			buildPermission: { id:'buildPermission',text:'生成权限',img:'fa-cubes',click:buildPermission }
		};
		var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

		gridManager = UICtrl.grid("#maingrid", {
					columns: [
					          { display: '图标', name: 'icon', width: 60, minWidth: 60, type: "string", align: "center", isAutoWidth: 0,
									render: function(item) {
										if (!Public.isBlank(item.icon)) {
											return '<span style="font-size:16px;"><i class="fa '+item.icon+'"></i></span>';
										}
										return '';
									}
					          },
					          { display: '编码', name: 'code', width: 200, minWidth: 60, type: "string", align: "left" }, 
					          { display: '名称', name: 'name', width: 150, minWidth: 60, type: "string", align: "left" }, 
					          { display: '描述', name: 'description', width: 150, minWidth: 60, type: "string", align: "left" },
					          { display: '状态', name: 'status', width: 60, minWidth: 60, type: "int", align: "left",
									render: function(item) {
										return UICtrl.getStatusInfo(item.status);
									}
					          },
					          { display: '全名称', name: 'fullName', width: 150, minWidth: 60, type: "string", align: "left" },
					          { display: 'Url', name: 'url', width: 280, minWidth: 60, type: "string", align: "left", hide: 0 },
					          { display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "int", align: "left",
									render: function(item) {
										return "<input type='text' id='txtSequence_"+ item.id+ "' class='textbox' value='"+ item.sequence + "' />";
									}
							  }
							],
					dataAction: 'server',
					url: web_app.name+ "/sysFunction/queryFunctions.ajax",
					parms: {parentId: 0},
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
					onDblClickRow: function(data, rowindex, rowobj) {
						doShowUpdateDialog(data.id);
					}
				});

		UICtrl.setSearchAreaToggle(gridManager);
	}

	function loadFunctionTree() {
		$('#maintree').commonTree({
			loadTreesAction: "sysFunction/queryFunctions.ajax",
			isLeaf: function(data) {
				return data.hasChildren == 0;
			},
			onClick: function(data) {
				if (data && lastSelectedId != data.id) {
					reloadGrid3(data.id, data.name);
				}
			},
			IsShowMenu: false
		});
	}
});

function getId() {
	return $("#id").val();
}
//查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

//重置表单
function resetForm(obj) {
	$(obj).formClean();
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
	$('#layout').layout('setCenterTitle',"<span class=\"tomato-color\">[" + name+ "]</span>子功能列表");
	lastSelectedId = id;
	var params = $("#queryMainForm").formToJSON();
	params.parentId = id;
	UICtrl.gridSearch(gridManager, params);
}

function reloadGrid4() {
	$("#maintree").commonTree('reload');
	var params = $("#queryMainForm").formToJSON();
	UICtrl.gridSearch(gridManager, params);
}

function showInsertDialog() {
	if (!$('#maintree').commonTree('getSelectedId')) {
		Public.tip('请选择左侧父功能!');
		return;
	}
	UICtrl.showAjaxDialog({
		title: "添加功能",
		param: {
			parentId: $('#maintree').commonTree('getSelectedId')
		},
		width: 400,
		url: web_app.name+ '/sysFunction/showFunctionDetail.load',
		ok: doSaveFunction,
		close: onDialogCloseHandler,
		init:initDetail
	});
}

function doShowUpdateDialog(id) {
	UICtrl.showAjaxDialog({
		url: web_app.name + '/sysFunction/loadFunction.load',
		param: { id: id },
		title: "修改功能",
		width: 400,
		ok: doSaveFunction,
		close: onDialogCloseHandler,
		init:initDetail
	});
}

function initDetail(div){
	var _icon=$('input[name="icon"]',div);
	var _showIcon=function(icon){
		var span=$('span.show-icon',div).empty();
		if (!Public.isBlank(icon)) {
			span.append('<i class="fa '+icon+'"></i>');
		}
	};
	_icon.on('blur',function(){
		_showIcon($(this).val());
	});
	_showIcon(_icon.val());
}

function showUpdateDialog() {
	var id = DataUtil.getUpdateRowId(gridManager);
	if (!id){ return; }
	
	doShowUpdateDialog(id);
}

function doSaveFunction() {
	var _self = this;
	var id = getId();
	$('#submitForm').ajaxSubmit({
		url: web_app.name+ (id ? '/sysFunction/updateFunction.ajax': '/sysFunction/insertFunction.ajax'),
		success: function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

function deleteFunction() {
	var action = "sysFunction/deleteFunctions.ajax";
	DataUtil.del({
				action: action,
				gridManager: gridManager,
				onSuccess: reloadGrid
			});
}

function updateFunctionSequence() {
	var action = "sysFunction/updateFunctionsSequence.ajax";
	DataUtil.updateSequence({
				action: action,
				gridManager: gridManager,
				onSuccess: reloadGrid
			});
}

/**
 * 移动
 */
function moveHandler() {
	var excludeIds=DataUtil.getSelectedIds({
		gridManager: gridManager
	});
	if (!excludeIds || excludeIds.length < 1) {
		Public.tip('请选择数据！');
		return;
	}
	UICtrl.showDialog({title:'移动到...',width:300,
		content:'<div style="overflow-x: hidden; overflow-y: auto; width:280px;height:250px;"><ul class="move-tree"></ul></div>',
		init:function(doc){
			$('ul.move-tree',doc).commonTree({
		        loadTreesAction: 'sysFunction/queryFunctions.ajax',
		        getParam:function(){
		        	//排除当前选中节点
		        	return {excludeIds:excludeIds.join(',')};
		        },
		        IsShowMenu: false
		    });
		},
		ok:function(doc){
			var parentId=$('ul.move-tree',doc).commonTree('getSelectedId');
			if(!parentId){
				Public.tip('请选择树节点！');
				return false;
			}
			var _self=this;
			DataUtil.updateById({action:'sysFunction/moveFunctions.ajax',
				gridManager:gridManager,idFieldName:'id',param:{parentId:parentId},
				onSuccess:function(){
					reloadGrid4();
					_self.close();
				}
			});
		},
		close: function (doc) {
			$('ul.move-tree',doc).removeAllNode();
		    return true;
		}
	});
}


function onDialogCloseHandler() {
	if (refreshFlag) {
		reloadGrid();
		refreshFlag = false;
	}
}

function updateStatus(message, status){
	DataUtil.updateById({ action: 'sysFunction/updateSysFunctionsStatus.ajax',
		gridManager: gridManager,  param:{status: status},
		message: message,
		onSuccess:function(){
			reloadGrid();	
		}
	});	
}

function enableHandler(){
	updateStatus('确实要启用选中数据吗?', 1);
}

//禁用
function disableHandler(){
	updateStatus('确实要禁用选中数据吗?', 0);
}


function buildPermission(){
	var rows = gridManager.getSelectedRows();
	if (rows.length != 1){
		Public.errorTip("请选择一条数据。");
		return false;
	}
	var url = web_app.name + '/sysFunction/buildPermission.ajax';
    Public.ajax(url, { fullId: rows[0].fullId}, function (data) {});
}