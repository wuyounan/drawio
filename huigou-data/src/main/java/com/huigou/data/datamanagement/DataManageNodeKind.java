package com.huigou.data.datamanagement;

import java.util.LinkedHashMap;
import java.util.Map;

import com.huigou.exception.ApplicationException;

/**
 * 节点类型
 * 
 * @author xx
 */
public enum DataManageNodeKind {
    LIMB("1", "分类"), LEAF("2", "节点");

    private String id;

    private String name;

    private DataManageNodeKind(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static DataManageNodeKind fromId(String id) {
        DataManageNodeKind nodeKind = null;
        if (LEAF.id.equals(id)) {
            nodeKind = LEAF;
        } else if (LIMB.id.equals(id)) {
            nodeKind = LIMB;
        } else {
            throw new ApplicationException(String.format("无效的NodeKind“%s”。", id));
        }
        return nodeKind;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(6);
        for (DataManageNodeKind item : DataManageNodeKind.values()) {
            result.put(item.getId(), item.getName());
        }
        return result;
    }

}