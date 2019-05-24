package com.huigou.uasp.log.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.util.Assert;

import com.huigou.context.RoleKind;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.excel.exporter.ExportExcel;
import com.huigou.data.excel.exporter.LogExport;
import com.huigou.data.exception.ExportExcelException;
import com.huigou.uasp.bmp.opm.domain.model.access.TMAuthorize;
import com.huigou.uasp.bmp.opm.repository.org.TMAuthorizeRepository;
import com.huigou.uasp.log.domain.model.BizLog;
import com.huigou.uasp.log.domain.model.BizLogDetail;
import com.huigou.uasp.log.domain.model.LogStatus;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;

public class BizLogUtil {

    private final static String BIZ_LOG_KEY = "_bizLogs_";

    private final static String BIZ_LOG_EXCEPTION_KEY = "_bizLogException_";

    public static Map<String, String> getLogQueryCriteria(String roleKindId, TMAuthorizeRepository tmAuthorizeRepository, String personMemberId,
                                                          boolean isEnableTspm) {
        // String targetLogType = "";
        String targetRoleKindId = "";
        String targetStatusId = "";
        String targetAppId = "";
        String targetFullId = "";

        Map<String, String> data = new HashMap<String, String>();
        if (isEnableTspm) {
            List<TMAuthorize> tmAuthorizes = tmAuthorizeRepository.findByManagerIdAndRoleKindId(personMemberId, roleKindId);
            Assert.notEmpty(tmAuthorizes, "结果集tmAuthorizes不能为空");
            for (TMAuthorize tmAuthorize : tmAuthorizes) {
                if (targetFullId.indexOf(tmAuthorize.getSubordinationFullId()) == -1) {
                    targetFullId += tmAuthorize.getSubordinationFullId() + ",";
                }
                if (targetAppId.indexOf(tmAuthorize.getSystemId()) == -1) {
                    targetAppId += tmAuthorize.getSystemId() + ",";
                }
            }
            if (StringUtil.isNotBlank(targetAppId)) {
                targetAppId = targetAppId.substring(0, targetAppId.length() - 1);
            }
            if (StringUtil.isNotBlank(targetFullId)) {
                targetFullId = targetFullId.substring(0, targetFullId.length() - 1);
            }
            if (roleKindId.equals(RoleKind.ADMINISTRATOR.getId())) {
                targetStatusId = String.valueOf(LogStatus.FAILURE.getId());
                targetRoleKindId = StringPool.AT;
            } else if (roleKindId.equals(RoleKind.SECURITY_GUARD.getId())) {
                // targetRoleKindId = RoleKind.ADMINISTRATOR.getId();
                targetRoleKindId = RoleKind.COMMON.getId();
            } else if (roleKindId.equals(RoleKind.AUDITOR.getId())) {
                // targetRoleKindId = String.format("%s,%s,%s", RoleKind.ADMINISTRATOR.getId(), RoleKind.SECURITY_GUARD.getId(), RoleKind.COMMON.getId());
                targetRoleKindId = String.format("%s,%s,%s", RoleKind.ADMINISTRATOR.getId(), RoleKind.SECURITY_GUARD.getId(), RoleKind.AUDITOR.getId());
            }
        }

        // 调整规则：审计员查看三员日志，安全员查看除三员以外的普通用户的日志

        data.put("targetRoleKindId", targetRoleKindId);
        data.put("targetStatusId", targetStatusId);
        // data.put("targetLogType", targetLogType);
        data.put("targetAppId", targetAppId);
        data.put("targetFullId", targetFullId);

        return data;
    }

    public static String convert(Date date) {
        Assert.notNull(date, "参数date不能为空。");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }

    public static String exportExecute(List<Map<String, Object>> list, String exportHead) {
        Assert.notEmpty(list, "结果集list不能为空");
        Assert.notNull(exportHead, "参数exportHead不能为空");
        LogExport exporter = new LogExport();
        Map<String, Object> result = new HashMap<String, Object>(1);
        String fileName = "";
        try {
            Element headRoot = ExportExcel.readXml(exportHead, "");
            exporter.setHeadRoot(headRoot);
            exporter.setDatas(result);
            String path = exporter.expExcel(list);
            File file = new File(path);
            if (file.exists()) {
                fileName = file.getName();
            } else {
                throw new ExportExcelException("文件生成失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static void putBizLog(BizLog bizLog) {
        ThreadLocalUtil.putVariable(BIZ_LOG_KEY, bizLog);
    }

    public static BizLog getBizLog() {
        Object bizLog = ThreadLocalUtil.getVariable(BIZ_LOG_KEY);
        if (bizLog != null) {
            return (BizLog) bizLog;
        }
        return null;
    }

    /**
     * 设置业务日志描述
     * <p>
     * 设置简要描述和详细描述
     * 
     * @param description
     *            描述
     */
    public static void setBizLogDescription(String description) {
        BizLog bizLog = BizLogUtil.getBizLog();
        if (bizLog != null) {
            BizLogDetail bizLogDetail = bizLog.getBizLogDetail();
            bizLogDetail.setDescription(description);
            bizLog.setDescription(bizLogDetail.getBriefDescription());
        }
    }

    public static BizLog getNotNullBizLog() {
        Object bizLog = ThreadLocalUtil.getVariable(BIZ_LOG_KEY);
        Assert.state(bizLog != null, "环境变量中没有业务日志信息。");
        return (BizLog) bizLog;
    }

    public static void putBizLogException(Object exception) {
        ThreadLocalUtil.putVariable(BIZ_LOG_EXCEPTION_KEY, exception);
    }

    public static Object getBizLogException() {
        return ThreadLocalUtil.getVariable(BIZ_LOG_EXCEPTION_KEY);
    }

}
