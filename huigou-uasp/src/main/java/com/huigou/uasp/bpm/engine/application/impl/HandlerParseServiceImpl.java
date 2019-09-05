package com.huigou.uasp.bpm.engine.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.express.ExpressManager;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bpm.configuration.domain.model.HandlerKind;
import com.huigou.uasp.bpm.engine.application.HandlerParseService;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.LogHome;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

@Service("handlerParseService")
public class HandlerParseServiceImpl implements HandlerParseService {

    @Autowired
    private OrgApplicationProxy orgApplication;

    @SuppressWarnings("unchecked")
    @Override
    public void buildHandler(HandlerKind handlerKind, String handlerId, List<OrgUnit> orgUnits) {
        List<OrgUnit> calcOrgUnits;
        String express;
        Org org;

        switch (handlerKind) {
        case MANAGE_AUTHORITY:
        case MANUAL_SELECTION:
            break;
        case PSM:
            org = orgApplication.loadEabledOrg(handlerId);

            OrgUnit orgUnit = new OrgUnit(org.getFullId(), org.getFullName());
            orgUnits.add(orgUnit);
            break;
        case POS:
        case DEPT:
            try {
                org = orgApplication.loadEabledOrg(handlerId);

                express = String.format("findPersonMembersInOrg('%s',true)", org.getFullId());
                calcOrgUnits = (List<OrgUnit>) ExpressManager.evaluate(express);
                orgUnits.addAll(calcOrgUnits);
            } catch (Exception e) {
                throw new ApplicationException(e.getMessage());
            }
            break;
        case MANAGER_FUN:
        case SCOPE_SELECTION:
            try {
                express = handlerId;
                // 替换参数
                SDO bizData = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
                if (bizData != null) {
                    String param, value;
                    Map<String, Object> bizParams = bizData.getProperty("bizParams", Map.class);
                    // findManagers(@orgId, @manageType) findManagers('aaaa', @manageType)
                    String regex = "@(.+?)[,|)]";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(express);
                    while (m.find()) {
                        value = "";
                        param = m.group(1).trim();
                        // （业务、系统）参数优先，单据数据次之
                        if (bizParams != null && bizParams.get(param) != null) {
                            value = ClassHelper.convert(bizParams.get(param), String.class);
                        }
                        if (StringUtil.isBlank(value)) {
                            value = bizData.getProperty(param, String.class, "");
                        }
                        Util.check(!StringUtil.isBlank(value), String.format("没有找到参数“%s”对应的值。", param));
                        express = express.replace("@" + m.group(1), "'" + value + "'");
                    }
                }

                calcOrgUnits = (List<OrgUnit>) ExpressManager.evaluate(express);
                List<String> orgFullIds = new ArrayList<String>(calcOrgUnits.size());

                for (OrgUnit unit : calcOrgUnits) {
                    orgFullIds.add(unit.getFullId());
                }

                express = "findPersonMembersInOrg(org, true)";
                Map<String, Object> variables = new HashMap<String, Object>(1);
                variables.put("org", orgFullIds);
                calcOrgUnits = (List<OrgUnit>) ExpressManager.evaluate(express, variables);
                orgUnits.addAll(calcOrgUnits);
            } catch (Exception e) {
                LogHome.getLog(this.getClass()).error(e);
                throw new ApplicationException(e);
            }
            break;
        default:
            throw new ApplicationException("无效的处理人类型。");
        }
    }

    @Override
    public void buildSegmentationHandler(HandlerKind handlerKind, Long bizSegementationId, String handlerId, List<OrgUnit> orgUnits) {
        ThreadLocalUtil.putVariable(CURRENT_BIZ_SEGEMENTATION_ID, bizSegementationId);
        try {
            buildHandler(handlerKind, handlerId, orgUnits);
        } finally {
            ThreadLocalUtil.removeVariable(CURRENT_BIZ_SEGEMENTATION_ID);
        }
    }

}
