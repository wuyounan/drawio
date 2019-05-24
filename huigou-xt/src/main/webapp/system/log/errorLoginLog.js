var gridManager = null;

$(document).ready(function() {
	initializeGrid();
});

function initializeGrid() {
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
            { display: "登录名", name: "loginName", width: 120, minWidth: 60, type: "string", align: "left" },
		    { display: "IP地址", name: "clientIp", width: 100, minWidth: 60, type: "string", align: "left" },		   
		    { display: "登录时间", name: "loginDate", width: 150, minWidth: 60, type: "string", align: "left" },
		    { display: "错误信息", name: "errorMessage", width: 800, minWidth: 60, type: "string", align: "left" }
		],
		dataAction: 'server',
		url: web_app.name+'/log/sliceQueryHistoricSessions.ajax',
		parms:{ queryKindId: "INVALID_LOGIN_NAME"},
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -8,
		sortName:'loginDate',
		sortOrder:'desc',
		fixedCellHeight: true,
		selectRowButtonOnly: true
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
	gridManager.loadData();
} 

function resetForm(obj) {
	$(obj).formClean();
}