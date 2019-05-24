var gridManager, dateRangeKindId = 1, currentTask, requestViewTaskKind, orgTree, parentId;
var shortcutTaskSearchData = {};

$(function () {
	$('html').addClass('dom-overflow');
    requestViewTaskKind = Public.getQueryStringByName("viewTaskKind");
    if (Public.isBlank(requestViewTaskKind)) {
        requestViewTaskKind = '1';
    }
    initializateUI();
    refreshBtnStatus();
    initializeGrid();
    //loadOrgTreeView();
    loadProcTreeView();
    bindEvents();
});

function doQueryByProcKind(procFullId){
	UICtrl.gridSearch(gridManager, {procFullId:procFullId});
}

function initializateUI() {
	var toolBarOptions={
		titleLang:TaskButtonLang,
		items:[ { id: 'processTask', remark: '处理', name :'common.button.process' ,icon: 'fa-mail-forward', event: function(){processTask(currentTask); }},
		     { id: 'browseTask', remark: '查看', name :'common.button.view' , icon: 'fa-list-alt', event: function(){browseTask(currentTask);}},
		     { id: 'withdrawTask', remark: '回收' , name :'common.button.withdraw', icon: 'fa-undo', event: withdrawTask},
		     { id: 'mend', remark: '补审', name :'common.button.mend' , icon: 'fa-user-plus', event: mend },
		     { id: 'recallProcessInstance', remark: '撤销', name :'common.button.recall' , icon: 'fa-reply-all', event: recallProcessInstance},
		     { id: 'deleteProcessInstance', remark: '删除', name :'common.button.delete' , icon: 'fa-trash-o', event: deleteProcessInstance},
		     { id: 'showChart', remark: '流程图', name :'common.button.flowchart' , icon: 'fa-sitemap', event: processChart},
		     { id: 'showApprovalHistory', remark: '流程轨迹', name :'common.button.approvalhistory' , icon: 'fa-table', event: showApprovalHistory},
		     { id: 'taskCollect', remark: '收藏任务', name :'common.button.taskcollect' , icon: 'fa-star', event: saveTaskCollect},
		     { id: 'queryTask', remark: '高级搜索', name :'common.button.seniorquery' , icon: 'fa-search', event: showTaskQueryDialog}
	    ]
	};
    $('#taskBar').toolBar(toolBarOptions);
    $("#selectViewTaskKind").combox({ data: $("#selectViewTaskKind1").combox("getJSONData") });

    queryTaskQueryScheme(); //查询用户定义的查询
    //添加查询输入框
    UICtrl.createQueryBtn('#taskBar', function (value) {
        UICtrl.gridSearch(gridManager, { searchContent: encodeURI(value)});
    });
    
    setTaskCollectButtomText(requestViewTaskKind);
    $('#saveQuerySchemeButton').addClass('hidden-xs');
}

function setTaskCollectButtomText(taskKindId){
	if(taskKindId=='6'){
		//取消收藏
		$('#taskCollect').html('<i class="fa fa-star-o"></i>&nbsp;'+$.i18nProp('common.button.untaskcollect'));
	}else{
		//收藏任务
		$('#taskCollect').html('<i class="fa fa-star"></i>&nbsp;'+$.i18nProp('common.button.taskcollect'));
	}
}

function bindEvents() {    
	
    $("#selectDateRange").combox({
        onChange: function (item) {
            dateRangeKindId = item.value;
            (dateRangeKindId == 10) ? $("#customDataRange").show() : $("#customDataRange").hide();
        }
    });

    //$("#selectViewTaskKind").combox( { checkbox: true });
    
    //我的任务事件绑定
    $('#myselfTaskSearch').bind('click', function (e) {
        var $clicked = $(e.target || e.srcElement);
        if($clicked.is('i.fa')) $clicked=$clicked.parent();
        if($clicked.is('a')) $clicked=$clicked.parent();
        if($clicked.is('li')){
        	$('li.active',this).removeClass('active');
        	requestViewTaskKind = $clicked.attr('taskKind');
        	$clicked.addClass('active');
        	
        }
    });
    //快捷查询事件绑定
    $('#shortcutTaskSearch').bind('click', function (e) {
        var $clicked = $(e.target || e.srcElement);
        if ($clicked.is('a')) {
        	$('li.active',this).removeClass('active');
        	$clicked.parent().addClass('active');
            var schemeId = $clicked.attr('id');
            var param = shortcutTaskSearchData[schemeId];
            if (param) {
                var tmpParam = {}; //这里需要对中文编码
                tmpParam.queryCategory = "taskQuery";
                $.each(param, function (p, o) {
                    tmpParam[p] = encodeURI(o);
                });
                clearQueryInput();
                UICtrl.gridSearch(gridManager, tmpParam);
                closeTaskQueryDialog();
            }
        } else if ($clicked.hasClass('ui-icon-trash')) {//删除
            deleteTaskQueryScheme($clicked.parent().attr('id'), $clicked.parent().text());
        } else if ($clicked.hasClass('ui-icon-edit')) {//编辑
            var schemeId = $clicked.parent().attr('id');
            var param = shortcutTaskSearchData[schemeId];
            $('#queryMainForm').formSet(param);
            $('li.active',this).removeClass('active');
            //时间是否显示
            (param.dateRange == 10) ? $("#customDataRange").show() : $("#customDataRange").hide();
            $clicked.parent().parent().addClass('active');
        }
    });
}


function initializeGrid() {
    gridManager = UICtrl.grid("#maingrid", {
        columns: getTaskCenterGridColumns(),
        dataAction: "server",
        url: web_app.name + "/workflow/queryTasks.ajax",
        pageSize: 20,
        parms: { queryCategory: "myTransaction", viewTaskKindList: requestViewTaskKind},
        width: "99.8%",
        height: '100%',
        rownumbers: true,
        heightDiff: -2,
        sortName: 'startTime',
        sortOrder: 'desc',
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onSuccess: function () {
            currentTask = null;
            refreshBtnStatus();
        },
        onDblClickRow: function (data, rowIndex, rowObj) {
            if (canExecute()) {
                processTask(data);
            }else {
                browseTask(data);
            }
        },
        onSelectRow: function (data, rowindex, rowobj) {
            currentTask = data;
            refreshBtnStatus();
        },
        rowAttrRender: function (item, rowid) {
            currentTask = item;
            if (isToDoStatus() && isExecutor()) {
                return "style='font-weight:bold;'";
            }
            return '';
        }
    });
}

function getStartDate() {
    return $("#editStartDate").val();
}

function getEndDate() {
    return $("#editEndDate").val();
}
function clearQueryInput() {
    $('#taskBar').find('div.ui-grid-query-div').find('input').val('');
}

function isMySelfTaskKind(){
	return requestViewTaskKind  == "5";
}

function getAdministrativeOrgFullId(){
    var org = $('#orgTree').commonTree('getSelected');
   	if (org) {
   		var hasPermission = org.managerPermissionFlag;
   		if (hasPermission){
   			return org.fullId;
		}
	}
	return "";
}

function getProcDefineId(){
	var proc = $('#procTree').commonTree('getSelected');
	if (proc){
		return proc.procId;
	}
	 return "";
}

function query() {
    if (dateRangeKindId == 10 && (!getStartDate() || !getEndDate())) {
        Public.tip("请选择开始日期和结束日期。");
        return;
    }
    clearQueryInput();
    var params = $('#queryMainForm').formToJSON();
    
    params.queryCategory = "taskQuery";
    params.viewTaskKindList = requestViewTaskKind;
    params.administrativeOrgFullId = getAdministrativeOrgFullId();
	params.procDefineId =	getProcDefineId();
	params.onlyQueryApplyProcUnit = $("#onlyQueryApplyProcUnit").is(":checked");
	params.singleProcInstShowOneTask = $("#singleProcInstShowOneTask").is(":checked"); 

    UICtrl.gridSearch(gridManager, params);
    closeTaskQueryDialog();//关闭对话框
}

//刷新表格
function reloadGrid() {
    gridManager.loadData();
}

function hasData() {
	try{
		return gridManager.getData().length > 0 && currentTask != null;
	}catch(e){
		return false;
	}
}

function isToDoStatus() {
	return TaskStatus.isToDoStatus(currentTask.statusId);
}

function isCompletedStatus(){
	return TaskStatus.isCompletedStatus(currentTask.statusId); 
}

/**
* 是否为创建者
* 
* @returns {Boolean}
*/
function isCreator() {
    var creatorFullId = currentTask.creatorFullId;
    var operator = ContextUtil.getOperator();
    var id = operator["id"];

    return (creatorFullId.indexOf("/" + id) != -1);
}

/**
 * 是否流程起始任务
 * @return {}
 */
function isRootTask(){
	return !currentTask.previousId;
}

function isApplyActivity() {
    return currentTask.taskDefKey == "Apply";
}

/**
* 是否为执行人
* 
* @param task
* @returns {Boolean}
*/
function isExecutor() {
    var executorFullId = currentTask.executorFullId;
    var operator = ContextUtil.getOperator();
    var id = operator["id"];
  
    var result = false;
    if (executorFullId) {
        if (executorFullId.indexOf("/" + id) != -1) {
            result = true;
        } else {
            /*var items = Public.Context.getAllPersonMemberFIDs();
            if (items!=null){
            for(var i = 0; i < items.length; i++) {
            //有设计问题：当前认为一个人不仅可以处理自己的任务，而且还可以处理自己上层组织(包括所有上层)的任务
            if (startsWith(items[i], executorFullId)){
            result = true;
            break;
            }
            }
            }
            */
        }
    }
    return result;
};

/**
* 是否可以执行
* 
* @returns {Boolean}
*/
function canExecute() {
    return hasData() && isToDoStatus() && isExecutor();
}

function canBrowse() {
    return hasData();
}

function isFlow() {
    return !!currentTask.procInstId;
}

function isProcInstCompleted(){
	return !!currentTask.procInstEndTime;
}

function isProcessTask() {
    return (currentTask.catalogId == "process");
}

function canShowChart() {
    return hasData() && (isFlow() || TaskKind.isReplenishTask(currentTask.kindId));
}

function canShowApprovalHistory() {
    return hasData() &&  (isFlow() || TaskKind.isReplenishTask(currentTask.kindId));
}

function canShowTaskCollect() {
    return hasData() && (isFlow() || TaskKind.isMakeACopyFor(currentTask.kindId));
}

function canRecallProcessInstance(){
	return hasData() && isCreator() && isFlow() && isApplyActivity() && isCompletedStatus() && !isProcInstCompleted();
}

function canDeleteProcessInstance() {
    //我的正在处理的申请流程任务
	return hasData() && isCreator() && isFlow()  && isApplyActivity() && isToDoStatus()&& isRootTask();
}

function canMend(){
	return hasData() && isCreator() && isFlow() && isApplyActivity() && isProcInstCompleted();
}

function canWithdraw() {
    return hasData() && isToDoStatus() && isCreator() && isProcessTask() && !isApplyActivity();
}

function refreshBtnStatus() {
    if (canExecute()) {
        $("#taskBar").toolBar("enable", "processTask");
    } else {
        $("#taskBar").toolBar("disable", "processTask");
    }

    if (canBrowse()) {
        $("#taskBar").toolBar("enable", "browseTask");
    } else {
        $("#taskBar").toolBar("disable", "browseTask");
    }

    if (canWithdraw()) {
        $("#taskBar").toolBar("enable", "withdrawTask");
    } else {
        $("#taskBar").toolBar("disable", "withdrawTask");
    }

    if (canShowChart()) {
        $("#taskBar").toolBar("enable", "showChart");
    } else {
        $("#taskBar").toolBar("disable", "showChart");
    }

    if (canShowApprovalHistory()) {
        $("#taskBar").toolBar("enable", "showApprovalHistory");
    } else {
        $("#taskBar").toolBar("disable", "showApprovalHistory");
    }

    if (canShowTaskCollect()) {
        $("#taskBar").toolBar("enable", "taskCollect");
    } else {
        $("#taskBar").toolBar("disable", "taskCollect");
    }

    if (canDeleteProcessInstance()) {
        $("#taskBar").toolBar("enable", "deleteProcessInstance");
    } else {
        $("#taskBar").toolBar("disable", "deleteProcessInstance");
    }
    
    if (canMend()){
        $("#taskBar").toolBar("enable", "mend");
    } else {
        $("#taskBar").toolBar("disable", "mend");
    }
    
    if (canRecallProcessInstance()){
    	$("#taskBar").toolBar("enable", "recallProcessInstance");
    } else {
        $("#taskBar").toolBar("disable", "recallProcessInstance");
    }
};

/**
* 回收任务
*/
function withdrawTask() {
    if (currentTask) {
    	//您要回收当前选中的任务吗？
        UICtrl.confirm("common.job.confirm.withdrawTask", function () {
            Public.ajax(web_app.name + '/workflow/withdrawTask.ajax',
			{ taskId: currentTask.id, previousId: currentTask.previousId, processAction: "withdraw" }, function () {
				//您已成功回收任务。
			    Public.successTip("common.job.success.withdrawTask");
			    reloadGrid();
			});
        });
    }
}

/**
* 显示流程图
*/
function processChart() {
    if (currentTask) {
    	var url=web_app.name + '/workflow/showFlowChart.load?bizId='+currentTask.bizId;
        UICtrl.addTabItem({ tabid:'workFlowChart'+currentTask.bizId, text:$.i18nProp('common.job.chartdialog'),url:url}); 
    }
}

/**
* 显示审批历史
*/
function showApprovalHistory() {
    if (currentTask) {
        UICtrl.showFrameDialog({
            title: 'common.button.approvalhistory',
            width: getDefaultDialogWidth(),
            height: getDefaultDialogHeight(),
            url: web_app.name + '/workflow/showApprovalHistory.do',
            param: { bizId: currentTask.bizId },
            resize: true,
            ok: false,
            cancelVal: 'common.button.close',
            cancel: true
        });
    }
}

//保存任务查询方案
function saveQueryScheme() {
    if (dateRangeKindId == 10 && (!getStartDate() || !getEndDate())) {
        Public.errorTip("请选择开始日期和结束日期。");
        return;
    }
    var param = $('#queryMainForm').formToJSON({ encode: false });
    //附件参数
    param.administrativeOrgFullId = getAdministrativeOrgFullId();
	param.procDefineId =	getProcDefineId();
	param.onlyQueryApplyProcUnit = $("#onlyQueryApplyProcUnit1").is(":checked");
	param.singleProcInstShowOneTask = $("#singleProcInstShowOneTask1").is(":checked"); 
	param.viewTaskKindList = requestViewTaskKind;
	
    var div = $('#shortcutTaskSearch').find('li.active');
    var schemeId = '', schemeName = '';
    if (div.length > 0) {
        schemeId = div.find('a').attr('id');
        schemeName = $.trim(div.text());
    }
    var html = ['<input type="text" class="text" maxlength="30" id="taskQuerySchemeName" value="', schemeName, '">'];
    UICtrl.showDialog({
        title: '输入查询方案名称', width: 300,height:50,
        content: html.join(''),
        ok: function () {
            var _self = this;
            var name = $('#taskQuerySchemeName').val();
            if (name == '') {
                Public.tip("请输入查询方案名称。");
                return;
            }
            var url = web_app.name + '/personQueryScheme/savePersonQueryScheme.ajax';
            Public.ajax(url, { id: schemeId, kindId: 'task', name:Public.encodeURI(name), param:Public.encodeJSONURI(param) }, function (data) {
                queryTaskQueryScheme();
                _self.close();
            });
        }
    });
}

/*获取任务查询方案列表*/
function queryTaskQueryScheme() {
    var url = web_app.name + '/personQueryScheme/queryPersonQuerySchemes.ajax';
    Public.ajax(url, { kindId: 'task' }, function (data) {
        var div = $('#shortcutTaskSearch'), length = data.length;
        shortcutTaskSearchData = {};
        var html = [];
        $.each(data.Rows, function (i, o) {
            html.push('<li class="list-group-item">');
            html.push('<a href="javascript:void(0);" id="', o.id, '">');
            html.push('<i class="fa fa-trash ui-icon-trash" title="删除"></i>&nbsp;');
            html.push('<i class="fa fa-edit ui-icon-edit" title="编辑"></i>&nbsp;');
            html.push(o.name);
            html.push('</a>');
            html.push('</li>');
            shortcutTaskSearchData[o.id] = o.param;//$.evalJSON(o.param);
        });
        div.find('ul').html(html.join(''));
    });
}

//删除查询方案
function deleteTaskQueryScheme(schemeId, name) {
    UICtrl.confirm('您确定删除<font color=red>[' + $.trim(name) + ']</font>吗?', function () {
        Public.ajax(web_app.name + '/personQueryScheme/deletePersonQueryScheme.ajax', { id: schemeId }, function () {
            queryTaskQueryScheme();
        });
    });
}

//撤销流程
function recallProcessInstance(){
	//流程实例没有完成 、申请环节已提交才能撤销流程实例
	if (currentTask  && currentTask.taskDefKey == "Apply" ) {
		//您是否要撤销当前流程？
        UICtrl.confirm("common.job.confirm.recallProcess", function () {
            Public.ajax(web_app.name + '/workflow/recallProcessInstance.ajax',
			{ procInstId: currentTask.procInstId, taskId: currentTask.id }, function () {
				//您已成功撤销当前流程。
			    Public.successTip("common.job.success.recallProcess");
			    reloadGrid();
			});
        });
    }
}

//删除流程实例
function deleteProcessInstance() {
    if (currentTask) {
    	//您是否要删除当前选中的任务？
        UICtrl.confirm("common.job.confirm.deleteTask", function () {
            Public.ajax(web_app.name + '/workflow/deleteProcessInstance.ajax',
			{ procInstId: currentTask.procInstId },
			function () {
				//您已成功删除当前流程。
			    Public.successTip("common.job.success.deleteTask");
			    reloadGrid();
			});
        });
    }
}

function showMendDialog(){
	var params = {};
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();

    params.procInstId = currentTask.procInstId;
    params.bizId = currentTask.bizId;
    params.bizCode = currentTask.bizCode;
    params.counterSignKindId = "mend";
    
    params = $.extend({}, params, selectOrgParams);
    
    UICtrl.showFrameDialog({
        title: 'common.button.mend',
        width: 800,
        height: 380,
        url: web_app.name + '/workflow/showCounterSignDialog.do',
        param: params,
        ok: doMend,
        close: onDialogCloseHandler,
        cancelVal: 'common.button.close',
        cancel: true
    });
}

function doMend() {
	var counterSignWindow = this.iframe.contentWindow;
    var params = counterSignWindow.getCounterSignData();
    if (!params) {
        return;
    }
    params.bizId = currentTask.bizId;
    params.procUnitId = counterSignWindow.procUnitId;
    var _self = this;
    Public.ajax(web_app.name + '/workflow/mend.ajax',params, function () {
		_self.close();
	});
}

//补审
function mend(){
   if (currentTask) {
	    //您是否对当前流程实例发起补审？
        UICtrl.confirm("common.job.confirm.mend", function () {
            showMendDialog();
        });
    }
}

//保存收藏任务
function saveTaskCollect() {
    if (currentTask) {
    	if(requestViewTaskKind == '6'){
    		deleteTaskCollect();
    		return;
    	}
    	
        Public.ajax(web_app.name + '/workflow/collectTask.ajax',
			{ taskId: currentTask.id },
			function () {
				//任务收藏成功。
			    Public.successTip("common.job.success.taskCollect");
			}
		);
    }
}

//取消任务收藏
function deleteTaskCollect() {
    if (currentTask) {
    	//您是否要取消收藏当前选中的任务？
        UICtrl.confirm("common.job.confirm.deleteTaskCollect", function () {
            Public.ajax(web_app.name + '/workflow/cancelCollectionTask.ajax',
			{ taskId: currentTask.id },
			function () {
			    Public.successTip("common.job.success.deleteTaskCollect");
			    if ($('#myselfCollect').hasClass('taskCenterChoose')||requestViewTaskKind  == "6") {
			        reloadGrid();
			    }
			});
        });
    }
}
//刷新任务列表
function reloadTaskGrid(taskKind){
	   gridManager.options.parms = {};
       clearQueryInput();
       closeTaskQueryDialog();
       requestViewTaskKind = taskKind;
       UICtrl.gridSearch(gridManager, { queryCategory: "myTransaction", viewTaskKindList: taskKind});
       setTaskCollectButtomText(taskKind);
}
//打开高级搜索对话框
function showTaskQueryDialog(){
	var div=$('#advancedQueryDiv');
	var dialog=Public.dialog({width:700,top:50,title:'common.button.seniorquery',onClose:function(){
		div.appendTo('body').addClass('hide');
	}});
	$('div.ui-public-dialog-content',dialog).append(div);
	div.removeClass('hide');
	dialog.css('zIndex',101);
	$('#jquery-screen-over').css('zIndex',100);
}

function closeTaskQueryDialog(){
	$('#advancedQueryDiv').parent().parent().find('a.ui-public-dialog-close').each(function(){
		$(this).triggerHandler('mousedown');
	});
}

function loadOrgTreeView() {
    $('#orgTree').commonTree({
        loadTreesAction:  '/org/queryOrgs.ajax',
        parentId: 'orgRoot',
        getParam: function (e) {
            if (e) {
                return { showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt,pos,psm" };
            }
            return { showDisabledOrg: 0 };
        },
        manageType: 'taskQuery,admin',
        isLeaf: function (data) {
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
        },
        IsShowMenu: false
    });
}

function loadProcTreeView(){
	$('#procTree').commonTree({
        loadTreesAction: '/procDefinition/queryProcDefinitions.ajax',
        idFieldName: 'id',
        parentIDFieldName: "parentId",
        textFieldName: "name",
        parentId: 0,
        IsShowMenu: false
    });
}