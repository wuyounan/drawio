var gridManager = null, manageTypeKindGridManager =null,lastSelectedId = 0, refreshFlag = false;
$(document).ready(function () {
    initializeUI();
    initializeGrid();
});

//初始化Tree
function initializeUI() {
    UICtrl.initDefaultLayout();
    $('#maintree').commonTree({
        loadTreesAction: 'dataManageType/queryDatamanagetypekind.ajax',
        parentId: "",
        IsShowMenu: false,
        onClick: function (data) {
            if (data && lastSelectedId != data.id) {
                onFolderTreeNodeClick(data.id, data.name, data.fullId);
            }
        }
    });
}

//点击树节点时加载表格
function onFolderTreeNodeClick(id, name, fullId) {
    var html = "<font class='tomato-color'>[" + name + "]</font>数据管理权限列表";
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
            {display: "权限类别", name: "nodeKindIdTextView", width: 60, minWidth: 110, type: "string", align: "left"},
            {
                display: "序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return UICtrl.sequenceRender(item);
                }
            },
            {display: "路径", name: "fullName", width:400, minWidth: 60, type: "string", align: "left"}
        ],
        dataAction: 'server',
        url: web_app.name + "/dataManageType/slicedQueryOpdatamanagetype.ajax",
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
        title: "添加数据管理权限",
        width: 700,
        url: web_app.name + '/dataManageType/showInsertOpdatamanagetype.load',
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
        title: "修改数据管理权限",
        width: 700,
        url: web_app.name + '/dataManageType/showLoadOpdatamanagetype.load',
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
        url: web_app.name + '/dataManageType/insertOpdatamanagetype.ajax',
        success: function (data) {
        	if (Public.isBlank(id)) {
        		$('#detailPageId').val(data);
        		manageTypeKindGridManager.options.parms['parentId']=data;
        	}
            $('#maintree').commonTree("refresh", $('#detailPageParentId').val());
            refreshFlag = true;
        }
    });
}

//编辑保存
function update() {
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/dataManageType/updateOpdatamanagetype.ajax',
        success: function () {
            $('#maintree').commonTree("refresh", $('#detailPageParentId').val());
            refreshFlag = true;
        }
    });
}

function initDetailDialog(div){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: function(){},
		deleteHandler: function(){
			DataUtil.delSelectedRows({action:'dataManageType/deleteDatamanagetypekinds.ajax',
				gridManager:manageTypeKindGridManager,idFieldName:'id',
				onSuccess:function(){
					manageTypeKindGridManager.loadData();
				}
			});
		}
	});
	manageTypeKindGridManager=UICtrl.grid('#manageTypeKindGrid', {
		columns: [
		     {display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
		     {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
		     {display: "数据类型", name: "dataKindTextView", width: 100, minWidth: 60, type: "string", align: "left"}
		],
		dataAction: 'server',
		url: web_app.name+'/dataManageType/queryOpdatamanagetypekind.ajax',
		parms:{parentId: $('#detailPageId').val()},
		width: '100%', 
		height: '260',
		sortName:'sequence',
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
		onSuccess:function(data){
			var hasChildren=$('#hasChildren',div).val();
			if(hasChildren==0){
				var total=parseInt(data['Total']||0,10);
				total=isNaN(total)?0:total;
				if(total > 0){
					UICtrl.setDisable('#nodeKindIdDiv');
				}else{
					UICtrl.setEditable('#nodeKindIdDiv');
				}
			}
		}
	});
	setTimeout(function(){
		initNodeKindDiv(div);
	},1);
}

function initNodeKindDiv(div){
	var hasChildren=$('#hasChildren',div).val();
	if(hasChildren==0){
		initChooseDialog(div);
		$('#nodeKindIdDiv').on('click',function(e){
			var $clicked = $(e.target || e.srcElement);
			if($clicked.is(':radio')){
				setTimeout(function(){
					manageTypeKindGridToggle();
				},0);
			}
		});
	}else{
		UICtrl.setDisable('#nodeKindIdDiv');
	}
	manageTypeKindGridToggle();
}

function manageTypeKindGridToggle(){
	var param = $('#submitForm').formToJSON({check:false});
	var nodeKindId=param['nodeKindId'];
	$('#manageTypeKindGridShowDiv')[nodeKindId==1?'addClass':'removeClass']('ui-hide');
}

function initChooseDialog(div){ 
	$('#toolbar_menuAdd',div).comboDialog({
		type:'sys',name:'opDataKind',checkbox:true,
		dataIndex:'id',parent:window['ajaxDialog'],  
		onShow:function(){
			if (Public.isBlank($('#detailPageId').val())) {
				insert();
				return false;
			}
			return true;
		},
		onChoose: function(dialog){
	    	var rows = this.getSelectedRows();
	    	if(!rows.length){
	    		Public.errorTip('请选择资源类型。');
	    		return;
	    	}
	    	var kindIds=$.map(rows,function(o){
	    		return o.id;
	    	});
	    	Public.ajax(web_app.name+'/dataManageType/saveDatamanagetypekinds.ajax',
	    		{id: $('#detailPageId').val(), kindIds: $.toJSON(kindIds)},
	    		function(){
	    			manageTypeKindGridManager.loadData();
	    			dialog.close();
	    		}
	    	);
	    	return false;
		}
	});
}

//删除按钮
function deleteHandler(id) {
  DataUtil.del({
      action: 'dataManageType/deleteOpdatamanagetype.ajax',
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
				loadTreesAction: 'dataManageType/queryDatamanagetypekind.ajax',
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
			DataUtil.updateById({action:'dataManageType/moveOpdatamanagetype.ajax',
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
	var action = "dataManageType/updateOpdatamanagetypeSequence.ajax";
    DataUtil.updateSequence({
    	action: action, gridManager: gridManager,
    	onSuccess: function () {
    		reloadGrid();
        }
    });
    return false;
}