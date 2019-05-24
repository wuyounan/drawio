var gridManager, parentId, searchContentDefalutValue = "在主题、编号中搜索"; 

$(function () {	
    initUI();
    bindEvents();
    
    loadOrgTreeView();
    initGrid();
    
    function initUI(){
		UICtrl.layout("#layout", {leftWidth : 250,heightDiff : -5});
    }
    
    function bindEvents(){
    	$("#searchContent").on("focus", function() {
    		if ($(this).val() == searchContentDefalutValue){
    			$(this).val("");
    		}
    	}).on("blur", function() {
    		if ($(this).val() == ""){
    			$(this).val(searchContentDefalutValue);
    		}
    	});
    	
    	$("#btnQuery").click(function() {
    		alert("");
    		alert(null);
    		alert(undefined);
    		alert(false);
    		
    		if (Public.isBlank(parentId)){
    			Public.errorTip("请选择人员。");
    			return;
    		} 
    		
    		if (!$(this.form).formToJSON()){
    			return;
    		}
			
    		var params = {};
			
			params.startDate = getStartDate();
			params.endDate = getEndDate();
			
			params.operatorPersonId = OpmUtil.getPersonIdFromPsmId(parentId);
			params.searchContent = getSearchContent();
			params.viewTaskKindList = $("#selectViewTaskKind").val();
			
			UICtrl.gridSearch(gridManager, params);
		});
		
		$("#btnReset").click(function() {
			$(this.form).formClean();
		});
    }
    
    function initGrid() {
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
			            { display: "名称", name: "name", width: 120, minWidth: 60, type: "string", align: "left" },
			            { display: "主题", name: "description", width: 300, minWidth: 60, type: "string", align: "left" },
			            { display: "申请人", name: "applicantPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "提交人", name: "creatorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "提交人机构", name: "creatorOgnName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "提交人部门", name: "creatorDeptName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "类型", name: "catalogId", width: 80, minWidth: 60, type: "string", align: "left",
			                render: function (item) {
			                    var catalogName = "";
			                    if (item.catalogId == "process") {
			                        catalogName = "流程任务";
			                    } else {
			                        catalogName = "协同任务";
			                    }
			                    return catalogName;
			                }
			            },
			            { display: "状态", name: "statusName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "编码", name: "bizCode", width: 120, minWidth: 60, type: "string", align: "left" },
			            { display: "提交时间", name: "startTime", width: 140, minWidth: 60, type: "time", align: "left" },
			            { display: "执行人", name: "executorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
			            { display: "执行时间", name: "endTime", width: 140, minWidth: 60, type: "time", align: "left" }
			 ],
            dataAction: "server",
            url: web_app.name + "/workflow/queryTasks.ajax",
            pageSize: 20,
            parms: { queryCategory: "dimissionPerson", dateRange: 10 },
            width: "99.8%",
            height: '100%',
            rownumbers: true,
            heightDiff: -10,
            sortName: 'startTime',
            sortOrder: 'desc',
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                browseTask(data);
            },
            onLoadData: function(){
            	return (!Public.isBlank(parentId));
            }
        });
        UICtrl.setSearchAreaToggle(gridManager, false);
    }
    
    function loadOrgTreeView() {
    	$('#orgTree').commonTree({	
    		loadTreesAction: web_app.name +'/org/queryOrgs.ajax',
    		parentId: "orgRoot",
    		getParam: function (e) {
    			var params = {};
    			
    			params.showDisabledOrg = 1;
    			params.showMasterPsm = 1;
    			params.showVirtualOrg = 0;
    			
    			return params;
            },
            manageType: 'queryDimissionPersonTask',
    		isLeaf : function(data) {
    			data.nodeIcon = OpmUtil.getOrgImgUrl (data.orgKindId, data.status, false);
    		},
    		onClick : function(data) {
    			if (!data){
    				return;
    			}
    			
    			if (data.orgKindId == OrgKind.Psm){
        			var changeOrg = false;
        			if (parentId != data.id) {
        				parentId = data.id;
        				changeOrg = true;
        			}
        			if(changeOrg){
        				$('#layout > div.l-layout-center > div.l-layout-header').html("<font style=\"color:Tomato;font-size:13px;\">[" + data.name + "]</font>任务列表");
        				$("#btnQuery").trigger("click");
        			}    			
    			}else{
    				parentId = "";
    			}
    		},
    		IsShowMenu : false
    	});
    }
    
    function getSearchContent(){
    	var result = $("#searchContent").val();
    	return searchContentDefalutValue == result ? "" : result;
    }
    
    function getStartDate() {
        return $("#editStartDate").val();
    }

    function getEndDate() {
        return $("#editEndDate").val();
    }
});

