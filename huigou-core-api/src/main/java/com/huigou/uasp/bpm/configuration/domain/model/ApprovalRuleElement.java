package com.huigou.uasp.bpm.configuration.domain.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "WF_ApprovalRuleElement")
public class ApprovalRuleElement extends AbstractEntity {

    private static final long serialVersionUID = -3993127857842784696L;

    /**
     * 审批要素编码
     */
    @Column(name = "element_code")
    private String elementCode;

    /**
     * 操作符
     */
    private String foperator;

    @Column(name = "fvalue_id")
    private String fvalueId;

    private String fvalue;

    private Integer sequence;

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public String getFoperator() {
        return foperator;
    }

    public void setFoperator(String foperator) {
        this.foperator = foperator;
    }

    public String getFvalueId() {
        return fvalueId;
    }

    public void setFvalueId(String fvalueId) {
        this.fvalueId = fvalueId;
    }

    public String getFvalue() {
        return fvalue;
    }

    public void setFvalue(String fvalue) {
        this.fvalue = fvalue;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonIgnore
    public OperatorKind getOperatorKind(){
        return OperatorKind.fromId(this.foperator);
    }
    
    /**
     * 操作符类型
     * 
     * @author gongmm
     * 
     */
    public enum OperatorKind {
        EQ("EQ", "="), OIN("OIN", "in"), GT("GT", ">"), LT("LT", "<"), GE("GE",
                ">="), LE("LE", "<="), INTERVAL("INTERVAL", "区间"), NOT_EQ(
                "NOT_EQ", "!=");
        private final String id;
        private final String displayName;

        private OperatorKind(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static OperatorKind fromId(String id) {
            if (id.equalsIgnoreCase("EQ"))
                return EQ;
            else if (id.equalsIgnoreCase("OIN"))
                return OIN;
            else if (id.equalsIgnoreCase("GT"))
                return GT;
            else if (id.equalsIgnoreCase("LT"))
                return LT;
            else if (id.equalsIgnoreCase("GE"))
                return GE;
            else if (id.equalsIgnoreCase("LE"))
                return LE;
            else if (id.equalsIgnoreCase("INTERVAL"))
                return INTERVAL;    
            else if (id.equalsIgnoreCase("NOT_EQ"))
                return NOT_EQ;  

            throw new RuntimeException(String.format("无效的操作符“%s”。",
                    new Object[] { Integer.valueOf(id) }));
        }

        public static Map<String, String> getData() {
            Map<String, String> data = new HashMap<String, String>(8);
            for (OperatorKind item : OperatorKind.values()) {
                data.put(item.id, item.displayName);
            }
            return data;
        }
    }
}
