package com.hssa.certificate.specification;

import com.hssa.certificate.entity.Certificate;
import com.hssa.certificate.enums.CertificateStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public class CertificateSpecification {

    public static Specification<Certificate> filterBy(UUID studentId, UUID courseId, String certificateNumber, String status, LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (studentId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), studentId));
            }

            if (courseId != null) {
                predicates.add(cb.equal(root.get("course").get("id"), courseId));
            }

            if (certificateNumber != null && !certificateNumber.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("certificateNumber")), "%" + certificateNumber.toLowerCase() + "%"));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), CertificateStatus.valueOf(status.trim().toUpperCase())));
            }

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("issuedAt"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("issuedAt"), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
