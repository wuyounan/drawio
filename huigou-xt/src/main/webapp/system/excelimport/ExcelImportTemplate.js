var gridManager = null, refreshFlag = false,detailGridManager=null;
$(document).ready(function() {
	initializeGrid();
	initializeUI();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.ExpTemplet,
		onClick: onFolderTreeNodeClick
	});
}

function  onFolderTreeNodeClick(data,folderId){
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.ExpTemplet){
		parentId="";
		html.push('模板列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>模板列表');
	}
	$("#layout").layout("setCenterTitle", html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId: parentId});
	}
}

//初始化表格
function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		addHandler: addHandler,
		updateHandler:function (){
			updateHandler();
		}, 
		deleteHandler: deleteHandler,
		enableHandler:enableHandler,
		disableHandler:disableHandler,
		moveHandler:moveHandler,
		expTempletbtn:{id:'expTempletbtn',text:'导出模板',click:function(){exportExcelHandler();},img:'fa-hand-o-down'},
		excelImpManager:{id:'excelImpManager',text:'导入对话框测试',click:function(){excelImpManager();},img:'fa-link'}
     });
	
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		          { display: "编码", name: "code", width: 180, minWidth: 60, type: "string", align: "left" },	
		          { display: "名称", name: "name", width: 220, minWidth: 60, type: "string", align: "left" },
		          { display: "表名称", name: "tableName", width: 220, minWidth: 60, type: "string", align: "left" },		
		          { display: "Java组件名", name: "procedureName", width: 240, minWidth: 60, type: "string", align: "left" },
		          {display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "left",
		        	  render: function (item) {
		        		  return UICtrl.getStatusInfo(item.status);
		        	  } 
		          }
		         ],
		dataAction: 'server',
		url: web_app.name+'/excelImport/slicedQueryExcelImportTemplates.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -5,
		checkbox: true,
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			updateHandler(data.id);
		},
		onAfterShowData: function() {
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

function reloadDetailGrid(){
	detailGridManager.loadData();
}

//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

//添加按钮 
function addHandler() {
	var parentId=$('#treeParentId').val();
	if(parentId==''){
		Public.errorTip("请选择模板类型。");
		return false ;
	}
	UICtrl.showAjaxDialog({url: web_app.name + '/excelImport/showInsertExcelImportTemplate.load',
		ok: insert, close: dialogClose,width:750,init:initDetailDialog,title:'新增模板'
	});
}

//初始化明细表
function initDetailDialog(doc){
	var templateId=$('#id').length>0?$('#id').val():'';
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
	       addHandler: function() {
				UICtrl.addGridRow(detailGridManager);
	       },
	       deleteHandler: function(){
				DataUtil.delSelectedRows({action:'excelImport/deleteExcelImportTemplateDetails.ajax',
					param: {templateId: templateId},
					gridManager:detailGridManager,idFieldName:'id',
					onSuccess:function(){
						detailGridManager.loadData();
					}
				});
	          }
	      });
	
	detailGridManager=UICtrl.grid('#detailGrid',{
		columns:[
		    {display: 'EXCEL列号', name: 'excelColumnNumber', width: 120, sortable: false, align: 'center',editor: { type:'spinner',required:true,min:1,max:100,mask:'nnn'}},
	        {display: 'EXCEL列名', name: 'excelColumnName', width:170, sortable: false, align: 'left',editor: { type: 'text',required:true}},
	        {display: '中间表列名', name: 'columnName', width: 150, sortable: false, align: 'left',editor: { type: 'text',required:true,maxLength:30}},
	        {display: '列说明', name: 'columnDescription', width: 150, sortable: true, align: 'left',editor: { type: 'text'}}
        ],
        parms: {
        	templateId: templateId
		},
        dataAction: 'server',
		url: web_app.name+'/excelImport/queryExcelImportTemplateDetails.ajax',
		width: '100%',
		sortName:'excelColumnNumber',
		sortOrder: 'asc',
		height: 250,
		toolbar: toolbarOptions,
		enabledEdit: true,
		usePager: false,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		autoAddRow:{status:0},
		onLoadData:function(){
			return $('#id').val()!='';
		}});
}


//编辑按钮
function updateHandler(id){
	if(!id){
		var id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	UICtrl.showAjaxDialog({url: web_app.name + '/excelImport/showUpdateExcelImportTemplate.load',
		param:{id:id}, 
		init: initDetailDialog,
		ok: update,
		width:750,
		title:'修改模板',
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'excelImport/deleteExcelImportTemplates.ajax',
		gridManager:gridManager,
		onSuccess:function(){
			reloadGrid();		  
		}
	});

}

//新增保存
function insert() {
	var id=$('#id').val();
	if(id!='') {
		return update();
	}
	var detailData=DataUtil.getGridData({gridManager:detailGridManager});
	if(!detailData) {
		return false;
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/excelImport/insertExcelImportTemplate.ajax',
		param:{folderId: $('#treeParentId').val(), detailData:Public.encodeJSONURI(detailData)},
		success: function(id) {
			$('#id').val(id);
			detailGridManager.options.parms['templateId']=id;
			detailGridManager.loadData();
			refreshFlag = true;
		}
	});
}

//修改按钮状态
function intModifPageButton(){
	$('#doExpTemplButton').attr('disabled',false);
	$('#adddtlbutton').attr('disabled',false);
}

function getTemplateId(){
	return $("#id").val();
}

//编辑保存
function update(){
	var detailData=DataUtil.getGridData({gridManager:detailGridManager,idFieldName:'id'});
	if(!detailData) {
		return false;
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/excelImport/updateExcelImportTemplate.ajax',
		param:{templateId: getTemplateId(), detailData:Public.encodeJSONURI(detailData)},
		success: function() {
			refreshFlag = true;
			detailGridManager.loadData();
		}
	});
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}

//导出模板
function exportExcelHandler(){
	var rows = gridManager.getSelectedRows();
	if(!rows.length){
		Public.tip('请选择数据模板！'); 
		return; 
    }
	if(rows.length>1){
		Public.tip('只能选择一条记录导出!');
		return;
	}
	var row = rows[0];
	var templetName=row.name;
	var url=web_app.name+'/excelImport/exportExcelTemplate.ajax';
	UICtrl.downFileByAjax(url,{id:row.id},templetName);
}

//启用模板
function enableHandler(){
	DataUtil.updateById({ action: 'excelImport/updateExcelImportTemplateStatus.ajax',
		gridManager: gridManager, param:{status: 1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

//禁用模板
function disableHandler(){
	DataUtil.updateById({ action: 'excelImport/updateExcelImportTemplateStatus.ajax',
		gridManager: gridManager, param:{status: 0},
		message:'确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}
//移动
function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,kindId:CommonTreeKind.ExpTemplet,
		save:function(parentId){
			DataUtil.updateById({action:'excelImport/moveExcelImportTemplates.ajax',
				gridManager:gridManager,param:{folderId: parentId},
				onSuccess:function(){
					reloadGrid();	
				}
			});
		}
	});
}

function excelImpManager(){
	var row = DataUtil.getUpdateRow(gridManager);
	if (!row){ return; }
	ExcelImpManager.showImpDialog('测试xx导入',row.code,'100');
}
