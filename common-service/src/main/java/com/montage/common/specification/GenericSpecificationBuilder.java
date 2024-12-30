package com.montage.common.specification;

import com.montage.common.dto.FilterCriteria;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class GenericSpecificationBuilder<T> {
    private final List<FilterCriteria> filters;

    public GenericSpecificationBuilder(List<FilterCriteria> filters) {
        this.filters = filters;
    }

    public Specification<T> build() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (FilterCriteria filter : filters) {
                switch (filter.getOperator().toUpperCase()) {
                    case "EQ":
                        predicates.add(criteriaBuilder.equal(
                            root.get(filter.getField()), filter.getValue()));
                        break;
                    case "LIKE":
                        predicates.add(criteriaBuilder.like(
                            root.get(filter.getField()), "%" + filter.getValue() + "%"));
                        break;
                    case "GT":
                        predicates.add(criteriaBuilder.greaterThan(
                            root.get(filter.getField()), filter.getValue()));
                        break;
                    case "LT":
                        predicates.add(criteriaBuilder.lessThan(
                            root.get(filter.getField()), filter.getValue()));
                        break;
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
} 