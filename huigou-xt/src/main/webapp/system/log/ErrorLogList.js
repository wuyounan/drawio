var gridManager = null, refreshFlag = false;
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
		html.push('错误日志');
	}else{
		fullId=data.fullId,fullName=data.fullName;
		html.push('<span class="tomato-color">[',fullName,']</span>错误日志');
	}
	$('.l-layout-center .l-layout-header').html(html.join(''));
	if (gridManager&&fullId!='') {
		UICtrl.gridSearch(gridManager,{fullId:fullId});
	}else{
		gridManager.options.parms['fullId']='';
	}
}
//初始化表格
function initializeGrid() {
	gridManager = UICtrl.grid('#maingrid', {
		columns: [
            { display: "系统编码", name: "appCode", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "系统名称", name: "appName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "操作者", name: "personMemberName", width: 120, minWidth: 60, type: "string", align: "left" },
            { display: "操作者路径", name: "fullName", width: 320, minWidth: 60, type: "string", align: "left" },
            { display: "IP地址", name: "ip", width: 80, minWidth: 60, type: "string", align: "left" },		   
            { display: "开始时间", name: "beginDate", width: 150, minWidth: 60, type: "string", align: "left" },
            { display: "结束时间", name: "endDate", width: 150, minWidth: 60, type: "string", align: "left" },
            { display: "操作描述", name: "description", width: 200, minWidth: 60, type: "string", align: "left" }
		],
		dataAction : 'server',
		url: web_app.name+'/appLog/slicedQueryOperationLogs.ajax',
		parms:{status: 0},
		pageSize : 20,
		width: '100%',
		height: '100%',
		heightDiff : -10,
		sortName: 'createdDate',
		sortOrder: 'desc',
		fixedCellHeight : true,
		selectRowButtonOnly : true,
		onDblClickRow : function(data, rowindex, rowobj) {
			viewHandler(data.id);
		}
	});
	UICtrl.setSearchAreaToggle(gridManager);
}

// 查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
	gridManager.loadData();
} 

//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

function viewHandler(id){
	UICtrl.showAjaxDialog({
		url: web_app.name + '/appLog/showOperationLog.load',
		title: "查看日志",
		width:600, 
		param:{id:id}, 
		ok:false});
}