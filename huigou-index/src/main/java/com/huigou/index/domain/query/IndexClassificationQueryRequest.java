package com.huigou.index.domain.query;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;

/**
 * 指标分类
 * 
 * @author
 *         NG_INDEX_CLASSIFICATION
 * @date 2017-09-25 14:58
 */
public class IndexClassificationQueryRequest extends ParentAndCodeAndNameQueryRequest {
    
    private Integer isDefault;

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    

}
