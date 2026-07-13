package com.hssa.literary.repository;

import com.hssa.literary.entity.LiteraryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LiteraryCategoryRepository extends JpaRepository<LiteraryCategory, UUID> {
}
