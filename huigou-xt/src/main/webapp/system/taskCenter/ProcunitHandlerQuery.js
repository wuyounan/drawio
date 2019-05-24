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
        columns:  [
                 { display:"流程类型", name: "procName", width:150, minWidth: 60, type: "string", align: "left"},
       	         { display:"流程名称", name: "procDescription", width: 350, minWidth: 60, type: "string", align: "left" },
       	         { display:"单据编号", name: "bizCode", width: 150, minWidth: 60, type: "string", align: "left" },
       	         { display:"流程开始时间",name: "procStartTime", width:120, minWidth: 60, type: "date", align: "left" },
       	         { display:"申请人",name: "applicantPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
       	         { display:"流程环节", name: "procUnitName", width: 120, minWidth: 60, type: "string", align: "left" },
       	         { display:"审批环节名",name: "subProcUnitName", width: 150, minWidth: 60, type: "string", align: "left" },
       	         { display:"处理人",name: "handlerName", width: 100, minWidth: 60, type: "string", align: "left" },
       	         { display:"任务状态",name: "taskStatusName", width: 120, minWidth: 60, type: "string", align: "left" },
       	         { display:"任务处理意见",name: "handleResultTextView", width: 120, minWidth: 60, type: "string", align: "left" }
       	],
        dataAction: "server",
        url: web_app.name + "/workflow/slicedQueryProcunitHandler.ajax",
        manageType: 'taskQuery,admin',
        pageSize: 20,
        width: "99.8%",
        height: '100%',
        heightDiff: -2,
        sortName: 'group_id,version',
        sortOrder: 'asc',
        fixedCellHeight: true,
        selectRowButtonOnly: true,
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
	UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
	gridManager.loadData();
} 

function resetForm(obj) {
	$(obj).formClean();
}
