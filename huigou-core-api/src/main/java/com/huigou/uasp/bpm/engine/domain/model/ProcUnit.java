package com.huigou.uasp.bpm.engine.domain.model;

/**
 * 流程定义
 * 
 * @author Administrator
 */
public class ProcUnit {
    private String id;

    private String name;

    private String parentId;

    private boolean isexpand;

    private boolean hasChildren;

    private String nodeKindId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setisexpand(boolean expand) {
        this.isexpand = expand;
    }

    public boolean getisexpand() {
        return this.isexpand;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean getHasChildren() {
        return this.hasChildren;
    }

    public String getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    /**
     * 节点类型
     * 
     * @author Administrator
     */
    public enum NodeKind {
        PROC(1, "Proc"), PROC_UNIT(2, "ProcUnit");
        private final int id;

        private final String name;

        private NodeKind(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }

}
