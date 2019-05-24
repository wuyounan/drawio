var approvalRuleTree, elementGridManager = null, scopeGridManager,
    handlerGridManager = null, refreshFlag = false, parentId = 0, operatorKind = {},
    elementData = [], handlerData = [], yesNoList = { 0: '否', 1: '是' }, fieldType = {}, fieldAuthority = {},
    hasPermission = false, orgId;
    
$(document).ready(function () {
    initializeUI();
	
    operatorKind = $("#operatorKindId").combox("getFormattedData");
    //什么地方用下面两个变量
    fieldType = $('#fieldType').combox('getJSONData');
    fieldAuthority = $('#fieldAuthority').combox('getJSONData');
    
    loadOrgTreeView();
    loadProcTreeView();
    initializeGrid();
    
    setToolBarEnabled(false);

    bindEvents();
    


    function initializeUI() {
    	UICtrl.layout("#layout", {leftWidth:3,onSizeChanged:function(){
    		UICtrl.onGridResize(elementGridManager);
    		UICtrl.onGridResize(handlerGridManager);
    	}});
    	$('#approvalTabPage').tab({onClick:function($clicked){
    		var id=$clicked.attr('id');
    		setTimeout(function(){
    			UICtrl.onGridResize(id=='elementTab'?elementGridManager:handlerGridManager)
    		},0);
    	}});
    	
    	$('#toolBar').toolBar([
			{ id: "insert", name: '添加', icon:"fa-plus", event:  insertApprovalRule },
	        { id: "update", name: '修改', icon: "fa-edit", event:  updateApprovalRule  },
	        { id: "delete", name: '删除', icon: 'fa-trash',  event: deleteApprovalRule },
	        { id: "copy", name: '复制', icon: 'fa-copy',   event: copyApprovalRule  },
	        { id: "paste", name: '粘贴', icon: 'fa-paste', event: pasteApprovalRule },
	        { id: "move", name: '移动', icon: 'fa-arrows', event: moveApprovalRule },
	        { id: "syn", name: '同步', icon: 'fa-list-alt', event: synApprovalRule }
	    ]);

        if (Public.isReadOnly) {
            $('#toolBar').hide();
        }
    }

    function bindEvents() {
    	//组织选择
    	$('#orgName').addClass('ui-text-white-bg').attr('readonly',true);
    	$('#chooseOrgDialog').add('#orgName').on('click',function(){
    		var height=getDefaultDialogHeight()*0.9;
    		var orgTree=$('#orgTree');
    		var dialog=Public.dialog({
    			width:400,
    			top:50,
    			title:'组织机构',
    			content:'<div class="dom-overflow-auto" style="height:'+height+'px;"></div>',
    			onClose:function(){
    				orgTree.appendTo('#treeHideDiv');
    			}
    		});
    		$('div.dom-overflow-auto',dialog).append(orgTree);
    	});
    	//流程选择
    	$('#procName').addClass('ui-text-white-bg').attr('readonly',true);
    	$('#chooseProcDialog').add('#procName').on('click',function(){
    		var height=getDefaultDialogHeight()*0.9;
    		var procTree=$('#maintree');
    		var dialog=Public.dialog({
    			width:400,
    			top:50,
    			title:'流程树',
    			content:'<div class="dom-overflow-auto" style="height:'+height+'px;"></div>',
    			onClose:function(){
    				procTree.appendTo('#treeHideDiv');
    			}
    		});
    		$('div.dom-overflow-auto',dialog).append(procTree);
    	});
    }
    
});

function closeChooseOrgDialog(fullName){
	$('#orgTree').parent().parent().parent().find('a.ui-public-dialog-close').each(function(){
		$(this).triggerHandler('mousedown');
	});
	$('#orgName').val(fullName);
}

function closeChooseProcDialog(fullName){
	if($('#maintree').is(':hidden')){
		return;
	}
	$('#maintree').parent().parent().parent().find('a.ui-public-dialog-close').each(function(){
		$(this).triggerHandler('mousedown');
	});
	$('#procName').val(fullName);
}


function checkSelectedApprovalRule() {
    try {
        var selectedNode = approvalRuleTree.getSelected();
        if (selectedNode && selectedNode.data.nodeKindId == "2") {
            return true;
        }
    } catch (e) {

    }
    Public.errorTip("请选择审批规则节点。");
    return false;
}

// 初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function () {
            if (!checkSelectedApprovalRule()) {
            	return;
            }
            UICtrl.addGridRow(elementGridManager, { sequence: elementGridManager.getData().length + 1 });
        },
        saveHandler: saveApprovalRuleElements,
        deleteHandler: function () {
            DataUtil.delSelectedRows({ action: 'approvalRule/deleteApprovalRuleElements.ajax',
            	param: { approvalRuleId: parentId },
            	gridManager: elementGridManager,
                onSuccess: function () {
                    elementGridManager.loadData();
                }
            });
        }
    });
    elementGridManager = UICtrl.grid('#elementGrid', {
        columns: [
            { display: "审批要素", name: "elementName", width: 200, minWidth: 60, type: "string", align: "left",
              editor: {
                 	type: 'select', beforeChange: function (item, editParm) {
                    var id = item['id'];
                    var data = editParm.record;
                    if (id != data['elementId']) {
                        data['fvalueId'] = "";
                        data['fvalue'] = "";
                        elementGridManager.reRender({ rowdata: data });
                    }
                }, data: { type: "bpm", name: "procApprovalElement", getParam: function () {
                    var procUnitId = $("#approvalRuleProcUnitId").val();
                    var proc = findProc();

                    if (!procUnitId || !proc) return false;
                    return { procKey: proc.procId, procUnitId: procUnitId };
                },
                    back: { id: "elementId", code: "elementCode", name: "elementName", dataSourceConfig: "dataSourceConfig"}
                }, required: true
                } 
            },
            { display: "操作符", name: "foperatorName", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: operatorKind, valueField: "foperator", required: true}
            },
            { display: "值ID", name: "fvalueId", width: 200, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text', required: true}
            },
            { display: "值", name: "fvalue", width: 200, minWidth: 60, type: "string", align: "left",
                editor: { type: "dynamic", getEditor: function (row) {
                    var dataSourceConfig = row['dataSourceConfig'] || {};
                    if (typeof dataSourceConfig == "string"){
                        dataSourceConfig = (new Function("return " + dataSourceConfig))();
                    }
                    return dataSourceConfig;
                }
                }
            },
            { display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'spinner', min: 1, max: 100, mask: 'nnn'}
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/approvalRule/queryApprovalRuleElements.ajax',
        parms: {
            approvalRuleId: 0
        },
        pageSize: 20,
        width: '99%',
        height: '100%',
        heightDiff:-67,
        sortName: 'sequence',
        sortOrder: 'asc',
        isCacheScrollLeft:false,
        usePager: false,
        toolbar: toolbarOptions,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        enabledEdit: true,
        checkbox: true,
        rownumbers: true,
        onAfterEdit: function (editParm) {
            var c = editParm.column, data = editParm.record;
            if (c.name == 'fvalue') {//启用的数据value 不能编辑
                elementGridManager.reRender({ rowdata: data });
            }
        },
        autoAddRow: { elementCode: "", foperator: "", dataSourceConfig: "" }
    });

    toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
    	addHandler: function () {
            if (!checkSelectedApprovalRule()) {
            	return;
            }
            UICtrl.addGridRow(handlerGridManager,{ sequence: handlerGridManager.getData().length + 1,
                    kindId: "chief", allowAdd: 1, allowAbort: 0,  mustPass: 1, allowTransfer: 1, needTiming: 1
            });
        },
        saveHandler: saveApprovalRuleHandlers,
        deleteHandler: function () {
                DataUtil.delSelectedRows({ action: 'approvalRule/deleteApprovalRuleHandlers.ajax',
                	param: { approvalRuleId: parentId },
                    gridManager: handlerGridManager,
                    onSuccess: function () {
                        handlerGridManager.loadData();
                    }
                });
         },
           // groupConfigHandler: { id: 'groupConfigHandler', text: '分组配置', img: 'page_dynamic.gif', click: groupConfigHandler },
         addConfigStep: { id: 'addConfigStep', text: '配置', img: 'fa-gear', click: configStepHandler }
    });
    
    handlerGridManager = UICtrl.grid('#handlerGrid', {
        columns: [
            { display: "环节ID", name: "handlerKindId", width: 150, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'text' }
            },
            { display: "描述", name: "description", width: 200, minWidth: 60, type: "string", align: "left", frozen: true,
                editor: { type: 'text', required: true}
            },
            { display: "审批人类别", name: "handlerKindName", width: 120, minWidth: 60, type: "string", align: "left",
                editor: { type: 'select', data: { 
                	type: "bpm", 
                	name: "approvalHandlerKind", 
                	back: { id: "approvalHandlerKindId", code: "handlerKindCode", name: "handlerKindName", dataSourceConfig: "dataSourceConfig"} }, 
                	required: true }
            },
            { display: "审批人ID", name: "handlerId", width: 200, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text', required: true}
            },
            { display: "审批人", name: "handlerName", width: 250, minWidth: 60, type: "string", align: "left",
                editor: { type: "dynamic", getEditor: function (row) {
                	 var dataSourceConfig = row['dataSourceConfig'] || {};
                     if (typeof dataSourceConfig == "string"){
                         dataSourceConfig = (new Function("return " + dataSourceConfig))();
                     }
                     return dataSourceConfig;
                }}
            },
            { display: "分组号", name: "groupId", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'spinner', required: true}
            },
            { display: "业务处理人参数", name: "bizHandlerParam", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text' }
            },
            { display: "必经节点", name: "mustPass", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            /*{ display: "允许加签", name: "allowAdd", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            { display: "允许中止", name: "allowAbort", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            { display: "允许转交", name: "allowTransfer", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            { display: "是否计时", name: "needTiming", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            { display: "限制时间", name: "limitTime", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text', required: false }
            },*/
            { display: "发送消息", name: "sendMessage", width: 100, minWidth: 60, type: "string", align: "left",
            	editor: { type: 'combobox', data: yesNoList, required: true }, render: yesNoRender
            },
            { display: "审批要点", name: "helpSection", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text', required: false}
            },
            { display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left",
                editor: { type: 'spinner', required: true}
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/approvalRule/queryApprovalRuleHandlers.ajax',
        parms: {
            approvalRuleId: 0
        },
        pageSize: 20,
        width: '99%',
        height: '100%',
        heightDiff:-67,
        sortName: 'groupId,sequence',
        sortOrder: 'asc',
        isCacheScrollLeft:false,
        usePager: false,
        toolbar: toolbarOptions,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        enabledEdit: true,
        rownumbers: true,
        checkbox: true,
        autoAddRow: { handlerKindName: "", dataSourceConfig: "", kindId: 'chief',sendMessage:1}
    });
}

function yesNoRender(item, index, columnValue, columnInfo) {
    return yesNoList[columnValue];
}

function insertApprovalRule() {
   	if (!orgId) {
   		Public.errorTip("请选组织。");
	}
	if (!hasPermission){
  		Public.errorTip("您没有维护该组织的权限。");
	}
	
    if (!approvalRuleTree) {
        Public.errorTip("请选择规则节点。");
        return;
    }
    var rule = approvalRuleTree.getSelected();
    if (!rule) {
        Public.tip("请选择规则节点。");
        return;
    }
    
    if (rule.data.nodeKindId == '2') {
        Public.tip("不支持往该节点下面添加子节点。");
        return;
    }

    var procUnit = findProcUnit();
    if (!procUnit) {
        return;
    }
    if (parentId == '0') {
        Public.tip("请选择流程审批规则父节点。");
        return;
    }
    UICtrl.showAjaxDialog({
        title: "添加审批规则",
        width: 600,
        url: web_app.name + '/approvalRule/showInsertApprovalRule.load',
        param: {
        	orgId: orgId,
            procId: procUnit.procId,
            procName: procUnit.procName,
            procUnitId: procUnit.code,
            parentId: parentId,
            procUnitName: procUnit.name
        },
        init: initScopeDialog,
        ok: doInsertApprovalRule,
        close: dialogClose
    });
}

function getApprovalRuleId() {
    return $("#approvalRuleId").val() || 0;
}

function checkApprovalRuleId(id) {
    if (!id || id == 1) {
        Public.tip("无效的审批规则Id。");
        return 0;
    }
    return id;
}

function updateApprovalRule() {
    var id = getApprovalRuleId();
    if (!checkApprovalRuleId(id)) {
        return;
    }

    UICtrl.showAjaxDialog({
        title: "修改审批规则",
        width: 600,
        url: web_app.name + '/approvalRule/showUpdateApprovalRule.load',
        param: { id: id },
        init: initScopeDialog,
        ok: doUpdateApprovalRule, close: dialogClose
    });
}

function dialogClose() {
    if (refreshFlag) {
        refreshFlag = false;
    }
}

function deleteApprovalRule() {
    var id = getApprovalRuleId();
    if (!checkApprovalRuleId(id)) {
        return;
    }
    var ids = [id];
    var confirmMessage = '<span>您确定要删除选中数据吗?</span><span style="color:red;padding-top:2px;">删除后无法恢复。</span>';
    UICtrl.confirm(confirmMessage, function () {
        Public.ajax(web_app.name + '/approvalRule/deleteApprovalRule.ajax', {
            ids: $.toJSON(ids)
        }, function (data) {
            refreshApprovalRuleTree();
        });
    });
}

function refreshApprovalRuleTree() {
    var proc = findProc();
    if (!proc)
        return;

    var currentNode = approvalRuleTree.getSelected();
    var parentNode = null;
    if (currentNode) {
        if (parentId > 1) {
            parentNode = approvalRuleTree.getParent(currentNode.target);
        } else {
            parentNode = currentNode.data;
        }
        refreshNode({ manager: approvalRuleTree, queryAction: "/approvalRule/queryApprovalRule.ajax",
            params: { orgId: orgId, procId: proc.procId, procUnitId: proc.code, parentId: parentId }, pNode: parentNode
        });
    }
}

function getScopeDataParam(){
	var detailData = scopeGridManager.getData();
	return {detailData: $.toJSON(detailData)};
}

function doInsertApprovalRule() {
    _self = this;
    
    $('#submitForm').ajaxSubmit({ url: web_app.name + '/approvalRule/insertApprovalRule.ajax',
    	param: getScopeDataParam(),
    	success: function () {
            refreshApprovalRuleTree();
            _self.close();
        }
    });
}

function doUpdateApprovalRule() {
    _self = this;
    $('#submitForm').ajaxSubmit({ url: web_app.name + '/approvalRule/updateApprovalRule.ajax',
        param: getScopeDataParam(),
    	success: function () {
            refreshApprovalRuleTree();
            _self.close();
        }
    });
}

function findProcUnit() {
    var procUnit = $('#maintree').commonTree("getSelected");
    if (procUnit) {
        return procUnit;
    }
    return null;
}

function findProc() {
    var proc = $('#maintree').commonTree("getSelected");
    if (proc) {
        return proc;
    }

    Public.tip("流程环节未找到对应的流程！");
    return null;
}

function procNodeClick(nodeData) {
    if (!nodeData){
    	return;    
    }
    
    var params = {};
    $("#approvalRuleTree").removeAllNode();
    $("#divApprovalRuleTreeArea").append($('<ul id="approvalRuleTree"></ul>'));
    parentId = 0;
    if (nodeData.hasChildren == 0) {
    	params.parentId = 0;
        params.procId = nodeData.procId;
        params.procName = nodeData.procName;
        params.procUnitId = nodeData.code;
        params.procUnitName = nodeData.name;
        loadApprovalRuleTreeView(params);
    } else {
        refreshGrid(0);
    }
    closeChooseProcDialog(nodeData.fullName);
}

function setApprovalRuleTitle(approvalRule){
	var title = "审批规则信息";
    if (approvalRuleId && !Public.isBlank(approvalRule.fullName)){
    	title = "<span class='tomato-color'>[" + approvalRule.fullName + "]</span>" + "审批规则信息";
    }
    $("#group .titleSpan").html(title);
}

function refreshGrid(approvalRuleId) {
    elementGridManager.options.parms.approvalRuleId = approvalRuleId;
    elementGridManager.loadData();
    handlerGridManager.options.parms.approvalRuleId = approvalRuleId;
    handlerGridManager.loadData();
}

function loadApprovalRuleTreeView(options) {
    if (approvalRuleTree) {
        approvalRuleTree.clear();
        approvalRuleTree = null;
    }

    Public.ajax(web_app.name + "/approvalRule/queryApprovalRule.ajax", {
    	orgId: orgId, 
        procId: options.procId,
        parentId: options.parentId,
        procName: options.procName,
        procUnitId: options.procUnitId,
        procUnitName: options.procUnitName
    }, function (data) {
        approvalRuleTree = $("#approvalRuleTree").ligerTree({
            data: data.Rows,
            idFieldName: "id",
            parentIDFieldName: "parentId",
            textFieldName: "name",
            checkbox: false,
            iconFieldName: "icon",
            btnClickToToggleOnly: true,
            nodeWidth: 180,
            isExpand: false,
            isLeaf: function (data) {
                data.children = [];
                return data.hasChildren == 0;
            },
            onBeforeExpand: onApprovalRuleTreeBeforeExpand,
            onClick: function (node) {
                if (!node || !node.data){
                	return;
                }
                var approvalRule = node.data;
                parentId = node.data.id;
                $("#approvalRuleId").val(approvalRule.id);
                $("#approvalRuleProcId").val(approvalRule.procId);
                $("#approvalRuleProcName").val(approvalRule.procName);
                $("#approvalRuleProcUnitId").val(approvalRule.procUnitId);
                $("#approvalRuleProcUnitName").val(approvalRule.procUnitName);
                $("#approvalRuleName").val(approvalRule.name);
                $("#approvalRulePriority").val(approvalRule.priority);
                $("#createTime").val(approvalRule.createdDate);
                $("#creatorName").val(approvalRule.createdByName);
                $("#lastUpdateTime").val(approvalRule.lastModifiedDate);
                $("#lastUpdaterName").val(approvalRule.lastModifiedByName);
                $("#scopeKindId1").setValue(approvalRule.scopeKindId);
                //$("#scopeNames").val(approvalRule.scopeNames);
                $("#approvalRuleRemark").val(approvalRule.remark);
                
                $('#approvalRuleNodeKindId1').setValue(approvalRule.nodeKindId);
                $("#approvalRuleStatus0").setValue(approvalRule.status);
                
                
                refreshGrid(parentId);
                setApprovalRuleTitle(approvalRule); 
            }
        });
    });
}

function onApprovalRuleTreeBeforeExpand(node) {
    if (node.data.hasChildren) {
        if (!node.data.children || node.data.children.length == 0) {
            parentId = node.data.id;
            var proc = findProc();
            Public.ajax(web_app.name + "/approvalRule/queryApprovalRule.ajax", {
                orgId: orgId,
                procId: proc.procId,
                procUnitId: proc.code,
                parentId: parentId
            }, function (data) {
                approvalRuleTree.append(node.target, data.Rows);
            });
        }
    }
}

function checkItemDuplicate(grid, idFieldName, nameFiledName, message) {
    grid.endEdit();
    var data = grid.getData();
    var ids = [];
    var id;
    for (var i = 0; i < data.length; i++) {
        id = parseInt(data[i][idFieldName]);
        if ($.inArray(id, ids) > -1) {
            Public.tip(message + "[" + data[i][nameFiledName] + "]重复。");
            return false;
        }
        ids.push(id);
    }
    return true;
}

function saveApprovalRuleElements() {
    var elementData = DataUtil.getGridData({ gridManager: elementGridManager });
    if (!elementData){
    	return;
    }
    if (elementData.length == 0 ) {
    	Public.tip("数据没有变化。");
    	return;
    }
    
    $.each(elementData, function(i, o){
    	delete o.dataSourceConfig;
    });
    
	if (!checkItemDuplicate(elementGridManager, "elementCode", "elementName", "审批要素")) {
    	return;
    }

    Public.ajax(web_app.name + '/approvalRule/saveApprovalRuleElements.ajax',
        { approvalRuleId: parentId, data: Public.encodeJSONURI(elementData) },
        function () {
            elementGridManager.loadData();
        });
}

function saveApprovalRuleHandlers() {
    var handlerData = DataUtil.getGridData({ gridManager: handlerGridManager });
    if (!handlerData){
    	return;
    }
    if (handlerData.length == 0) {
    	Public.tip("数据没有变化。");
    	return;
    }
    
    $.each(handlerData, function(i, o){
    	delete o.dataSourceConfig;
    });
        
    Public.ajax(web_app.name + '/approvalRule/saveApprovalRuleHandlers.ajax',
        { approvalRuleId: parentId, data: Public.encodeJSONURI(handlerData) },
        function () {
            handlerGridManager.loadData();
        }
    );
}

function clearDetailKey(data) {
    $.each(data, function (i, o) {
        delete o["approvalRuleId"];
        delete o["id"];
    });
}

function copyApprovalRule() {
    elementData = elementGridManager.getData();
    clearDetailKey(elementData);
    handlerData = handlerGridManager.getData();
    clearDetailKey(handlerData);
    Public.tip("您已成功复制规则。");
}

function pasteApprovalRule() {
    if (!checkSelectedApprovalRule()) {
        return;
    }

    elementGridManager.appendRange(elementData);
    handlerGridManager.appendRange(handlerData);
    Public.tip("您已成功粘贴规则。");
}

function moveApprovalRule() {
    try {
        var selectedNode = approvalRuleTree.getSelected();
        if (!selectedNode || selectedNode.data.parentId == 0) {
            Public.errorTip("请选择审批规则节点。");
            return;
        }
    } catch (e) {

    }

    UICtrl.showDialog({
        title: "移动到...",
        width: 350,
        content: '<div style="overflow-x: hidden; overflow-y: auto; width: 340px;height:250px;"><ul id="movetree"></ul></div>',
        init: function () {
            var procUnit = findProcUnit();
            var url = "approvalRule/queryApprovalRule.ajax";
            $('#movetree').commonTree({
                loadTreesAction: url,      
                getParam: function(e){
                	return { orgId: orgId,  procId:  procUnit.procId, procName: encodeURI(procUnit.procName), procUnitId: procUnit.code,procUnitName: encodeURI(procUnit.name) };
                },
                IsShowMenu: false
            });
        },
        ok: function() {
        	doMoveApprovalRule.call(this);
        }
    });
}

function doMoveApprovalRule() {
	var _self = this;
    var newParentId = $('#movetree').commonTree('getSelectedId');
    if (!newParentId) {
        Public.errorTip("请选择需要移动节点。");
        return;
    }

    var currentNode = approvalRuleTree.getSelected();
    var currentParentId = currentNode.data.parentId;
    if (currentParentId == newParentId) {
        Public.errorTip("没有更新父节点，不能移动。");
        return;
    }

    var params = {};
    params.newParentId = newParentId;
    params.id = currentNode.data.id;

    if (params.newParentId == params.id) {
        Public.errorTip("父、子节点相同，不能移动。");
        return;
    }

    Public.ajax(web_app.name + "/approvalRule/moveApprovalRules.ajax", params, function (data) {
    	_self.close(); 
    	procNodeClick(treeManager.commonTree("getSelected"));
        Public.tip("您已成功移动审批规则。");
    });
}

function synApprovalRule() {
    var proc = findProc();
    if (proc == null || proc.procId == null || proc.nodeKindId == ProcNodeKind.FOLDER) {
        Public.errorTip("请选择需同步的流程节点。");
        return;
    }

    if (parentId == 0){
        Public.errorTip("请选需同步的审批规则。");
        return;
    }
    
    var msg = proc.name;
    if (parentId > 1){
    	var selectedNode = approvalRuleTree.getSelected();
    	msg += "\\" + selectedNode.data.name;
    }
    msg = "同步操作将同步流程“" + msg + "”下的审批规则数据，您是否确定同步？";
    
    UICtrl.confirm(msg, function () {
        Public.ajax(web_app.name + '/approvalRule/synApprovalRule.ajax',
            { orgId: orgId, procKey: proc.procId, approveRuleId: parentId },
            function (data) {
                if (data.returnCode != 0) {
                    Public.errorTip(data.returnMessage);
                } else {
                    Public.tip("您已成功同步流程审批规则。");
                }
            }
        );
    });
}

function setToolBarEnabled(value){
	 if (Public.isReadOnly){
		 return;
	 }
	
	var enableValue = value ? "enable" : "disable";
	$("#toolBar").toolBar(enableValue, "insert", "update", "delete", "paste", "move", "copy", "syn");
	//toolbar = 
	
	var toolbarManager = $('#elementGrid').prev('div.ui-toolbar').data('ui_tool_bar');
	//elementGridManager.toolbarManager;
	if (value){
		toolbarManager.enable("menuAdd");
		toolbarManager.enable("menuSave");
		toolbarManager.enable("menuDelete");
	}else{
		toolbarManager.disable("menuAdd");
		toolbarManager.disable("menuSave");
		toolbarManager.disable("menuDelete");
	}
	
	var toolbarManager = $('#handlerGrid').prev('div.ui-toolbar').data('ui_tool_bar');
		//handlerGridManager.toolbarManager;
	if (value){
		toolbarManager.enable("menuAdd");
		toolbarManager.enable("menuSave");
		toolbarManager.enable("menuDelete");
		toolbarManager.enable("menuaddConfigStep");
	}else{
		toolbarManager.disable("menuAdd");
		toolbarManager.disable("menuSave");
		toolbarManager.disable("menuDelete");
		toolbarManager.disable("menuaddConfigStep");		
	}
}

function loadOrgTreeView() {
    $('#orgTree').commonTree({
        loadTreesAction: '/org/queryOrgs.ajax',
        parentId: '',
        getParam: function (e) {
            if (e) {
                return { showDisabledOrg: 0, displayableOrgKinds: "ogn" };
            }
            return { showDisabledOrg: 0 };
        },
        manageType: 'procApprovalRule',
        isLeaf: function (data) {
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
        },
        onClick: function (data) {
        	orgId = data.id;
        	//TODO 
        	//hasPermission = data.managerPermissionFlag;
        	hasPermission = true;//data.managerPermissionFlag;
        	setToolBarEnabled(hasPermission);
        	procNodeClick(treeManager.commonTree("getSelected"));
        	closeChooseOrgDialog(data.fullName);
        },
        IsShowMenu: false
    });
}

function showSelectOrgDialog(){
	var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
	selectOrgParams['selectableOrgKinds']='ogn';
	selectOrgParams['displayableOrgKinds'] = "ogn";
	var options = { params: selectOrgParams,title : "选择组织机构",
		confirmHandler: function(){
			var rows = this.getSelectedData();
			if (rows.length == 0) {
				Public.errorTip("请选择数据。");
				return;
			}

		    $.each(rows, function (i, o) {
		    	scopeGridManager.addRow({ orgId: o.id, fullName: o.fullName });
		    });
			this.close();
		}
	};
	OpmUtil.showSelectOrgDialog(options);
}

function initScopeDialog(){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions(
	        { addHandler: showSelectOrgDialog,
	         deleteHandler: function () {
	                DataUtil.delSelectedRows({ action: "approvalRule/deleteApprovalRuleScopes.ajax",
	                    idFieldName: "id",
	                    param: {approvalRuleId: getApprovalRuleId()},
	                    gridManager: scopeGridManager,
	                    onSuccess: function () {
	                    	scopeGridManager.loadData();
	                    }
	                });
	            }
	        });
	
	scopeGridManager = UICtrl.grid('#scopeGrid',{
	    columns: [
	            { display: "组织名称", name: "fullName", width: 400, minWidth: 60, type: "string", align: "left" }
	             ],
	    dataAction: "server",
	    url: web_app.name + '/approvalRule/queryApprovalRuleScopes.ajax',
	    parms: { approvalRuleId: getApprovalRuleId() },
	    toolbar: toolbarOptions,
	    width: "100%",
	    height: 180,
	    sortName: 'fullName',
	    sortOrder: "asc",
	    heightDiff: -12,
	    enabledEdit: true,
	    checkbox: true,
	    fixedCellHeight: true,
	    selectRowButtonOnly: true,
	    rownumbers: true,
	    onLoadData: function () {
	        return Public.isNotBlank(getApprovalRuleId());
	    }
	});
}
