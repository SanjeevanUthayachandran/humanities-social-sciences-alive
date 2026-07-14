package com.hssa.contact.specification;

import com.hssa.contact.entity.ContactMessage;
import com.hssa.contact.enums.MessageStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ContactSpecification {

    public static Specification<ContactMessage> filterBy(
            String status, String priority, String inquiryType, String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), MessageStatus.valueOf(status.toUpperCase())));
            }
            if (priority != null && !priority.isBlank()) {
                predicates.add(cb.equal(root.get("priority"), priority.toUpperCase()));
            }
            if (inquiryType != null && !inquiryType.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("inquiryType")), inquiryType.toLowerCase()));
            }
            if (keyword != null && !keyword.isBlank()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";
                Predicate subjectMatch = cb.like(cb.lower(root.get("subject")), searchPattern);
                Predicate messageMatch = cb.like(cb.lower(root.get("message")), searchPattern);
                Predicate nameMatch = cb.like(cb.lower(root.get("fullName")), searchPattern);
                Predicate emailMatch = cb.like(cb.lower(root.get("email")), searchPattern);
                predicates.add(cb.or(subjectMatch, messageMatch, nameMatch, emailMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}