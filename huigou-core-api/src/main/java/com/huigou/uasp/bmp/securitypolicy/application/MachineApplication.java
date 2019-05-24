package com.huigou.uasp.bmp.securitypolicy.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.bmp.securitypolicy.domain.query.MachinesQueryRequest;

/**
 * 机器接口定义
 * 
 * @author yuanwf
 */
public interface MachineApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/securitypolicy.xml";

    /**
     * 保存机器
     * 
     * @param machine
     *            机器实体
     * @return 机器实体
     * @throws IllegalArgumentException
     *             如果属性编码、名称、状态、ip、mac一项为空。
     * @throws PropertyDuplicateException
     *             <ol>
     *             <li>如果编码重复。</li>
     *             <li>如果名称重复。</li>
     *             <li>如果ip地址与mac地址组合重复。</li>
     *             <li>如果启用的ip地址重复。</li>
     *             <li>如果启用的mac地址重复。</li>
     *             </ol>
     */
    Machine saveMachine(Machine machine);

    /**
     * 加载机器
     * 
     * @param id
     *            机器
     * @return 机器实体
     * @throws IllegalArgumentException
     *             如果id为空。
     */
    Machine loadMachine(String id);

    /**
     * 修改机器状态
     * 
     * @param ids
     *            机器ID列表
     * @param status
     *            状态
     * @throws IllegalArgumentException
     *             如果ids或status为空。
     * @throws PropertyDuplicateException
     *             如果启用的ip地址重复或启用的mac地址重复。
     */
    void updateMachinesStatus(List<String> ids, Integer status);

    /**
     * 删除机器
     * 
     * @param ids
     *            机器ID列表
     * @throws IllegalArgumentException
     *             如果ids为空。
     * @throws EntityIsReferencedException
     *             如果机器被人员登录限制引用。
     */
    void deleteMachines(List<String> ids);

    /**
     * 分页查询机器
     * 
     * @param queryRequest
     *            查询模型
     * @return 分页查询的机器
     */
    Map<String, Object> sliceQueryMachines(MachinesQueryRequest queryRequest);

    /**
     * 根据机器MAC地址查询机器
     * 
     * @param mac
     *            MAC地址
     * @return 机器实体
     */
    Machine queryMachineByMac(String mac);

    /**
     * 根据机器IP查询机器
     * 
     * @param mac
     *            IP地址
     * @return 机器实体
     */
    Machine queryMachineByIP(String ip);
}
