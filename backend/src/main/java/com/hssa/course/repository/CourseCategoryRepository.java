package com.hssa.course.repository;

import com.hssa.course.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, UUID> {
    Optional<CourseCategory> findBySlugAndDeletedAtIsNull(String slug);
    Optional<CourseCategory> findByIdAndDeletedAtIsNull(UUID id);
}
