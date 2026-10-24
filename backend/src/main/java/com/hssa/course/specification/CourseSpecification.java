package com.hssa.course.specification;

import com.hssa.course.dto.request.SearchCourseRequest;
import com.hssa.course.entity.Course;
import com.hssa.course.enums.CourseLevel;
import com.hssa.course.enums.CourseStatus;
import com.hssa.course.enums.CourseVisibility;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CourseSpecification {

    public static Specification<Course> filterBy(SearchCourseRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Only courses that are not soft-deleted
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), keyword),
                        cb.like(cb.lower(root.get("subtitle")), keyword),
                        cb.like(cb.lower(root.get("description")), keyword)
                ));
            }

            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), request.getCategoryId()));
            }

            if (request.getLevel() != null && !request.getLevel().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("level"),
                        CourseLevel.valueOf(request.getLevel().trim().toUpperCase())));
            }

            if (request.getLanguage() != null && !request.getLanguage().trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("language")), request.getLanguage().trim().toLowerCase()));
            }

            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"),
                        CourseStatus.valueOf(request.getStatus().trim().toUpperCase())));
            }

            if (request.getVisibility() != null && !request.getVisibility().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("visibility"),
                        CourseVisibility.valueOf(request.getVisibility().trim().toUpperCase())));
            }

            if (request.getInstructorId() != null) {
                Join<Object, Object> instructorsJoin = root.join("instructors");
                predicates.add(cb.equal(instructorsJoin.get("instructorId"), request.getInstructorId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
