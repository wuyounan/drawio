package com.huigou.uasp.bmp.opm.repository.org;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.util.StringUtil;

public class OrgTemplateSpecification<OrgTemplate> implements Specification<OrgTemplate> {

    private String parentId;

    private String code;

    private String name;

    public OrgTemplateSpecification(String parentId, String code, String name) {
        this.parentId = parentId;
        this.code = code;
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<OrgTemplate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicate = new ArrayList<>();

        Join<OrgTemplate, OrgType> orgType = root.join("orgType", JoinType.INNER);
        predicate.add(cb.equal(root.get("parentId"), parentId));

        if (!StringUtil.isBlank(code)) {
            predicate.add(cb.like(orgType.get("code").as(String.class), String.format("%%%s%%", code)));
        }

        if (!StringUtil.isBlank(name)) {
            predicate.add(cb.like(orgType.get("name").as(String.class), String.format("%%%s%%", name)));
        }

        Predicate[] pre = new Predicate[predicate.size()];
        query.orderBy(cb.asc(root.get("sequence").as(Integer.class)));

        return query.where(predicate.toArray(pre)).getRestriction();
    }

}
