package com.hssa.payment.specification;

import com.hssa.payment.dto.request.SearchPaymentRequest;
import com.hssa.payment.entity.Payment;
import com.hssa.payment.enums.PaymentMethod;
import com.hssa.payment.enums.PaymentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentSpecification {

    public static Specification<Payment> filterBy(SearchPaymentRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), request.getUserId()));
            }

            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), PaymentStatus.valueOf(request.getStatus().trim().toUpperCase())));
            }

            if (request.getMethod() != null && !request.getMethod().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("paymentMethod"), PaymentMethod.valueOf(request.getMethod().trim().toUpperCase())));
            }

            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getFromDate()));
            }

            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getToDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
