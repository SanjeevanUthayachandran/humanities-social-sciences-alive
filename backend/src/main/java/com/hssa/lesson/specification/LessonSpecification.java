package com.hssa.lesson.specification;

import com.hssa.lesson.dto.request.SearchLessonRequest;
import com.hssa.lesson.entity.Lesson;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class LessonSpecification {

    public static Specification<Lesson> filterBy(SearchLessonRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (request.getModuleId() != null) {
                predicates.add(cb.equal(root.get("module").get("id"), request.getModuleId()));
            }
            
            if (request.getCourseId() != null) {
                predicates.add(cb.equal(root.get("module").get("course").get("id"), request.getCourseId()));
            }

            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), keyword),
                        cb.like(cb.lower(root.get("description")), keyword)
                ));
            }

            if (request.getLessonType() != null && !request.getLessonType().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("lessonType").as(String.class), request.getLessonType().trim().toUpperCase()));
            }

            if (request.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), request.getIsActive()));
            }

            if (request.getIsFreePreview() != null) {
                predicates.add(cb.equal(root.get("isFreePreview"), request.getIsFreePreview()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
