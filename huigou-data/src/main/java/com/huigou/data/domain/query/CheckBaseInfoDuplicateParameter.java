package com.huigou.data.domain.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;

/**
 * 检查重复参数
 * 
 * @author gongmm
 */
public class CheckBaseInfoDuplicateParameter {

    /**
     * 检查字段类别
     */
    private CheckFieldKind checkFieldKind;

    /**
     * 租户ID
     */
    private Serializable tenantId;

    /**
     * 租户 字段名称
     */
    private String tenantFieldName;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * ID
     */
    private String id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    public CheckBaseInfoDuplicateParameter() {

    }

    public Serializable getTenantId() {
        return this.tenantId;
    }

    public String getTenantFieldName() {
        return this.tenantFieldName;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public CheckFieldKind getCheckFieldKind() {
        return checkFieldKind;
    }

    public void setCheckTenantInfo(String tenantFieldName, Serializable tenantId) {
        this.tenantFieldName = tenantFieldName;
        this.tenantId = tenantId;
    }

    public void setCheckCode(String id, String code) {
        this.checkFieldKind = CheckFieldKind.CODE;
        this.id = id;
        this.code = code;
    }

    public void setCheckName(String id, String name) {
        this.checkFieldKind = CheckFieldKind.NAME;
        this.id = id;
        this.name = name;
    }

    public void setCheckCodeAndName(String id, String code, String name) {
        this.checkFieldKind = CheckFieldKind.CODE_AND_NAME;
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public void setCheckParentIdAndName(String parentId, String id, String name) {
        this.checkFieldKind = CheckFieldKind.PARENT_ID_AND_NAME;
        this.parentId = parentId;
        this.id = id;
        this.name = name;
    }

    public void setCheckParentIdAndCodeAndName(String parentId, String id, String code, String name) {
        this.checkFieldKind = CheckFieldKind.PARENT_ID_AND_CODE_AND_NAME;
        this.parentId = parentId;
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public void setCheckParentIdAndGlobalCodeAndName(String parentId, String id, String code, String name) {
        this.checkFieldKind = CheckFieldKind.PARENT_ID_AND_GLOBALCODE_AND_NAME;
        this.parentId = parentId;
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public void setCheckFolderIdAndName(String folderId, String id, String name) {
        this.checkFieldKind = CheckFieldKind.FOLDER_ID_AND_NAME;
        this.parentId = folderId;
        this.id = id;
        this.name = name;
    }

    public void setCheckFolderIdAndCodeAndName(String folderId, String id, String code, String name) {
        this.checkFieldKind = CheckFieldKind.FOLDER_ID_AND_CODE_AND_NAME;
        this.parentId = folderId;
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public void setCheckFolderIdAndGlobalCodeAndName(String folderId, String id, String code, String name) {
        this.checkFieldKind = CheckFieldKind.FOLDER_ID_AND_GLOBALCODE_AND_NAME;
        this.parentId = folderId;
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public boolean isTenantFilter() {
        return (this.tenantId != null);
    }

    public void checkConstraints() {
        if (isTenantFilter()) {
            Assert.hasText(this.tenantFieldName, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "tenantFieldName"));
            Assert.notNull(this.tenantId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "tenantId"));
        }

        switch (this.checkFieldKind) {
        case CODE:
            Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
            break;
        case NAME:
            Assert.hasText(name, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
            break;
        case CODE_AND_NAME:
            Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
            Assert.hasText(name, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
            break;
        case PARENT_ID_AND_NAME:
            Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
            Assert.hasText(name, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
            break;
        case PARENT_ID_AND_CODE_AND_NAME:
        case PARENT_ID_AND_GLOBALCODE_AND_NAME:
            Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
            Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
            Assert.hasText(name, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
            break;
        case FOLDER_ID_AND_NAME:
            Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.FOLDER_ID_NOT_BLANK));
            Assert.hasText(name, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
            break;
        case FOLDER_ID_AND_CODE_AND_NAME:
        case FOLDER_ID_AND_GLOBALCODE_AND_NAME:
            Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.FOLDER_ID_NOT_BLANK));
            Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
            Assert.hasText(name, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
            break;
        }
    }

    public Map<String, Object> getQueryParams() {
        Map<String, Object> result = new HashMap<String, Object>();
        if (this.isTenantFilter()) {
            result.put("tenantId", tenantId);
        }
        result.put(MessageConstants.ID_FIELD_NAME, StringUtil.isBlank(id) ? StringPool.AT : id);
        switch (this.checkFieldKind) {
        case CODE:
            result.put(MessageConstants.CODE_FIELD_NAME, code.toUpperCase());
            break;
        case NAME:
            result.put(MessageConstants.NAME_FIELD_NAME, name.toUpperCase());
            break;
        case CODE_AND_NAME:
            result.put(MessageConstants.CODE_FIELD_NAME, code.toUpperCase());
            result.put(MessageConstants.NAME_FIELD_NAME, name.toUpperCase());
            break;
        case PARENT_ID_AND_NAME:
            result.put(MessageConstants.PARENT_ID_FIELD_NAME, parentId);
            result.put(MessageConstants.NAME_FIELD_NAME, name.toUpperCase());
            break;
        case PARENT_ID_AND_CODE_AND_NAME:
        case PARENT_ID_AND_GLOBALCODE_AND_NAME:
            result.put(MessageConstants.PARENT_ID_FIELD_NAME, parentId);
            result.put(MessageConstants.CODE_FIELD_NAME, code.toUpperCase());
            result.put(MessageConstants.NAME_FIELD_NAME, name.toUpperCase());
            break;
        case FOLDER_ID_AND_NAME:
            result.put(MessageConstants.FOLDER_ID_FIELD_NAME, parentId);
            result.put(MessageConstants.NAME_FIELD_NAME, name.toUpperCase());
            break;
        case FOLDER_ID_AND_CODE_AND_NAME:
        case FOLDER_ID_AND_GLOBALCODE_AND_NAME:
            result.put(MessageConstants.FOLDER_ID_FIELD_NAME, parentId);
            result.put(MessageConstants.CODE_FIELD_NAME, code.toUpperCase());
            result.put(MessageConstants.NAME_FIELD_NAME, name.toUpperCase());
            break;
        }
        return result;
    }

    public String getSqlName() {
        String result;
        switch (this.checkFieldKind) {
        case CODE:
            result = "findDuplicateEntitiesByCode";
            break;
        case NAME:
            result = "findDuplicateEntitiesByName";
            break;
        case CODE_AND_NAME:
            result = "findDuplicateEntitiesByCodeAndName";
            break;
        case PARENT_ID_AND_NAME:
            result = "findDuplicateEntitiesByParentIdAndName";
            break;
        case PARENT_ID_AND_CODE_AND_NAME:
            result = "findDuplicateEntitiesByParentIdAndCodeAndName";
            break;
        case PARENT_ID_AND_GLOBALCODE_AND_NAME:
            result = "findDuplicateEntitiesByParentIdAndGlobalCodeAndName";
            break;
        case FOLDER_ID_AND_NAME:
            result = "findDuplicateEntitiesByFolderIdAndName";
            break;
        case FOLDER_ID_AND_CODE_AND_NAME:
            result = "findDuplicateEntitiesByFolderIdAndCodeAndName";
            break;
        case FOLDER_ID_AND_GLOBALCODE_AND_NAME:
            result = "findDuplicateEntitiesByFolderIdAndGlobalCodeAndName";
            break;
        default:
            throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.INVALID_FIELD_TYPE));
        }

        return result;
    }

    public enum CheckFieldKind {
        CODE,
        NAME,
        CODE_AND_NAME,
        PARENT_ID_AND_NAME,
        PARENT_ID_AND_CODE_AND_NAME,
        PARENT_ID_AND_GLOBALCODE_AND_NAME,
        FOLDER_ID_AND_NAME,
        FOLDER_ID_AND_CODE_AND_NAME,
        FOLDER_ID_AND_GLOBALCODE_AND_NAME
    }

}
