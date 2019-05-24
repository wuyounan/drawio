package com.huigou.uasp.bpm.engine.domain.model;

/**
 * 时间限制
 * @author gongmm
 *
 */
public class LimitTime {
    
    private Integer needTiming;
    
    private Integer limitTime;
    
    public LimitTime(Integer needTiming, Integer limitTime){
        this.needTiming = needTiming;
        this.limitTime = limitTime;
    }
    
    public Integer getNeedTiming() {
        return needTiming;
    }

    public void setNeedTiming(Integer needTiming) {
        this.needTiming = needTiming;
    }

    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Integer limitTime) {
        this.limitTime = limitTime;
    } 

}
