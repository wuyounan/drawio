
function bindTaskEvent() {
	$("#waitTaskContent").on('click', function (e) {
		var $clicked = $(e.target || e.srcElement);
		if ($clicked.is('a.aLink')) {
			var taskId=$clicked.attr('taskId');
			var currentTask = getCurrentTask($('#task_'+taskId));
			processTask(currentTask);
			return false;
		}
	});
	$("#trackTaskContent").on('click', function (e) {
        var $clicked = $(e.target || e.srcElement);
        if ($clicked.is('a.aLink')) {
        	var taskId=$clicked.attr('taskId');
        	$clicked=$('#task_'+taskId);
            showTrackingTask($clicked.attr('taskId'),$clicked.attr('bizId'),$clicked.attr('name'),$clicked.attr('url'));
            return false;
        }
    });
}


function getCurrentTask(el){
	var currentTask = {};
    currentTask.catalogId = el.attr('catalogId');
    currentTask.kindId = el.attr('taskKindId');
    currentTask.bizId = el.attr('bizId');
    currentTask.id = el.attr('taskId');
    currentTask.name = el.attr('name');
    currentTask.executorUrl = el.attr('url');
    currentTask.statusId = el.attr('statusId');
    return currentTask;
}

function showTrackingTask(taskId, bizId, name, url) {
    var params = (url.indexOf("?") >= 0) ? "&" : "?";
    params += "isReadOnly=true&taskId=" + taskId + "&bizId=" + bizId;
    UICtrl.addTabItem({tabid: bizId, text: name, url: web_app.name + '/' + url + params});
}

function showTasksMore(taskKind){
	if(parent.showTaskCenter){
		parent.showTaskCenter(taskKind);
	}else{
		var url = web_app.name + '/workflow/forwardTaskCenter.do?viewTaskKind='+taskKind;
		UICtrl.addTabItem({tabid: 'TaskCenter', text: $.i18nProp('index.task.center'), url: url});
	}
}

function reloadExecuteTasks() {
	Public.ajax(web_app.name + "/homePage/queryAllTasks.ajax", {}, function (data) {
		parseTaskHtml('#waitTaskContent',data['tasks']);
		parseTaskHtml('#trackTaskContent',data['trackingTasks']);
		$('span.taskCount').html(data['taskCount']);
		$('span.trackingTaskCount').html(data['trackingTaskCount']);
    });
}

function parseTaskHtml(container,taskList){
	if(!taskList) return;
	var html=[];
	$.each(taskList, function (i, o) {
		html.push('<div id="task_',o.id,'"');
		if(o.statusId=='ready'){
			html.push(' class="ready_task"');
		}
		html.push(' catalogId="',o.catalogId,'"');
		html.push(' taskKindId="',o.kindId,'"');
		html.push(' bizId="',o.bizId,'"');
		html.push(' taskId="',o.id,'"');
		html.push(' statusId="',o.statusId,'"');
		html.push(' name="',o.name,'"');
		html.push(' url="',o.executorUrl,'"');
		html.push('>');					
		html.push('<div class="title-view">');		
		html.push('<a href="javascript:void(0)" class="aLink" taskId="',o.id,'">',o.description,'</a>');		
		html.push('</div>');		
		html.push('<div class="date-view">');		
		html.push('<a href="javascript:void(0)" class="aLink" taskId="',o.id,'">',o.startTime,'</a>');		
		html.push('</div>');		
		html.push('</div>');	
    });
	$(container).html(html.join(''));
}