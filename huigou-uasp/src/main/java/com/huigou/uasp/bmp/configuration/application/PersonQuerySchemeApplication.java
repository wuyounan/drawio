package com.huigou.uasp.bmp.configuration.application;

import java.util.Map;

import com.huigou.uasp.bmp.configuration.domain.model.PersonQueryScheme;

/**
 * 人员查询方案应用
 * 
 * @author gongmm
 */
public interface PersonQuerySchemeApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/configuration.xml";
    /**
     * 保存人员查询方案
     * 
     * @param personQueryScheme
     *            人员查询方案
     * @return
     */
    String savePersonQueryScheme(PersonQueryScheme personQueryScheme);

    /**
     * 加载人员查询方案
     * 
     * @param id
     *            人员查询方案ID
     * @return
     */
    PersonQueryScheme loadPersonQueryScheme(String id);

    /**
     * 查询人员查询方案
     * 
     * @param personId
     *            人员ID
     * @param kindId
     *            查询方案类别ID
     * @return
     */
    Map<String, Object> queryPersonQuerySchemes(String personId, String kindId);

    /**
     * 删除人员查询方案
     * 
     * @param id
     *            人员查询方案ID
     */
    void deletePersonQueryScheme(String id);

}
