package com.huigou.uasp.log.domain.view;


import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.huigou.uasp.log.domain.model.HistoricSession;
import com.huigou.util.StringUtil;

public class HistoricSessionSpecification implements Specification<HistoricSession> {

    private String fullId, personName, ip;

    private Date beginDate, endDate;

    public HistoricSessionSpecification(String fullId, String personName, Date beginDate, Date endDate, String ip) {
        this.fullId = fullId;
        this.personName = personName;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.ip = ip;
    }

    @Override
    public Predicate toPredicate(Root<HistoricSession> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Predicate predicate = cb.conjunction();

        Path<String> fullIdPath = root.get("fullId");
        Path<String> personMemberNamePath = root.get("personMemberName");
        Path<Date> loginDatePath = root.get("loginDate");
        Path<String> clientIpPath = root.get("clientIp");

        if (StringUtil.isNotBlank(fullId)) {
            predicate.getExpressions().add(cb.like(fullIdPath, String.format("%s%%", fullId)));
        }

        if (StringUtil.isNotBlank(personName)) {
            predicate.getExpressions().add(cb.like(personMemberNamePath, String.format("%%%s%%", personName)));
        }

        if (beginDate != null) {
            predicate.getExpressions().add(cb.greaterThanOrEqualTo(loginDatePath, beginDate));
        }

        if (endDate != null) {
            predicate.getExpressions().add(cb.greaterThanOrEqualTo(loginDatePath, endDate));
        }

        if (StringUtil.isNotBlank(ip)) {
            predicate.getExpressions().add(cb.like(clientIpPath, String.format("%%%s%%", ip)));
        }
        return predicate;
    }

}
