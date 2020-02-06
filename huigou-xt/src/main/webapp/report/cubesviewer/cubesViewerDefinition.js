var gridManager = null, refreshFlag = false;
CommonTreeKind.CubesViewerDefine=50;
$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.CubesViewerDefine,
		onClick: onFolderTreeNodeClick
	});
}
//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		addHandler: addHandler, 
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
        saveSortIDHandler: updateSequenceHandler,
        enableHandler: enableHandler,
		disableHandler: disableHandler,
		moveHandler:moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		 columns: [
			{ display: "编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left",frozen: 1 },
			{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },
            { display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "int", align: "left",
                render: function (item) {                    
                    return UICtrl.sequenceRender(item);
                }
            },
            { display: "状态", name: "status", width: 80, minWidth: 60, type: "string", align: "center",
                render: UICtrl.getStatusInfo
            }
		],
		dataAction: 'server',
		url: web_app.name+'/cubesViewerDefinition/slicedQueryCubesViewerDefinitions.ajax',
		width: '100%',
		height: '100%',
        sortName: "sequence",
        sortOrder: "asc",
		heightDiff: -8,
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true, 
		onDblClickRow: function(data, rowindex, rowobj) {
			updateHandler(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[];
	if(folderId==CommonTreeKind.CubesViewerDefine){
		folderId="";
		html.push('cubesViewer列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>cubesViewer列表');
	}
	$('#layout').layout('setCenterTitle',html.join(''));
	$('#folderId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId: folderId});
	}
}
//添加按钮 
function addHandler() {
	var folderId=$('#folderId').val();
	if(folderId==''){
		Public.tip('请选择类别树。'); 
		return;
	}

    UICtrl.showAjaxDialog({
        title: "添加cubesViewer定义",
        url: web_app.name + '/cubesViewerDefinition/showInsertCubesViewerDefinition.load',
        param: { folderId: folderId },
        width: 600,
        ok: doSave,
        close: dialogClose
    });
}

//编辑按钮
function updateHandler(id){
	if(!id){
		id=DataUtil.getUpdateRowId(gridManager);
	}
	UICtrl.showAjaxDialog({title:'编辑cubesViewer定义',
		url: web_app.name + '/cubesViewerDefinition/showUpdateCubesViewerDefinition.load',
		width:600, 
		param:{ id: id }, 
		ok: doSave, 
		close: dialogClose
	});
}

function doSave() {
    var _self = this;
    var id = getId();
    var url = "/cubesViewerDefinition/insertCubesViewerDefinition.ajax";
    if (id && id.length > 0)
        url = "/cubesViewerDefinition/updateCubesViewerDefinition.ajax";
    $('#submitForm').ajaxSubmit({
        url: web_app.name + url,
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'cubesViewerDefinition/deleteCubesViewerDefinitions.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//更新排序号
function updateSequenceHandler() {
 var action = "cubesViewerDefinition/updateCubesViewerDefinitionsSequence.ajax";
 DataUtil.updateSequence({
     action: action,
     idFieldName: "id",
     gridManager: gridManager,
     onSuccess: reloadGrid
 });
}

//启用
function enableHandler() {
 updateStatus('您确定要启用选中数据吗?', 1);
}

//禁用
function disableHandler() {
 updateStatus('您确定要禁用选中数据吗?', 0);
}

//修改状态
function updateStatus(message, status) {
 DataUtil.updateById({ action: 'cubesViewerDefinition/updateCubesViewerDefinitionsStatus.ajax',
     gridManager: gridManager, param: { status: status },
     message: message,
     onSuccess: function () {
         reloadGrid();
     }
 });
} 

//移动
function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,
		title:'移动cubesViewer',
		kindId:CommonTreeKind.CubesViewerDefine,
		save:function(parentId){
			DataUtil.updateById({action:'cubesViewerDefinition/moveCubesViewerDefinitions.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
}

function getId() {
    return $("#id").val() || "";
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag = false;
	}
}

//查询
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