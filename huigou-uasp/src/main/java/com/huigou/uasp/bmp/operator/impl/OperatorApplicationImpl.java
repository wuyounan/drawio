package com.huigou.uasp.bmp.operator.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.PersonMember;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.context.User;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.fn.impl.OrgFun;
import com.huigou.uasp.bmp.operator.OperatorApplication;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.TenantApplicationProxy;

/**
 * 创建登录用户信息
 * 
 * @author xx
 */
@Service("operatorApplication")
public class OperatorApplicationImpl implements OperatorApplication {
    @Autowired
    private OrgApplicationProxy orgApplication;

    @Autowired
    private TenantApplicationProxy tenantApplication;

    @Autowired
    private AccessApplicationProxy accessApplication;

    @Autowired
    private OrgFun orgFun;

    @Override
    public Operator createOperatorByLoginName(String loginName) {
        Assert.hasText(loginName, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, loginName));
        Org mainPersonMemberEntity = this.orgApplication.loadMainOrgByLoginName(loginName.toUpperCase());
        if (mainPersonMemberEntity == null) {
            throw new UnknownAccountException();
        }
        Person person = mainPersonMemberEntity.getPerson();
        if (person == null) {
            throw new UnknownAccountException();
        }
        ValidStatus personStatus = person.getValidStatus();
        if (personStatus != ValidStatus.ENABLED) {
            throw new DisabledAccountException();
        }
        ThreadLocalUtil.putVariable("_mainPerson_", person);
        ThreadLocalUtil.putVariable("_mainPersonMember_", OpmUtil.buildLoginResultData(mainPersonMemberEntity));
        return createOperator(mainPersonMemberEntity);
    }

    @Override
    public Operator createOperatorByPersonMemberId(String personMemberId) {
        Assert.hasText(personMemberId, "参数personMemberId不能为空。");
        Org personMemberEntity = this.orgApplication.loadOrg(personMemberId);
        return createOperator(personMemberEntity);
    }

    /**
     * 根据组织节点创建 Operator
     * 
     * @param mainPersonMemberEntity
     * @return
     */
    private Operator createOperator(Org mainPersonMemberEntity) {
        Person person = mainPersonMemberEntity.getPerson();
        PersonMember personMember = OpmUtil.toPersonMember(mainPersonMemberEntity);
        User user = new User(personMember);
        Operator operator = new Operator(user, new Date());
        operator.setOrgContext(personMember);

        Tenant tenant = this.tenantApplication.loadTenant(user.getTenantId());
        if (tenant != null) {
            operator.setRootOrgId(tenant.getOrgId());
            operator.setRootOrgFullId(tenant.getRootFullId());
            operator.setTenantName(tenant.getName());
        }
        // 设置扩展属性
        orgFun.setOperatorExtensionProperties(operator);

        List<Org> personMemberEntities = this.orgApplication.queryPersonMembersByPersonId(person.getId());
        List<String> fullIds = new ArrayList<String>(personMemberEntities.size());
        for (Org item : personMemberEntities) {
            fullIds.add(item.getFullId());
        }
        operator.fillPersonMemberFullIds(fullIds);
        // 加载角色信息
        List<String> roles = accessApplication.queryPersonRoleIds(person.getId());
        operator.fillRoleIds(roles);
        operator.setRoleKind(accessApplication.getPersonRoleKind(operator.getUserId()));
        return operator;
    }

    /**
     * 根据组织节点创建 Operator 不查询角色信息
     * 
     * @param mainPersonMemberEntity
     * @return
     */
    private Operator createOperatorNoRole(Org mainPersonMemberEntity) {
        Person person = mainPersonMemberEntity.getPerson();
        PersonMember personMember = OpmUtil.toPersonMember(mainPersonMemberEntity);
        User user = new User(personMember);
        Operator operator = new Operator(user, new Date());
        operator.setOrgContext(personMember);
        // 设置扩展属性
        orgFun.setOperatorExtensionProperties(operator);
        List<Org> personMemberEntities = this.orgApplication.queryPersonMembersByPersonId(person.getId());
        List<String> fullIds = new ArrayList<String>(personMemberEntities.size());
        for (Org item : personMemberEntities) {
            fullIds.add(item.getFullId());
        }
        operator.fillPersonMemberFullIds(fullIds);
        return operator;
    }

    @Override
    public Operator createOperatorByPersonId(String personId) {
        Assert.hasText(personId, "参数personMemberId不能为空。");
        Org personMemberEntity = this.orgApplication.loadMainOrgByPersonId(personId);
        if (personMemberEntity == null) {
            throw new UnknownAccountException();
        }
        return createOperatorNoRole(personMemberEntity);
    }
}
