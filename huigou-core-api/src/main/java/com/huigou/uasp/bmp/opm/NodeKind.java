package com.huigou.uasp.bmp.opm;

import com.huigou.util.Util;

/**
 * 节点类型
 * 
 * @author gongmm
 */
public enum NodeKind {
    leaf, limb; // 叶子 分支

    public static NodeKind fromString(String value) {
        NodeKind nodeKind = null;
        if (leaf.toString().equals(value)) {
            nodeKind = leaf;
        } else if (Util.isEmptyString(value)) {
            nodeKind = limb;
        }
        Util.check(nodeKind != null, "无效的NodeKind“%s”！", new Object[] { value });
        return nodeKind;
    }
}