var gridManager;
$(document).ready(function() {
	initializeGrid();
	$('body').addClass('dom-overflow');
	function initializeGrid() {
		var bizId = Public.getQueryStringByName("bizId");
		gridManager = UICtrl.grid("#maingrid", {
			columns: [ 
			            { display: "common.field.subprocunitname",remark:"环节名称",  name: "name", width: 120, minWidth: 60, type: "string", align: "left",
			            	render:function(item){
			            		if (Public.isNotBlank(item.subProcUnitName)){
			            			return item.name + "." + item.subProcUnitName;
			            		}
			            		return item.name;
			            	}
			            }, 
			            { display: "common.field.handlername",remark:"处理人", name: "handler", width: 160 , minWidth: 80, type: "string", align: "left" },
			            { display: "common.field.handleresult",remark:"处理结果",  name: "result", width: 60, minWidth: 60, type: "string", align: "left",
			               render: function(item){
			               	  return HandleResult.getDisplayName(item.result);
			               }
			            },
			            { display: "common.field.opinion",remark:"处理意见",  name: "opinion", width: 220, minWidth: 60, type: "string", align: "left",
			            	 render: function(item){
			            		 if (item.manuscript){
			            			 return '<img id="img_'+ item.id + ' class="manuscript" style="height:60px;width:100%;" src="' +  item.manuscript + '"></img>';
			            		 }else{
			            			 return item.opinion;
			            		}
			            	 }
			            },
                        { display: "common.field.taskKind",remark:"任务类型",  name: "taskKindTextView", width: 70 , minWidth: 60, type: "string", align: "left" },                      
                		{ display: "common.field.startTime",remark:"开始时间",  name: "startTime", width: 130, minWidth: 60, type: "time", align: "left" },
			            { display: "common.field.endTime",remark:"结束时间",  name: "endTime", width: 130, minWidth: 60, type: "time", align: "left" },
			            { display: "common.field.duration",remark:"耗时(小时)",  name: "duration", width: 80, minWidth: 60, type: "time", align: "left" },
			            { display: "common.field.status",remark:"状态",  name: "statusName", width: 60, minWidth: 60, type: "time", align: "left" }
			          ],
			dataAction: "server",
			url: web_app.name + "/workflow/queryApprovalHistoryByBizId.ajax",
			parms: {bizId: bizId},
			usePager: false,
			width: "99.8%",
			height: "100%",
			rownumbers: true,
			heightDiff: -20,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onAfterShowData: function (currentData) {
                setTimeout(function () {
                    UICtrl.setGridRowAutoHeight($('#maingrid'));
                }, 200);
            }			
		});
	}
});