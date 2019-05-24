var gridManager = null, refreshFlag = false;
$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
		kindId: CommonTreeKind.i18nProperties,
		onClick: onFolderTreeNodeClick
	});
}

function onFolderTreeNodeClick(data,folderId) {
	var html=[],parentId=folderId;
	if(folderId==CommonTreeKind.i18nProperties){
		parentId="";
		html.push($.i18nProp('system.i18nProperties.treetitle'));
	}else{
		html.push('<span class="tomato-color">[',data.name,']</span>',$.i18nProp('system.i18nProperties.gridtitle'));
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
		moveHandler:moveHandler,
		syncCache:{id:'syncCache',text:$.i18nProp('common.button.sync',$.i18nProp('common.i18nProperties.title')),img:'fa-link',click:function(){
			Public.ajax(web_app.name + "/i18nProperties/syncCacheI18nproperties.ajax");
		}},
		initData:{id:'initData',text:'common.button.addinit',img:'fa-user-circle-o',click:function(){
			showInitData();
		}},
		exportExcelHandler: function(){
	        UICtrl.gridExport(gridManager);
	    },
	    saveExcelImpManager: {id: 'saveExcelImpManager', text: '导入Excel', img: 'fa-cloud-upload'}
	});
	var columns=[{ display: "common.field.code", name: "code", width: 300, minWidth: 60, type: "string", align: "left" }];
	$('div.i18nLanguage').each(function(){
		columns.push({display:$(this).data('name'), name: $(this).data('type'), width: 250, minWidth: 60, type: "string", align: "left" });
	});
	gridManager = UICtrl.grid('#maingrid', {
		columns: columns,
		dataAction: 'server',
		url: web_app.name+'/i18nProperties/slicedQueryI18nproperties.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -5,
		sortName:'code',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			updateHandler(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
	$('#toolbar_menusaveExcelImpManager').uploadButton({
		filetype:['xls','xlsx'],
		param:function(){
	      	return {templateCode:'i18nExcelImp'};
	    },
	    url:web_app.name+'/excelImport/upload.ajax',
	    afterUpload:function(){
	    	reloadGrid();
	    }
	});
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
		Public.tip('common.warning.not.kindtree');
		return;
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/i18nProperties/showInsertI18nproperties.load',
		title:$.i18nProp('common.field.add.title',$.i18nProp('common.i18nProperties.title')),
		width: 410, 
		ok: function(){
			var _self=this;
			$('#submitForm').ajaxSubmit({url: web_app.name + '/i18nProperties/insertI18nproperties.ajax',
				param:{folderId:$('#treeParentId').val()},
				success: function(id) {
					reloadGrid();
					_self.close();
				}
			});
		}
	});
}

function updateHandler(id){
	if(!id){
		var id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
	
	UICtrl.showAjaxDialog({
		url: web_app.name + '/i18nProperties/showUpdateI18nproperties.load',
		title:$.i18nProp('common.field.modif.title',$.i18nProp('common.i18nProperties.title')), 
		param:{id:id}, 
		width: 410,
		ok: function(){
			var _self=this;
			$('#submitForm').ajaxSubmit({url: web_app.name + '/i18nProperties/updateI18nproperties.ajax',
				success: function(id) {
					reloadGrid();
					_self.close();
				}
			});
		}
	});
}

function deleteHandler(){
	DataUtil.del({action:'i18nProperties/deleteI18nproperties.ajax',
		gridManager:gridManager,idFieldName:'id',
		onSuccess:function(){
			reloadGrid();		  
		}
	});
}

function moveHandler(){
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'common.button.move',kindId:CommonTreeKind.i18nProperties,
		save:function(parentId){
			DataUtil.updateById({action:'i18nProperties/moveI18nProperties.ajax',
				gridManager:gridManager,param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});
}

function showInitData(){
	UICtrl.showAjaxDialog({
		url: web_app.name + '/i18nProperties/showInitI18nproperties.load',
		title:$.i18nProp('common.button.addinit'), 
		width: 310,
		init:function(){
			$('#dialogKindTree').commonTree({kindId : CommonTreeKind.i18nProperties,IsShowMenu : false});
		},
		ok: function(){
			var parentId=$('#dialogKindTree').commonTree('getSelectedId');
			if(!parentId){
				Public.tip('common.warning.not.nodetree');
				return false;
			}
			var i18nResourceKind=$('#i18nResourceKind').val();
			if (Public.isBlank(i18nResourceKind)) {
				Public.tip($.i18nProp('common.warning.notnull',$.i18nProp('common.field.kind')));
				return false;
			}
			var _self=this;
			Public.ajax(web_app.name + "/i18nProperties/initI18nproperties.ajax",{folderId:parentId,resourcekind:i18nResourceKind},function(){
				reloadGrid();
				_self.close();
			});
		}
	});
}