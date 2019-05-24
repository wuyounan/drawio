var assistantGrid = null, ccGrid = null, uiElementPermissionGrid = null;
var stepRefreshFlag=false;
function configStepHandler() {
    if (!handlerGridManager) {
        return;
    }

    var rows = handlerGridManager.getSelectedRows();
    if (!rows || rows.length == 0) {
        Public.tip("请选择处理人节点。");
        return;
    }
    if (rows.length > 1) {
        Public.tip("只能选择一个处理人节点进行配置。");
        return;
    }
    if (Public.isBlank(rows[0].id)) {
        Public.tip("处理人尚未保存无法配置。");
        return;
    }

    UICtrl.showAjaxDialog({
        title: "审批环节处理人配置",
        width: 700,
        url: web_app.name + '/approvalRule/forwardApprovalHandlerDetail.load',
        param: {id: rows[0].id, approvalRuleId: rows[0].approvalRuleId},
        ok:  doUpdateHandlerDetailConfig,
        close: handlerDetailConfigClose,
        init: function () {
        	UICtrl.autoGroupAreaToggle($("#submitForm"));
            initAssistantGrid();
            initCCGrid();
            initUIElementPermissionGrid();
            initializateTabPannel();
            UICtrl.autoGroupAreaToggle();
            //$("a.togglebtn:gt(0)").trigger("click");
        }
    });
}

function initializateTabPannel() {
	$('#tabPage').tab();
}

function doUpdateHandlerDetailConfig() {
	if (Public.isReadOnly) {
		return;
	}
	
    var param = {};

    var assistantDetailData = assistantGrid.rows;
    if (assistantDetailData != null && assistantDetailData.length > 0) {
    	$.each(assistantDetailData, function(i, o){
        	delete o.dataSourceConfig;
        });
        param.assistantList = Public.encodeJSONURI(assistantDetailData);
    }
    var ccDetailData = ccGrid.rows;
    if (ccDetailData != null && ccDetailData.length > 0) {
    	 $.each(ccDetailData, function(i, o){
    	    	delete o.dataSourceConfig;
    	    });
        param.ccList = Public.encodeJSONURI(ccDetailData);
    }

   
    var fieldPermissionData = uiElementPermissionGrid.rows;
    if (!(!fieldPermissionData || fieldPermissionData.length == 0)) {
        param.fieldPermissionList = Public.encodeJSONURI(fieldPermissionData);
    }

    
    $('#submitForm').ajaxSubmit({url: web_app.name + '/approvalRule/saveApprovalRuleHandler.ajax',
        param: param,
        success: function () {
        	assistantGrid.loadData();
        	ccGrid.loadData();
        	uiElementPermissionGrid.loadData();
        	stepRefreshFlag=true;
        }
    });
}

function handlerDetailConfigClose(){
	if(stepRefreshFlag){
		if(handlerGridManager){
			handlerGridManager.loadData();
		}
		stepRefreshFlag=false;
	}
}

function initAssistantGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function () {
            UICtrl.addGridRow(assistantGrid, { sequence: assistantGrid.getData().length + 1 });
        },
        deleteHandler: function () {
            assistantGrid.deleteSelectedRow();
        }
    });
    var param = {chiefId: $("#id").val(), kindId: 'assistant', approvalRuleId: $("#approvalRuleId").val()}
    assistantGrid = UICtrl.grid('#assistantGrid', {
        columns: [
            { display: "描述", name: "description", width: 150, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'text', required: true} },
            { display: "审批人类别", name: "handlerKindName", width: 100, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'select', data: { type: "bpm", name: "approvalHandlerKind", back: { code: "handlerKindCode", name: "handlerKindName", dataSourceConfig: "dataSourceConfig" }}, required: true} },
            { display: "审批人ID", name: "handlerId", width: 100, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'text', required: true} },
            { display: "审批人", name: "handlerName", width: 150, minWidth: 60, type: "string", align: "left", frozen: true,
                    editor: { type: "dynamic", getEditor: function (row) {
                   	 var dataSourceConfig = row['dataSourceConfig'] || {};
                        if (typeof dataSourceConfig == "string"){
                            dataSourceConfig = (new Function("return " + dataSourceConfig))();
                        }
                        return dataSourceConfig;
                   }
                   }},
            { display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'spinner', required: true} }
        ],
        dataAction: 'server',
        url: web_app.name + '/approvalRule/queryAssistantHandlers.ajax',
        parms: param,
        height: 200,
        sortName: 'sequence',
        sortOrder: 'asc',
        toolbar: toolbarOptions,
        enabledEdit: true,
        usePager: false,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        autoAddRow: { kindId: "assistant", dataSourceConfig: "", approvalRuleId: $("#approvalRuleId").val()/*, chiefId: $("#id").val()*/},
        onBeforeEdit: function (editParm) {
            var c = editParm.column;
            if (c.name == 'value') {//启用的数据value 不能编辑
                return editParm.record['status'] === 0;
            }
            return true;
        }
    });
}

function initCCGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function () {
            UICtrl.addGridRow(ccGrid, { sequence: ccGrid.getData().length + 1 });
        },
        deleteHandler: function () {
            ccGrid.deleteSelectedRow();
        }
    });
    var param = {chiefId: $("#id").val(), kindId: 'cc', approvalRuleId: $("#approvalRuleId").val()}
    ccGrid = UICtrl.grid('#ccGrid', {
        columns: [
            { display: "描述", name: "description", width: 150, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'text', required: true} },
            { display: "审批人类别", name: "handlerKindName", width: 100, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'select', data: { type: "bpm", name: "approvalHandlerKind", back: { code: "handlerKindCode", name: "handlerKindName", dataSourceConfig: "dataSourceConfig" }}, required: true} },
            { display: "审批人ID", name: "handlerId", width: 100, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'text', required: true} },
            { display: "审批人", name: "handlerName", width: 150, minWidth: 60, type: "string", align: "left", frozen: true,
                	editor: { type: "dynamic", getEditor: function (row) {
                      	 var dataSourceConfig = row['dataSourceConfig'] || {};
                           if (typeof dataSourceConfig == "string"){
                               dataSourceConfig = (new Function("return " + dataSourceConfig))();
                           }
                           return dataSourceConfig;
                      }
                }  },
            { display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'spinner', required: true} }
        ],
        dataAction: 'server',
        url: web_app.name + '/approvalRule/queryCCHandlers.ajax',
        parms: param,
        height: 200,
        sortName: 'sequence',
        sortOrder: 'asc',
        toolbar: toolbarOptions,
        enabledEdit: true,
        usePager: false,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        autoAddRow: {kindId: "cc", dataSourceConfig: "", approvalRuleId: $("#approvalRuleId").val()/*, chiefId: $("#id").val()*/},
        onBeforeEdit: function (editParm) {
            var c = editParm.column;
            if (c.name == 'value') {//启用的数据value 不能编辑
                return editParm.record['status'] === 0;
            }
            return true;
        }
    });
}

function initAddUIElementPermissionHandler() {
    $("#uiElementPermissionDiv #toolbar_menuAdd").comboDialog({
        type: 'opm',
        name: 'uiElement',
        dataIndex: 'id',
        width: 400,
        lock: false,
        checkbox: true,
        onChoose: function () {
            var rows = this.getSelectedRows();
            var addRows = [];
            $.each(rows, function (i, o) {
            	o.sequence = uiElementPermissionGrid.getData().length + 1;
                addRows.push(o);
            });
            uiElementPermissionGrid.addRows(addRows);
            return true;
        }
    });

}

function initUIElementPermissionGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function () {
        },
        deleteHandler: function () {
        	uiElementPermissionGrid.deleteSelectedRow();
        }
    });
    var param = {approvalRuleHandlerId: $("#id").val()}
    uiElementPermissionGrid = UICtrl.grid('#uiElementPermissionGrid', {
        columns: [
            { display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text', required: true} },
            { display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text', required: true} },
            { display: "类别", name: "kindId", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: fieldType, required: true},
                render: function (item) {
                    return fieldType[item.kindId];
                } },
            { display: "操作", name: "operationId", width: 150, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: fieldAuthority, required: true},
                render: function (item) {
                    return fieldAuthority[item.operationId];
                }},
    			{ display: "排序号", name: "sequence", width: 150, minWidth: 60, type: "string", align: "left",
    				editor: { type: 'spinner',required: true}
    			}
        ],
        dataAction: 'server',
        url: web_app.name + '/approvalRule/queryUIElementPermissions.ajax',
        parms: param,
        sortName: 'sequence',
        sortOrder: 'asc',
        height: '200',
        toolbar: toolbarOptions,
        usePager: false,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        enabledEdit: true,
        onLoadData: function () {
            initAddUIElementPermissionHandler();
        }
    });
}
