var gridManager = null, refreshFlag = false,detailGridManager=null;
$(document).ready(function() {
	initializeGrid();
	initializeUI();
});
function initializeUI(){
	UICtrl.initDefaultLayout();

	$('#maintree').commonTree({
		kindId: CommonTreeKind.AttachmentConfig,
		onClick: onFolderTreeNodeClick
	});
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.AttachmentConfig){
		parentId="";
		html.push('附件配置列表');
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>附件配置列表');
	}
	$('#layout').layout('setCenterTitle',html.join(''));
	$('#treeParentId').val(folderId);
	if (gridManager) {
		UICtrl.gridSearch(gridManager,{folderId:parentId});
	}
}

function initializeGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: addHandler,
		updateHandler: function(){
			updateHandler();
		},
		deleteHandler: deleteHandler,
		moveHandler:moveHandler
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
		{ display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left" },				   
		{ display: "可删除 ", name: "allowDelete", width: 100, minWidth: 60, type: "string", align: "left",
			render: function (item) { 
				return item.allowDelete == 1 ? '是': '否'; 
			} 
		},
		{ display: "备注", name: "remark", width: 300, minWidth: 60, type: "string", align: "left" }	
		],
		dataAction: 'server',
		url: web_app.name+'/attachment/slicedQuery.ajax',
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
			updateHandler(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
	gridManager.loadData();
} 

function resetForm(obj) {
	$(obj).formClean();
}

function addHandler() {
	var parentId=$('#treeParentId').val();
	if(parentId==''){
		Public.tip('请选择类别树。'); 
		return;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/attachment/showInsert.load',
		title: "添加附件配置",
		ok: saveAttachmentConfig, 
		close: dialogClose,
		width:800, 
		init:initDialog
	});
}

function updateHandler(id){
	if(!id){
		var id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/attachment/showUpdate.load',
		title: "修改附件配置",
		param: { id: id },
		width: 800,
		ok: saveAttachmentConfig, close: dialogClose,init:initDialog
	});
}

function deleteHandler(){
	DataUtil.del({action:'attachment/delete.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function saveAttachmentConfig() {
	var detailData = DataUtil.getGridData({gridManager:detailGridManager});
	if(!detailData) {
		return false;
	}
	
	var isAdded = Public.isBlank(getId());
	var url;
	var param = {};

	param.detailData = Public.encodeJSONURI(detailData);
	if (isAdded){
		url = web_app.name + '/attachment/insert.ajax';
		param.folderId =  $('#treeParentId').val();
	}else{
		url = web_app.name + '/attachment/update.ajax';
	}
	if(!checkDetailData()){
		return false;
	}
	$('#submitForm').ajaxSubmit({url: url, param: param,
		success: function(data) {
			var id = isAdded ? data: getId();
			$('#id').val(id);
			detailGridManager.options.parms['attachmentConfigurationId']=id;
			detailGridManager.loadData();
			refreshFlag = true;
		}
	});
}

function checkDetailData(){
	//校验明细中是否存在重复的成员
	var detailData=DataUtil.getGridData({gridManager:detailGridManager,isAllData:true});
	var flag=true,names={},codes={};
	$.each(detailData,function(i,o){
		if(names[o.name]){
			Public.tip('附件名称['+o.name+']重复，请检查!');
			flag=false;
			return false;
		}else{
			names[o.name]=1;
		}
		if(codes[o.code]){
			Public.tip('附件编码['+o.code+']重复，请检查!');
			flag=false;
			return false;
		}else{
			codes[o.code]=1;
		}
	});
	return flag;
}

function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'移动附件配置',kindId:CommonTreeKind.AttachmentConfig,
		save:function(parentId){
			DataUtil.updateById({action:'attachment/move.ajax',
				gridManager:gridManager,idFieldName:'id',param:{folderId: parentId},
				onSuccess:function(){
					reloadGrid();	
				}
			});
		}
	});
}

function getId(){
	return $('#id').length>0 ? $('#id').val(): '';
}

function initDialog(doc){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: function(){
			//新增时传入detailId -(new Date().getTime())临时主键
			UICtrl.addGridRow(detailGridManager, {isMore:0,colspan:1, sequence: detailGridManager.getData().length + 1});
		},
		deleteHandler: function(){
			DataUtil.delSelectedRows({action:'attachment/deleteDetails.ajax',
				gridManager:detailGridManager,idFieldName:'id',
				onSuccess:function(){
					detailGridManager.loadData();
				},
				param: {
					attachmentConfigurationId:  getId()
				}
			});
		}
	});
	var param={attachmentConfigurationId: getId()};
	param[gridManager.options['pagesizeParmName']]=1000;
	detailGridManager=UICtrl.grid('#attachmentConfigDetailGrid', {
		 columns: [
			     { display: "附件编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left",
			       	editor: { type: 'text',required:true,maxLength:16}
			     },
		         { display: "附件名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left",
		        	editor: { type: 'text',required:true,maxLength:16}
		        },	
		        { display: "允许多个", name: "allowMultiple", width:60, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'combobox',data:{'1':'是','0':'否'}},
		        	render: function (item) { 
						return item.allowMultiple==1?'是':'否'; 
					}
				},
				{ display: "列数", name: "colSpan", width: 50, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:9}
				},
		        { display: "允许文件类型", name: "fileKind", width: 200, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'text',maxLength:128}
				},
		        { display: "排序号", name: "sequence", width: 64, minWidth: 60, type: "string", align: "left",
		        	editor: { type:'spinner',min:1,max:100,mask:'nnn'}
				}
		],
		dataAction: 'server',
		url: web_app.name+'/attachment/slicedQueryDetails.ajax',
		parms:param,
		width: 740,
		sortName:'sequence',
		sortOrder:'asc',
		height: '100%',
		heightDiff: -60,
		toolbar: toolbarOptions,
		enabledEdit: true,
		usePager: true,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onLoadData:function(){
			return $('#id').val() != "";
		}
	});
}