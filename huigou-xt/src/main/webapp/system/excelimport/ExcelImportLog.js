var gridManager = null, refreshFlag = false,temp_templ_id=null;
var imp_result_table_param={pageSize:20,width:'100%',height:'100%',heightDiff : -300,fixedCellHeight : true,selectRowButtonOnly : true};//导入结果列表初始参数
var imp_result_table_url=web_app.name+'/excelImport/slicedQueryExcelImportDetails.ajax';//导入结果列表查询地址
var imp_log_query_manage_type='';//日志查询管理权限类型
$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	UICtrl.layout("#layout",{topHeight:290});
	
	$("#topToolbar").toolBar({
		items : [ 
		  {name : '导入数据', id : "importdata",icon:'fa-cloud-upload'},
		  {name : '导出模板', id : "exporttemplet",icon:'fa-cloud-download',event:exportExcel},
		  {name : '导出失败',id : "exportfail",icon:'fa-pause-circle',event:function(){
			  doExpImpResult(false);
		  }},
		  {name : '导出成功',id : "exportsuccess",icon:'fa-forward',event:function(){
			  doExpImpResult(true);
		  }}
	   ]
	});
	
	//初始化选项卡
	$('#impResultTab').tab({onClick:function(item){
		var _id=item.data('id');
		var gm=UICtrl.getGridManager('#'+_id);
		UICtrl.onGridResize(gm);
	}});
	
    $('#importdata').uploadButton({
    	filetype:['xls','xlsx'],
	    param:function(){
      		if(getTemplateId()==''){
      			Public.tip('请选择模板。');
      			return false;
      		}
      		return {templateId: getTemplateId()};
      	},
    	url:web_app.name+'/excelImport/upload.ajax',
    	afterUpload:function(){
    		query($('#queryMainForm'));
    	}
    });
    
    $('#chooseImptemplet').comboDialog({type:'sys',name:'importTemplate',dataIndex:'id',onChoose:function(){
    	var row=this.getSelectedRow();
    	var templateId=row['id'],templetName=row['name'];
    	if($('#id').val()!=templateId){
    		$('#id').val(templateId);
    		$('#templetName').val(templetName);
    		//动态引入JS文件
    		//importJS(row['code']);
    		query($('#queryMainForm'));
    	}
    	return true;
    }});
   
}
//动态引入JS文件
function importJS(templateCode){
	var src=web_app.name+'/system/ExcelImport/js/'+templateCode+'.js';
	var head = $('head').remove('#impOperationJSFile');
    $("<script></script>").attr({src:src,type:'text/javascript',id:'impOperationJSFile'}).appendTo(head);
}

//初始化表格
function initializeGrid() {
	var toolbarOptions =getLogGridToolbarOptions();
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
			{ display: "流水号", name: "batchNumber", width: 250, minWidth: 60, type: "string", align: "left" },	
			{ display: "导入信息", name: "errorMessage", width: 400, minWidth: 60, type: "string", align: "left" },	
			{ display: "失败数", name: "errorCount", width: 80, minWidth: 60, type: "number", align: "right" },		   
			{ display: "成功数", name: "successCount", width: 80, minWidth: 60, type: "number", align: "right" },	
			{ display: "操作用户", name: "personMemberName", width: 120, minWidth: 60, type: "string", align: "left" },	
			{ display: "操作时间", name: "createdDate", width: 130, minWidth: 60, type: "datetime", align: "left" },
			{ display: "文件名", name: "fileName", width: 200, minWidth: 60, type: "string", align: "left" }
		],
		dataAction : 'server',
		url: web_app.name+'/excelImport/slicedQueryExcelImportLogs.ajax',
		parms:{templateId: getTemplateId()},
		manageType: imp_log_query_manage_type,
		pageSize : 10,
		sortName: 'createdDate',
		sortOrder: 'desc',
		width : '100%',
		height : '200',
		heightDiff:-5,
		toolbar: toolbarOptions,
		fixedCellHeight : true,
		selectRowButtonOnly : true,
		onDblClickRow : function(data, rowindex, rowobj) {
			queryImportLogDetails(data.templateId, data.batchNumber);
		},
		onLoadData :function(){
			return getTemplateId() != "";
		}
	});
	UICtrl.createGridQueryBtn('#maingrid','div.l-panel-topbar',function(param){
		UICtrl.gridSearch(gridManager, {param:encodeURI(param)});
	});
}

function getTemplateId(){
	return $('#id').val();
}

// 查询
function query(obj) {
	var param = $(obj).formToJSON();
	param.templateId = getTemplateId();
	UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
} 

//导出模板
function exportExcel(){
	if(getTemplateId() == ''){
		Public.tip('请选择数据模板。');
		return;
	}
	var templetName='';
	if($('#templetName').length>0){
		templetName=$('#name').val();
	}else{
		templetName=$('#templetNameSubject').text();
	}
	var url=web_app.name+'/excelImport/exportExcelTemplate.ajax';
	UICtrl.downFileByAjax(url,{id:getTemplateId()},templetName);
}
function doExpImpResult(flag){
	var table=flag?$('#imp_success_grid'):$('#imp_error_grid');
	var fileName=(flag?'导入成功数据':'导入失败数据');
	var gridManager=table.ligerGetGridManager();
	if(gridManager){
		UICtrl.gridExport(gridManager,{exportType:'all',fileName:fileName});
	}else{
		alert('未执行查询无法导出！');
	}
}

//查询导入结果表头
function queryImportLogDetails(id, batchNumber){
	if(Public.isBlank(batchNumber)){
		return;
	}
	$('#imp_success_title').trigger('click'); 
	if(temp_templ_id == id){ 
		doQueryImpResult(id, batchNumber); 
		return false;
	}
	var url=web_app.name+'/excelImport/queryExcelImportGridHead.ajax';
	Public.ajax(url,{templateId: id},function(data){
		initImpSuccessGrid(getImpResultTableColModel(data.Rows),id,batchNumber);
		initImpErrorGrid(getImpResultTableColModel(data.Rows),id,batchNumber);
		temp_templ_id = id;
	});
}

//组合表头数据
function getImpResultTableColModel(rows){
	var colModel=[];
	$.each(rows, function(i,o){
		colModel.push({display: o['excelColumnName'], name : o['columnName'], width :100, align: 'left'});
	});
	colModel.push({display:'备注', name :'message', width :120, align: 'left'});
	return colModel;
}

function initImpSuccessGrid(colModel,id,batchNumber){
	$('#imp_success_tmp_div').removeAllNode();
	$('#imp_success_content').html('<div id="imp_success_tmp_div"><div id="imp_success_grid"></div></div>');
	UICtrl.grid('#imp_success_grid',
		$.extend({},
			imp_result_table_param,
			{columns:colModel,url:imp_result_table_url,parms:{templateId: id, batchNumber: batchNumber, status:'1'}},
			getSuccessGridOptions()
		)
	);
}

function initImpErrorGrid(colModel,id,batchNumber){
	$('#imp_error_tmp_div').removeAllNode();
	$('#imp_error_content').html('<div id="imp_error_tmp_div"><div id="imp_error_grid"></div></div>');
	UICtrl.grid('#imp_error_grid',
		$.extend({},
			imp_result_table_param,
			{columns:colModel,url:imp_result_table_url,parms:{templateId: id, batchNumber: batchNumber, status:'2'}},
			getErrorGridOptions()
		)
	);
}

function getSuccessGridManager(){
	return $('#imp_success_grid').ligerGetGridManager();
}
function getErrorGridManager(){
	return $('#imp_error_grid').ligerGetGridManager();
}


function doQueryImpResult(id, batchNumber){
	if(batchNumber=='') {
		return;
	}
	UICtrl.gridSearch(getSuccessGridManager(),{templateId: id, batchNumber:batchNumber, status:'1'});
	UICtrl.gridSearch(getErrorGridManager(),{templateId: id, batchNumber: batchNumber, status:'2'});
}

function getLogGridToolbarOptions(){
	return {items:[]};
}

function getSuccessGridOptions(){
	return {};
}
function getErrorGridOptions(){
	return {};
}

