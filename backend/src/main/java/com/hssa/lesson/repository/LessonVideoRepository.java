package com.hssa.lesson.repository;

import com.hssa.lesson.entity.LessonVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LessonVideoRepository extends JpaRepository<LessonVideo, UUID> {
    Optional<LessonVideo> findByLessonId(UUID lessonId);
}
