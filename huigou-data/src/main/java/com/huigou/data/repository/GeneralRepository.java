package com.huigou.data.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Transactional("transactionManager")
public class GeneralRepository extends GeneralRepositorySuper {

    @PersistenceContext(unitName="system")
    @Qualifier(value = "entityManagerFactory")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
