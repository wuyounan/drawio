var gridManager = null,gridExport=null, statusId, operationType, logType;
$(document).ready(function () {
    initializeUI();
    initializeTree();
    initializeGrid();
});

function initializeUI() {
	UICtrl.initDefaultLayout();
    statusId = $('#statusId').val() || "";
    operationType = $('#operationType').combox('getJSONData');
    logType = $('#logType').combox('getJSONData');
}

function initializeTree() {
	$('#maintree').commonTree({
        loadTreesAction: 'tmAuthorize/queryDelegationOrgs.ajax',
        parentId: 'orgRoot',
        getParam: function (e) {
            if (e) {
                return {showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt,pos,psm"};
            }
            return {showDisabledOrg: 0};
        },
        changeNodeIcon: function (data) {
            data[this.options.iconFieldName] = OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
        },
        IsShowMenu: false,
        onClick: function (data) {
            onFolderOrgTreeNodeClick(data.fullName, data.fullId);
        }
    });
} 


function onFolderOrgTreeNodeClick(fullName,fullId) {
	if(fullName) {
		fullName = "[" + fullName + "]";	
	}
	$("#layout").layout("setCenterTitle", '<span class="tomato-color">' + fullName + "</span>日志列表")
    var params = $("#queryMainForm").formToJSON();
    params.fullId = fullId;
    UICtrl.gridSearch(gridManager, params);
}

function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
    	viewHandler:viewLog,
    	exportExcelHandler:exportExcel
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            { display: "系统", name: "appName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "机构名称", name: "organName", width: 120, minWidth: 60, type: "string", align: "left" },
			{ display: "部门名称", name: "deptName", width: 120, minWidth: 60, type: "string", align: "left" },
			{ display: "操作时间", name: "beginDate", width: 150, minWidth: 60, type: "string", align: "left" },
            { display: "操作状态", name: "statusName", width: 80, minWidth: 30, type: "string", align: "left"},
			{ display: "操作员", name: "personMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			{ display: "操作员角色", name: "roleKindName", width: 80, minWidth: 60, type: "string", align: "left" },
			{ display: "日志类型", name: "logType", width: 80, minWidth: 60, type: "string", align: "left" ,
				render: function (item) {
		            return logType[item.logType];
			}},
			{ display: "操作类型", name: "operateName", width: 110, minWidth: 60, type: "string", align: "left",
				render: function (item) {
		            return operationType[item.operateName];
			}},
			{ display: "IP", name: "ip", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "MAC", name: "mac", width: 130, minWidth: 60, type: "string", align: "left" },
			{ display: "方法名称", name: "methodName", width: 240, minWidth: 60, type: "string", align: "left" },
			{ display: "描述", name: "description", width: 240, minWidth: 60, type: "string", align: "left" }
			],
        dataAction: 'server',
        url: web_app.name + '/log/slicedQueryOperationLogs.ajax',
        parms: { statusId: statusId },
        toolbar: toolbarOptions,
        width: '99.8%',
        height: '100%',
        heightDiff: -8,
        sortName:'beginDate',
		sortOrder:'desc',
        checkbox: true,
        enabledMultiColumnSort:false,// 考虑MongoDB 实现不启用多字段排序
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {
        	doViewLog(data.id);
        }
    });
    //TODO 这样做的目的是什么？？？
    /*gridExport = UICtrl.grid('#exportGrid', {
        columns: [
            { display: "系统编码", name: "appCode", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "系统名称", name: "appName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "机构名称", name: "organName", width: 120, minWidth: 60, type: "string", align: "left" },
			{ display: "部门名称", name: "deptName", width: 120, minWidth: 60, type: "string", align: "left" },
			{ display: "操作员", name: "personMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			{ display: "操作员角色", name: "roleKindName", width: 80, minWidth: 60, type: "string", align: "left" },
			{ display: "操作时间", name: "beginDate", width: 150, minWidth: 60, type: "string", align: "left" },
			{ display: "IP", name: "ip", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "MAC", name: "mac", width: 130, minWidth: 60, type: "string", align: "left" },
			{ display: "类名称", name: "className", width: 240, minWidth: 60, type: "string", align: "left" },
			{ display: "方法名称", name: "methodName", width: 240, minWidth: 60, type: "string", align: "left" },
			{ display: "日志类型", name: "logType", width: 80, minWidth: 60, type: "string", align: "left" ,
				render: function (item) {
		            return logType[item.logType];
			}},
			{ display: "操作类型", name: "operateName", width: 110, minWidth: 60, type: "string", align: "left",
				render: function (item) {
		            return operationType[item.operateName];
			}},
			{ display: "操作类型", name: "operateName", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "人员密级", name: "operateSecurityLevelName", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "资源密级", name: "resourceSecurityLevelName", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "机器密级", name: "machineSecurityLevelName", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "修改前数据", name: "beforeImage", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "修改后数据", name: "afterImage", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "参数详细", name: "params", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "错误详细", name: "errorMessage", width: 110, minWidth: 60, type: "string", align: "left" },
			{ display: "摘要详细", name: "description", width: 110, minWidth: 60, type: "string", align: "left" },
			],
        dataAction: 'server',
        url: web_app.name + '/log/slicedQueryOperationLogs.ajax',
        parms: { statusId:statusId },
        toolbar: toolbarOptions,
        width: '99.8%',
        height: '100%',
        heightDiff: -8,
        sortName:'beginDate',
		sortOrder:'desc',
        checkbox: true,
        fixedCellHeight: true,
        enabledMultiColumnSort:false,// 考虑MongoDB 实现不启用多字段排序
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {
        	doViewLog(data.id);
        }
    });*/
    
    UICtrl.setSearchAreaToggle(gridManager);
}

function viewLog(){
	var row = UICtrl.checkSelectedRows(gridManager);
    if (!row || row.length > 1) {
    	Public.tip("请选择一条数据。");
        return;
    }
    doViewLog(row.id);
}

function doViewLog(id){
	UICtrl.showAjaxDialog({
    	title: "日志详细",
    	param: { id:id },
    	width: 900,
    	url: web_app.name + '/log/loadOperationLog.load',
    	init: initShowDialog,
    	ok: false
    });
}

function initShowDialog() {
    var operateName = $('#operateName').val();
    if(operateName == 'UPDATE'){
    	$('#oldImage').show();
    	$('#newImage').show();
    }else{
    	$('#oldImage').hide();
    	$('#newImage').hide();
    }
    var statusId = $('#statusId').val();
    if(statusId == 1){
    	$('#error').hide();
    }else{
    	$('#error').show();
    }
}

function exportExcel(){
//	var params = gridManager.options.parms;
//	gridExport.options.pageSize = gridManager.options.pageSize;
//	gridExport.options.page = gridManager.options.page;
	UICtrl.gridExport(gridManager);
}

function query(obj) {
    var param = $(obj).formToJSON();
    UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}

function reloadGrid() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}
