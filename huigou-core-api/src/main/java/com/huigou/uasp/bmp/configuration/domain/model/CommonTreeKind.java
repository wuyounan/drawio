package com.huigou.uasp.bmp.configuration.domain.model;

/**
 * 通用树类别
 * 
 * @author gongmm
 */
public class CommonTreeKind {
	/* 机构 */
	public static final int ORG_KIND = 1;

	/* 部门 */
	public static final int DPT_KIND = 2;

	/* 岗位 */
	public static final int POS_KIND = 3;

	/* 角色 */
	public static final int ROLE = 4;

	/* 扩展属性定义 */
	public static final int FLEXFIELDDEFINE = 5;

	/* 扩展属性 */
	public static final int FLEXFIELDGROUP = 6;

	/* 系统参数 */
	public static final int PARAMETER = 7;

	/* 单据编号 */
	public static final int SERIALNUMBER = 8;

	/**
	 * 系统字典
	 */
	public static final int DICTIONARY = 9;

	/**
	 * 导入模板
	 */
	public static final int EXP_TEMPLET = 10;

	/* 基础管理权限分类 */
	public static final int BASEMANAGEMENTTYPE = 17;

	/* 业务管理权限分类 */
	public static final int BIZMANAGEMENTTYPE = 18;

	/* 附件配置分类 */
	public static final int ATTACHMENTCONFIG = 21;

	/**
	 * 系统分组
	 */
	public static final int USER_GROUP = 25;

	/**
	 * 自定义分组
	 */
	public static final int CUSTOM_GROUP = 26;

	/**
	 * 根据树类别ID获取业务表名及关联字段名
	 * 
	 * @param kindId
	 * @return
	 */
	public static String[] getBusinessTableInfo(Integer kindId) {
		switch (kindId) {
		case ORG_KIND:
		case DPT_KIND:
		case POS_KIND:
			return new String[] { "OrgType", "folderId" };
		case ROLE:
			return new String[] { "Role", "folderId" };
		case FLEXFIELDDEFINE:
			return new String[] { "FlexFieldDefinition", "folderId" };
		case FLEXFIELDGROUP:
			return new String[] { "FlexFieldBizGroup", "folderId" };
		case PARAMETER:
			return new String[] { "SysParameter", "folderId" };
		case SERIALNUMBER:
			return new String[] { "CodeBuildRule", "folderId" };
		case DICTIONARY:
			return new String[] { "SysDictionary", "folderId" };
		case EXP_TEMPLET:
			return new String[] { "ExcelImportTemplate", "folderId" };
		case BASEMANAGEMENTTYPE:
			return new String[] { "BaseManagementType", "folderId" };
		case ATTACHMENTCONFIG:
			return new String[] { "AttachmentConfiguration", "folderId" };
		case USER_GROUP:
			return new String[] { "UserGroup", "folderId" };
		}
		return null;
	}
}
