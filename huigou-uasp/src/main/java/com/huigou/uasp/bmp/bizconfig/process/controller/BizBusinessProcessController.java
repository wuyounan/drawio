package com.huigou.uasp.bmp.bizconfig.process.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.bizconfig.process.application.BizBusinessProcessApplication;
import com.huigou.uasp.bmp.bizconfig.process.domain.model.BpmBusinessProcess;
import com.huigou.uasp.bmp.bizconfig.process.domain.query.BusinessProcessQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("bizBusinessProcess")
public class BizBusinessProcessController extends CommonController {
    private static final String BUSINESS_PROCESS_LIST_PAGE = "businessProcessList";

    private static final String BUSINESS_PROCESS_EDIT_PAGE = "businessProcessEdit";

    private static final String BUSINESS_PROCESS_DETAIL_PAGE = "businessProcessDetail";

    @Autowired
    private BizBusinessProcessApplication bizBusinessProcessApplication;

    protected String getPagePath() {
        return "/system/bizconfig/process/";
    }

    @RequiresPermissions("BizBusinessProcess:query")
    public String forwardBusinessProcess() {
        return forward(BUSINESS_PROCESS_LIST_PAGE);
    }

    @RequiresPermissions("BizBusinessProcess:create")
    public String showInsertBusinessProcess() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getString("parentId");
        Integer sequence = bizBusinessProcessApplication.getBusinessProcessNextSequence(parentId);
        if (!parentId.equals(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID)) {
            BpmBusinessProcess businessProcess = bizBusinessProcessApplication.loadBusinessProcess(parentId);
            sdo.putProperty("parentName", businessProcess.getName());
            sdo.putProperty("parentCode", businessProcess.getCode());
        }
        sdo.putProperty("sequence", sequence);
        return forward(BUSINESS_PROCESS_DETAIL_PAGE, sdo);
    }

    @RequiresPermissions("BizBusinessProcess:update")
    public String showUpdateBusinessProcess() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        BpmBusinessProcess businessProcess = bizBusinessProcessApplication.loadBusinessProcess(id);
        return forward(BUSINESS_PROCESS_DETAIL_PAGE, businessProcess);
    }

    @RequiresPermissions("BizBusinessProcess:update")
    public String showEditBusinessProcessAttribute() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        BpmBusinessProcess businessProcess = bizBusinessProcessApplication.loadBusinessProcess(id);
        return forward(BUSINESS_PROCESS_EDIT_PAGE, businessProcess);
    }

    @RequiresPermissions("BizBusinessProcess:create")
    public String insertBusinessProcess() {
        SDO sdo = this.getSDO();
        BpmBusinessProcess businessProcess = sdo.toObject(BpmBusinessProcess.class);
        String id = bizBusinessProcessApplication.insertBusinessProcess(businessProcess);
        return success(id);
    }

    @RequiresPermissions("BizBusinessProcess:update")
    public String saveBusinessProcessAndAttribute() {
        SDO sdo = this.getSDO();
        BpmBusinessProcess businessProcess = sdo.toObject(BpmBusinessProcess.class);
        bizBusinessProcessApplication.saveBusinessProcessAndAttribute(businessProcess);
        return success();
    }

    @RequiresPermissions("BizBusinessProcess:update")
    public String updateBusinessProcess() {
        SDO sdo = this.getSDO();
        BpmBusinessProcess businessProcess = sdo.toObject(BpmBusinessProcess.class);
        bizBusinessProcessApplication.updateBusinessProcess(businessProcess);
        return success();
    }

    @RequiresPermissions("BizBusinessProcess:update")
    public String updateBusinessProcessesSequence() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getString(PARENT_ID_KEY_NAME);
        Map<String, Integer> params = sdo.getStringMap("data");
        this.bizBusinessProcessApplication.updateBusinessProcessesSequence(parentId, params);
        return success();
    }

    @RequiresPermissions("BizBusinessProcess:delete")
    public String deleteBusinessProcesses() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        bizBusinessProcessApplication.deleteBusinessProcesses(ids);
        return toResult("");
    }

    public String slicedQueryBusinessProcesses() {
        SDO sdo = this.getSDO();
        BusinessProcessQueryRequest queryRequest = sdo.toQueryRequest(BusinessProcessQueryRequest.class);
        Map<String, Object> data = bizBusinessProcessApplication.slicedQueryBusinessProcesses(queryRequest);
        return this.toResult(data);
    }

    public String queryBusinessProcesses() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getString(PARENT_ID_KEY_NAME);
        List<Map<String, Object>> data = bizBusinessProcessApplication.queryBusinessProcesses(parentId);
        return this.toResult(data);
    }

    public String queryBusinessProcessesOnMove() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getString(PARENT_ID_KEY_NAME);
        String excludeIds = sdo.getString("excludeIds");
        List<Map<String, Object>> data = bizBusinessProcessApplication.queryBusinessProcessesOnMove(parentId, excludeIds);
        return this.toResult(data);
    }

    @RequiresPermissions("BizBusinessProcess:update")
    public String moveBusinessProcesses() {
        SDO params = this.getSDO();
        String parentId = params.getString("parentId");
        List<String> ids = params.getStringList("ids");
        this.bizBusinessProcessApplication.moveBusinessProcesses(parentId, ids);
        return this.success();
    }
}
