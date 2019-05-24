package com.huigou.uasp.bmp.opm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.doc.attachment.application.AttachmentApplication;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgProperty;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgPropertyDefinition;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.domain.query.OrgPropertyDefinitionQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.OrgQueryModel;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.OrgTypeApplicationProxy;
import com.huigou.uasp.bmp.securitypolicy.application.SecurityPolicyApplication;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 
 * @author gongmm
 */
@Controller
@ControllerMapping("org")
public class OrgController extends CommonController {

    private static final String ORG_PROPERTY_DEFINITION_PAGE = "OrgPropertyDefinition";

    private static final String ORG_PROPERTY_DEFINITION_DETAIL_PAGE = "OrgPropertyDefinitionDetail";

    private static final String ORG_PAGE = "Org";

    private static final String ORG_DETAIL_PAGE = "OrgDetail";

    private static final String PERSON_DETAIL_PAGE = "PersonDetail";

    private static final String PERSON_QUERY_PAGE = "PersonQuery";

    private static final String SELECT_ORG_DIALOG = "SelectOrgCommonPage";

    //private static final String ADJUST_ORG_DIALOG = "ShowAdjustOrganDialog";

    private static final String RECYCLE_BIN_PAGE = "RecycleBin";

    @Autowired
    private OrgApplicationProxy application;

    @Autowired
    private OrgTypeApplicationProxy orgTypeApplication;

    @Autowired
    private AttachmentApplication attachmentApplication;

    @Override
    protected String getPagePath() {
        return "/system/opm/organization/";
    }

    @RequiresPermissions("OrgPropertyDefinition:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到组织属性定义页面")
    public String forwardOrgPropertyDefinition() {
        this.putAttribute("orgKindList", OrgNodeKind.getData());
        return this.forward(ORG_PROPERTY_DEFINITION_PAGE);
    }

    @RequiresPermissions("Org:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到组织管理页面")
    public String forwardOrg() {
        return this.forward(ORG_PAGE);
    }

    @RequiresPermissions("RecycleBin:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到回收站页面")
    public String forwardRecycleBin() {
        return this.forward(RECYCLE_BIN_PAGE);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到人员查询页面")
    public String forwardPersonQuery() {
        return this.forward(PERSON_QUERY_PAGE);
    }

    // @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到组织机构AD页面")
    // public String forwardOrgAD() {
    // return this.forward("/system/opm/ad/Org.jsp");
    // }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改密码页面")
    public String showUpdatePassword() {
        return forward("/system/userPanel/UpdatePassword.jsp");
    }

    @RequiresPermissions("OrgPropertyDefinition:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加组织属性定义页面")
    public String showInsertOrgPropertyDefinition() {
        this.putAttribute("orgKindList", OrgNodeKind.getData());
        return forward(ORG_PROPERTY_DEFINITION_DETAIL_PAGE);
    }

    @RequiresPermissions("OrgPropertyDefinition:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改组织属性定义页面")
    public String showUpdateOrgPropertyDefinition() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        this.putAttribute("orgKindList", OrgNodeKind.getData());
        OrgPropertyDefinition orgPropertyDefinition = application.loadOrgPropertyDefinition(id);
        return forward(ORG_PROPERTY_DEFINITION_DETAIL_PAGE, orgPropertyDefinition);
    }

    @RequiresPermissions(value = { "OrgPropertyDefinition:create", "OrgPropertyDefinition:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.SAVE, description = "保存组织属性定义")
    public String saveOrgPropertyDefinition() {
        SDO params = this.getSDO();
        OrgPropertyDefinition orgPropertyDefinition = params.toObject(OrgPropertyDefinition.class);
        String id = this.application.saveOrgPropertyDefinition(orgPropertyDefinition);
        return success(id);
    }

    @RequiresPermissions("OrgPropertyDefinition:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除组织属性定义")
    public String deleteOrgPropertyDefinitions() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        this.application.deleteOrgPropertyDefinitions(ids);
        return success();
    }

    @RequiresPermissions("OrgPropertyDefinition:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改组织属性定义排序号")
    public String updateOrgPropertyDefinitionsSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap(DATA_KEY_NAME);
        this.application.updateOrgPropertyDefinitionsSequence(data);
        return success();
    }

    @RequiresPermissions("OrgPropertyDefinition:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询组织属性定义")
    public String queryOrgPropertyDefinitions() {
        SDO params = this.getSDO();
        OrgPropertyDefinitionQueryRequest queryRequest = params.toQueryRequest(OrgPropertyDefinitionQueryRequest.class);
        Map<String, Object> data = this.application.queryOrgPropertyDefinitions(queryRequest);
        return this.toResult(data);
    }

    /**
     * 得到父组织的路径
     * 
     * @param parentId
     * @return
     */
    private String getParentPath(String parentId) {
        String parentFullName = "/";
        Org parentOrg = this.application.loadOrg(parentId);
        if (parentOrg != null) {
            parentFullName = parentOrg.getFullName();
        }
        return parentFullName;
    }

    @RequiresPermissions("Org:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改组织明细页面")
    public String showOrgDetail() {
        Map<String, Object> params = this.getSDO().getProperties();

        String orgKindId = ClassHelper.convert(params.get("orgKindId"), String.class, "");
        String parentId = ClassHelper.convert(params.get("parentId"), String.class, "");
        String nextSequence = this.application.getOrgNextSequence(parentId);

        params.put("parentFullName", getParentPath(parentId));
        params.put("orgKindName", OrgNodeKind.fromValue(orgKindId).getDisplayName());
        params.put("sequence", Integer.valueOf(nextSequence));
        params.put("status", ValidStatus.ENABLED.getId());

        return this.forward(ORG_DETAIL_PAGE, params);
    }

    @RequiresPermissions("Org:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "通过组织模板添加组织")
    public String insertOrgByTemplate() {
        SDO params = this.getSDO();
        Org org = params.toObject(Org.class);
        String templateId = params.getString("templateId");
        application.insertOrgByTemplateId(org, templateId);
        return success();
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改组织明细页面")
    public String loadOrg() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        Org org = this.application.loadOrg(id);

        this.putAttribute("parentFullName", getParentPath(org.getParentId()));
        this.putAttribute("orgKindName", OrgNodeKind.fromValue(org.getOrgKindId()).getDisplayName());

        return forward(ORG_DETAIL_PAGE, org);
    }

    @RequiresPermissions("Org:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "新增组织")
    public String insertOrg() {
        SDO params = this.getSDO();
        Org org = params.toObject(Org.class);
        String orgTypeId = params.getString("typeId");
        OrgType orgType = null;
        if (!StringUtil.isBlank(orgTypeId)) {
            orgType = orgTypeApplication.loadOrgType(orgTypeId);
        }
        String id = this.application.insertOrg(org, orgType);
        // 保存机构的属性列表
        return success(id);
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改组织")
    public String updateOrg() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        Org org = this.application.loadOrg(id);
        org.getOrgType();
        org.fromMap(params.getProperties());

        List<OrgProperty> orgProperties = params.getList("properties", OrgProperty.class);
        org.setOrgProperties(orgProperties);

        this.application.updateOrg(org);

        return success();
    }

    // @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到调整组织页面")
    // public String showAdjustOrgDialog() {
    // return this.forward(ADJUST_ORG_DIALOG);
    // }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "调整组织")
    public String adjustOrg() {
        /*
         * SDO params = this.getSDO(); try { String sourceOrgId =
         * params.getProperty("sourceOrgId", String.class); String destOrgId =
         * params.getProperty("destOrgId", String.class);
         * this.orgService.adjustOrg(sourceOrgId, destOrgId); return success();
         * } catch (Exception e) { return error(e); }
         */
        return error("not implements.");
    }

    @RequiresPermissions("Org:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "逻辑删除组织")
    public String logicDeleteOrg() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.application.logicDeleteOrg(ids);
        return success();
    }

    @RequiresPermissions("Org:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "物理删除组织")
    public String physicalDeleteOrg() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        this.application.physicalDeleteOrg(id, true);
        return success();
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "恢复组织")
    public String restoreOrg() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        this.application.restoreOrg(id, true);
        return success();
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "启用组织")
    public String enableOrg() {
        SDO params = this.getSDO();
        String id = params.getString(CommonDomainConstants.ID_FIELD_NAME);
        this.application.enableOrg(id, false);
        return success();
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "启用附属人员成员")
    public String enableSubordinatePsm() {
        SDO params = this.getSDO();
        String orgId = params.getString(CommonDomainConstants.ID_FIELD_NAME);
        String personId = params.getString("personId");
        if (StringUtil.isBlank(personId)) {
            personId = orgId.substring(0, orgId.indexOf('@'));
        }
        this.application.enableSubordinatePsm(orgId, personId);
        return success();
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "禁用组织")
    public String disableOrg() {
        SDO params = this.getSDO();
        String id = params.getString(CommonDomainConstants.ID_FIELD_NAME);
        this.application.disableOrg(id);
        return success();
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改人员主组织")
    public String changePersonMainOrg() {
        SDO params = this.getSDO();
        String personId = params.getProperty("personId", String.class);
        String personMemberId = params.getProperty("personMemberId", String.class);
        this.application.changePersonMainOrg(personId, personMemberId, false);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询组织机构")
    public String queryOrgs() {
        SDO params = this.getSDO();
        OrgQueryModel orgQueryModel = new OrgQueryModel(params);
        Map<String, Object> data = this.application.queryOrgs(orgQueryModel);
        return this.toResult(data);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询组织机构")
    public String slicedQueryOrgs() {
        SDO params = this.getSDO();
        OrgQueryModel orgQueryModel = new OrgQueryModel(params);
        Map<String, Object> data = this.application.slicedQueryOrgs(orgQueryModel);
        return this.toResult(data);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询人员的人员成员")
    public String queryPersonMembersByPersonId() {
        SDO params = this.getSDO();
        String personId = params.getOperator().getUserId();
        List<Org> orgs = this.application.queryPersonMembersByPersonId(personId);
        return this.toResult(orgs);// this.packGridDataAndResult(orgs);
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改组织排序号")
    public String updateOrgSequence() {
        Map<String, Object> m = this.getSDO().getObjectMap("data");

        Map<String, String> params = new HashMap<String, String>(m.size());
        for (String item : m.keySet()) {
            params.put(ClassHelper.convert(item, String.class), ClassHelper.convert(m.get(item), String.class));
        }

        this.application.updateOrgSequence(params);
        return success();
    }

    @RequiresPermissions("Org:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加人员明细页面")
    public String showPersonDetail() {
        SDO sdo = this.getSDO();
        String mainOrgId = sdo.getString("mainOrgId");
        this.putAttribute("parentFullName", getParentPath(mainOrgId));
        this.putAttribute("status", ValidStatus.ENABLED.getId());

        this.putAttribute("status", ValidStatus.ENABLED.getId());

        return this.forward(PERSON_DETAIL_PAGE, sdo.getProperties());
    }

    @RequiresPermissions("Org:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加人员")
    public String insertPerson() {
        SDO params = this.getSDO();
        Person person = params.toObject(Person.class);
        this.application.insertPerson(person);
        return success();
    }

    @RequiresPermissions("Org:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加人员成员")
    public String insertPersonMembers() {
        SDO params = this.getSDO();
        List<String> personIds = params.getStringList("personIds");
        String orgId = params.getString("orgId");

        this.application.insertPersonMembers(personIds, orgId, ValidStatus.ENABLED, false);
        return success();
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改人员")
    public String updatePerson() {
        SDO params = this.getSDO();
        Person person = params.toObject(Person.class);
        this.application.updatePerson(person);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改操作员")
    public String updateOperator() {
        /*
         * SDO params = this.getSDO(); try {
         * this.orgService.updateOperator(params); return success(); } catch
         * (Exception e) { return error(e); }
         */
        return error("not implements.");
    }

    // TODO
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "对比AD")
    public String compareAD() {
        /*
         * SDO params = this.getSDO(); try { String personId =
         * params.getProperty("personId", String.class);
         * this.orgService.compareAD(personId); return success(); } catch
         * (Exception e) { return error(e); }
         */
        return error("not implements.");
    }

    @RequiresPermissions("Org:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改人员明细页面")
    public String loadPerson() {
        SDO params = this.getSDO();
        String id = params.getString("id");

        Person person = this.application.loadPerson(id);

        this.putAttribute("parentFullName", getParentPath(person.getMainOrgId()));

        List<Attachment> attachments = attachmentApplication.queryAttachments(Constants.PERSON_PICTURE, id);

        if (attachments.size() > 0) {
            Attachment attachment = attachments.get(0);
            this.putAttribute("picturePath", StringUtil.encode(attachment.getPath()));
        }
        return forward(PERSON_DETAIL_PAGE, person);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询人员")
    public String slicedQueryPerson() {
        SDO params = this.getSDO();
        String parentIdOrFullId = params.getString("parentIdOrFullId");
        boolean showDisabled = "1".equals(params.getString("showDisabledOrg"));
        boolean showAllChildren = "1".equals(params.getString("showAllChildrenOrg"));
        CodeAndNameQueryRequest queryRequest = params.toQueryRequest(CodeAndNameQueryRequest.class);
        Map<String, Object> data = this.application.slicedQueryPerson(parentIdOrFullId, showDisabled, showAllChildren, queryRequest);
        return this.toResult(data);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到人员明细页面")
    public String loadPersonAD() {
        return error("not implements.");
        /*
         * SDO params = this.getSDO(); try { String id =
         * params.getProperty("id", String.class); Map<String, Object> data =
         * this.orgService.loadPerson(id); String mainOrgId =
         * ClassHelper.convert(data.get("mainOrgId"), String.class);
         * data.put("parentFullName", getParentPath(mainOrgId)); String
         * picturePath = orgService.loadArchivesPictureByPersonId(id);
         * this.putAttribute("picturePath", StringUtil.encode(picturePath));
         * this.putAttribute("isPersonSynToAD",
         * this.orgService.isPersonSynToAD(id)); return
         * forward("/system/opm/ad/PersonDetail.jsp", data); } catch (Exception
         * e) { return errorPage(e.getMessage()); }
         */
    }

    //@LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到查询组织页面")
    public String showSelectOrgDialog() {
        return this.forward(SELECT_ORG_DIALOG, this.getSDO().getProperties());
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到组织职能页面")
    public String forwardOrgFuncExample() {
        // 平台未使用本功能
        return this.forward("OrgFunExample", this.getSDO().getProperties());
    }

    @RequiresPermissions("Org:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询组织属性")
    public String queryOrgProperties() {
        SDO params = this.getSDO();
        String orgId = params.getString("orgId");
        Map<String, Object> map = this.application.queryOrgProperties(orgId);
        return toResult(map);
    }

    @RequiresPermissions("Org:initPassword")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.INIT, description = "初始化密码")
    public String initPassword() {
        SDO sdo = this.getSDO();
        String personId = sdo.getString("personId");
        this.application.initPassword(personId);
        return success("初始化密码成功。");
    }

    private void clearSessionSecurityPolicyAttributes(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute(SecurityPolicyApplication.FIRST_LOGIN_UPDATE_PASSWORD_ATTRIBUTE, false);
        session.setAttribute(SecurityPolicyApplication.PASSWORD_EXPIRED_ATTRIBUTE, false);
        session.setAttribute(SecurityPolicyApplication.UPDATE_PASSWORD_PROMPT_ATTRIBUTE, false);
    }
    
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "修改密码")
    public String updatePassword() {
        SDO sdo = this.getSDO();
        String oldPassword = sdo.getString("psw");
        String newPassword = sdo.getString("newPsw");

        this.application.updatePassword(oldPassword, newPassword);

        clearSessionSecurityPolicyAttributes();

        return success("密码修改成功。");
    }

    @RequiresPermissions("Org:quote")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "引用权限")
    public String quoteAuthorizationAndBizManagement() {
        SDO sdo = this.getSDO();
        String sourceOrgId = sdo.getProperty("sourceOrgId", String.class);
        String destOrgId = sdo.getProperty("destOrgId", String.class);
        if (StringUtil.isBlank(sourceOrgId) || StringUtil.isBlank(destOrgId)) {
            throw new ApplicationException("被引用或者引用组织为空。");
        }
        this.application.quoteAuthorizationAndBizManagement(sourceOrgId, destOrgId);
        return success("您已成功引用权限。");
    }

}
