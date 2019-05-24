package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.opm.domain.model.resource.UIElement;
import com.huigou.uasp.bmp.opm.domain.query.UIElementsQueryRequest;

public interface UIElementApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 保存界面元素
     * 
     * @param uiElement
     *            界面元素对象
     * @return
     */
    String saveUIElement(UIElement uiElement);

    /**
     * 加载界面元素
     * 
     * @param id
     *            界面元素ID
     * @return
     */
    UIElement loadUIElement(String id);

    /**
     * 删除界面元素
     * 
     * @param ids
     *            界面元素ID列表
     */
    void deleteUIElements(List<String> ids);

    /**
     * 分页查询界面元素
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryUIElements(UIElementsQueryRequest queryRequest);

    /**
     * 更新界面元素状态
     * 
     * @param ids
     *            界面元素ID列表
     * @param status
     *            状态
     */
    void updateUIElementsStatus(List<String> ids, Integer status);

    /**
     * 得到下一个排序号
     * 
     * @param folderId
     *            文件夹ID
     * @return
     */
    Integer getUIElementNextSequence(String folderId);

    /**
     * 更新界面元素排序号
     * 
     * @param paramsgetUIElementNextSequence
     *            界面元素ID和排序号的Map
     */
    void updateUIElementsSequence(Map<String, Integer> params);

    /**
     * 移动界面元素
     * 
     * @param ids
     *            界面元素ID列表
     * @param folderId
     *            文件夹
     */
    void moveUIElements(List<String> ids, String folderId);

}
