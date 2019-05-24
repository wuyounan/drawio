package com.huigou.uasp.bmp.securitypolicy.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.EntityUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.securitypolicy.application.MachineApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.bmp.securitypolicy.domain.query.MachinesQueryRequest;
import com.huigou.uasp.bmp.securitypolicy.repository.MachineRepository;
import com.huigou.uasp.bmp.securitypolicy.repository.PersonLoginLimitRepository;
import com.huigou.util.StringPool;

/**
 * Machine接口实现
 * 
 * @author yuanwf
 */
@Service("machineApplication")
public class MachineApplicationImpl extends BaseApplication implements MachineApplication {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private PersonLoginLimitRepository personLoginLimitRepository;

    private void checkMachineConstraints(Machine machine) {
        String id = machine.isNew() ? StringPool.AT : machine.getId();
        Integer count = this.machineRepository.countByCodeAndId(machine.getCode(), id);
        EntityUtil.isNotDuplicate(count == 0, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));

        count = this.machineRepository.countByNameAndId(machine.getName(), id);
        EntityUtil.isNotDuplicate(count == 0, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));

        count = this.machineRepository.countByMacAndIpAndId(machine.getMac(), machine.getIp(), id);
        EntityUtil.isNotDuplicate(count == 0, "“" + machine.getIp() + "-" + machine.getMac() + "”已存在。");

        count = this.machineRepository.countByIpAndStatusAndId(machine.getIp(), ValidStatus.ENABLED.getId(), id);
        EntityUtil.isNotDuplicate(count == 0 || machine.isDisabled(), "机器IP“" + machine.getIp() + "”已启用。");

        count = this.machineRepository.countByMacAndStatusAndId(machine.getMac(), ValidStatus.ENABLED.getId(), id);
        EntityUtil.isNotDuplicate(count == 0 || machine.isDisabled(), "机器Mac“" + machine.getMac() + "”已启用。");
    }

    @Override
    @Transactional
    public Machine saveMachine(Machine machine) {
        Assert.notNull(machine, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));

        machine.checkConstraints();

        this.commonDomainService.loadAndFillinProperties(machine);
        checkMachineConstraints(machine);
        return this.machineRepository.save(machine);
    }

    @Override
    public Machine loadMachine(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.machineRepository.findOne(id);
    }

    @Override
    @Transactional
    public void updateMachinesStatus(List<String> ids, Integer status) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        Assert.notNull(status, MessageSourceContext.getMessage(MessageConstants.STATUS_NOT_BLANK));

        List<Machine> machines = this.machineRepository.findAll(ids);
        Integer count;
        for (Machine machine : machines) {
            if (status.equals(ValidStatus.ENABLED.getId())) {
                count = this.machineRepository.countByIpAndStatusAndId(machine.getIp(), ValidStatus.ENABLED.getId(), machine.getId());
                EntityUtil.isNotDuplicate(count == 0, "机器IP“" + machine.getIp() + "”已启用。");

                count = this.machineRepository.countByMacAndStatusAndId(machine.getMac(), ValidStatus.ENABLED.getId(), machine.getId());
                EntityUtil.isNotDuplicate(count == 0, "机器Mac“" + machine.getMac() + "”已启用。");
            }
        }
        this.commonDomainService.updateStatus(Machine.class, ids, status);
    }

    @Override
    @Transactional
    public void deleteMachines(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        List<Machine> machines = this.machineRepository.findAll(ids);
        long count;
        for (Machine item : machines) {
            count = this.personLoginLimitRepository.countByMachineId(item.getId());
            EntityUtil.IsNotReferenced(count == 0L, MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, item.getName(), "登录限制"));
        }
        this.machineRepository.delete(machines);
    }

    @Override
    public Map<String, Object> sliceQueryMachines(MachinesQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "machine");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Machine queryMachineByMac(String mac) {
        Assert.hasText(mac, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "mac"));
        return this.machineRepository.findByMacAndStatus(mac, ValidStatus.ENABLED.getId());
    }

    @Override
    public Machine queryMachineByIP(String ip) {
        Assert.hasText(ip, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "ip"));
        return this.machineRepository.findByIpAndStatus(ip, ValidStatus.ENABLED.getId());
    }
}