package com.huigou.uasp.bpm.monitor.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bpm.monitor.application.ProcMonitorApplication;
import com.huigou.uasp.bpm.monitor.domain.model.Ask;
import com.huigou.uasp.bpm.monitor.domain.model.AskDetailStatus;
import com.huigou.uasp.bpm.monitor.domain.model.AskStatus;
import com.huigou.uasp.bpm.monitor.domain.query.AskQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("procMonitor")
public class ProcMonitorController extends CommonController {

    @Autowired
    private ProcMonitorApplication procMonitorApplication;

    @Autowired
    private OrgApplicationProxy orgApplication;

    protected String getPagePath() {
        return "/system/procmonitor/";
    }

    public String forwardQueryExecutingProcs() {
        return forward("queryExecutingProcs");
    }

    public String forwardQueryCompletedProcs() {
        return forward("queryCompletedProcs");
    }

    public String showAskDetail() {
        SDO params = this.getSDO();
        try {
            String bizId = params.getString("bizId");
            String procId = params.getString("procId");
            String id = params.getString("id");
            String askDetailId = params.getString("askDetailId");
            String mode = params.getString("mode");
            String bizTitle = params.getString("bizName");
            // Integer kindId = params.getInteger("kindId");
            // Integer exceptionKindId = params.getInteger("exceptionKindId");
            bizTitle = new String(bizTitle.getBytes("iso-8859-1"), "utf-8");
            // String personMemberName = params.getOperator().getPersonMemberName();
            String accepterId = params.getString("accepterId");

            Ask ask = null;
            if (id != null && id.length() > 0) {
                ask = this.procMonitorApplication.loadAsk(id);
            } else {
                // if (mode.equals("ask")) {
                // // 验证是否询问过
                // ask = this.procMonitorApplication.findByBizIdAndKindId(bizId, kindId);
                // if (ask == null) {
                // ask = new Ask();
                // ask.setBizId(bizId);
                // ask.setBizTitle(bizTitle);
                // ask.setKindId(kindId);
                // ask.setExceptionkindId(exceptionKindId);
                // ask.setAskStatus(AskStatus.UNASK.getId());
                // ask.setSponsor(personMemberName);
                //
                // this.procMonitorApplication.insertAsk(ask);
                // }
                // }
            }

            if (ask != null && ask.getAskStatus() != null && ask.getAskStatus().intValue() > 0) this.putAttribute("statusName",
                                                                                                                  AskStatus.fromId(ask.getAskStatus())
                                                                                                                           .getDisplayName());
            this.putAttribute("procId", procId);
            this.putAttribute("mode", mode);
            this.putAttribute("askDetailId", askDetailId);
            this.putAttribute("askDetailStatusData", AskDetailStatus.getMap());
            this.putAttribute("accepterId", "accepterId");
            if (accepterId != null && accepterId.length() > 0) {
                Org org = orgApplication.loadOrg(accepterId);
                this.putAttribute("accepterName", org.getName());
                this.putAttribute("accepterName_text", org.getName());
            }

            if (ask == null) {
                ask = new Ask();
                ask.setBizId(bizId);
            }

            return forward("askDetail", ask);
        } catch (Exception e) {
            return error(e);
        }
    }

    public String sliceQueryExecutingProcs() {
        SDO params = this.getSDO();
        AskQueryRequest askQueryRequest = params.toQueryRequest(AskQueryRequest.class);
        Map<String, Object> data = this.procMonitorApplication.sliceQueryExecutingProcs(askQueryRequest);
        return toResult(data);
    }

    public String sliceQueryCompletedProcs() {
        SDO params = this.getSDO();
        AskQueryRequest askQueryRequest = params.toQueryRequest(AskQueryRequest.class);
        Map<String, Object> data = this.procMonitorApplication.sliceQueryCompletedProcs(askQueryRequest);
        return toResult(data);
    }

}
