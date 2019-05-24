package com.huigou.data.datamanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.util.ListUtil;

/**
 * 数据管理权限包含维度资源
 * 
 * @author xx
 */
public class DataManageResourceGroup implements Serializable {
    private static final long serialVersionUID = -2024758078008988441L;

    private String dataManagedetalId;

    private Map<String, List<DataManageResource>> resources;

    public DataManageResourceGroup() {
        resources = new HashMap<>();
    }

    public String getDataManagedetalId() {
        return dataManagedetalId;
    }

    public void setDataManagedetalId(String dataManagedetalId) {
        this.dataManagedetalId = dataManagedetalId;
    }

    public Map<String, List<DataManageResource>> getResources() {
        return resources;
    }

    public List<DataManageResource> getResourcesByCode(String code) {
        return resources.get(code);
    }

    /**
     * 数据去重复
     */
    public void distinctResources() {
        for (String key : resources.keySet()) {
            resources.put(key, ListUtil.distinct(resources.get(key)));
        }
    }

    /**
     * 加入资源数据
     * 
     * @param resource
     */
    public void putResources(String code, List<DataManageResource> data) {
        List<DataManageResource> codeResources = resources.get(code);
        if (codeResources == null) {
            codeResources = new ArrayList<>();
        }
        if (data != null && data.size() > 0) {
            codeResources.addAll(data);
        }
        resources.put(code, codeResources);
    }

}
