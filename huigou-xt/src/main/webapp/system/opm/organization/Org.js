var treeManager, gridManager, parentId, orgKindId, parentOrgKindId, refreshFlag, propteryGridManager = null;
var dataSource = { yesOrNo: { 1: '是', 0: '否'} };

var displayableOrgKinds = "ogn,fld,prj,stm,grp,dpt,pos,psm,fun";

var pageParam = { org: { orgId: "", isProjectOrg: false} };

$(function () {
    bindEvents();
    loadOrgTreeView();
    initializeGrid();
    initializateUI();

    function initializateUI() {
    	UICtrl.initDefaultLayout();
        UICtrl.setSearchAreaToggle(gridManager, false);
        initializateChangePersonMainOrg();
        initializateAddProjectOrgFun();
    }

    function initializateAddProjectOrgFun() {
        $('#toolbar_addProjectOrgFun').comboDialog({ type: 'opm', name: 'selectBizFunction', width: 480,
            dataIndex: "id",
            title: "选择业务岗位",
            checkbox: true,
            onShow: function () {
                if (!parentId) {
                    Public.errorTip("请选择人员。");
                    return false;
                }
                return true;
            },
            onChoose: function () {
                var bizFunctionData = this.getSelectedRows();
                if (bizFunctionData == null || bizFunctionData.length == 0) {
                    Public.errorTip("选择业务岗位。");
                    return;
                }

                var bizFunctionIds = [];
                $.each(bizFunctionData, function (i, o) {
                    bizFunctionIds.push(o.id);
                })

                var params = {};
                params.orgId = parentId;
                params.bizFunctionIds = $.toJSON(bizFunctionIds);

                Public.ajax(web_app.name + "/org/insertBizFunctions.ajax",
    		             params, function () {
    		                 reloadGrid();
    		             });

                return true;
            }
        });
    }

    function initializateChangePersonMainOrg() {
        $('#toolbar_changePersonMainOrg').comboDialog({ type: 'opm', name: 'selectPersonOwnPsm', width: 480,
            getParam: function () {
            	var row=DataUtil.getUpdateRow(gridManager);
            	if(!row) return false;
                return { personId: row.personId };
            },
            title: "选择主岗位",
            checkbox: false,
            onShow: function () {
            	var row=DataUtil.getUpdateRow(gridManager);
            	if(!row) return false;
                return true;
            },
            onChoose: function () {
                var personData = gridManager.getSelectedRow();
                var mainOrgData = this.getSelectedRow();
                if (mainOrgData == null) {
                    Public.errorTip("请选择主岗位。");
                    return;
                }
                var params = {};
                params.personId = personData.personId;
                params.personMemberId = mainOrgData.id;
                Public.ajax(web_app.name + "/org/changePersonMainOrg.ajax", params);
                return true;
            }
        });
    }

    function bindEvents() {
        $("#btnQuery").click(function () {
            reloadGrid2();
        });

        $("#btnReset").click(function () {
            $(this.form).formClean();
        });

        $("#showDisabledOrg,#showVirtualOrg").click(function () {
            parentId = "orgRoot";
            refreshNode();
            reloadGrid2();
        });
    }

    function initializeGrid() {        
        var toolbarOptions = {
            items: [
                { id: "addOrg", text: "添加机构", click: showInsertOrgDialog, img: "fa-plus" },
                { id: "addDept", text: "添加部门", click: showInsertOrgDialog, img: "fa-plus" },
                { id: "addProjectOrgFolder", text: "添加项目组织分类", click: showInsertOrgDialog, img: "fa-plus" },
                { id: "addProjectOrg", text: "添加项目组织", click: showInsertOrgDialog, img: "fa-plus" },
                { id: "addProjectOrgGroup", text: "添加项目组织分组", click: showInsertOrgDialog, img: "fa-plus" },
                { id: "addSaleTeam", text: "添加销售团队", click: showInsertOrgDialog, img: "fa-plus" },
                { id: "addPosition", text: "添加岗位", click: showInsertOrgDialog, img: "fa-plus" },
                { id: "addPerson", text: "添加人员", click: showInsertPersonDialog, img: "fa-user" },
                { id: "addProjectOrgFun", text: "添加业务岗位", img: "fa-plus" },
                { id: "assignPerson", text: "分配人员", click: assignPerson, img: "fa-user-plus" },
                { id: "updateOrg", text: "修改", click: showUpdateOrg, img: "fa-edit" },
                { id: "deleteOrg", text: "删除", click: logicDeleteOrg, img: "fa-trash-o" },
                { id: "enableOrg", text: "启用", click: enableOrg, img:  "fa-thumbs-o-up" },
                { id: "disableOrg", text: "禁用", click: disableOrg, img:  "fa-thumbs-down" },                
                { id: 'saveID', text:'保存排序号', click: updateOrgSequence, img: "fa-floppy-o"},
                { id: 'initPassword', text:'初始化密码', click: initPassword, img: "fa-wrench"},
                { id: "changePersonMainOrg", text: "设置主岗位", img:  "fa-address-book" },
                { id: "quoteAuthorizationAndBizManagement", text: "引用权限", click: quoteAuthorizationAndBizManagement, img: "fa-gavel"},
                ]
        };
        
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
                { display: "类型", name: "orgKindId", width: 30, minWidth: 30, type: "string", align: "center",
                    render: function (item) {
                        return '<img src="'+ OpmUtil.getOrgImgUrl(item.orgKindId,item.status) + '">';
                    }
                },
                { display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
                { display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },
                { display: "证件号码", name: "certificateNo", width: 160, minWidth: 60, type: "string", align: "left" },
                { display: "全称", name: "longName", width: 100, minWidth: 60, type: "string", align: "left" },
                { display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
                  render: function(item) {
                	  				var id = item.id.replace(/\@/g,"_");
										return "<input type='text' id='txtSequence_"
												+ id
												+ "' class='textbox' value='"
												+ item.sequence + "' />";
									}
				},
                { display: "机构", name: "orgName", width: 100, minWidth: 60, type: "string", align: "left" },
				{ display: "部门", name: "deptName", width: 100, minWidth: 60,type: "string", align: "left" },
                { display: "岗位", name: "positionName", width: 100, minWidth: 60,type: "string", align: "left" },
                { display: "主岗位", name: "mainOrgName", width: 100, minWidth: 60,type: "string", align: "left" },
                { display: "路径", name: "fullName", width: 100, minWidth: 60, type: "string", align: "left" },
                { display: "是否虚拟组织", name: "isVirtual", width: 100, minWidth: 60, type: "string", align: "left",
                     render: function (item) {
                         return item.isVirtual == 1 ? "是": "否";
                     }
                 }
            ],
            dataAction: "server",
            url: web_app.name + "/org/slicedQueryOrgs.ajax",
            parms: { parentId: "orgRoot", showPosition: 1,manageCodes:$('#delegationManageType').val()},
            pageSize: 20,
            sortName: 'fullSequence',
            sortOrder: 'asc',
            toolbar: toolbarOptions,
            width: "99.8%",
            height: "100%",
            heightDiff: -10,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
            	if (data.orgKindId != OrgKind.Function){
	                if (data.orgKindId == OrgKind.Psm){
	                	doShowUpdatePerson(data.personId);
	                } else{
	                	doShowUpdateOrg(data.id);
	                }
            	}
            }
        });

        hideOrgOperateButtons();

        $("#toolbar_addOrg").show();
    }

    function getId() {
        return ($("#id").val());
    }

    function getTemplateId() {
        return ($("#templateId").val());
    }

    function checkSelectedParentId() {
        if (parentId == "null" || !parentId) {
            Public.tip("请选择父节点。");
            return false;
        }
        return true;
    }

    function checkSelectedData() {
    	return DataUtil.getUpdateRow(gridManager);
    }

    function initializateTabPannel() {
    	$('#tabPage').tab();
    }

    function initializePropertyGrid() {
    	 propteryGridManager = UICtrl.grid('#propertyGrid', {
         columns: [
                { display: "属性名", name: "description", width: 150, minWidth: 60, type: "string", align: "left" },
                { display: "属性值", name: "propertyDisplay", width: 150, minWidth: 60, type: "string", align: "left",
                	 editor: { 
                		 type: "dynamic", getEditor: function (row) {
                    		 var dataSource = row['dataSource'] || "";
                    		 if (dataSource) {
                    			 if (typeof dataSource == "string"){
                    				 dataSource = (new Function("return " + dataSource))();
                                 }
                    			 return dataSource;
                    		 }
                    		 return {}; 
                		 }
                	 }
                }
            ],
        dataAction: 'server',
        url: web_app.name + '/org/queryOrgProperties.ajax',
        parms: { orgId: $("#id").val() },
        width: 356,
        sortName: 'sequence',
        sortOrder: 'asc',
        height: 340,
        heightDiff: -2,
        enabledEdit: true,
        usePager: false,
        fixedCellHeight: true,
        selectRowButtonOnly: true,
        autoAddRowByKeydown: false,
        onLoadData: function () {
            return $('#id').val() != '';
        }
    });
}

function showInsertOrgDialog(id) {
    if (!checkSelectedParentId()){
    	return;
    }
    var orgKindId = "";
    switch (id) {
        case "toolbar_addOrg":
            orgKindId = OrgKind.Org;
            break;
        case "toolbar_addDept":
            orgKindId = OrgKind.Dept;
            break;
        case "toolbar_addPosition":
            orgKindId = OrgKind.Position;
            break;
        case "toolbar_addProjectOrgFolder":
            orgKindId = OrgKind.Folder;
            break;
        case "toolbar_addProjectOrg":
            orgKindId = OrgKind.Project;
            break;
        case "toolbar_addProjectOrgGroup":
            orgKindId = OrgKind.Group;
            break;
        case "toolbar_addSaleTeam":
        	orgKindId = OrgKind.SaleTeam;
        	break;
    }

    UICtrl.showAjaxDialog({
        title: "添加组织",
        param: {
            parentId: parentId,
            orgKindId: orgKindId
        },
        init: function () {
            //机构，选择模板
            $("#btnSelectOrgType").hide();
            $("#btnSelectOrgTemplate").hide();
            
            switch(orgKindId){
            case OrgKind.Org:
            	$("#btnSelectOrgTemplate").show();
            	break;
            case OrgKind.Dept:
            case OrgKind.Position:
            	$("#btnSelectOrgType").show();
            	break;
            case OrgKind.Project:
            	break;
            default:
            	$("#l2").hide();
            }

            $('#detailName').on('blur', function () {
                if (!$('#detailCode').val()) {
                    $('#detailCode').val($.chineseLetter($(this).val()));
                }
            });
           // initializateIsCenter(orgKindId);
            initializteIsVirtual();
            initializateTabPannel();
            initializePropertyGrid();
        },
        width: 406,
        url: web_app.name + '/org/showOrgDetail.load',
        ok: doSaveOrg,
        close: onDialogCloseHandler
    });
}

function initializteIsVirtual() {
    $('#isVirtual').combox({ data: dataSource.yesOrNo });
}

//function initializateIsCenter(orgKindId) {
//    //部门，是否中心可见
//    if (orgKindId == OrgKind.Dept) {
//        $('#divIsCenter').show();
//        $('#isCenter').combox({ data: dataSource.yesOrNo });
//    } else {
//        $('#divIsCenter').hide();
//    }
//}

//function orgNodeKindHasExtendProperty(orgKindId){
//	return orgKindId == OrgKind.Dept || orgKindId == OrgKind.Org || orgKindId == OrgKind.Project;	
//}

function doShowUpdateOrg(id) {
    UICtrl.showAjaxDialog({
        url: web_app.name + '/org/loadOrg.load',
        param: { id: id },
        init: function () {
            $("#btnSelectOrgType").show();
            $("#btnSelectOrgTemplate").hide();

            var orgKindId = $("#orgKindId").val();

            if (orgKindId == OrgKind.Folder || orgKindId == OrgKind.Project || orgKindId == OrgKind.Group || orgKindId == OrgKind.Function) {
                $("#btnSelectOrgType").hide();
            }
//            if (!orgNodeKindHasExtendProperty(orgKindId)) {
//                $("#l2").hide();
//            }
            $('#detailName').on('blur', function () {
                if (!$('#detailCode').val()) {
                    $('#detailCode').val($.chineseLetter($(this).val()));
                }
            });
            //部门，是否中心可见
            //initializateIsCenter(orgKindId);
            initializteIsVirtual();
            initializateTabPannel();
            initializePropertyGrid();
        },
        title: "修改组织",
        width: 406,
        ok: doSaveOrg,
        close: onDialogCloseHandler
    });
}

function showUpdateOrg(item) {
    var row = checkSelectedData();
    if (!row)
        return;
    doShowUpdateOrg(row.id);
}

function doSaveOrg() {
    var _self = this;
   
    var id = getId();
    var templateId = getTemplateId();
    var url = web_app.name + "/org/insertOrgByTemplate.ajax";
    if (!templateId){
        url = web_app.name + (id ? '/org/updateOrg.ajax': '/org/insertOrg.ajax');
    }

    var param = {};
    if (propteryGridManager != null) {
        propteryGridManager.endEdit();
        var detailData = propteryGridManager.rows;
        if (detailData && detailData.length > 0) {
            $.each(detailData, function(i, o){
            	//没有定义数据源 显示属性赋值到属性值中
            	if (Public.isBlank(o['dataSource'])||$.isEmptyObject(o['dataSource'])) {
                    o['propertyValue']=o['propertyDisplay'];
                }
            	delete o.dataSource;
            });
        	param.properties = Public.encodeJSONURI(detailData);
        }
    }

    $('#submitForm').ajaxSubmit({ url: url, param: param, success: function (data) {
            refreshFlag = true;
            _self.close();
        }
    });
}

function updateOrgSequence() {
	var action = "org/updateOrgSequence.ajax";
	DataUtil.updateSequence({
				action: action,
				gridManager: gridManager,
				onSuccess: reloadGrid
			});
}

function initPassword(){
	var row=DataUtil.getUpdateRow(gridManager);
	if(!row) return false;
	if (row.orgKindId != OrgKind.Psm){
		Public.errorTip("请选择人员。");
		return ;
	}

	var action = "/org/initPassword.ajax";
	
	var params = {};
    params.personId = row.personId;

    Public.ajax(web_app.name + action, params, function () { });
}

function showInsertPersonDialog() {
    if (!checkSelectedParentId())
        return;

    UICtrl.showAjaxDialog({
        title: "添加人员",
        param: {
            mainOrgId: parentId
        },
        width: 840,
        url: web_app.name + '/org/showPersonDetail.load',
        ok: doSavePerson,
        close: onDialogCloseHandler,
        init: initPersonDialog
    });
}

function showUpdatePerson() {
    var row = checkSelectedData();
    if (!row())
        return;

    doShowUpdatePerson(row.id);
}

function doShowUpdatePerson(id) {
    UICtrl.showAjaxDialog({
        url: web_app.name + '/org/loadPerson.load',
        param: {
            id: id
        },
        title: "修改人员",
        width: 840,
        ok: doSavePerson,
        close: onDialogCloseHandler,
        init: initPersonDialog
    });
}

function initPersonDialog() {
	var _img=$('#showPersonPicture');
	var _picturePath=$('#picturePath').val();
	if (Public.isNotBlank(_picturePath)) {
		_img[0].src = $.getCSRFUrl('attachment/downFileBySavePath.ajax',{file:_picturePath});
	}
	//注册上传照片事件
	$('#addPicture').uploadButton({
		filetype: ['jpg','gif','jpeg','png','bmp'],
		afterUpload: function(data){
			_img[0].src = $.getCSRFUrl('attachmentDownFile.ajax',{id:data.id});
		},
		param: function(){
			var id = $('#id').val();
			if(id == ''){
				Public.errorTip('请先保存人员。');
				return false;
			}
			return {bizCode: 'PersonPicture', bizId: id, flag: 'false', deleteOld: 'true', returnPath: 'true'};
		}
	});
}


function doSavePerson() {
    var _self = this;
    var id = getId();
    
    var param = {};
    
    var url = web_app.name  + (id ? '/org/updatePerson.ajax' : '/org/insertPerson.ajax');
    $('#submitForm').ajaxSubmit({ url: url, param: param, success: function () {
            refreshFlag = true;
            _self.close();
        }
    });
}

function onDialogCloseHandler() {
    propteryGridManager = null;
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function quoteAuthorizationAndBizManagement() {
    var row = checkSelectedData();
    if (!row) {
        return;
    }

    var params = OpmUtil.getSelectOrgDefaultParams();
    params = jQuery.extend(params, { selectableOrgKinds: "ogn,dpt,pos,psm" });
    var destOrgId = row.id;
    var options = {
            params: params, confirmHandler: function () {
            	doQuoteAuthorizationAndBizManagement.call(this, destOrgId);
            },
            closeHandler: onDialogCloseHandler, title: "选择源组织"
    };
    OpmUtil.showSelectOrgDialog(options);
    /*UICtrl.showFrameDialog({
        title: "选择源组织",
        url: web_app.name + "/org/showSelectOrgDialog.do",
        param: params,
        width: 700,
        height: 400,
        ok: function () {
            doQuoteAuthorizationAndBizManagement.call(this, destOrgId);
        },
        close: onDialogCloseHandler,
        cancel: true
    });*/
}

function doQuoteAuthorizationAndBizManagement(destOrgId) {
	var data = this.getSelectedData();
    if (data.length == 0) {
        Public.errorTip("请选择源组织。");
        return;
    }
    var _self = this;

    var params = {};
    params.sourceOrgId = data[0].id;
    params.destOrgId = destOrgId;
    Public.ajax(web_app.name + "/org/quoteAuthorizationAndBizManagement.ajax",
            params, function () {
                refreshFlag = true;
                _self.close();
            });
}

function assignPerson() {
    var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
    selectOrgParams = jQuery.extend(selectOrgParams, { multiSelect: 1, selectableOrgKinds: "psm" });
    
    var options = {
        params: selectOrgParams, confirmHandler: function () {
        	doAssignPerson.call(this);
        },
        closeHandler: onDialogCloseHandler, title: "分配人员"
    };
    OpmUtil.showSelectOrgDialog(options);

//    UICtrl.showFrameDialog({
//        title: "分配人员",
//        url: web_app.name + "/org/showSelectOrgDialog.do",
//        param: OpmUtil.getSelectOrgDefaultParams(),
//        width: 700,
//        height: 400,
//        ok: doAssignPerson,
//        close: onDialogCloseHandler
//    });
}

function doAssignPerson() {
    var data = this.getSelectedData();
    if (data.length == 0) {
        Public.errorTip("请选择人员。");
        return;
    }
    var _self = this;

    var personIds = [];
    for (var i = 0; i < data.length; i++) {
        personIds[personIds.length] = OpmUtil
                .getPersonIdFromPsmId(data[i].id);
    }

    var params = {};
    params.orgId = parentId;
    params.personIds = $.toJSON(personIds);
    Public.ajax(web_app.name + "/org/insertPersonMembers.ajax",
            params, function () {
                refreshFlag = true;
                _self.close();
            });
}

function enableOrg() {
    var row = checkSelectedData();
    if (!row)
        return;

    var isMasterPsm = (row.parentId == row.personMainOrgId);
    var name = row.name;
    var orgKindId = row.orgKindId;
    var personStatus = row.personStatus;

    var msg;
    var enableOrgAction;
    if (orgKindId != "psm" || isMasterPsm || personStatus == 1) {
        msg = "确实要启用“"
                + name
                + "”吗？"
                + (orgKindId != "psm" ? "<br/><br/>“启用”操作会同时启用选中组织的所有下级组织。"
                   : "");
        enableOrgAction = "org/enableOrg.ajax";
    } else {
        msg = "“" + name + "”" + "的主岗位已被禁用，在启动当前岗位时会同时启用主岗位。"
                + "<br/><br/>确实要启用“" + name + "”的主岗位和当前岗位吗？";
        enableOrgAction = "org/enableSubordinatePsm.ajax";
    }

    var params = {};
    params.id = row.id;
    params.personId = row.personId;
    params.version = row.version;

    DataUtil.updateById({
        action: enableOrgAction,
        gridManager: gridManager,
        param: params,
        message: msg,
        onSuccess: function () {
            Public.tip("启用“" + name + "”成功。");
            reloadGrid();
        }
    });
}

function disableOrg() {
    var row = checkSelectedData();
    if (!row)
        return;

    var isMasterPsm = (row.parentId == row.mainOrgId);
    var name = row.name;
    var orgKindId = row.orgKindId;

    var msg = "确实要禁用“"
            + name
            + "”吗？"
            + (orgKindId != "psm" ? "<br/><br/>“禁用”操作会禁用选中组织及其所有下级组织。": "");
+((orgKindId == "psm" && isMasterPsm) ? "<br/><br/>禁用人员的主岗位会同时禁用人员的所有其他岗位。"
           : "");

    var params = {};
    params.id = row.id;
    params.version = row.version;

    DataUtil.updateById({
        action: 'org/disableOrg.ajax',
        gridManager: gridManager,
        param: params,
        message: msg,
        onSuccess: function () {
            Public.tip("禁用“" + name + "”成功。");
            reloadGrid();
        }
    });
}

function logicDeleteOrg() {
    var row = checkSelectedData();
    if (!row){
    	return;
    }

    var action = "org/logicDeleteOrg.ajax";
    action = action + "?id=" + row.id;

    var name = row.name;

    var msg = "“删除”操作会同时删除当前选中组织的所有直属下级及直属人员的所有人员成员。删除后的组织您还可以在回收站中进行“还原”和“清除”操作。<br/><br/>确实要删除“"
            + name + "”吗？";
    DataUtil.del({ action: action, gridManager: gridManager, message: msg, onSuccess: reloadGrid });
}
});

function hideOrgOperateButtons() {
    $("#toolbar_addOrg").hide();
    $("#toolbar_addProjectOrgFolder").hide();
    $("#toolbar_addProjectOrg").hide();
    $("#toolbar_addProjectOrgGroup").hide();
    $("#toolbar_addSaleTeam").hide();
    $("#toolbar_addProjectOrgFun").hide();
    $("#toolbar_addDept").hide();
    $("#toolbar_addPosition").hide();
    $("#toolbar_addPerson").hide();
    $("#toolbar_assignPerson").hide();
    $("#toolbar_changePersonMainOrg").hide();
    $("#toolbar_initPassword").hide();
    $("#separator_assignLine").hide();
}

function switchButtonsVisible(parentOrgKindId){
	switch (parentOrgKindId) {
	case OrgKind.Root:
	    $("#toolbar_addOrg").show();
	    break;
	case OrgKind.Org:
	    $("#toolbar_addOrg").show();
	    $("#toolbar_addDept").show();
	    $("#toolbar_addProjectOrgFolder").show();
	    break;
	case OrgKind.Dept:
	    $("#toolbar_addDept").show();
	    $("#toolbar_addPosition").show();
	    break;
	case OrgKind.Position:
	    $("#toolbar_addPerson").show();
	    $("#toolbar_assignPerson").show();
	    $("#toolbar_changePersonMainOrg").show();
	    $("#separator_assignLine").show();
	    $("#toolbar_initPassword").show();
	    break;
	case OrgKind.Folder:
	    $("#toolbar_addProjectOrgFolder").show();
	    $("#toolbar_addProjectOrg").show();
	    break;
	case OrgKind.Project:
	    $("#toolbar_addProjectOrgGroup").show();
	    $("#toolbar_addSaleTeam").show();
	    break;
	case OrgKind.SaleTeam:
		$("#toolbar_addDept").show();
		break;
	case OrgKind.Group:
	    $("#toolbar_addPerson").show();
	    $("#toolbar_assignPerson").show();
	    break;
	case OrgKind.Psm:
	    if (pageParam.org.isProjectOrg) {
	        $("#toolbar_addProjectOrgFun").show();
	    }	    
	    break;
	}
}


function isShowDisabledOrg(){
	return $("#showDisabledOrg").is(":checked") ? 1 : 0;
}

function isShowVirtualOrg(){
	return $("#showVirtualOrg").is(":checked") ? 1 : 0;
}

function loadOrgTreeView() {
    if (treeManager){
    	treeManager.clear();
    }
    var manageType=$('#delegationManageType').val();
	if (Public.isBlank(manageType)) {
		manageType=null;
	}
	var orgRootId=$('#delegationOrgRootId').val();
	if (Public.isBlank(orgRootId)) {
		orgRootId=parentId;
	}
    Public.ajax(web_app.name + "/org/queryOrgs.ajax", {
        parentId: orgRootId,
        showPosition: 1,
        showDisabledOrg: isShowDisabledOrg(),
        showVirtualOrg: isShowVirtualOrg(),
        displayableOrgKinds: displayableOrgKinds,
        manageCodes:manageType
    }, function (data) {
        treeManager = UICtrl.tree("#maintree", {
            data: data.Rows,
            idFieldName: "id",
            parentIDFieldName: "parentId",
            textFieldName: "name",
            checkbox: false,
            iconFieldName: "icon",
            btnClickToToggleOnly: true,
            nodeWidth: 180,
            isLeaf: function (data) {
                data.children = [];
                data.icon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
                return data.hasChildren == 0;
            },
            onBeforeExpand: onBeforeExpand,
            onClick: function (node) {
                if (!node || !node.data) {
                    return;
                }

                pageParam.org.orgId = node.data.orgId;
                pageParam.org.isProjectOrg = OpmUtil.isProjectOrg(node.data.fullId);

                parentOrgKindId = node.data.orgKindId;
                parentId = node.data.id;
                
                gridManager.options.parms.parentId = parentId;
                gridManager.options.parms.fullId = node.data.fullId;
                
                if (!node.data.parentId) {
                	$("#layout").layout("setCenterTitle", "组织机构列表");
                } else {
                	$("#layout").layout("setCenterTitle",  "<span class=\"tomato-color\">["
                            + node.data.name + "]</span>组织机构列表");
                }

                hideOrgOperateButtons();
                switchButtonsVisible(parentOrgKindId);
                
                reloadGrid2();
            } // end of onClick
        }); // end of UICtrl.tree
    }); // end of function(data)
}

function onBeforeExpand(node) {
    if (node.data.hasChildren) {
        if (!node.data.children || node.data.children.length == 0) {
        	var manageType=$('#delegationManageType').val();
        	if (Public.isBlank(manageType)) {
        		manageType=null;
        	}
            Public.ajax(web_app.name + "/org/queryOrgs.ajax", {
                parentId: node.data.id,
                showPosition: 1,
                showDisabledOrg: isShowDisabledOrg(),
                showVirtualOrg:  isShowVirtualOrg(),
                displayableOrgKinds: displayableOrgKinds,
                manageCodes:manageType
            }, function (data) {
                treeManager.append(node.target, data.Rows);
            });
        }
    }
}

function reloadGrid() {
    refreshNode();
    reloadGrid2();
}

function getShowDisabledOrg(){
	return $("#showDisabledOrg").is(':checked') ? 1 : 0;
}

function getShowVirtualOrg(){
	return $("#showVirtualOrg").is(':checked') ? 1 : 0;
}

function getShowAllChildrenOrg(){
	return $("#showAllChildrenOrg").is(':checked') ? 1 : 0;
}

function getDisplayableOrgKinds(){
	var orgKindIds = "";
	if ($("#showOrg").is(':checked')){
		orgKindIds = "ogn,"
	}
	if ($("#showDept").is(':checked')){
		orgKindIds += "dpt,"
	}
	if ($("#showPos").is(':checked')){
		orgKindIds += "pos,"
	}
	if ($("#showPsm").is(':checked')){
		orgKindIds += "psm,"
	}	
	if (!Public.isBlank(orgKindIds)) {
    	orgKindIds = orgKindIds.substring(0, orgKindIds.length - 1);
    }
	return orgKindIds;
}


function reloadGrid2() {
    var params = $("#queryMainForm").formToJSON();
    
    gridManager.options.parms.showDisabledOrg = getShowDisabledOrg();
    gridManager.options.parms.showVirtualOrg = getShowVirtualOrg();
    gridManager.options.parms.showAllChildrenOrg = getShowAllChildrenOrg();    
    gridManager.options.parms.displayableOrgKinds = getDisplayableOrgKinds();

    UICtrl.gridSearch(gridManager, params);
}

function refreshNode(pNode) {
    var parentData;
    if (pNode){
    	parentData = pNode;
    }  else{
    	parentData = treeManager.getDataByID(parentId || "orgRoot");
    }
    if (parentData) {
        if (parentData.children && parentData.children.length > 0) {
            for (var i = 0; i < parentData.children.length; i++) {
                treeManager.remove(parentData.children[i].treedataindex);
            }
        }
        Public.ajax(web_app.name + "/org/queryOrgs.ajax", {
            parentId: parentData.id,
            displayableOrgKinds: displayableOrgKinds,
            showDisabledOrg: this.isShowDisabledOrg(),
            showVirtualOrg: this.isShowVirtualOrg()
        }, function (data) {
            if (!data.Rows || data.Rows.length == 0) {
                var pn = treeManager.getParent(parentData.treedataindex);
                if (pn)
                    refreshNode(pn);
            } else {
                treeManager.append(parentData, data.Rows);
            }
        });
    }
}

function showSelectOrgTypeDialog() {
    var orgKindId = $("#orgKindId").val();
    var params = {
        orgKindId: orgKindId,
        confirmHandler: onSelectedOrgTypeHandler,
        closeHandler: function () {
        },
        lock: false,
        isMultipleSelect: "false"
    };
    OpmUtil.showSelectOrgTypeDialog(params);
}

function onSelectedOrgTypeHandler() {
    _self = this;
    var fn = this.iframe.contentWindow.getOrgTypeData;
    var data = fn();
    if (data){
	    $("#typeId").val(data[0].id);
	    $("#detailCode").val(data[0].code);
	    $("#detailName").val(data[0].name);
	    $("#longName").val(data[0].name);
	    _self.close();
    }
}

function showSelectOrgTempalteDialog() {
    var params = { orgKindId: orgKindId, 
    		confirmHandler: onSelectedOrgTemplateHandler, 
    		lock: false,
    		isMultipleSelect: "false",
    		closeHandler: function (){}
           }
    OpmUtil.showSelectOrgTemplateDialog(params);
}

function onSelectedOrgTemplateHandler() {
    _self = this;
    var data = this.iframe.contentWindow.getOrgTemplateData();
    if (!data) return;
    $("#templateId").val(data[0].id);
    $("#detailCode").val(data[0].code);
    $("#detailName").val(data[0].name);
    $("#longName").val(data[0].name);
    _self.close();
}

/**
* 调整组织架构 
*/
function doAdjustOrg() {
    var params = {};
    params.sourceOrgId = $("#sourceOrgId").val();
    params.destOrgId = $("#destOrgId").val();

    if (!params.sourceOrgId) {
        Public.errorTip("请选择源组织。");
        return;
    }

    if (!params.destOrgId) {
        Public.errorTip("请选择目标组织。");
        return;
    }

    if (params.sourceOrgId == params.destOrgId) {
        Public.errorTip("选择的源组织和目标组织相同。");
        return;
    }
    var _self = this;
    Public.ajax(web_app.name + "/org/adjustOrg.ajax",
         params, function () {
             _self.close();
         });
}

function dialogCloseHandler() {

}

function adjustOrg() {
    UICtrl.showAjaxDialog({ url: web_app.name + "/org/showAdjustOrgDialog.load",
        width: 300, top: 100, ok: doAdjustOrg, title: "调整组织架构", init: initShowAdjustOrgSelectOrgDialog, close: dialogCloseHandler
    });
}

function initShowAdjustOrgSelectOrgDialog() {
    initSelectOrgDialog("#sourceOrgId", "#sourceOrgName");
    initSelectOrgDialog("#destOrgId", "#destOrgName");
}

/**
* 初始化选择组织对话框
*/
function initSelectOrgDialog(elId, elName) {
    var orgName = $(elName);
    var filter = 'ogn,dpt';
    orgName.orgTree({ filter: filter, param: { searchQueryCondition: "org_kind_id in('ogn','dpt')" },
        getParam: function () {
            var mode = this.mode;
            if (mode == 'tree') {//更改树的根节点
                return { a: 1, b: 1, orgRoot: 'orgRoot', searchQueryCondition: "org_kind_id in('ogn','dpt')" };
            } else {
                var param = { a: 1, b: 1 }, condition = ["org_kind_id in ('ogn','dpt')"];
                param['searchQueryCondition'] = condition.join('');
                return param;
            }
        },
        back: {
            text: orgName,
            value: elId,
            id: elId,
            name: orgName
        }
    });
}