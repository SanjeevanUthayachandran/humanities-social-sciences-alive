package com.hssa.course.repository;

import com.hssa.course.entity.CourseInstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CourseInstructorRepository extends JpaRepository<CourseInstructor, UUID> {
    List<CourseInstructor> findAllByInstructorId(UUID instructorId);
    boolean existsByCourse_IdAndInstructorId(UUID courseId, UUID instructorId);
}
