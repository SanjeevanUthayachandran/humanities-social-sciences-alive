package com.hssa.lesson.controller;

import com.hssa.common.response.ApiResponse;
import com.hssa.lesson.dto.request.CreateModuleRequest;
import com.hssa.lesson.dto.request.ReorderModuleRequest;
import com.hssa.lesson.dto.request.UpdateModuleRequest;
import com.hssa.lesson.dto.response.ModuleResponse;
import com.hssa.lesson.service.ModuleService;
import com.hssa.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Module Management", description = "Endpoints for managing course modules/sections")
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping("/modules")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Create a module", description = "Create a new module for a course")
    public ResponseEntity<ApiResponse<ModuleResponse>> createModule(
            @Valid @RequestBody CreateModuleRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        ModuleResponse response = moduleService.createModule(request, userDetails.getId(), isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Module created successfully"));
    }

    @PutMapping("/modules/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Update a module", description = "Update an existing module")
    public ResponseEntity<ApiResponse<ModuleResponse>> updateModule(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateModuleRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        ModuleResponse response = moduleService.updateModule(id, request, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Module updated successfully"));
    }

    @DeleteMapping("/modules/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Delete a module", description = "Soft delete a module")
    public ResponseEntity<ApiResponse<Void>> deleteModule(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        moduleService.deleteModule(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Module deleted successfully"));
    }

    @PatchMapping("/courses/{courseId}/modules/reorder")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Reorder modules", description = "Reorder modules within a course")
    public ResponseEntity<ApiResponse<Void>> reorderModules(
            @PathVariable UUID courseId,
            @Valid @RequestBody ReorderModuleRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        moduleService.reorderModules(courseId, request, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Modules reordered successfully"));
    }

    @GetMapping("/courses/{courseId}/modules")
    @Operation(summary = "Get course modules", description = "Get all modules for a specific course")
    public ResponseEntity<ApiResponse<List<ModuleResponse>>> getModulesByCourse(@PathVariable UUID courseId) {
        List<ModuleResponse> responses = moduleService.getModulesByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success(responses, "Modules retrieved successfully"));
    }
}
