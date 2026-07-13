package com.hssa.publication.specification;

import com.hssa.publication.entity.Publication;
import com.hssa.publication.enums.PublicationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PublicationSpecification {

    public static Specification<Publication> filterBy(String title, String status, String doi) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (title != null && !title.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), PublicationStatus.valueOf(status.trim().toUpperCase())));
            }

            if (doi != null && !doi.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("doi"), doi.trim()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
