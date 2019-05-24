package com.huigou.util;

public class Constants {
    public static String WEB_NAME = "统一应用支撑平台";

    public static String WEB_APP = "";

    public static final String NONE = "none";

    public static final String SDO = "sdo";

    public static final String ERROR_RESULT = "/common/errorPage.jsp";

    public static final String ERROR_RESULT_WITHOUT_SUFFIX = "/common/errorPage";

    public static final String AUTH_ERROR_RESULT_WITHOUT_SUFFIX = "/common/authErrorPage";

    public static final String UNAUTHORIZATION_RESULT = "unauthorizationPage";

    public static final String LOGON_OUT_PAGE = "/Logout";

    public static final String HOME_PAGE = "/HomePage";

    public static final String SECURITY_CODE = "securityCode";

    public static final String CSRF_TOKEN = "csrfToken";

    public static final String SESSION_OPERATOR_ATTRIBUTE = "sessionOperatorAttribute";

    public static final String LEAF_FLAG = "FunctionID";

    public static final String MANAGE_TYPE = "sys_Manage_Type";

    public static final String DATA_FILTER = "dataFilterGroup";

    public final static String PERMISSION_MANAGE_TYPE = "ManagementType";

    public final static String DATA_PERMISSION_MANAGE = "dataManagement";

    public final static String DATA_PERMISSION_MANAGE_FIELDS = "dataManageFields";

    public final static String NO_CONTROL_AUTHORITY = "noControlAuthority";

    public final static String PERMISSION_FIELD_BY_FUNCTION = "PermissionFieldByFunction";

    public final static String PERMISSION_FIELD_BY_PROCUNITHANDLERID = "PermissionFieldByProcUnitHandlerId";

    public final static String IS_REQUEST_EXCEPTION = "isRequestException";

    /**
     * 用于标识用户身份的Cookie名称
     */
    public static final String USER_COOKIE_KEY = "user_cookie_key";

    /******* CAS认证返回ticket属性名 ********/
    public static final String CAS_TICKET_PARAMETER = "ticket";

    /******* CAS验证错误信息 ********/
    public static final String CAS_VALIDATOR_ERROR = "_casValidatorError_";

    public static final String CAS_SERVICE_URL = "_casServiceUrl_";

    /**
     * 用于URL加密解密KEY
     */
    public static final String ENCRYPT_KEY = "encrypturlparakeyxx";

    public static final String TOKEN = "token";

    public static final String SERVICE_TICKET = "_serviceTicket_";

    /*********** 分页查询相关 ***************/
    /**
     * 记录总数字段名
     */
    public static final String RECORD = "Total";

    /**
     * 当前页码字段名
     */
    public static final String PAGE_PARAM_NAME = "page";

    /**
     * 页大小字段名
     */
    public static final String PAGE_SIZE_PARAM_NAME = "pagesize";

    /**
     * 排序字段字段名
     */
    public static final String SORT_NAME_PARAM_NAME = "sortname";

    /**
     * 排序方向字段名
     */
    public static final String SORT_ORDER_PARAM_NAME = "sortorder";

    /**
     * 页排序字段列表
     */
    public static final String SORT_FIELDS_PARM_NAME = "sortfields";

    /**
     * 导出标识
     */
    public static final String EXPORT_TYPE = "exportType";

    /**
     * 导出表头
     */
    public static final String EXPORT_HEAD = "exportHead";

    /**
     * 统计字段名列表
     */
    public static final String TOTAL_FIELDS = "totalFields";

    /**
     * 返回数据字段名
     */
    public static final String ROWS = "Rows";

    /**
     * 导出文件类型标识
     */
    public static final String EXPORT_EXCEL_TYPE = "exportExcelType";

    /**
     * 显示转化数据字典
     */
    public static final String DICTIONARY_MAP = "queryModelDictionaryMap";

    public static final String DATA = "data";

    public static final String FLEX_FIELD = "flexField_";

    public static final String PERSON_PICTURE = "PersonPicture";

    public static final String PARENTID = "parentId";

    public static final String ID = "id";

    public static final String BIZID = "bizId";

    public static final String TASKID = "taskId";

    public static final String PROCTASKID = "procTaskId";

    public static final String MAKEACOPYFORPREFIX = "makeACopyForPrefix";

    public static final String AUTHENTICATION_EXCEPTION_KEY = "_authenticationException_";
}
