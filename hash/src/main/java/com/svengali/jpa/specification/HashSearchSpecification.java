package com.svengali.jpa.specification;

import com.svengali.dto.HashSearchDTO;
import com.svengali.jpa.models.Hash;
import com.svengali.jpa.models.Hash_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class HashSearchSpecification implements Specification<Hash> {
    private final HashSearchDTO dto;

    @Override
    public Predicate toPredicate(Root<Hash> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (dto.isUsed != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(root.get(Hash_.IS_USED), dto.isUsed)
            );
        }

        if (dto.hash != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(root.get(Hash_.HASH), dto.hash)
            );
        }
        return predicate;
    }
}
