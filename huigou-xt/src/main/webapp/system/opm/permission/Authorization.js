var gridManager,orgQueryGridManager,permissionGridManager,roleQueryGridManager, parentId, refreshFlag,queryOrgFlag=false;
$(function() {
	initializateUI();
	loadOrgTreeView();
	initializeOrgQueryGrid();
	initializeGrid();
	initializeRoleQueryGrid();
	initializePermissionGrid();
	bindEvents();
	
	
	function initializateUI() {
		UICtrl.layout("#layout",{leftWidth:3});
		$('body').addClass('dom-overflow');
		$('#tabPage').tab();
		UICtrl.autoWrapperHeight(function(pageSize){
			$('#orgTreeViewDiv').height(pageSize.h - 90);
		});
	}

	function bindEvents() {
		$("#queryRoleFormBtn").click(function() {
			var params = $(this.form).formToJSON();
			UICtrl.gridSearch(gridManager, params);
		});
		$("#queryPermissionMainBtn").click(function() {
			var params = $(this.form).formToJSON();
			UICtrl.gridSearch(permissionGridManager, params);
		});
		$("#resetRoleFormBtn").click(function() {
			$(this.form).formClean();
		});
		$("#resetPermissionMainBtn").click(function() {
			$(this.form).formClean();
		});
		//切换tab 刷新grid
		$('li.tabli').on('click',function(){
			if(!$(this).hasClass('active')){
				setTimeout(function(){reloadGrid();},0);
			}
		});
		$("#queryRoleForm").bind('keydown',function(e){
			if (e.ctrlKey) {
				return;
			}
			var k =e.charCode||e.keyCode||e.which;
			if(k==13){// 回车
				var params = $(this).formToJSON();
				UICtrl.gridSearch(gridManager, params);
				return false;
			}
		});
		$("#queryPermissionTable").bind('keydown',function(e){
			if (e.ctrlKey) {
				return;
			}
			var k =e.charCode||e.keyCode||e.which;
			if(k==13){// 回车
				var params = $(this).formToJSON();
				UICtrl.gridSearch(permissionGridManager, params);
				return false;
			}
		});
		$("#queryRoleMainBtn").click(function() {
			var params = $(this.form).formToJSON();
			UICtrl.gridSearch(roleQueryGridManager, params);
		});
		$("#resetRoleMainBtn").click(function() {
			$(this.form).formClean();
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
                { display: "common.field.fullname", name: "fullName", width: 400, minWidth: 60, type: "string", align: "left"}
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
		var toolbarOptions = {
			items: [ { id: "addAuthorization", text: "分配", click: showInsertAuthorizationDialog, img: "fa-tags" },
			         { id: "deleteAuthorization", text: "删除", click: deleteAuthorization, img: "fa-trash-o"}
			       ]
		};

		gridManager = UICtrl.grid("#maingrid", {
			columns: [
			        { display: "类型", name: "roleKindId", width: "60", minWidth: 60, type: "string", align: "left",
			        	render:function(item){
			        		if (item.roleKindId == "fun")
			        			return "<div style='text-align:center;height:25px;'> <img style='margin-top:4px;' src=\"" + OpmUtil.OrgImagePath + "funRole.gif\" /></div>";
			        		else
			        			return "<div style='text-align:center;height:25px;'><img style='margin-top:4px;' src=\"" + OpmUtil.OrgImagePath + "dataRole.gif\"/></div>";
			        	}
			        },
			        { display: "编码", name: "roleCode", width: 200, minWidth: 60, type: "string", align: "left" },
			        { display: "名称", name: "roleName", width: 280, minWidth: 60, type: "string", align: "left" }//,
					//{ display: "创建人", name: "createdByName", width: 100, minWidth: 60, type: "string", align: "left" },
					//{ display: "创建日期", name: "createdDate", width: 100, minWidth: 60, type: "date", align: "left" }
			],
			dataAction: "server",
			url: web_app.name + "/access/queryAuthorizations.ajax",
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
	}

	function initializePermissionGrid() {
		var toolbarOptions = UICtrl.getDefaultToolbarOptions({
	        exportExcelHandler: function(){
	        	UICtrl.gridExport(permissionGridManager);
	        }
	    });
	    permissionGridManager = UICtrl.grid("#permissiongrid", {
				columns: [
						{ display: "编码", name: "code", width: "200", minWidth: 60, type: "string", align: "left" },
     				    { display: "名称", name: "name", width: "250", minWidth: 60, type: "string", align: "left",
							render: function (item) { 
								if(item.type=='field'){//字段权限显示连接
									var html=['<a href="javascript:void(null);" class="GridStyle" '];
									 html.push('funId="',item.id,'" ');
									 html.push('funName="',item.name,'" ');
									 html.push('onclick="showPermissionField(this)" ');
									 html.push('>');
									 html.push(item.name);
									 html.push('</a>');
									 return html.join('');
								}
								return item.name;
							}
						},
 					    { display: "路径", name: "fullName", width: "500", minWidth: 60, type: "string", align: "left" }
				],
				dataAction: "server",
				url: web_app.name  + "/access/slicedQueryAuthorizedPermissionsByOrgFullId.ajax",
				pageSize: 20,
				width: '99%',
				height: "100%",
				sortName: 'fullId',
	            sortOrder: 'asc',
				heightDiff: -8,
				toolbar: toolbarOptions,
				checkbox: true,
				fixedCellHeight: true,
				selectRowButtonOnly: true,
				onLoadData:function(){
					return !(Public.isBlank(parentId));
				}
			});
	 	   UICtrl.setSearchAreaToggle(permissionGridManager, $("#navTitle02"));
	}
	
	function showInsertAuthorizationDialog() {
		if (Public.isBlank(parentId)){
			Public.errorTip("请选择组织节点。");
			return false;
		}
		
		UICtrl.showFrameDialog({
			title: "选择角色",
			width: 800,
			height: 400,
			url: web_app.name + '/access/showSelectRoleDialog.do',
			ok: doSaveAuthorization,
			close: onDialogCloseHandler
		});
	}

	function doSaveAuthorization(){		
		var data = this.iframe.contentWindow.getRoleData();
		if (!data) {
			return;	
		}
		
		var _self = this;
		
		var roleIds = [];
	    for (var i = 0; i < data.length; i++) {
	    	roleIds[roleIds.length] = data[i].id;
	    }
	    
		var params = {};
		params.orgId = parentId;
		params.roleIds = $.toJSON(roleIds);
		
		Public.ajax(web_app.name + "/access/allocateRoles.ajax",params, function() {
			refreshFlag = true;
			_self.close();
		});
	}
	
	function onDialogCloseHandler() {
		if (refreshFlag) {
			reloadGrid();
			refreshFlag = false;
		}
	}
	
	function deleteAuthorization() {
		var data = gridManager.getSelectedRows();
		if (data.length == 0) {
			Public.errorTip("请选择要删除的角色。");
			return;	
		}
		
		UICtrl.confirm("您确定删除选中数据吗?", function(){
			var roleIds = [];
		    for (var i = 0; i < data.length; i++) {
		    	roleIds[roleIds.length] = data[i].roleId;
		    }
		    
			var params = {};
			params.orgId = parentId;
			params.roleIds = $.toJSON(roleIds);
			
			var action = "/access/deallocateRoles.ajax";
			Public.ajax(web_app.name + action, params, function() {
				reloadGrid();
			});
		});
	}
	
	function initializeRoleQueryGrid() {
		roleQueryGridManager = UICtrl.grid("#roleQuerygrid", {
			columns: [
					{ display: "编码", name: "code", width: "150", minWidth: 60, type: "string", align: "left" },
     				{ display: "名称", name: "name", width: "200", minWidth: 60, type: "string", align: "left",
						render: function (item) { 
							var html=['<a href="javascript:void(null);" class="GridStyle" '];
							html.push('id="',item.id,'" ');
							html.push('name="',item.name,'" ');
							html.push('onclick="showRolePermission(this)" ');
							html.push('>');
							html.push(item.name);
							html.push('</a>');
							return html.join('');
						}
					},
 					{ display: "路径", name: "fullName", width: "300", minWidth: 60, type: "string", align: "left" },
 					{ display: "授权组织", name: "orgName", width: "150", minWidth: 60, type: "string", align: "left" },
 					{ display: "授权组织路径", name: "orgFullName", width: "300", minWidth: 60, type: "string", align: "left" }
 					
			],
			dataAction: "server",
			url: web_app.name  + "/access/slicedQueryRolesByOrgFullId.ajax",
			pageSize: 20,
			width: '99%',
			height: "100%",
			sortName: 'fullId',
	        sortOrder: 'asc',
			heightDiff: -8,
			checkbox: true,
			fixedCellHeight: true,
			selectRowButtonOnly: true,
			onLoadData:function(){
				return !(Public.isBlank(parentId));
			}
		});
		UICtrl.setSearchAreaToggle(roleQueryGridManager, $("#navTitle03"));
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
		gridManager.options.parms.orgId = data.id;
		permissionGridManager.options.parms.orgKindId = data.orgKindId;
		permissionGridManager.options.parms.orgId = data.id;
		permissionGridManager.options.parms.orgFullId = data.fullId;
		roleQueryGridManager.options.parms.orgFullId = data.fullId;
		reloadGrid();
		var html=['<span class="tomato-color">[',data.name,']</span>','组织机构树'];
		$('#layout').layout('setLeftTitle',html.join(''));
	}
}

function reloadGrid() {
	if ($("#menuRoleAuthorize").hasClass("active")){
		gridManager.loadData();
	}else if ($("#menuPermissionQuery").hasClass("active")){
		permissionGridManager.loadData();
	}else{
		roleQueryGridManager.loadData();
	}
}

function showPermissionField(obj){
	var id=$(obj).attr('funId'),name=$(obj).attr('funName');
	UICtrl.showFrameDialog({
		url: web_app.name + "/system/opm/permissionField/showPermissionField.jsp",
		param: {functionFieldGroupId: id},
		title: name,
		width: 650,
		height: 400,
		cancelVal: '关闭',
		ok: false,
		cancel:true
	});
}

function showRolePermission(obj){
	var id=$(obj).attr('id'),name=$(obj).attr('name');
	UICtrl.showFrameDialog({
		url: web_app.name + "/access/forwardRolePermission.do",
		param: {roleId: id},
		title: name,
		width: getDefaultDialogWidth(),
		height: getDefaultDialogHeight(),
		cancelVal: '关闭',
		ok: false,
		cancel:true
	});
}