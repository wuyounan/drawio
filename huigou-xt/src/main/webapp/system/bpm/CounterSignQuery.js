var gridManager;

$(function() {
	initializeGrid();

	function initializeGrid() {
		var approvalRuleId = Public.getQueryStringByName("approvalRuleId");
		var groupId = Public.getQueryStringByName("groupId");
		
		gridManager = UICtrl.grid("#maingrid", {
			columns: [ 
			   { display : "common.field.procUnitName",remark : "环节名称", name: "description", width: 120, minWidth: 60, type: "string", align: "left" }, 
			   { display : "common.field.handlername",remark : "处理人", width: 300 , minWidth: 60, type: "string", align: "left" }
			],
			dataAction: "server",
			url: web_app.name + "/workflow/queryCounterSignProcUnit.ajax",
			parms: { approvalRuleId: approvalRuleId, groupId: groupId },
			checkbox: true,
			usePager: false,
			width: "99.8%",
			height: "100%",
			rownumbers: true,
			heightDiff: -10,
			fixedCellHeight: true,
			selectRowButtonOnly: true
		});
	}
});

function getCounterSignProcUnitData(){
	var data = gridManager.getSelecteds();
    if (!data || data.length == 0) {
    	//请选择数据.
        Public.tip("common.warning.nochoose");
        return;
    }

    if (data.length > 1) {
    	//请选择一条数据!
    	Public.tip('common.warning.onlychoose');
        return;
    }
    return data;
}