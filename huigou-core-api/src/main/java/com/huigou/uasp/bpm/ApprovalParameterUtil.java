package com.huigou.uasp.bpm;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 审批参数
 * 
 * @author gongmm
 */
public class ApprovalParameterUtil {
    
    private final static String APPROVAL_PARAMETER_KEY = "_approvalParameter_";

    public static ApprovalParameter getApprovalParameter() {
        ApprovalParameter approvalParameter =  ThreadLocalUtil.getVariable(APPROVAL_PARAMETER_KEY, ApprovalParameter.class);
        if (approvalParameter == null) {
            SDO params = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
            approvalParameter = ApprovalParameter.newInstance(params);
            ThreadLocalUtil.putVariable(APPROVAL_PARAMETER_KEY, approvalParameter);
        }
        return approvalParameter;
    }

}
