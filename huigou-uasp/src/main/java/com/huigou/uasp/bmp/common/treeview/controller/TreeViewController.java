package com.huigou.uasp.bmp.common.treeview.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.common.treeview.application.TreeViewApplication;
import com.huigou.uasp.bmp.common.treeview.domain.TreeViewLoadInterface;
import com.huigou.uasp.bmp.common.treeview.domain.model.TreeModel;
import com.huigou.uasp.bmp.common.treeview.domain.model.TreeViewMappingModel;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.Constants;
import com.huigou.util.LogHome;
import com.huigou.util.SDO;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

/**
 * 查询树结构
 *
 * @author Gerald
 */
@Controller
@ControllerMapping("treeView")
public class TreeViewController extends CommonController {

    @Autowired
    private TreeViewLoadInterface treeViewManager;

    private TreeModel getTreeModel(SDO sdo) throws ApplicationException {
        TreeModel model = null;
        String configType = sdo.getProperty("treeViewMappingType", String.class, "sys");
        String name = sdo.getProperty("treeViewMappingName", String.class);
        try {
            if (StringUtil.isBlank(name)) {
                throw new ApplicationException("treeViewMappingName不能为空！");
            }
            TreeViewMappingModel mappingModel = treeViewManager.loadConfigFile(configType);
            if (null != mappingModel) {
                model = mappingModel.getTreeModel(name);
                if (model == null) {
                    throw new ApplicationException("未找到名字为" + name + "的配置！");
                }
            } else {
                throw new ApplicationException("未找到类别为" + configType + "的查询配置！");
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return model;
    }

    private TreeViewApplication getTreeViewApplication(String serviceName) {
        if (StringUtil.isBlank(serviceName)) {
            serviceName = "treeViewApplication";
        }
        return SpringBeanFactory.getBean(this.getServletContext(), serviceName, TreeViewApplication.class);
    }

    /**
     * 查询根节点
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public String root() throws Exception {
        SDO sdo = this.getSDO();
        try {
            TreeModel model = getTreeModel(sdo);
            model.setManageType(sdo.getProperty(Constants.MANAGE_TYPE, String.class));
            Operator operator = this.getOperator();
            if (operator != null && operator.getLoginUser() != null) {
                ThreadLocalUtil.putOperator(operator);
            }
            Map<String, Object> param = sdo.getProperties();
            param.put("currentPersonId", sdo.getOperator().getUserId());
            param.put("currentOrgId", sdo.getOperator().getOrgId());
            param.put("currentPositionId", sdo.getOperator().getPositionId());
            param.put("currentPersonMemberId", sdo.getOperator().getPersonMemberId());
            param.put("currentDeptId", sdo.getOperator().getDeptId());
            param.put("currentFullId", sdo.getOperator().getFullId());
            param.put("currentTenantId", sdo.getOperator().getTenantId());
            TreeViewApplication application = this.getTreeViewApplication(model.getServiceName());
            List<Map<String, Object>> list = application.treeBuilder(model, param);
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("Rows", list);
            map.put("isAjax", model.isAjax());
            map.put("idFieldName", StringUtil.getHumpName(model.getPrimarykey()));
            map.put("parentIDFieldName", StringUtil.getHumpName(model.getConnectby()));
            map.put("textFieldName", StringUtil.getHumpName(model.getLabel()));
            return toResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            LogHome.getLog(this).error(e);
            return error(e.getMessage());
        }
    }

    /**
     * 查询子节点
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public String children() throws ServletException, IOException {
        SDO sdo = this.getSDO();
        Operator operator = this.getOperator();
        if (operator != null && operator.getLoginUser() != null) {
            ThreadLocalUtil.putOperator(operator);
        }
        try {
            TreeModel model = getTreeModel(sdo);
            String id = sdo.getProperty(TreeModel.TREE_PARENT_ID, String.class);
            Map<String, Object> param = sdo.getProperties();
            param.put("currentPersonId", sdo.getOperator().getUserId());
            param.put("currentOrgId", sdo.getOperator().getOrgId());
            param.put("currentPositionId", sdo.getOperator().getPositionId());
            param.put("currentPersonMemberId", sdo.getOperator().getPersonMemberId());
            param.put("currentDeptId", sdo.getOperator().getDeptId());
            param.put("currentFullId", sdo.getOperator().getFullId());
            param.put("currentTenantId", sdo.getOperator().getTenantId());
            TreeViewApplication application = this.getTreeViewApplication(model.getServiceName());
            List<Map<String, Object>> list = application.treeBuilderChildren(id, model, param);
            return toResult(list);
        } catch (Exception e) {
            e.printStackTrace();
            LogHome.getLog(this).error(e);
            return error(e.getMessage());
        }
    }

}
