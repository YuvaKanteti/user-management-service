package com.company.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.usermanagement.entity.UserEntity;
import com.company.usermanagement.enums.UserStatus;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByMobile(String mobile);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByEmail(String email);
    
    long countByStatus(UserStatus status);
    
    @Query("""
    	       SELECT COUNT(DISTINCT u.id)
    	       FROM UserEntity u
    	       JOIN u.roles r
    	       WHERE r.roleName = com.company.usermanagement.enums.RoleName.USER
    	       AND u.isDeleted = false
    	       """)
    	long countRegisteredUsers();
    
    @Query("""
    	       SELECT COUNT(DISTINCT u.id)
    	       FROM UserEntity u
    	       JOIN u.roles r
    	       WHERE r.roleName = com.company.usermanagement.enums.RoleName.USER
    	       AND u.isDeleted = false
    	       AND u.status = :status
    	       """)
    	long countRegisteredUsersByStatus(
    	        @Param("status") UserStatus status);
    
    boolean existsByEmailAndIdNot(
            String email,
            Long id);

    boolean existsByMobileAndIdNot(
            String mobile,
            Long id);
    
    List<UserEntity> findByIsDeletedFalse();

    Optional<UserEntity> findByIdAndIsDeletedFalse(Long id);
    
    @Query("""
    	       SELECT DISTINCT u
    	       FROM UserEntity u
    	       JOIN u.roles r
    	       WHERE r.roleName = com.company.usermanagement.enums.RoleName.USER
    	       AND u.isDeleted = false
    	       """)
    	List<UserEntity> findAllRegisteredUsers();
}