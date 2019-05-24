var gridManager = null,manageDetailResourceGridGridManager =null,lastSelectedId=0,refreshFlag=false,queryOrgFlag=false;
$(document).ready(function () {
    initializeUI();
    initializeGrid();
    bindEvents();
    initializeTypeQueryGrid();
});

//初始化Tree
function initializeUI() {
	UICtrl.layout("#layout",{leftWidth:3});
	$('body').addClass('dom-overflow');
    $('#maintree').commonTree({
        loadTreesAction: 'dataManageType/queryDatamanagetypekind.ajax',
        parentId: "0",
        IsShowMenu: false,
        changeNodeIcon:function(data){
        	var nodeKindId=data.nodeKindId;
        	if(nodeKindId==2){
        		data[this.options.iconFieldName] = web_app.name + "/images/icons/application.png";
        	}
        },
        onClick: function (data) {
            if (data && lastSelectedId != data.id) {
                onFolderTreeNodeClick(data.id, data.name, data.fullId);
            }
        }
    });
    UICtrl.autoWrapperHeight(function(pageSize){
		$('#typeTreeViewDiv').height(pageSize.h - 90);
	});
}

//点击树节点时加载表格
function onFolderTreeNodeClick(id, name, fullId) {
    var html = "<font class='tomato-color'>[" + name + "]</font>数据管理权限列表";
    $('#layout').layout('setCenterTitle', html);
    lastSelectedId = id;
    var params = $("#queryMainForm").formToJSON();
    params['fullId'] = fullId;
    UICtrl.gridSearch(gridManager, params);
}

function bindEvents(){
	//点击事件
	$('#queryTypeGroup').on('click',function(e){
		var $clicked=$(e.target || e.srcElement);
		if($clicked.is('i.fa')) $clicked=$clicked.parent();
		if($clicked.hasClass('ui-grid-query-clear')){
			$(this).find('input.text').val('');
			switchSelectable({isQuery:false});
		}
		if($clicked.hasClass('ui-grid-query-button')){
			var value=$(this).find('input.text').val();
			if (value != '') {
				switchSelectable({isQuery:true});
				UICtrl.gridSearch(manageTypeGridManager, {
                	paramValue: encodeURI(value)
                });
			}
		}
	});
	//回车查询事件
	$('#queryTypeGroup').find('input.text').on('keydown',function(event){
		var value = $(this).val();
        if (value == '') {
        	switchSelectable({isQuery:false});
        } else {
            var k = event.charCode || event.keyCode || event.which;
            if (k == 13) {
            	switchSelectable({isQuery:true});
            	UICtrl.gridSearch(manageTypeGridManager, {
                	paramValue: encodeURI(value)
                });
                event.preventDefault();
                event.stopPropagation();
                return false;
            }
        }
	});
}

function switchSelectable(op){
	if(op.isQuery){
		$('#typeTreeViewDiv').addClass('ui-hide');
		$('#typeGridViewDiv').removeClass('ui-hide');
		queryOrgFlag=true;
	}else{
		$('#typeTreeViewDiv').removeClass('ui-hide');
		$('#typeGridViewDiv').addClass('ui-hide');
		queryOrgFlag=false;
	}
}

//初始化管理权限类别查询表格
function initializeTypeQueryGrid() {
	manageTypeGridManager = UICtrl.grid('#typeGridView', {
      columns: [
          {display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left"},
          {display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left"}
      ],
      dataAction: 'server',
      url: web_app.name + "/dataManageType/slicedQueryOpdatamanagetype.ajax",
      parms:{nodeKindId:2},
      pageSize: 20,
      width: '100%',
      height: '100%',
      heightDiff: -13,
      usePager: false,
      sortName:'sequence',
	  sortOrder:'asc',
	  onLoadData: function(){
			return queryOrgFlag;
      },
      onSelectRow:function(data){
    	  onFolderTreeNodeClick(data.id, data.name, data.fullId);
      }
  });
}

//初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: addHandler,
        updateHandler: function () {
            updateHandler();
        },
        deleteHandler: deleteHandler,
        romoveCache:{ id: "romoveCache", text: "清除权限缓存", img: "fa-files-o" , click: function(){
        	Public.ajax(web_app.name + '/management/removePermissionCache.ajax');
        }}
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {display: "权限编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "权限名称", name: "manageName", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
            {display: "备注", name: "remark", width:300, minWidth: 60, type: "string", align: "left"},
            {display: "授权情况", name: "view", width: 100, minWidth: 60, type: "string", align: "left",
				render: function (item) { 
					var html=['<a href="javascript:void(null);" class="GridStyle" '];
					html.push('id="',item.id,'" ');
					html.push('name="',item.name,'" ');
					html.push('onclick="showDataManageDetailPermission(this)" ');
					html.push('>');
					html.push('授权情况');
					html.push('</a>');
					return html.join('');
				}
			},
        ],
        dataAction: 'server',
        url: web_app.name + "/dataManagement/slicedQueryOpdatamanagedetail.ajax",
        pageSize: 20,
        width: '100%',
        height: '100%',
        heightDiff: -8,
        sortName:'sequence',
		sortOrder:'asc',
        toolbar: toolbarOptions,
        checkbox: true,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        onDblClickRow: function (data, rowindex, rowobj) {
            updateHandler(data.id);
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

function addHandler() {
	var node = null;
	if($('#typeTreeViewDiv').hasClass('ui-hide')){
		node = manageTypeGridManager.getSelectedRow();
	}else{
		node = $('#maintree').commonTree('getSelected');
	}
    if (!node) {
        Public.tip('请选择左侧数据管理权限类型!');
        return;
    }
    if (node.nodeKindId != 2) {
        Public.tip('只能在数据权限类型下添加');
        return;
    }
    UICtrl.showAjaxDialog({
        title: "添加"+node.name,
        width: 700,
        url: web_app.name + '/dataManagement/showInsertOpdatamanagedetail.load',
        param: {dataManageId: node.id},
        init:initDetailDialog,
        ok: insert,
        close: dialogClose
    });
}

//编辑修改
function updateHandler(id) {
	if (!id) {
		var id = DataUtil.getUpdateRowId(gridManager);
	    if (!id) {
	    	return;
	    }
	}
    UICtrl.showAjaxDialog({
        title: "修改",
        width: 700,
        url: web_app.name + '/dataManagement/showLoadOpdatamanagedetail.load',
        param: {id: id},
        init:initDetailDialog,
        ok: update,
        close: dialogClose
    });
}

//关闭对话框
function dialogClose(){
	if(refreshFlag){
		reloadGrid();
		refreshFlag=false;
	}
}

//新增保存
function insert() {
	var id=$('#detailPageId').val();
	if (Public.isNotBlank(id)) {
		update();
		return;
	}
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/dataManagement/insertOpdatamanagedetail.ajax',
        success: function (data) {
        	if (Public.isBlank(id)) {
        		$('#detailPageId').val(data);
        		manageDetailResourceGridGridManager.options.parms['dataManagedetalId']=data;
        	}
            refreshFlag = true;
        }
    });
}

//编辑保存
function update() {
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/dataManagement/updateOpdatamanagedetail.ajax',
        success: function () {
            refreshFlag = true;
        }
    });
}

//删除按钮
function deleteHandler(id) {
  DataUtil.del({
      action: 'dataManagement/deleteOpdatamanagedetail.ajax',
      gridManager: gridManager,
      onSuccess: function () {
          reloadGrid();
      }
  });
}

function showDataManageDetailPermission(obj){
	var id=$(obj).attr('id'),name=$(obj).attr('name');
	UICtrl.showFrameDialog({
		url: web_app.name + "/dataManagement/forwardDataManageDetailPermission.do",
		param: {dataManagedetalId: id},
		title: name+'授权情况',
		width: getDefaultDialogWidth(),
		height: getDefaultDialogHeight(),
		cancelVal: '关闭',
		ok: false,
		cancel:true
	});
}

function initDetailDialog(div){
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler: function(){
			addDetailResource(div);
		},
		deleteHandler: function(){
			DataUtil.del({action:'dataManagement/deleteOpdatamanagedetailresource.ajax',
				gridManager:manageDetailResourceGridGridManager,
				onSuccess:function(){
					manageDetailResourceGridGridManager.loadData();
				}
			});
		}
	});
	manageDetailResourceGridGridManager=UICtrl.grid('#datamanagedetailresourceGrid', {
		columns: [
		     {display: "资源名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left"},
		     {display: "资源KEY", name: "resourceKey", width: 120, minWidth: 60, type: "string", align: "left"},
		     {display: "资源值", name: "resourceValue", width: 200, minWidth: 60, type: "string", align: "left"},
		     {display: "资源路径", name: "fullName", width: 200, minWidth: 60, type: "string", align: "left"},
		     {display: "资源类型", name: "dataKindTextView", width: 80, minWidth: 60, type: "string", align: "left"},
		     {display: "资源编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left"}
		],
		dataAction: 'server',
		url: web_app.name+'/dataManagement/queryOpdatamanagedetailresource.ajax',
		parms:{dataManagedetalId: $('#detailPageId').val()},
		width: '100%', 
		height: '260',
		heightDiff: -8,
		toolbar: toolbarOptions,
		usePager: false,
		checkbox: true,
		fixedCellHeight: true,
		selectRowButtonOnly: true,
		onLoadData: function(){
			return $('#detailPageId').val()!='';
		}
	});
}

function addDetailResource(div){
	var param = $('#submitForm').formToJSON({check:false});
	if (Public.isBlank(param['id'])) {
		insert();
		return false;
	}
	var dataKindId=param['resourcekind'];
	if (Public.isBlank(dataKindId)) {
		Public.tip('请选择需要添加的资源类型!');
		return false;
	}
	UICtrl.showAjaxDialog({
        title: "添加",
        width: 400,
        id:'datamanagedetailresource',
        parent:window['ajaxDialog'],
        url: web_app.name + '/dataManagement/showInsertOpdatamanagedetailresource.load',
        param: {dataKindId:dataKindId},
        init:function(div){
        	intDetailResourceChoose(div);
        },
        ok: function(){
        	var dataManagedetalId=$('#detailPageId').val(),_self=this;
        	var param = $('#detailResourceSubmitForm').formToJSON();
        	if(!param){
        		return false;
        	}
        	var dataKind=param['dataKind'];
        	if(dataKind=='org'){//组织机构选择
        		var orgDataKind=param['orgDataKind'];
        		if(orgDataKind=='manageType'){
        			param['resourceKey']=param['orgManageType'];
        			param['resourceValue']=param['orgManageTypeName'];
        		}else if(orgDataKind=='orgfun'){
        			param['resourceKey']=param['dictOrgDataKind'];
        			param['resourceValue']=param['dictOrgDataKind_text'];
        		}
        	}
        	param['dataManagedetalId']=dataManagedetalId;
        	Public.ajax(web_app.name + '/dataManagement/insertOpdatamanagedetailresource.ajax', param, function(){
        		manageDetailResourceGridGridManager.loadData();
            	_self.close();
        	});
        }
    });
}

//ORG("org", "组织机构"), DICTIONARY("dictionary", "数据字典"), ENUMKIND("enum", "枚举"), DEFINE("define", "自定义选择"), INPUT("input", "手工输入");
function intDetailResourceChoose(div){
	var dataKind=$('#dataKind',div).val();
	var dataSource=$('#dataSource',div).val();
	dataSource=$.trim(dataSource);
	if(dataKind!='input'&&dataKind!='org'){
		if (Public.isBlank(dataSource)) {
			Public.tip('资源数据源为不能为空，请检查资源维度定义!');
			return;
		}
	}
	if(dataKind=='dictionary'){//数据字典
		$('#resourceValue',div).remotebox({
			name:dataSource,
			back:{
				value:$('#resourceKey',div),
				text:$('#resourceValue',div)
			}
		});
	}else if(dataKind=='enum'){//枚举 
		$('#resourceValue',div).remotebox({
			kind:'enum',
			name:dataSource,
			back:{
				value:$('#resourceKey',div),
				text:$('#resourceValue',div)
			}
		});
	}else if(dataKind=='org'){//组织机构
		//数据字典sysOrgDataKind中定义
		$('#orgDataKind',div).combox({onChange:function(data){
			var key=data.value;
			if(key=='choose'){
		    	$('#orgChooseDiv').removeClass('ui-hide');
		    	$('#orgDictionaryDiv').addClass('ui-hide');
		    	$('#orgManageTypeDiv').addClass('ui-hide');
		    	UICtrl.setRequiredFlag($('#orgChooseDiv'),true);
		    	UICtrl.setRequiredFlag($('#orgDictionaryDiv'),false);
		    	UICtrl.setRequiredFlag($('#orgManageTypeDiv'),false);
			}else if(key=='manageType'){
				$('#orgChooseDiv').addClass('ui-hide');
		    	$('#orgDictionaryDiv').addClass('ui-hide');
		    	$('#orgManageTypeDiv').removeClass('ui-hide');
		    	UICtrl.setRequiredFlag($('#orgChooseDiv'),false);
		    	UICtrl.setRequiredFlag($('#orgDictionaryDiv'),false);
		    	UICtrl.setRequiredFlag($('#orgManageTypeDiv'),true);
			}else if(key=='orgfun'){
				$('#orgChooseDiv').addClass('ui-hide');
		    	$('#orgDictionaryDiv').removeClass('ui-hide');
		    	$('#orgManageTypeDiv').addClass('ui-hide');
		    	UICtrl.setRequiredFlag($('#orgChooseDiv'),false);
		    	UICtrl.setRequiredFlag($('#orgDictionaryDiv'),true);
		    	UICtrl.setRequiredFlag($('#orgManageTypeDiv'),false);
			}
			$('#resourceKey',div).val('');
	    	$('#resourceValue',div).val('');
			$('#fullId',div).val('');
	    	$('#fullName',div).val('');
		}});
		$('#resourceValue',div).orgTree({
			 filter: "ogn,dpt,psm", excludePos: 1, param: {orgKindId: "ogn,dpt,psm"},
		     back: {text: $('#resourceValue',div)},
		     onChange:function(value,data){
		    	 $('#resourceKey',div).val(data.id);
		    	 $('#fullId',div).val(data.fullId);
		    	 $('#fullName',div).val(data.fullName);
		     }
		});
	}else if(dataKind=='define'){//自定义选择
		var dataOptions = Public.getJSONDataSource(dataSource);
		dataOptions['onChange']=function(value,data){
			//读取返回信息后单独处理
			$.each(this.options.back,function(p,o){
				$('#'+o,div).val(data[p]||'');
			});
		};
		var kind=dataOptions.kind||'searchbox';
		if(kind=='tree'){
			//例： kind:'tree',type: 'sys',name: 'procTreeView',back: { name: 'resourceValue',code:'resourceKey',fullId:'fullId',fullName:'fullName'}
			$('#resourceValue',div).treebox(dataOptions);
		}else{
			//例： type: 'sys',name: 'saOrgFun',back: { name: 'resourceValue',code:'resourceKey'}
			$('#resourceValue',div).searchbox(dataOptions);
		}
	}
}