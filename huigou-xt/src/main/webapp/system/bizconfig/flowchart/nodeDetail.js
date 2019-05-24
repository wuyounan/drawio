//节点存在推荐(查询流程对应选择)
BPMCUtil.recommendedWhenSelecting=true;
//推荐时查询和流程图关联的数据
BPMCUtil.getRecommendedWhenSelectingParam=function(){
	return {businessProcessId:geBusinessProcessId()};
};

function initializeEditPageUI(div,node) {
	var buttons=[];
	buttons.push({id:'saveDetail',name:'保 存',icon:'fa-save',event:function(){doSave();}});
	buttons.push({id:'closeDetail',name:'关 闭',icon:'fa-times',event:function(){ BPMCUtil.hideEditAttributePage();}});
    BPMCUtil.createFormButton(buttons);
    Public.autoInitializeUI(div);
    $('input[name="code"]',div).spinner({min:1,max:999}).mask('999',{number:true});
    $('input[name="xaxis"]',div).spinner({min:1,max:100}).mask('99', {number: true});
    $('input[name="yaxis"]',div).spinner({min:1,max:100}).mask('99', {number: true});
    //处理数据
    initData(div,node);
    initNodeFunctionGrid(div,node);
}

function initData(div,node){
	var id=$('input[name="id"]',div).val();
	if(Public.isBlank(id)){
		$('#submitForm',div).formSet(node);
	}
	var param=$('#submitForm',div).formToJSON({encode:false});
	if (Public.isBlank(param['enName'])) {
		$('#enName',div).val(node['enName']);
	}
	if (Public.isBlank(param['functionCode'])) {
		$('#functionCode',div).val(node['functionCode']);
	}
	if (Public.isBlank(param['nodeCode'])) {
		$('#nodeCode',div).val(node['nodeCode']);
	}
	var quoteId=$('input[name="quoteId"]',div);
	if(quoteId.length>0){
		var data=$('#flowChartDiv').flowChart('getShadowNodeDataSource');
		quoteId.combox({data:data});
	}
	
}

function doSave() {
	var formData = $('#submitForm').formToJSON({encode: false});
	if (!formData) return;
	var viewId=formData.viewId;
	formData=$.extend(formData,{id:viewId});
	//节点重复校验
	var flag=$('#flowChartDiv').flowChart('checkNode',formData);
	if(!flag) return;
	var param ={};
	if(window['functionGridManager']){
		var datas=DataUtil.getGridData({gridManager:window['functionGridManager'],isAllData:true});
		var linkKindCodes=$.map(datas,function(d){
			return d.icon;
		});
		param['linkKindCodes']=linkKindCodes.join(',');
	}
	param=$.extend(param,formData,{businessProcessId:geBusinessProcessId()});
	//执行后台保存
	Public.ajax(web_app.name + '/bizFlowChart/saveFlowNode.ajax', param, function(data){
		formData['linkKindCodes']=data['linkKindCodes'];
		//保存后修改图形显示
		$('#flowChartDiv').flowChart('updateNode',formData);
		BPMCUtil.initializePageParameters(true);
	});
}

function initNodeFunctionGrid(div,node){
	var gridDiv=$('div.nodeFunctionGrid',div);
	if(!gridDiv.length){
		window['functionGridManager']=null;
		return;
	}
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: function(){
			UICtrl.showAjaxDialog({
				url: web_app.name + '/bizFlowChart/showInsertProcessNodeFunction.load',
				param:{businessProcessId:geBusinessProcessId(),viewId:$('#viewId',div).val()},
				title: "添加功能",
				width: 400, 
				ok: doSaveNodeFunction
			});
		},
		updateHandler: function(){
			updateNodeFunction();
		},
		deleteHandler: function(){
			DataUtil.del({action:'bizFlowChart/deleteProcessNodeFunction.ajax',
				gridManager:window['functionGridManager'],
				onSuccess:function(){
					window['functionGridManager'].loadData();
				}
			});
		},
		saveSortIDHandler: function(){
			var action = "bizFlowChart/updateProcessNodeFunctionSequence.ajax";
			DataUtil.updateSequence({
			    action: action, gridManager: window['functionGridManager'], onSuccess: function () {
			    	window['functionGridManager'].loadData();
			    }
			});
		}
	});
	
	window['functionGridManager'] = UICtrl.grid(gridDiv, {
		columns: [
		{ display: "图标", name: "icon", width: 60, minWidth: 40, type: "string", align: "center",
			render: function(item) {
				if (!Public.isBlank(item.icon)) {
					return '<span style="font-size:16px;"><i class="fa '+item.icon+'" style="margin-top:10px;"></i></span>';
				}
				return '';
			}
		},
		{ display: "编码", name: "code", width:100, minWidth: 60, type: "string", align: "left" },		   
		{ display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },	
		{
            display: "common.field.sequence", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
            render: function (item) {
                return UICtrl.sequenceRender(item);
            }
        },
		{ display: "连接", name: "url", width: 200, minWidth: 60, type: "string", align: "left" }
		],
		dataAction: 'server',
		url: web_app.name+'/bizFlowChart/queryProcessNodeFunction.ajax',
		parms:{businessProcessId:geBusinessProcessId(),viewId:$('#viewId',div).val()},
		width: '100%',
		height: '100%',
		heightDiff: -45,
		sortName:'sequence',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		checkbox: true,
		usePager:false,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onDblClickRow: function(data, rowindex, rowobj) {
			updateNodeFunction(data.id);
		}
	});
}

function doSaveNodeFunction(div){
	var _self=this;
	$('#saveNodeFunctionForm',div).ajaxSubmit({url: web_app.name + '/bizFlowChart/saveProcessNodeFunction.ajax',
		success : function(id) {
			window['functionGridManager'].loadData();
			_self.close();
		}
	});
}

function updateNodeFunction(id){
	if(!id){
		id = DataUtil.getUpdateRowId(window['functionGridManager']);
		if (!id){ return; }
	}
	UICtrl.showAjaxDialog({
		url: web_app.name + '/bizFlowChart/showLoadProcessNodeFunction.load',
		param:{id:id},
		title: "编辑功能",
		width: 400, 
		ok: doSaveNodeFunction
	});
}
