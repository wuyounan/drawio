var elementGridManager = null,  handlerGridManager = null;

$(document).ready(function () {
    initializeUI();
    bindEvents();

    loadProcTreeView();
    initializeGrid();
    
    function autoSetWrapperDivHeight() {
        $('html').addClass("html-body-overflow");
        var div = $('#mainWrapperDiv'), pageSize = UICtrl.getPageSize();
        if ($.browser.msie && $.browser.version < 8) {
            div.height(pageSize.h - 10);
        }
        $('#divTreeArea').height(pageSize.h - 40);
        $('#approvalRule').height(pageSize.h - 50);
        var str_data = 'resize-special-event';
        div.data(str_data, pageSize);
        setInterval(function () {
            var _size = UICtrl.getPageSize(), data = div.data(str_data);
            if (_size.h !== data.h) {
                if ($.browser.msie && $.browser.version < 8) {
                    div.data(str_data, _size).height(_size.h - 10);
                }
                $('#divTreeArea').height(_size.h - 40);
                $('#approvalRule').height(_size.h - 50);
            }
        }, 140);
    }
    
    function initializeUI() {
    	autoSetWrapperDivHeight();
        UICtrl.layout("#layout", {
            leftWidth: 200,
            heightDiff: -5,
            onSizeChanged: function () {
                elementGridManager.reRender();
                handlerGridManager.reRender();
            }
        });
    }
});

function bindEvents() {
    $("#btnGetHandler").click(function(){
    	var procUnit = findProcUnit();
    	if (procUnit == null){
    		Public.tip("请选择流程环节。");
    	}
    	
    	Public.ajax(web_app.name + '/approvalRule/queryApprovalRuleApply.ajax',
    			{ procId: procUnit.procId,
            procUnitId: procUnit.procUnitId,
            bizParams: getBizParams()},function(result){
            	handlerGridManager.set({data: result});
            	if (result.approvalRule){
            		$("#approvalRuleName").html(result.approvalRule.name);
            	}
    		});
    });
}

function getBizParams(){
	elementGridManager.endEdit();
	var params ={};
	var data = elementGridManager.getData();
	$.each(data, function(i, o){
		params[o["elementName"]]= o["fvalue"];
	});
	return Public.encodeJSONURI(params);
}

function findProcUnit() {
    var procUnit = $('#maintree').commonTree("getSelected");
    if (procUnit && procUnit.nodeKindId == 2) {
        return procUnit;
    }
    return null;
}

// 初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function () {
            elementGridManager.addRow({elementName: "", fvalue: ""});
        },
        deleteHandler: function () {
        	elementGridManager.deleteSelectedRow();
        }
    });
    
    elementGridManager = UICtrl.grid('#elementGrid', {
        columns: [
            { display: "审批要素", name: "elementName", width: 200, minWidth: 60, type: "string", align: "left",
                editor: { type: 'text', required: true } },
            { display: "审批要素值", name: "fvalue", width: 200, minWidth: 60, type: "string", align: "left",
                    editor: { type: 'text', required: true } 
            }
        ],
        dataAction: 'local',
        usePager: false,
        width: '99.9%',
        height: 200,
        toolbar: toolbarOptions,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        enabledEdit: true,
        rownumbers: true
    });
    
    handlerGridManager = UICtrl.grid('#handlerGrid', {
        columns: [
            { display: "流程环节", name: "handleKindName", width: 150, minWidth: 60, type: "string", align: "left", frozen: true },
            { display: "协作模式", name: "cooperationModelId", width: 150, minWidth: 60, type: "string", align: "left" },
            { display: "机构", name: "orgName", width: 100, minWidth: 60, type: "string", align: "left" },
            { display: "部门", name: "deptName", width: 100, minWidth: 60, type: "string", align: "left" },
            { display: "处理人", name: "handlerName", width: 100, minWidth: 60, type: "string", align: "left" },
            { display: "分组号", name: "groupId", width: 100, minWidth: 60, type: "string", align: "left" },
            { display: "排序号", name: "sequence", width: 100, minWidth: 60, type: "string", align: "left"  }//,
            //{ display: "处理人全名称", name: "fullName", width: 100, minWidth: 60, type: "string", align: "left" }
        ],
        dataAction: 'local',
        width: '99.9%',
        height: '100%',
         usePager: false,
        heightDiff: -20,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        rownumbers: true
    });
}

function procNodeClick(nodeData) {
	var jsonObj = {};
	elementGridManager.set({ data: jsonObj }); 
	handlerGridManager.set({ data: jsonObj }); 
	$("#approvalRuleName").html("");
}

