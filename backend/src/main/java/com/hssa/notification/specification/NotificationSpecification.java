package com.hssa.notification.specification;

import com.hssa.notification.entity.Notification;
import com.hssa.notification.enums.NotificationStatus;
import com.hssa.notification.enums.NotificationType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationSpecification {

    public static Specification<Notification> filterBy(
            UUID recipientId, String type, String status, String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (recipientId != null) {
                predicates.add(cb.equal(root.get("recipient").get("id"), recipientId));
            }
            if (type != null && !type.isBlank()) {
                predicates.add(cb.equal(root.get("notificationType"), NotificationType.valueOf(type.toUpperCase())));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), NotificationStatus.valueOf(status.toUpperCase())));
            }
            if (keyword != null && !keyword.isBlank()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), searchPattern);
                Predicate msgMatch = cb.like(cb.lower(root.get("message")), searchPattern);
                predicates.add(cb.or(titleMatch, msgMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}