package com.huigou.uasp.bpm;

/**
 * 协作模式
 * 
 * @author gongmm
 */
public class CooperationModelKind {
    /**
     * 主审
     */
    public static final String CHIEF = "chief";

    /**
     * 协审
     */
    public static final String ASSISTANT = "assistant";

    /**
     * 抄送
     */
    public static final String CC = "cc";

    /**
     * 补审
     */
    public static final String MEND = "mend";
    
    public static boolean isChief(String kindId){
        return CHIEF.equals(kindId);
        
    }
}
