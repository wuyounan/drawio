var gridManager,orgQueryGridManager,inheritDataManageGridManager,dataResourceManageGridManager,parentId, refreshFlag,queryOrgFlag=false;
$(function() {
	initializateUI();
	loadOrgTreeView();
	initializeOrgQueryGrid();
	initializeGrid();
	initializeInheritDataManageGrid();
	initializeDataResourceManageGrid();
	bindEvents();
	
	
	function initializateUI() {
		UICtrl.layout("#layout",{leftWidth:3});
		$('body').addClass('dom-overflow');
		$('#tabPage').tab();
		UICtrl.autoWrapperHeight(function(pageSize){
			$('#orgTreeViewDiv').height(pageSize.h - 90);
		});
		$(['queryDataManageForm','queryInheritDataManageGridTable']).each(function(i,t){
			$('#name','#'+t).treebox({
				name:'dataManageTypeTreeView',
				searchName:'dataManageType',
				searchType:'sys',
				hasSearch :true,
				minWidth:250,
				back:{text:$('#name','#'+t)},
				onChange:function(node,data){
					$('#name','#'+t).val(data.name);
					$('#fullId','#'+t).val(data.fullId);
				}
			});
		});
	}

	function bindEvents() {
		$("#queryDataManageBtn").click(function() {
			var params = $(this.form).formToJSON();
			UICtrl.gridSearch(gridManager, params);
		});
		$("#resetDataManageBtn").click(function() {
			$(this.form).formClean();
		});
		$("#queryInheritDataManageBtn").click(function() {
			var params = $(this.form).formToJSON();
			UICtrl.gridSearch(inheritDataManageGridManager, params);
		});
		$("#resetInheritDataManageBtn").click(function() {
			$(this.form).formClean();
		});
		$("#queryDataResourceBtn").click(function() {
			var params = $(this.form).formToJSON();
			UICtrl.gridSearch(dataResourceManageGridManager, params);
		});
		$("#resetDataResourceBtn").click(function() {
			$(this.form).formClean();
		});
		//切换tab 刷新grid
		$('li.tabli').on('click',function(){
			if(!$(this).hasClass('active')){
				setTimeout(function(){reloadGrid();},0);
			}
		});
		
		//点击事件
    	$('#queryOrgGroup').on('click',function(e){
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
    				UICtrl.gridSearch(orgQueryGridManager, {
                    	paramValue: encodeURI(value)
                    });
    			}
    		}
    	});
    	//回车查询事件
    	$('#queryOrgGroup').find('input.text').on('keydown',function(event){
    		var value = $(this).val();
            if (value == '') {
            	switchSelectable({isQuery:false});
            } else {
                var k = event.charCode || event.keyCode || event.which;
                if (k == 13) {
                	switchSelectable({isQuery:true});
                	UICtrl.gridSearch(orgQueryGridManager, {
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
    		$('#orgTreeViewDiv').addClass('ui-hide');
    		$('#orgGridViewDiv').removeClass('ui-hide');
    		queryOrgFlag=true;
    	}else{
    		$('#orgTreeViewDiv').removeClass('ui-hide');
    		$('#orgGridViewDiv').addClass('ui-hide');
    		queryOrgFlag=false;
    	}
    }
	
	function initializeOrgQueryGrid(){
    	var _param=$.extend({},{
    		showAllChildrenOrg: 1,
    		showProjectOrg:  0,
    		showVirtualOrg:  0,
    		displayableOrgKinds: 'ogn,dpt,pos,psm'
    	});
    	orgQueryGridManager = UICtrl.grid($('#orgGridView'), {
            columns: [
                { display: "common.field.name", name: "name", width: 80, minWidth: 60, type: "string", align: "left", frozen: true},
                { display: "common.field.fullname", name: "fullName", width: 400, minWidth: 60, type: "string", align: "left",}
            ],
            url: web_app.name + '/org/slicedQueryOrgs.ajax',
            manageType: 'functionDelegation',
            parms: _param,
            height: '100%',
            heightDiff: -13,
            usePager: false,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onLoadData: function(){
    			return queryOrgFlag;
    		},
    		onSelectRow:function(data){
    			treeNodeOnclick(data);
    		}
        });
	}
	
	function initializeGrid() {
		var toolbarOptions = UICtrl.getDefaultToolbarOptions({
			addAuthorization: { id: "addAuthorization", text: "分配", click: function(){}, img: "fa-tags" },
	        deleteAuthorization: { id: "deleteAuthorization", text: "删除", click: deleteAuthorization, img: "fa-trash-o"},
	        romoveCache:{ id: "romoveCache", text: "清除权限缓存", img: "fa-files-o" , click: function(){
	        	Public.ajax(web_app.name + '/management/removePermissionCache.ajax');
	        }}
	    });
		gridManager = UICtrl.grid("#maingrid", {
			columns: [
			    {display: "名称", name: "name", width: 300, minWidth: 60, type: "string", align: "left",
			    	render: function (item) { 
						var html=['<a href="javascript:void(null);" class="GridStyle" '];
						html.push('id="',item.dataManagedetalId,'" ');
						html.push('name="',item.name,'" ');
						html.push('onclick="showDataManageDetailPermission(this)" ');
						html.push('>');
						html.push(item.name);
						html.push('</a>');
						return html.join('');
					}
			    },
				{display: "权限编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
				{display: "权限类别", name: "manageName", width: 200, minWidth: 60, type: "string", align: "left"}
			],
			dataAction: "server",
			url: web_app.name + "/dataManagement/queryOpdatamanagement.ajax",
			usePager: false,
			toolbar: toolbarOptions,
			width: "99%",
			height: "100%",
			heightDiff: -8,
			checkbox: true,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return !(Public.isBlank(parentId));
			}
		});
		UICtrl.setSearchAreaToggle(gridManager, $("#navTitle01"));
		$('#toolbar_menuaddAuthorization').comboDialog({type:'sys',name:'dataManagDetail',
			width:685,checkbox: true,
			treeOptions:function(doc){//资源树
				var _self=this;
				return {
					loadTreesAction: 'dataManageType/queryDatamanagetypekind.ajax',
					parentId :'0',
					IsShowMenu:false,
					changeNodeIcon:function(){},
					onClick : function(data){
						_self.folderId=data.fullId;
						UICtrl.gridSearch(_self.gridManager, {folderId:data.fullId});
					}
				};
			},
			onShow:function(){
				if (Public.isBlank(parentId)){
					Public.errorTip("请选择组织节点。");
					return false;
				}
				return true;
			},
			onChoose:function(dialog){
				var rows = this.getSelectedRows();
		    	if(!rows.length){
		    		Public.errorTip('请选择权限资源。');
		    		return false;
		    	}
		    	var datas=$.map(rows,function(o){
		    		return {dataManageId:o.dataManageId,dataManagedetalId:o.id};
		    	});
		    	Public.ajax(web_app.name+'/dataManagement/insertOpdatamanagement.ajax',
		    		{managerId:parentId, datas: $.toJSON(datas)},
		    		function(){
		    			gridManager.loadData();
		    			dialog.close();
		    		}
		    	);
		    	return false;
			}
		});
	}
	
	function deleteAuthorization() {
		DataUtil.del({
		      action: 'dataManagement/deleteOpdatamanagement.ajax',
		      gridManager: gridManager, idFieldName: 'id',
		      onSuccess: function () {
		    	  gridManager.loadData();
		      }
		});
	}

	function initializeInheritDataManageGrid() {
		var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	        exportExcelHandler: function(){
	        	UICtrl.gridExport(inheritDataManageGridManager);
	        }
	    });
		inheritDataManageGridManager = UICtrl.grid("#inheritDataManageGrid", {
				columns: [
				    {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left",
				    	render: function (item) { 
							var html=['<a href="javascript:void(null);" class="GridStyle" '];
							html.push('id="',item.dataManagedetalId,'" ');
							html.push('name="',item.name,'" ');
							html.push('onclick="showDataManageDetailPermission(this)" ');
							html.push('>');
							html.push(item.name);
							html.push('</a>');
							return html.join('');
						}
				    },
					{display: "权限编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
					{display: "权限类别", name: "manageName", width: 200, minWidth: 60, type: "string", align: "left"},
					{display: "授权组织", name: "orgName", width: "150", minWidth: 60, type: "string", align: "left" },
 					{display: "授权组织路径", name: "orgFullName", width: "300", minWidth: 60, type: "string", align: "left" }
				],
				dataAction: "server",
				url: web_app.name  + "/dataManagement/slicedQueryDataManagementByOrgFullId.ajax",
				pageSize: 20,
				width: '99%',
				height: "100%",
				sortName: 'fullId',
	            sortOrder: 'asc',
				heightDiff: -5,
				toolbar: toolbarOptions,
				checkbox: true,
				fixedCellHeight: true,
				selectRowButtonOnly: true,
				onLoadData:function(){
					return !(Public.isBlank(parentId));
				}
	    });
	 	UICtrl.setSearchAreaToggle(inheritDataManageGridManager, $("#navTitle02"));
	}
	
	function initializeDataResourceManageGrid() {
		var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	        exportExcelHandler: function(){
	        	UICtrl.gridExport(dataResourceManageGridManager);
	        }
	    });
		dataResourceManageGridManager = UICtrl.grid("#dataResourceManageGrid", {
				columns: [
				    {display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left"},
					{display: "权限编码", name: "code", width: 200, minWidth: 60, type: "string", align: "left"},
					{display: "权限类别", name: "manageName", width: 200, minWidth: 60, type: "string", align: "left"},
					{display: "资源KEY", name: "resourceKey", width: 200, minWidth: 60, type: "string", align: "left" },
 					{display: "资源值", name: "resourceValue", width:200, minWidth: 60, type: "string", align: "left" }
				],
				dataAction: "server",
				url: web_app.name  + "/dataManagement/slicedQueryDataManageResourceByOrgFullId.ajax",
				pageSize: 20,
				width: '99%',
				height: "100%",
				sortName: 'sequence,detail_id,data_kind_id',
	            sortOrder: 'asc',
				heightDiff: -5,
				toolbar: toolbarOptions,
				checkbox: true,
				fixedCellHeight: true,
				selectRowButtonOnly: true,
				onLoadData:function(){
					return !(Public.isBlank(parentId));
				}
	    });
	 	UICtrl.setSearchAreaToggle(dataResourceManageGridManager, $("#navTitle03"));
	}
});

function loadOrgTreeView(){
	var url = "/org/queryOrgs.ajax";
	 $('#maintree').commonTree({
        loadTreesAction: url,
        parentId: 'orgRoot',
        getParam: function (e) {
            if (e) {
                return { showDisabledOrg: 0, showPosition: 1, displayableOrgKinds: "ogn,dpt,pos,psm,fld,prj,grp" };
            }
            return { showDisabledOrg: 0, showPosition: 1 };
        },
        manageType: 'functionDelegation',
        isLeaf: function (data) {
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
            return data.hasChildren == 0;
        },
        onClick: treeNodeOnclick,
        IsShowMenu: false
    });
}

function treeNodeOnclick(data){
	if (!data) {
		return;
	}
	if(data.id=='orgRoot') {
		parentId=null;
		return;
	}
	if (parentId != data.id){
		parentId = data.id;
		gridManager.options.parms.managerId = data.id;
		inheritDataManageGridManager.options.parms.orgFullId = data.fullId;
		dataResourceManageGridManager.options.parms.orgFullId = data.fullId;
		reloadGrid();
		$('#layout').layout('setLeftTitle',['<span class="tomato-color">[',data.name,']</span>','组织机构树'].join(''));
	}
}

function reloadGrid() {
	if ($("#menuDataAuthorize").hasClass("active")){
		gridManager.loadData();
	}else if ($("#menuDataQuery").hasClass("active")){
		inheritDataManageGridManager.loadData();
	}else if ($("#menuDataResourceQuery").hasClass("active")){
		dataResourceManageGridManager.loadData();
	}
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