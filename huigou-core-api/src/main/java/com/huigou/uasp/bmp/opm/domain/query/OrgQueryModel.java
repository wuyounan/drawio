package com.huigou.uasp.bmp.opm.domain.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryModel;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.opm.SelectOrgScope;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 组织机构查询模型
 * 
 * @author gongmm
 */
public class OrgQueryModel extends QueryModel {

    private final static String SELECT_ORG_SCOPE_KEY = "selectOrgScope";

    private final static String SELECT_ORG_SCOPE_PARAMETER_KEY = "selectOrg.scope";

    private final static String CUSTOM_DEFINED_ROOT_KEY = "customDefinedRoot";

    private final static String ROOT_IDS_KEY = "rootIds";

    private StringBuilder condition = new StringBuilder();

    private StringBuilder countCondition = new StringBuilder();

    protected SDO inputParams;

    private String countSql;

    public OrgQueryModel(SDO sdo) {
        this.initPageInfo(sdo.getProperties());
        String manageType = sdo.getString(Constants.MANAGE_TYPE);
        this.setManageType(manageType);
        this.inputParams = sdo;
    }

    public String getParentId() {
        return inputParams.getString(CommonDomainConstants.PARENT_ID_FIELD_NAME);
    }

    public void setParentId(String parentId) {
        inputParams.putProperty(CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
    }

    private String getParamValue() {
        return inputParams.getString("paramValue");
    }

    private boolean getShowDisabledOrg() {
        return "1".equals(this.inputParams.getProperty("showDisabledOrg", String.class, "0"));
    }

    private boolean getShowVirtualOrg() {
        return "1".equals(this.inputParams.getProperty("showVirtualOrg", String.class, "0"));
    }

    private boolean getShowProjectOrg() {
        return "1".equals(this.inputParams.getProperty("showProjectOrg", String.class, "1"));
    }

    private boolean getShowPosition() {
        return "1".equals(this.inputParams.getProperty("showPosition", String.class, "1"));
    }

    private boolean getShowMasterPsm() {
        return "1".equals(this.inputParams.getProperty("showMasterPsm", String.class, "0"));
    }

    private boolean getShowAllChildrenOrg() {
        return "1".equals(this.inputParams.getProperty("showAllChildrenOrg", String.class, "0"));
    }

    private boolean isCustomDefinedRoot() {
        return "1".equals(this.inputParams.getProperty(CUSTOM_DEFINED_ROOT_KEY, String.class, "0"));
    }

    private void setCustomDefinedRoot(String customDefinedRoot) {
        this.inputParams.putProperty(CUSTOM_DEFINED_ROOT_KEY, customDefinedRoot);
    }

    private String getRootIds() {
        return this.inputParams.getString(ROOT_IDS_KEY);
    }

    private void setRootIds(String rootIds) {
        this.inputParams.putProperty(ROOT_IDS_KEY, rootIds);
    }

    private String getDisplayableOrgKinds() {
        return this.inputParams.getString("displayableOrgKinds");
    }

    private String getFilter() {
        return this.inputParams.getString("filter");
    }

    private String getManageCodes() {
        return this.inputParams.getString("manageCodes");
    }

    private boolean hasFilter() {
        return !StringUtil.isBlank(this.getFilter());
    }

    private boolean hasManageCodes() {
        return !StringUtil.isBlank(this.getManageCodes());
    }

    public boolean isQueryVirtualRoot() {
        String parentId = this.getParentId();
        return StringUtil.isBlank(parentId);
    }

    public boolean isQueryRootChildren() {
        String parentId = this.getParentId();
        return Org.ORG_ROOT_ID.equals(parentId);
    }

    public SelectOrgScope getSelectOrgScope() {
        String id = this.inputParams.getString(SELECT_ORG_SCOPE_KEY);
        if (StringUtil.isBlank(id)) {
            id = SystemCache.getParameter(SELECT_ORG_SCOPE_PARAMETER_KEY, String.class);
        }
        return SelectOrgScope.fromId(id);
    }

    public void parse() {
        int first = 0, last = 0;
        String paramName;

        if (getSelectOrgScope().equals(SelectOrgScope.TENANT)) {
            // 查询 租户根组织
            boolean hasRootOrgPermission = ThreadLocalUtil.getOperator().hasRootOrgPermission();
            if (!hasRootOrgPermission && !isCustomDefinedRoot() && (isQueryVirtualRoot() || isQueryRootChildren())) {
                setCustomDefinedRoot("1");
                setRootIds(ThreadLocalUtil.getOperator().getRootOrgId());
            }

            condition.append(" and o.full_Id like :tenantFullId");
            this.putStartWithParam("tenantFullId", ThreadLocalUtil.getOperator().getRootOrgFullId());
        }

        if (getShowAllChildrenOrg()) {
            condition.append(" and o.full_Id like :fullId");
            String fullId = this.inputParams.getString("fullId");
            this.putStartWithParam("fullId", fullId);
        } else {
            if (isCustomDefinedRoot()) {
                String rootIds = this.getRootIds();
                Assert.hasText(rootIds, "参数rootIds不能为空。");
                String[] orgIds = rootIds.split(",");
                first = condition.length();
                condition.append(" and o.id in (");
                for (int i = 0; i < orgIds.length; i++) {
                    paramName = String.format(":inOrgId%d", i);
                    condition.append(paramName);
                    if (i < orgIds.length - 1) {
                        condition.append(",");
                    }
                    this.putParam(String.format("inOrgId%d", i), orgIds[i]);
                }
                condition.append(")");
                last = condition.length();
                this.putParam("id", this.getParentId());
            } else {
                if (!this.isTreeQuery()) {
                    if (StringUtil.isNotBlank(this.getParentId())) {
                        condition.append(" and o.parent_Id = :parentId");
                        this.putParam("parentId", this.getParentId());
                    }
                } else {
                    condition.append(" and o.parent_Id = :parentId");
                    this.putParam("parentId", this.getParentId());
                }
            }
        }

        if (!getShowVirtualOrg()) {
            condition.append(" and nvl(o.is_Virtual, 0) = :isVirtual");
            this.putParam("isVirtual", 0);
        }

        if (!getShowProjectOrg()) {
            condition.append(" and instr(o.full_Org_Kind_Id, 'prj') = :isProjectOrg ");
            this.putParam("isProjectOrg", 0);
        }

        String displayableOrgKinds = getDisplayableOrgKinds();
        if (!StringUtil.isBlank(displayableOrgKinds)) {
            List<String> orgKindIds = Arrays.asList(displayableOrgKinds.split(","));
            condition.append(" and o.org_Kind_Id in(");
            for (int i = 0; i < orgKindIds.size(); i++) {
                paramName = String.format(":orgKindId%d", i);
                condition.append(paramName);
                if (i < orgKindIds.size() - 1) {
                    condition.append(",");
                }
                this.putParam(String.format("orgKindId%d", i), orgKindIds.get(i));
            }
            condition.append(")");
        }

        if (!StringUtil.isBlank(getParamValue())) {
            condition.append(" and (");
            String[] values = getParamValue().split(" ");
            int i = 0;
            for (String v : values) {
                if (!StringUtil.isBlank(v)) {
                    condition.append(" (upper(o.code) like upper(:paramValue").append(i).append(")");
                    condition.append(" or o.name like :paramValue").append(i).append(") ");
                    condition.append("or");
                    this.putLikeParam("paramValue" + (i++), v.toUpperCase());
                }
            }
            if (condition.lastIndexOf("or") == (condition.length() - 2)) {
                condition.replace(condition.length() - 2, condition.length(), "");
            }
            condition.append(")");
        }

        if (hasFilter()) {
            condition.append(" and " + getFilter());
        }

        if (hasManageCodes()) {
            this.setManageType(this.getManageCodes());
        }

        if (getShowMasterPsm() && getShowPosition()) {
            condition.append(" and (o.org_Kind_Id != 'psm' or o.parent_id = p.main_Org_Id )");
        }

        // 状态条件
        List<Integer> statuses = new ArrayList<Integer>();
        Integer status = this.inputParams.getInteger(CommonDomainConstants.STATUS_FIELD_NAME);
        if (status != null) {
            statuses.add(status);
        } else {
            statuses.add(ValidStatus.ENABLED.getId());
            if (getShowDisabledOrg()) {
                statuses.add(ValidStatus.DISABLED.getId());
            }
        }

        String code = this.inputParams.getString(CommonDomainConstants.CODE_FIELD_NAME);
        String name = this.inputParams.getString(CommonDomainConstants.NAME_FIELD_NAME);

        if (!StringUtil.isBlank(code)) {
            condition.append(" and o.code like :code");
            this.putLikeParam("code", code);
        }
        if (!StringUtil.isBlank(name)) {
            condition.append(" and o.name like :name");
            this.putLikeParam("name", name);
        }

        condition.append(" and o.status in (");
        for (int i = 0; i < statuses.size(); i++) {
            paramName = String.format(":status%d", i);
            condition.append(paramName);
            if (i < statuses.size() - 1) {
                condition.append(",");
            }
            this.putParam(String.format("status%d", i), statuses.get(i));
        }
        condition.append(")");

        this.countCondition.append(condition);
        if (first != last) {
            countCondition.delete(first, last);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.getSql());
        sb.append(condition);
        this.setSql(sb.toString());
    }

    public String getSqlName() {
        if (this.getShowPosition()) {
            return "orgQuery";
        } else {
            return "queryOrgExcludePos";
        }
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public String getCountSql() {
        StringBuilder sb = new StringBuilder(this.countSql);
        sb.append(countCondition);
        return sb.toString();
    }

}
