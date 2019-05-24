var gridManager, refreshFlag, operateCfg = {}, detailGridManager;

$(function () {
    initializeUI();
    initializeOperateCfg();
    initializeGrid();

    function initializeOperateCfg() {
        var path = web_app.name + '/agent/';
        operateCfg.queryAction = path + 'slicedQueryAgents.ajax';
        operateCfg.showInsertAction = path + 'showInsertAgent.load';
        operateCfg.showUpdateAction = path + 'showUpdateAgent.load';
        operateCfg.insertAction = path + 'insertAgent.ajax';
        operateCfg.updateAction = path + 'updateAgent.ajax';
        operateCfg.deleteAction = 'agent/deleteAgents.ajax';
        
        operateCfg.queryAgentProcsAction = path + 'slicedQueryAgentProcs.ajax';
        operateCfg.deleteAgentProcAction = 'agent/deleteAgentProcs.ajax';

        operateCfg.insertTitle = "添加代理";
        operateCfg.updateTitle = "修改代理";
    }

    function initializeGrid() {
        var toolbarparam = { addHandler: showInsertDialog, updateHandler: showUpdateDialog, deleteHandler: deleteAgent };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarparam);

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "创建人", name: "createdByName", width: 80, minWidth: 60, type: "string", align: "left" },
					{ display: "创建时间", name: "createdDate", width: 150, minWidth: 60, type: "datetime", align: "left" },
					{ display: "委托人", name: "clientName", width: 80, minWidth: 60, type: "string", align: "left" },
					{ display: "代理人", name: "agentName", width: 80, minWidth: 60, type: "string", align: "left" },
					{ display: "开始时间", name: "startDate", width: 150, minWidth: 60, type: "datetime", align: "left" },
					{ display: "结束时间", name: "endDate", width: 150, minWidth: 60, type: "datetime", align: "left" },
					{ display: "代理方式", name: "procAgentKindName", width:100, minWidth: 60, type: "string", align: "center",sortField:'procAgentKindId',
					render : function(item) {
						if (item.procAgentKindId == 1){
							return "全部";
						}else{
							return "自定义流程";
						}
					}
					},
					{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
						render : function(item) {
							return UICtrl.getStatusInfo(item.status);							
						}}
					],
            dataAction: "server",
            url: operateCfg.queryAction,
            pageSize: 20,
            toolbar: toolbarOptions,
            sortName: "startDate",
            sortOrder: "asc",
            width: "99.8%",
            height: "100%",
            heightDiff: -10,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                doShowUpdateDialog(data);
            }
        });
        UICtrl.setSearchAreaToggle(gridManager);
    }

    function initializeUI() {
    }
});

function initDetailDialog() {
	$('#procAgentKindId').combox({
		onChange:function(){
			var procAgentKindId = $('#procAgentKindId').val();
	        if (procAgentKindId == 2) {
	            $('#detailGrid').show();
	        }
	        else {
	            $('#detailGrid').hide();
	        } 
		}
	});
    $clientName = $("#clientName");
    $clientName.orgTree({ filter: 'psm', back: { text: $clientName, value: '#clientId' }});

    $agentName = $("#agentName");
    $agentName.orgTree({ filter: 'psm', back: { text: $agentName, value: '#agentId'} });

    var toolbarOptions = UICtrl.getDefaultToolbarOptions(
	        { addHandler: function () {
	            UICtrl.showFrameDialog({
	                url: web_app.name + '/procDefinition/showSelectProcDialog.do',
	                param: { includeProcUnit: 0 },
	                title: "选择流程",
	                parent:window['ajaxDialog'],
	                width: 500,
	                height: 400,
	                ok: doSelectedProc,
	                cancelVal: '关闭',
	                cancel: true
	            });
	        },
	            deleteHandler: function () {
	                DataUtil.delSelectedRows({ action: operateCfg.deleteAgentProcAction,
	                    idFieldName: "id",
	                    param:{ agentId: getId()},
	                    gridManager: detailGridManager,
	                    onSuccess: function () {
	                        detailGridManager.loadData();
	                    }
	                });
	            }
	        });

    detailGridManager = UICtrl.grid("#detailGrid", {
        columns: [
	            { display: "流程ID", name: "procId", width: 150, minWidth: 60, type: "string", align: "left", hide: true },
	            { display: "流程名称", name: "fullName", width: 400, minWidth: 60, type: "string", align: "left" }
	             ],
        dataAction: "server",
        url: operateCfg.queryAgentProcsAction,
        parms: { agentId: getId() },
        toolbar: toolbarOptions,
        width: 600,
        height: 250,
        sortName: 'fullName',
        sortOrder: "asc",
        heightDiff: -8,
        enabledEdit: true,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        rownumbers: true,
        onLoadData: function () {
            return !($('#id').val() == '');
        }
    });
}

function doSelectedProc() {
    var fn = this.iframe.contentWindow.getSelectedObj;
    var rows = fn();
    if (!rows) {
        return;
    }

    var _self = this;
   
    $.each(rows, function (i, o) {
        detailGridManager.addRow({ procId: o.id, fullName: o.fullName });
    });

    _self.close();
}

function getId() {
    return $("#id").val();
}

function showInsertDialog() {
    UICtrl.showAjaxDialog({
        title: operateCfg.insertTitle,
        width: 600,
        init: initDetailDialog,
        url: operateCfg.showInsertAction,
        ok: doSaveAgent,
        close: onDialogCloseHandler
    });
}

function doShowUpdateDialog(row) {
	var operator = ContextUtil.getOperator();
    var personId = operator["id"];    
    var isOtherCreated = row.clientId.indexOf(personId + "@") == -1;
    UICtrl.showAjaxDialog({
        url: operateCfg.showUpdateAction,
        param: {
            id: row.id
        },
        title: operateCfg.updateTitle,
        width: 600,
        init: initDetailDialog,
        ok: isOtherCreated ? false : doSaveAgent,
        close: onDialogCloseHandler
    });
}

function showUpdateDialog() {
	var row = DataUtil.getUpdateRow(gridManager);
    if(!row){return;}
    doShowUpdateDialog(row);
}

function doSaveAgent() {
    var _self = this;
    var id = getId();
    var params = {};

    var detailData = DataUtil.getGridData({ gridManager: detailGridManager });
    params.detailData = $.toJSON(detailData);
    $('#submitForm').ajaxSubmit({ url: id ? operateCfg.updateAction : operateCfg.insertAction,
        param: params,
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

/**
 *  验证创建人
 */
function checkCreator(rows, operateKind){
	var operator = ContextUtil.getOperator();
    var personId = operator["id"];
    
    var row;
    for (var i = 0, len = rows.length; i < len;  i++){
    	row = rows[i];
    	if (row.clientId.indexOf(personId) == -1){
    		Public.errorTip("您不能" + operateKind + "“" +  row.clientName  + "”" + "创建的代理。" );
    		return false;
    	}
    }    
    return true;
}

function deleteAgent() {
	if (!checkCreator(gridManager.getSelectedRows(), "删除")){
		return;
	};
    DataUtil.del({ action: operateCfg.deleteAction, idFieldName: "id", gridManager: gridManager, onSuccess: reloadGrid });
}

function reloadGrid() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function setGridStatus() {
    var procAgentKindId = $('input:radio[name=procAgentKindId]:checked').val();
    if (procAgentKindId == 2) {
        $('#detailGrid').show();
    }
    else {
        $('#detailGrid').hide();
    } 
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}