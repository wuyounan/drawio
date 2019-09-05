package com.huigou.context;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.huigou.exception.ApplicationException;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

/**
 * 组织单元
 * 
 * @author gongmm
 */
public class OrgUnit implements Serializable {
    private static final long serialVersionUID = 6809247788310053987L;

    public static final String ORG_ID_FIELD_NAME = "orgId";

    public static final String ORG_CODE_FIELD_NAME = "orgCode";

    public static final String ORG_NAME_FIELD_NAME = "orgName";

    public static final String DEPT_ID_FIELD_NAME = "deptId";

    public static final String DEPT_CODE_FIELD_NAME = "deptCode";

    public static final String DEPT_NAME_FIELD_NAME = "deptName";

    public static final String POSITION_ID_FIELD_NAME = "posId";

    public static final String POSITION_CODE_FIELD_NAME = "posCode";

    public static final String POSITION_NAME_FIELD_NAME = "posName";

    public static final String PERSON_MEMBER_ID_FIELD_NAME = "psmId";

    public static final String PERSON_MEMBER_CODE_FIELD_NAME = "psmCode";

    public static final String PERSON_MEMBER_NAME_FIELD_NAME = "psmName";

    private String fullId;

    private String fullName;

    private boolean isBuildedAttribute;

    protected Map<String, Object> attributes = new HashMap<String, Object>();

    public OrgUnit() {

    }

    /**
     * 组织单元构造函数
     * 
     * @param fullId
     * @param fullName
     */
    public OrgUnit(String fullId, String fullName) {
        Util.check((Util.isNotEmptyString(fullId)) && (Util.isNotEmptyString(fullId)), "创建OrgUnit时fullId，fullName参数不能为空。");
        this.fullId = fullId;
        this.fullName = fullName;
    }

    /**
     * 判断当前组织单元是否是指定组织单元的父或相等
     * 
     * @param obj
     * @return
     */
    public boolean contains(OrgUnit obj) {
        if ((obj != null) && (obj.getFullId() != null)) {
            return obj.getFullId().startsWith(this.fullId);
        }
        return false;
    }

    public int hashCode() {
        return this.getFullId() == null ? 221 : this.getFullId().hashCode();
    }

    public boolean equals(Object obj) {
        if ((obj != null) && ((obj instanceof OrgUnit))) return this.fullId == null ? false
                                                                                   : ((OrgUnit) obj).getFullId() == null ? true
                                                                                                                        : this.fullId.equals(((OrgUnit) obj).getFullId());
        return false;
    }

    public String getFullId() {
        return this.fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAttributeValue(String key, Object value) {
        this.attributes.put(key, value);
    }

    public String getAttributeValue(String key) {
        if (!isBuildedAttribute) {
            Class<?> opmUtilClass;
            try {
                opmUtilClass = Class.forName("com.huigou.uasp.bmp.opm.OpmUtil");

                Method method;
                method = opmUtilClass.getMethod("buildOrgIdNameExtInfo", String.class, String.class, Map.class);

                method.invoke(null, fullId, fullName, attributes);

                String psmId = ClassHelper.convert(attributes.get("psmId"), String.class);
                if (!StringUtil.isBlank(psmId)) {
                    method = opmUtilClass.getMethod("getPersonIdFromPersonMemberId", String.class);
                    String personId = (String) method.invoke(null, psmId);
                    attributes.put("personId", personId);
                    attributes.put("personName", attributes.get("psmName"));
                }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                throw new ApplicationException(e);
            }
            isBuildedAttribute = true;
        }
        return ClassHelper.convert(this.attributes.get(key), String.class);
    }

    public String getOrgId() {
        return this.getAttributeValue(ORG_ID_FIELD_NAME);
    }

    public String getOrgCode() {
        return this.getAttributeValue(ORG_CODE_FIELD_NAME);
    }

    public String getOrgName() {
        return this.getAttributeValue(ORG_NAME_FIELD_NAME);
    }

    public String getDeptId() {
        return this.getAttributeValue(DEPT_ID_FIELD_NAME);
    }

    public String getDeptCode() {
        return this.getAttributeValue(DEPT_CODE_FIELD_NAME);
    }

    public String getDeptName() {
        return this.getAttributeValue(DEPT_NAME_FIELD_NAME);
    }

    public String getPositionId() {
        return this.getAttributeValue(POSITION_ID_FIELD_NAME);
    }

    public String getPositionCode() {
        return this.getAttributeValue(POSITION_CODE_FIELD_NAME);
    }

    public String getPositionName() {
        return this.getAttributeValue(POSITION_NAME_FIELD_NAME);
    }

    public String getPersonMemberId() {
        return this.getAttributeValue(PERSON_MEMBER_ID_FIELD_NAME);
    }

    public String getPersonMemberCode() {
        return this.getAttributeValue(PERSON_MEMBER_CODE_FIELD_NAME);
    }

    public String getPersonMemberName() {
        return this.getAttributeValue(PERSON_MEMBER_NAME_FIELD_NAME);
    }

    @Override
    public String toString() {
        return "(" + this.fullId + "," + this.fullName + ")@OrgUnit";
    }
}