package com.huigou.uasp.bmp.portal.mainpage.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.portal.mainpage.domain.model.PersonDesktopScreen;

/**
 * 个人桌面应用
 * 
 * @author gongmm
 */
public interface PersonDesktopApplication {

    /**
     * 保存用户桌面屏幕
     * 
     * @return
     */
    String savePersonDesktopScreen();

    /**
     * 保存用户桌面屏幕功能
     * 
     * @param screenId
     *            桌面屏幕ID
     * @param functionIds
     *            功能ID
     * @return
     */
    void savePersonDesktopScreenFunctions(String screenId, List<String> functionIds);

    public String savePersonDesktopScreenAndFunctions(String screenId, List<String> functionIds);

    /**
     * 删除用户桌面屏幕
     * 
     * @param id
     */
    void deletePersonDesktopScreen(String id);

    /**
     * 查询用户桌面屏幕
     * 
     * @return
     */
    List<PersonDesktopScreen> queryPersonDesktopScreens();

    /**
     * 查询用户功能
     * 
     * @param parentId
     *            父ID
     * @return
     */
    List<Map<String, Object>> queryPersonFunctions(String parentId);

    /**
     * 查询用户功能
     * 
     * @param personId
     * @return
     */
    public List<Map<String, Object>> queryPersonDesktopFunctions(String personId);

    /**
     * 查询用户流程功能
     * 
     * @param parentId
     *            父ID
     * @return
     */
    List<Map<String, Object>> queryJobFunctions(Long parentId);

    /**
     * 查询常用功能
     * 
     * @return
     */
    List<Map<String, Object>> queryOftenUseFunctions();

}
