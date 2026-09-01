package com.academy.shop.model.repository.specification;

import com.academy.shop.model.entity.Item;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ItemSpecification implements Specification<Item> {
    private final SearchCriteria criteria;

    public ItemSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public @Nullable Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getItemName() != null) {
            predicates.add(cb.like(root.get("itemName").as(String.class), "%" + criteria.getItemName() + "%"));
        }
        if (criteria.getInStock() != null) {
            predicates.add(cb.equal(root.get("inStock").as(Boolean.class), criteria.getInStock()));
        }
        if (criteria.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
        }
        if (criteria.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
        }
        if (criteria.getSeller() != null && !criteria.getSeller().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("seller")), criteria.getSeller()));
        }
        if (criteria.getMinArrivalDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("arrivalDate"), criteria.getMinArrivalDate()));
        }
        if (criteria.getMaxArrivalDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("arrivalDate"), criteria.getMaxArrivalDate()));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
