//定义页面变量
var treeManager = null, gridManager = null, refreshFlag = false, lastSelectedId = 0, bizClassificationDetailGridManager = null;

//页面初始化事件
$(document).ready(function () {
    initializeUI();
    initializeTree();
    initializeGrid();
});
//初始化UI
function initializeUI() {
    UICtrl.initDefaultLayout();
}

//初始化Tree
function initializeTree() {
    $('#maintree').commonTree({
        loadTreesAction: 'bizClassification/queryBizClassifications.ajax',
        idFieldName: 'id',
        onClick: function (data) {
            if (data && lastSelectedId != data.id) {
                onFolderTreeNodeClick(data.id, data.name);
            }
        },
        IsShowMenu: false
    });
}

//点击树节点时加载表格
function onFolderTreeNodeClick(id, name) {
	$("#layout").layout("setCenterTitle", "<span class='tomato-color'>[" + name + "]</span>业务分类配置列表");
    lastSelectedId = id;
    var params = $("#queryMainForm").formToJSON();
    params.parentId = id;
    UICtrl.gridSearch(gridManager, params);
}

// 初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: showInsertDialog,
        updateHandler: showUpdateDialog,
        deleteHandler: deleteBizClassification,
        saveSortIDHandler: updateBizClassificationSequence,
        enableHandler: enableHandler,
        disableHandler: disableHandler,
        moveHandler:moveHandler,
        buildPermission: { id:'buildPermission',text:'生成权限',img:'fa-link',click:buildPermission }
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
            {
                display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return UICtrl.getStatusInfo(item.status);
                }
            },
            {display: "描述", name: "description", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "路径", name: "fullName", width: 300, minWidth: 60, type: "string", align: "left"},
            {
            	display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return UICtrl.sequenceRender(item);
                }
            }],
        dataAction: 'server',
        url: web_app.name + '/bizClassification/sliceQueryBizClassifications.ajax',
        parms: {
            parentId: 1
        },
        usePager: true,
        sortName: "sequence",
        sortOrder: "asc",
        toolbar: toolbarOptions,
        width: '99.8%',
        height: '100%',
        heightDiff: -11,
        checkbox: true,
        toolbar: toolbarOptions,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {
            doShowUpdateDialog(data.id);
        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

//获取ID
function getId() {
    return $("#id").val() || "";
}

//初始化新增、修改对话框
function initShowDialog() {
    var bizClassificationId = getId();
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addByBizClassGroupHandler: {id: 'addBizFieldSysGroup', text: '添加业务字段分组',  img: 'fa-plus'},
        deleteHandler: deleteBizClassificationDetail
    });
    bizClassificationDetailGridManager = UICtrl.grid('#classificationDetalGrid', {
        columns: [
            {display: "预览", name: "view", width: 40, minWidth:40, type: "string", align: "center", 
            	render: function (item) {
                return '<a href="javascript:void(0);" class="GridStyle" onClick="previewGroup(\''+item.bizCode+'\',\''+item.dialogWidth+'\')">预览</a>';
            }},
            {display: "业务编码", name: "bizCode", width: 120, minWidth: 60, type: "string", align: "left" },
            {display: "名称", name: "bizName", width: 130, minWidth: 60, type: "string", align: "left",
            	editor: { type:'text',required:true}
            },
            { display: "业务表实体", name: "entityClassName", width:340, minWidth: 60, type: "string", align: "left",
				editor: { type:'text'}
			},
			{
                display: "对话框宽", name: "dialogWidth", width: 60, minWidth: 60, type: "number", align: "left",
                editor: { type:'spinner',min:1,max:100,mask:'nnn'}
            },
            {
                display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "number", align: "left",
                editor: { type:'spinner',min:1,max:100,mask:'nnn'}
            },
            {
                display: "状态", name: "status", width: 80, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return UICtrl.getStatusInfo(item.status);
                }
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/bizClassification/queryBizClassificationDetails.ajax',
        parms: {
            bizClassificationId: bizClassificationId
        },
        pageSize: 20,
        width: '100%',
        height: '80%',
        sortName: 'sequence',
        sortOrder: 'asc',
        toolbar: toolbarOptions,
        heightDiff: -10,
        checkbox: true,
        usePager: false,
        enabledEdit: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onLoadData :function(){
			return !($("#id").val()=='');
		}
    });
    initAddEvent();
}
// 弹出新增对话框
function showInsertDialog() {
    var node = $('#maintree').commonTree('getSelected');
    if (!node) {
        Public.tip('请选择左侧父节点!');
        return;
    }
    UICtrl.showAjaxDialog({
        title: "添加业务分类配置",
        param: {
            parentId: node.id
        },
        width: 900,
        url: web_app.name + '/bizClassification/showInsertBizClassification.load',
        init: initShowDialog,
        ok: doSaveBizClassification,
        close: onDialogCloseHandler,
    });
}

// 弹出修改对话框
function showUpdateDialog() {
	var id = DataUtil.getUpdateRowId(gridManager);
	if (!id){return;}
    doShowUpdateDialog(id);
}

// 进行修改操作
function doShowUpdateDialog(id) {
    UICtrl.showAjaxDialog({
        title: "修改业务分类配置",
        url: web_app.name + '/bizClassification/showUpdateBizClassification.load',
        param: { id: id },
        width: 900,
        init: initShowDialog,
        ok: doSaveBizClassification,
        close: onDialogCloseHandler
    });
}

// 进行新增、修改数据处理
function doSaveBizClassification() {
    var _self = this;
    var id = getId();
    var url = "/bizClassification/insertBizClassification.ajax";
    if (id && id.length > 0){
    	url = "/bizClassification/updateBizClassification.ajax";
    }
    var detailData = DataUtil.getGridData({gridManager:bizClassificationDetailGridManager});
	if(!detailData) {
		return false;
	}
    $('#submitForm').ajaxSubmit({
        url: web_app.name + (url),
        param:{bizClassificationDetails:Public.encodeJSONURI(detailData)},
        success: function (data) {
            $('#id').val(data);
            bizClassificationDetailGridManager.options.parms['bizClassificationId'] = data;
            refreshFlag = true;
        }
    });
}
// 删除
function deleteBizClassification() {
    var action = "bizClassification/deleteBizClassification.ajax";
    DataUtil.del({
        action: action,
        idFieldName: 'id',
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}

// 更新排序号
function updateBizClassificationSequence() {
    var action = "bizClassification/updateBizClassificationsSequence.ajax";
    DataUtil.updateSequence({
        action: action,
        idFieldName: "id",
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}
// 更新排序号
function saveSortIDHandler() {
    var action = "bizClassification/updateBizClassificationDetailsSequence.ajax";
    DataUtil.updateSequence({
        action: action,
        idFieldName: "id",
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}

//启用
function enableHandler() {
    updateStatus('确实要启用选中数据吗?', 1);
}

//禁用
function disableHandler() {
    updateStatus('确实要禁用选中数据吗?', 0);
}

//修改状态
function updateStatus(message, status) {
    DataUtil.updateById({
        action: 'bizClassification/updateBizClassificationsStatus.ajax',
        gridManager: gridManager, param: {status: status},
        message: message,
        onSuccess: function () {
            reloadGrid();
        }
    });
}

// 查询
function query(obj) {
    var param = $(obj).formToJSON();
    UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
    $("#maintree").commonTree('refresh', lastSelectedId);
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

function reloadGridAll() {
	$("#maintree").commonTree('reload');
	var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

// 重置表单
function resetForm(obj) {
    $(obj).formClean();
}

// 关闭对话框
function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

//移动
function moveHandler(){
	var excludeIds=DataUtil.getSelectedIds({
		gridManager : gridManager
	});
	if (!excludeIds || excludeIds.length < 1) {
		Public.tip('请选择数据！');
		return;
	}
	UICtrl.showDialog({title:'移动到...',width:300,
		content:'<div style="overflow-x: hidden; overflow-y: auto; width:280px;height:250px;"><ul class="move-tree"></ul></div>',
		init:function(doc){
			$('ul.move-tree',doc).commonTree({
		        loadTreesAction: 'bizClassification/queryBizClassifications.ajax',
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
			DataUtil.updateById({action:'bizClassification/moveBizClassifications.ajax',
				gridManager:gridManager,idFieldName:'id',param:{parentId:parentId},
				onSuccess:function(){
					reloadGridAll();
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

function buildPermission(){
	var rows = gridManager.getSelectedRows();
	if (rows.length != 1){
		Public.errorTip("请选择一条数据。");
		return false;
	}
	var url = web_app.name + '/bizClassification/buildPermission.ajax';
    Public.ajax(url, { fullId: rows[0].fullId}, function (data) {});
}
/*===============================================================================业务分类配置明细==============================================================================*/

function initAddEvent() {
    //业务字段分组
    $('#toolbar_menuaddBizFieldSysGroup').comboDialog({
        type: 'sys', name: 'bizFieldSysGroup', lock: false, checkbox: true,
        dataIndex: 'id',
        columnRender: {
            visible: function (item) {
                return  UICtrl.getStatusInfo(item.visible);
            }
        },
        onShow: function () {
            var bizClassificationId = getId();
            if (Public.isBlank(bizClassificationId)) {
            	Public.tip('请先保存单据!');
                return false;
            }
        },
        onChoose: function (dialog) {
            var bizClassificationId = getId();
            var selectedRows = this.getSelectedRows();
            if (selectedRows.length == 0) {
                Public.errorTip('请选择业务字段分组！');
                return false;
            }
            var params = {bizClassificationId:bizClassificationId};
            var ids=[];
            $.each(selectedRows,function(i,o){
            	ids.push(o.id);
            });
            params['ids']=ids.join(',')
            Public.ajax(web_app.name + '/bizClassification/batchInsertBizFieldSysGroup.ajax', params,
                function (data) {
                    bizClassificationDetailGridManager.loadData();
                    dialog.close();
            	}
            );
            return false;
        }
    });
}

//删除业务分类详情
function deleteBizClassificationDetail() {
    DataUtil.del({
        action: 'bizClassification/deleteBizclassificationdetails.ajax',
        param: {id: $('#id').val()},
        inDialog:true,
        gridManager: bizClassificationDetailGridManager, idFieldName: 'id',
        onSuccess: function () {
            bizClassificationDetailGridManager.loadData();
        }
    });
    return false;
}

//预览
function previewGroup(bizCode,dialogWidth){
	var width=parseInt(dialogWidth);
	width=isNaN(width)?getDefaultDialogWidth():width;
	width=width<=0?getDefaultDialogWidth():width;
	var height=getDefaultDialogHeight();
	UICtrl.showDialog({title:'预览' + bizCode,width:width,lock: false,
		content:'<div style="overflow:auto;width:'+(width-50)+'px;height:'+(height-100)+'px;position: relative;"><div id="previewGroupDiv"></div></div>',
		init:function(){
			$('#previewGroupDiv').flexField({bizCode: bizCode,onInit:function(){
				//UICtrl.setDisable($('#previewGroupDiv'));
			}});
		},
		ok:function(){
			var values=$('#previewGroupDiv').flexField('getFieldValues', false);
			alert($.toJSON(values));
		}
	});
	return false;
}