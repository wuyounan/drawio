package com.huigou.uasp.bpm;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;

/**
 * 流程启动模式
 * 
 * @author gongmm
 */
public enum ProcessStartModel {
    MANUAL("manual", "手动"), AUTOMATIC("automatic", "自动");

    private final String id;

    private final String displayName;

    private ProcessStartModel(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static ProcessStartModel fromId(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "id"));
        // 2、转换
        for (ProcessStartModel item : ProcessStartModel.values()) {
            if (item.id.equalsIgnoreCase(id)) {
                return item;
            }
        }
        // 3、不能转换
        throw new IllegalArgumentException(String.format("无效的流程启动模式“%S”。", id));
    }

    public static boolean isManual(String id) {
        return ProcessStartModel.fromId(id) == MANUAL;
    }

    public static boolean isAutomatic(String id) {
        return ProcessStartModel.fromId(id) == AUTOMATIC;
    }
}
