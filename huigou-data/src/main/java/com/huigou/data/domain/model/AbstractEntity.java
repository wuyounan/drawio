package com.huigou.data.domain.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NotImplementedException;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.listener.VersionListener;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 抽象类体实
 * <p>
 * 所有实体的基类，主要包括实体标识ID和版本属性。
 *
 * @author gongmm
 */
@MappedSuperclass
@EntityListeners({VersionListener.class})
public abstract class AbstractEntity implements IdentifiedEntity, Serializable {

    private static final long serialVersionUID = 352956443971788977L;

    public static final String INPUT_DETAILS_FIELD_NAME = "inputDetails_";

    /**
     * 实体唯一标识
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "guid")
    private String id;

    /**
     * 实体版本号
     */
    private Long version;

    @Transient
    private String[] updateFields_;

    @Transient
    private List<? extends AbstractEntity> inputDetails_;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        if ("".equals(id)) {
            id = null;
        }
        this.id = id;
    }

    @Override
    public Long getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(Long version) {
        this.version = version;
    }

    public void checkConstraints() {
    }

    public AbstractEntity() {

    }

    protected void checkItemNotNull(String itemName, String message) {
        Assert.hasText(itemName, message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) obj;

        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return StringUtil.isBlank(this.id);
    }

    /**
     * 删除明细
     *
     * @param ids 明细ID
     */
    public void removeDetails(List<String> ids) {
        Assert.notEmpty(ids, "参数ids不能为空。");
        for (String id : ids) {
            for (Iterator<? extends AbstractEntity> iter = this.getDetails().iterator(); iter.hasNext(); ) {
                if (id.equals(iter.next().getId())) {
                    iter.remove();
                }
            }
        }
    }

    public void removeDetails(List<? extends AbstractEntity> entities, List<String> ids) {
        for (Iterator<? extends AbstractEntity> iter = entities.iterator(); iter.hasNext(); ) {
            if (ids.contains(iter.next().getId())) {
                iter.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void buildDetails() {
        List<AbstractEntity> details = (List<AbstractEntity>) this.getDetails();
        List<AbstractEntity> inputDetails = (List<AbstractEntity>) this.getInputDetails_();
        if (details == null) {
            details = new ArrayList<AbstractEntity>(inputDetails.size());
            this.setDetails(details);
        }
        int index;
        AbstractEntity detail;
        for (AbstractEntity inputDetail : inputDetails) {
            if (inputDetail.isNew()) {
                details.add(inputDetail);
            } else {
                index = details.indexOf(inputDetail);
                if (index > -1) {
                    detail = details.get(index);
                    detail.fromEntity(inputDetail);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected List<? extends AbstractEntity> buildDetails(List<? extends AbstractEntity> oldDelaills, List<? extends AbstractEntity> newDetails) {
        List<AbstractEntity> result = (List<AbstractEntity>) oldDelaills;
        if (oldDelaills == null) {
            result = new ArrayList<AbstractEntity>(newDetails.size());
        }
        int index;
        AbstractEntity detail;
        for (AbstractEntity inputDetail : newDetails) {
            if (inputDetail.isNew()) {
                result.add(inputDetail);
            } else {
                index = result.indexOf(inputDetail);
                if (index > -1) {
                    detail = result.get(index);
                    detail.fromEntity(inputDetail);
                }
            }
        }
        return result;
    }

    protected List<? extends AbstractEntity> getDetails() {
        throw new NotImplementedException();
    }

    protected void setDetails(List<? extends AbstractEntity> details) {
        throw new NotImplementedException();
    }

    public String[] getUpdateFields_() {
        return updateFields_;
    }

    @Override
    public void setUpdateFields_(Collection<String> names) {
        if (names != null && names.size() > 0) {
            this.updateFields_ = names.toArray(new String[names.size()]);
        }
    }

//    public void setUpdateFields_(String... updateFields) {
//        if (updateFields != null) {
//            this.updateFields_ = updateFields;
//        }
//    }

    public void addUpdateFields_(String... updateFields) {
        if (updateFields != null) {
            this.updateFields_ = (String[]) ArrayUtils.addAll(updateFields_, updateFields);
        }
    }

    public void fromEntity(AbstractEntity otherEntity) {
        if (otherEntity.getUpdateFields_() == null) {
            ClassHelper.copyProperties(otherEntity, this);
        } else {
            Object value = null;
            List<String> propertyNames = ClassHelper.getPropertyNames(this.getClass());
            Field field;
            for (String name : otherEntity.getUpdateFields_()) {
                if (propertyNames.contains(name)) {
                    field = ClassHelper.getField(otherEntity, name);
                    if (field != null) {
                        // value = ClassHelper.getProperty(otherEntity, name); // 返回为String
                        value = ClassHelper.getFieldValue(otherEntity, name);
                        if (field.getType().isEnum() && value == null) {
                            value = "";
                        }
                        ClassHelper.setProperty(this, name, value);
                    }
                }
            }
        }
    }

    public void setInputDetails_(List<? extends AbstractEntity> inputDetails) {
        this.inputDetails_ = inputDetails;
    }

    @JsonIgnore
    public List<? extends AbstractEntity> getInputDetails_() {
        if (inputDetails_ == null) {
            inputDetails_ = new ArrayList<>(0);
        }
        return inputDetails_;
    }

}
