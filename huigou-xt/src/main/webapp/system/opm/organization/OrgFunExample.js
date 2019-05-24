var gridManager, psmManager, orgId, managerTypeId;
$(function() {
	bindEvents();
	initializeGrid();
	initializePsmGrid();

	function getQueryParameters() {
		orgKindId = $("#orgKindId").val();
		isMultipleSelect = $("#isMultipleSelect").val();
	}

	function bindEvents() {
		$("#btnFindManager").click(function() {
			//var params =  { callFunc: "findManagers(\"" + orgId + "\",\"" + managerTypeId + "\",false,\"\")" };
			//var params =  { callFunc: "currentCenterManager()" }; 
			//var params =  { callFunc: "findSubordinationsByOrgManageType('" + orgId + "','" + managerTypeId + "','',true,false)" };
			//var params =  { callFunc: "findBizLineManagers('" + orgId + "','" + managerTypeId + "')" };
			Public.ajax(web_app.name + "/org/test.ajax", {}
			, function(data) {
				alert(data);
			});
		});
	}

	function initializeGrid() {
		gridManager = UICtrl.grid("#orgGrid", {
			columns : [ { display : "全路径", name : "fullId", 	width : "300", minWidth : 60, type : "string", align : "left" }, 
			            { display : "全名称", name : "fullName", width : "300", minWidth : 60, type : "string", align : "left" } 
			],
			usePager: false,
			width : "100%",
			height : "50%",
			heightDiff : -7,
			fixedCellHeight : true,
			selectRowButtonOnly : true,
			onDblClickRow : function(data, rowIndex, rowObj) {
				var params =  { callFunc: "findPersonMembersInOrg(\"" + data.fullId + "\",true)" };
				Public.ajax(web_app.name + "/org/testOrgFunc.ajax", params
				, function(result) {
					var data = {
						Rows : result
					};
					psmManager.options.data = data;
					psmManager.loadData();
				});
			}
		});
	}
	
	function initializePsmGrid() {
		psmManager = UICtrl.grid("#psmGrid", {
			columns : [ { display : "全路径", name : "fullId", 	width : "300", minWidth : 60, type : "string", align : "left" }, 
			            { display : "全名称", name : "fullName", width : "300", minWidth : 60, type : "string", align : "left" } 
			],
			usePager: false,
			width : "100%",
			height : "50%",
			heightDiff : -7,
			fixedCellHeight : true,
			selectRowButtonOnly : true
		});
	}
});

function showSelectOrgDialog() {
	var params = {
		"filter" : "",
		"multiSelect" : true,
		"rootFilter" : "SA_OPOrg.sParent is null",
		"manageCodes" : "",
		displayableOrgKinds : "ogn,dpt,pos,psm",
	    selectableOrgKinds : "psm",
		"includeDisabledOrg" : false,
		"listMode" : false,
		"showCommonGroup" : false,
		"cascade" : true,
		"selected" : []
	};
	
	UICtrl.showFrameDialog({
		title : "选择组织",
		url : web_app.name + "/org/showSelectOrgDialog.do",
		param : params,
		width : 700,
		height : 400,
		ok : function(data) {
			_self = this;
			var data = this.iframe.contentWindow.selectedData;
			if (!data)
				return;
			orgId = data[0].id;
			$("#org").val(data[0].name);
			_self.close();
		},
		close : function() {

		}
	});
}

function showSelectBizManagementTypeDialog() {
	var params = {
		confirmHandler : onSelectBizManagementTypeHandler,
		closeHandler : function() {
		}
	};
	OpmUtil.showSelectBizManagementTypeDialog(params);
}

function onSelectBizManagementTypeHandler() {
	_self = this;
	var fn = this.iframe.contentWindow.getBizManagementTypeData;
	var data = fn();
	managerTypeId = data[0].code;
	$("#managerType").val( data[0].name);
	_self.close();
}