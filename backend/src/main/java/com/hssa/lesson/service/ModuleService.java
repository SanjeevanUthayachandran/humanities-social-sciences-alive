package com.hssa.lesson.service;

import com.hssa.lesson.dto.request.CreateModuleRequest;
import com.hssa.lesson.dto.request.ReorderModuleRequest;
import com.hssa.lesson.dto.request.UpdateModuleRequest;
import com.hssa.lesson.dto.response.ModuleResponse;

import java.util.List;
import java.util.UUID;

public interface ModuleService {
    ModuleResponse createModule(CreateModuleRequest request, UUID userId, boolean isAdmin);
    ModuleResponse updateModule(UUID id, UpdateModuleRequest request, UUID userId, boolean isAdmin);
    void deleteModule(UUID id, UUID userId, boolean isAdmin);
    void reorderModules(UUID courseId, ReorderModuleRequest request, UUID userId, boolean isAdmin);
    List<ModuleResponse> getModulesByCourse(UUID courseId);
}
