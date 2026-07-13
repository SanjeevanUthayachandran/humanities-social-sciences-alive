package com.hssa.course.repository;

import com.hssa.course.entity.CourseInstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CourseInstructorRepository extends JpaRepository<CourseInstructor, UUID> {
}
