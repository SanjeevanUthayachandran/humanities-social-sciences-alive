package com.hssa.workshop.specification;

import com.hssa.workshop.entity.Workshop;
import com.hssa.workshop.enums.EventStatus;
import com.hssa.workshop.enums.EventType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WorkshopSpecification {

    public static Specification<Workshop> filterBy(
            String title, String status, String eventType, String locationType, Boolean isFree, Boolean isFeatured) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), EventStatus.valueOf(status.toUpperCase())));
            }
            if (eventType != null && !eventType.isBlank()) {
                predicates.add(cb.equal(root.get("eventType"), EventType.valueOf(eventType.toUpperCase())));
            }
            if (locationType != null && !locationType.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("locationType")), locationType.toLowerCase()));
            }
            if (isFree != null) {
                predicates.add(cb.equal(root.get("isFree"), isFree));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
