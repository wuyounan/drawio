var gridManager = null, manageTypeKindGridManager =null,lastSelectedId = 0, refreshFlag = false;
$(document).ready(function () {
    initializeUI();
    initializeGrid();
});

//初始化Tree
function initializeUI() {
    UICtrl.initDefaultLayout();
    $('#maintree').commonTree({
        loadTreesAction: 'dataManageBusiness/queryDatamanagebusiness.ajax',
        parentId: "",
        IsShowMenu: false,
        onClick: function (data) {
            if (data && lastSelectedId != data.id) {
                onFolderTreeNodeClick(data.id, data.name, data.fullId);
            }
        }
    });
    $('#queryDataManageName').treebox({
		name:'dataManageTypeTreeView',
		searchName:'dataManageType',
		searchType:'sys',
		hasSearch :true,
		minWidth:250,
		back:{text:$('#queryDataManageName')},
		onChange:function(node,data){
			$('#queryDataManageName').val(data.name);
			$('#dataManageFullId').val(data.fullId);
		}
	});
    
}

//点击树节点时加载表格
function onFolderTreeNodeClick(id, name, fullId) {
    var html = "<font class='tomato-color'>[" + name + "]</font>数据管理权限业务类型列表";
    $('#layout').layout('setCenterTitle', html);
    lastSelectedId = id;
    var params = $("#queryMainForm").formToJSON();
    params['fullId'] = fullId;
    UICtrl.gridSearch(gridManager, params);
}

//初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: addHandler,
        updateHandler: function () {
            updateHandler();
        },
        deleteHandler: deleteHandler,
        moveHandler:moveHandler,
        saveSortIDHandler: saveSortIDHandler
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {display: "编码", name: "code", width: 250, minWidth: 60, type: "string", align: "left"},
            {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "管理权限编码", name: "manageCode", width: 160, minWidth: 110, type: "string", align: "left"},
            {display: "管理权限名称", name: "manageName", width: 160, minWidth: 110, type: "string", align: "left"},
            {
                display: "序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return UICtrl.sequenceRender(item);
                }
            },
            {display: "路径", name: "fullName", width:400, minWidth: 60, type: "string", align: "left"}
        ],
        dataAction: 'server',
        url: web_app.name + "/dataManageBusiness/slicedQueryOpdatamanagebusiness.ajax",
        pageSize: 20,
        width: '100%',
        height: '100%',
        heightDiff: -8,
        sortName:'fullId',
		sortOrder:'asc',
        toolbar: toolbarOptions,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {
            updateHandler(data.id);
        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
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

//重置表单
function resetForm(obj) {
    $(obj).formClean();
}

function addHandler() {
    var node = $('#maintree').commonTree('getSelected');
    if (!node) {
        Public.tip('请选择左侧父节点!');
        return;
    }
    if (node.nodeKindId != 1) {
        Public.tip('数据权限类型下不能再添加子节点');
        return;
    }
    UICtrl.showAjaxDialog({
        title: "添加业务类型",
        width: 700,
        url: web_app.name + '/dataManageBusiness/showInsertOpdatamanagebusiness.load',
        param: {parentId: node.id},
        init:initDetailDialog,
        ok: insert,
        close: dialogClose
    });
}

//编辑修改
function updateHandler(id) {
	if (!id) {
		var id = DataUtil.getUpdateRowId(gridManager);
	    if (!id) {
	    	return;
	    }
	}
    UICtrl.showAjaxDialog({
        title: "修改业务类型",
        width: 700,
        url: web_app.name + '/dataManageBusiness/showLoadOpdatamanagebusiness.load',
        param: {id: id},
        init:initDetailDialog,
        ok: update,
        close: dialogClose
    });
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}

//新增保存
function insert() {
	var id=$('#detailPageId').val();
	if (Public.isNotBlank(id)) {
		update();
		return;
	}
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/dataManageBusiness/insertOpdatamanagebusiness.ajax',
        success: function (data) {
        	if (Public.isBlank(id)) {
        		$('#detailPageId').val(data);
        		window['fieldGridManager0'].options.parms['parentId']=data;
        		window['fieldGridManager1'].options.parms['parentId']=data;
        	}
            $('#maintree').commonTree("refresh", $('#detailPageParentId').val());
            refreshFlag = true;
        }
    });
}

//编辑保存
function update() {
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/dataManageBusiness/updateOpdatamanagebusiness.ajax',
        success: function () {
            $('#maintree').commonTree("refresh", $('#detailPageParentId').val());
            refreshFlag = true;
        }
    });
}

//删除按钮
function deleteHandler(id) {
  DataUtil.del({
      action: 'dataManageBusiness/deleteOpdatamanagebusiness.ajax',
      gridManager: gridManager, idFieldName: 'id',
      onSuccess: function () {
          reloadGrid();
          $('#maintree').commonTree("reload");
      }
  });
}

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
				loadTreesAction: 'dataManageBusiness/queryDatamanagebusiness.ajax',
				parentId:'0',
		        getParam:function(){
		        	//排除当前选中节点
		        	return {excludeIds:excludeIds.join(',')};
		        },
		        IsShowMenu: false
		    });
		},
		ok:function(doc){
			var node = $('ul.move-tree',doc).commonTree('getSelected');
		    if (!node) {
		        Public.tip('请选择树节点!');
		        return;
		    }
		    if (node.nodeKindId != 1) {
		        Public.tip('数据权限类型下不能再添加子节点');
		        return;
		    }
			var parentId=node.id,_self=this;
			DataUtil.updateById({action:'dataManageBusiness/moveOpdatamanagebusiness.ajax',
				gridManager:gridManager,idFieldName:'id',param:{parentId:parentId},
				onSuccess:function(){
					reloadGrid();
			        $('#maintree').commonTree("reload");
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

//保存扩展字段排序号
function saveSortIDHandler() {
	var action = "dataManageBusiness/updateOpdatamanagebusinessSequence.ajax";
    DataUtil.updateSequence({
    	action: action, gridManager: gridManager,
    	onSuccess: function () {
    		reloadGrid();
        }
    });
    return false;
}

function initDetailDialog(div){
	$('#opdatamanagebusinessFieldTabDiv').tab();
	$('#dataManageName',div).treebox({
		name:'dataManageTypeTreeView',
		searchName:'dataManageType',
		searchType:'sys',
		hasSearch :true,
		minWidth:250,
		back:{text:$('#dataManageName',div)},
		beforeChange:function(data){
			return data.nodeKindId==2;
		},
		onChange:function(node,data){
			$('#dataManageName',div).val(data.name);
			$('#dataManageId',div).val(data.id);
		}
	});
	initOpdatamanagebusinessFieldGrid(0);
	initOpdatamanagebusinessFieldGrid(1);
	initNodeKindDiv(div);
}

function initNodeKindDiv(div){
	var hasChildren=$('#hasChildren',div).val();
	if(hasChildren==0){
		$('.hg-form-list',div).on('click',function(e){
			var $clicked = $(e.target || e.srcElement);
			if($clicked.is(':radio')){
				setTimeout(function(){
					fieldGridToggle();
				},0);
			}
		});
	}else{
		$('input[type="radio"]',div).each(function(i,o){
			$(this).attr('disabled',true);
		});
	}
	fieldGridToggle();
}

function fieldGridToggle(){
	var param = $('#submitForm').formToJSON({check:false});
	var nodeKindId=param['nodeKindId'];
	if(nodeKindId==1){
		$('#opdatamanagebusinessFieldShowDiv').addClass('ui-hide');
		UICtrl.disable($('#dataManageName'));
	}else{
		$('#opdatamanagebusinessFieldShowDiv').removeClass('ui-hide');
		UICtrl.enable($('#dataManageName'));
	}
}

function initOpdatamanagebusinessFieldGrid(isOrgCondition){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: function(){
			showInsertOpdatamanagetypeField(isOrgCondition);
		},
		updateHandler: function(){
			var id = DataUtil.getUpdateRowId(window['fieldGridManager'+isOrgCondition]);
		    if (!id) {
		    	return;
		    }
			showLoadOpdatamanagetypeField(id);
		},
		deleteHandler: function(){
			DataUtil.delSelectedRows({action:'dataManageBusiness/deleteOpdatamanagebusinessField.ajax',
				gridManager:window['fieldGridManager'+isOrgCondition],
				onSuccess:function(){
					window['fieldGridManager'+isOrgCondition].loadData();
				}
			});
		},
		romoveCache:{ id: "romoveCache", text: "清除权限缓存", img: "fa-files-o" , click: function(){
	        Public.ajax(web_app.name + '/management/removePermissionCache.ajax');
	    }}
	});
	var columns=[];
	columns.push({display: "资源编码", name: "dataKindCode", width: 100, minWidth: 60, type: "string", align: "left"});
	if(isOrgCondition==0){
		columns.push({display: "资源名称", name: "dataKindName", width: 100, minWidth: 60, type: "string", align: "left"});
		columns.push({display: "资源类型", name: "dataKindIdTextView", width: 100, minWidth: 60, type: "string", align: "left"});
	}else{
		columns.push({display: "资源类型", name: "dataKindTextView", width: 100, minWidth: 60, type: "string", align: "left"});
	}
	columns.push({display: "字段名", name: "tableColumn", width: 100, minWidth: 60, type: "string", align: "left"});
	columns.push({display: "数据库别名", name: "tableAlias", width: 100, minWidth: 60, type: "string", align: "left"});
	columns.push({display: "数据类型", name: "columnDataTypeTextView", width: 100, minWidth: 60, type: "string", align: "left"});
	columns.push({display: "操作符", name: "columnSymbolTextView", width: 100, minWidth: 60, type: "string", align: "left"});
	columns.push({display: "拼接模板", name: "formula", width: 250, minWidth: 60, type: "string", align: "left"});
	var gridId=isOrgCondition==0?'opdatamanagebusinessFieldGrid':'opdatamanagebusinessOrgFieldGrid';
	window['fieldGridManager'+isOrgCondition]=UICtrl.grid('#'+gridId, {
		columns: columns,
		dataAction: 'server',
		url: web_app.name+'/dataManageBusiness/queryOpdatamanagebusinessField.ajax',
		parms:{parentId: $('#detailPageId').val(),isOrgCondition:isOrgCondition},
		width: '100%', 
		height: '250',
		sortName:'kindSequence',
		sortOrder:'asc',
		heightDiff: -8,
		toolbar: toolbarOptions,
		usePager: false,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onLoadData: function(){
			return $('#detailPageId').val()!='';
		},
		onDblClickRow: function (data, rowindex, rowobj) {
			showLoadOpdatamanagetypeField(data.id);
        }
	});
}

function showInsertOpdatamanagetypeField(isOrgCondition){
	var id=$('#detailPageId').val();
	if (Public.isBlank(id)) {
		insert();
		return;
	}
	UICtrl.showAjaxDialog({
		 id:'opdatamanageField',
		 parent:window['ajaxDialog'],
	     title: "添加权限字段",
	     width: 650,
	     url: web_app.name + '/dataManageBusiness/showInsertOpdatamanagebusinessField.load',
	     init:initOpdatamanagetypeField,
	     param: {datamanagebusinessId:$('#detailPageId').val(),isOrgCondition:isOrgCondition},
	     ok: saveOpdatamanagetypeField
	 });
}

function showLoadOpdatamanagetypeField(id){
    UICtrl.showAjaxDialog({
    	id:'opdatamanageField',
		parent:window['ajaxDialog'],
        title: "修改数据管理权限",
        width: 650,
        url: web_app.name + '/dataManageBusiness/showLoadOpdatamanagebusinessField.load',
        init:initOpdatamanagetypeField,
        param: {id: id},
        ok: saveOpdatamanagetypeField
    });
}

function initOpdatamanagetypeField(div){
	$('#dataKindName',div).searchbox({
        type: 'sys',
        name: 'opDataKind',
        back: {
            code: $('#dataKindCode',div),
            name: $('#dataKindName',div)
        }
    });
}

function saveOpdatamanagetypeField(){
	var _self=this;
	$('#opdatamanageFieldForm').ajaxSubmit({url: web_app.name + '/dataManageBusiness/saveOpdatamanagebusinessField.ajax',
		success: function() {
			window['fieldGridManager'+$('#pageIsOrgCondition').val()].loadData();
			_self.close();
		}
	});
}
