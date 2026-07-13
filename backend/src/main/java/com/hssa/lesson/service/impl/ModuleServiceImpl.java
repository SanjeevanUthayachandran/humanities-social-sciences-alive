package com.hssa.lesson.service.impl;

import com.hssa.course.entity.Course;
import com.hssa.course.repository.CourseRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.lesson.dto.request.CreateModuleRequest;
import com.hssa.lesson.dto.request.ReorderModuleRequest;
import com.hssa.lesson.dto.request.UpdateModuleRequest;
import com.hssa.lesson.dto.response.ModuleResponse;
import com.hssa.lesson.entity.Module;
import com.hssa.lesson.repository.ModuleRepository;
import com.hssa.lesson.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    private Course getCourse(UUID courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private void validateAccess(Course course, UUID userId, boolean isAdmin) {
        if (isAdmin) return;
        boolean isInstructor = course.getInstructors().stream()
                .anyMatch(ci -> ci.getInstructorId().equals(userId));
        if (!isInstructor && !course.getCreatedBy().equals(userId)) {
            throw new BusinessException("You do not have permission to modify this course's modules");
        }
    }

    @Override
    @Transactional
    public ModuleResponse createModule(CreateModuleRequest request, UUID userId, boolean isAdmin) {
        Course course = getCourse(request.getCourseId());
        validateAccess(course, userId, isAdmin);

        long count = moduleRepository.findByCourseIdAndDeletedAtIsNullOrderByDisplayOrderAsc(course.getId()).size();

        Module module = Module.builder()
                .course(course)
                .title(request.getTitle())
                .description(request.getDescription())
                .displayOrder((int) count)
                .isActive(true)
                .build();

        module = moduleRepository.save(module);
        return toModuleResponse(module);
    }

    @Override
    @Transactional
    public ModuleResponse updateModule(UUID id, UpdateModuleRequest request, UUID userId, boolean isAdmin) {
        Module module = moduleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));
        validateAccess(module.getCourse(), userId, isAdmin);

        if (request.getTitle() != null) module.setTitle(request.getTitle());
        if (request.getDescription() != null) module.setDescription(request.getDescription());

        module = moduleRepository.save(module);
        return toModuleResponse(module);
    }

    @Override
    @Transactional
    public void deleteModule(UUID id, UUID userId, boolean isAdmin) {
        Module module = moduleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));
        validateAccess(module.getCourse(), userId, isAdmin);

        module.setDeletedAt(LocalDateTime.now());
        moduleRepository.save(module);
    }

    @Override
    @Transactional
    public void reorderModules(UUID courseId, ReorderModuleRequest request, UUID userId, boolean isAdmin) {
        Course course = getCourse(courseId);
        validateAccess(course, userId, isAdmin);

        List<Module> modules = moduleRepository.findByCourseIdAndDeletedAtIsNullOrderByDisplayOrderAsc(courseId);
        for (int i = 0; i < request.getOrderedModuleIds().size(); i++) {
            UUID id = request.getOrderedModuleIds().get(i);
            modules.stream().filter(m -> m.getId().equals(id)).findFirst().ifPresent(m -> {
                m.setDisplayOrder(request.getOrderedModuleIds().indexOf(id));
                moduleRepository.save(m);
            });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleResponse> getModulesByCourse(UUID courseId) {
        return moduleRepository.findByCourseIdAndDeletedAtIsNullOrderByDisplayOrderAsc(courseId)
                .stream().map(this::toModuleResponse).collect(Collectors.toList());
    }

    private ModuleResponse toModuleResponse(Module module) {
        return ModuleResponse.builder()
                .id(module.getId())
                .courseId(module.getCourse().getId())
                .title(module.getTitle())
                .description(module.getDescription())
                .displayOrder(module.getDisplayOrder())
                .isActive(module.getIsActive())
                .lessons(new ArrayList<>())
                .build();
    }
}
