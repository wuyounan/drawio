package com.huigou.uasp.bmp.opm.domain.model.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;

/**
 * 组织机构类别
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPOrgType")
public class OrgType extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 1443905640867022993L;

    /**
     * 组织节点类别ID
     */
    @Column(name = "org_kind_id")
    private String orgKindId;

    private Integer sequence;

    public String getOrgKindId() {
        return this.orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public OrgType() {

    }

    public OrgType(String orgKindId, String folderId, Integer sequence) {
        this.setStatus(ValidStatus.ENABLED.getId());
        this.setFolderId(folderId);
        this.setOrgKindId(orgKindId);
        this.setSequence(sequence);
    }

    public void checkDuplication(OrgType other) {
        if (other != null) {
            if (this.getCode().equalsIgnoreCase(other.getCode())) {
                throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
            }
            if (this.getName().equalsIgnoreCase(other.getName())) {
                throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
            }
        }
    }

    @Override
    public void checkConstraints() {
        super.checkConstraints();
    }

    /**
     * 组织类别类型
     * 
     * @author gongmm
     */
    public enum OrgKind {
        ogn("ogn", "机构"), dpt("dpt", "部门"), pos("pos", "岗位");

        private final String id;

        private final String displayName;

        private OrgKind(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

    }
}
