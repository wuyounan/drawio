package com.huigou.uasp.bpm.configuration.domain.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 审批要素
 * 
 * @author gongmm
 */
@Entity
@Table(name = "WF_ApprovalElement")
public class ApprovalElement extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = -6947637967565525199L;

    @Column(name = "kind_id")
    private Integer kindId;

    @Column(name = "data_source_config")
    private String dataSourceConfig;
    
    private Integer sequence;

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public String getDataSourceConfig() {
        return dataSourceConfig;
    }

    public void setDataSourceConfig(String dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
    
    public String getKindName(){
        return Kind.fromId(kindId).displayName;
    }
    
//    public String getDataSourceName(){
//        return DataSource.fromId(kindId).displayName;
//    }

//    public enum DataSource {
//        ORG(1, "组织"), DICTIONARY(2, "数据字典"), MANUAL(3, "手工录入");
//
//        private final int id;
//
//        private final String displayName;
//
//        private DataSource(int id, String displayName) {
//            this.id = id;
//            this.displayName = displayName;
//        }
//
//        public int getId() {
//            return this.id;
//        }
//
//        public String getDisplayName() {
//            return this.displayName;
//        }
//
//        public static DataSource fromId(int id) {
//            switch (id) {
//            case 1:
//                return ORG;
//            case 2:
//                return DICTIONARY;
//            case 3:
//                return MANUAL;
//            }
//            throw new RuntimeException(String.format("无效的数据源类型“%s”。", new Object[] { Integer.valueOf(id) }));
//        }
//
//        public static Map<Integer, String> getData() {
//            Map<Integer, String> data = new HashMap<Integer, String>(2);
//            for (DataSource item : DataSource.values()) {
//                data.put(item.getId(), item.getDisplayName());
//            }
//            return data;
//        }
//    }

    public enum Kind {
        SYSTEM(1, "系统"), BIZ(2, "业务");
        private final int id;

        private final String displayName;

        private Kind(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static Kind fromId(int id) {
            switch (id) {
            case 1:
                return SYSTEM;
            case 2:
                return BIZ;
            }
            throw new RuntimeException(String.format("无效的审批要素类型“%s”。", new Object[] { Integer.valueOf(id) }));
        }

        /**
         * 得到类别数据
         * 
         * @return
         */
        public static Map<Integer, String> getData() {
            Map<Integer, String> data = new HashMap<Integer, String>(2);
            for (Kind item : Kind.values()) {
                data.put(item.getId(), item.getDisplayName());
            }
            return data;
        }
    }
}
