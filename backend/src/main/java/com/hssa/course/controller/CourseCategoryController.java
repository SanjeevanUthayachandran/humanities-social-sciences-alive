package com.hssa.course.controller;

import com.hssa.course.dto.response.CourseCategoryResponse;
import com.hssa.course.repository.CourseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course-categories")
@RequiredArgsConstructor
public class CourseCategoryController {

    private final CourseCategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CourseCategoryResponse>> listCategories() {
        return ResponseEntity.ok(categoryRepository.findAll().stream()
                .filter(category -> category.getDeletedAt() == null && category.getIsActive())
                .sorted((left, right) -> left.getDisplayOrder().compareTo(right.getDisplayOrder()))
                .map(category -> new CourseCategoryResponse(
                        category.getId(), category.getName(), category.getSlug(), category.getDescription()))
                .toList());
    }
}
