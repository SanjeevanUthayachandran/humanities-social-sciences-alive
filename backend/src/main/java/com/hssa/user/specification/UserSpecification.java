package com.hssa.user.specification;

import com.hssa.auth.entity.Role;
import com.hssa.auth.entity.User;
import com.hssa.user.dto.request.SearchUserRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterBy(SearchUserRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search by general keyword (in email, firstName, lastName)
            if (request.getSearch() != null && !request.getSearch().trim().isEmpty()) {
                String keyword = "%" + request.getSearch().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("email")), keyword),
                        cb.like(cb.lower(root.get("firstName")), keyword),
                        cb.like(cb.lower(root.get("lastName")), keyword),
                        cb.like(cb.lower(root.get("displayName")), keyword)
                ));
            }

            // Specific fields
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + request.getEmail().trim().toLowerCase() + "%"));
            }

            if (request.getDepartment() != null && !request.getDepartment().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("department")), "%" + request.getDepartment().trim().toLowerCase() + "%"));
            }

            if (request.getInstitution() != null && !request.getInstitution().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("institution")), "%" + request.getInstitution().trim().toLowerCase() + "%"));
            }

            if (request.getAccountStatus() != null && !request.getAccountStatus().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("accountStatus").as(String.class), request.getAccountStatus().trim().toUpperCase()));
            }

            if (request.getCreatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedAfter()));
            }

            if (request.getCreatedBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getCreatedBefore()));
            }

            // Role filtering (requires join)
            if (request.getRole() != null && !request.getRole().trim().isEmpty()) {
                Join<User, Role> rolesJoin = root.join("roles", JoinType.INNER);
                predicates.add(cb.equal(cb.upper(rolesJoin.get("roleCode")), request.getRole().trim().toUpperCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
