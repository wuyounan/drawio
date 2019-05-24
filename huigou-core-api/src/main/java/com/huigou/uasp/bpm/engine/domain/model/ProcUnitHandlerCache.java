package com.huigou.uasp.bpm.engine.domain.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

/**
 * 环节处理人
 * 
 * @author gongmm
 */
@Entity
@Table(name = "WF_ProcUnitHandler_Cache")
public class ProcUnitHandlerCache extends ProcUnitHandlerBase {

    private static final long serialVersionUID = 7627740680619707939L;

    public static List<ProcUnitHandlerCache> fromProcUnitHandlers(List<ProcUnitHandler> procUnitHandlers) {
        Assert.notEmpty(procUnitHandlers, "参数procUnitHandlers不能为空。");

        List<ProcUnitHandlerCache> result = new ArrayList<ProcUnitHandlerCache>(procUnitHandlers.size());

        try {
            for (ProcUnitHandler procUnitHandler : procUnitHandlers) {
                ProcUnitHandlerCache item = new ProcUnitHandlerCache();
                BeanUtils.copyProperties(item, procUnitHandler);
                result.add(item);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

}
