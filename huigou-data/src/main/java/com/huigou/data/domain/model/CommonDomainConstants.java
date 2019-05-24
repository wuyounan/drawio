package com.huigou.data.domain.model;

/**
 * 通用领域常量
 * 
 * @author gongmm
 */
public interface CommonDomainConstants {
    String ID_NOT_BLANK = "参数id不能为空。";

    String IDS_NOT_BLANK = "参数ids不能为空。";

    String FOLDER_ID_NOT_BLANK = "文件夹id不能为空。";

    String FILE_NAME_NOT_BLANK = "文件名不能为空。";

    String CODE_NOT_BLANK = "编码不能为空。";

    String NAME_NOT_BLANK = "名称不能为空。";

    String BIZ_CODE_NOT_BLANK = "业务编码不能为空。";

    String STATUS_NOT_BLANK = "参数status不能为空。";

    String CLAZZ_NOT_NULL = "参数clazz不能为空。";

    String OBJECT_NOT_NULL = "对象不能为空。";

    String PARAMETER_NOT_NULL_FORMAT = "参数“%s”不能为空。";

    String ENTITY_NOT_NULL = "参数entity不能为空。";

    String REPOSITORY_NOT_NULL = "参数repository不能为空。";

    String PARENT_ID_NOT_BLANK = "参数parentId不能为空。";

    String PARENT_ID_FIELD_NAME_NOT_BLANK = "父ID字段名称不能为空。";

    String TABLE_NAME_NOT_BLANK = "表名不能为空。";

    String NODE_KIND_ID_NOT_BLANK = "节点ID不能为空。";

    String CODE_NOT_DUPLICATE = "编码不能重复。";

    String NAME_NOT_DUPLICATE = "名称不能重复。";

    String OBJECT_NOT_FOUND_BY_ID = "未找到ID“%s”对应的%s数据。";

    String OBJECT_NOT_FOUND_BY_CODE = "未找到编码“%s”对应的%s数据。";

    String OBJECT_NOT_FOUND_BY_BIZ_ID = "未找到业务ID“%s”对应的%s数据。";

    String OBJECT_NOT_FOUND_BY_TASK_ID = "未找到业务ID“%s”对应的%s数据。";

    String CAN_NOT_DELETE_HAS_CHILDREN = "节点“%s”存在子节点，不能删除。";

    String NOT_DEFINITION_COMMON_TREE = "不能识别树结构类型。";

    String CAN_NOT_DELETE_REFERENCED_BY_BIZ = "存在业务数据不能删除。";

    String IDS_EXIST_INVALID_ID = "“%s”ID列表中存在无效的ID。";

    String OBJECT_REFERENCED_BY_WHO = "“%s”已被“%s”引用，不能删除。";

    String OBJECT_REFERENCED = "“%s”已被使用，不能删除。";

    String ID_FIELD_NAME = "id";

    String IDS_FIELD_NAME = "ids";

    String CODE_FIELD_NAME = "code";

    String NAME_FIELD_NAME = "name";

    String STATUS_FIELD_NAME = "status";

    String PARENT_ID_FIELD_NAME = "parentId";

    String FOLDER_ID_FIELD_NAME = "folderId";

    String PARENT_ID_COLUMN_NAME = "parent_id";

    String SEQUENCE_FIELD_NAME = "sequence";

    String DEFAULT_ROOT_PARENT_ID = "0";

    String LOAD_OBJECT_IS_NULL = "查询的数据不存在，可能被其他用户删除或修改。";
    
    String OBJECT_NOT_FOUND_BY_CODE_KEY = "object.not.found.by.code";
}
