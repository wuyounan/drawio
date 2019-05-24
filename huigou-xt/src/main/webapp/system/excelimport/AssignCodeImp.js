var ExcelImpManager=function(div,param){
	this.element=div;
	this.param=param||{};
	this.templateId=$('#templetId',div).val();
	this.batchNumber=$('#batchNumber',div).val();
	this.refreshFlag=false;
	this.gridManager=null;
};

//打开导入数据对话框
ExcelImpManager.showImpDialog=function(title,code,bizId,closeFunction){
	if (Public.isBlank(code)) {
		Public.tip('导入模板编码不能为空!');
        return false;
    }
	if (Public.isBlank(bizId)) {
		Public.tip('导入数据业务ID不能为空!');
        return false;
    }
	UICtrl.showAjaxDialog({
        title: title,
        width: getDefaultDialogWidth(),
        top:20,
        height: 450,
        url: web_app.name + '/excelImport/forwardAssignCodeImpPage.load',
        param:{code:code,batchNumber:bizId},
        init:function(div){
        	var _manager = new ExcelImpManager(div);
        	_manager.init();
        },
        ok:false,
        close: function () {
            if ($.isFunction(closeFunction)) {
                closeFunction.call(this)
            }
        }
    });
};

//打开导入数据对话框上传数据时允许传递参数
ExcelImpManager.showImpParamDialog=function(op){
	var options=op||{};
	if (Public.isBlank(options.code)) {
		Public.tip('导入模板编码不能为空!');
        return false;
    }
	if (Public.isBlank(options.bizId)) {
		Public.tip('导入数据业务ID不能为空!');
        return false;
    }
	UICtrl.showAjaxDialog({
        title: options.title||'导入Excel',
        width: getDefaultDialogWidth(),
        top:20,
        height: 450,
        parent:options.parent,
        url: web_app.name + '/excelImport/forwardAssignCodeImpPage.load',
        param:{code:options.code,batchNumber:options.bizId},
        init:function(div){
        	var _manager = new ExcelImpManager(div,options.param||{});
        	_manager.init();
        },
        ok:false,
        close: function () {
            if ($.isFunction(options.closeFunction)) {
            	options.closeFunction.call(this)
            }
        }
    });
};

$.extend(ExcelImpManager.prototype,{
	_$:function(id){
		return $(id,this.element);
	},
	getTemplateId:function(){
		return this.templateId;
	},
	getBatchNumber:function(){
		return this.batchNumber;
	},
	init:function(){
		this.initializeGrid();
	},
	initUI:function(){
		var _self=this;
		this._$('#toolbar_import').uploadButton({
			filetype:['xls','xlsx'],
			param:function(){
				if(_self.getTemplateId()==''){
		      		Public.errorTip('请选择模板。');
		      		return false;
		      	}
				var _param=$.extend({},_self.param,{templateId: _self.getTemplateId(),batchNumber: _self.getBatchNumber()});
		      	return _param;
		    },
		    url:web_app.name+'/excelImport/upload.ajax',
		    afterUpload:function(){
		    	_self.query();
		    	_self.refreshFlag=true;
		    }
		});
		//初始化状态查询条件
		$('#excelImpManagerStatusListDiv').on('click',function(e){
			var $clicked = $(e.target || e.srcElement),_div=$(this);
			if($clicked.is(':radio')){
				setTimeout(function(){
					var _status=_div.find(':checked').val();
					UICtrl.gridSearch(_self.gridManager,{status:_status});
				},0);
			}
		});
	},
	initGrid:function(columns){
		var _self=this;
		var toolbarOptions={items : [ 
			     {name : '导入数据', id : "import",icon:'fa-cloud-upload'},
			     {name : '导出模板', id : "exporttemplet",icon:'fa-cloud-download',event:function(){
			    	 _self.exportExcel();
				 }},
				 {name : '导出数据',id : "exportfail",icon:'fa-pause-circle',event:function(){
					 _self.doExpImpResult();
				 }},
				 {name : '删除临时数据',id : "doDeleteTemporaryData",icon:'fa-trash',event:function(){
					 _self.deleteTemporaryData();
				 }}
		]};
		this.gridManager = UICtrl.grid(this._$("#impBatchNumberMaingrid"), {
			columns:columns,
			dataAction : 'server',
			url: web_app.name+'/excelImport/slicedQueryExcelImportDetails.ajax',
			parms:{templateId: this.getTemplateId(), batchNumber: this.getBatchNumber()},//查询导入失败数据
			pageSize:20,
			width:'99%',
			height:'345',
			toolbar: toolbarOptions,
			fixedCellHeight : true,
			selectRowButtonOnly : true
		});
		this.initUI();
	},
	initializeGrid:function(){
		var url=web_app.name+'/excelImport/queryExcelImportGridHead.ajax';
		var _self=this,templateId=_self.getTemplateId();
		Public.ajax(url,{templateId:templateId},function(data){
			var rows=data.Rows,colModel=[];//组合表头数据
			$.each(rows,function(i,o){
				colModel.push({display: o['excelColumnName'], name : o['columnName'], width :100, align: 'left'});
			});
			colModel.push({display:'状态', name :'statusTextView', width :60, align: 'left'});
			colModel.push({display:'备注', name :'message', width :120, align: 'left'});
			_self.initGrid(colModel);
		});
	},
	exportExcel:function(){//导出模板
		var templetId= this.getTemplateId();
		var templetName=$('#templetName',this.element).val();
		if(templetId==''){
			Public.tip('请选择数据模板！');
			return;
		}
		var url=web_app.name+'/excelImport/exportExcelTemplate.ajax';
		UICtrl.downFileByAjax(url,{id:templetId},templetName);
	},
	query:function(){
		if(this.gridManager){
			UICtrl.gridSearch(this.gridManager, {});
		}
	},
	doExpImpResult:function(){//导出数据
		if(this.gridManager){
			UICtrl.gridExport(this.gridManager,{exportType:'all'});
		}else{
			alert('未执行查询无法导出！');
		}
	},
	deleteTemporaryData:function(){
		var _self=this,templateId=_self.getTemplateId(),batchNumber=_self.getBatchNumber();
		UICtrl.confirm('您确定执行该操作吗?',function(){
			 Public.ajax(web_app.name+'/excelImport/deleteTemporaryData.ajax',{templateId:templateId, batchNumber:batchNumber},function(data){
				 _self.query();//刷新列表
			 });
		});
	},
	getRefreshFlag:function(){
		return this.refreshFlag;
	}
});

