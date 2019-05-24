package com.huigou.uasp.bpm.engine.domain.model;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

@Entity
@Table(name = "ACT_HI_Taskinst_Extension")
public class HistoricTaskInstExtension extends TaskExtension {

    private static final long serialVersionUID = -2771781772368632751L;

    public HistoricTaskInstExtension(){
        
    }
   
    public HistoricTaskInstExtension(RuntimeTaskExtension runtimeTaskExtension) {
        Assert.notNull(runtimeTaskExtension, "参数runtimeTaskExtension不能为空。");

        try {
            BeanUtils.copyProperties(this, runtimeTaskExtension);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
