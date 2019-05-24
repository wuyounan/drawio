package com.huigou.uasp.bmp.fn;

import java.util.List;

/**
 * 编码生成器
 * 
 * @author gongmm
 */
public interface CodeGenerator {

    /**
     * 单据编号
     * 
     * @param bizKindId
     *            业务类别ID
     * @return
     */
    String getNextCode(String bizKindId);

    /**
     * 按步长批量取号
     * 
     * @param bizKindId
     * @param step
     */
    List<String> getNextCodesAsStep(String bizKindId, Integer step);

}
