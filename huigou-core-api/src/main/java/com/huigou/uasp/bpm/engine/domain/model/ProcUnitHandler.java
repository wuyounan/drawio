package com.huigou.uasp.bpm.engine.domain.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.Assert;

import com.huigou.exception.ApplicationException;

/**
 * 环节处理人
 * 
 * @author gongmm
 */
@Entity
@Table(name = "WF_ProcUnitHandler")
public class ProcUnitHandler extends ProcUnitHandlerBase {

    private static final long serialVersionUID = 7627740680619707939L;

    @Column(name="approval_Rule_Id")
    private String approvalRuleId;

    
    @Override
    public ProcUnitHandler clone() {
        ProcUnitHandler procUnitHandler = null;
        try {
            procUnitHandler = (ProcUnitHandler) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ApplicationException("转换数据出错。");
        }
        return procUnitHandler;
    }

    /**
     * 清空处理意见
     */
    public void clearHandleOpinion() {
        this.setStatus(0);
        this.setResult(null);
        this.setOpinion(null);
        this.setHandledDate(null);
    }

    public static List<ProcUnitHandler> fromProcUnitHandlerCaches(List<ProcUnitHandlerCache> procUnitHandlerCaches) {
        Assert.notEmpty(procUnitHandlerCaches, "参数procUnitHandlerCaches不能为空。");

        List<ProcUnitHandler> result = new ArrayList<ProcUnitHandler>(procUnitHandlerCaches.size());

        try {
            for (ProcUnitHandlerCache procUnitHandlerCache : procUnitHandlerCaches) {
                ProcUnitHandler item = new ProcUnitHandler();
                BeanUtils.copyProperties(item, procUnitHandlerCache);
                result.add(item);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getApprovalRuleId() {
        return approvalRuleId;
    }

    public void setApprovalRuleId(String approvalRuleId) {
        this.approvalRuleId = approvalRuleId;
    }
}
