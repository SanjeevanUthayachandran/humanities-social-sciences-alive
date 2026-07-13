package com.hssa.course.repository;

import com.hssa.course.entity.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CourseTagRepository extends JpaRepository<CourseTag, UUID> {
    Optional<CourseTag> findByTagName(String tagName);
}
