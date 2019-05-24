package com.huigou.uasp.bpm;

/**
 * 流程环节类别
 * 
 * @author gongmm
 */
public class ActivityKind {

    public static final String APPLY = "apply";

    public static final String APPROVE = "approve";

    public static final String START_EVENT = "startEvent";
    
    public static final String END_EVENT = "endEvent";

    public static final String USER_TASK = "userTask";

    public static final String EXCLUSIVE_GATEWAY = "exclusiveGateway";

    /**
     * 是否申请环节
     * 
     * @return
     */
    public static boolean isApplyActivity(String activityId) {
        return ActivityKind.APPLY.equalsIgnoreCase(activityId);
    }

    public static boolean isApproveActivity(String activityId) {
        return activityId.toLowerCase().contains(ActivityKind.APPROVE);
    }
}
