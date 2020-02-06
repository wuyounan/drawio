var gridManager = null,  formulaParamGridManager = null, uiParamGridManager = null, tabGridManager = null, refreshFlag=false;
var yesNoList = { 0: '否', 1: '是' }
$(document).ready(function () {
    initUI();
    initEntryGrid();
    
    function initUI() {
        $.getFormButton(
            [
                {id: 'saveDetail', name: '保 存', event: saveIndex},
                {name: '关 闭', event: closeWindow}
            ]
        );
    }
    
    function initEntryGrid() {
        var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        	addHandler: addIndexEntryHandler, 
    		updateHandler: function(){
    			updateIndexEntryHandler();
    		},
    		deleteHandler: deleteIndexEntryHandler,
    		saveSortIDHandler: saveIndexEntrySortIDHandler
        });
        gridManager = UICtrl.grid('#entryGrid', {
            columns: [
                { display: "时间维度", name: "timeDimTextView", width: 70, minWidth: 60, type: "string", align: "center" },		   
    			{ display: "组织维度", name: "organDimTextView", width: 70, minWidth: 60, type: "string", align: "center" },		   
    			{ display: "正常上限", name: "upperLimit", width: 70, minWidth: 60, type: "string", align: "right" },		   
    			{ display: "正常下限", name: "lowerLimit", width: 70, minWidth: 60, type: "string", align: "right" },		   
    			{ display: "公式", name: "formula", width: 200, minWidth: 60, type: "string", align: "left" },
    			{ display: "显示类型", name: "viewKind", width: 70, minWidth: 60, type: "string", align: "center" },		   
    			{ display: "url", name: "url", width: 200, minWidth: 60, type: "string", align: "left" },
    			{ display: "排序号", name: "sequence", width: 60, minWidth: 30, type: "int", align: "left",
                    render: function (item) {                    
                        return UICtrl.sequenceRender(item);
                    }
                }
            ],
            dataAction: 'server',
            url: web_app.name + '/index/slicedQueryIndexEntries.ajax',
            title:'指标明细',
            parms: {indexId: getId()},
            width: '100%',
            height: '180',
            heightDiff: -8,
            toolbar: toolbarOptions,
    		sortName:'sequence',
    		sortOrder:'asc',
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowindex, rowobj) {
            	updateIndexEntryHandler(data.id);
            },
            onLoadData: function () {
            	return !(Public.isBlank(getId()));
            }
        });
    }
    
});

function getId() {
    return $("#id").val() || '';
}

function setId(value) {
	$("#id").val(value);
    gridManager.options.parms['indexId'] =value;
}

function getEntryId(){
	return $("#entryId").val();
}

function setEntryId(value){
	return $("#entryId").val(value);
}

function saveIndex() {
    var id = getId(), param = {};
    var url = web_app.name + "/index/insertIndex.ajax";
    if (id != '') {
        url = web_app.name + "/index/updateIndex.ajax";
    }
    $('#submitForm').ajaxSubmit({ url: url,param: param,
        success: function (data) {
        	setId(data);
        }
    });
}

function closeWindow() {
    UICtrl.closeAndReloadTabs('MCSIndex');
}

//添加按钮 
function addIndexEntryHandler() {
	var indexId = getId();
	if (!indexId){ 
		Public.tip("请先保存指标数据。");
		return; 
	}
	
	UICtrl.showAjaxDialog({
		title: "添加指标明细",
		url: web_app.name + '/index/showInsertIndexEntry.load', 
		param:{indexId: getId()}, 
		width: 810, 
		height: 500,
		ok: doSaveIndexEntry, 
		init: initEntryDetailDialog,
		close: function (){
			reloadGrid();
		}
	});
}

//编辑按钮
function updateIndexEntryHandler(id){
	if(!id){
		id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	UICtrl.showAjaxDialog({
		title: "修改指标明细",
		url: web_app.name + '/index/showUpdateIndexEntry.load', 
		width:810, 
		height: 500,
		param:{id: id}, 
		ok: doSaveIndexEntry, 
		init: initEntryDetailDialog,
		close: function (){
			reloadGrid();
		}
	});
}

function reloadEntryGrids(){
	var entryId = getEntryId();
	
	formulaParamGridManager.options.parms['entryId'] = entryId;
	uiParamGridManager.options.parms['entryId'] = entryId;
	tabGridManager.options.parms['entryId'] = entryId;
	
	formulaParamGridManager.loadData();
	uiParamGridManager.loadData();
	tabGridManager.loadData();
}

function doSaveIndexEntry(){
    var _self = this;
   
    var id = getEntryId() || ''; 
    var isInsert = Public.isBlank(id);
    
    var url = isInsert ? "/index/insertIndexEntry.ajax" : "/index/updateIndexEntry.ajax";
    
    var indexEntryFormulaParams = DataUtil.getGridData({gridManager: formulaParamGridManager});
    var indexEntryUIParams = DataUtil.getGridData({gridManager: uiParamGridManager});
    var indexEntryTabs = DataUtil.getGridData({gridManager: tabGridManager});
    
	if(!indexEntryFormulaParams && !indexEntryUIParams && !indexEntryTabs) {
		return false;
	}
	//完整性检查
    $('#entrySubmitForm').ajaxSubmit({
        url: web_app.name + (url),
        param: { indexEntryFormulaParamData: Public.encodeJSONURI(indexEntryFormulaParams),
        	     indexEntryUIParamData: Public.encodeJSONURI(indexEntryUIParams),
        	     indexEntryTabData: Public.encodeJSONURI(indexEntryTabs)
        	   },
        success: function (data) {
        	if (isInsert) {
        		setEntryId(data);
        	}
        	reloadEntryGrids();
        }
    });	
}

//删除按钮
function deleteIndexEntryHandler(){
	DataUtil.del({action:'index/deleteIndexEntries.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

//保存扩展字段排序号
function saveIndexEntrySortIDHandler(){
	var action = "index/updateIndexEntriesSequence.ajax";
	DataUtil.updateSequence({action: action,gridManager: gridManager,idFieldName:'id', onSuccess: function(){
		reloadGrid(); 
	}});
}

function doDeleteIndexEntryEntry(gridManager, action){
	DataUtil.delSelectedRows({action:'index/' + action + '.ajax',
		gridManager:gridManager, idFieldName:'id',
		onSuccess:function(){
			gridManager.loadData();
		},
		param: {
			entryId: getEntryId()
		}
	});
}

function doSaveIndexEntryEntrySortId(gridManager, action){
	action = "index/' + action + '.ajax";
	DataUtil.updateSequence({action: action, gridManager: gridManager, idFieldName:'id', onSuccess: function(){
		gridManager.reloadGrid(); 
		}
	});		
}

function addEntryEntryHandler(gridManager, values){
	if (getEntryId() == ""){
		Public.errorTip("请先保存明细数据。");
		return;
	}
	UICtrl.addGridRow(gridManager, $.extend({sequence: gridManager.getData().length + 1}, values || {}));
} 

function initFormulaParamGrid(){	
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	    	addHandler: function(){
	    		addEntryEntryHandler(formulaParamGridManager);
	    	},
			deleteHandler: function(){
				doDeleteIndexEntryEntry(formulaParamGridManager, 'deleteIndexEntryFormulaParams');
			}
	});
	formulaParamGridManager=UICtrl.grid('#formulaParamGrid', {
		 columns: [
				{ display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left",
		        	editor: { type: 'text',required:true} 
				},		   
				{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left",
		        	editor: { type: 'text',required:true} 
				},		   
				{ display: "数据类型", name: "dataTypeTextView", width: 100, minWidth: 60, type: "string", align: "left",
		        	editor: { type: 'dictionary', data: { name: 'fieldTypeList' }, 
		        			textField: 'dataTypeTextView', valueField: 'dataType',required:true }
				},		   
				{ display: "参数值", name: "paramValue", width: 150, minWidth: 60, type: "string", align: "left",
		        	editor: { type: 'text',required:true} 
				},		   
				{ display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'}
				}
		],
		dataAction: 'server',
		url: web_app.name+'/index/queryIndexEntryFormulaParams.ajax',
		parms: {entryId: getEntryId()},
		usePager: false,
		width: '100%', 
		height: '200',
		sortName:'sequence',
		sortOrder:'asc',
		heightDiff: -8,
		toolbar: toolbarOptions,
		enabledEdit: true,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onLoadData: function(){
			return getEntryId()!='';
		}
	});
}

/**
 * 初始化界面参数表格
 */
function initUIParamGrid(){	
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	    	addHandler: function(){
	    		addEntryEntryHandler(uiParamGridManager);
	    	},
	    	deleteHandler: function(){
				doDeleteIndexEntryEntry(uiParamGridManager, 'deleteIndexEntryUIParams');
			}
	});
	uiParamGridManager = UICtrl.grid('#uiParamGrid', {
        columns: [
            { display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" ,
	        	editor: { type: 'text',required:true} },		   
			{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" ,
		        editor: { type: 'text',required:true} },		   
			{ display: "排序号", name: "sequence", width: 80, minWidth: 30, type: "int", align: "left",
		        editor: { type:'spinner',min:1,max:100,mask:'nnn'}
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/index/queryIndexEntryUIParams.ajax',
        parms: { entryId: getEntryId() },
        width: '100%',
        height: '200',
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
        	return getEntryId()!='';
        }
    });
}

function defineTableHeader(id, rowIndex){
	var cascade, sortJson, tableColumnsJson;
	var data = tabGridManager.getData();
	
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
			
			tabGridManager.updateCell('cascade', cascade, rowIndex);
			tabGridManager.updateCell('sortJson', sortJson, rowIndex);
			tabGridManager.updateCell('tableColumnsJson', tableColumnsJson, rowIndex);
			_self.close();
        }
	});
}

function yesNoRender(item, index, columnValue, columnInfo) {
    return yesNoList[columnValue];
}

function initTabGrid(){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	    	addHandler:function(){
	    		addEntryEntryHandler(tabGridManager, {provinceEnable: 1, cityEnable: 1, countyEnable: 1});
	    	},
	    	deleteHandler: function(){
				doDeleteIndexEntryEntry(tabGridManager, 'deleteIndexEntryTabs');
			}	    	
	});

	tabGridManager = UICtrl.grid('#tabGrid', {
        columns: [
            { display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" ,
	        	editor: { type: 'text',required:true} },		   
			{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" ,
		        editor: { type: 'text',required:true} },
			{ display: "数据集", name: "datasetName", width: 260, minWidth: 30, type: "string", align: "left" ,
		        	editor: { type: 'select', 
		        		data: { type: "cboard", name: "selectDataset", 
		        			back: { datasetId: "datasetId", datasetName: "datasetName" }}, 
		        			required: true } 
		    },			
            { display: "省", name: "provinceEnable", width: 80, minWidth: 30,  type: "int", align: "left",
            	 editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            { display: "市", name: "cityEnable", width: 80, minWidth: 30,  type: "int", align: "left",
            	editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            { display: "县", name: "countyEnable", width: 80, minWidth: 30,  type: "int", align: "left",
            	editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
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
        url: web_app.name + '/index/queryIndexEntryTabs.ajax',
        parms: { entryId: getEntryId() },
        usePager: false,
        width: '100%',
        height: '200',
        heightDiff: -8,
        toolbar: toolbarOptions,
        enabledEdit: true,
		sortName: 'sequence',
		sortOrder:'asc',
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onLoadData: function () {
        	return getEntryId()!='';
        }
    });
	
	$('#tabGrid').on('click',function(e){
		var $clicked = $(e.target || e.srcElement);
		if($clicked.hasClass('GridStyle')){
			var id = $clicked.attr("id");
			var rowIndex = $clicked.attr("rowIndex");
			defineTableHeader(id, rowIndex);
		}
	});	
}	

function initEntryDetailDialog(){	
	initFormulaParamGrid();
	initUIParamGrid();
	initTabGrid();
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
} 

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}

