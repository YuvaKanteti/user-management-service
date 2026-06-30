package com.company.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.usermanagement.entity.RoleEntity;
import com.company.usermanagement.enums.RoleName;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(RoleName roleName);

    boolean existsByRoleName(RoleName roleName);
}