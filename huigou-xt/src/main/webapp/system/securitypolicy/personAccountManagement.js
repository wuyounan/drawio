var gridManager = null, refreshFlag = false, fullId = "", statusList;
$(document).ready(function () {
    initializeUI();
    initializeTreeView();
    initializeGrid();
});

function initializeUI() {
    UICtrl.initDefaultLayout();
    statusList = $('#statusList').combox('getJSONData');
}

function initializeTreeView() {
    $('#maintree').commonTree({
		loadTreesAction:'org/queryOrgs.ajax',
		parentId :'orgRoot',
		getParam : function(e){
			if(e){
				return {showDisabledOrg:0, displayableOrgKinds : "ogn,dpt,pos,psm", showMasterPsm: 1, showPosition: 0};
			}
			return {showDisabledOrg:0};
		},
		changeNodeIcon:function(data){
			data[this.options.iconFieldName]= OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
		},
		IsShowMenu:false,
		onClick : function(data){
			fullId = data.fullId || "";
			onFolderOrgTreeNodeClick(data.fullName,data.fullId);
		}
	}); 
} 


function onFolderOrgTreeNodeClick(fullName,fullId) {
	if(fullName) {
		fullName = "[" + fullName + "]";	
	}
	$("#layout").layout("setCenterTitle", "<span class=\"tomato-color\">" + fullName + "</span>账号信息列表")
    var params = $("#queryMainForm").formToJSON();
    params.fullId = fullId;
    UICtrl.gridSearch(gridManager, params);
}

function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
    	enable:{ id: 'enable', text: '解锁', img: ' ', click: enableHandler },
    	disable:{ id: 'disable', text: '锁定', img: '', click: disableHandler }
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            { display: "登录名", name: "loginName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "是否锁定", name: "status", width: 120, minWidth: 60, type: "string", align: "left", 
            	render: function (item) {
	            	 if (item.status == 1){
	            		 return "<div class='status-disable' title='锁定'/>";
	            	 }else{
	            		 return "<div class='status-enable' title='未锁定'/>";
	            	 }
            	}
            },
            { display: "锁定时间", name: "lockedDate", width: 150, minWidth: 60, type: "string", align: "left" },
            { display: "最后登录时间", name: "lastLoginDate", width: 150, minWidth: 60, type: "string", align: "left"},
            { display: "最后修改密码时间", name: "lastModifiedPasswordDate", width: 150, minWidth: 60, type: "string", align: "left"}
			],
        dataAction: 'server',
        url: web_app.name + '/personAccountManagement/sliceQueryPersonAccountManagements.ajax',
        parms: { fullId:fullId },
        toolbar: toolbarOptions,
        width: '99.8%',
        height: '100%',
        heightDiff: -5,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

function enableHandler() {
	updateStatus('您要解锁账号吗?', 1);
}

function disableHandler() {
	updateStatus('您要锁定账号吗?',0);
}

function updateStatus(message, status) {
	DataUtil.updateById({ action: 'personAccountManagement/updatePersonAccountManagementsStatus.ajax',
      gridManager: gridManager, param: { status: status },
      message: message,
      onSuccess: function () {
          reloadGrid();
      }
  });
}

function reloadGrid() {
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}
