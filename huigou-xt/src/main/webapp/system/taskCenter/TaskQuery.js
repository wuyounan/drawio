var gridManager=null;
$(function () {
	bindEvent();
	initializeGrid();
});

function bindEvent(){
	$("#orgName").orgTree({
        filter: "ogn,dpt", excludePos: 1, param: {orgKindId: "ogn,dpt"},
        back: {text: "#orgName"},
        manageType: 'taskQuery,admin',
        onChange:function(value,data){
        	$('#administrativeOrgFullId').val(data.fullId);
        }
    });
	
	$('#procName').treebox({
		name : 'procTreeView',
		param: {nodeKindId: "folder,proc"},
		back: {text: "#procName"},
		onChange:function(value,data){
			$('#procFullId').val(data.fullId);
		}
	});
	$('#selectDateRange').combox({onChange:function(){
		setTimeout(function(){initdateRange();},10);
	}})
	initdateRange();
}

function initdateRange(){
	var dateRange=$('#selectDateRange').val();
	if(dateRange=='10'){
		UICtrl.enable('#editStartDate');
		UICtrl.enable('#editEndDate');
	}else{
		UICtrl.disable('#editStartDate');
		UICtrl.disable('#editEndDate');
	}
}

function initializeGrid() {
    gridManager = UICtrl.grid("#maingrid", {
        columns: getTaskCenterGridColumns(),
        dataAction: "server",
        url: web_app.name + "/workflow/queryTasks.ajax",
        pageSize: 20,
        parms: {queryCategory: "taskQuery"},
        width: "99.8%",
        height: '100%',
        heightDiff: -2,
        sortName: 'startTime',
        sortOrder: 'desc',
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowIndex, rowObj) {
        	browseTask(data);
        },
        delayLoad: true,
        onLoadData: function(){
			return $('#administrativeOrgFullId').val() != '';
		}
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

function query(obj) {
	var param = $(obj).formToJSON();
	if(!param) return;
	param.onlyQueryApplyProcUnit = $("#onlyQueryApplyProcUnit").is(":checked");
	param.singleProcInstShowOneTask = $("#singleProcInstShowOneTask").is(":checked"); 
	UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
	gridManager.loadData();
} 

function resetForm(obj) {
	$(obj).formClean();
}
