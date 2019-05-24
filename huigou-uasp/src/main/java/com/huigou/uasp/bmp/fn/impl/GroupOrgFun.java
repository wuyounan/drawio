package com.huigou.uasp.bmp.fn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;

/**
 * 集团组织机构函数
 * @author gongmm
 *
 */
@Service("groupOrgFun")
public class GroupOrgFun {
	
	@Autowired
	private OrgFun orgFun;
	
    /**
     * 是否总部
     * 
     * @param orgId
     *            组织机构ID
     * @return
     */
    public Boolean isHQ(String orgId) {
    	Assert.hasText(orgId, "调用集团组织函数出错，参数orgId不能为空。");
        return this.getHQOrganId().equalsIgnoreCase(orgId);
    }

    /**
     * 得到总部公司ID
     * 
     * @return
     */
    private String getHQOrganId() {
        return SystemCache.getParameter("HQOrganId", String.class);
    }
	
    /**
     * 当前人员成员是否总部人员
     * @return
     */
    public Boolean currentPersonMemberIsHQ(){
    	return isHQ(orgFun.currentOrgId());
    }
    

}
