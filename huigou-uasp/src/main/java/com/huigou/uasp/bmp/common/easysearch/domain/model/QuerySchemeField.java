package com.huigou.uasp.bmp.common.easysearch.domain.model;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.query.parser.model.FieldModel;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 查询包含字段模型
 * 
 * @author xx
 */
public class QuerySchemeField extends FieldModel {

    private static final long serialVersionUID = 2212172553921903984L;

    public String getType() {
        return StringUtil.tryThese(super.getType(), "text");
    }

    public boolean isCondition() {
        String autoCondition = this.getAutoCondition();
        if (StringUtil.isNotBlank(autoCondition) && autoCondition.equalsIgnoreCase("false")) {
            return false;
        }
        return true;
    }

    public static QuerySchemeField newInstance(Object obj) {
        QuerySchemeField model = new QuerySchemeField();
        ClassHelper.copyProperties(obj, model);
        return model;
    }

    /**
     * 重写覆盖父类方法用于实现国际化
     */
    public String getName() {
        return MessageSourceContext.getMessage(super.getName());
    }
}
