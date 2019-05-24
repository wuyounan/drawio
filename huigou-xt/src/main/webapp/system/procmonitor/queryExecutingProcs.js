var gridManager = null, refreshFlag = false;
$(document).ready(function () {
    initializeUI();
    initializeGrid();
});

function initializeUI() {
    $('#procID_Name').searchbox({
        type: 'bpm',
        name: 'procIds',
        getParam: function () {
            return {};
        },
        back: {
            procId: '#procId',
            name: '#procID_Name_text'
        },
        onChange: function () {

        }
    });
}

function initializeGrid() {
    gridManager = UICtrl.grid('#maingrid', {
        columns: [{
            display: "流程名称",
            name: "title",
            width: 300,
            minWidth: 200,
            type: "string",
            align: "left",
            render: function (item) {
                return "<a href='#' class='GridStyle' onclick='showDoc(\"" + item.procId + "\",\"" + item.bizId + "\",\"" + item.title + "\")'>" + item.title + "</a>";
            }
        }, {
            display: "状态",
            name: "statusName",
            width: 80,
            minWidth: 80,
            type: "string",
            align: "center"
        }, {
            display: "开始时间",
            name: "startTime",
            width: 150,
            minWidth: 150,
            type: "string",
            align: "left"
        }, {
            display: "业务类型",
            name: "procName",
            width: 100,
            minWidth: 60,
            type: "string",
            align: "left"
        }, {
            display: "流程发起部门",
            name: "deptName",
            width: 120,
            minWidth: 100,
            type: "string",
            align: "left"
        }, {
            display: "操作",
            name: "bizId",
            width: 60,
            minWidth: 60,
            type: "int",
            align: "center",
            render: function (item) {
                return "<a href='#'  class='GridStyle' onclick='sampling(\"" + item.procId + "\",\"" + item.bizId + "\",\"" + item.title + "\")'>抽检</a>";
            }
        }],
        dataAction: 'server',
        url: web_app.name + '/procMonitor/sliceQueryExecutingProcs.ajax',
        parms: {
            procId: '',
            startTime: '',
            endTime: '',
            procName: '',
            deptName: ''
        },
        sortName: "start_time",
        sortOrder: "desc",
        title: '在办业务事项',
        width: '99.8%',
        height: '100%',
        heightDiff: -8,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {

        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

function query(obj) {
    var param = $(obj).formToJSON();
    UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

function resetForm(obj) {
    $(obj).formClean();
}

function sampling(procId, bizId, bizName) {
	UICtrl.addTabItem({
        tabid: 'ask1' + bizId,
        text: '业务抽检',
        url: web_app.name + '/procMonitor/showAskDetail.do?mode=ask&parentTabId=queryExecutingProcs&exceptionKindId=' + ExceptionKind.NONE + '&kindId=' + AskKind.SAMPLING + '&bizId=' + bizId + '&procId=' + procId + '&bizName=' + encodeURIComponent(bizName)
    });
}

function showDoc(procId, bizId, bizName) {
	UICtrl.addTabItem({
        tabid: 'ask1' + bizId,
        text: '业务抽检',
        url: web_app.name + '/procMonitor/showAskDetail.do?mode=view&bizId=' + bizId + '&procId=' + procId
    });
}
