package com.hssa.literary.repository;

import com.hssa.literary.entity.LiteraryTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LiteraryTagRepository extends JpaRepository<LiteraryTag, UUID> {
    Optional<LiteraryTag> findByNameIgnoreCase(String name);
}
