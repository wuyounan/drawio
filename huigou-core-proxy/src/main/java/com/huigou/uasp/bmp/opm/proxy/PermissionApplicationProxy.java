package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.model.TreeEntity;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.repository.GeneralRepository;
import com.huigou.uasp.bmp.opm.application.PermissionApplication;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;
import com.huigou.uasp.bmp.opm.impl.PermissionApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.PermissionRepository;

@Service("permissionApplicationProxy")
public class PermissionApplicationProxy {

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private PermissionApplication permissionApplication;
    
    void initProperties(PermissionApplicationImpl permissionApplicationImpl){
        permissionApplicationImpl.setCommonDomainService(commonDomainService);
        permissionApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        permissionApplicationImpl.setGeneralRepository(generalRepository);
        permissionApplicationImpl.setPermissionRepository(permissionRepository);
    }

    public PermissionApplication getPermissionApplication() {
        if (permissionApplication == null) {
           // synchronized (PermissionApplicationProxy.class) {
                if (permissionApplication == null) {
                    PermissionApplicationImpl permissionApplicationImpl = coreApplicationFactory.getPermissionApplication();

                    

                    this.permissionApplication = permissionApplicationImpl;
                }
           // }
        }
        return permissionApplication;
    }

    /**
     * 创建新权限
     * 
     * @param treeEntitys
     */
    @Transactional
    public void buildPermission(List<? extends TreeEntity> treeEntitys, PermissionResourceKind resourceKind) {
        getPermissionApplication().buildPermission(treeEntitys, resourceKind);
    }

    /**
     * 修改权限
     * 
     * @param treeEntity
     * @param oldName
     * @param oldFullName
     */
    @Transactional
    public void updatePermission(TreeEntity treeEntity, String oldName, String oldFullName, PermissionResourceKind resourceKind) {
        getPermissionApplication().updatePermission(treeEntity, oldName, oldFullName, resourceKind);
    }

    public Integer countByResourceId(String resourceId) {
        return getPermissionApplication().countByResourceId(resourceId);
    }

    /**
     * 移动权限
     * 
     * @param parentResourceId
     * @param resourceIds
     * @param resourceKind
     */
    @Transactional
    public void movePermission(String parentResourceId, List<String> resourceIds, PermissionResourceKind resourceKind) {
        getPermissionApplication().movePermission(parentResourceId, resourceIds, resourceKind);
    }

}
