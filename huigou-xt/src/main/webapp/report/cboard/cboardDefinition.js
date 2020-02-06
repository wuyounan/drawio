var gridManager = null, refreshFlag = false;
var tableGridManager, uiParamGridmanager;

var CommonTreeKind = CommonTreeKind || {};
CommonTreeKind.CboardDefinition=51;

$(document).ready(function() {
	initUI();
	initGrid();
});

function initUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.CboardDefinition,
		onClick: onFolderTreeNodeClick
	});
}
//初始化表格
function initGrid() {
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
		url: web_app.name+'/cboardDefinition/slicedQueryCboardDefinitions.ajax',
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
	if(folderId==CommonTreeKind.CboardDefinition){
		folderId="";
		html.push('cboard列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>cboard列表');
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
        title: "添加cboard定义",
        url: web_app.name + '/cboardDefinition/showInsertCboardDefinition.load',
        param: { folderId: folderId },
        width: 800,
        height: 400,
        ok: doSave,
        init: initEntryDialog,
        close: dialogClose
    });
}

//编辑按钮
function updateHandler(id){
	if(!id){
		id=DataUtil.getUpdateRowId(gridManager);
	}
	UICtrl.showAjaxDialog({
		title: '编辑cboard定义',
		url: web_app.name + '/cboardDefinition/showUpdateCboardDefinition.load',
		width: 800, 
		height: 400,
		param:{ id: id }, 
		init: initEntryDialog,
		ok: doSave, 
		close: dialogClose
	});
}

function doSave() {
    var _self = this;
    var id = getId();
    var url = "/cboardDefinition/insertCboardDefinition.ajax";
    if (id && id.length > 0){
    	url = "/cboardDefinition/updateCboardDefinition.ajax";
    }
    
    var uiParams = DataUtil.getGridData({gridManager: uiParamGridManager});
    var tables = DataUtil.getGridData({gridManager: tableGridManager});
    
	if(!uiParams && !tables) {
		return false;
	}
    
    $('#submitForm').ajaxSubmit({
        url: web_app.name + url,
        param: { uiParamData: Public.encodeJSONURI(uiParams), tableData: Public.encodeJSONURI(tables) },
        success: function (data) {
        	setId(data);
        	reloadEntryGrids();
            refreshFlag = true;
        }
    });
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'cboardDefinition/deleteCboardDefinitions.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//更新排序号
function updateSequenceHandler() {
 var action = "cboardDefinition/updateCboardDefinitionsSequence.ajax";
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
 DataUtil.updateById({ action: 'cboardDefinition/updateCboardDefinitionsStatus.ajax',
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
		title:'移动cboard',
		kindId:CommonTreeKind.CboardDefinition,
		save:function(parentId){
			DataUtil.updateById({action:'cboardDefinition/moveCboardDefinitions.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
}

function setId(value){
	$("#id").val(value);
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

function reloadEntryGrids(){
	var definitionId = getId();
	
	uiParamGridManager.options.parms['definitionId'] = definitionId;
	tableGridManager.options.parms['definitionId'] = definitionId;
	
	uiParamGridManager.loadData();
	tableGridManager.loadData();
}

//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

/**
 * 初始化界面参数表格
 */
function initUIParamGrid(){	
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	    	addHandler: function(){
	    		addEntryHandler(uiParamGridManager);
	    	},
	    	deleteHandler: function(){
				doDeleteEntry(uiParamGridManager, 'deleteUIParams');
			}
	});
	uiParamGridManager = UICtrl.grid('#uiParamGrid', {
        columns: [
            { display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" ,
	        	editor: { type: 'text',required:true} },		   
			{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" ,
		        editor: { type: 'text',required:true} },	
			{ display: "控件类型", name: "uiParamKindTextView", width: 200, minWidth: 60, type: "string", align: "left" ,
			    editor:{ type: 'dictionary', data: { name: 'uiParamKind' }, 
    			textField: 'uiParamKindTextView', valueField: 'uiParamKind',required:true }
			},
			{ display: "排序号", name: "sequence", width: 80, minWidth: 30, type: "int", align: "left",
		        editor: { type:'spinner',min:1,max:100,mask:'nnn'}
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/cboardDefinition/queryUIParams.ajax',
        parms: { definitionId: getId() },
        width: '100%',
        height: '180',
        heightDiff: -8,
        toolbar: toolbarOptions,
        enabledEdit: true,
		sortName: 'sequence',
		sortOrder:'asc',
        checkbox: true,
        usePager: false,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onLoadData: function () {
        	return getId()!='';
        }
    });
}

function addEntryHandler(localGridManager){
	if (getId() == ""){
		Public.errorTip("请先保存Cboard定义。");
		return;
	}
	UICtrl.addGridRow(localGridManager, {sequence: localGridManager.getData().length + 1});
} 

function doDeleteEntry(localGridManager, action){
	DataUtil.delSelectedRows({action:'cboardDefinition/' + action + '.ajax',
		gridManager: localGridManager, idFieldName:'id',
		onSuccess:function(){
			localGridManager.loadData();
		},
		param: {
			definitionId: getId()
		}
	});
}

function initTableGrid(){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	    	addHandler:function(){
	    		addEntryHandler(tableGridManager);
	    	},
	    	deleteHandler: function(){
	    		doDeleteEntry(tableGridManager, 'deleteTables');
			}	    	
	});

	tableGridManager = UICtrl.grid('#tableGrid', {
        columns: [
            { display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" ,
	        	editor: { type: 'text',required:true} },		   
			{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" ,
		        editor: { type: 'text',required:true} },
			{ display: "数据集", name: "datasetName", width: 150, minWidth: 30, type: "string", align: "left" ,
		        	editor: { type: 'select',  
		        		      data: { type: "cboard", name: "selectDataset", 
		        			          back: { datasetId: "datasetId", datasetName: "datasetName" }}, 
		        			   required: true 
		        			} 
		    },
			{ display: "排序号", name: "sequence", width: 80, minWidth: 30,  type: "int", align: "left",
				editor: { type:'spinner',min:1,max:100,mask:'nnn'}
            },
            { display: "cascade", name: "cascade", width: 80, minWidth: 30, type: "string", align: "left", hide: true },
            { display: "sortJson", name: "sortJson", width: 80, minWidth: 30, type: "string", align: "left", hide: true },
            { display: "tableColumnsJson", name: "tableColumnsJson", width: 80, minWidth: 30, type: "string", align: "left", hide: true },
            { display: "操作", name: "operation", width: 60, minWidth: 60, type: "string", align: "center",
            	render: function (item){
            		return  "<a href='javascript:void(0);' class='GridStyle' rowIndex='" + item.__index + "' id='"+ item.id +"'>详细信息</a>";
            	}
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/cboardDefinition/queryTables.ajax',
        parms: { definitionId: getId() },
        usePager: false,
        width: '100%',
        height: '180',
        heightDiff: -8,
        toolbar: toolbarOptions,
        enabledEdit: true,
		sortName: 'sequence',
		sortOrder:'asc',
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onLoadData: function () {
        	return getId() != '';
        }
    });
	
	$('#tableGrid').on('click',function(e){
		var $clicked = $(e.target || e.srcElement);
		if($clicked.hasClass('GridStyle')){
			var id = $clicked.attr("id");
			var rowIndex = $clicked.attr("rowIndex");
			defineTableHeader(id, rowIndex);
		}
	});	
}	

function defineTableHeader(id, rowIndex){
	var cascade, sortJson, tableColumnsJson;
	var data = tableGridManager.getData();
	
	$.each(data, function(i, o){
		if (o["id"] == id){
			cascade = o.cascade;
			sortJson = o.sortJson;
			tableColumnsJson = o.tableColumnsJson;
			return false;
		}
	});
	
	UICtrl.showDialog({
		content: 
			"<div class=\"hg-form\">\n" +
			"  <div class=\"col-xs-4 col-sm-2\">\n" + 
			"     <label class=\"hg-form-label\" id=\"cascade_label\">级联查询&nbsp;:</label>\n" + 
			"  </div>\n" + 
			"  <div class=\"col-xs-8 col-sm-10 col-white-bg\">\n" + 
			"     <textarea id=\"cascade\" label=\"级联查询\" rows=\"2\" cols=\"60\"></textarea>\n" + 
			"  </div>\n" + 
			
			"  <div class=\"col-xs-4 col-sm-2\">\n" + 
			"     <label class=\"hg-form-label\" id=\"sortJson_label\">默认排序&nbsp;:</label>\n" + 
			"  </div>\n" + 
			"  <div class=\"col-xs-8 col-sm-10 col-white-bg\">\n" + 
			"     <textarea id=\"sortJson\" label=\"默认排序\" rows=\"2\" cols=\"60\"></textarea>\n" + 
			"  </div>\n" + 

			
			"  <div class=\"col-xs-4 col-sm-2\">\n" + 
			"    <label class=\"hg-form-label\" id=\"tableColumnsJson_label\">表头定义&nbsp;:</label></div>\n" + 
			"  <div class=\"col-xs-8 col-sm-10 col-white-bg\">\n" + 
			"    <textarea id=\"tableColumnsJson\" required=\"false\" label=\"表头定义\" rows=\"13\" cols=\"50\"></textarea>\n" + 
			"  </div>\n" + 
			"</div>",
		title: "详细信息",
		width: 600,
        height: 430, 
        cancel: "取消",
        init: function(){
        	$("#cascade").val(cascade);
        	$("#sortJson").val(sortJson);
        	$("#tableColumnsJson").val(tableColumnsJson);
        },
		ok: function(){
			var _self = this;
			tableColumnsJson = $("#tableColumnsJson").val();
			tableColumnsJson = tableColumnsJson.replace(/\'/g, "\"");

			cascade = $("#cascade").val();
			sortJson = $("#sortJson").val();
			
			tableGridManager.updateCell('cascade', cascade, rowIndex);
			tableGridManager.updateCell('sortJson', sortJson, rowIndex);
			tableGridManager.updateCell('tableColumnsJson', tableColumnsJson, rowIndex);
			_self.close();
        }
	});
}

function initEntryDialog(){	
	initUIParamGrid();
	initTableGrid();
}