package com.huigou.uasp.bpm.configuration.domain.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

@Entity
@Table(name = "WF_ApprovalHandlerKind")
public class ApprovalHandlerKind extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = -4023251391352822218L;

    @Column(name = "data_Source_id")
    private Integer dataSourceId;

    @Column(name = "data_source_config")
    private String dataSourceConfig;

    private Integer sequence;

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
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

    public enum DataSource {
        ORG(1, "组织"), DICTIONARY(2, "数据字典"), MANUAL(3, "手工录入");

        private final int id;

        private final String displayName;

        private DataSource(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static DataSource fromNumber(int number) {
            switch (number) {
            case 1:
                return ORG;
            case 2:
                return DICTIONARY;
            case 3:
                return MANUAL;
            }
            throw new RuntimeException(String.format("无效的数据源类型“%s”。", new Object[] { Integer.valueOf(number) }));
        }

        public static Map<String, String> getData() {
            Map<String, String> data = new HashMap<String, String>(2);
            for (DataSource item : DataSource.values()) {
                data.put(String.valueOf(item.getId()), item.getDisplayName());
            }
            return data;
        }
    }

}
