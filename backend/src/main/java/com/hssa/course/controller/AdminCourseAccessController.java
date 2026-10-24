package com.hssa.course.controller;

import com.hssa.auth.entity.Role;
import com.hssa.auth.entity.User;
import com.hssa.auth.repository.RoleRepository;
import com.hssa.auth.repository.UserRepository;
import com.hssa.common.response.ApiResponse;
import com.hssa.course.dto.request.AssignInstructorByEmailRequest;
import com.hssa.course.entity.Course;
import com.hssa.course.entity.CourseInstructor;
import com.hssa.course.repository.CourseInstructorRepository;
import com.hssa.course.repository.CourseRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/admin/course-access")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCourseAccessController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;
    private final CourseInstructorRepository instructorRepository;

    @PostMapping("/instructors")
    @Transactional
    public ResponseEntity<ApiResponse<String>> assignInstructor(
            @Valid @RequestBody AssignInstructorByEmailRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No registered user has this email. Ask them to register first."));
        Course course = courseRepository.findByIdAndDeletedAtIsNull(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Role instructorRole = roleRepository.findByRoleCode("INSTRUCTOR")
                .orElseThrow(() -> new ResourceNotFoundException("Instructor role not found"));

        user.getRoles().add(instructorRole);
        userRepository.save(user);
        if (instructorRepository.existsByCourse_IdAndInstructorId(course.getId(), user.getId())) {
            throw new BusinessException("This instructor is already assigned to the course");
        }
        instructorRepository.save(CourseInstructor.builder()
                .course(course)
                .instructorId(user.getId())
                .isPrimary(request.isPrimary())
                .displayOrder(course.getInstructors().size())
                .build());

        return ResponseEntity.ok(ApiResponse.success(
                email + " can now manage " + course.getTitle(), "Instructor assigned"));
    }
}
