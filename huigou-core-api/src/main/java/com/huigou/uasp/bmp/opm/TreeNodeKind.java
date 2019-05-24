package com.huigou.uasp.bmp.opm;

import com.huigou.exception.ApplicationException;

/**
 * 节点类型
 * 
 * @author gongmm
 */
public enum TreeNodeKind {
    LIMB(1, "LIMB"), LEAF(2, "leaf"); // 叶子 分支

    private Integer id;

    private String code;

    private TreeNodeKind(Integer id, String code) {
        this.id = id;
        this.code = code;
    }

    public static TreeNodeKind fromId(Integer id) {
        TreeNodeKind nodeKind = null;
        if (LEAF.id.equals(id)) {
            nodeKind = LEAF;
        } else if (LIMB.id.equals(id)) {
            nodeKind = LIMB;
        } else {
            throw new ApplicationException(String.format("无效的NodeKind“%s”。", id));
        }
        return nodeKind;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

}