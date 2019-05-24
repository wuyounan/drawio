/**
 * 流程类别
 */
var FlowKind = FlowKind || {};

FlowKind.APPROVAL = "APPROVAL"; // 审批流程
FlowKind.BUSINESS = "BUSINESS"; // 业务流程
FlowKind.FREE = "FREE" ; // 自由流
FlowKind.FULL_FREE = "FULL_FREE"; // 完全自由流

var TaskScope = TaskScope || {};

TaskScope.PROCESS = "process"; //流程

TaskScope.TASK = "task"; //非流程

var ToDoTaskKind =  ToDoTaskKind || {};

ToDoTaskKind.NEED_TIMING = "needTiming";
ToDoTaskKind.NOT_NEED_TIMING = "notNeedTiming";

var TaskKind = TaskKind || {};

TaskKind.TASK = "task"; //任务

TaskKind.NOTICE = "notice"; //通知

TaskKind.MAKE_A_COPY_FOR = "makeACopyFor"; //抄送

TaskKind.REMIND = "remind"; //催办

TaskKind.MEND = "mend"; //补审

TaskKind.REPLENISH = "replenish"; //补充（打回）

/**
* 是否打回任务
* @param {} taskKindId
* @return {}
*/
TaskKind.isReplenishTask = function (taskKindId) {
    return taskKindId == TaskKind.REPLENISH;
}

/**
* 是否补审任务
* @param {} taskKindId
* @return {}
*/
TaskKind.isMendTask = function (taskKindId) {
    return taskKindId == TaskKind.MEND;
}

/**
* 是否抄送任务
* @param {} taskKindId
* @return {}
*/
TaskKind.isMakeACopyFor = function (taskKindId) {
    return taskKindId == TaskKind.MAKE_A_COPY_FOR;
}
TaskKind.isCustom = function (taskKindId) {
    return taskKindId != TaskKind.TASK &&  taskKindId != TaskKind.NOTICE && taskKindId != TaskKind.MAKE_A_COPY_FOR && taskKindId != TaskKind.MEND &&  taskKindId != TaskKind.REPLENISH;
}
TaskKind.isAdvance = function (taskKindId) {
    return taskKindId == TaskKind.TASK || taskKindId == TaskKind.MEND || taskKindId == TaskKind.REPLENISH;
}

TaskKind.getDisplay = function (id) {
    var result = "";
    switch (id) {
        case TaskKind.TASK:
            result = $.i18nProp('common.taskkind.task');
            break;
        case TaskKind.NOTICE:
            result = $.i18nProp('common.taskkind.notice');
            break;
        case TaskKind.MAKE_A_COPY_FOR:
            result = $.i18nProp('common.taskkind.makeacopyfor');
            break;
        case TaskKind.REMIND:
            result = $.i18nProp('common.taskkind.remind');
            break;
        case TaskKind.MEND:
            result = $.i18nProp('common.taskkind.mend');
            break;
        case TaskKind.REPLENISH:
            result = $.i18nProp('common.taskkind.replenish');
            break;
    }
    return result;
}

var TaskStatus = TaskStatus || {};

TaskStatus.READY = "ready"; //尚未处理

TaskStatus.EXECUTING = "executing"; //正在处理

TaskStatus.COMPLETED = "completed"; //已完成

TaskStatus.SLEEPING = "sleeping"; //暂缓处理

TaskStatus.CANCELED = "canceled"; //已取消

TaskStatus.ABORTED = "aborted"; //已终止

TaskStatus.RETURNED = "returned"; //已回退

TaskStatus.WAITED = "waited"; //等待中

TaskStatus.TRANSMITED = "transmited"; //已转交

TaskStatus.SUSPENDED = "suspended"; //已暂停

TaskStatus.DELETED = "deleted"; //已删除

TaskStatus.isReayStatus = function (statusId) {
    return statusId == TaskStatus.READY || statusId == TaskStatus.EXECUTING;
}

TaskStatus.isCompletedStatus = function (statusId) {
    return statusId == TaskStatus.COMPLETED;
}

TaskStatus.isSleepingStatus = function (statusId) {
    return statusId == TaskStatus.SLEEPING;
}

TaskStatus.isToDoStatus = function (statusId) {
    return TaskStatus.isReayStatus(statusId) || TaskStatus.isSleepingStatus (statusId) || (TaskStatus.EXECUTING ==  statusId);
}


TaskStatus.getDisplay = function (id) {
    var result = "";
    switch (id) {
        case TaskStatus.READY:
            result =$.i18nProp('common.taskstatus.ready');
            break;
        case TaskStatus.EXECUTING:
            result = $.i18nProp('common.taskstatus.executing');
            break;
        case TaskStatus.COMPLETED:
            result = $.i18nProp('common.taskstatus.completed');
            break;
        case TaskStatus.SLEEPING:
            result = $.i18nProp('common.taskstatus.sleeping');
            break;
        case TaskStatus.CANCELED:
            result = $.i18nProp('common.taskstatus.canceled');
            break;
        case TaskStatus.ABORTED:
            result = $.i18nProp('common.taskstatus.aborted');
            break;
        case TaskStatus.RETURNED:
            result = $.i18nProp('common.taskstatus.returned');
            break;
        case TaskStatus.WAITED:
            result = $.i18nProp('common.taskstatus.waited');
            break;
        case TaskStatus.TRANSMITED:
            result = $.i18nProp('common.taskstatus.transmited');
            break;
        case TaskStatus.SUSPENDED:
            result = $.i18nProp('common.taskstatus.suspended');
            break;
        case TaskStatus.DELETED:
            result = $.i18nProp('common.taskstatus.deleted');
            break;
    }
    return result;
}

/**
* 流程控制命令
* @type 
*/
var ProcessAction = ProcessAction || {};
ProcessAction.SAVE = "save"; // 保存
ProcessAction.ADVANCE = "advance"; //流转
ProcessAction.DELETE = "delete"; //删除
ProcessAction.BACK = "back"; // 回退
ProcessAction.REPLENISH = "replenish"; // 打回
ProcessAction.QUERY_HANDLERS = "queryHandlers"; //查询处理人
ProcessAction.QUERY_ADVANCE = "queryAdvance"; //询问流转
ProcessAction.TRANSMIT = "transmit";//转交
ProcessAction.ASSIST = "assist";// 协审
ProcessAction.MAKE_A_COPYFOR = "makeACopyFor";//抄送
ProcessAction.WITHDRAW = "withdraw"; //撤回
ProcessAction.RECALL_PROCESS_INSTANCE = "recallProcessInstance";//撤销流程
ProcessAction.DELETE_PROCESS_INSTANCE = "deleteProcessInstance";//删除流程实例
ProcessAction.ABORT = "abort"; //终止流程
ProcessAction.SLEEP = "sleep"; //暂缓

/**
* 处理结果
* @type 
*/
var HandleResult = HandleResult || {};

HandleResult.AGREE = 1;  //同意

HandleResult.DISAGREE = 2; //不同意

HandleResult.HAVEPASSED = 3; //已阅

HandleResult.REPLENISH = 4; //打回

HandleResult.getDisplayName = function(id){
	var result = "";
    switch (id) {
    	 case HandleResult.AGREE:
            result = $.i18nProp('dictionary.handleResult.1');
            break;
    	 case HandleResult.DISAGREE:
            result = $.i18nProp('dictionary.handleResult.2');
            break;
    	 case HandleResult.HAVEPASSED:
            result = $.i18nProp('dictionary.handleResult.3');
            break;
    	 case HandleResult.REPLENISH:
             result = $.i18nProp('dictionary.handleResult.4');
             break;
          default:
          result = "";            
    }
    return result;
 }

var ViewTaskKind = ViewTaskKind || {};

ViewTaskKind.WAITING = 1; //待办任务
ViewTaskKind.COMPLETED = 2;//已办任务
ViewTaskKind.SUBMITED = 3;//提交任务
ViewTaskKind.DRAFT = 4;//待发任务
ViewTaskKind.INITIATE = 5;//本人发起
ViewTaskKind.COLLECT = 6;//收藏任务
 
var ProcNodeKind = {
    FOLDER: 'folder', //文件夹
    PROC: 'proc', //流程
    PROC_UNIT: 'procUnit'  // 流程环节
};

var QueryHandlerShowFieldKind = QueryHandlerShowFieldKind || {};

QueryHandlerShowFieldKind.FULL_NAME = "fullName";
QueryHandlerShowFieldKind.HANDLER_NAME = "handlerName";

QueryHandlerShowFieldKind.isShowFulName = function(id){
	return QueryHandlerShowFieldKind.FULL_NAME == id;
}

function processTask(task) {
    if (!task.executorUrl) {
        throw new Error("任务: " + task.name + "的executorUrl为空!");
    }

    var _taskUrl = DataUtil.composeURLByParam(task.executorUrl,{bizId:task.bizId,taskId:task.id});

    if (task.kindId == TaskKind.NOTICE) {
        // 通知
        if (task.executorUrl == 'workflow/showTaskDetail.do') {
            showTaskDetail("do", "RuntimeTask", task.executorUrl, task.id);
        } else {
            //其他任务点开后直接自动完成
            completeTask(task.id, function () {
                UICtrl.addTabItem({tabid: task.id,text: task.name,url: _taskUrl});
            });
        }
        return;
    } else if (task.kindId == TaskKind.MAKE_A_COPY_FOR || TaskKind.isCustom(task.kindId)){
    	//抄送任务自动完成
    	completeTask(task.id, function(){
    		UICtrl.addTabItem({tabid: task.id,text: task.name,url: _taskUrl});
    	});
    }else{
    	//待办任务修改任务状态后打开
    	updateTaskExtensionStatusToExecuting(task.id, function (){
    		UICtrl.addTabItem({tabid: task.id,text: task.name,url: _taskUrl});		
    	});
    }
}

/**
* 查看任务
*/
function browseTask(currentTask) {
    if (currentTask) {
    	var _taskUrl = currentTask.executorUrl;
        if (currentTask.kindId == TaskKind.NOTICE) {
            var kindId = "HistoricTask";
            if (window['isWaitingStatus'] && isWaitingStatus()) {
                kindId = "RuntimeTask";
            }
            if (url == 'workflow/showTaskDetail.do') {
                showTaskDetail("view", kindId, _taskUrl, currentTask.id);
                return;
            } else {
            	_taskUrl=DataUtil.composeURLByParam(_taskUrl,{
            		isReadOnly:true,
            		bizId:currentTask.bizId,
            		taskId:currentTask.id
            	});
            }
        } else {
        	_taskUrl=DataUtil.composeURLByParam(_taskUrl,{
        		isReadOnly:true,
        		bizId:currentTask.bizId,
        		taskId:currentTask.id
        	});
        }
        UICtrl.addTabItem({tabid: currentTask.id,text:currentTask.name,url: _taskUrl});
    }
}

function showTaskDetail(action, kindId, url, taskId) {
	//任务明细
    UICtrl.showFrameDialog({
        title: 'common.taskdetail.title',
        width: 400,
        height: 360,
        url: web_app.name + "/" + url,
        param: {
            kindId: kindId,
            taskId: taskId
        },
        ok: action == "do" ? function () {
            completeTask.call(this, taskId);
            return true;
        } : false,
        okVal: 'common.button.over',
        cancelVal: 'common.button.close',
        cancel: true,
        close: onDialogCloseHandler
    });
}

function completeTask(taskId, fn) {
    Public.ajax(web_app.name + '/workflow/completeTask.ajax', {
        taskId: taskId
    }, function () {
        reloadGrid();
        if ($.isFunction(fn)) {
            fn.call(window);
        } 
    });
}

function updateTaskExtensionStatusToExecuting(taskId, fn){
	 Public.ajax(web_app.name + '/workflow/updateTaskExtensionStatusToExecuting.ajax', {taskId: taskId}, function () {
		 reloadGrid();
	     if ($.isFunction(fn)) {
	    	 fn.call(window);
	     } 
	 });
}


function onDialogCloseHandler() {

}

var HandlerKind = HandlerKind || {};

HandlerKind.MANAGE_AUTHORITY = "ManageAuthority";
HandlerKind.POS = "Pos";
HandlerKind.PSM = "Psm";
HandlerKind.DEPT = "Dept";
HandlerKind.MANAGER_FUN = "ManagerFun";
HandlerKind.SEGMENTATION = "Segmentation";
HandlerKind.MANUAL_SELECTION = "ManualSelection";
HandlerKind.SCOPE_SELECTION = "ScopeSelection";

/**
 * 选择类别处理人
 */
HandlerKind.isSelection = function(handerKindCode){
	return handerKindCode == HandlerKind.MANUAL_SELECTION || handerKindCode == HandlerKind.SCOPE_SELECTION;
}

var CounterSignKind = CounterSignKind || {};
CounterSignKind.CHIEF = "chief";
CounterSignKind.MEND = "mend";
CounterSignKind.MANUAL_SELECTION = "manualSelection";

CounterSignKind.isManualSelection = function(value){
	return value == CounterSignKind.MANUAL_SELECTION
}
