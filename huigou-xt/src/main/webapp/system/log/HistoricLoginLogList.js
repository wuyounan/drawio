var gridManager = null;

$(document).ready(function() {
	UICtrl.autoSetWrapperDivHeight();
	initializeGrid();
	initializeUI();
});

function initializeUI(){
	UICtrl.layout("#layout", {leftWidth : 200,heightDiff : -5});
	$('#maintree').commonTree({
		loadTreesAction:'org/queryOrgs.ajax',
		parentId :'orgRoot',
	 	manageType: "queryLog", 
		getParam : function(e){
			if(e){
				return {showDisabledOrg:0,displayableOrgKinds : "ogn,dpt"};
			}
			return {showDisabledOrg:0};
		},
		changeNodeIcon:function(data){
			data[this.options.iconFieldName]= OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
		},
		IsShowMenu:false,
		onClick : onFolderTreeNodeClick
	});
}

function onFolderTreeNodeClick(data) {
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
            { display: "操作者", name: "personMemberName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "操作者路径", name: "fullName", width: 320, minWidth: 60, type: "string", align: "left" },
		    { display: "IP地址", name: "clientIp", width: 80, minWidth: 60, type: "string", align: "left" },		   
		    { display: "登录时间", name: "loginDate", width: 180, minWidth: 60, type: "string", align: "left" }	   
		],
		dataAction: 'server',
		url: web_app.name+'/appLog/sliceQueryHistoricSessions.ajax',
		pageSize: 20,
		width: '100%',
		height: '100%',
		heightDiff: -10,
		sortName: 'loginDate',
		sortOrder: 'desc',
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