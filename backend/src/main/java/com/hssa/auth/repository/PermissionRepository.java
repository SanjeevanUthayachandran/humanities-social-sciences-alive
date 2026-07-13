package com.hssa.auth.repository;

import com.hssa.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByPermissionCode(String permissionCode);
    List<Permission> findByPermissionCodeIn(List<String> codes);
}
