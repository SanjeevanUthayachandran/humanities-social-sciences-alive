package com.hssa.enrollment.specification;

import com.hssa.enrollment.dto.request.SearchEnrollmentRequest;
import com.hssa.enrollment.entity.Enrollment;
import com.hssa.enrollment.enums.EnrollmentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentSpecification {

    public static Specification<Enrollment> filterBy(SearchEnrollmentRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), request.getUserId()));
            }
            
            if (request.getCourseId() != null) {
                predicates.add(cb.equal(root.get("course").get("id"), request.getCourseId()));
            }

            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), EnrollmentStatus.valueOf(request.getStatus().trim().toUpperCase())));
            }

            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("course").get("title")), keyword),
                        cb.like(cb.lower(root.get("user").get("email")), keyword),
                        cb.like(cb.lower(root.get("user").get("firstName")), keyword),
                        cb.like(cb.lower(root.get("user").get("lastName")), keyword)
                ));
            }

            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("enrolledAt"), request.getFromDate()));
            }

            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("enrolledAt"), request.getToDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
