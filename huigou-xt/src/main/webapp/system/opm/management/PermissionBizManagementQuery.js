var gridManager = null;
$(document).ready(function() {
	initUI();
	initializeManagementGrid();
});

function initUI(){
	$('#managementName').add($('#managementName').siblings('span')).on('click',function(){
		OpmUtil.showSelectBizManagementTypeDialog({confirmHandler: function(){
			_self = this;
		    var data = this.iframe.contentWindow.getBizManagementTypeData();
		    if (data == null) return;
		    $("#fullId").val(data.fullId);
		    $("#managementName").val(data.name);

		    _self.close();
		}});
	});
	
	$("#manageOrgName").orgTree({
        filter: "ogn,dpt,psm", excludePos: 1, param: {orgKindId: "ogn,dpt,psm"},
        back: {text: "#manageOrgName"},
        manageType: 'taskQuery,admin',
        onChange:function(value,data){
        	$('#manageOrgId').val(data.id);
        }
    });
	
	$("#subordinationOrgName").orgTree({
        filter: "ogn,dpt,psm", excludePos: 1, param: {orgKindId: "ogn,dpt,psm"},
        back: {text: "#subordinationOrgName"},
        manageType: 'taskQuery,admin',
        onChange:function(value,data){
        	$('#subordinationOrgId').val(data.id);
        }
    });
}



function initializeManagementGrid() {
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		deleteOrg:{ id: "deleteOrg", text: "删除", img:  "fa-trash-o" , click: function(){
			DataUtil.del({ action: '/management/deleteBizManagement.ajax', gridManager: gridManager, onSuccess: function(){
				reloadGrid();
			}});
		}},
		romoveCache:{ id: "romoveCache", text: "清除权限缓存", img: "fa-files-o", click: function(){
			Public.ajax(web_app.name + '/management/removePermissionCache.ajax');
	    }},
		exportExcelHandler: function(){
        	UICtrl.gridExport(gridManager);
        }
	});  
	var manageType=$('#delegationManageType').val();
	if (Public.isBlank(manageType)) {
		manageType=null;
	}
	gridManager=UICtrl.grid($('#maingrid'), {
		columns: [
		{ display: "业务权限编码", name: "code", width: 180, minWidth: 60, type: "string", align: "left" },
		{ display: "业务权限名称", name: "name", width: 180, minWidth: 60, type: "string", align: "left" },		  
		{ display: "管理者", name: "managerName", width: 120, minWidth: 60, type: "string", align: "left" },		   
		{ display: "管理者路径", name: "managerFullName", width: 250, minWidth: 60, type: "string", align: "left" },		   
		{ display: "管理者状态", name: "managerStatus", width: 80, minWidth: 60, type: "string", align: "left",
			render: function (item){
		   	   	return OpmUtil.getOrgStatusDisplay(item.managerStatus) ; 
		   	} 
		},		   
		{ display: "被管理者", name: "subName", width: 150, minWidth: 60, type: "string", align: "left" },
		{ display: "被管理者路径", name: "subFullName", width: 220, minWidth: 60, type: "string", align: "left" },
		{ display: "创建人", name: "createdByName", width: 100, minWidth: 60, type: "string", align: "left" },
		{ display: "创建时间", name: "createdDate", width: 120, minWidth: 60, type: "date", align: "left" }
		],
		dataAction: 'server',
		url: web_app.name+'/management/slicedPermissionBizManagementQuery.ajax',
		manageType:manageType,
		pageSize: 20,
		width: '100%',
		sortName:'code,fullSequence',
		sortOrder:'asc',
		height: '100%',
		heightDiff: -5,
		toolbar: toolbarOptions,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true
	});
}


function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function reloadGrid() {
	gridManager.loadData();
} 

function resetForm(obj) {
	$(obj).formClean();
}