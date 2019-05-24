var gridManager = null,yesorno={ 0: '否', 1: '是' };

$(document).ready(function() {
	initializeUI();
	initializeGrid();
});

function initializeUI(){
	$.getFormButton(
		[
		 	{id:'saveDetail',name:'保 存',icon:'fa-save',event:doSave},
		 	{id:'previewGroup',name:'预 览',icon:'fa-table',event:previewGroup},
			{name:'关 闭',icon:'fa-times',event:closeWindow}
		]
	);
}

function closeWindow(){
	UICtrl.closeCurrentTab();
}

function doSave(){
	var id=$('#id').val();
	if(id==''){
		insert();
	}else{
		var detailData = DataUtil.getGridData({gridManager:gridManager});
		if(!detailData) {
			return false;
		}
		update({details:Public.encodeJSONURI(detailData)});
	}
}

//新增保存
function insert() {
	$('#submitForm').ajaxSubmit({url: web_app.name + '/flexField/insertFlexFieldBizGroup.ajax',
		success : function(data) {
			$('#id').val(data);
			gridManager.options.parms['parentId']=data;
			UICtrl.reloadTabById('FlexFieldGroup');
		}
	});
}

//编辑保存
function update(param){
	$('#submitForm').ajaxSubmit({url: web_app.name + '/flexField/updateFlexFieldBizGroup.ajax',
		param:param,
		success : function() {
			UICtrl.reloadTabById('FlexFieldGroup');
			reloadGrid();
		}
	});
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
} 


function initializeGrid(){
	var id=$('#id').val();
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
		addHandler: function(){}, 
		deleteHandler: deleteFields
	});
	gridManager=UICtrl.grid('#mainGrid', {
		 columns: [
			{ display: "英文名称", name: "fieldName", width: 200, minWidth: 60, type: "string", align: "left" },
			{ display: "中文名称", name: "description", width: 200, minWidth: 60, type: "string", align: "left" },
			{ display: "字段类型", name: "controlTypeTextView", width: 100, minWidth: 60, type: "string", align: "left" },
			{ display: "允许为空", name: "nullable", width: 80, minWidth: 60, type: "string", align: "left", 
				editor: { type: 'combobox', data: yesorno, required: true },
				render: function(item){
		    		return yesorno[item.nullable];
		    	}
			},
			{ display: "是否只读", name: "readOnly", width: 80, minWidth: 60, type: "string", align: "left", 
				editor: { type: 'combobox', data: yesorno, required: true } ,
				render: function(item){
		    		return  yesorno[item.readOnly];
		    	}
			},
			{ display: "是否显示", name: "visible", width: 80, minWidth: 60, type: "string", align: "left", 
				editor: { type: 'combobox', data: yesorno, required: true },
				render: function(item){
		    		return  yesorno[item.visible];
		    	}
			},
			{ display: "标签宽", name: "labelWidth", width: 80, minWidth: 60, type: "number", align: "left",
				editor: { type:'spinner',min:1,max:100,mask:'n'}
			},
			{ display: "控件宽", name: "controlWidth", width: 80, minWidth: 60, type: "number", align: "left",
				editor: { type:'spinner',min:1,max:100,mask:'n'}
			},
			/*{ display: "跨列", name: "colSpan", width: 80, minWidth: 60, type: "number", align: "left",
				editor: { type:'spinner',min:1,max:9,mask:'n'}
			},*/
			{ display: "新行显示", name: "newLine", width: 80, minWidth: 60, type: "string", align: "left", 
				editor: { type: 'combobox', data: yesorno},
				render: function(item){
		    		return  yesorno[item.newLine];
		    	}
			},
			{ display: "排序号", name: "sequence", width: 80, minWidth: 60, type: "number", align: "left",
				editor: { type:'spinner',min:1,max:100,mask:'nnn', required: true}
			}
		],
		dataAction : 'server',
		url: web_app.name+'/flexField/slicedQueryFlexFieldBizGroupFields.ajax',
		parms:{parentId: id,pagesize:1000},
		width : '99%',
		sortName:'sequence',
		sortOrder:'asc',
		height : '100%',
		toolbar: toolbarOptions,
		usePager:false,
		heightDiff : -60,
		checkbox: true,
		enabledEdit: true,
		autoAddRow:false,
		onLoadData :function(){
			return !($('#id').val()=='');
		}
	});
	UICtrl.setSearchAreaToggle(gridManager,$('div.navTitle'),false);
	//注册添加按钮
	initAddHandler();
}

function initAddHandler(){
	$('#toolbar_menuAdd').comboDialog({type:'sys',name:'flexFieldDefine',checkbox:true,showSelected:true,
		dataIndex:'defineId',
		onShow:function(){
			if($('#id').val()==''){
				insert();
				return false;
			}
			return true;
		},
		columnRender:{
			readOnly:function (item) {return "<div class='"+(item.readOnly?"status-enable":"status-disable")+"'/>";},
			visible:function (item) {return "<div class='"+(item.visible?"status-enable":"status-disable")+"'/>";} 
		},
		onChoose: function(dialog){
	    	var rows = this.getSelectedRows(), defineIds=[];
	    	if(!rows.length){
	    		Public.errorTip('请选择扩展字段。');
	    		return;
	    	}
	    	$.each(rows,function(i,o){
	    		defineIds.push(o.defineId);
	    	});
	    	Public.ajax(web_app.name+'/flexField/saveFlexFieldBizGroupFields.ajax',
	    		{id: $('#id').val(), defineIds: $.toJSON(defineIds)},
	    		function(){
	    			gridManager.loadData();
	    			dialog.close();
	    		}
	    	);
	    	return false;
		}
	});
}

//删除扩展字段
function deleteFields(){
	DataUtil.del({action:'flexField/deleteFlexFieldBizGroupFields.ajax',
		param:{id:$('#id').val()},
		gridManager:gridManager,
		onSuccess:function(){
			gridManager.loadData();  
		}
	});
	return false;
}

//预览
function previewGroup(){
	var bizCode=$('#detailBizCode').val();
	var width=getDefaultDialogWidth();
	var height=getDefaultDialogHeight();
	UICtrl.showDialog({title:'预览' + bizCode,width:width,
		content:'<div style="overflow:auto;width:'+(width-20)+'px;height:'+(height-100)+'px;position: relative;"><div id="previewGroupDiv" style="width:100%;"></div></div>',
		init:function(){
			$('#previewGroupDiv').flexField({bizCode: bizCode,onInit:function(){
				//UICtrl.setDisable($('#previewGroupDiv'));
			}});
		},
		ok:function(){
			var values=$('#previewGroupDiv').flexField('getFieldValues', false);
		}
	});
	return false;
}