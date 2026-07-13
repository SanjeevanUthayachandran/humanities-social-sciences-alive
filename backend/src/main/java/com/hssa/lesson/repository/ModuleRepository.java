package com.hssa.lesson.repository;

import com.hssa.lesson.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {
    List<Module> findByCourseIdAndDeletedAtIsNullOrderByDisplayOrderAsc(UUID courseId);
    Optional<Module> findByIdAndDeletedAtIsNull(UUID id);
}
