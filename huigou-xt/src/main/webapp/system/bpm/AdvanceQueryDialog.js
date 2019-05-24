var advanceQueryGridManager, scopeQueryGridManager;

var MergeHandlerManager = MergeHandlerManager || {}; //合并处理人管理器
//排它网关手工选择
var hasGatewayManual;

/**
 * 初始化流转预览表格
 * @param previewHandlerData
 * 预览数据
 */
function initAdvanceQueryGrid(previewHandlerData) {
	hasGatewayManual = previewHandlerData.hasGatewayManual == "true";
	
    MergeHandlerManager.initCanMerge(previewHandlerData.Rows);

    var showMergeHandler = $("#showMergeHandler").val() == "true";

    var hasCanMerge = showMergeHandler && MergeHandlerManager.calculateCanMerge(previewHandlerData.Rows);
    
    var hideOperation = !previewHandlerData.hasSelection && !hasCanMerge;
    
    var subProcUnitNameDisplay =$.i18nProp(hasGatewayManual ? "common.job.advanceQuery.subProcUnitName" : "common.job.advanceQuery.procUnitName");//子环节  环节
    
    var queryHandlerShowField = previewHandlerData.queryHandlerShowField;
    var isShowFulName = QueryHandlerShowFieldKind.isShowFulName(queryHandlerShowField);
    var fullNameColumnWidth = isShowFulName ? 160 : 120;
    
    var columns = [];
    
    if (hasGatewayManual){
    	columns.push({ display: "common.button.choose",remark: "选择", name: "select", width: 60, minWidth: 40, type: "string", align: "left",
    		render: function (item) {
		        if (item.showRadio && item.showRadio == "true")  {
		        	var checked = item.checked == true ? ' checked="true" ' : '';
		        	return '<center><input type="radio" style="height:36px; line-height:36px;" name="selectProcUnit"' 
		        	+ '" value="' + item.id + '" procUnitId="' + item.procUnitId + '"' + checked +' /></center>';
		        }
		    }
		});
    	/*
    	columns.push({ display: "环节", name: "procUnitName", width: 180, minWidth: 60, type: "string", align: "left",
    		render:function(item){
        		if (Public.isNotBlank(item.subProcUnitName)){
        			return item.procUnitName + "." + item.subProcUnitName;
        		}
        		return item.procUnitName;
        	}});*/
    }
    columns = columns.concat([{ display: subProcUnitNameDisplay, name: "subProcUnitName", width: 200, minWidth: 60, type: "string", align: "left",
        render: function (item) {
        	var procUnitName = item.procUnitName + "." +  item.subProcUnitName;
            var result = "";
        	if (HandlerKind.isSelection(item.handleKindCode)) {
                result = '<span class="tomato-color">' + procUnitName + '</span>';
            } else {
            	result = procUnitName;
            }
            var cooperationModelId = item.cooperationModelId;
        	var additionalInfo = cooperationModelId == "cc" ? $.i18nProp("dictionary.cooperationModel.cc") : cooperationModelId == "assistant" ? $.i18nProp("dictionary.cooperationModel.assistant") : "";
        	if (Public.isNotBlank(additionalInfo)){
        		additionalInfo = '<span class="tomato-color">['+ additionalInfo +']</span>';
        	}
        	return result + additionalInfo;
        }
    },
    { display: "common.field.handlername",remark: "办理人", name: "fullName", width: fullNameColumnWidth, minWidth: 60, type: "string", align: "left",
        render: function (item) {
        	if (HandlerKind.isSelection(item.handleKindCode)){
        		if (Public.isBlank(item.fullName) && item.isMustPass == "1"){
        			//必经环节...
        			return '<span class="tomato-color">'+$.i18nProp('common.job.advanceQuery.mustPass')+'</span>';
        		}
        		return item.fullName;
    		}
        	return isShowFulName ? item.deptName + "." + item.handlerName : item.handlerName;
        }
    },
    { display: "common.field.sendmessage",remark: "发送消息", name: "sendMessage", width: 100, minWidth: 60, type: "string", align: "left",
    	editor: { type: 'combobox', data: { 0: $.i18nProp('dictionary.yesorno.0'), 1: $.i18nProp('dictionary.yesorno.1') }, required: true }, 
    	render: function(item){
    		return item.sendMessage == 1 ? $.i18nProp('dictionary.yesorno.1') : $.i18nProp('dictionary.yesorno.0');
    	}
    }]);
    
    if (!hideOperation){
    	columns.push({ display:"common.field.operation",remark: "操作", name: "operation", width: 60, minWidth: 60, type: "string", align: "center",frozen:true,
        	render: function (item){
        		var result = "";
        		if (HandlerKind.isSelection(item.handleKindCode)){
        			result = "<a href='javascript:void(0);' class='GridStyle Selection' HandlerKind ='" + item.handleKindCode + "' rowIndex='" + item.__index + "' procUnitName='" + item.subProcUnitName +"'>"+$.i18nProp('common.button.choose')+"</a>";
        		}
        		if (item.canMerge == "1" && Public.isBlank(item.subProcUnitId) && !hasGatewayManual){
        			var operationalDisplay = MergeHandlerManager.getOperationalDisplay(item.merged);
        			result += "<a href='javascript:void(0);' " + operationalDisplay.style + " class='GridStyle Merging' merged='"+ item.merged + "' rowIndex='" + item.__index + "'>"  + operationalDisplay.display + "</a>"
        		}
        		return result;
        	}
        });
    }
    	
    columns = columns.concat([{ display: "common.job.advanceQuery.merged",remark: "已合并", name: "merged", width: 1, minWidth: 1, type: "string", hide: true},
	            { display: "common.field.groupid",remark: "分组序号", name: "groupId", width: 60, minWidth: 60, type: "string", align: "left" },
	            { display: "common.field.handlername",remark: "处理人", name: "handlerData", width: 1, minWidth: 1, type: "string", align: "left", hide: true}]);
    
    advanceQueryGridManager = UICtrl.grid($("#advanceQueryGrid"),{
        columns: columns,
        dataAction: "local",
        data: previewHandlerData,
        checkbox: false,
        usePager: false,
        width: "100%",
        height: 360,
        heightDiff: -10,
        enabledSort: false,
        autoAddRowByKeydown:false,
        enabledEdit: true,
        rownumbers: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        title:"流程审批规则：" + previewHandlerData.procApprovalRuleFullName,
        onAfterShowData: function (currentData) {
            setTimeout(function () {
                var grid = $("#advanceQueryGrid");
                UICtrl.mergeCell("procUnitName", grid);                
                UICtrl.mergeCell("subProcUnitName", grid);
                UICtrl.setGridRowAutoHeight($('#advanceQueryGrid'));
            }, 100);
        },
        onAfterChangeColumnWidth: function () {
            UICtrl.setGridRowAutoHeight($('#advanceQueryGrid'));
        }
    });
    
    $('#advanceQueryGrid').on('click',function(e){
    	var $clicked = $(e.target || e.srcElement);
		if($clicked.hasClass('Selection')){
			var handlerKind = $clicked.attr('handlerKind');
			var procUnitName = $clicked.attr('procUnitName');
			var rowIndex = $clicked.attr("rowIndex");
			showSelectHandlerDialog(handlerKind, rowIndex, procUnitName);
		}else if ($clicked.hasClass('Merging')){
			var merged = $clicked.attr('merged');
			var rowIndex = $clicked.attr("rowIndex");
			MergeHandlerManager.mergeProcUnitHandler(merged, rowIndex);
		}else if ($clicked.hasClass('SelectionMerging')){
			var merged = $clicked.attr('merged');
			var rowIndex = $clicked.attr("rowIndex");
			var selectedRowIndex = $clicked.attr("selectedRowIndex");
			MergeHandlerManager.mergeProcUnitInnerHandler(merged, rowIndex, selectedRowIndex);
		}
    });
}

function initScopeQueryGrid(scopeData){
	scopeQueryGridManager = UICtrl.grid($("#scopeQueryGrid"),{
        columns: [ { display: "common.field.handlername",remark: "处理人",name: "fullName", width: 360, minWidth: 60, type: "string", align: "left",
        	      render: function(item){
        	    	  return item.deptName + "." + item.handlerName;
        	      }}
		         ],
        dataAction: "local",
        data: scopeData,
        checkbox: true,
        usePager: false,
        width: "100%",
        height: 420,
        enabledSort: false,
        autoAddRowByKeydown:false,
        rownumbers: true,
        heightDiff: -10,
        fixedCellHeight: true,
        selectRowButtonOnly: true
    });
}

function showSelectHandlerDialog(handlerKind, rowIndex, procUnitName){
	if (handlerKind == HandlerKind.MANUAL_SELECTION){
		showManualSelectHandlerDialog(rowIndex, procUnitName)
	}else{
		showScopeSelectHandlerDialog(rowIndex, procUnitName);
	}
}

function showManualSelectHandlerDialog(rowIndex, procUnitName){
	var params = {};
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();

    var additionSelectOrgParams = getAdditionSelectOrgParams();
    
    params.counterSignKindId = CounterSignKind.MANUAL_SELECTION;
    params.procUnitName = procUnitName;
    params.beginGroupId = advanceQueryGridManager.getRow(rowIndex).groupId;
    var nextRow = advanceQueryGridManager.getRow(rowIndex+1);
    if (nextRow){
    	params.endGroupId = nextRow.groupId;
    }else{
        params.endGroupId = params.groupId + 200;
    }
    
    params = $.extend({}, params, selectOrgParams, additionSelectOrgParams, { showPosition: 0 });
   
    UICtrl.showFrameDialog({
        title: 'common.job.advanceQuery.counterSign',
        width: 800,
        height: 380,
        parent: window['advanceQueryDialog'],
        url: web_app.name + '/workflow/showCounterSignDialog.do',
        param: params,
        init:function(){
        	var handlerData = advanceQueryGridManager.getRow(rowIndex).handlerData;
        	if (!handlerData){
        		return;
        	}
        	
			var addFn=this.iframe.contentWindow.addDataOneNode;
			if($.isFunction(addFn)){//初始化已选择列表
				this.iframe.contentWindow.isInitializingData = true;
				
				$.each(handlerData,function(i,d){
					addFn.call(window, d);
				});
				
				this.iframe.contentWindow.isInitializingData = false;
				//刷新列表
				var reloadGrid=this.iframe.contentWindow.reloadGrid;
				if($.isFunction(reloadGrid)){
					reloadGrid.call(window);
				}
			}
		},
        ok: function(){
        	doSelectHandler.call(this, HandlerKind.MANUAL_SELECTION, rowIndex);
        	return false;
        },	
        close: onDialogCloseHandler,
        cancelVal: 'common.button.close',
        okVal:'common.button.ok',
        cancel: true
    });
}

function doSelectHandler(kindId, rowIndex){
	var selectedData = kindId == HandlerKind.MANUAL_SELECTION ? this.iframe.contentWindow.getChooseGridData() : scopeQueryGridManager.getSelectedRows();
    if (!selectedData) {
        return false;
    }
    if (!selectedData.length) {
    	Public.tip('common.warning.handler.empty');
        return false;
    }
    var _self = this;
    
    var selectedProcUnitId = "";
    var selectedProcUnit = $("#advanceQueryGrid input:radio:checked");
    if (selectedProcUnit.length > 0){
    	selectedProcUnitId = $(selectedProcUnit[0]).attr("procUnitId");
    }

    MergeHandlerManager.initCanMerge(selectedData);

    advanceQueryGridManager.updateCell('handlerData', selectedData, rowIndex);

    var data = advanceQueryGridManager.getData();
    
    MergeHandlerManager.initCanMerge(data);
    MergeHandlerManager.calculateCanMerge(data);
    MergeHandlerManager.calculateProcUnitInnerFullName(data);
    
    advanceQueryGridManager.setData({Rows: data});
    advanceQueryGridManager.reload();
    
    if (selectedProcUnitId){
    	$("#advanceQueryGrid input:radio[procUnitId='" + selectedProcUnitId +"']").attr("checked", "checked"); 
    }

	_self.close();
}

/**
 * 显示范围选择处理人对话框
 * @param rowIndex
 * @param procUnitName
 */
function showScopeSelectHandlerDialog(rowIndex, procUnitName){
	var scopeData = { Rows: advanceQueryGridManager.getRow(rowIndex).scopeData };
    UICtrl.showDialog({
     	content:'<div id="scopeQueryGrid"></div>',
     	title: 'common.job.advanceQuery.counterSign',
     	width: 480,
        height: 430,               
        cancel: "common.button.cancel",
        parent: window['advanceQueryDialog'],
        init: function (){
        	initScopeQueryGrid(scopeData);
        },
        ok: function(){
        	doSelectHandler.call(this, HandlerKind.SCOPE_SELECTION, rowIndex);
        	return false;
        }
     });
}

/**
 * 初始化可合并属性
 */
MergeHandlerManager.initCanMerge = function (data){
	var obj;
	for (var i = 0, len = data.length; i < len; i++){
		obj = data[i];
		if (HandlerKind.isSelection(obj.handleKindCode || HandlerKind.PSM)){
			selectedData = obj.handlerData;
			if (selectedData){
				MergeHandlerManager.initCanMerge(selectedData);
			}
		}else{
			data[i].canMerge = "0";
			data[i].merged = "0";
		}
	}
}

/**
 * 合并环节处理人
 */
MergeHandlerManager.mergeProcUnitHandler = function(merged, rowIndex){
	merged = merged == "1" ? "0" : "1";
	advanceQueryGridManager.updateCell('merged', merged, rowIndex);
	advanceQueryGridManager.reload();
}

/**
 *合并环节内部处理人
 */
MergeHandlerManager.mergeProcUnitInnerHandler = function(merged, rowIndex, selectedRowIndex){
	var data = advanceQueryGridManager.getData();
	var selectedData;
	for (var i = 0, outerLen = data.length; i < outerLen; i++){
		if (data[i].__id == rowIndex){
			selectedData = data[i].handlerData;
			for (var j = 0, innerLen = selectedData.length; j < innerLen; j++){
				if (selectedData[j].__id == selectedRowIndex){
					merged = merged == "1" ? "0" : "1"
					selectedData[j].merged = merged;
					MergeHandlerManager.updateProcUnitInnerHandler(selectedData, rowIndex);
				}
			}
		}
	}	
}

MergeHandlerManager.updateProcUnitInnerHandler = function(selectedData, rowIndex){
	var fullName = "", operationalDisplay;
	if (selectedData){
		for(var i = 0, len = selectedData.length; i < len; i++){
			selectedObj = selectedData[i];
			fullName +=  selectedObj.handlerName;
			if (selectedObj.canMerge == "1"){
				operationalDisplay = MergeHandlerManager.getOperationalDisplay(selectedObj.merged);
				fullName += "&nbsp;&nbsp;<a href='javascript:void(0);' " + operationalDisplay.style + " class='GridStyle SelectionMerging' merged='"+ selectedObj.merged + "' rowIndex='" + rowIndex +  "' selectedRowIndex='" + selectedObj.__id + "'>"  + operationalDisplay.display + "</a>"
			}
			
			if (i != len - 1){
				fullName += ","
	    	}
		}
	}
	advanceQueryGridManager.updateCell('handlerData', selectedData, rowIndex);
	advanceQueryGridManager.updateCell('fullName', fullName, rowIndex);
}

MergeHandlerManager.internalCalculateBackwardCanMerge = function(obj, currentPersonId){
	var result = false;
	if (obj.handlerId) {
		var personId = OpmUtil.getPersonIdFromPsmId(obj.handlerId);	
		if (personId == currentPersonId){
			obj.canMerge = "1";
			result = true;
		}
	}
	return result;
}

/**
 * 向后计算处理人的可合并属性
 */
MergeHandlerManager.calculateBackwardCanMerge = function (data, currentIndex, currentPersonId){
	var obj, selectedData, selectedObj, canMerged = false, currentHandlerCanMerged;
	for (var i = currentIndex + 1, l = data.length; i < l; i++){
		obj = data[i];
		
		if (obj.handleKindCode == HandlerKind.SEGMENTATION){
			continue;
		}
		
		if (HandlerKind.isSelection(obj.handleKindCode)){
			selectedData = obj.handlerData;
			if (selectedData){
				for (var j = 0, len = selectedData.length; j < len; j++){
					selectedObj = selectedData[j];
					currentHandlerCanMerged = MergeHandlerManager.internalCalculateBackwardCanMerge(selectedObj, currentPersonId);
					canMerged = canMerged || currentHandlerCanMerged;
				}
			}
		}else{
			currentHandlerCanMerged = MergeHandlerManager.internalCalculateBackwardCanMerge(obj, currentPersonId);
			canMerged = canMerged || currentHandlerCanMerged;
		}
	}
	return canMerged;
}

/**
 * 内部计算处理人的可合并属性
 */
MergeHandlerManager.internalCalculateCanMerge = function(data, index, currentObj){
	if (hasGatewayManual){
		return false;
	}
	
	var canMerge = false;
	if (currentObj.handlerId){
		var currentPersonId = OpmUtil.getPersonIdFromPsmId(currentObj.handlerId);
		canMerge = MergeHandlerManager.calculateBackwardCanMerge(data, index, currentPersonId);
		
		if (canMerge){
			currentObj.canMerge = "1";
		}else{
			currentObj.canMerge = "0";
			currentObj.merged = "0";
		}	
	}
	return canMerge;
}

/**
 * 计算处理人的可合并属性
 A1  A                     B        C      B      D       A1
     A1 A2 A3                                       A1 A3
*/
MergeHandlerManager.calculateCanMerge = function(data){
	var currentObj, currentPersonId, hasCanMerge = false, canMerge, selectedData;
	for (var i = 0, l = data.length; i < l - 1; i++)
    {
		currentObj = data[i];		
		//段不需合并
		if (currentObj.canMerge == "1" || currentObj.handleKindCode == HandlerKind.SEGMENTATION){
			continue;
		}
		//选择处理人
		if (HandlerKind.isSelection(currentObj.handleKindCode)){
			selectedData = currentObj.handlerData;
			if (selectedData){
				for (var j = 0, len = selectedData.length; j < len; j++){
					//currentObj已重新赋值了
					currentObj = selectedData[j];
					if (currentObj.canMerge != "1"){
						canMerge = MergeHandlerManager.internalCalculateCanMerge(data, i, currentObj);
						hasCanMerge = hasCanMerge || canMerge;
					}
				}
			}
		}else{
			canMerge = MergeHandlerManager.internalCalculateCanMerge(data, i, currentObj);
			hasCanMerge = hasCanMerge || canMerge;
		}
    }
	return hasCanMerge;
}

MergeHandlerManager.getOperationalDisplay = function(merged){
	//已合并 未合并
	var display = $.i18nProp(merged == "1" ? "common.job.advanceQuery.merged" : "common.job.advanceQuery.notmerged");            			
	var style = merged == "1" ? "style='color:red;'" : "";
	return { display: display, style: style }; 
}

/**
 * 计算选择处理人的FullName
 */
MergeHandlerManager.calculateProcUnitInnerFullName = function (data){	
	var obj, fullName, selectedData, selectedObj, operationalDisplay;
    for(i = 0, l = data.length; i < l; i++){
    	obj = data[i];
    	if (HandlerKind.isSelection(obj.handleKindCode)){
    		fullName = "";
    		selectedData = obj.handlerData;
			if (selectedData){
				for(var j = 0, len = selectedData.length; j < len; j++){
					selectedObj = selectedData[j];
					fullName +=  selectedObj.handlerName;
					
					if (selectedObj.canMerge == "1"){
						operationalDisplay = MergeHandlerManager.getOperationalDisplay(selectedObj.merged);
						fullName += "&nbsp;&nbsp;<a href='javascript:void(0);' " + operationalDisplay.style + " class='GridStyle SelectionMerging' merged='"+ selectedObj.merged + "' rowIndex='" + obj.__id +  "' selectedRowIndex='" + selectedObj.__id + "'>"  + operationalDisplay.display + "</a>"
					}
					
					if (j != len - 1){
						fullName += ",";
			    	}
				}
			}
			obj.fullName = fullName;
			advanceQueryGridManager.updateCell('fullName', fullName, obj.__id);
    	}
    }
}
/**
 * TODO 是否必经环节的判断
 * 检查合并处理人被全部合并
 */
MergeHandlerManager.checkMergeHandlers = function(data){
	var currentRow, tempRow, currentPersonId, tempPersonId;
	var count;
	var handers=[];
	for(var i = 0, len = data.length; i < len; i++){
		currentRow = data[i];
		currentPersonId = OpmUtil.getPersonIdFromPsmId(currentRow.handlerId);
		//不合并或者已人员检查
		if (currentRow.canMerge == "0" || $.inArray(currentPersonId, handers) > -1){
			continue;
		}
		handers.push(currentPersonId);
		
		if (currentRow.merged == "1"){
			count = 0;
			for(var j = i + 1; j < len; j++){
				tempRow = data[j];
				if (tempRow.canMerge == "0"){
					continue;
				}
				tempPersonId = OpmUtil.getPersonIdFromPsmId(tempRow.handlerId);
				if (currentPersonId == tempPersonId && tempRow.merged == "0"){
					count++;
				}
			}
			if (count == 0){
				//"办理人“" + currentRow.handlerName + "”不能被全部合并。"
				Public.errorTip($.i18nProp('common.job.advanceQuery.merged.error',currentRow.handlerName));
				return false;
			}
		}
	}
	return true;
}

/**
* 得到处理人
*/
function getAdvanceQueryHandlers() {
    var row;
    var data = advanceQueryGridManager.getData();
    if (!MergeHandlerManager.checkMergeHandlers(data)){
    	return;
    }
    for (var i = 0; i < data.length; i++) {
        row = data[i];
        if (HandlerKind.isSelection(row.handleKindCode) && data[i].isMustPass == "1") {
            if (!row.fullName) {
            	//选择节点没有选择办理人，不能提交。
                Public.errorTip("common.job.advanceQuery.person.empty");
                return false;
            }
        }
    }

    var manualProcUnitId;
    if (hasGatewayManual){
    	var $SelectedProcUnites = $("#advanceQueryGrid input:radio:checked");
        if ($SelectedProcUnites.length != 1 ){
        	//您没有选择环节，不能提交。
        	Public.errorTip("common.job.advanceQuery.procUnit.empty")
        	return false;
        }
        
        manualProcUnitId = $($SelectedProcUnites[0]).attr("procUnitId");
    }    

    var result = [];
    for (var i = 0; i < data.length; i++) {
    	if (!hasGatewayManual) {
            result.push(data[i]);
    	}else{
        	if (data[i].procUnitId == manualProcUnitId &&( "true" == data[i].isEndFlowNode || data[i].fullName)) {
        		result.push(data[i]);
        	}
    		
    	}
    }
    $.each(result, function(i, o){
    	delete o.scopeData;
    });
       
    return { hasGatewayManual: hasGatewayManual, manualProcUnitId: manualProcUnitId, handlers: result};
}