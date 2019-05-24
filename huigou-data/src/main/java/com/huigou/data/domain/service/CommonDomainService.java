package com.huigou.data.domain.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.EntityUtil;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.BaseInfoAbstractEntity;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.FlowBillAbstractEntity;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.model.TreeEntity;
import com.huigou.data.domain.query.CheckBaseInfoDuplicateParameter;
import com.huigou.data.domain.query.QueryParameter;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.repository.GeneralRepositorySuper;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.util.StringUtil;

/**
 * 通用领域服务
 * 
 * @author gongmm
 */
public class CommonDomainService {
    private static final String COMMON_XML_FILE_PATH = "config/uasp/query/bmp/common.xml";

    protected GeneralRepositorySuper generalRepository;

    protected SQLExecutorDao sqlExecutorDao;

    public GeneralRepositorySuper getGeneralRepository() {
        return generalRepository;
    }

    public void setGeneralRepository(GeneralRepositorySuper generalRepository) {
        this.generalRepository = generalRepository;
    }

    public SQLExecutorDao getSqlExecutorDao() {
        return sqlExecutorDao;
    }

    public void setSqlExecutorDao(SQLExecutorDao sqlExecutorDao) {
        this.sqlExecutorDao = sqlExecutorDao;
    }

    public String getSqlByName(String name) {
        QueryDescriptor queryDescriptor = sqlExecutorDao.getQuery(COMMON_XML_FILE_PATH, "common");
        return queryDescriptor.getSqlByName(name);
    }

    /**
     * 获取下一个排序号
     * 
     * @param clazz
     *            实体类型
     * @return 下一个排序号
     */
    @Transactional
    public Integer getNextSequence(Class<? extends IdentifiedEntity> clazz) {
        return getNextSequence(clazz, null, null);
    }

    /**
     * 获取下一个排序号
     * 
     * @param clazz
     *            实体类型
     * @param parentIdFieldName
     *            父ID字段名称
     * @param parentId
     *            父ID
     * @return 下一个排序号
     */
    @Transactional
    public Integer getNextSequence(Class<? extends IdentifiedEntity> clazz, String parentIdFieldName, String parentId) {
        CriteriaBuilder cb = generalRepository.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<?> root = query.from(clazz);
        query.select(cb.max(root.<Integer> get(CommonDomainConstants.SEQUENCE_FIELD_NAME)));
        if (!StringUtil.isBlank(parentIdFieldName)) {
            query.where(cb.equal(root.get(parentIdFieldName), parentId));
        }
        Integer result = (Integer) generalRepository.createQuery(query).getSingleResult();
        return result == null ? 1 : ++result;
    }

    /**
     * 移动
     * 
     * @param clazz
     *            实体类型
     * @param parentIdFieldName
     *            父ID字段名称
     * @param parentId
     *            父ID
     * @param ids
     *            ID列表
     */
    @Transactional
    public void move(Class<? extends AbstractEntity> clazz, List<String> ids, String parentIdFieldName, String parentId) {
        Assert.notNull(clazz, MessageSourceContext.getMessage(MessageConstants.CLAZZ_NOT_NULL));
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Assert.hasText(parentIdFieldName, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_FIELD_NAME_NOT_BLANK));
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));

        String tableName = EntityUtil.getTableName(clazz);
        String sql = this.getSqlByName("moveSqlByParentId");
        sql = String.format(sql, tableName, parentIdFieldName);

        Map<String, Object> params = new HashMap<String, Object>(2);

        params.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
        params.put(CommonDomainConstants.IDS_FIELD_NAME, ids);

        this.generalRepository.updateByNativeSql(sql, params);
    }

    /**
     * 移动树
     * 
     * @param clazz
     *            实体类型
     * @param parentIdFieldName
     *            父ID字段名称
     * @param parentId
     *            父ID
     * @param ids
     *            ID列表
     */
    @SuppressWarnings("unchecked")
    public void moveForTree(Class<? extends TreeEntity> clazz, List<String> ids, String parentIdFieldName, String parentId) {
        Assert.notNull(clazz, MessageSourceContext.getMessage(MessageConstants.CLAZZ_NOT_NULL));
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Assert.hasText(parentIdFieldName, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_FIELD_NAME_NOT_BLANK));
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        String tableName = EntityUtil.getTableName(clazz);

        List<TreeEntity> treeEntities = (List<TreeEntity>) generalRepository.findAll(clazz, ids);
        TreeEntity moveToParent = generalRepository.findOne(clazz, parentId);
        String parentFullId = moveToParent.getFullId();
        // 1、更新fullId 和fullName
        String chlidFullId = null;
        TreeEntity oldParent;
        String sql = getSqlByName("updateFullIdAndName");
        for (TreeEntity treeEntity : treeEntities) {
            chlidFullId = treeEntity.getFullId();
            // 校验不能循环引用
            Assert.isTrue(parentFullId.indexOf(chlidFullId) == -1,
                          MessageSourceContext.getMessage(MessageConstants.UNABLE_TO_MOVE_TO, treeEntity.getName(), moveToParent.getName()));
            oldParent = generalRepository.findOne(clazz, treeEntity.getParentId());
            Map<String, Object> parameterMap = new HashMap<String, Object>(2);
            parameterMap.put("parentNewFullId", moveToParent.getFullId());
            parameterMap.put("parentOldFullId", oldParent.getFullId());

            parameterMap.put("parentNewFullName", moveToParent.getFullName());
            parameterMap.put("parentOldFullName", oldParent.getFullName());
            parameterMap.put("likeFullId", chlidFullId + "%");

            this.generalRepository.updateByNativeSql(String.format(sql, tableName), parameterMap);
        }
        // 2、移动
        sql = this.getSqlByName("moveSqlByParentId");
        sql = String.format(sql, tableName, parentIdFieldName);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
        map.put(CommonDomainConstants.IDS_FIELD_NAME, ids);
        this.generalRepository.updateByNativeSql(sql, map);
    }

    /**
     * 移动树
     * 
     * @param clazz
     * @param ids
     * @param parentId
     */
    @SuppressWarnings("unchecked")
    public void moveTree(Class<? extends TreeEntity> clazz, List<String> ids, String parentId) {
        Assert.notNull(clazz, MessageSourceContext.getMessage(MessageConstants.CLAZZ_NOT_NULL));
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));

        String tableName = EntityUtil.getTableName(clazz);
        TreeEntity moveToParent = generalRepository.findOne(clazz, parentId);
        Assert.notNull(moveToParent, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        String parentFullId = moveToParent.getFullId();
        List<TreeEntity> treeEntities = (List<TreeEntity>) generalRepository.findAll(clazz, ids);
        String chlidFullId = null;
        TreeEntity oldParent;
        String sql = getSqlByName("updateFullIdAndName");
        for (TreeEntity treeEntity : treeEntities) {
            chlidFullId = treeEntity.getFullId();
            // 校验不能循环引用
            Assert.isTrue(parentFullId.indexOf(chlidFullId) == -1,
                          MessageSourceContext.getMessage(MessageConstants.UNABLE_TO_MOVE_TO, treeEntity.getName(), moveToParent.getName()));
            oldParent = generalRepository.findOne(clazz, treeEntity.getParentId());
            Assert.notNull(oldParent, MessageSourceContext.getMessage(MessageConstants.UNABLE_TO_MOVE_TO, treeEntity.getName(), moveToParent.getName()));
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("parentNewFullId", parentFullId);
            param.put("parentOldFullId", oldParent.getFullId());
            param.put("parentNewFullName", moveToParent.getFullName());
            param.put("parentOldFullName", oldParent.getFullName());
            param.put("likeFullId", chlidFullId + "%");
            this.generalRepository.updateByNativeSql(String.format(sql, tableName), param);

        }
        sql = this.getSqlByName("moveSqlByParentId");
        sql = String.format(sql, tableName, CommonDomainConstants.PARENT_ID_COLUMN_NAME);
        Map<String, Object> parameterMap = new HashMap<String, Object>(2);
        parameterMap.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
        parameterMap.put(CommonDomainConstants.IDS_FIELD_NAME, ids);
        this.generalRepository.updateByNativeSql(sql, parameterMap);
    }

    /**
     * 移动
     * 
     * @param clazz
     *            移动实体类型
     * @param folderId
     *            文件夹
     * @param ids
     *            ID列表
     */
    @Transactional
    public void moveForFolder(Class<? extends AbstractEntity> clazz, List<String> ids, String folderId) {
        Assert.notNull(clazz, MessageSourceContext.getMessage(MessageConstants.CLAZZ_NOT_NULL));
        Assert.hasText(folderId, MessageSourceContext.getMessage(MessageConstants.FOLDER_ID_NOT_BLANK));
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));

        String tableName = EntityUtil.getTableName(clazz);
        String sql = this.getSqlByName("moveSqlByFolderId");
        sql = String.format(sql, tableName);

        Map<String, Object> parameterMap = new HashMap<String, Object>(2);
        parameterMap.put(CommonDomainConstants.FOLDER_ID_FIELD_NAME, folderId);
        parameterMap.put(CommonDomainConstants.IDS_FIELD_NAME, ids);

        this.generalRepository.updateByNativeSql(sql, parameterMap);
    }

    @SuppressWarnings("unchecked")
    public List<? extends IdentifiedEntity> findDuplicateEntities(Class<? extends IdentifiedEntity> clazz, CheckBaseInfoDuplicateParameter parameter) {
        Assert.notNull(clazz, MessageSourceContext.getMessage(MessageConstants.CLAZZ_NOT_NULL));
        Assert.notNull(parameter, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "parameter"));
        parameter.checkConstraints();
        String jpql = String.format(this.getSqlByName(parameter.getSqlName()), clazz.getSimpleName());
        if (parameter.isTenantFilter()) {
            jpql = String.format("%s and  %s = :tenantId", jpql, parameter.getTenantFieldName());
        }
        Map<String, Object> params = parameter.getQueryParams();
        return this.generalRepository.query(jpql, params);
    }

    public void updateSequence(Class<? extends AbstractEntity> clazz, Map<String, Integer> params) {
        Assert.notNull(clazz, MessageSourceContext.getMessage(MessageConstants.CLAZZ_NOT_NULL));
        Assert.notEmpty(params, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "params"));

        String tableName = EntityUtil.getTableName(clazz);
        String jpql = this.getSqlByName("updateSequenceSql");
        jpql = String.format(jpql, tableName);

        Map<String, Object> parameterMap = new HashMap<String, Object>(2);
        for (String key : params.keySet()) {
            parameterMap.put(CommonDomainConstants.ID_FIELD_NAME, key);
            parameterMap.put(CommonDomainConstants.SEQUENCE_FIELD_NAME, params.get(key));
            this.generalRepository.updateByNativeSql(jpql, parameterMap);
        }
    }

    public void updateStatus(Class<? extends AbstractEntity> clazz, List<String> ids, Integer status) {
        Assert.notNull(clazz, MessageSourceContext.getMessage(MessageConstants.CLAZZ_NOT_NULL));
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Assert.notNull(status, MessageSourceContext.getMessage(MessageConstants.STATUS_NOT_BLANK));

        String tableName = EntityUtil.getTableName(clazz);
        String jpql = this.getSqlByName("updateStatusesSql");
        jpql = String.format(jpql, tableName);

        Map<String, Object> parameterMap = new HashMap<String, Object>(2);
        parameterMap.put(CommonDomainConstants.STATUS_FIELD_NAME, status);
        parameterMap.put(CommonDomainConstants.IDS_FIELD_NAME, ids);

        this.generalRepository.updateByNativeSql(jpql, parameterMap);
    }

    public void updateStatus(Class<? extends AbstractEntity> clazz, String id, Integer status) {
        Assert.notNull(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Assert.notNull(status, MessageSourceContext.getMessage(MessageConstants.STATUS_NOT_BLANK));

        String tableName = EntityUtil.getTableName(clazz);
        String jpql = this.getSqlByName("updateStatusSql");
        jpql = String.format(jpql, tableName);

        Map<String, Object> parameterMap = new HashMap<String, Object>(2);
        parameterMap.put(CommonDomainConstants.STATUS_FIELD_NAME, status);
        parameterMap.put(CommonDomainConstants.ID_FIELD_NAME, id);

        this.generalRepository.updateByNativeSql(jpql, parameterMap);
    }

    public void updateChildenFullName(Class<? extends AbstractEntity> clazz, String fullId, String oldFullName, String newFullName) {
        String tableName = EntityUtil.getTableName(clazz);
        String jpql = getSqlByName("updateFullName");
        jpql = String.format(jpql, tableName);
        Map<String, Object> params = new HashMap<String, Object>(3);

        params.put("fullId", fullId + "/%");
        params.put("oldFullName", oldFullName);
        params.put("newFullName", newFullName);

        this.generalRepository.updateByNativeSql(jpql, params);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BaseInfoWithFolderAbstractEntity saveBaseInfoWithFolderEntity(BaseInfoWithFolderAbstractEntity entity, JpaRepository repository) {
        Assert.notNull(entity, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        Assert.notNull(repository, MessageSourceContext.getMessage(MessageConstants.REPOSITORY_NOT_NULL));

        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        if (entity.hasTenant()) {
            checkParameter.setCheckTenantInfo(entity.getTenantField_(), entity.getTenantId_());
        }
        checkParameter.setCheckFolderIdAndGlobalCodeAndName(entity.getFolderId(), entity.getId(), entity.getCode(), entity.getName());
        checkParameter.checkConstraints();

        List<BaseInfoWithFolderAbstractEntity> duplicateEntities = (List<BaseInfoWithFolderAbstractEntity>) findDuplicateEntities(entity.getClass(),
                                                                                                                                  checkParameter);
        BaseInfoWithFolderAbstractEntity other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }

        if (entity.isNew() && entity.getStatus() == null) {
            entity.setStatus(BaseInfoStatus.ENABLED.getId());
        }

        entity.checkConstraints(other);

        entity = (BaseInfoWithFolderAbstractEntity) repository.save(entity);
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FlowBillAbstractEntity saveFlowBillEntity(FlowBillAbstractEntity entity, JpaRepository repository) {
        Assert.notNull(entity, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        Assert.notNull(repository, MessageSourceContext.getMessage(MessageConstants.REPOSITORY_NOT_NULL));
        if (entity.isNew()) {
            String tableName = EntityUtil.getTableName(entity.getClass());
            String sql = this.getSqlByName("countByBillCodeSql");
            sql = String.format(sql, tableName);
            Integer count = this.sqlExecutorDao.queryToInt(sql, entity.getBillCode());
            Assert.isTrue(count == 0, MessageSourceContext.getMessage(MessageConstants.BILLCODE_NOT_DUPLICATE));
            if (entity.getStatusId() == null) {
                entity.setStatusId(0);
            }
        }
        entity = (FlowBillAbstractEntity) repository.save(entity);
        repository.flush();
        return entity;
    }

    @SuppressWarnings({ "rawtypes" })
    public TreeEntity saveTreeEntity(TreeEntity entity, JpaRepository repository) {
        return saveTreeEntity(entity, repository, true);
    }

    @SuppressWarnings({ "rawtypes" })
    public TreeEntity saveTreeEntity(TreeEntity entity, JpaRepository repository, boolean useDefaultCheck) {
        return saveTreeEntity(entity, repository, useDefaultCheck, true);
    }

    // public Integer getNextSequence(String entityName, String parentIdFieldName, String parentId) {
    // Integer result = 1;
    // String sql = getSqlByName("getMaxSequenceByParentId");
    // sql = String.format(sql, entityName, parentIdFieldName);
    // Number maxSequence = (Number) this.generalRepository.single(sql, QueryParameter.buildParameters(parentIdFieldName, parentId));
    // if (maxSequence != null) {
    // result = maxSequence.intValue() + 1;
    // }
    // return result;
    // }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TreeEntity saveTreeEntity(TreeEntity entity, JpaRepository repository, boolean useDefaultCheck, boolean globalCode) {
        Assert.notNull(entity, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "entity"));
        Assert.notNull(repository, MessageSourceContext.getMessage(MessageConstants.REPOSITORY_NOT_NULL));

        if (entity.getStatus() == null) {
            entity.setStatus(BaseInfoStatus.ENABLED.getId());
        }

        if (useDefaultCheck) {
            CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();

            if (entity.hasTenant()) {
                checkParameter.setCheckTenantInfo(entity.getTenantField_(), entity.getTenantId_());
            }
            if (globalCode) {
                checkParameter.setCheckParentIdAndGlobalCodeAndName(entity.getParentId(), entity.getId(), entity.getCode(), entity.getName());
            } else {
                checkParameter.setCheckParentIdAndCodeAndName(entity.getParentId(), entity.getId(), entity.getCode(), entity.getName());
            }
            checkParameter.checkConstraints();
            List<TreeEntity> duplicateEntities = (List<TreeEntity>) findDuplicateEntities(entity.getClass(), checkParameter);

            TreeEntity other = null;
            if (duplicateEntities.size() > 0) {
                other = duplicateEntities.get(0);
            }
            entity.checkConstraints(other);
        }

        TreeEntity parent = (TreeEntity) repository.findOne(entity.getParentId());
        if (entity.getSequence() == null) {
            Integer sequence = 0;
            if (parent != null) {
                String sql = getSqlByName("getMaxSequenceByParentId");
                sql = String.format(sql, entity.getClass().getSimpleName(), CommonDomainConstants.PARENT_ID_FIELD_NAME);
                Number maxSequence = (Number) this.generalRepository.single(sql,
                                                                            QueryParameter.buildParameters(CommonDomainConstants.PARENT_ID_FIELD_NAME,
                                                                                                           entity.getParentId()));
                if (maxSequence != null) {
                    sequence = maxSequence.intValue();
                }
            }
            entity.setSequence(sequence + 1);
        }
        // 先保存实体，获取ID
        entity = (TreeEntity) repository.save(entity);
        entity.buildFullIdAndName(parent);
        entity = (TreeEntity) repository.save(entity);

        return entity;
    }

    @SuppressWarnings("rawtypes")
    public TreeEntity saveTreeEntity(TreeEntity entity, JpaRepository repository, String oldName) {
        return saveTreeEntity(entity, repository, oldName, true);
    }

    @SuppressWarnings("rawtypes")
    public TreeEntity saveTreeEntity(TreeEntity entity, JpaRepository repository, String oldName, boolean useDefaultCheck) {
        return saveTreeEntity(entity, repository, oldName, useDefaultCheck, true);
    }

    @SuppressWarnings("rawtypes")
    public TreeEntity saveTreeEntity(TreeEntity entity, JpaRepository repository, String oldName, boolean useDefaultCheck, boolean globalCode) {
        Assert.notNull(entity, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        Assert.notNull(repository, MessageSourceContext.getMessage(MessageConstants.REPOSITORY_NOT_NULL));
        // Assert.hasText(oldName, "参数oldName不能为空。");

        boolean updatedName = entity.isUpdateName(oldName);
        String oldFullName = entity.getFullName();
        entity = this.saveTreeEntity(entity, repository, useDefaultCheck, globalCode);
        if (updatedName) {
            this.updateChildenFullName(entity.getClass(), entity.getFullId(), oldFullName, entity.getFullName());
        }
        return entity;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BaseInfoAbstractEntity saveBaseInfoEntity(BaseInfoAbstractEntity entity, JpaRepository repository) {
        Assert.notNull(entity, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));

        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        if (entity.hasTenant()) {
            checkParameter.setCheckTenantInfo(entity.getTenantField_(), entity.getTenantId_());
        }
        checkParameter.setCheckCodeAndName(entity.getId(), entity.getCode(), entity.getName());
        checkParameter.checkConstraints();
        List<BaseInfoAbstractEntity> duplicateEntities = (List<BaseInfoAbstractEntity>) this.findDuplicateEntities(entity.getClass(), checkParameter);

        BaseInfoAbstractEntity other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }

        if (StringUtil.isBlank(entity.getId()) && null == entity.getStatus()) {
            entity.setStatus(BaseInfoStatus.ENABLED.getId());
        }

        entity.checkConstraints(other);

        entity = (BaseInfoAbstractEntity) repository.save(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public void checkBaseInfoEntity(BaseInfoAbstractEntity entity) {
        Assert.notNull(entity, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        checkParameter.setCheckCodeAndName(entity.getId(), entity.getCode(), entity.getName());
        checkParameter.checkConstraints();

        List<BaseInfoAbstractEntity> duplicateEntities = (List<BaseInfoAbstractEntity>) findDuplicateEntities(entity.getClass(), checkParameter);
        BaseInfoAbstractEntity other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }
        entity.checkConstraints(other);
    }

    public AbstractEntity loadAndFillinProperties(AbstractEntity entity) {
        if (entity.isNew()) {
            return entity;
        }

        AbstractEntity result = (AbstractEntity) this.generalRepository.findOne(entity.getClass(), entity.getId());
        Assert.notNull(result,
                       MessageSourceContext.getMessage(MessageConstants.LOAD_OBJECT_IS_NULL)
                                       + MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, entity.getId(), entity.getClass().getName()));
        result.fromEntity(entity);
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T loadAndFillinProperties(AbstractEntity entity, Class<T> clazz) {
        if (entity.isNew()) {
            return (T) entity;
        }

        AbstractEntity result = (AbstractEntity) this.generalRepository.findOne(entity.getClass(), entity.getId());
        Assert.notNull(result,
                       MessageSourceContext.getMessage(MessageConstants.LOAD_OBJECT_IS_NULL)
                                       + MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, entity.getId(), entity.getClass().getName()));
        result.fromEntity(entity);
        return (T) result;
    }

    public Date getDBSystemDateTime() {
        return this.generalRepository.getDBSystemDateTime();
    }

    public String getCountByParentIdSql() {
        return getSqlByName("countByParentId");
    }

}
