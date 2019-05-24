var counterSignManager =null;
var counterSignKindId, bizId, bizCode, procUnitId, groupId, beginGroupId, endGroupId, procUnitName;
$(document).ready(function () {
	var inputParams=initializeInputParams();
	counterSignManager=$.selectOrgCommon(this,inputParams,$('body'));
	counterSignManager.extend($.counterSignCommon(inputParams));
	counterSignManager.initView();
	
	function initializeInputParams() {
		var inputParams=OpmUtil.getSelectOrgDefaultParams();
	    for (var item in inputParams) {
	        inputParams[item] = Public.getQueryStringByName(item);
	    }

	    counterSignKindId = Public.getQueryStringByName("counterSignKindId");
	    bizId = Public.getQueryStringByName("bizId");
	    bizCode = Public.getQueryStringByName("bizCode");
	    procUnitId = Public.getQueryStringByName("procUnitId");
	    
	    beginGroupId = Public.getQueryStringByName("beginGroupId");
	    endGroupId = Public.getQueryStringByName("endGroupId");
	    procUnitName = Public.getQueryStringByName("procUnitName");
	    
	    switch(counterSignKindId){
	    case CounterSignKind.CHIEF:// 主审中的加减签	
	    	groupId = $("#groupId").val();
	    	if (procUnitId == "Apply") {
	            procUnitId = $("#procUnitId").val();
	        } 
	    	break;
	    case CounterSignKind.MEND://补审	
	    	procUnitId = $("#procUnitId").val();
	        groupId = $("#groupId").val();
	    	break;
	    case CounterSignKind.MANUAL_SELECTION:
	    	break;
	    }
	    return inputParams;
	}
});


(function ($) {
	
	$.counterSignCommon=function(inputParams){
		return new counterSign(inputParams);
	};
	
	function counterSign(inputParams){
		this.inputParams=inputParams;
		this.minusData=[];
	};
	
	$.extend(counterSign.prototype,{
		initializeLayout:function(){//选择框单独布局处理
			var _layoutDom=$('div.orgSelectLayout',this.div);
		    UICtrl.layout(_layoutDom, {leftWidth:5,allowLeftResize:false,allowLeftCollapse:false});
		    var layout=_layoutDom.data('ui_bootstrap_layout'),html=[];
		    html.push('<div class="ui-layout-header-toggle" style="margin-top:-1px;">');
	    	html.push('<button type="button" class="btn btn-success header-btn divAdd" title="添加"><i class="fa fa-angle-double-right"></i></button>&nbsp;&nbsp;');
	    	html.push('<button type="button" class="btn btn-danger header-btn divDelete" title="删除"><i class="fa fa-angle-double-left"></i></button>&nbsp;&nbsp;');
	    	html.push('</div>');
		    layout.left.header.prepend(html.join(''));
		    layout.center.content.addClass('dom-overflow').css('padding',0);
		    layout.center.content.parent().css({'borderWidth':0});
		    var parentWidth=parent.getDefaultDialogWidth();
		    //查询条件显示样式
	    	var conditionCss={left:'60px',width:'160px',top:'2px'};
		    if(parentWidth>=768){
		    	//iframe 内调整网格布局
		        layout.left.removeClass('col-xs-12').addClass('col-xs-5').css('paddingRight','4px');
		        layout.center.removeClass('col-xs-12').addClass('col-xs-7');
		        $('html').addClass('dom-overflow');
		    }else{
		    	//修改顺序
	    		layout.left.insertAfter(layout.center);
	    		layout.center.content.addClass('ui-layout-height-auto').css({height:300,maxHeight:305});
	    		layout.center.height('auto');
	    		$('html').addClass('dom-overflow-auto');
	    		conditionCss={left:'5px',width:'160px',top:'2px'};
		    }
		    //查询条件显示位置调整
    		$('div.queryConditionDiv',this.div).css(conditionCss).removeClass('ui-hide');
		},
		initializeGrid:function(){
			var g=this,handlerGrid=$("div.handlerGrid",g.div);
			var _height='100%',_heightDiff=-67,parentWidth=parent.getDefaultDialogWidth();
		    if(parentWidth < 768){
		    	_height=300;
		    	_heightDiff=0;
		    }
			var showSendMessageColumn = g.inputParams.showSendMessageColumn;
		    var showSendMessage = showSendMessageColumn ? showSendMessageColumn == 1 : false;
		    g.gridManager = UICtrl.grid(handlerGrid, {
		        columns: [
		                  //环节描述
			            { display: "common.field.subprocunitname", name: "subProcUnitName", width: 200, minWidth: 60, type: "string", align: "left",
			                editor: { type: 'text', required: true }
			            },
			            //处理人
			            { display: "common.field.handlername", name: "handlerName", width: 120, minWidth: 60, type: "string", align: "left" },
			            //分组号
			            { display: "common.field.groupid", name: "groupId", width: 60, minWidth: 60, type: "string", align: "left",
			                editor: { type: 'spinner', required: true }
			            },
			            //发送消息
			            { display: "common.field.sendmessage", name: "sendMessage", width: 100, minWidth: 60, type: "string", align: "left",  hide: !showSendMessage,
			            	editor: { type: 'combobox', data: { 0: $.i18nProp('dictionary.yesorno.0'), 1: $.i18nProp('dictionary.yesorno.1') }, required: showSendMessage }, 
			            	render: function(item){
			            		return item.sendMessage == 1 ? $.i18nProp('dictionary.yesorno.1') : $.i18nProp('dictionary.yesorno.0');
			            	}
			            }
			    ],
		        dataAction: 'server',
		        url: web_app.name + '/workflow/queryCounterSignHandler.ajax',
		        parms: { bizId: bizId, procUnitId: procUnitId },
		        pageSize: 20,
		        width: '100%',
		        height: _height,
		        heightDiff: _heightDiff,
		        sortName: 'groupId, sequence',
		        sortOrder: 'asc',
		        usePager: false,
		        fixedCellHeight: true,
		        selectRowButtonOnly: true,
		        enabledEdit: true,
		        rownumbers: true,
		        checkbox: true,
		        autoAddRowByKeydown: false,
		        onLoadData: function () {
		            return !(Public.isBlank(bizId));
		        },
		        //禁止全选
		        onBeforeCheckAllRow: function () {
		            return false;
		        },
		        onBeforeCheckRow: function (checked, data, rowid, rowdata) {
		            return g._checkBeforeCheckRow(data);
		        },
		        onAfterShowData:function(datas){
		        	var _g=this;
		        	$.each(datas.Rows,function(i,data){
		        		var _id=data['__id'],_flag=g._checkBeforeCheckRow(data);
		        		//隐藏复选框
		        		if(!_flag){
		        			$(_g.getRowObj(_id,true)).find('span.l-grid-row-cell-btn-checkbox').hide();
		        		}
		        	});
		        },
		        onBeforeEdit: function (editParm) {
		            //没有分组ID可随意编辑
		            if (Public.isBlank(groupId)) {
		                return true;
		            }

		            var data = editParm.record;
		            //补审
		            if (counterSignKindId == CounterSignKind.MEND && (data.cooperationModelId == "chief" || data.id)) {
		                return false;
		            }

		            var c = editParm.column;
		            if (c.name == 'subProcUnitName' || c.name == "groupId" || c.name == "sequence" || c.name=="sendMessage") {
		                return (parseInt(data.groupId) > parseInt(groupId)) && (data.allowAdd == 1 || data.allowAdd === "");
		            }
		            return false;
		        },
		        onBeforeSubmitEdit: function (e) {
		            if (e.column.name == "groupId") {
		                if (!e.value) {
		                	//请输入分组号。
		                    Public.errorTip("common.warning.groupid.empty");
		                    return false;
		                }
		                if (isNaN(e.value)) {
		                	//请输入数字。
		                    Public.errorTip("common.warning.validator.number");
		                    return false;
		                }
		                
		                if (counterSignKindId == CounterSignKind.MANUAL_SELECTION){
		                	if (parseInt(e.value) < beginGroupId || parseInt(e.value) >= endGroupId){
		                		//分组号必须在“[" + beginGroupId + "," + endGroupId + ")”之间。
		                		Public.errorTip('common.warning.groupid.between',beginGroupId,endGroupId);
		                		return false;
		                	}
		                }else{
		                	if (parseInt(e.value) <= parseInt(groupId)) {
		                		//分组号必须大于“" + groupId + "”。
		                		Public.errorTip('common.warning.groupid.gt',groupId);
		                		return false;
		                	}
		                }
		                return true;
		            }
		        }
		    });
		    //处理人选择不能全选
		    handlerGrid.find('div.l-grid-hd-cell-btn-checkbox').hide();
		},
		_checkBeforeCheckRow:function(data){//判断是否能被选中
			//没有分组ID可随意编辑
            if (Public.isBlank(groupId)) {
                return true;
            }
            if (counterSignKindId == CounterSignKind.MEND && (data.cooperationModelId == "chief" || data.id)) {
                return false;
            }
            //当前审批组后面的可被减签的或者加签的人员可以被减签
            return (parseInt(data.groupId) > parseInt(groupId)) && (data.allowAdd == 1 || data.allowAdd === "");
		},
		deleteData:function(){
			var g=this;
			var rows = g.gridManager.getSelecteds();
		    if (!rows || rows.length < 1) {
		    	//请选择处理人。
		        Public.errorTip('common.warning.handler.empty');
		        return;
		    }
		    parent.UICtrl.confirm($.i18nProp('common.confirm.delete.user'), function () {
		        for (var i = 0; i < rows.length; i++) {
		            if ((parseInt(rows[i].groupId) > parseInt(groupId)) && rows[i].id) {
		                g.minusData.push(rows[i].id);
		            }
		            g.gridManager.deleteRow(rows[i]);
		        }
		        g.gridManager.deletedRows = null;
		    });
		},
		addData:function(rowData){
			var rows = this.getChooseRowData();
		    if (rowData) {
		        rows.push(rowData);
		    }
		    
		    if (!rows || rows.length < 1) {
		    	//请选择左侧组织。
		        Public.tip('common.warning.org.empty');
		        return;
		    }

		    for (var i = 0; i < rows.length; i++) {
		        this.addDataOneNode(rows[i]);
		    }
		},
		addDataOneNode:function(data){
			var g=this,inputParams=g.inputParams;
			if (g.isGroupTreeView()) {
		    	data.id = data.orgNodeId;
		        delete data.groupId;
		    }
		    if (inputParams.selectableOrgKinds.indexOf(data.orgKindId) == -1) {
		        return;
		    }
		    var row,rows = g.gridManager.getData();
		    $.each(rows, function(index, object){
		    	if (parseInt(object.groupId) > parseInt(groupId)){
		    		row = object;
		    		return false;
		    	}
		    });
		    
		    var sequence = g.gridManager.getData().length + 1;
		    var currentGroupId = null;
		    if (row&&row['groupId']) {
		        currentGroupId = row['groupId'];
		    } else {
		        currentGroupId = g.getNextGroupId();
		    }

		    var subProcUnitName;
		    if (CounterSignKind.isManualSelection(counterSignKindId)){
		    	subProcUnitName = procUnitName;
		    }else{
		    	subProcUnitName = data['subProcUnitName'];
		    	if (Public.isBlank(subProcUnitName)) {
		            if (Public.isBlank(data.positionName)) {
		            	subProcUnitName = data.name + $.i18nProp("common.field.approval");
		            } else {
		            	subProcUnitName = data.positionName + $.i18nProp("common.field.approval");
		            }
		        }    	
		    }

		    var status = (counterSignKindId == CounterSignKind.MANUAL_SELECTION || counterSignKindId == CounterSignKind.CHIEF) ? 0 : -2;
		    
		    var cooperationModelId = counterSignKindId;
		    if (counterSignKindId == CounterSignKind.MANUAL_SELECTION){
		    	cooperationModelId = "chief";
		    }
		    
		    g.gridManager.addRow({
		        bizId: bizId,
		        bizCode: bizCode,
		        procUnitId: procUnitId,
		        orgId: data.orgId,
		        orgName: data.orgName,
		        deptId: data.deptId,
		        deptName: data.deptName,
		        positionId: data.positionId,
		        positionName: data.positionName,
		        subProcUnitName: subProcUnitName,
		        handlerName: data.name || data.handlerName,
		        handlerId: data.id || data.handlerId,
		        fullId: data.fullId,
		        fullName: data.fullName,
		        orgKindId: data.orgKindId,
		        status: status, //加减签状态
		        cooperationModelId: cooperationModelId,
		        chiefId: 0,
		        assistantSequence: 0,
		        groupId: currentGroupId,
		        sequence: sequence,
		        sendMessage: 0,
		        allowAdd: 1
		    }, row, true);
		    g.cancelSelect(data);
		},
		getNextGroupId:function(){
			//手工选择
			if (counterSignKindId == CounterSignKind.MANUAL_SELECTION){
				return parseInt(beginGroupId);
			}
		    //var rows = this.gridManager.getData();
		    //var result = 0, currentGroupId;
		    /*
		    for (var i = 0; i < rows.length; i++) {
		    	currentGroupId = parseInt(rows[i].groupId);
		    	if (result < currentGroupId) {
		            result = rows[i].groupId;
		        }
		    }*/
		    //var result = parseInt(groupId) + 5;
		    return parseInt(groupId) + 5;
		},
		checkDuplicateInGroup:function(){// 检查组内人员是否重复
			var rows = this.gridManager.getData();
		    var currentRowId, rowId, currentHandlerId,
		    	handlerId, currentGroupId, groupId,
		    	handlerName, currentPersonId, personId;
		    for (var i = 0; i < rows.length; i++) {
		        currentRowId = rows[i]["__id"];
		        currentHandlerId = rows[i]["handlerId"];
		        currentPersonId = OpmUtil.getPersonIdFromPsmId(currentHandlerId);
		        currentGroupId = rows[i]["groupId"];
		        for (var j = 0; j < rows.length; j++) {
		            rowId = rows[j]["__id"];
		            handlerId = rows[j]["handlerId"];
		            personId = OpmUtil.getPersonIdFromPsmId(handlerId);
		            groupId = rows[j]["groupId"];
		            if (currentRowId != rowId && currentPersonId == personId && currentGroupId == groupId) {
		                handlerName = rows[i]["handlerName"];
		                //"审批分组“" + groupId + "”人员“" + handlerName + "”重复。"
		                Public.errorTip('common.selectorg.warning.grouphandler.repeat',groupId,handlerName);
		                return true;
		            }
		        }
		    }
		    return false;
		},
		checkSingleSelect:function(){
			if (this.inputParams.multiSelect != 1){
		    	if (this.gridManager.getData().length > 1){
		    		//只能选择一个处理人。
		    		Public.errorTip("common.selectorg.warning.moreorg");
		    		return false;
		    	}
		    }
			return true;
		},
		getCounterSignData:function(){
			if (!this.checkSingleSelect()){
		    	return false;
		    }
		    var detailData = DataUtil.getGridData({ gridManager: this.gridManager });
		    if (!detailData) {
		        return false;
		    }

		    if (this.checkDuplicateInGroup()) {
		        return false;
		    }

		    var data = {};
		    data.deleted = Public.encodeJSONURI(this.minusData);
		    data.detailData = Public.encodeJSONURI(detailData);
		    data.bizId = bizId;
		    data.procUnitId = $("#procUnitId").val();
		    data.hiProcUnitHandlerInstVersion = $("#hiProcUnitHandlerInstVersion").val();
		    return data;
		},
		getChooseGridData:function(){
			if (!this.checkSingleSelect()){
		    	return false;
		    }
		    
			var detailData = DataUtil.getGridData({ gridManager: this.gridManager });
		    if (!detailData) {
		        return false;
		    }

		    
		    if (this.checkDuplicateInGroup()) {
		        return false;
		    }

		    return detailData;
		}
	});
	
})(jQuery);

function addDataOneNode(data){
	counterSignManager.addDataOneNode(data);
}

function getChooseGridData(){
	return counterSignManager.getChooseGridData();
}

function getCounterSignData(){
	return counterSignManager.getCounterSignData();
}

function reloadGrid() {
	
}



