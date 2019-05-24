var taskGridManager, procUnitHandlerGridManager, hiProcUnitHandlerGridManager, cooperationModelData, statusData;
$(document).ready(function() {
	initializeUI();
	bindEvents();
	//initialize -->init
	initializeData();
	initializeTaskGrid();
	initializeProcUnitHandlerGrid();
	//initializeHIProcUnitHandlerGrid();
			
	function initializeUI() {
		$('#pageTab').tab();
	}

	function initializeData() {
		statusData = { "-2": "补审初始化状态" ,"-1": "已合并", 	"0": "未处理",  1: "已处理" };
		cooperationModelData = { chief: "主审", assistant: "协审", cc: "抄送", mend: "补审" };
	}

	function bindEvents() {
		$("#btnQueryTask").click(function() {
			var bizCode = checkAndGetBizCode();
			if (!bizCode){
				return;
			}
			
			taskGridManager.options.parms.bizCode = bizCode;
			UICtrl.gridSearch(taskGridManager);

			procUnitHandlerGridManager.options.parms.bizCode = bizCode;
			UICtrl.gridSearch(procUnitHandlerGridManager);

			//hiProcUnitHandlerGridManager.options.parms.bizCode = bizCode;
			//UICtrl.gridSearch(hiProcUnitHandlerGridManager);
			});
   }
	
  function initializeTaskGrid() {
		var toolbarParam = { 
		  updateHandler: { id: "updateTaskHandler", text: "更改处理人", click: updateTaskHandler, img: "fa-user-circle" },
		  updateTaskReadyHandler: { id: "ready", text: "未处理", click: updateTaskStatusHandler, img: "fa-flag-o" },
		  updateTaskCompletedyHandler: { id: "completed", text: "已完成", click: updateTaskStatusHandler, img: "fa-flag" },
		  updateTasksDescriptionHandler: { id: "updateTasksDescription", text: "修改主题", click: updateTasksDescription, img: "fa-newspaper-o"},
		  transferHandler: { id: "transfer", text: "交办", click: transfer, img: "fa-user-md"},
		  handTasksHandler: { id: "handTasks", text: "交接任务", click: handTasks, img: "page_user.gif"},
		  timingProcessHandler: { id: "timingProcess", text: "计时处理", img: "fa-clock-o" },
		  abortProcessInstance: { id: "abortProcessInstance", text: "终止流程", click: abortProcessInstanceHandler, img: "fa-trash-o" }
		};
				
		var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarParam);
		
		taskGridManager = UICtrl.grid("#taskGrid", {
			columns: [ 
			            { display: "名称", name: "name", width: 120, minWidth: 60, type: "string", align: "left" }, 
			            { display: "状态", name: "statusName", width: 80, minWidth: 60, type: "string", align: "left" }, 
			            { display: "主题", name: "description", width: 300, minWidth: 60, type: "string", align: "left",
							render: function(item) {
								return "<input type='text' id='txtDescription_" + item.id + "' class='textbox'  style='width:300px;padding:2px;' value='" + item.description + "' />";
							}
			            },
			            { display: "类型", name: "catalogId", width: 80, minWidth: 60, type: "string", align: "left",
			            	render:function(item){
			            		var catalogName = "";
			            		if (item.catalogId == "process"){
			            			catalogName = "流程任务";
			            		}else{
			            			catalogName = "协同任务";
			            		}
			            		return catalogName;				            			
			            	}
			            }, 
			            { display: "任务类型", name: "kindId", width: 80, minWidth: 60, type: "string", align: "left",
			            	render: function(item){return TaskKind.getDisplay(item.kindId) ;} }, 			            
			            { display: "提交人部门", name: "creatorDeptName", width: 80 , minWidth: 60, type: "string", align: "left" },
			            { display: "提交人", name: "creatorPersonMemberName", width: 80 , minWidth: 60, type: "string", align: "left" },
			            { display: "执行人部门", name: "executorDeptName", width: 80 , minWidth: 60, type: "string", align: "left" },
			            { display: "执行人", name: "executorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			    		{ display: "提交时间", name: "startTime", width: 140, minWidth: 60, type: "time", align: "left" },
			            { display: "执行时间", name: "endTime", width: 140, minWidth: 60, type: "time", align: "left" },
			            { display: "计时", name: "needTiming", width: 60, minWidth: 60, type: "string", align: "center",
			            	render: function(item){
			            		var result = "否";
			            		if (item.needTiming == "1"){
			            			result = "是";
			            		}
			            		return result;	
			            	}
			            },
			            { display: "编码", name: "bizCode", width: 120, minWidth: 60, type: "string", align: "left" }
			 ],
			dataAction: "server",
			url: web_app.name + "/processInstanceOperation/queryHiTaskInstsByBizCode.ajax",
			width: "99.8%",
			height: '100%',
			usePager: false,
			toolbar: toolbarOptions,
			rownumbers: true,
			heightDiff: -8,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			enabledEdit: true,
			onDblClickRow: function (data, rowindex, rowobj) {
				var params = (data.executorUrl.indexOf("?") >= 0) ? "&": "?";
				params += "isReadOnly=true&procInstId=" + data.procInstId +"&bizId=" + data.bizId +  "&procUnitId=" + data.taskDefKey + "&taskId=" + data.id;
			    UICtrl.addTabItem({ tabid: data.id, text: data.name, url: data.executorUrl + params });  
		   },
		   onSelectRow: function(data, rowid, rowobj){
		   	  var toolbarManager = $('#taskGrid').prev('div.ui-toolbar').data('ui_tool_bar');
		   	  if (TaskStatus.isReayStatus(data.statusId)){
		   	      toolbarManager.enable("toolbar_menutransfer", "toolbar_menuupdateTask", "toolbar_menucompleted");
		   	      toolbarManager.disable("toolbar_menuready");
		   	  }else{
		   	      toolbarManager.disable("toolbar_menutransfer", "toolbar_menuupdateTask", "toolbar_menucompleted");
		   	      toolbarManager.enable("toolbar_menuready");
		   	  }
		   },
		   onLoadData:function(){
				return $('#bizCode').val()!='';
		  }
		});
		
		initTimingProcessMenu();
	}
	
    function initTimingProcessMenu(){
    	var menu=$('#toolbar_menutimingProcess');
    	
    	menu.contextMenu({
    		width:"100px",
    		eventType:'mouseover',
    		autoHide:true,
    		overflow:function(){
    			var of=menu.offset(),height=menu.height()+2;
    			return {left:of.left,top:of.top+height};
    		},
    		items:[
    			{name:"计时",icon:'signTiming',handler: function (){ signNeedTimeHandler(1)}},
    			{name:"不计时",icon:'signNotTiming',handler: function (){ signNeedTimeHandler(0) }}
    		],
    		onSelect:function(){
    			this._hideMenu();
    		}
    	});
    }
  
	function getHIProcUnitHandlerGridOptions(){
		return  {		
			columns: [ 
			    { name: "procUnitId", display: "流程环节ID", width: 100, minWidth: 60, type: "string", align: "left" }, 
                { name: "subProcUnitName", display: "处理环节", width: 100, minWidth: 60, type: "string", align: "left" },
                { name: "handlerName", display: "处理人", width: 200, minWidth: 60, type: "string", align: "left",
                  render: function(item){
                  	return item.orgName + "." + item.deptName + "." + item.handlerName;
                  }
                  }, 
                { name: "status", display: "状态", width: 60, minWidth: 60, type: "string", align: "left",
                   render: function (item)
                    {
                    	if (item.status == "-2"){
                    		return "补审初始化状态";
                    	}else if (item.status == "-1") {
                        	return "已合并";
                        }else if (item.status == "0") {
                        	return "未处理";
                        }else if (item.status == "1"){
                        	return "已处理";
                        }  else {
                        	return "";
                        }
                    }},
                { name: "sequence", display: "序号", width: 60, minWidth: 60, type: "string", align: "left" },
                { name: "cooperationModelId", display: "协助模式", width: 100, minWidth: 60, type: "string", align: "left",
                   render: function (item)
                    {
                        if (item.cooperationModelId == "chief") {
                        	return "主审";
                        }else if (item.cooperationModelId == "assistant"){
                        	return "协审";
                        }else if (item.cooperationModelId == "mend"){
                        	return " 补审";
                        }else {
                            return "抄送";
                        }
                    }},
                { name: "groupId", display: "分组号", width: 60, minWidth: 60, type: "string", align: "left" },
                { name: "id", display: "id", width: 60, minWidth: 60, type: "string", align: "left" }, 
                { name: "chiefId", display: "主审人ID", width: 60, minWidth: 60, type: "string", align: "left" },
                { name: "approvalRuleHandlerId", display: "审批规则处理人ID", width: 100, minWidth: 60, type: "string", align: "left" },
                { name: "approvalRuleId", display: "审批规则ID", width: 60, minWidth: 60, type: "string", align: "left" },
                { name: "bizCode", display: "编码",  width: 120, minWidth: 60, type: "string", align: "left" },
                { name: "operateKindId", display: "操作类别",  width: 80, minWidth: 60, type: "string", align: "left" },
                { name: "operatorName", display: "操作员",  width: 120, minWidth: 60, type: "string", align: "left" },
                { name: "vers", display: "版本",  width: 120, minWidth: 60, type: "string", align: "left" }
                ],
			dataAction: "server",
			url: web_app.name + "/processInstanceOperation/queryHistoricProcUnitHandlers.ajax",
			checkbox: true,
			usePager: false,
			width: "99.8%",
			height: "100%",
			rownumbers: true,
			heightDiff: -8,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return $('#bizCode').val()!='';
			}
		}
	}
	
	function getProcUnitHandlerGridOptions(){
		var toolbarParam = { addHandler: addProcUnitHandler, 
		saveHandler: saveProcUnitHandler,
		deleteHandler: deleteProcUnitHandler,
		mendHandler: { id: "launchMendTask", text: "发起补审任务", click: launchMendTask, img: "fa-id-card-o" }//,
		//misOperateHandler: { id: "misOperate", text: "误操作处理", img: "action_go.gif" }
		};
		var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarParam);
		return {		
			columns: [ 
			    { name: "procUnitId", display: "流程环节ID", width: 100, minWidth: 60, type: "string", align: "left",
                 editor: { type: 'text', required: true} 
                 }, 
                { name: "subProcUnitName", display: "处理环节", width: 100, minWidth: 60, type: "string", align: "left",
                 editor: { type: 'text', required: true } 
                 }, 
                { name: "orgName", display: "机构名称", width: 100, minWidth: 60, type: "string", align: "left" }, 
                { name: "deptName", display: "部门名称", width: 100, minWidth: 60, type: "string", align: "left" }, 
                { name: "positionName", display: "岗位名称", width: 100, minWidth: 60, type: "string", align: "left" }, 
                { name: "handlerName", display: "人员成员", width: 100, minWidth: 60, type: "string", align: "left",
                   editor: { type: 'select',  data: { type:"sys", name: "orgSelect", 
				   getParam: function(){
				      return { orgKindId: "psm" };
				   }, back:{fullId: "fullId", fullName: "fullName", orgId: "orgId", orgName: "orgName",
					deptId: "deptId", deptName: "deptName", positionId: "positionId", positionName: "positionName",
					id: "handlerId", name: "handlerName" }
				
	        	} }}, 
                { name: "status", display: "状态", width: 60, minWidth: 60, type: "string", align: "left",
                  editor: { type: 'combobox', data: statusData, required: true },
                   render: function (item)
                    {
                    	if (item.status == "-2"){
                    		return "补审初始化状态";
                    	}else if (item.status == "-1") {
                        	return "已合并";
                        }else if (item.status == "0") {
                        	return "未处理";
                        }else if (item.status == "1"){
                        	return "已处理";
                        }  else {
                        	return "";
                        }
                    }},
                { name: "sequence", display: "序号", width: 60, minWidth: 60, type: "string", align: "left",
                 editor: { type: 'spinner', required: true}},
                { name: "cooperationModelId", display: "协助模式", width: 100, minWidth: 60, type: "string", align: "left",
                   editor: { type: 'combobox', data: cooperationModelData, required: true },
                   render: function (item)
                    {
                        if (item.cooperationModelId == "chief") {
                        	return "主审";
                        }else if (item.cooperationModelId == "assistant"){
                        	return "协审";
                        }else if (item.cooperationModelId == "mend"){
                        	return " 补审";
                        }else {
                            return "抄送";
                        }
                    }},
                { name: "groupId", display: "分组号", width: 60, minWidth: 60, type: "string", align: "left",
                    editor: { type: 'spinner', required: true} },
                { name: "id", display: "id", width: 60, minWidth: 60, type: "string", align: "left" }, 
                { name: "chiefId", display: "主审人ID", width: 60, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text' }},
                { name: "approvalRuleHandlerId", display: "审批规则处理人ID", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text'}},
                //{ name: "approvalRuleId", display: "审批规则ID", width: 60, minWidth: 60, type: "string", align: "left",
                //editor: { type: 'text', required: true}},
                { name: "approvalRuleName", display: "审批规则名称", width: 100, minWidth: 60, type: "string", align: "left" }, 
                { name: "bizId", display: "bizId", width: 60, minWidth: 60, type: "string", align: "left" }, 
                { name: "bizCode", display: "编码",  width: 120, minWidth: 60, type: "string", align: "left" },
                { name: "handleTime", display: "处理时间", width: 100, minWidth: 60, type: "string", align: "left" }
			    ],
			dataAction: "server",
			url: web_app.name + "/processInstanceOperation/queryProcunitHandlersByBizCode.ajax",
			checkbox: true,
			usePager: false,
			toolbar: toolbarOptions,
			width: "99.8%",
			height: "100%",
			rownumbers: true,
			heightDiff: -8,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			enabledEdit: true,
			onLoadData:function(){
				return $('#bizCode').val()!='';
			}
		}
	}
	
	function initializeProcUnitHandlerGrid() {
		procUnitHandlerGridManager = UICtrl.grid("#procUnitHandlerGrid", getProcUnitHandlerGridOptions());
		initMisOperateMenu();
	}
	
	function initMisOperateMenu(){
    	var menu=$('#toolbar_menumisOperate');
    	
    	menu.contextMenu({
    		width:"100px",
    		eventType:'mouseover',
    		autoHide:true,
    		overflow:function(){
    			var of=menu.offset(),height=menu.height()+2;
    			return {left:of.left,top:of.top+height};
    		},
    		items:[
    			{name:"备份",icon:'backup',handler:  backupProcUnitHandler },
    			{name:"恢复",icon:'recover',handler: recoverProcUnitHandler }
    		],
    		onSelect:function(){
    			this._hideMenu();
    		}
    	});
    }
	
	function initializeHIProcUnitHandlerGrid() {
		hiProcUnitHandlerGridManager = UICtrl.grid("#hiProcUnitHandlerGrid", getHIProcUnitHandlerGridOptions());
	}
});

function checkTaskStauts(){
	var row = taskGridManager.getSelectedRow();
	if (!row) {
		Public.errorTip("请选择需要更改处理人的数据。");
		return false;
	}
	if (!TaskStatus.isReayStatus(row.statusId)) {
		Public.errorTip("当前任务已完成，不能修改处理人。");
		return false;
	}
	return true;
}

function updateTaskHandler() {
	/*
	if (!checkTaskStauts()){
		return;	
	}
   */
    var row = taskGridManager.getSelectedRow();
	
	var options = {
		params: OpmUtil.getSelectOrgDefaultParams(),
		confirmHandler: function() {
			doUpdateTaskHandlerHandler.call(this, row.id)
		},
		closeHandler: onDialogCloseHandler,
		title: "选择流程任务处理人"
	};
	OpmUtil.showSelectOrgDialog(options);
}

function checkAndGetTaskRow(errorMessage){
	var row = taskGridManager.getSelectedRow();
	if (!row) {
		Public.errorTip(errorMessage);
		return false;
	}
	return row;
}

function updateTaskStatusHandler(id){
	var errorMessage  = "请选择需要更改状态的数据。";
	var row = checkAndGetTaskRow(errorMessage);
	if (!row){
		return;
	}
	
	var params = {};	
	params.taskId = row.id;
	params.oldStatus = row.statusId;
	params.status = id.substring(12);
   
	if (params.oldStatus == params.status){
		Public.errorTip("新旧状态一致，不能更新。");
		return;
	}
	var _self=this;
	Public.ajax(web_app.name + "/processInstanceOperation/updateHistoricTaskInstanceStatus.ajax",
		params, function() {
			_self.close();
			taskGridManager.loadData();
	 });
}

/**
 * 更新流程任务处理人事件处理
 */
function doUpdateTaskHandlerHandler(taskId) {
	var data = this.getSelectedData();
	if (data.length == 0) {
		Public.errorTip("请选流程任务处理人。");
		return;
	}

	var _self = this;

	var params = {};
	params.taskId = taskId;
	params.personMemberId = data[0].id;

	Public.ajax(web_app.name + "/processInstanceOperation/updateTaskHandler.ajax", params, function() {
		_self.close();
		$("#btnQueryTask").click();
	});
}

function deleteTaskHandler() {

}

function onDialogCloseHandler() {

}

function addProcUnitHandler() {
	var data = procUnitHandlerGridManager.getData();
	var procUnitId = "", bizCode = "", bizId = "", approvalRuleId = "", approvalRuleHandlerId = "";
	var nextGroupId = data.length + 1;

	if (data.length > 0) {
		var index = data.length - 1;
		procUnitId = data[index]["procUnitId"];
		bizCode = data[index]["bizCode"];
		bizId = data[index]["bizId"];
		approvalRuleId = data[index]["approvalRuleId"];
		approvalRuleHandlerId = data[index]["approvalRuleHandlerId"];
	}

	procUnitHandlerGridManager.addRow({
				bizId: bizId,
				status: 0,
				procUnitId: procUnitId,
				bizCode: bizCode,
				groupId: nextGroupId,
				sequence: nextGroupId,
				chiefId: "",
				approvalRuleId: approvalRuleId,
				approvalRuleHandlerId: approvalRuleHandlerId,
				cooperationModelId: "chief",
				assistantSequence: 0
			});
}

function doUpdateProcUnitHandlerHandler(procUnitHandlerId) {
	var data = this.iframe.contentWindow.selectedData;
	if (data.length == 0) {
		Public.errorTip("请选环节处理人。");
		return;
	}

	var _self = this;

	var params = {};
	params.procUnitHandlerId = procUnitHandlerId;
	params.personMemberId = data[0].id;

	Public.ajax(web_app.name  + "/processInstanceOperation/updateTaskHandler.ajax", params, function() {
			_self.close();
			procUnitHandlerGridManager.loadData();
	});
}

   
function saveProcUnitHandler() {
	var procUnitHandlerData = DataUtil.getGridData({
				gridManager: procUnitHandlerGridManager
			});
	if (!procUnitHandlerData) {
		return false;
	}
	if(!procUnitHandlerData.length) return;
	Public.ajax(web_app.name + '/processInstanceOperation/saveProcUnitHandlers.ajax', {
				data: Public.encodeJSONURI(procUnitHandlerData)
			}, function() {
				procUnitHandlerGridManager.loadData();
			});
}

function deleteProcUnitHandler() {
	var data = procUnitHandlerGridManager.getSelectedRows();

	for (var i = 0; i < data.length; i++) {
		if (data[i].status != 0) {
			Public.errorTip("处理环节“" + data[i].handleKindName + "”状态不为“未处理”，不能删除。");
			return;
		}
	}
	
	var action = '/processInstanceOperation/deleteProcUnitHandlers.ajax';
	DataUtil.delSelectedRows({action: action,
				gridManager: procUnitHandlerGridManager,
				onSuccess: function() {
					procUnitHandlerGridManager.loadData();
				}
			});
}

function launchMendTask(){	
	var row = procUnitHandlerGridManager.getSelectedRow();
	if (!row) {
		Public.errorTip("请选择需补审的环节。");
		return;
	}

    if (row.length > 1) {
		Public.errorTip("请选择唯一补审环节。");
		return;
	}
	
	if (row.cooperationModelId != "chief"){
		Public.errorTip("请选择主审环节。");
		return;
	}
	
	var params = {};	
	params.bizId = row.bizId;
	params.bizCode =  row.bizCode;
	params.procUnitId = row.procUnitId;
	
	var _self = this;
	
	Public.ajax(web_app.name + "/processInstanceOperation/launchMendTask.ajax",
					params, function() {
						//parent.Public.successTip("您已成功发起补审任务。");
						_self.close();
						//procUnitHandlerGridManager.loadData();
					});
}

function updateTasksDescription(){
	var data = taskGridManager.getData();

	if (!data || data.length < 1) {
		Public.tip("没有数据，不能保存。");
		return;
	}

	var updateRowCount = 0;
	var params = new PublicMap();
	var txtDescription, description;
	for (var i = 0; i < data.length; i++) {
		txtDescription = "#txtDescription_" +data[i].id;
		description = $.trim($(txtDescription).val());
		if (description == "") {
			Public.errorTip("请输入任务主题。");
			$(txtDescription).focus();
			return;
		}
		if (data[i].description != description) {
			params.put(data[i].id, description);
			updateRowCount++;
		}
	}
	
	if (updateRowCount == 0) {
		Public.tip("没有修改任务主题。");
		return;
	}

	Public.ajax(web_app.name + '/processInstanceOperation/updateTasksDescription.ajax', {data: params.toString() }, function(data) {
		taskGridManager.loadData();
	});
};

function transfer() {
	//检查任务状态
	if (!checkTaskStauts()){
		return;	
	}
	
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
    selectOrgParams = $.extend({}, selectOrgParams, { multiSelect: 0, showPosition: 0 });
    var options = { params: selectOrgParams, confirmHandler: doTransfer,
        closeHandler: onDialogCloseHandler, title: "选择交办人员"
    };
    OpmUtil.showSelectOrgDialog(options);
}

function doTransfer() {
    var data = this.getSelectedData();
    if (data.length == 0) {
        Public.errorTip("请选择交办人员。");
        return;
    }

    var _self = this;
   
    var row = taskGridManager.getSelectedRow();
    
    var params = {};
    params.executorId =  data[0].id;
    params.catalogId = row.catalogId;
    params.taskId = row.id;
    params.currentHandleId = row.procUnitHandlerId;
    
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/processInstanceOperation/transmit.ajax',
        param: params,
        success: function () {
            _self.close();
            $("#btnQueryTask").click();
        }
    });
}

function handTasks() {
    UICtrl.showAjaxDialog({ url: web_app.name + "/org/showAdjustOrgDialog.load",
        width: 300, top: 100, 
        ok: doHandTasks, 
        title: "交接任务", 
        init: initShowAdjustOrgSelectOrgDialog, 
        close: function(){
        }
    });
}

function abortProcessInstanceHandler(){
	var bizCode = checkAndGetBizCode();
	if (!bizCode){
		return;
	}
	
	var _self=this;
	Public.ajax(web_app.name + "/processInstanceOperation/abortProcessInstanceByBizCode.ajax",
		{ bizCode: bizCode }, function() {
			_self.close();
			taskGridManager.loadData();
	 });
}

function initShowAdjustOrgSelectOrgDialog() {
    initSelectOrgDialog("#sourceOrgId", "#sourceOrgName");
    initSelectOrgDialog("#destOrgId", "#destOrgName");
}

function initSelectOrgDialog(elId, elName) {
    var orgName = $(elName);
    orgName.orgTree({ filter: "psm", getParam: function () {
            var mode = this.mode;
            if (mode == 'tree') {//更改树的根节点
                return { orgRoot: 'orgRoot', org_kind_id: "ogn,dpt,pos,psm" };
            } else {
            	return { org_kind_id: "ogn,dpt,pos,psm" };
            }
        },
        back: {
            text: orgName,
            value: elId,
            id: elId,
            name: orgName
        }
    });
}

/**
* 初始化选择组织对话框
*/
function initSelectOrgDialog(elId, elName) {
    var orgName = $(elName);
    var filter = 'psm';
    var searchQueryCondition = "org_kind_id in('ogn','dpt','pos','psm')";
    orgName.orgTree({ filter: filter, param: { searchQueryCondition: searchQueryCondition },
        getParam: function () {
            var mode = this.mode;
            if (mode == 'tree') {//更改树的根节点
                return { a: 1, b: 1, orgRoot: 'orgRoot', searchQueryCondition: searchQueryCondition };
            } else {
                var param = { a: 1, b: 1 }, condition = [searchQueryCondition];
                param['searchQueryCondition'] = condition.join('');
                return param;
            }
        },
        back: {
            text: orgName,
            value: elId,
            id: elId,
            name: orgName
        }
    });
}

function doHandTasks(){
	var params = {};
    params.fromPersonId = $("#sourceOrgId").val();
    params.toPsmId = $("#destOrgId").val();

    if (!params.fromPersonId) {
        Public.errorTip("请选择交接人。");
        return;
    }
    
    if (!params.toPsmId) {
        Public.errorTip("请选择接收人。");
        return;
    }

    params.fromPersonId = OpmUtil.getPersonIdFromPsmId (params.fromPersonId);
    if (params.fromPersonId == OpmUtil.getPersonIdFromPsmId(params.toPsmId)) {
        Public.errorTip("交接人和接收人相同。");
        return;
    }
    var _self = this;
    Public.ajax(web_app.name + "/processInstanceOperation/handTasks.ajax", params, function () {
             _self.close();
    });
}

function signNeedTimeHandler(needTiming){	
	//1、check condition
	var errorMessage  = "请选择需计时处理的任务。";
	var row = checkAndGetTaskRow(errorMessage);
	if (!row){
		return;
	}
	//2、assign param
	var param = {taskId: row.id, needTiming: needTiming};

	//3、call service
	Public.ajax(web_app.name + '/processInstanceOperation/updateTaskInstExtensionNeedTiming.ajax', param);
}

function getBizCode() {
	return $("#bizCode").val();
}

function checkAndGetBizCode(){
	var bizCode = getBizCode();
	
	if (Public.isBlank(bizCode)){
		Public.errorTip("请录入单据编号。");
		return false;
	}
	return bizCode;
}

function doMisOperate(kindId){
	var bizCode = checkAndGetBizCode();
	if (!bizCode){
		return;
	}
	
	var action = kindId == "back" ? "backupProcUnitHandler": "recoverProcUnitHandler";
	Public.ajax(web_app.name + '/workflow/' + action + '.ajax',  { bizCode: bizCode });
}

function backupProcUnitHandler(){
	doMisOperate("back");
}

function recoverProcUnitHandler(){
	doMisOperate("recover");	
}