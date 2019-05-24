$(document).ready(function() {
	//首页任务事件绑定 TasksExecute.js
	bindTaskEvent();
	//消息提醒 remind.js
	bindRemindEvent();
	//快捷功能选择 
	AddFunction.initAddFunction();
});

function reloadGrid(){
	//TasksExecute.js
	reloadExecuteTasks();
}
