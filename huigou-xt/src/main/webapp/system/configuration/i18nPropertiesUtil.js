var I18nPropertiesUtil = {};

I18nPropertiesUtil.gridManager = null;

var i18nGridManager = null;

I18nPropertiesUtil.showDialog = function(resourceKind){
	UICtrl.showAjaxDialog({
		id:'i18nPropertiesUtilDialog',
		url: web_app.name + '/i18nProperties/showUpdateDialog.load',
		title:$.i18nProp('common.field.modif.title','国际化'), 
		param:{resourceKind:resourceKind}, 
		init:function(div){
			I18nPropertiesUtil.initializeI18nDialogUI(div);
		},
		width: getDefaultDialogWidth()*0.8,
		ok:false
	});
};

I18nPropertiesUtil.initializeI18nDialogUI = function(div){
	I18nPropertiesUtil.initializeI18nGrid(div);
	$('#doQuery').on('click',function(e){
		var param = $('form.hg-form',div).formToJSON();
		UICtrl.gridSearch(I18nPropertiesUtil.gridManager, param);
	});
	$('#resetQueryForm').on('click',function(e){
		$('form.hg-form',div).formClean();
	});
};

I18nPropertiesUtil.initializeI18nGrid = function(div) {
	var resourceKind=$('#resourceKind',div).val();
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		updateHandler: function(){
			I18nPropertiesUtil.updateHandler();
		},
		deleteHandler: function(){
			I18nPropertiesUtil.deleteHandler();
		},
		syncCache:{id:'syncCache',text:$.i18nProp('common.button.sync',$.i18nProp('common.i18nProperties.title')),img:'fa-link',click:function(){
			Public.ajax(web_app.name + "/i18nProperties/syncCacheI18nproperties.ajax");
		}},
		initData:{id:'initData',text:'common.button.addinit',img:'fa-user-circle-o',click:function(){
			I18nPropertiesUtil.showInitData(resourceKind);
		}}
	});
	var columns=[{ display: "common.field.code", name: "code", width: 300, minWidth: 60, type: "string", align: "left" }];
	$('div.i18nLanguage',div).each(function(){
		columns.push({display:$(this).data('name'), name: $(this).data('type'), width: 250, minWidth: 60, type: "string", align: "left" });
	});
	I18nPropertiesUtil.gridManager = UICtrl.grid($('div.grid',div), {
		columns: columns,
		dataAction: 'server',
		url: web_app.name+'/i18nProperties/slicedQueryI18nproperties.ajax',
		parms :{resourceKind:resourceKind},
		pageSize: 20,
		width: '100%',
		height: '300',
		sortName:'code',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			I18nPropertiesUtil.updateHandler(data.id);
		}
	});
};


I18nPropertiesUtil.reloadGrid=function() {
	I18nPropertiesUtil.gridManager.loadData();
};

I18nPropertiesUtil.updateHandler=function(id){
	if(!id){
		var id = DataUtil.getUpdateRowId(I18nPropertiesUtil.gridManager);
		if (!id){ return; }
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/i18nProperties/showUpdateI18nproperties.load',
		title:$.i18nProp('common.field.modif.title',$.i18nProp('common.i18nProperties.title')), 
		parent :window['i18nPropertiesUtilDialog'],
		param:{id:id}, 
		init:function(div){
			UICtrl.disable($('#resourceKind',div));
			UICtrl.disable($('#code',div));
		},
		width: 410,
		ok: function(){
			var _self=this;
			$('#submitForm').ajaxSubmit({url: web_app.name + '/i18nProperties/updateI18nproperties.ajax',
				success: function(id) {
					I18nPropertiesUtil.reloadGrid();
					_self.close();
				}
			});
		}
	});
}

I18nPropertiesUtil.deleteHandler=function(){
	DataUtil.del({action:'i18nProperties/deleteI18nproperties.ajax',
		gridManager:I18nPropertiesUtil.gridManager,idFieldName:'id',
		onSuccess:function(){
			I18nPropertiesUtil.reloadGrid();		  
		}
	});
}


I18nPropertiesUtil.showInitData=function(resourceKind){
	UICtrl.showAjaxDialog({
		url: web_app.name + '/i18nProperties/showInitI18nproperties.load',
		title:$.i18nProp('common.button.addinit'), 
		parent :window['i18nPropertiesUtilDialog'],
		width: 310,
		init:function(){
			$('#i18nResourceKindDiv').hide();
			$('#dialogKindTree').commonTree({kindId : CommonTreeKind.i18nProperties,IsShowMenu : false});
		},
		ok: function(){
			var parentId=$('#dialogKindTree').commonTree('getSelectedId');
			if(!parentId){
				Public.tip('common.warning.not.nodetree');
				return false;
			}
			var _self=this;
			Public.ajax(web_app.name + "/i18nProperties/initI18nproperties.ajax",{folderId:parentId,resourcekind:resourceKind},function(){
				I18nPropertiesUtil.reloadGrid();
				_self.close();
			});
		}
	});
}