var treeManager, gridManager = null, refreshFlag = false, selectFunctionDialog, lastSelectedId = 0;
var ids = null;

$(document).ready(function () {
	UICtrl.initDefaultLayout();
    initializeGrid();
    loadProcessManageTree();
});

function onBeforeExpand(node) {
    if (node.data.hasChildren) {
        if (!node.data.children || node.data.children.length == 0) {
            Public.ajax(web_app.name + "/procDefinition/queryProcDefinitions.ajax", {
                parentId: node.data.id, inculdeProcUnit: 1
            }, function (data) {
                treeManager.append(node.target, data.Rows);
            });
        }
    }
}

function procNodeClick(nodeData) {
	$("#layout").layout("setCenterTitle", "<span class='tomato-color'>[" + nodeData.name + "]</span>流程列表");

    lastSelectedId = nodeData.id;
    var params = $("#queryMainForm").formToJSON();
    params.parentId = lastSelectedId;
    UICtrl.gridSearch(gridManager, params);
}

function loadProcessManageTree(){
	$('#maintree').commonTree({
        loadTreesAction: '/procDefinition/queryProcDefinitions.ajax',
        idFieldName: 'id',
        parentIDFieldName: "parentId",
        textFieldName: "name",
        getParam: function(){
        	return { inculdeProcUnit: 1};
        },
        onClick: function (data) {
            if (!data){
            	return;
            }
            procNodeClick(data);
        },
        IsShowMenu: false
    });
}

function bindActivitiProcDefinition(key) {
    var params = {};
    var row = gridManager.getSelected();
    params["id"] = row.id;
    params["procId"] = key;    
    Public.ajax(web_app.name + "/procDefinition/bindActivitiProcDefinition.ajax", params);
}

function initComboDialog() {
    $("#toolbar_menulinkToProcess").comboDialog({ disabled: true, type: 'bpm', name: 'bindProc',
        width: 700,
        dataIndex: 'id',
        checkbox: true,
        title: "选择流程",
        onShow: function () {
            var row = gridManager.getSelected();
            if (!row) {
                Public.errorTip('请选择一个流程。');
                return false;
            } else if (row.hasChildren > 0) {
                Public.errorTip('该节点下面有子节点不能添加流程链接。');
                return false;
            }
            return true;
        },
        onChoose: function () {
            var row = this.getSelectedRow();
            if (!row) {
                Public.tip("请选择一条流程。");
                return;
            }
            bindActivitiProcDefinition(row.procId);
            refreshFlag = true;
            onDialogCloseHandler();
            return true;
        }
    });
}

/**
* 得到选择的对象
*/
function getSelectedObj() {
    var rows = gridManager.getSelectedRows();

    if (!rows || rows.length != 1) {
        Public.tip("请选择一条数据。");
        return;
    }

    return rows[0];
}

function importProcUnitHandler() {
    var obj = getSelectedObj();
    if (obj == null) {
        return;
    }
    
    if(Public.isBlank(obj.procId)){
    	Public.errorTip("流程定义未关联Activiti流程模板。");
    	return;
    }

    Public.ajax(web_app.name + '/procDefinition/importProcUnits.ajax', { parentId: obj.id }, function (data) {
        reloadGrid();
    });
}

function deployProcessHandler() {
    var obj = getSelectedObj();
    if (obj == null) {
        return;
    }

    if (obj.nodeKindId != ProcNodeKind.PROC) {
        Public.tip("请选择流程。");
        return;
    }

    deployProcess(obj.id);
}

// 配置
function deployProcess(id) {
    var html = ['<div class="ui-form" style="width:340px">'],
        fields = [
            { id: 'filePath', text: '流程文件路径', type: 'text' }
        ];
    var template = ["<dt style='width:290px'>{text}<font color='#FF0000'>*</font>&nbsp;:</dt>"];
    template.push("<dd style='width:330px'>");
    template.push("<input type='{type}' class='text' id='{id}' maxlength='300' value='{value}'/>");
    template.push("</dd>");
    $.each(fields, function (i, o) {
        html.push(template.join('').replace('{id}', o.id)
            .replace('{text}', o.text)
            .replace('{value}', o.value || '')
            .replace('{type}', o.type));
    });
    UICtrl.showDialog({
        width: 350,
        top: 150,
        title: '发布流程',
        height: 100,
        content: html.join(''),
        ok: function () {
            var fileName = $("#filePath").val();
            if (!fileName) return;
            Public.ajax(web_app.name + '/workflow/deploy.ajax', {
                fileName: fileName,
                id: id
            }, function (data) {
            	 refreshFlag = true;
            });
        },
        close: onDialogCloseHandler
    });
}

function doMoveProcess() {
    var moveToNode = $('#movetree').commonTree('getSelected');
    var moveToId = moveToNode.id;
    if (!moveToId) {
        Public.tip('请选择移动到的节点。');
        return false;
    }

    if (moveToNode.procId) {
        Public.tip('不能移动到该节点下面。');
        return false;
    }

    var params = {};
    params.parentId = moveToId;
    params.ids = $.toJSON(ids);
    Public.ajax(web_app.name+"/procDefinition/moveProcess.ajax", params, function (data) {
        reloadGrid();
        selectFunctionDialog.hide();
    });
}

function moveHandler() {
    ids = DataUtil.getSelectedIds({
        gridManager: gridManager,
        idFieldName: "id"
    });
    if (!ids) {
        return;
    }

    if (!selectFunctionDialog) {
        selectFunctionDialog = UICtrl.showDialog({
            title: "移动到...",
            width: 350,
            content: '<div style="overflow-x: hidden; overflow-y: auto; width: 340px;height:250px;"><ul id="movetree"></ul></div>',
            init: function () {
                $('#movetree').commonTree({
                    loadTreesAction: "/procDefinition/queryProcDefinitions.ajax",
                    idFieldName: 'id',
                    IsShowMenu: false
                });
            },
            ok: doMoveProcess,
            close: function () {
                this.hide();
                return false;
            }
        });
    } else {
        $('#movetree').commonTree('refresh');
        selectFunctionDialog.show().zindex();
    }
}

function renderBooleanKind(value){
   return "<div class='" + (value ? "status-enable" : "status-disable") + "'/>";
}

//初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: showInsertChildDialog,
        updateHandler: showUpdateDialog,
        moveHandler: moveHandler,
        deleteHandler: deleteHandler,
        saveSortIDHandler: updateSequence,
        importProcUnit: { id: 'importProcUnit', text: '导入流程环节', img: 'fa-external-link-square', click: importProcUnitHandler },
        linkToProcess: { id: 'linkToProcess', text: '链接流程', img: 'fa-link' },
        deployProcess: { id: 'deployProcess', text: '发布流程', img: 'fa-wifi', click: deployProcessHandler }
    });

    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            { display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
            { display: "流程ID", name: "procId", width: 100, minWidth: 80, type: "string", align: "left" },
            { display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },
            { display: "路径", name: "fullName", width: 200, minWidth: 60, type: "string", align: "left" },
            { display: "是否计时", name: "needTiming", width: 60, minWidth: 60, type: "string", align: "left",
               render : function(item) {
               	   return renderBooleanKind(item.needTiming);
				}
			},
			{ display: "限制时间", name: "limitTime", width: 60, minWidth: 80, type: "string", align: "right" },
            { display: "预览处理人", name: "showQueryHandlers", width: 80, minWidth: 60, type: "string", align: "left",
                           render : function(item) {
               	   return renderBooleanKind(item.showQueryHandlers);
				}
            },
            { display: "处理人合并", name: "mergeHandlerKindTextView", width: 80, minWidth: 80, type: "string", align: "left" },
            { display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "int", align: "left",
                render: function (item) {
                    item.id = item.id; //只为了能够更新sequence
                    return UICtrl.sequenceRender(item);
                }
            },
            { display: "状态", name: "status", width:60, minWidth: 60, type: "string", align: "left",
			         render : function(item) {
			    	   return renderBooleanKind(item.status);
					}
			 },
            { display: "描述", name: "description", width: 190, minWidth: 60, type: "string", align: "left" }
        ],
        dataAction: 'server',
        url: web_app.name + '/procDefinition/queryProcDefinitions.ajax',
        parms: {
            parentId: 1, inculdeProcUnit: 1
        },
        usePager: false,
        sortName: "sequence",
        SortOrder: "asc",
        toolbar: toolbarOptions,
        width: '99.8%',
        height: '100%',
        heightDiff: -13,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {
            doShowUpdateDialog(data.id);
        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
    initComboDialog();
}

function getId() {
    return $("#id").val();
}

//弹出新增对话框
function showInsertChildDialog() {
    var node = $('#maintree').commonTree('getSelected');
    if (!node) {
        Public.errorTip('请选择左侧父节点。');
        return;
    }
    if (node.procId) {
        Public.errorTip('节点已经链接到流程，不能再添加子结点。');
        return;
    }
    UICtrl.showAjaxDialog({
        title: "添加子节点",
        param: { parentId: node.id },
        width: 600,
        url: web_app.name
            + '/procDefinition/showInsertProcDefinition.load',
        ok: doSaveChild,
        close: onDialogCloseHandler
    });
}

//弹出修改对话框
function showUpdateDialog() {
    var id = DataUtil.getUpdateRowId(gridManager);
    if(!id){Public.errorTip("请选择需要修改的数据。"); return;}
    doShowUpdateDialog(id);
}

//进行修改操作
function doShowUpdateDialog(id) {
    UICtrl.showAjaxDialog({
        title: "修改子节点",
        url: web_app.name + '/procDefinition/showUpdateProcDefinition.load',
        param: { id: id },
        width: 600,
        ok: doSaveChild,
        close: onDialogCloseHandler
    });
}

//进行新增、修改操作
function doSaveChild() {
    var _self = this;
    var id = getId();
    $('#submitForm').ajaxSubmit({
        url: web_app.name + (id ? '/procDefinition/updateProcDefinition.ajax' : '/procDefinition/insertProcDefinition.ajax'),
        success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

//删除节点 
function deleteHandler() {
    var rows = gridManager.getSelectedRows();
    if (rows.length > 1) {
        Public.tip("只能选择一条数据进行删除。");
        return;
    }

    var param = {};
    if (rows[0]) {
        param.id = rows[0].id;
    }

    var action = "procDefinition/deleteProcDefinitions.ajax";
    DataUtil.del({
        action: action,
        param: param,
        idFieldName: 'id',
        gridManager: gridManager,
        onSuccess: function () {
            refreshFlag = true;
            onDialogCloseHandler();
        }
    });
}

//更新排序号
function updateSequence() {
    var action = "procDefinition/updateProcDefinitionSequence.ajax";
    DataUtil.updateSequence({
        action: action,
        idFieldName: "id",
        gridManager: gridManager,
        onSuccess: reloadGrid
    });
}

// 查询
function query(obj) {
    var param = $(obj).formToJSON();
    UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
    $("#maintree").commonTree('refresh', lastSelectedId);
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

//重置表单
function resetForm(obj) {
    $(obj).formClean();
}

//关闭对话框
function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}