package com.huigou.uasp.bpm.configuration.domain.query;

public class ProcApprovalElementDesc {

    private String id;

    private String procId;

    private String procName;

    private String procUnitId;

    private String procUnitName;

    private String elementId;

    private String elementCode;

    private String elementName;

    private Integer sequence;

    public ProcApprovalElementDesc(String id, String procId, String procName, String procUnitId, String procUnitName, String elementId, String elementCode,
                                   String elementName, Integer sequence) {
        this.id = id;
        this.procId = procId;
        this.procName = procName;
        this.procUnitId = procUnitId;
        this.procUnitName = procUnitName;
        this.elementId = elementId;
        this.elementCode = elementCode;
        this.elementName = elementName;
        this.sequence = sequence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcUnitId() {
        return procUnitId;
    }

    public void setProcUnitId(String procUnitId) {
        this.procUnitId = procUnitId;
    }

    public String getProcUnitName() {
        return procUnitName;
    }

    public void setProcUnitName(String procUnitName) {
        this.procUnitName = procUnitName;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }


}
