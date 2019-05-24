package com.huigou.uasp.bmp.configuration.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.configuration.application.CommonTreeApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTree;
import com.huigou.uasp.bmp.configuration.domain.query.CommonTreeQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 配置管理类
 *
 * @author Gerald
 */
@Controller
@ControllerMapping("commonTree")
public class CommonTreeController extends CommonController {

    private static final String COMMON_TREE_DETAIL_PAGE = "/common/CommonTreeDetail.jsp";

    private static final String SELECT_COMMONTREE_DIALOG_PAGE = "SelectCommonTreeDialog";

    @Autowired
    private CommonTreeApplication application;

    @Override
    protected String getPagePath() {
        return "/system/configuration/";
    }

    public String showCommonTreeDialog() {
        return this.forward(SELECT_COMMONTREE_DIALOG_PAGE);
    }

    public String forwardCommonTreeDetail() {
        return forward(COMMON_TREE_DETAIL_PAGE);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加通用树")
    public String insertCommonTree() {
        SDO params = this.getSDO();
        CommonTree commonTree = params.toObject(CommonTree.class);
        String id = application.insert(commonTree);
        return success(id);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改通用树")
    public String updateCommonTree() {
        SDO params = this.getSDO();
        CommonTree commonTree = params.toObject(CommonTree.class);
        this.application.update(commonTree);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除通用树")
    public String deleteCommonTree() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        this.application.delete(Arrays.asList(id));
        return success();

    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "查询通用树")
    public String loadCommonTree() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        CommonTree commonTree = this.application.load(id);
        return forward(COMMON_TREE_DETAIL_PAGE, commonTree);

    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改通用树")
    public String updateCommonTreeSequence() {
        Map<String, Integer> params = this.getSDO().getStringMap("data");
        this.application.updateSequence(params);
        return success();

    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询通用树")
    public String queryCommonTrees() {
        SDO params = this.getSDO();
        CommonTreeQueryRequest queryRequest = params.toQueryRequest(CommonTreeQueryRequest.class);
        Map<String, Object> data = this.application.query(queryRequest);
        return toResult(data);
    }

}
