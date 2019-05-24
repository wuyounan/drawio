package com.huigou.uasp.log.aspect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.huigou.context.ContextUtil;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.context.TmspmConifg;
import com.huigou.uasp.bmp.securitypolicy.application.ApplicationSystemApplication;
import com.huigou.uasp.bmp.securitypolicy.application.MachineApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.exception.AppAuthRequestException;
import com.huigou.uasp.exception.AuthException;
import com.huigou.uasp.log.application.LogApplication;
import com.huigou.uasp.log.domain.model.BizLog;
import com.huigou.uasp.log.domain.model.BizLogDetail;
import com.huigou.uasp.log.domain.model.LogStatus;
import com.huigou.uasp.log.util.BizLogUtil;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 日志切面
 * 
 * @author gongmm
 */
@Aspect
public class LogAspect implements ApplicationContextAware {

    @Autowired
    private ApplicationSystemApplication applicationSystemApplication;

    @Autowired
    private MachineApplication machineApplication;

    @Autowired
    private TmspmConifg tmspmConifg;

    private ApplicationContext applicationContext;

    /**
     * 日志接口
     */
    private LogApplication logApplication;

    public void setLogApplication(LogApplication logApplication) {
        this.logApplication = logApplication;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private BizLog preMethodInvoke(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature ms = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        Method method = ms.getMethod();

        BizLog bizLog = createBizLog();
        BizLogDetail bizLogDetail = createBizLogDetail();
        bizLog.setBizLogDetail(bizLogDetail);
        BizLogUtil.putBizLog(bizLog);

        Machine machine = this.machineApplication.queryMachineByIP(ContextUtil.getRequestIP());
        BizLogBuilder.buildLogInfo(bizLog, bizLogDetail, className, method, machine, tmspmConifg.isEnableTspm());
        return bizLog;
    }

    private void postMethodInvoke(BizLog bizLog, Throwable failureCause) {
        bizLog.setEndDate(new Date());
        
        SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        if (sdo != null) {
            String params = sdo.getProperties().toString();
            bizLog.getBizLogDetail().setParams(params);
        }
        
        Object exceptionMessage = BizLogUtil.getBizLogException();
        if (failureCause != null || exceptionMessage != null) {
            bizLog.setStatusId(LogStatus.FAILURE.getId());
            bizLog.setStatusName(LogStatus.FAILURE.getName());
            if (failureCause != null) {
                bizLog.getBizLogDetail().setErrorMessage(getStackTrace(failureCause));
            } else {
                if (exceptionMessage instanceof String) {
                    bizLog.getBizLogDetail().setErrorMessage((String) exceptionMessage);
                } else if (exceptionMessage instanceof Throwable) {
                    bizLog.getBizLogDetail().setErrorMessage(getStackTrace((Throwable) exceptionMessage));
                }
            }
        }
        logApplication.savelog(bizLog, bizLog.getBizLogDetail());
    }

    @Around("@annotation(com.huigou.uasp.log.annotation.LogInfo)")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        BizLog bizLog = preMethodInvoke(proceedingJoinPoint);
        Throwable failureCause = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (AppAuthRequestException | AuthException ex) {
            failureCause = ex;
            throw ex;
        } catch (Throwable ex) {
            failureCause = ex;
            throw ex;
        } finally {
            postMethodInvoke(bizLog, failureCause);
        }
        return result;
    }

    /**
     * 创建bizLog实例
     * 
     * @return
     */
    private BizLog createBizLog() {
        return (BizLog) this.applicationContext.getBean("bizLog");
    }

    /**
     * 创建bizLogDetail实例
     * 
     * @return
     */
    private BizLogDetail createBizLogDetail() {
        return (BizLogDetail) this.applicationContext.getBean("bizLogDetail");
    }

    /**
     * 获取异常堆栈信息
     * 
     * @param throwable
     * @return
     */
    private String getStackTrace(Throwable throwable) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            pw.flush();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }

}