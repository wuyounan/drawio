var gridManager = null, refreshFlag = false, bizId;
$(document).ready(function() {
	initializeUI();
});

function initializeUI() {
	$('#infoTab').tab(); 

	var mode = $('#mode').val() || "view";
	if (mode == "view") {
		$("#ask_tab").hide();
		$("#ask_div").hide();
		
	} else if (mode == "viewAsk") {

	} else if (mode == "ask") {
		$("#ask").show();
	} else if (mode == "reply") {
		$("#ask_reply").show();
	} else if (mode == "end") {
		$("#ask_end").show();
	}

    if(mode != "view") $("#ask_tab").click(); 
    else{
    	$("#info_tab").click(); 
    }

	if (mode != "view" && $('#askStatus').val() != AskStatus.UnAsk)
		initializeGrid();
	bizId = $('#bizId').val() || '';
	$("#accepterName").searchbox({
		type : "oa",
		name : "procUnitHandlers",
		getParam : function() {
			return {
				bizId : bizId
			};
		},
		back : {
			handlerId : "#accepterId",
			handlerName : "#accepterName_text",
		},
		onChange : function() {
			$('#accepterName').val($('#accepterName_text').val());
		}
	});
	$('#accepterName_text').val($('#accepterName').val());
	// 业务流程
	var procId = $('#procId').val();
	var url = web_app.name;
	if (procId == 'planeTicketProc' || procId == 'postDocProc' || procId == 'receiptDocProc') {//收文、发文、传阅件
		url += '/planeTicket/forwardPlaneTicketUpdate.job?useDefaultHandler=0&bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'motionApplyProc') {//议案申请
		url += '/motionApply/showUpdateMotionApply.job?bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'receiptDoc'){//督办事项
		url += '/officialDocument/forwardOfficialDocumentUpdate.job?useDefaultHandler=0&bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'outWorkProc'){//异常考勤
		url += '/outWork/showUpdateOutWork.job?bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'collaborativeProcessProc'){//协同流程
		url += '/collaborativeProcess/showUpdateCollaborativeProcess.job?bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'overTimeProc'){//加班流程
		url += '/overtime/showUpdateOvertime.job?bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'leaveProc'){//请假
		url += '/leave/showUpdateLeave.job?bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'blackProc'){//黑名单
		url += '/blackApply/showUpdateBlackApply.job?bizId=' + bizId + '&isReadOnly=true';   
	} else if (procId == 'documentExchangeProc'){//公文交换
		url += '/documentExchange/showUpdateDocumentExchange.job?bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'meetPlanApplyProc'){//上会方案
		url += '/meetPlanApply/showUpdateMeetPlanApply.job?bizId=' + bizId + '&isReadOnly=true'
	} else if (procId == 'meetingMinutesDraftProc'){//会议纪要草稿
		url += '/meetingMinutesDraft/showUpdateMeetingMinutesDraft.job?bizId=' + bizId + '&isReadOnly=true';
	} else if (procId == 'subReceiptDocProc'){//会议纪要流程
		url += '/meetingMinutes/showUpdateMeetingMinutes.job?bizId=' + bizId + '&isReadOnly=true';
	}

	$('#flowIframe').attr('src', url);
}

function initializeGrid() {
	gridManager = UICtrl.grid('#maingrid', {
		columns : [ {
			display : "发送人",
			name : "senderName",
			width : 100,
			minWidth : 60,
			type : "string",
			align : "left"
		}, {
			display : "发送时间",
			name : "sendTime",
			width : 140,
			minWidth : 140,
			type : "string",
			align : "left"

		}, {
			display : "问题",
			name : "askContent",
			width : 200,
			minWidth : 120,
			type : "string",
			align : "left"
		}, {
			display : "接受人姓名",
			name : "accepterName",
			width : 100,
			minWidth : 100,
			type : "string",
			align : "left"
		}, {
			display : "回复状态",
			name : "status",
			width : 80,
			minWidth : 80,
			type : "int",
			align : "center",
			render : function(item) {
				return AskDetailStatus.getDisplayName(item.status);
			}
		}, {
			display : "回复内容",
			name : "replyContent",
			width : 150,
			minWidth : 150,
			type : "string",
			align : "left"
		}, {
			display : "回复时间",
			name : "replyTime",
			width : 140,
			minWidth : 140,
			type : "string",
			align : "left"
		} ],
		dataAction : 'server',
		url : web_app.name + '/ask/queryAskDetails.ajax',
		parms : {
			askId : $('#id').val() || ''
		},
		sortName : "send_time",
		sortOrder : "asc",
		title : '被询问人列表',
		width : '99.8%',
		height : '250',
		usePager : false,
		rownumbers : true,
		heightDiff : -3,
		checkbox : false,
		fixedCellHeight : true,
		selectRowButtonOnly : true,
		onDblClickRow : function(data, rowindex, rowobj) {

		}
	});
}

function submitAsk() {
	var url = "/ask/submitAsk.ajax";
	$('#submitForm').ajaxSubmit({
		url : web_app.name + (url),
		success : function() {
			Public.tip('提交询问成功.');
			var  parentTabId = Public.getQueryStringByName("parentTabId") || "";
            if(parentTabId)
			    top.closeTabItemAndReloadParentTab(parentTabId);   
            else
                top.closeTabItem();
		}
	});
}

function replyAsk() {
	var url = "/ask/replyAsk.ajax";
	$('#submitForm').ajaxSubmit({
		url : web_app.name + (url),
		success : function() {
			Public.tip('询问回复成功.');
			var  parentTabId = Public.getQueryStringByName("parentTabId") || "";
            if(parentTabId)
			    top.closeTabItemAndReloadParentTab(parentTabId);   
            else
                top.closeTabItem();
		}
	});
}

function stopAsk() {
	var url = "/ask/stopAsk.ajax";
	$('#submitForm').ajaxSubmit({
		url : web_app.name + (url),
		success : function() {
			Public.tip('询问终结成功.');
            var  parentTabId = Public.getQueryStringByName("parentTabId") || "";
            if(parentTabId)
			    top.closeTabItemAndReloadParentTab(parentTabId);   
            else
                top.closeTabItem();
		}
	});
}