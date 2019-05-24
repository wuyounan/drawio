package com.huigou.uasp.bmp.opm.domain.model.resource;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.TreeEntity;
import com.huigou.uasp.bmp.opm.domain.model.access.Permission;

/**
 * 系统功能
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPFunction")
public class SysFunction extends TreeEntity {

    private static final long serialVersionUID = -3171537655631537608L;

    public static final String ROOT_ID = "1";

    private String url;

    private String icon;

    private String remark;

    private String description;
    
    @Column(name = "node_kind_id")
    private String nodeKindId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public boolean isFolder(){
        return NodeKind.FOLDER.getId().equals(this.nodeKindId);
    }
    
    @JsonIgnore
    public boolean isFun() {
        return NodeKind.FUN.getId().equals(this.nodeKindId);
    }

    @JsonIgnore
    public boolean isRoot() {
        return this.getId().equals(ROOT_ID);
    }
    
    public Permission toPermission() {
        Permission permission = new Permission();

        permission.setCode(this.getCode());
        permission.setName(this.getName());
        permission.setResourceKindId(ResourceKind.FUN.getId());
        permission.setResourceId(this.getId());
        permission.setNodeKindId(this.nodeKindId);
        permission.setStatus(this.getStatus());        
        permission.setSequence(this.getSequence());
        
        return permission;
    }

    public enum NodeKind {
        FOLDER("folder", "文件夹"), FUN("fun", "功能");
        
        private final String id;
        
        private final String displayName;
        
        public String getId(){
            return this.id;
        }
        
        public String getDisplayName(){
            return this.displayName;
        }
        
        private NodeKind(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }
        
        public static boolean isFolder(String name){
            return FOLDER.name().equals(name);
        }
        
        public static Map<String, String> getData() {
            Map<String, String> result = new HashMap<String, String>(2);
            for (NodeKind item : NodeKind.values()) {
                    result.put(item.getId(), item.getDisplayName());
            }
            return result;
        }
        
    }

}
