package com.montage.common.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.montage.common.dto.FilterCriteria;

public class GenericSpecificationBuilder<T> {

    private final List<FilterCriteria> filterCriteria;

    public GenericSpecificationBuilder(List<FilterCriteria> filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public Specification<T> build() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (FilterCriteria criteria : filterCriteria) {
                switch (criteria.getOperator().toLowerCase()) {
                    case "eq":
                        predicates.add(builder.equal(root.get(criteria.getField()), 
                            criteria.getValue()));
                        break;
                    case "ne":
                        predicates.add(builder.notEqual(root.get(criteria.getField()), 
                            criteria.getValue()));
                        break;
                    case "gt":
                        predicates.add(builder.greaterThan(root.get(criteria.getField()), 
                            (Comparable) criteria.getValue()));
                        break;
                    case "lt":
                        predicates.add(builder.lessThan(root.get(criteria.getField()), 
                            (Comparable) criteria.getValue()));
                        break;
                    case "ge":
                        predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getField()), 
                            (Comparable) criteria.getValue()));
                        break;
                    case "le":
                        predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getField()), 
                            (Comparable) criteria.getValue()));
                        break;
                    case "like":
                        predicates.add(builder.like(root.get(criteria.getField()), 
                            "%" + criteria.getValue() + "%"));
                        break;
                    case "in":
                        predicates.add(root.get(criteria.getField()).in(criteria.getValue()));
                        break;
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
} 