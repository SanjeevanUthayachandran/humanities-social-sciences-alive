package com.hssa.enrollment.repository;

import com.hssa.enrollment.entity.Enrollment;
import com.hssa.enrollment.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID>, JpaSpecificationExecutor<Enrollment> {
    Optional<Enrollment> findByUserIdAndCourseIdAndDeletedAtIsNull(UUID userId, UUID courseId);
    Optional<Enrollment> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsByUserIdAndCourseIdAndDeletedAtIsNull(UUID userId, UUID courseId);
    List<Enrollment> findByCourseIdAndDeletedAtIsNull(UUID courseId);
    List<Enrollment> findByUserIdAndDeletedAtIsNull(UUID userId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ENROLLED' AND e.deletedAt IS NULL")
    long countEnrolledStudents(@Param("courseId") UUID courseId);
}
