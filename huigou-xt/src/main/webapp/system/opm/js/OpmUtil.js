var OpmUtil = {};

var OrgTemplate = OrgTemplate || {};

OrgTemplate.ROOT_PARENT_ID_VALUE = "root";

var OrgKind = OrgKind || {};

/**
* 根节点
*/
OrgKind.Root = "root";

/**
* 机构
*/
OrgKind.Org = "ogn";

/**
* 部门
*/
OrgKind.Dept = "dpt";

/**
* 岗位
*/
OrgKind.Position = "pos";

/**
* 人员
*/
OrgKind.Psm = "psm";

/**
* 项目组织分类
*/
OrgKind.Folder = "fld";

/**
* 项目组
*/
OrgKind.Project = "prj";

/**
 * 销售团队
 */
OrgKind.SaleTeam = "stm";

/**
* 分组
*/
OrgKind.Group = "grp";

/**
* 业务职能
*/
OrgKind.Function = "fun";

/**
* 组织系统级分组
*/
OrgKind.SystemGroup = "system";



OpmUtil.showSelectOrgTypeDialog = function (options) {
    UICtrl.showFrameDialog({
        url: web_app.name + '/orgType/showSelectOrgTypeDialog.do',
        param: {
            orgKindId: options.orgKindId,
            isMultipleSelect: options.isMultipleSelect
        },
        title: OpmUtil.getSelectOrgTypeDialogTitle(options.orgKindId),
        lock: options.lock,
        width: 780,
        height: 400,
        ok: options.confirmHandler,
        close: options.closeHandler
    });
}

OpmUtil.getSelectOrgTypeDialogTitle = function (orgKindId) {
    switch (orgKindId) {
        case OrgKind.Org:
            result = "机构类型";
            break;
        case OrgKind.Dept:
            result = "部门类型";
            break;
        case OrgKind.Position:
            result = "岗位类型";
            break;
        case OrgKind.Project:
            result = "项目组类型";
        default:
            result = "";
    }
    return "选择" + result;
}

OpmUtil.showSelectOrgTemplateDialog = function (options) {
    UICtrl.showFrameDialog({
        url: web_app.name + '/orgTemplate/showSelectOrgTemplateDialog.do',
        title: "选择机构模板",
        lock: options.lock,
        width: 760,
        height: 400,
        ok: options.confirmHandler,
        close: options.closeHandler
    });
}

OpmUtil.showSelectBizManagementTypeDialog = function (options) {
    UICtrl.showFrameDialog({
        url: web_app.name
				+ '/management/showSelectBizManagementTypeDialog.do',
        title: "选择业务管理权限",
        param: { multiSelect: options.multiSelect == 1 ?  1 : 0 },
        width: 780,
        height: 400,
        parent:options.parent,
        ok: options.confirmHandler,
        close: options.closeHandler
    });
}

OpmUtil.showOrgFunctionDialog = function (options) {
    UICtrl.showFrameDialog({
        url: web_app.name 	+ '/org/showSelectOrgFunctionDialog.do',
        title: "选择组织职能",
        width: 600,
        height: 400,
        cancelVal: '关闭',
        ok: options.confirmHandler,
        close: options.closeHandler
    });
}

OpmUtil.showSelectOrgDialog = function (options) {
    UICtrl.showAjaxDialog({
        url: web_app.name + "/org/showSelectOrgDialog.load",
        param: options.params,
        title: options.title,
        width: 740,
        height:380,
        ok: function(){
        	if($.isFunction(options.confirmHandler)){
        		options.confirmHandler.call(this._selectOrg);
        	}
        	return false;
        },
        close:function(){
        	if($.isFunction(options.closeHandler)){
        		return options.closeHandler.call(this._selectOrg);
        	}
        	return true;
        },
        init: function(div){
        	var _obj=$.selectOrgCommon(this,options.params,div);
        	_obj.initView();
            if($.isFunction(options.initHandler)){
            	options.initHandler.call(_obj);
            }
        },
        okVal:typeof options['okVal'] == 'undefined' ? 'common.button.save' : options['okVal'],
        parent: options.parent,
        lock: typeof options['lock'] == 'undefined' ? true : options['lock'],
        cancelVal: 'common.button.close',
        cancel: true
    });
}

OpmUtil.joinCondition = function (condition1, condition2, operator) {
    if (!operator)
        operator = "and";
    if (condition1 && condition2) {
        return "(" + condition1 + ") " + operator + " (" + condition2 + ")";
    }
    else {
        return (condition1 ? condition1 : "") + (condition2 ? condition2 : "");
    }
}

OpmUtil.getMultiLikeFilter = function (fields, value, split) {
    if (!split)
        split = ",";

    var fieldArray = [];
    if (typeof (fields) == "string")
        fieldArray = fields.split(split);
    else if (Object.prototype.toString.apply(fields) == "[object Array]")
        fieldArray = fields;
    else
        throw new Error("无效的fields");

    value = value.toUpperCase();
    var filter = "";
    for (var i = 0; i < fieldArray.length; i++) {
        filter = OpmUtil.joinCondition(filter, "upper(" + fieldArray[i]
				+ ") like '%" + value + "%'", "or");
    }
    return filter;
}

OpmUtil.getOrgKindDisplay = function (orgKindId) {
    if (orgKindId == OrgKind.Org)
        return "机构";
    else if (orgKindId == OrgKind.Dept)
        return "部门";
    else if (orgKindId == OrgKind.Position)
        return "岗位";
    else if (orgKindId == OrgKind.Psm)
        return "人员";
    else if (orgKindId == OrgKind.SystemGroup)
        return "分组";
    else
        return "";
}

OpmUtil.getValidStateLabel = function (validState) {
    if (Object.prototype.toString.apply(validState) == "[object Array]"
			&& validState.length > 0) {
        validState = validState[0].getValue();
    } else if (typeof validState != "string") {
        return "";
    }
    if (validState == 1)
        return "启用";
    else if (validState == 0)
        return "禁用";
    else if (validState == -1)
        return "删除";
    else
        return "";
}

OpmUtil.getRoleKindLabel = function (roleKind) {
    if (Object.prototype.toString.apply(roleKind) == "[object Array]"
			&& roleKind.length > 0) {
        roleKind = roleKind[0].getValue();
    } else if (typeof roleKind != "string") {
        return "";
    }
    if (roleKind == "fun")
        return "功能角色";
    else if (roleKind == "data")
        return "数据角色";
    else
        return "";
}

/**
* 从组织id中提取岗位id
*/
OpmUtil.getOrgIdFromPsmId = function (psmId) {
    return psmId.substring(psmId.indexOf("@") + 1);
}

/**
* 从组织id中提取人员id
*/
OpmUtil.getPersonIdFromPsmId = function (psmId) {
	if(!psmId) return '';
    return psmId.substring(0, psmId.indexOf("@"));
}

OpmUtil.formatPsmId = function (personId, orgId) {
    return personId + "@" + orgId;
}

OpmUtil.isProjectOrg = function (fullId) {
    if (!fullId) {
        return false;
    }
    else {
        return fullId.indexOf(OrgKind.Project) > -1;
    }
}

OpmUtil.checkSearchText = function (s, showError) {
    if (!!s && s.indexOf("'") > -1) {
        if (showError)
            alert("搜索字符串中不能包含单引号。");
        return false;
    }
    return true;
}

OpmUtil.isTwoDepartment = function (orgFullId) {
    var len = orgFullId.match(/dpt/g); //正则表达式
    return len > 1;
}

OpmUtil.getOrgDisplayName = function (orgFullId, orgFullName) {
    var orgInfo = { ognName: "", dptName: "", posName: "" };
    var orgIds = orgFullId.substring(1).split("/");
    var orgNames = orgFullName.substring(1).split("/");
    for (var i = 0; i < orgIds.length; i++) {
        if (orgIds[i].endsWith(OrgKind.Org))
            orgInfo.ognName = orgNames[i];
        else if (orgIds[i].endsWith(OrgKind.Dept))
            orgInfo.dptName = orgNames[i];
        else if (orgIds[i].endsWith(OrgKind.Position))
            orgInfo.posName = orgNames[i];
    }
    return orgInfo;
}

OpmUtil.OrgImagePath = web_app.name + "/images/org/";

OpmUtil.getOrgImgUrl = function (orgKindId, status, isMasterPsm) {
    var url = OpmUtil.OrgImagePath;
    switch (orgKindId) {
        case OrgKind.Root:
            url += "org_root";
            break;
        case OrgKind.Org:
            url += "org";
            break;
        case OrgKind.Dept:
            url += "dept";
            break;
        case OrgKind.Position:
            url += "pos";
            break;
        case OrgKind.Psm:
            url += "personMember";
            break;
        case OrgKind.Folder:
            url += "folder";
            break;
        case OrgKind.Project:
            url += "project";
            break;
        case OrgKind.Group:
        case OrgKind.SaleTeam:
        	url += "group";
            break;
        case OrgKind.Function:
            url += "role";
            break;
    }
    return (!status) ? url + "-disable.gif" : url + '.gif';
}

OpmUtil.getOrgStatusDisplay = function (status) {
    switch (status) {
        case -1:
            return "删除";
            break;
        case 0:
            return "停用";
            break;
        case 1:
            return "启用";
            break
        default:
            return "";
    }
};

OpmUtil.getSelectOrgDefaultParams = function () {
    return {
        filter: "",
        multiSelect: 1,
        parentId: "orgRoot",
        manageCodes: "",
        displayableOrgKinds: "ogn,dpt,pos,psm",
        selectableOrgKinds: "psm",
        showDisabledOrg: 0,
        showVirtualOrg: 0,
        showProjectOrg: 0,
        showPosition: 0,
        chooseChildCheck: 1,
        showCommonGroup: 0,
        cascade: 1,
        customDefinedRoot:0,
        rootIds: "",
        selectOrgScope: "all"
    }
}

/**
* 项目组织工具类
*/
var ProjectOrgUtil = {};

/**
* 从FullId中得到最近的机构Id
*/
ProjectOrgUtil.getOrganIdFromFullId = function (fullId) {
    if (fullId) {
        var splits = fullId.split('/');
        var reg = new RegExp("\\s*.ogn($)", "i");
        var organInfo;
        for (var i = splits.length - 1; i >= 0; i--) {
            if (splits[i] != '' && reg.test(splits[i])) {
                organInfo = splits[i].split('.');
                return organInfo[0];
            }
        }
    }
    return "";
}

var CommonNodeKind = CommonNodeKind || {};
CommonNodeKind.Limb = 1; //分支
CommonNodeKind.Leaf = 2;//叶子

OpmUtil.createCheckBoxToGridTile = function (el, event, labeName) {
    var html = ['<div class="ui-grid-query-div">'];
    html.push('<input type="checkbox">' + labeName);
    html.push('</div>');
    var div = $(html.join('')).appendTo(el);
    div.find('input').bind('click', function () {
        if ($.isFunction(event)) {
            event.call(window, div.find('input').is(':checked'));
        }
    });
};

/**
 * 资源类型
 */
var ResourceKind = ResourceKind || {};

ResourceKind.FUN = "fun";
ResourceKind.UI_ELEMENT = "uiElement";
ResourceKind.REMIND = "remind";
ResourceKind.BUSINESSCLASS = "businessclass";

var PermissionNodeKind = PermissionNodeKind || {};
PermissionNodeKind.FOLDER = "folder";
PermissionNodeKind.FUN = "fun";
PermissionNodeKind.PERMISSION = "permission";
PermissionNodeKind.REMIND = "remind";
PermissionNodeKind.BUSINESSCLASS = "businessclass";

