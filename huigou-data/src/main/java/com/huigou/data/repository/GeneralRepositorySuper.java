/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huigou.data.repository;

import static org.springframework.data.jpa.repository.query.QueryUtils.COUNT_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.applyAndBind;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link org.springframework.data.repository.CrudRepository} interface. This
 * will offer you a more sophisticated interface than the plain {@link EntityManager} .
 * 
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @param <T>
 *            the type of the entity to handle
 * @param <ID>
 *            the type of the entity's identifier
 */
@Transactional()
abstract public class GeneralRepositorySuper {

    abstract public EntityManager getEntityManager();

    private static final String ID_MUST_NOT_BE_NULL = "ID不能为空。";

    public static final String EXISTS_QUERY_STRING = "select count(%s) from %s x where x.%s = :id";

    // private final PersistenceProvider provider = PersistenceProvider.fromEntityManager(em);

    // private LockMetadataProvider lockMetadataProvider;

    public CriteriaBuilder getCriteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    public <T> Query createQuery(CriteriaQuery<T> cq) {
        return getEntityManager().createQuery(cq);
    }

    public <T> List<T> queryByCriteriaQuery(CriteriaQuery<T> cq) {
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Configures a custom {@link LockMetadataProvider} to be used to detect {@link LockModeType}s to be applied to queries.
     * 
     * @param lockMetadataProvider
     */
    /*
     * public void setLockMetadataProvider(LockMetadataProvider lockMetadataProvider) {
     * this.lockMetadataProvider = lockMetadataProvider;
     * }
     */
    private String getDeleteAllQueryString(Class<?> domainClass) {
        return getQueryString(DELETE_ALL_QUERY_STRING, JpaEntityInformationSupport.getEntityInformation(domainClass, getEntityManager()).getEntityName());
    }

    private String getCountQueryString(Class<?> domainClass) {
        PersistenceProvider provider = PersistenceProvider.fromEntityManager(getEntityManager());
        String countQuery = String.format(COUNT_QUERY_STRING, provider.getCountQueryPlaceholder(), "%s");
        return getQueryString(countQuery, JpaEntityInformationSupport.getEntityInformation(domainClass, getEntityManager()).getEntityName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.io.
     * Serializable)
     */
    @Transactional
    public <T, ID extends Serializable> void delete(Class<T> domainClass, ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!exists(domainClass, id)) {
            throw new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!",
                                                                   JpaEntityInformationSupport.getEntityInformation(domainClass, this.getEntityManager())
                                                                                              .getJavaType(), id), 1);
        }

        delete(findOne(domainClass, id));
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.repository.CrudRepository#delete(java.lang.Object
     * )
     */
    @Transactional
    public <T> void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        getEntityManager().remove(getEntityManager().contains(entity) ? entity : getEntityManager().merge(entity));
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable
     * )
     */
    @Transactional
    public <T> void deleteIterable(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        for (Object entity : entities) {
            delete(entity);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaRepository#deleteInBatch(java
     * .lang.Iterable)
     */
    @Transactional
    public <T> void deleteInBatch(Class<T> domainClass, Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        if (!entities.iterator().hasNext()) {
            return;
        }
        applyAndBind(
                     getQueryString(DELETE_ALL_QUERY_STRING, JpaEntityInformationSupport.getEntityInformation(domainClass, getEntityManager()).getEntityName()),
                     entities, getEntityManager()).executeUpdate();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.Repository#deleteAll()
     */
    @Transactional
    public <T> void deleteAll(Class<T> domainClass) {
        for (T element : findAll(domainClass)) {
            delete(element);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaRepository#deleteAllInBatch()
     */
    @Transactional
    public <T> void deleteAllInBatch(Class<T> domainClass) {
        getEntityManager().createQuery(getDeleteAllQueryString(domainClass)).executeUpdate();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.repository.Repository#readById(java.io.Serializable
     * )
     */
    public <T, ID extends Serializable> T findOne(Class<T> domainClass, ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return getEntityManager().find(domainClass, id);
    }

    /*
     * public <T, ID extends Serializable> T findOneWidthDetach(Class<T> domainClass, ID id) {
     * Assert.notNull(id, "The given id must not be null!");
     * T data = getEntityManager().find(domainClass, id);
     * getEntityManager().detach(data);
     * return data;
     * }
     */

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#exists(java.io.
     * Serializable)
     */
    public <T, ID extends Serializable> boolean exists(Class<T> domainClass, ID id) {

        Assert.notNull(id, "The given id must not be null!");
        JpaEntityInformation<T, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, getEntityManager());
        PersistenceProvider provider = PersistenceProvider.fromEntityManager(getEntityManager());
        if (entityInformation.getIdAttribute() != null) {

            String placeholder = provider.getCountQueryPlaceholder();
            String entityName = entityInformation.getEntityName();
            String idAttributeName = entityInformation.getIdAttribute().getName();
            String existsQuery = String.format(EXISTS_QUERY_STRING, placeholder, entityName, idAttributeName);

            TypedQuery<Long> query = getEntityManager().createQuery(existsQuery, Long.class);
            query.setParameter("id", id);

            return query.getSingleResult() == 1;
        } else {
            return findOne(domainClass, id) != null;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#findAll()
     */
    public <T> List<T> findAll(Class<T> domainClass) {
        return getQuery(domainClass, null, (Sort) null).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll(ID[])
     */
    public <T, ID extends Serializable> List<T> findAll(Class<T> domainClass, Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) return new ArrayList<T>();
        final Class<T> t = domainClass;
        return getQuery(domainClass, new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<?> path = root.get(JpaEntityInformationSupport.getEntityInformation(t, getEntityManager()).getIdAttribute());
                return path.in(cb.parameter(List.class, "ids"));
            }
        }, (Sort) null).setParameter("ids", ids).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#findAll(org.
     * springframework.data.domain.Sort)
     */
    public <T> List<T> findAll(Class<T> domainClass, Sort sort) {
        return getQuery(domainClass, null, sort).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.repository.PagingAndSortingRepository#findAll
     * (org.springframework.data.domain.Pageable)
     */
    public <T> Page<T> findAll(Class<T> domainClass, Pageable pageable) {
        if (null == pageable) {
            return new PageImpl<T>(findAll(domainClass));
        }

        return findAll(domainClass, null, pageable);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findOne
     * (org.springframework.data.jpa.domain.Specification)
     */
    public <T> T findOne(Class<T> domainClass, Specification<T> spec) {

        try {
            return getQuery(domainClass, spec, (Sort) null).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll
     * (org.springframework.data.jpa.domain.Specification)
     */
    public <T> List<T> findAll(Class<T> domainClass, Specification<T> spec) {
        return getQuery(domainClass, spec, (Sort) null).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll
     * (org.springframework.data.jpa.domain.Specification,
     * org.springframework.data.domain.Pageable)
     */
    public <T> Page<T> findAll(Class<T> domainClass, Specification<T> spec, Pageable pageable) {

        TypedQuery<T> query = getQuery(domainClass, spec, pageable);
        return pageable == null ? new PageImpl<T>(query.getResultList()) : readPage(domainClass, query, pageable, spec);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll
     * (org.springframework.data.jpa.domain.Specification,
     * org.springframework.data.domain.Sort)
     */
    public <T> List<T> findAll(Class<T> domainClass, Specification<T> spec, Sort sort) {

        return getQuery(domainClass, spec, sort).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#count()
     */
    public <T> long count(Class<T> domainClass) {
        return getEntityManager().createQuery(getCountQueryString(domainClass), Long.class).getSingleResult();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaSpecificationExecutor#count
     * (org.springframework.data.jpa.domain.Specification)
     */
    public <T> long count(Class<T> domainClass, Specification<T> spec) {

        return getCountQuery(domainClass, spec).getSingleResult();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.repository.CrudRepository#save(java.lang.Object)
     */
    // @Autowired private OptimisticLockExceptionDispatcher lockExceptionDispatcher;

    @SuppressWarnings("unchecked")
    @Transactional
    public <T> T save(T entity) {
        if (JpaEntityInformationSupport.getEntityInformation((Class<T>) entity.getClass(), getEntityManager()).isNew(entity)) {
            getEntityManager().persist(entity);
            return entity;
        } else {
            return getEntityManager().merge(entity);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaRepository#saveAndFlush(java
     * .lang.Object)
     */
    @Transactional
    public <T> T saveAndFlush(T entity) {
        T result = save(entity);
        flush();
        return result;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.data.jpa.repository.JpaRepository#save(java.lang.
     * Iterable)
     */
    @Transactional
    public <T> List<T> save(Iterable<T> entities) {
        List<T> result = new ArrayList<T>();
        if (entities == null) {
            return result;
        }

        for (T entity : entities) {
            result.add(save(entity));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#flush()
     */
    @Transactional
    public void flush() {
        getEntityManager().flush();
    }

    /**
     * Reads the given {@link TypedQuery} into a {@link Page} applying the given {@link Pageable} and {@link Specification}.
     * 
     * @param query
     *            must not be {@literal null}.
     * @param spec
     *            can be {@literal null}.
     * @param pageable
     *            can be {@literal null}.
     * @return
     */
    private <T> Page<T> readPage(Class<T> domainClass, TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        Long total = getCountQuery(domainClass, spec).getSingleResult();
        List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();

        return new PageImpl<T>(content, pageable, total);
    }

    /**
     * Creates a new {@link TypedQuery} from the given {@link Specification}.
     * 
     * @param spec
     *            can be {@literal null}.
     * @param pageable
     *            can be {@literal null}.
     * @return
     */
    private <T> TypedQuery<T> getQuery(Class<T> domainClass, Specification<T> spec, Pageable pageable) {
        Sort sort = pageable == null ? null : pageable.getSort();
        return getQuery(domainClass, spec, sort);
    }

    /**
     * Creates a {@link TypedQuery} for the given {@link Specification} and {@link Sort}.
     * 
     * @param spec
     *            can be {@literal null}.
     * @param sort
     *            can be {@literal null}.
     * @return
     */
    private <T> TypedQuery<T> getQuery(Class<T> domainClass, Specification<T> spec, Sort sort) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(domainClass);

        Root<T> root = applySpecificationToCriteria(domainClass, spec, query);
        query.select(root);

        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }

        return applyLockMode(getEntityManager().createQuery(query));
    }

    /**
     * Creates a new count query for the given {@link Specification}.
     * 
     * @param spec
     *            can be {@literal null}.
     * @return
     */
    private <T> TypedQuery<Long> getCountQuery(Class<T> domainClass, Specification<T> spec) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<T> root = applySpecificationToCriteria(domainClass, spec, query);
        query.select(builder.count(root));

        return getEntityManager().createQuery(query);
    }

    /**
     * Applies the given {@link Specification} to the given {@link CriteriaQuery}.
     * 
     * @param spec
     *            can be {@literal null}.
     * @param query
     *            must not be {@literal null}.
     * @return
     */
    private <T, S> Root<T> applySpecificationToCriteria(Class<T> domainClass, Specification<T> spec, CriteriaQuery<S> query) {

        Assert.notNull(query);
        Root<T> root = query.from(domainClass);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    private <T> TypedQuery<T> applyLockMode(TypedQuery<T> query) {
        // LockModeType type = lockMetadataProvider == null ? null : lockMetadataProvider.getLockModeType();
        LockModeType type = null;
        return type == null ? query : query.setLockMode(type);
    }

    @SuppressWarnings("rawtypes")
    public List query(String hql) {
        return getEntityManager().createQuery(hql).getResultList();
    }

    @SuppressWarnings("rawtypes")
    public List query(String hql, Map<String, Object> parameterMap) {
        Query query = getEntityManager().createQuery(hql);
        for (String key : parameterMap.keySet()) {
            query.setParameter(key, parameterMap.get(key));
        }
        return query.getResultList();
    }

    @SuppressWarnings("rawtypes")
    public List query(String hql, Map<String, Object> parameterMap, Sort sort) {
        if (!hql.contains(" order by ")) {
            hql += buildOrderBy(sort);
        }

        Query query = getEntityManager().createQuery(hql);
        for (String key : parameterMap.keySet()) {
            query.setParameter(key, parameterMap.get(key));
        }
        return query.getResultList();
    }

    public <T> Page<T> query(String hql, Pageable pageable, long total) {
        if (!hql.contains(" order by ")) {
            hql += buildOrderBy(pageable);
        }
        Query query = getEntityManager().createQuery(hql);
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Long total =
        // (Long)getEntityManager().createQuery("select count(*) from (" + hql +
        // ")").getSingleResult();
        @SuppressWarnings("unchecked")
        List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.emptyList();

        return new PageImpl<T>(content, pageable, total);
    }

    public <T> Page<T> query(String hql, Map<String, Object> parameterMap, Pageable pageable, long total) {
        if (!hql.contains(" order by ")) {
            hql += buildOrderBy(pageable);
        }
        Query query = getEntityManager().createQuery(hql);
        for (String key : parameterMap.keySet()) {
            query.setParameter(key, parameterMap.get(key));
        }

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.emptyList();

        return new PageImpl<T>(content, pageable, total);
    }

    @Transactional
    public List<?> hqlQuery(String hql, int offset, int pageSize) {
        Query query = getEntityManager().createQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        Long total = (Long) single(buildCountHql(hql));
        List<?> content = total > offset ? query.getResultList() : Collections.emptyList();
        return content;
    }

    @Transactional
    public Long hqlQueryTotal(String hql) {
        Long total = (Long) single(buildCountHql(hql));
        return total;
    }

    /**
     * 构建统计数量HQL
     * 
     * @param hql
     *            hql
     * @return
     */
    private String buildCountHql(String hql) {
        StringBuilder sb = new StringBuilder("select count(*) ");

        int index = hql.indexOf("from");
        sb.append(hql.substring(index));

        return sb.toString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Page query(String hql, Map<String, Object> ps, Pageable pageable) {
        if (!hql.contains(" order by ")) {
            hql += buildOrderBy(pageable);
        }

        Query q = getEntityManager().createQuery(hql);
        for (String k : ps.keySet()) {
            q.setParameter(k, ps.get(k));
        }
        q.setFirstResult(pageable.getOffset());
        q.setMaxResults(pageable.getPageSize());
        Query qt = getEntityManager().createQuery(buildCountHql(hql));
        for (String k : ps.keySet()) {
            qt.setParameter(k, ps.get(k));
        }
        Long total = (Long) qt.getSingleResult();
        List<?> content = (total.longValue() > pageable.getOffset()) ? q.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Page query(String hql, Pageable pageable) {
        if (!hql.contains(" order by ")) {
            hql += buildOrderBy(pageable);
        }
        Query query = getEntityManager().createQuery(hql);
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        Long total = (Long) getEntityManager().createQuery(buildCountHql(hql)).getSingleResult();
        List content = total > pageable.getOffset() ? query.getResultList() : Collections.emptyList();

        return new PageImpl(content, pageable, total);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Page<?> queryByNativeSql(String sql, Map<String, Object> ps, Pageable pageable) {
        Query query = getEntityManager().createNativeQuery(sql);

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        for (String k : ps.keySet())
            query.setParameter(k, ps.get(k));

        Query totalQuery = getEntityManager().createNativeQuery("select count(*) from (" + sql + ") ");
        for (String k : ps.keySet())
            totalQuery.setParameter(k, ps.get(k));
        Long total = ((BigDecimal) totalQuery.getSingleResult()).longValue();

        List<?> content = total > pageable.getOffset() ? query.getResultList() : Collections.emptyList();

        return new PageImpl(content, pageable, total);
    }

    public List<?> queryByNativeSql(String sql, Map<String, Object> ps, int start, int maxResult) {
        Query query = getEntityManager().createNativeQuery(sql);
        query.setFirstResult(start);
        query.setMaxResults(maxResult);
        for (String k : ps.keySet()) {
            query.setParameter(k, ps.get(k));
        }
        return query.getResultList();
    }

    public long coungByNativeSql(String sql, Map<String, Object> ps) {
        Query query = getEntityManager().createNativeQuery(sql);

        if (ps != null) {
            for (String k : ps.keySet()) {
                query.setParameter(k, ps.get(k));
            }
        }

        Number result = (Number) query.getSingleResult();

        return (long) result.longValue();
    }

    public List<?> queryByNativeSql(String sql, Map<String, Object> ps) {
        Query query = getEntityManager().createNativeQuery(sql);

        for (String k : ps.keySet()) {
            query.setParameter(k, ps.get(k));
        }

        return query.getResultList();
    }

    /**
     * 使用本地SQL更新数据
     * 
     * @param sql
     *            SQL
     * @param parameterMap
     *            参数
     * @return
     *         更新或删除的记录数
     */
    public int updateByNativeSql(String sql, Map<String, Object> parameterMap) {
        Query query = this.getEntityManager().createNativeQuery(sql);

        for (String key : parameterMap.keySet()) {
            query.setParameter(key, parameterMap.get(key));
        }

        return query.executeUpdate();
    }

    private String buildOrderBy(Sort sort) {
        if (sort == null) {
            return "";
        }

        String orderBy = null;
        Iterator<Order> it = sort.iterator();
        while (it.hasNext()) {
            Order order = it.next();
            String o = order.getProperty() + " " + order.getDirection().toString();
            if (orderBy == null) {
                orderBy = " order by " + o + " ";
            } else {
                orderBy += o;
            }
            if (it.hasNext()) orderBy += ",";
        }
        orderBy = orderBy == null ? "" : orderBy;
        return orderBy;
    }

    private String buildOrderBy(Pageable page) {
        if (page.getSort() == null) {
            return "";
        }
        String orderBy = null;
        Iterator<Order> it = page.getSort().iterator();
        while (it.hasNext()) {
            Order order = it.next();
            String o = order.getProperty() + " " + order.getDirection().toString();
            if (orderBy == null) {
                orderBy = " order by " + o + " ";
            } else {
                orderBy += o;
            }
            if (it.hasNext()) orderBy += ",";
        }
        orderBy = orderBy == null ? "" : orderBy;
        return orderBy;
    }

    public List<?> query(String hql, int start, int maxResult) {
        Query query = getEntityManager().createQuery(hql);
        query.setFirstResult(start);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    public List<?> query(String hql, Map<String, Object> parameterMap, int start, int maxResult) {
        Query query = getEntityManager().createQuery(hql);
        for (String key : parameterMap.keySet()) {
            query.setParameter(key, parameterMap.get(key));
        }
        query.setFirstResult(start);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    public Object single(String hql) {
        return getEntityManager().createQuery(hql).getSingleResult();
    }

    public Object single(String hql, Map<String, Object> parameterMap) {
        Query query = getEntityManager().createQuery(hql);
        for (String key : parameterMap.keySet()) {
            query.setParameter(key, parameterMap.get(key));
        }
        return query.getSingleResult();
    }

    @Transactional
    public void update(String hql) {
        getEntityManager().createQuery(hql).executeUpdate();
    }

    @Transactional
    public void update(String jpql, Map<String, Object> params) {
        Query query = getEntityManager().createQuery(jpql);
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        query.executeUpdate();
    }

    private long getNextId(String sequenceName) {
        Query q = getEntityManager().createNativeQuery(String.format("SELECT %s.nextval from DUAL", sequenceName));
        BigDecimal result = (BigDecimal) q.getSingleResult();
        return result.longValue();
    }

    public long getVersionNextId() {
        return getNextId("version_seq");
    }

    public long getImportExcelNextId() {
        return getNextId("seq_imp_temp_table");
    }

    /**
     * 得到数据库系统时间
     * 
     * @return
     */
    public Date getDBSystemDateTime() {
        Query query = getEntityManager().createNativeQuery("select sysdate from dual");
        Timestamp result = (Timestamp) query.getSingleResult();
        return new Date(result.getTime());
    }

}
