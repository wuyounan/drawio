var gridManager = null;

$(document).ready(function() {
	initializeGrid();
	initializeUI();
});

function initializeUI(){
	UICtrl.initDefaultLayout();
	$('#maintree').commonTree({
        loadTreesAction: 'tmAuthorize/queryDelegationOrgs.ajax',
        parentId: 'orgRoot',
        getParam: function (e) {
            if (e) {
                return {showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt,pos,psm"};
            }
            return {showDisabledOrg: 0};
        },
        changeNodeIcon: function (data) {
            data[this.options.iconFieldName] = OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
        },
        IsShowMenu: false,
        onClick: function (data) {
            onFolderOrgTreeNodeClick(data);
        }
    });
}

function onFolderOrgTreeNodeClick(data) {
	var html=[],fullId='',fullName='';
	if(!data){
		html.push('登录日志');
	}else{
		fullId=data.fullId,fullName=data.fullName;
		html.push('<span class="tomato-color">[',fullName,']</span>登录日志');
	}
	$("#layout").layout("setCenterTitle", html.join(''));
	if (gridManager&&fullId!='') {
		UICtrl.gridSearch(gridManager,{fullId:fullId});
	}else{
		gridManager.options.parms['fullId']='';
	}
}

function initializeGrid() {
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
            { display: "登录名", name: "loginName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "操作者", name: "personMemberName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "操作者路径", name: "fullName", width: 400, minWidth: 60, type: "string", align: "left" },
		    { display: "IP地址", name: "clientIp", width: 80, minWidth: 60, type: "string", align: "left" },		   
		    { display: "登录时间", name: "loginDate", width: 180, minWidth: 60, type: "string", align: "left" },
		    { display: "错误信息", name: "errorMessage", width: 180, minWidth: 60, type: "string", align: "left" },
		    { display: "退出时间", name: "logoutDate", width: 180, minWidth: 60, type: "string", align: "left" },
		    { display: "退出方式", name: "logoutKindId", width: 180, minWidth: 60, type: "string", align: "left", 
		    	render:function (item){
		    		if (item.logoutKindId == "NORMAL"){
		    			return "正常退出";
		    		}else if (item.logoutKindId == "EXPIRATION"){
		    			return "会话过期";
		    		}else if (item.logoutKindId == "FORCE_QUIT"){
		    			return "强制退出";
		    		}
		    	}}		    
		],
		dataAction: 'server',
		url: web_app.name+'/log/sliceQueryHistoricSessions.ajax',
		parms:{ fullId:-1 },
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -10,
		sortName:'loginDate',
		sortOrder:'desc',
		fixedCellHeight: true,
		selectRowButtonOnly: true
	});
	UICtrl.setSearchAreaToggle(gridManager);
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