package com.huigou.uasp.log.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;





import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.uasp.log.annotation.LogPorperty;

/**
 * 应用系统
 * @author gongmm
 *
 */
@Entity
@LogPorperty
@Table(name="SA_ApplicationSystem")
public class ApplicationSystem extends AbstractEntity {
	
	private static final long serialVersionUID = 6571803878461914561L;

	@LogPorperty
	private String code;
    
	@LogPorperty
    private String name;
    
	@LogPorperty
    private Integer sequence;
    
	@LogPorperty
    @Column(name="class_prefix")
    private String classPrefix;

	@LogPorperty
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @LogPorperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @LogPorperty
    public String getClassPrefix() {
        return classPrefix;
    }

    public void setClassPrefix(String classPrefix) {
        this.classPrefix = classPrefix;
    }

    @LogPorperty
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
