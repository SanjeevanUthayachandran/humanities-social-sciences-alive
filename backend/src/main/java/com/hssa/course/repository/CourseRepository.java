package com.hssa.course.repository;

import com.hssa.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID>, JpaSpecificationExecutor<Course> {
    Optional<Course> findBySlugAndDeletedAtIsNull(String slug);
    Optional<Course> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsBySlug(String slug);
    boolean existsByTitle(String title);
}
