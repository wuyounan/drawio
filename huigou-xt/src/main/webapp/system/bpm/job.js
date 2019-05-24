var procInstId, bizId, bizCode, taskId, processDefinitionKey, procUnitId, activityModel, taskKindId, procInstEndTime, taskStatusId ,procInstStatusId;
var isCancelReadHandleResult = false;
var pageHiTaskinstRelations = {}; //关联任务信息
var jobLayoutManager = null;
var freeFlowDesiger;
var writeSignatureDialog;
$(document).ready(function () {
	bfcacheUrl();
	initTaskDetailParameters();
    initializeJobPageUI();
    refreshBtnStatus();
    bindJobPageEvent();//绑定事件
    initFloatHandleDialog(); //初始化快捷处理对话框
    initHiTaskinstRelations(); //初始化关联任务显示
    
    function initTaskDetailParameters() {
        /** 
        * 流程模板编码和流程环节
        */
        processDefinitionKey = getTaskDetailParameter("processDefinitionKey") || $("#processDefinitionKey").val();
        procUnitId = getTaskDetailParameter("taskDefKey") || $("#procUnitId").val();
        activityModel = getTaskDetailParameter("activityModel");
        procInstId = getTaskDetailParameter("procInstId");
        bizId = getTaskDetailParameter("bizId")||$("#jobPageCenterBizId").val();
        bizCode = getTaskDetailParameter("bizCode");
        taskId = getTaskDetailParameter("id");
        taskKindId = getTaskDetailParameter("kindId") || 'task';
        taskStatusId = getTaskDetailParameter("statusId") || TaskStatus.READY;
        procInstEndTime =  getTaskDetailParameter("procInstEndTime");
        procInstStatusId =  getTaskDetailParameter("procInstStatusId")||'completed';
        
        setReadOnlyByTaskStatus();
    }
    
    //当浏览器通过“后退”按钮返回历史页面时，一般浏览器都会保留缓存
    function bfcacheUrl(){
    	if (self != top) {
    		//框架内不处理
    		return;
    	}
    	var url = window.location.href;  
        var ps = url.split("#");  
        try{  
            if(ps[1] != 1){  
                url += "#1";  
            }else{  
                window.location = ps[0];  
            }  
        }catch(ex){  
            url += "#1";  
        }  
        window.location.replace(url);  
    }
    
});
//从页面获取流程参数
function getTaskDetailParameter(key){
	var _el=$('#jobPage'+key.ucFirst());
	if(!_el.length) return null;
	return _el.val(); 
}
//根据任务状体设置只读标志
function setReadOnlyByTaskStatus(){
	if(activityModel=='readonly'){
		Public.isReadOnly=true;
	}
	//任务状态完成,标志为只读
    if(!TaskStatus.isToDoStatus(taskStatusId)){
    	Public.isReadOnly=true;
    }
    activityModel = Public.isReadOnly ? "detail" : "do";
}

function getOpinion(){
	return $.trim($("#handleOpinion").val());
}

function isSupportManuscript(){
	return $("#supportManuscript").val() == "true";
}

function getHandleResult(){
	return parseInt($("#handleResult").val(),10);
}

function getProcessDefinitionKey() {
    return processDefinitionKey;
}

function getProcUnitId() {
    return procUnitId;
}
/**
* 得到子环节ID
*/
function getSubProcUnitId() {
	var _el=$("#currentSubProcUnitId");
	if(!_el.length) return '';
	return _el.val();
}

function setAgreeDefaultOpinion(){
	var handleResult=getHandleResult();
	var opinion = getOpinion();
	var agreeDefaultOpinion = $("#agreeDefaultOpinion").val();
	agreeDefaultOpinion = agreeDefaultOpinion == "null" ? "" : agreeDefaultOpinion;
	switch(handleResult){
	case HandleResult.AGREE:
		if (Public.isNotBlank(opinion)){
			return;
		}
		if (Public.isNotBlank(agreeDefaultOpinion)){
			$("div.jobPageFloatHandleDiv textarea").val(agreeDefaultOpinion);
			$("#handleOpinion").val(agreeDefaultOpinion);
		}
		break;
	case HandleResult.DISAGREE:		
		if (opinion == agreeDefaultOpinion){
			$("div.jobPageFloatHandleDiv textarea").val("");
			$("#handleOpinion").val("");
		}
		break;
	}
}

function getApprovalParams() {
    var approvalParam = {};

    approvalParam.bizId = bizId || getId();
    approvalParam.procUnitId = procUnitId;
    approvalParam.procInstId = procInstId;
    approvalParam.taskKindId = taskKindId;
    approvalParam.taskId = taskId;
    approvalParam.flowKind = getFlowKind();

    approvalParam.currentHandleId = $("#currentHandleId").val();
    approvalParam.currentHandleSequence = $("#currentHandleSequence").val();
    approvalParam.currentHandleGroupId = getCurrentHandleGroupId();
    approvalParam.currentHandleCooperationModelId = $("#currentHandleCooperationModelId").val();
    //撤回任务后，第一个处理人为自己
    if (isApplyProcUnit()) {
        approvalParam.currentHandleSequence = 0;
        approvalParam.currentHandleGroupId = 0;
    }
    var tmpHandleResult=getHandleResult();
    tmpHandleResult=isNaN(tmpHandleResult)?0:tmpHandleResult;
    approvalParam.handleResult = tmpHandleResult;
    approvalParam.handleOpinion = getOpinion();
    
    return approvalParam;
}

function initializeToolBar(){
	//下面的remark如注释信息 没有实际作用
    var toolBarOptions={
    	dropup:$('#toolBar').data('dropup')===true,//显示更多按钮时 是否向上打开菜单
    	items:[{ id: 'save', remark: '保存',name:'common.button.save', icon: 'fa-save',delay:true,relation:'advance', event: save},
	          	  { id: 'advance', remark: '提交',name:'common.button.advance', icon: 'fa-mail-forward',delay:true,relation:'save', event: advance},
	          	  { id: 'back', remark: '回退',name:'common.button.back', icon: 'fa-reply', event: back},
	          	  { id: 'withdrawTask', remark: '回收',name:'common.button.withdraw', icon: 'fa-undo', event: withdrawTask},
	          	  { id: 'recallProcessInstance', remark: '撤销', name :'common.button.recall' , icon: 'fa-reply-all', event: recallProcessInstance},
	              { id: 'transfer', remark: '交办',name:'common.button.transfer', icon: 'fa-user-md', event: transfer},
	          	  { id: 'counterSign', remark: '加减签',name:'common.button.countersign', icon: 'fa-vcard-o', event: counterSign},
	          	  { id: 'assist', remark: '协审',name:'common.button.assist', icon: 'fa-user-plus', event: assist},
	          	  { id: 'makeACopyFor', remark: '知会',name:'common.button.makeacopyfor', icon: 'fa-send-o', event: makeACopyFor},
	          	  { id: 'sleep', remark: '暂缓',name:'common.button.sleep', icon: 'fa-pause-circle', event: sleep },
	          	  { id: 'abort', remark: '终止',name:'common.button.abort', icon: 'fa-stop-circle', event: abort},
	          	  { id: 'relate', remark: '协同关联',name:'common.button.relate', icon: 'fa-link', event: relate},
		          //{ id: 'print', remark: '打印',name:'common.button.print', icon: 'fa-print', event: print },
	              { id: 'showChart', remark: '流程图',name:'common.button.flowchart', icon: 'fa-sitemap', event: showChart},
	          	  { id: 'showApprovalHistory', remark: '流程轨迹',name:'common.button.approvalhistory', icon: 'fa-table', event: showApprovalHistory},
	          	  { id: 'taskCollect', remark: '收藏任务',name:'common.button.taskcollect', icon: 'fa-star', event: saveTaskCollect}
	       ]
    };
    $('#toolBar').toolBar(toolBarOptions);
}

function initializeJobPageUI() {
	initializeJobPageLayout();
	initializeToolBar();
    $("#handleOpinion").placeholder($.i18nProp('common.job.handleopinion')).on('keyup paste cut',function(){
    	var _self=$(this);
        setTimeout(function () {$('div.jobPageFloatHandleDiv textarea').val(_self.val()).trigger('blur');}, 0);
    });
   
    setTimeout(function () {
        if ($('#show_error_info').length > 0) {//错误页面弹出
            $('#toolBar').toolBar('disable');
        }        
    }, 10);
}

//初始化页面布局
function initializeJobPageLayout(){
	UICtrl.layout($("#jobPageLayout"), {heightDiff:-2,topHeight:40,bottomHeight:40,onSizeChanged:function(){
		setHiTaskinstRelationsHeight();
		onJobPageLayoutSizeChanged();
	}});
}
//页面大小改变
function onJobPageLayoutSizeChanged(){
	
}
//添加打印按钮
function addPrintToolButton(){
	$('#toolBar').toolBar('addItem',{ id: 'print', remark: '打印',name:'common.button.print', icon: 'fa-print', event: print });
}
/**
 * 流程类别
 * @return {}
 */
function getFlowKind(){
	return FlowKind.APPROVAL;
}

function getId() {
    return "";
}

function onDialogCloseHandler() {

}

/**
* 检查约束
* 
*/
function checkConstraints() {
    return true;
}

/**
* 启动流程
*/
function startup(operate, fn, showTips) {
    var extendedData = getExtendedData(operate);
    if (extendedData === false) {
        return;
    }
    var params = $.extend({}, {
        processDefinitionKey: processDefinitionKey,
        procUnitId: procUnitId,
        processAction: operate,
        flowKind: getFlowKind()
    }, extendedData);

    if (showTips||showTips===false) {
        params.showTips = showTips;
    }

    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/workflow/startProcessInstanceByKey.ajax',
        param: params,
        async: false,
        success: function (data) {
        	// 新增，给ID赋值
            if (!getId()) {
                setId(data.bizId);

                bizId = data.bizId;
                procInstId = data.procInstId;
                taskId = data.taskId;
            }
            // 直接提交，不预览
            if (operate == ProcessAction.ADVANCE) {
                closeJobPageAndReloadTaskCenter();
            } else {
                reloadGrid();
                refreshBtnStatus();
                afterSave(data.bizData);
                //预览提交
                if ($.isFunction(fn)) {
                    fn.call(window);
                }
            }
        }
    });
}

// 保存
function save(fn, showTips) {

    var extendedData = getExtendedData(ProcessAction.SAVE);
    if (extendedData === false) {
        return false;
    }

    if (beforeSave() == false) {
        return false;
    }

    if (!getId()) {
        startup(ProcessAction.SAVE,fn,showTips);
        return;
    }
    var params = $.extend({}, getApprovalParams(), extendedData);
    if (showTips||showTips===false) {
        params.showTips = showTips;
    }
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/workflow/saveBizData.ajax',
        param: params,
        success: function (data) {
            if ($.isFunction(fn)) {	
                fn.call(window,data);
            } else {
                reloadGrid();
                refreshBtnStatus();
                afterSave(data.bizData);
            }
        }
    });
}

function doQueryAdvance() {
    var data = getAdvanceQueryHandlers();
    
    if (!data || !data.handlers || data.handlers.length == 0) {
        return;
    }

    var extendedData = getExtendedData(ProcessAction.QUERY_ADVANCE);
    if (!extendedData) {
        return;
    }

    var _self = this;

    var params = $.extend({}, getApprovalParams(), extendedData);
    params.procUnitHandlers = Public.encodeJSONURI(data.handlers);

    params.hasGatewayManual = data.hasGatewayManual;
    params.manualProcUnitId = data.manualProcUnitId;
    
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/workflow/queryAdvance.ajax',
        param: params,
        success: function () {
            _self.close();
            closeJobPageAndReloadTaskCenter();
        }
    });
}

function doQueryHandlers() {
    var extendedData = getExtendedData(ProcessAction.QUERY_HANDLERS);
    if (extendedData === false) {
        return;
    }
    
    //显示预览处理人
    var params = $.extend({}, getApprovalParams(), extendedData);
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/workflow/queryHandlers.ajax',
        param: params,
        success: function (data) {
            window['advanceQueryDialog']= UICtrl.showDialog({
             	content:'<div id="advanceQueryGrid"></div><div class="blank_div clearfix"></div>',
             	title: 'common.job.advancedialog',
             	width: 800,
                height: 360,               
                cancel: "common.button.cancel",
                init: function (){
                	initAdvanceQueryGrid(data);
                },
                ok: doQueryAdvance,
                close: onDialogCloseHandler
             });
        }
    });
}

function advanceByShowQueryHandlers(fn) {
    Public.ajax(web_app.name + '/workflow/showQueryHandlers.ajax',
    	{ bizId: bizId, processDefinitionKey: processDefinitionKey, procUnitId: procUnitId, groupId: getCurrentHandleGroupId(), procUnitHandlerId: $("#currentHandleId").val() },
    	function (data) {
    	    if (data == 'true') {
    	        //预览处理人
    	        if (!getId()) {
    	            //启动流程后，弹出预览人
    	            startup(ProcessAction.SAVE, doQueryHandlers, "false");
    	        } else {
    	            //保存数据    	        	
    	            save(doQueryHandlers, "false");
    	        }
    	    } else {
    	        //直接提交，不预览处理人
    	        fn.call(window);
    	    }
    	}
    );
}

/**
 * 检查处理意见
 * @return {Boolean}
 */
function checkHandleOpinion(){
	var approvalParams = getApprovalParams();
    if (!approvalParams.handleResult) {
    	//没有选择处理意见，不能提交。
    	Public.errorTip("common.job.warning.noresult");
    	return false;
    }
    if (approvalParams.handleResult == HandleResult.DISAGREE && !$.trim(approvalParams.handleOpinion)) {
    	Public.errorTip($.i18nProp('common.job.warning.disagree.noresult',HandleResult.getDisplayName(HandleResult.DISAGREE)));
        return false;
    } 
    return true;
}

// 提交
function advance() {
    var confirmAdvance = function () {
    	//您是否提交当前任务？
        UICtrl.confirm("common.job.confirm.advance", function () {
            switch (taskKindId) {
                case TaskKind.TASK:                	
                    if (isApproveProcUnit()) {//审批环节检查审批意见
                        if  (!checkHandleOpinion()){
                        	return;
                        }
                    } else {
                    	//协同任务或者非申请环节的约束检查
                    	if (!(processDefinitionKey && isApplyProcUnit()) && !checkConstraints())
                            return;
                    } 
                	
                    if (!(processDefinitionKey && isApplyProcUnit()) && !checkConstraints()){
                    	return;
                    } 
                    
                    if (processDefinitionKey){//流程任务
                    	doAdvance();
                    }else{//协同任务
                    	doCompleteTask();
                    }
                    
                    break;
                case TaskKind.MEND:
                    if  (!checkHandleOpinion()){
                    	return;
                    }
                    doCompleteMendTask();
                    break;
                case TaskKind.REPLENISH:
                	if (isApplyProcUnit()) {//打回申请环节增加提交校验
                        if  (!checkConstraints()){
                        	return;
                        }
                    }
                    doCompleteReplenishTask();
                    break;
                case TaskKind.NOTICE:
                case TaskKind.MAKE_A_COPY_FOR:
                    doCompleteTask();
                    break;
            }
        });
    };
    
    if (processDefinitionKey && isApplyProcUnit()) {//流程的申请环节
        if (!checkConstraints()) {
            return;
        }
        var formCheckBack = $('#submitForm').formToJSON();
        if (formCheckBack === false) {
            return;
        }
        var extendedData = getExtendedData(ProcessAction.ADVANCE);
        if (extendedData === false) {
            return;
        }

        if (isQueryHandlers()) {
            advanceByShowQueryHandlers(confirmAdvance);
        } else {
            confirmAdvance();
        }
    } else {
    	//流程的其他审批环节
        if (processDefinitionKey) {
        	var _param={bizId: bizId, processDefinitionKey: processDefinitionKey, procUnitId: procUnitId, groupId: getCurrentHandleGroupId(), procUnitHandlerId: $("#currentHandleId").val()};
            Public.ajax(web_app.name + '/workflow/showQueryHandlers.ajax',_param, function (data) {
            	if (data == 'true') {
            		save(doQueryHandlers, "false");
            	}else {    	    	
            		confirmAdvance();
            	}
            });
        } else { //非流程环节
            confirmAdvance();
        }
    }
}

function doCompleteTask() {
    Public.ajax(web_app.name + '/workflow/completeTask.ajax',{ taskId: taskId }, function (data) {
    	closeJobPageAndReloadTaskCenter();
	});
}

function doCompleteMendTask() {
    var params = $.extend({}, getApprovalParams(), { processAction: ProcessAction.ADVANCE });
    Public.ajax(web_app.name + '/workflow/completeMendTask.ajax',params,function (data) {
    	closeJobPageAndReloadTaskCenter();
    });
}

function doCompleteReplenishTask(){
	var extendedData = getExtendedData(ProcessAction.ADVANCE);
    if (!extendedData) {
        return;
    }
	var params = $.extend({}, getApprovalParams(), extendedData);

    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/workflow/completeReplenishTask.ajax',
        param: params,
        success: function () {
            closeJobPageAndReloadTaskCenter();
        }
    });	
}

function getAdvanceAction(){
	return 'workflow/advance.ajax';
}

function doAdvance() {	
	if (!isApplyProcUnit() && !this.getId()){
		//系统错误，没有取到业务ID。
		Public.errorTip("common.job.warning.advance.bizid");
		return;
	}
	
	if (!getId()) {
        startup(ProcessAction.ADVANCE);
        return;
    }

    var extendedData = getExtendedData(ProcessAction.ADVANCE);
    if (!extendedData) {
        return;
    }
    
    var params = $.extend({}, getApprovalParams(), extendedData);
    var url=web_app.name + '/' + getAdvanceAction();
  
    $('#submitForm').ajaxSubmit({
        url: url,
        param: params,
        success: function () {
            closeJobPageAndReloadTaskCenter();
        }
    });
}

/**
* 是否为申请环节
*/
function isApplyProcUnit() {
    return procUnitId == "Apply";
}

/**
 * 流程实例是否完成
 */
function isProcInstCompleted(){
	if($.inArray(procInstStatusId,['deleted','completed','aborted']) > -1){
		return true;
	}
	return !!procInstEndTime;
}

function isApproveProcUnit() {
    return procUnitId == "Approve";
}

function deleteProcessInstance() {
    if (isApplyProcUnit()) {
        UICtrl.confirm("common.job.confirm.advance", function () {
            Public.ajax(web_app.name + '/workflow/deleteProcessInstance.ajax',{ procInstId: procInstId }, function () {
			    closeJobPageAndReloadTaskCenter();
			});
        });
    }
}

//回退
function back() {
	var handleResult=getHandleResult();
	//回退处理结果不能为“{0}”!
	if(handleResult==HandleResult.AGREE){
		Public.errorTip($.i18nProp('common.job.warning.back.disagree',HandleResult.getDisplayName(HandleResult.AGREE)));
		return;
	}
	if (isApproveProcUnit() && !getOpinion()){
		//回退请输入处理意见
		Public.errorTip("common.job.warning.back");
		return;
	}
	showBackDialog({ title: "common.job.backdialog",  confirmHandler: doBack });	
}

function getCurrentHandleGroupId(){
	return $("#currentHandleGroupId").val();
}

function getCanReplenish(){
	return true;
}

function showBackDialog(options){	
	if (Public.isBlank(bizCode)) {
		Public.tip("单据编号为空,无法执行操作!");
		return;
	}
    UICtrl.showFrameDialog({
        title: options.title,
        width: 500,
        height: 380,
        url: web_app.name + '/workflow/showBackQuery.do',
        param: { bizCode: bizCode, procUnitId: procUnitId, groupId: getCurrentHandleGroupId(),canReplenish:getCanReplenish() },
        ok: internalDoBack,
        cancel: true,
        close: onDialogCloseHandler
    });
}

function showFreeFlowBackDialog(){
    UICtrl.showFrameDialog({
        title:  "common.job.backdialog",
        width: 500,
        height: 400,
        url: web_app.name + '/freeFlow/showBackQuery.do',
        param: { bizId: bizId, procInstId: procInstId, taskId: taskId },
        ok: internalDoBack,
        cancel: true,
        close: onDialogCloseHandler
    });
}

function internalDoBack(){
	var backModel = this.iframe.contentWindow.getBackModel();
    if (backModel == "back"){
    	doBack.call(this);    
    }else{
    	doReplenish.call(this);
    }
}

function isBackSaveBizData(){
	return false;
}

function onDefaultSuccess(opt){
	if (opt.dialog){
		opt.dialog.close();
	}
	closeJobPageAndReloadTaskCenter();
}

function doBack() {	
    var data = this.iframe.contentWindow.getBackProcUnitData();
    if (!data || data.length == 0) {
        return;
    }

    var _self = this;

    var approvalParams = getApprovalParams();
    var extendedData = getExtendedData(ProcessAction.BACK);

    var params = $.extend({}, { destActivityId: data[0].taskDefKey, backToProcUnitHandlerId: data[0].procUnitHandlerId },
			approvalParams, extendedData);
    
    params.isBackSaveBizData = isBackSaveBizData();
    
    var url = web_app.name + '/workflow/back.ajax';

    $('#submitForm').ajaxSubmit({
    	isCheck: params.isBackSaveBizData,
        url: url,
        param: params,
        success: function () {
        	onDefaultSuccess({ dialog: _self});
        }
    });
    /*
    if (params.isBackSaveBizData){
        $('#submitForm').ajaxSubmit({
            url: url,
            param: params,
            success: function () {
            	onDefaultSuccess({ dialog: _self});
            }
        });
    }else{
    	Public.ajax(url, params, function () {
    		onDefaultSuccess({ dialog: _self});
    	});
    }*/
}

function doReplenish(){
	var data = this.iframe.contentWindow.getBackProcUnitData();
    if (!data || data.length == 0) {
        return;
    }

    var _self = this;

    var approvalParams = getApprovalParams();
    var extendedData = getExtendedData(ProcessAction.REPLENISH);
    var params  = $.extend({}, { backToTaskId: data[0].id }, approvalParams, extendedData);
    
    params.isBackSaveBizData = isBackSaveBizData();
    
    var url = web_app.name + '/workflow/replenish.ajax';
    $('#submitForm').ajaxSubmit({
    	isCheck: params.isBackSaveBizData,
        url: url,
        param: params,
        success: function () {
        	onDefaultSuccess({ dialog: _self});
        }
    });
    /*
    if (params.isBackSaveBizData){
    	$('#submitForm').ajaxSubmit({
            url: url,
            param: params,
            success: function () {
            	onDefaultSuccess({ dialog: _self});
            }
        });
    }else{
    	var billData = $('#submitForm').formToJSON({ check: false });
    	params = $.extend({}, billData, params);
    	
    	Public.ajax(url, params, function () {
    		onDefaultSuccess({ dialog: _self});
    	});
    }*/
}

//交办
function transfer() {
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
    selectOrgParams = $.extend({}, selectOrgParams, { showSendMessageColumn: 1, multiSelect: 0, showPosition: 0 });
    var options = { params: selectOrgParams, confirmHandler: doTransfer,
        closeHandler: onDialogCloseHandler, title: "common.job.transferdialog"
    };
    //选择交办人员
    OpmUtil.showSelectOrgDialog(options);
}

function doTransfer() {
    var data = this.getSelectedData();
    if (data.length == 0) {
    	//请选择交办人员。
        Public.errorTip("common.job.warning.transfer.empty");
        return;
    }
    
    var _self = this;

    var extendedData = getExtendedData(ProcessAction.TRANSMIT);

    var params = $.extend({}, { executorId: data[0].id,sendMessage:data[0].sendMessage}, getApprovalParams(), extendedData);
    params.catalogId = !!procInstId ? "process" : "task";
    
    //!!procInstId 等价于 procInstId ? true : false;

    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/workflow/transmit.ajax',
        param: params,
        success: function () {
            _self.close();
            closeJobPageAndReloadTaskCenter();
        }
    });
}

function makeACopyFor() {
	if (!taskId) {
        return;
    }
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
    selectOrgParams = $.extend({}, selectOrgParams, { showPosition: 0 });
    var options = { params: selectOrgParams, confirmHandler: doMakeACopyFor,
        closeHandler: onDialogCloseHandler, title: "common.job.makeacopyfordialog"
    };
    //选择知会人员
    OpmUtil.showSelectOrgDialog(options);
}

function sleep(){
    Public.ajax(web_app.name + '/workflow/sleep.ajax',{ taskId:  taskId}, function () {
    	 closeJobPageAndReloadTaskCenter();
    });
}

function doMakeACopyFor() {
    var data = this.getSelectedData();
    if (data.length == 0) {
    	//请选择知会人员。
        Public.errorTip("common.job.warning.makeacopyfor.empty");
        return;
    }
    var _self = this;

    var receivers = $.map(data, function(o){
		return o.id;
	});

    var params = {};
    params.taskId = taskId;
    params.receivers = $.toJSON(receivers);
    Public.ajax(web_app.name + '/workflow/makeACopyFor.ajax',params, function () {
    	_self.close();
	});
}

/**
 * 显示自由流设计器
 */
function showFreeFlowDesiger(){
    if (!getId()){
    	//请先保存表单。
        Public.errorTip("common.warning.bizid.empty");
        return;
    }
    var params = {};

    params.bizId = bizId;
    params.procInstId = procInstId;
    params.procUnitId = procUnitId;
    params.isDoModel = isDoModel();
     
    freeFlowDesiger = UICtrl.showFrameDialog({
        width : 800,
        height : 450,
        url: web_app.name + '/freeFlow/forwardDesigner.do',
        param: params,
        ok : false,
        cancel: false
    });
}

function counterSign() {
	//申请环节 判断流程的当环节是否审批环节，只有审批环节才能加减签
    if (isApplyProcUnit()){    	
    	Public.ajax(web_app.name + '/workflow/getProcessInstanceActiveActivityId.ajax',{ procInstId:  procInstId }, function (data) {
    		if (data.indexOf("Approve") > -1){
    			showCounterSignDialog();
    		}else{
    			//流程实例的当前环节不是审批环节，不能加减签。
    			Public.errorTip("common.job.warning.countersign");
    		}
		});
    }else{
    	 var params = $.extend({}, getApprovalParams());
         params.showTips =  "false";
         params.onlySaveHandlerData = "true";
         Public.ajax(web_app.name + '/workflow/saveBizData.ajax',params, function (data) {
        	 showCounterSignDialog();
         });   	
    }
} 

function showCounterSignDialog(){
	var params = {};
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
    var additionSelectOrgParams = getAdditionSelectOrgParams();

    params.counterSignKindId = "chief";
    params.procInstId = procInstId;
    params.bizId = bizId;
    params.bizCode = bizCode;
    params.procUnitId = procUnitId;
    params.taskKindId = taskKindId;//任务类型ID
    
    params.groupId = getCurrentHandleGroupId();
    
    params = $.extend({}, params, selectOrgParams, additionSelectOrgParams, { showSendMessageColumn: 1, showPosition: 0 });
    
    UICtrl.showFrameDialog({
        title: 'common.job.countersignfordialog',
        width: 850,
        height: 380,
        url: web_app.name + '/workflow/showCounterSignDialog.do',
        param: params,
        ok: doCounterSign,
        close: onDialogCloseHandler,
        cancelVal: 'common.button.close',
        cancel: true
    });
}

function doCounterSign() {
    var params = this.iframe.contentWindow.getCounterSignData();
    if (!params) {
        return;
    }
    params.procUnitHandlerId = $("#currentHandleId").val();
    var _self = this;
    Public.ajax(web_app.name + '/workflow/saveCounterSignHandler.ajax',params, function () {
    	if (isApplyProcUnit()){ 
    		reLoadJobTaskExecutionList(bizId);
		}else{
			reLoadJobTaskExecutionList(bizId, procUnitId);
		}
    	_self.close();
	});
}

/**
* 协审
*/
function assist() {
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
    selectOrgParams = $.extend({},selectOrgParams, {
    	bizId:bizId,
    	procUnitId:procUnitId,
    	chiefId:$("#currentHandleId").val(),
    	showPosition: 0,
    	showSendMessageColumn: 1
    });
    UICtrl.showAjaxDialog({   
        url: web_app.name + "/org/showSelectOrgDialog.load",
        title: 'common.job.assistdialog',
        width: 740,
        height:380,
        ok:function(){
        	doAssist.call(this._selectOrg);
        	return false;
        },
        init: function(div){
        	var _obj=$.selectOrgCommon(this,selectOrgParams,div);
        	var _assist=$.selectAssistCommon(selectOrgParams);
        	_obj.extend(_assist);
        	_obj.initView();
        },
        cancelVal: 'common.button.close',
        cancel: true
    });
}

function doAssist() {
    var params = this.getAssistData();
    if(!params){
    	this.close();
    	return;
    }
    params.bizId = bizId;
    params.taskId = taskId;
    params.flowKind = getFlowKind();
    params.procUnitHandlerId = $("#currentHandleId").val();

    var _self = this;

    Public.ajax(web_app.name + '/workflow/assist.ajax',params, function () {
    	reLoadJobTaskExecutionList(bizId, procUnitId);
    	_self.close();
	});
}

/**
* 是否循环审批环节
*/
function isLoopApprovalProc() {
    return $("#currentHandleAllowAbort").length > 0;
}

// 终止
function abort() {
    //终止任务
    var approvalParams = getApprovalParams();
    if (!procInstId) {
    	//您是否要终止当前任务？
        UICtrl.confirm("common.job.confirm.abort.task", function () {
            Public.ajax(web_app.name + '/workflow/abortTask.ajax',approvalParams, function (data) {
            	closeJobPageAndReloadTaskCenter();
            });
        });
        return;
    }

    //终止流程
    if (!isApplyProcUnit() && isLoopApprovalProc() && approvalParams.handleResult != HandleResult.DISAGREE) {
    	//终止操作，处理意见请选择“不同意”。
        Public.errorTip("common.job.warning.abort");
        return;
    }

    var extendedData = getExtendedData(ProcessAction.ABORT);
    var params = $.extend({}, { procInstId: procInstId }, approvalParams, extendedData);
    //您是否要终止当前流程？
    UICtrl.confirm("common.job.confirm.abort.flow", function () {
        $('#submitForm').ajaxSubmit({
            url: web_app.name + '/workflow/abortProcessInstance.ajax',
            param: params,
            success: function () {
                closeJobPageAndReloadTaskCenter();
            }
        });
    });
}

/**
* 打印
*/
function print() {
	var url = location.href;
	url=url.replace(/\.(job)\?/g,'.print?');
	Public.openPostWindow(url);
}

// 流程图
function showChart() {
    if (!bizId) {
        return;
    }
    var url=web_app.name + '/workflow/showFlowChart.load?bizId='+bizId;
    UICtrl.addTabItem({ tabid:'workFlowChart'+bizId, text:$.i18nProp('common.job.chartdialog'),url:url});  
}

/**
* 显示审批历史
*/
function showApprovalHistory() {
    if (!bizId) {
        return;
    }
    //流程轨迹
    UICtrl.showFrameDialog({
        title: 'common.job.approvalhistory',
        width: getDefaultDialogWidth(),
        height: getDefaultDialogHeight(),
        url: web_app.name + '/workflow/showApprovalHistory.do',
        param: { bizId: bizId },
        resize: true,
        ok: false,
        cancelVal: 'common.button.close',
        cancel: true
    });
}

function getExtendedData(processAction) {
    return {};
}

//保存前的逻辑处理，继续执行返回true，中断处理返回false
function beforeSave() {
    return true;
}

function afterSave(bizData) {

}

function reloadGrid() {

}

/**
* 是否为查看模式
* @returns {Boolean}
*/
//function isDetailModel(){
//	return activityModel == "detail";
//}

/**
* 是否为处理模式
* @returns {Boolean}
*/
function isDoModel() {
    return activityModel == "do" && TaskStatus.isToDoStatus(taskStatusId);
}

/**
* 是否流程任务
* @returns {Boolean}
*/
function isProcessTask() {
    return (taskKindId == TaskKind.TASK);
}

function canSave() {
    return isDoModel() && (isProcessTask() || ( TaskKind.isMendTask(taskKindId)) || TaskKind.isReplenishTask(taskKindId));
}

function canDeleteProcessInstance() {
    return isDoModel() && isApplyProcUnit() && procInstId ;
}

function canBack() {
    return isDoModel() && isProcessTask() && isApproveProcUnit();
}

function canSleep(){
	return activityModel == "do" && isProcessTask() && TaskStatus.isReayStatus(taskStatusId) && isApproveProcUnit();
}

function canReplenish(){
	return canBack() && !isApplyProcUnit();
}

function canAdvance() {
    return isDoModel() && (TaskKind.isAdvance(taskKindId)||TaskKind.isMakeACopyFor(taskKindId));
}

function allowTransfer() {
    return $("#currentHandleAllowTransfer").val() == "1";
}

function canTransfer() {
    return isDoModel() && isProcessTask() && allowTransfer();
}

function allowAdd() {
    return $("#currentHandleAllowAdd").val() == "1";
}

function canAddExecutor() {
	//1、处理的流程任务，配置了允许加减签规则
	//2、申请环节没有结束的流程实例，申请人可以加减签
	//3、完全自由流	
    return !!procInstId && ((isDoModel() && allowAdd()) || (this.isApplyProcUnit() && !isDoModel() && !isProcInstCompleted()));
}

function canMakeACopyFor() {
    return getId();
}

function canAssist() {
    return !isApplyProcUnit() && isDoModel() && isProcessTask();
}

function allowAbort() {
    return ($("#currentHandleAllowAbort").length == 0) || $("#currentHandleAllowAbort").val() == "1";
}

//流程任务允许终止 或者 非流程任务
function canAbort() {
	if (Public.isBlank(taskId) && Public.isBlank(procInstId)){
		return false;
	}
    return isDoModel() && (isProcessTask() && (allowAbort() || isApplyProcUnit())) && (!TaskKind.isReplenishTask(taskKindId)) ;
}

function canRelate() {
    return isDoModel() && (!!taskId);
}

function canShowChart() {
    return !!bizId || TaskKind.isReplenishTask(taskKindId);
}

function canShowApprovalHistory() {
    return canShowChart();
}

function canRejectedReason(){
	return $('#handleOpinion').length > 0;
}

function canWithdrawTask(){
	if(TaskKind.isMakeACopyFor(taskKindId)){
		return false;
	}
	return !!bizId && !isDoModel() && isProcessTask() && !isProcInstCompleted();
}

function canRecallProcessInstance(){
	if (Public.isBlank(taskId) && Public.isBlank(procInstId)){
		return false;
	}
	if(!isApplicantPerson()){
		return false;
	}
	if(TaskKind.isMakeACopyFor(taskKindId)){
		return false;
	}
	return !!bizId && !isDoModel() && (isProcessTask()||TaskKind.isReplenishTask(taskKindId)) && !isProcInstCompleted();
}

//是否是流程申请人打开任务页面
function isApplicantPerson(){
	var isApplicantPerson=getTaskDetailParameter('isApplicantPerson');
	if(!isApplicantPerson) return false;
	if(isApplicantPerson!='true') return false;
	return true;
}

function refreshBtnStatus() {
    if (canSave()) {
        $("#toolBar").toolBar("enable", "save");
    } else {
        $("#toolBar").toolBar("disable", "save");
    }

    if (canDeleteProcessInstance()) {
        $("#toolBar").toolBar("enable", "deleteProcessInstance");
    } else {
        $("#toolBar").toolBar("disable", "deleteProcessInstance");
    }

    if (canBack()) {
        $("#toolBar").toolBar("enable", "back");
    } else {
        $("#toolBar").toolBar("disable", "back");
    }

    if (canReplenish()){
        $("#toolBar").toolBar("enable", "replenish");
    } else {
        $("#toolBar").toolBar("disable", "replenish");
    }
    
    if (canAdvance()) {
        $("#toolBar").toolBar("enable", "advance");
    } else {
        $("#toolBar").toolBar("disable", "advance");
    }

    if (canTransfer()) {
        $("#toolBar").toolBar("enable", "transfer");
    } else {
        $("#toolBar").toolBar("disable", "transfer");
    }

    if (canAddExecutor()) {
        $("#toolBar").toolBar("enable", "counterSign");
    } else {
        $("#toolBar").toolBar("disable", "counterSign");
    }

    if (canAssist()) {
        $("#toolBar").toolBar("enable", "assist");
    } else {
        $("#toolBar").toolBar("disable", "assist");
    }

    if (canAbort()) {
        $("#toolBar").toolBar("enable", "abort");
    } else {
        $("#toolBar").toolBar("disable", "abort");
    }
    
    if (canSleep()) {
        $("#toolBar").toolBar("enable", "sleep");
    } else {
        $("#toolBar").toolBar("disable", "sleep");
    }

    if (canMakeACopyFor()) {
        $("#toolBar").toolBar("enable", "makeACopyFor");
    } else {
        $("#toolBar").toolBar("disable", "makeACopyFor");
    }

    if (canRelate()) {
        $("#toolBar").toolBar("enable", "relate");
    } else {
        $("#toolBar").toolBar("disable", "relate");
    }

    if (canShowChart()) {
        $("#toolBar").toolBar("enable", "showChart");
    } else {
        $("#toolBar").toolBar("disable", "showChart");
    }

    if (canShowApprovalHistory()) {
        $("#toolBar").toolBar("enable", "showApprovalHistory");
    } else {
        $("#toolBar").toolBar("disable", "showApprovalHistory");
    }
    
    if (!canWithdrawTask()) {
        $("#toolBar").toolBar("removeItem", "withdrawTask");
    }
    
    if (!canRecallProcessInstance()) {
        $("#toolBar").toolBar("removeItem", "recallProcessInstance");
    } 
    refreshBtnStatusExtend();
}

//按钮状态扩展校验
function refreshBtnStatusExtend(){
	
}

//AJAX刷新处理人列表
function reLoadJobTaskExecutionList(bizId, procUnitId) {
	var url=web_app.name + "/common/TaskExecutionList.jsp";
	procUnitId=procUnitId || 'Approve';
	Public.load(url,{ bizId: bizId, procUnitId: procUnitId, taskId: taskId },function(data){
		$('div.job-task-execution').each(function(){
			if($(this).data('procUnitId')==procUnitId){
				var div=$(data).insertAfter(this);
				Public.autoInitializeUI(div);
		    	$(this).removeAllNode();
		    	if(isCancelReadHandleResult){
		    		cancelIsReadHandleResult();
		    	}
			}
		});
	});
}

//收藏任务
function saveTaskCollect() {
    if (!taskId || !bizId) {
        return;
    }
    Public.ajax(web_app.name + '/workflow/collectTask.ajax',{ taskId: taskId },function () {
    	Public.successTip("common.tip.taskcollect.success");
	});
}

function queryHiTaskinstRelations(fn) {
    if (!bizId || bizId == '') return;
    Public.ajax(web_app.name + "/workflow/queryHiTaskinstRelations.ajax", { bizId: bizId }, function (data) {
        fn.call(window, data);
    });
}

/**
* 任务关联
*/
function relate() {
    queryHiTaskinstRelations(function (data) {
        UICtrl.showFrameDialog({ title: 'common.job.relate', width: getDefaultDialogWidth(), height: getDefaultDialogHeight(),
            url: web_app.name + '/workflow/showSelectTaskDialog.do',
            resize: true,
            init: function () {
                //初始化数据
                var appendRow = this.iframe.contentWindow.appendRow;
                if ($.isFunction(appendRow)) {
                    $.each(data.Rows, function (i, o) {
                        var row = $.extend({}, o);
                        row.id = o.relatedTaskId;
                        appendRow.call(window, row);
                    });
                }
            },
            ok: doRelate,
            cancelVal: 'common.button.close',
            cancel: true
        });
    });
}

function doRelate() {
    var _self = this,rows = this.iframe.contentWindow.selectedData;
    $.each(rows, function (i, o) {
        o.relatedTaskId = o.id;
        o.id = "";
    });
    Public.ajax(web_app.name + "/workflow/saveHiTaskinstRelation.ajax", { taskId: taskId, bizId: bizId, procInstId: procInstId, data: Public.encodeJSONURI(rows) }, function () {
        refreshFlag = true;
        _self.close();
        showHiTaskinstRelations(rows);
    });
}
/**
* 初始化关联任务显示
* **/
function initHiTaskinstRelations() {
	if(!$('#jobPageRight').length) return;
    queryHiTaskinstRelations(function (data) {
        showHiTaskinstRelations(data['Rows']||data);
    });
    //绑定关联任务点击事件
    $('#showHiTaskinstRelations').on('click', function (e) {
        var $clicked = $(e.target || e.srcElement);
        if ($clicked.is('a')) {
            var id = $clicked.attr('id'), task = pageHiTaskinstRelations[id];
            if (task) {
                browseTask(task);
            }else{
            	var url=$clicked.data('url');
            	if (Public.isNotBlank(url)) {
            		UICtrl.addTabItem({tabid: $clicked.attr('id'),text:$clicked.data('name'),url:web_app.name+url});
            	}
            }
        }
    });
    setHiTaskinstRelationsHeight();
    checkRightCollapse();
}

//校验右侧工具区域是否显示
function checkRightCollapse(){
	if(!$('#jobPageRight').length) return;
	//iphone 中不自动隐藏
	if($.isIosPhone) return;
	//默认true 隐藏右侧区域
	var _isHideRight=true;
	//存在关联任务显示
	if($('#showHiTaskinstRelations').find('div.data-view-list').length > 0){
		_isHideRight=false;
	}
	//正在审批中需要显示
	if($('#handleOpinion').length > 0){
		_isHideRight=false;
	}
	$("#jobPageLayout").layout('setRightCollapse',_isHideRight);
}

function setHiTaskinstRelationsHeight(){
	var $relations=$('#showHiTaskinstRelations'),
		_next=$relations.next(),
		_height=$('#jobPageRight').height();
	if(_next.length > 0){
		_height-=_next.height();
	}
	$relations.height(_height-35);
}

function showHiTaskinstRelations(data) {
    pageHiTaskinstRelations = {}; //清空数据
    var div = $('#showHiTaskinstRelations');
    //清空旧数据
    div.find('>div.system-taskinst-relations').removeAllNode();
    if (data && data.length > 0) {
        var html = ['<div class="system-taskinst-relations">'];
        $.each(data, function (i, o) {
            pageHiTaskinstRelations[o.relatedTaskId] = $.extend({}, o, { id: o.relatedTaskId }); //写入数据注意id 的处理
            html.push('<div style="margin-left:5px; " class="data-view-list">');
            html.push('<a href="javascript:void(0);" id="', o.relatedTaskId,'">');
            html.push('<i class="fa fa-caret-right"></i>&nbsp;');
            html.push(o.description); 
            html.push('</a>');
            html.push('</div>');
        });
        html.push('</div>');
        div.prepend(html.join(''));
        flag = true;
    }
    checkRightCollapse();
}

//业务追加任务关联
function appendTaskinstRelations(data){
	var div = $('#showHiTaskinstRelations');
	if(!div.length) return;
	//清空旧数据
	div.find('>div.append-taskinst-relations').removeAllNode();
	if (data && data.length > 0) {
		var html = ['<div class="append-taskinst-relations">'];
		$.each(data, function (i, o) {
			html.push('<div style="margin-left:5px; " class="data-view-list">');
	        html.push('<a href="javascript:void(0);" data-url="', o.url,'" data-name="', o.name,'" id="', o.id,'">');
	        html.push('<i class="fa fa-caret-right"></i>&nbsp;');
	        html.push(o.description); 
	        html.push('</a>');
	        html.push('</div>');
	    });
	    html.push('</div>');
	    div.append(html.join(''));
	}
	checkRightCollapse();
}

//隐藏默认toolbar上的按钮
function removeToolBarItem(items) {
    $.each(items, function (i, p) {
    	$("#toolBar").toolBar('removeItem', p);
    });
}

/**
* 是否查询预览处理人
* 系统默认为true
*/
function isQueryHandlers() {
    return true;
}

function showManuscriptDialog($clicked){
	var idWithPrex =  $clicked.attr("id");
	var procUnitHandlerId = idWithPrex.split("_")[1]; 
    Public.ajax(web_app.name + '/workflow/loadProcUnitHandlerMaunscript.ajax', {procUnitHandlerId: procUnitHandlerId }, function(data){
    	if (!Public.isBlank(data) && !Public.isBlank(data.opinion64)){
    		var content ="<img style='height: 450px; width:100%;' src = '" + data.opinion64 + "' ></img>";
    		UICtrl.showDialog({title: 'common.job.manuscript', content: content,width: 500, height: 480,okVal:'common.button.close',close:false,ok:false});
        }
    });
}

function reLoadManuscript(){
	if (writeSignatureDialog){
		var imgId = "#img_" + $("#currentHandleId").val();
		var manuscriptImg = $(imgId);
		if (manuscriptImg.length == 0){
			$("#handleOpinion").after('<img class="manuscript" id = "img_' + $("#currentHandleId").val() + '" style="height:60px;width:100%;">');
			manuscriptImg = $(imgId);
		}
		manuscriptImg.attr("src", writeSignatureDialog.opinion64);
	}
}

function bindJobPageEvent(){
	$('#jobPageCenter').on('dblclick',function(e){
		var $clicked = $(e.target || e.srcElement);
		if($clicked.is('img.manuscript')){
			showManuscriptDialog($clicked);
			return false;
		}
	});
	initAddCommentDiv();//初始化添加评论按钮
	//驳回理由增加事件
	/*$("#toolBar #rejectedReason").comboDialog({type:'sys',name:'approvalRejectedReason',
		onShow:function(){
			return canRejectedReason();
		},
		onChoose: function(dialog){
	    	var row = this.getSelectedRow(),rejectedReason='';
	    	if(row){
	    		rejectedReason=row.content;
	    	}
	    	$("div.jobPageFloatHandleDiv textarea").val(rejectedReason);
			$("#handleOpinion").val(rejectedReason);
	    	return true;
		}
	});*/
}

//初始化添加评论按钮
function initAddCommentDiv() {
    //保存评论操作
    var doSaveComment = function (handleId, message) {
        var url = web_app.name + "/workflow/insertComment.ajax";
        Public.ajax(url, { handleId: handleId, bizId: bizId, message: Public.encodeURI(message) }, function (data) {
        	var showCommentDiv = $('#opinionTextRow' + handleId);
            $("div.comment", showCommentDiv).remove(); //先删除以前数据
            //重新组合数据显示
            $.each(data, function (i, o) {
                  var html = ['<div class="comment">'];
                  html.push('<b>&nbsp;',o['userId'], '&nbsp;[', o['time'], ']', '&nbsp;:&nbsp;</b>');
                  html.push(o['fullMessage'], '</div>');
                  showCommentDiv.append(html.join(''));
             });
        });
    };
    var _buttonHtml=['<div id="jobCommentButton" style="position:absolute;display:none;z-index:100;">'];
    _buttonHtml.push('<button type="button" class="btn btn-info">');
    _buttonHtml.push('<i class="fa fa-vcard"></i>&nbsp;',$.i18nProp('common.button.addcomment'));
    _buttonHtml.push('</button></div>');
    //添加按钮
    var _button = $(_buttonHtml.join('')).appendTo('body');
    _button.on('click',function(e){
        var id = $(this).attr('handleId'),handlerName=$(this).attr('handlerName');
        var html = ['<textarea class="textarea" style="height:76px;"></textarea>'];
        html.push('<div style="text-align:right;padding-top:3px;">');
        html.push('<button type="button" class="btn btn-primary dialog_ok"><i class="fa fa-save"></i>&nbsp;',$.i18nProp('common.button.save'),'</button>','&nbsp;&nbsp;');
        html.push('<button type="button" class="btn btn-primary dialog_close"><i class="fa fa-close"></i>&nbsp;',$.i18nProp('common.button.cancel'),'</button>', '</div>');
        var options = { title:$.i18nProp('common.job.addcomment',handlerName), content: html.join(''),width: 350,onClick: function (e,div) {
            if (e.is('button.dialog_close')) {
                this.close();
            } else if (e.is('button.dialog_ok')) {
                var comment = $('textarea',div).val();
                if (comment == '') {
                	//请输入意见回复内容
                    Public.tip("common.job.warning.addcomment");
                    return false;
                }
                doSaveComment(id,comment);
                this.close();
            }
        }};
        var div=Public.dialog(options);
        $('textarea',div).maxLength({ maxlength: 1000 });
    });
    //处理关闭按钮时间事件
    var hoverTimer = null;
    var clearHoverTimer = function () { if (hoverTimer) clearTimeout(hoverTimer); };
    var setHoverTimer = function () { hoverTimer = setTimeout(function () { _button.hide(); hoverTimer = null; }, 300); };
    //opinionTextRow 在标签模板中根据任务处理状态控制是否存在
    $('#jobPageCenter').on('mouseover mouseout',function(e){
    	 var el = $(e.target || e.srcElement);
    	 if(!el.parents('div.opinionTextRow').length) return true;
    	 var _this=el.parents('div.opinionTextRow');
    	 if(e.type == "mouseover"){
    		var id = $('input[name="id"]', _this).val(),handlerName=_this.attr('handlerName'),offset = _this.offset();
            clearHoverTimer();
            _button.attr({handleId:id,handlerName:handlerName}).css({ top: offset.top + 1, left: offset.left + _this.width() - _button.width()}).show();
    	 }else if(e.type == "mouseout"){
    		clearHoverTimer();
            setHoverTimer();
    	 }
    });
    $('#jobPageCenter').on('scroll', function () {
    	clearHoverTimer();
    	_button.hide();
	});
    _button.hover(function () { clearHoverTimer(); }, function () { clearHoverTimer(); setHoverTimer(); });
}

//初始化浮动处理对话框
function initFloatHandleDialog() {
    if (Public.isReadOnly) {
    	afterInitFloatHandleDialog();
        return;
    }
    if(!$('#jobPageRight').length){
    	return;
    }
    var handleOpinion = $('#handleOpinion');
    var flag = handleOpinion.length > 0;
    if (flag) {//存在操作框
        var handleResult = $('#handleResult');
        if (!handleResult.val() || handleResult.val() < 1){
        	handleResult.combox("setValue", "1");
        }
        var html = ['<div class="job-float-panel" id="jobPageFloatPanel">'];
        html.push('<div class="navTitle">');
        html.push('<a href="javascript:void(0);" class="togglebtn" id="jobPageFloatPanelTogglebtn" title="show or hide"><i class="fa fa-angle-double-down"></i></a>')
        html.push('<i class="fa fa-credit-card"></i>&nbsp;<span class="titleSpan">',$.i18nProp('common.job.floathandledialog'),'</span></div>');
        html.push('<div class="navline"></div>');
        html.push('<div class="jobPageFloatHandleDiv">');
        html.push('<textarea class="textarea" maxLength="500">', handleOpinion.val(), '</textarea>');
        html.push('<div class="job-handle-result" id="jobHandleResult"></div>');
        html.push('<div class="btn-group" id="jobPageFloatPanelButtons">');
        html.push('<button type="button" class="btn btn-info _submit"><i class="fa fa-mail-forward"></i>',$.i18nProp('common.button.advance'),'</button>');
        if (isSupportManuscript()){
            var _ie=$.browser.msie&&$.browser.version<9;
            if(!_ie){
            	//手写录入
            	html.push('<button type="button" class="btn btn-info _write"><i class="fa fa-pencil-square-o"></i>',$.i18nProp('common.button.manuscript'),'</button>');
            }
        }
        html.push('</div>');
        html.push('</div>');
        html.push('</div>');
        var floatDiv = $(html.join('')).appendTo('#jobPageRight');
        $("textarea",floatDiv).placeholder($.i18nProp('common.job.handleopinion')).maxLength({ maxlength: 300 }).on('keyup paste cut', function () {
        	var _self=$(this);
            setTimeout(function () {$('#handleOpinion').val(_self.val()).trigger('blur');}, 0);
        });
        floatDiv.on('click', function (e) {
            var $clicked = $(e.target || e.srcElement);
            if ($clicked.is('input')) {
                setTimeout(function () {
                    var value = $clicked.getValue();
                    $('#handleResult').combox('setValue', value);
                    setAgreeDefaultOpinion();
                }, 0);
            }
            if($clicked.is('i.fa')){$clicked=$clicked.parent();}
            if($clicked.is('button._submit')){//提交 
            	$('#advance','#toolBar').trigger('click.toolBar');
            }
            if($clicked.is('button._write')){//手写录入
            	showWriteSignatureDialog();
            }
            if($clicked.is('a.togglebtn')){//show or hide
            	$('div.jobPageFloatHandleDiv',this).toggle();
            	$clicked.find('i.fa').removeClass('fa-angle-double-down').addClass('fa-angle-double-up');
            	setHiTaskinstRelationsHeight();
            	//iphone 中需要调整中间区域显示高度
            	adjustCenterPanelHeightByIos();
            }
        });
        setJobHandleResult();
        handleResult.combox({onChange: function(values){
        	setAgreeDefaultOpinion();
        	$('input[name="jobPageFloatHandleResult"]:first',floatDiv).setValue(values.value);
        }});
        setAgreeDefaultOpinion();
    }
    //快捷处理栏 初始化完成后执行
    afterInitFloatHandleDialog();
}
//快捷处理栏 初始化完成后执行
function afterInitFloatHandleDialog(){
	if(!$.isMobile()) return;//手机端才执行
    //解决iframe页面嵌入后在ios设备上position=fixed属性失效的问题
    adjustFixedPanelByIos();
    var _buttons=$('#jobPageFloatPanelButtons');
    if(!_buttons.length) return;
    //判断toolBar类型 如果toolBar在页面下方显示 则隐藏快捷处理按钮 并增加显示高度35px
    if($('#toolBar').hasClass('job-button-fixed-bottom')){
    	$('#jobPageFloatPanel').append('<div style="height:35px;">&nbsp;</div>');
    	$('#jobPageFloatPanelButtons').hide();
    	//$('#jobPageFloatPanelTogglebtn').hide();
    }
}
//解决iframe页面嵌入后在ios设备上position=fixed属性失效的问题
function adjustFixedPanelByIos(){
	if(!$.isIosPhone) return;
	$('#jobPageContainer').addClass('dom-overflow-auto');
	$('#toolBar').appendTo('body');
	var _panel=$('#jobPageFloatPanel');
	if(_panel.length > 0){
		_panel.appendTo('body');
		_panel.css({left:0,width:'100%'});
	}
	adjustCenterPanelHeightByIos();
}

function adjustCenterPanelHeightByIos(){
	if(!$.isIosPhone) return;
	var _height = $.windowHeight();
	var _panel=$('#jobPageFloatPanel');
	if(_panel.length > 0){
		_height-=_panel.height();
	}
	$('#jobPageContainer').height(_height);
}

function setJobHandleResult(){
	var handleResult=$('#handleResult');
	if(!handleResult.length) return;
	var handleResultData =handleResult.combox('getFormattedData'),html=[];
	$.each(handleResultData, function (i, o) {
        html.push('<label><input type="radio" name="jobPageFloatHandleResult" value="', o['value'], '"');
        if (o.value == handleResult.val()) {
            html.push(' checked');
        }
        html.push('/>', o['text'], '</label>');
    });
	$('#jobHandleResult').html(html.join(''));
}

function showWriteSignatureDialog(){
	//手写签字
	writeSignatureDialog = UICtrl.showFrameDialog({
        title: "common.job.manuscript",
        param: {
        	bizId: bizId,
        	procUnitId: procUnitId,
        	procUnitHandlerId: $("#currentHandleId").val()
        },
        width: 1000,
        height: 480,
        url: web_app.name + '/workflow/forwardWriteSignature.do',
        ok: false
    });    
}

function withdrawTask(){
	if (!taskId) {
        return;
    }
	//您要回收当前选中的任务吗？
    UICtrl.confirm("common.job.confirm.withdrawTask", function () {
        Public.ajax(web_app.name + '/workflow/withdrawTaskByBizId.ajax',{taskId: taskId,bizId: bizId}, function (task) {
        	Public.locationHref(task.executorUrl,{bizId:task.bizId,taskId:task.id});
		});
    });
}


//撤销流程
function recallProcessInstance(){
	if (!taskId||!isApplicantPerson()) {
        return;
    }
	//您是否要撤销当前流程？
    UICtrl.confirm("common.job.confirm.recallProcess", function () {
        Public.ajax(web_app.name + '/workflow/recallProcessInstance.ajax',{ procInstId: procInstId, taskId: taskId,bizId: bizId}, function (task) {
        	Public.locationHref(task.executorUrl,{bizId:task.bizId,taskId:task.id});
		});
    });
}

//隐藏处理结果中已阅选项
function cancelIsReadHandleResult(){
	var handleResult = $('#handleResult');
	if(handleResult.length > 0){
		var data=handleResult.combox('getJSONData');
		delete data['3'];//删除已阅选项
		//通过  不通过
		handleResult.combox('setData',data);
		setJobHandleResult();
		isCancelReadHandleResult=true;
	}
}

//关闭当前页并刷新任务中心页面
function closeJobPageAndReloadTaskCenter(){
	UICtrl.closeAndReloadTabs("TaskCenter", null,function(){
		if(isDoModel() && procInstId){
			//不再框架内的单据无法直接关闭 跳转到流程图查看页面
			Public.locationHref('workflow/showFlowChart.load',{bizId:bizId});
		}else{
			//非流程任务直接关闭
			Public.closeWebPage();
		}
	});
}

function getAdditionSelectOrgParams(){
	return {};
}