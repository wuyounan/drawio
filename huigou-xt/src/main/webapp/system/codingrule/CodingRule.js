var gridManager = null, refreshFlag = false,detailGridManager=null;
var yesOrNo={1:'是',0:'否'};
var pageParam = { detailGridWidth: 850, url: "/easySearch/dictionaryForEnum.ajax" }
$(document).ready(function() {
	initGrid();
	initUI();
});

function initUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId : CommonTreeKind.SerialNumber,
		onClick : onFolderTreeNodeClick
	});
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.SerialNumber){
		parentId="";
		html.push('单据编号列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>单据编号列表');
	}
	$('#layout').layout('setCenterTitle',html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId:parentId});
	}
}

function initGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: addHandler,
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		enableHandler: enableHandler,
		disableHandler: disableHandler,
		moveHandler: moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" },				   
		{ display: "段间分隔符", name: "delimiter", width: 80, minWidth: 60, type: "string", align: "center"},	
		{ display: "新增不允许断号", name: "isAddNoBreak", width: 100, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.isAddNoBreak);
		}},	
		{ display: "新增显示", name: "isAddShow", width: 80, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.isAddShow);
		}},
		{ display: "支持修改", name: "isModifiable", width: 80, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.isModifiable);
		}},
		{ display: "支持断号", name: "isBreakCode", width: 80, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.isBreakCode);
		}},
		{ display: "断号用户选择", name: "isSelectBreakCode", width: 100, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.isSelectBreakCode);
		}},
		{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
			render: function (item) {
				return UICtrl.getStatusInfo(item.status);
			} 
		},
		{ display: "备注", name: "remark", width: 300, minWidth: 60, type: "string", align: "left" }	
		],
		dataAction: 'server',
		url: web_app.name+'/codingRule/slicedQueryCodingRules.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		sortName:'code',
		sortOrder:'asc',
		heightDiff: -8,
		toolbar: toolbarOptions,
		fixedCellHeight: true,
		checkbox: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			updateHandler(data);
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

//添加按钮 
function addHandler() {
	var parentId=$('#treeParentId').val();
	if(parentId=='' || parentId == CommonTreeKind.SerialNumber ){
		Public.errorTip('请选择类别树。');
		return;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/codingRule/showInsertCodingRule.load',
		title: "添加编码规则",
		ok: insert, 
		width: pageParam.detailGridWidth, 
		init: initDialog,
		close: dialogClose
	});
}

//编辑按钮
function updateHandler(data){
	if(!data){
		var rows = gridManager.getSelectedRows();
		if(!rows.length){
			Public.tip('请选择数据。'); 
			return; 
	    }
		if(rows.length>1){
			Public.tip('只能选择一条记录编辑。');
			return;
		}
		data=rows[0];
	}
	
	UICtrl.showAjaxDialog({
		url: web_app.name + '/codingRule/showUpdateCodingRule.load',
		title: "修改编码规则",
		param:{id: data.id},
		width: pageParam.detailGridWidth,
		ok: update, 
		init:initDialog,
		close: dialogClose
	});
}

//删除按钮
function deleteHandler(){
	DataUtil.del({action:'codingRule/deleteCodingRules.ajax',
		gridManager: gridManager, idFieldName: 'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function getId(){
	return $('#id').val();
}

function insert() {
	var id = getId();
	if(id!='') {
		return update();
	}
	var detailData = DataUtil.getGridData({gridManager:detailGridManager});
	if(!detailData) {
		return false;
	}
	if(!checkDetailData()){
		return false;
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/codingRule/insertCodingRule.ajax',
		param:{folderId:$('#treeParentId').val(),detailData:Public.encodeJSONURI(detailData)},
		success: function(id) {
			$('#id').val(id);
			detailGridManager.options.parms['codingRuleId']=id;
			detailGridManager.loadData();
			refreshFlag = true;
		}
	});
}

function checkDetailData(){
	return true;
}

//编辑保存
function update(){
	var detailData = DataUtil.getGridData({ gridManager: detailGridManager, idFieldName: 'id' });
	if(!detailData) {
		return false;
	}
	if(!checkDetailData()){
		return false;
	}
	$('#submitForm').ajaxSubmit({url: web_app.name + '/codingRule/updateCodingRule.ajax',
		param:{ id: getId(), detailData: Public.encodeJSONURI(detailData)},
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

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'移动编码规则',kindId:CommonTreeKind.SerialNumber,
		save:function(parentId){
			DataUtil.updateById({action:'codingRule/moveCodingRules.ajax',
				gridManager:gridManager,idFieldName:'id', param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();	
				}
			});
		}
	});
}

//启用
function enableHandler(){
	DataUtil.updateById({ action: 'codingRule/updateCodingRulesStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: 1},
		message:'确实要启用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

//禁用
function disableHandler(){
	DataUtil.updateById({ action: 'codingRule/updateCodingRulesStatus.ajax',
		gridManager: gridManager,idFieldName:'id', param:{status: 0},
		message: '确实要禁用选中数据吗?',
		onSuccess:function(){
			reloadGrid();	
		}
	});		
}

function initDialog(doc){
	var id=$('#id').length>0?$('#id').val():'';
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: function(){
			var defaultValues = {
					sequence: detailGridManager.getData().length + 1,
					isSortByItem: 0,
					isUseDelimiter: 0,
					isDisplay: 1
				};
			UICtrl.addGridRow(detailGridManager, defaultValues);
		},
		deleteHandler: function(){
			DataUtil.delSelectedRows({action:'codingRule/deleteCodingRuleDetails.ajax',
				gridManager:detailGridManager,idFieldName:'id',
				onSuccess:function(){
					detailGridManager.loadData();
				},
				param: {
					codingRuleId: $('#id').val()
				}
			});
		}
	});
	var param={codingRuleId: id};
	param[gridManager.options['pagesizeParmName']]=1000;
	
	detailGridManager=UICtrl.grid('#detailGrid', {
		 columns: [
				{ display: "排序号", name: "sequence", width: 80, minWidth: 60, type: "string", align: "left",
			        	editor: { type:'spinner', required: true, min: 1, max: 100, mask: 'nnn'}
				},
		        { display: "属性类别", name: "attributeKindTextView", width: 100, minWidth: 60, type: "string", align: "left",
					editor: { type: 'dictionary', data: { url: pageParam.url, param:{ enumKind: 'mdAttributeKind' } }, 
						      textField: 'attributeKindTextView', valueField: 'attributeKind',
						      required: true }
		        },			        	
				{ display: "值属性", name: "attributeValue", width: 250, minWidth: 60, type: "string", align: "left",
					editor: { type: 'text' }
				},
		        { display: "属性使用方式", name: "attributeUseKindTextView", width: 84, minWidth: 60, type: "string", align: "left",
					editor: { type: 'dictionary', data: { url: pageParam.url, param:{ enumKind: 'mdAttributeUseKind' }, emptyOption: true }, textField: 'attributeUseKindTextView', valueField: 'attributeUseKind' }
				},
		        { display: "显示格式", name: "formatTextView", width: 80, minWidth: 60, type: "string", align: "left",
					editor: { type: "dynamic", getEditor: function (row){
						var result = false;
						if (row.attributeKind == "FIXED_VALUE" || row.attributeKind  == "SYSTEM_TIME"){
	                       	switch(row.attributeKind){
	                       	case "FIXED_VALUE":
	                       		result = {type: 'text', required: true};
	                       		break;
	                       	case "SYSTEM_TIME":
	                       		result = { type: 'dictionary', 
                      			     data: { url: pageParam.url, param:{ enumKind: 'mdFormat' }, emptyOption: true }, 
                       			     textField: 'formatTextView', 
                       			     valueField: 'format' 
                       			   };
	                       	}
	                    }
						return result;
	                }},
	                render: function(item){
	                	if (item.attributeKind == "FIXED_VALUE"){
	                		if (item["__status"] == "add"  || item["__status"] == "update"){
	                			item.format = item.formatTextView;
	                		}else{
	                			item.formatTextView = item.format;
	                		}
	                	}
	                	return item.formatTextView;
	                }
				},
				{ display: "步长", name: "step", width: 80, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'},
		        	render: function(item){
		        		return covertIntDisplay(item.step);
		        	}
				},
				{ display: "初始值", name: "initialValue", width: 80, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'},
		        	render: function(item){
		        		return covertIntDisplay(item.initialValue);
		        	}
				},
				{ display: "长度", name: "length", width: 80, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'},
		        	render: function(item){
		        		return covertIntDisplay(item.length);
		        	}
				},
				{ display: "补位符", name: "fillSign", width: 80, minWidth: 60, type: "string", align: "left",
					editor: { type: 'text' }
				},
				{ display: "补位方向", name: "fillSignDirectionTextView", width: 100, minWidth: 60, type: "string", align: "left",
					editor: { type: 'dictionary', data: { url: pageParam.url, param:{ enumKind: 'mdDirectionKind' }, emptyOption: true }, textField: 'fillSignDirectionTextView', valueField: 'fillSignDirection' }
				},
				{ display: "截取位置", name: "interceptPos", width: 80, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'},
		        	render: function(item){
		        		return covertIntDisplay(item.interceptPos);
		        	}
				},
				{ display: "截取方向", name: "interceptDirectionTextView", width: 100, minWidth: 60, type: "string", align: "left",
					editor: { type: 'dictionary', data: {url: pageParam.url, param:{ enumKind: 'mdDirectionKind' }, emptyOption: true }, textField: 'interceptDirectionTextView', valueField: 'interceptDirection' }	 
				},
				{ display: "截取长度", name: "interceptLength", width: 80, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'},
		        	render: function(item){
		        		return covertIntDisplay(item.interceptLength);
		        	}
				},
				{ display: "是否可见", name: "isDisplay", width: 80, minWidth: 60, type: "string", align: "left",
					editor: { type:'combobox',data:yesOrNo},
					render: function (item) { 
						return UICtrl.getStatusInfo(item.isDisplay);
					}
				},
				{ display: "是否段间分隔符", name: "isUseDelimiter", width: 100, minWidth: 60, type: "string", align: "left",
					editor: { type:'combobox',data:yesOrNo},
					render: function (item) { 
						return UICtrl.getStatusInfo(item.isUseDelimiter);
					}
				},
				{ display: "是否分类排序", name: "isSortByItem", width: 80, minWidth: 60, type: "string", align: "left",
					editor: { type:'combobox',data:yesOrNo},
					render: function (item) { 
						return UICtrl.getStatusInfo(item.isSortByItem);
					}
				},
				{ display: "描述", name: "description", width: 160, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'text'}
				}
		],
		dataAction: 'server',
		url: web_app.name+'/codingRule/slicedQueryCodingRuleDetails.ajax',
		parms:param,
		width: '100%', 
		height: '260',
		sortName:'sequence',
		sortOrder:'asc',
		heightDiff: -8,
		toolbar: toolbarOptions,
		enabledEdit: true,
		usePager: true,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onLoadData: function(){
			return $('#id').val()!='';
		},
		onBeforeEdit: function(editParm,rowcell){
			var column = editParm.column;
			var record = editParm.record;
			if (column.columnname == "formatTextView" && record.attributeKind != "SYSTEM_TIME" && record.attributeKind != "FIXED_VALUE"){
				return false;
			}
			return true;
	    }
	});
	
	function covertIntDisplay(fieldValue){
		return fieldValue ? fieldValue : "";
	}
}