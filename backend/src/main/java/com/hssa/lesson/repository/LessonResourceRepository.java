package com.hssa.lesson.repository;

import com.hssa.lesson.entity.LessonResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LessonResourceRepository extends JpaRepository<LessonResource, UUID> {
    List<LessonResource> findByLessonIdAndDeletedAtIsNullOrderByDisplayOrderAsc(UUID lessonId);
}
