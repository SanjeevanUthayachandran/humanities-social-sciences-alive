package com.hssa.lesson.repository;

import com.hssa.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID>, JpaSpecificationExecutor<Lesson> {
    List<Lesson> findByModuleIdAndDeletedAtIsNullOrderByDisplayOrderAsc(UUID moduleId);
    Optional<Lesson> findByIdAndDeletedAtIsNull(UUID id);
    Optional<Lesson> findBySlugAndDeletedAtIsNull(String slug);
    boolean existsBySlug(String slug);

    @Query("SELECT l FROM Lesson l JOIN l.module m WHERE m.course.id = :courseId AND l.deletedAt IS NULL AND m.deletedAt IS NULL ORDER BY m.displayOrder ASC, l.displayOrder ASC")
    List<Lesson> findAllByCourseId(@Param("courseId") UUID courseId);
}
