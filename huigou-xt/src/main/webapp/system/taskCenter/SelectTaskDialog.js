var gridManager, selectedGridManager, dateRangeKindId = 1, requestViewTaskKind, taskId;
var selectedData = [];

$(function () {
    bindEvents();

    requestViewTaskKind = Public.getQueryStringByName("viewTaskKind");
    taskId = Public.getQueryStringByName("taskId");

    if (Public.isBlank(requestViewTaskKind)) {
        requestViewTaskKind = '1';
    }

    initUI();
    
    function initUI() {
    	$('html').addClass('dom-overflow');
        UICtrl.layout($("#pageLayout"), { leftWidth:3,onSizeChanged:function(){
        	var windowHeight = $(window).height();
        	if(windowHeight>10){
        		if(!gridManager){
        			initGrid(windowHeight*0.6);
        		}
        		if (!selectedGridManager) {
        	    	initSelectedGrid(windowHeight*0.4);  
        	    }
        	}
        }});
        $("#pageLayout").find('.ui-layout-center>.ui-layout-warp').css({border:0});
        var parentWidth=parent.getDefaultDialogWidth();
    	if(parentWidth < 768){
    		$("#pageLayout").find('.ui-layout-left').hide();
    	}
        $("#selectViewTaskKind").combox({ data:$("#selectViewTaskKind1").combox("getJSONData") });
        
    }

    function bindEvents() {
        $("#selectDateRange").combox({
            onChange: function (item) {
                dateRangeKindId = item.value;
                (dateRangeKindId == 10) ? $("#customDataRange").show() : $("#customDataRange").hide();
            }
        });

        $("#selectViewTaskKind").combox({ checkbox: true });

        //我的任务事件绑定
        $('#myselfTaskSearch').bind('click', function (e) {
            var $clicked = $(e.target || e.srcElement);
            if ($clicked.is('a')) {
                var taskKind = $clicked.parent().attr('taskKind');
                gridManager.options.parms = {};
                $("#searchWord").val("");
                UICtrl.gridSearch(gridManager, { queryCategory: "myTransaction", viewTaskKindList: taskKind });
            }
        });
       
    }

    /**
    * 初始化任务列表
    */
    function initGrid(height) {
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
            		   { display: "操作", name: "operate", width: 60, minWidth: 30, type: "string", align: "left",frozen: true,
            		     	render: function (item) { 
								return '<a href="javascript:void(0);" class="btn btn-link btn-sm addHandler"><i class="fa fa-plus"></i>&nbsp;添加</a>';
							}
            		   },
			           { display: "主题", name: "description", width: 300, minWidth: 60, type: "string", align: "left" },
			           { display: "编码", name: "bizCode", width: 120, minWidth: 60, type: "string", align: "left" },
			           { display: "申请人", name: "applicantPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			           { display: "名称", name: "name", width: 120, minWidth: 60, type: "string", align: "left" },
			           { display: "状态", name: "statusName", width: 80, minWidth: 60, type: "string", align: "left" },
			           { display: "提交人", name: "creatorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			           { display: "提交时间", name: "startTime", width: 140, minWidth: 60, type: "time", align: "left" },
			           { display: "执行人", name: "executorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			           { display: "执行时间", name: "endTime", width: 140, minWidth: 60, type: "time", align: "left" }
			 ],
            dataAction: "server",
            url: web_app.name + "/workflow/queryTasks.ajax",
            pageSize: 20,
            title: '可选关联任务',
            parms: { queryCategory: "myTransaction", viewTaskKindList: requestViewTaskKind },
            width: "99.8%",
            height: height,
            sortName: 'startTime',
            sortOrder: 'desc',
            onDblClickRow: function (data, rowindex, rowobj) {
                appendRow(data);
                reloadGrid();
            }
        });
        //通过任务主题、单据编号快速搜索
        UICtrl.createGridQueryBtn('#maingrid',function(param){
             UICtrl.gridSearch(gridManager, { searchContent: encodeURI(param) });
		});
		$("#maingrid").on('click',function(e){
			var $clicked = $(e.target || e.srcElement);
			if($clicked.is('a.addHandler')){
				setTimeout(function(){
					 appendRow(gridManager.getSelected());
	           		 reloadGrid();
				},0);
			}
		});
    }

    /**
    * 初始化选择任务列表
    */
    function initSelectedGrid(height) {
        selectedGridManager = UICtrl.grid("#selectedgrid", {
            columns: [
            			{ display: "操作", name: "operate", width: 60, minWidth: 30, type: "string", align: "left",frozen: true,
            		     	render: function (item) { 
								return '<a href="javascript:void(0);" class="btn btn-link btn-sm deleteHandler"><i class="fa fa-trash-o"></i>&nbsp;删除</a>';
							}
            		    },
            			{ display: "主题", name: "description", width: 300, minWidth: 60, type: "string", align: "left" },
            			{ display: "编码", name: "bizCode", width: 120, minWidth: 60, type: "string", align: "left" },
			            { display: "名称", name: "name", width: 120, minWidth: 60, type: "string", align: "left" },
			            { display: "状态", name: "statusName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "提交人", name: "creatorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "提交时间", name: "startTime", width: 140, minWidth: 60, type: "time", align: "left" },
			            { display: "执行人", name: "executorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "执行时间", name: "endTime", width: 140, minWidth: 60, type: "time", align: "left" }
			],
            width: "99.8%",
            height: height,
            heightDiff: -23,
            usePager: false,
            onDblClickRow: function (data, rowindex, rowobj) {
            	 deleteRow(data);
   				 reloadGrid();
            }
        });
        $("#selectedgrid").on('click',function(e){
			var $clicked = $(e.target || e.srcElement);
			if($clicked.is('a.deleteHandler')){
				setTimeout(function(){
					 deleteRow(selectedGridManager.getSelected());
	           		 reloadGrid();
				},0);
			}
		});
        setTimeout(function(){ reloadGrid();},100);
    }
});

function getStartDate() {
    return $("#editStartDate").val();
}

function getEndDate() {
    return $("#editEndDate").val();
}

function query(obj) {
    if (dateRangeKindId == 10 && (!getStartDate() || !getEndDate())) {
        Public.tip("请选择开始日期和结束日期！");
        return;
    }

    $('div.taskCenterChoose').removeClass('taskCenterChoose');
    var param = $(obj).formToJSON();
    param.queryCategory = "taskQuery";
    UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
	if(selectedGridManager){
		var data = { Rows: selectedData };
	    selectedGridManager.options.data = data;
	    selectedGridManager.loadData();
	}
}

function appendRow(o) {
    var added = false;
    for (var j = 0; j < selectedData.length; j++) {
        if (selectedData[j].id == o.id) {
            added = true;
            break;
        }
    }

    if (!added) {
        var row = $.extend({},o);
        selectedData[selectedData.length] = row;
    }
}

function deleteData() {
    var rows = selectedGridManager.getSelecteds();
    if (!rows || rows.length < 1) {
        Public.tip('请选择要删除的数据。');
        return;
    }
    for (var i = 0; i < rows.length; i++) {
        deleteRow(rows[i]);
    }
    reloadGrid();
}

function deleteRow(data) {
    for (var j = 0; j < selectedData.length; j++) {
        if (selectedData[j].id == data.id) {
            selectedData.splice(j, 1);
            break;
        }
    }
}
