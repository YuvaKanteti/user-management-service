package com.company.usermanagement.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.usermanagement.entity.RoleEntity;
import com.company.usermanagement.entity.UserEntity;
import com.company.usermanagement.enums.RoleName;
import com.company.usermanagement.enums.UserStatus;
import com.company.usermanagement.exception.BadRequestException;
import com.company.usermanagement.exception.ResourceAlreadyExistsException;
import com.company.usermanagement.exception.ResourceNotFoundException;
import com.company.usermanagement.repository.RoleRepository;
import com.company.usermanagement.repository.UserRepository;
import com.company.usermanagement.request.CreateUserRequest;
import com.company.usermanagement.request.UpdateUserRequest;
import com.company.usermanagement.request.UpdateUserStatusRequest;
import com.company.usermanagement.response.DashboardResponse;
import com.company.usermanagement.response.UserResponse;
import com.company.usermanagement.service.UserService;
import com.company.usermanagement.utils.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long createUser(CreateUserRequest request) {

        log.info("Creating user with mobile : {}", request.getMobile());

        validateUser(request);

        RoleEntity userRole = roleRepository
                .findByRoleName(RoleName.USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "USER role not found"));

        UserEntity user = buildUserEntity(request, userRole);

        UserEntity savedUser = userRepository.save(user);

        log.info("User created successfully with id : {}",
                savedUser.getId());

        return savedUser.getId();
    }

    private void validateUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new ResourceAlreadyExistsException(
                    "Email already exists");
        }

        if (userRepository.existsByMobile(request.getMobile())) {

            throw new ResourceAlreadyExistsException(
                    "Mobile number already exists");
        }
    }

    private UserEntity buildUserEntity(
            CreateUserRequest request,
            RoleEntity userRole) {

        UserEntity user = new UserEntity();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(
        	    passwordEncoder.encode(AppConstants.DEFAULT_PASSWORD)
        	);
        user.setMobile(request.getMobile());

        user.setStatus(UserStatus.ACTIVE);
        user.setIsDeleted(false);

        user.setRoles(Set.of(userRole));

        return user;
    }
    
    @Override
    public List<UserResponse> getAllUsers() {

        log.info("Fetching all registered users");

        return userRepository.findAllRegisteredUsers()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    private UserResponse mapToResponse(UserEntity user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .status(user.getStatus().name())
                .build();
    }
    
    @Override
    public DashboardResponse getDashboard() {

        long totalUsers = userRepository.countRegisteredUsers();

        long activeUsers =
        		userRepository.countRegisteredUsersByStatus(
                        UserStatus.ACTIVE);

        long inactiveUsers =
        		userRepository.countRegisteredUsersByStatus(
                        UserStatus.INACTIVE);

        return DashboardResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .build();
    }
    
    @Override
    public void updateUserStatus(
            Long userId,
            UpdateUserStatusRequest request) {

        log.info("Updating status for user id : {}", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getRoleName() == RoleName.ADMIN);

        if (isAdmin) {
        	throw new BadRequestException(
        	        "Admin status cannot be modified");
        }

        user.setStatus(request.getStatus());

        userRepository.save(user);

        log.info("Status updated successfully for user id : {}",
                userId);
    }
    
    @Override
    public UserResponse getUserById(Long id) {

        UserEntity user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id));

        return mapToResponse(user);
    }
    
    @Override
    public void updateUser(
            Long userId,
            UpdateUserRequest request) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getRoleName() == RoleName.ADMIN);

        if (isAdmin) {
            throw new BadRequestException(
                    "Admin user cannot be modified");
        }

        if (userRepository.existsByEmailAndIdNot(
                request.getEmail(),
                userId)) {

            throw new ResourceAlreadyExistsException(
                    "Email already exists");
        }

        if (userRepository.existsByMobileAndIdNot(
                request.getMobile(),
                userId)) {

            throw new ResourceAlreadyExistsException(
                    "Mobile already exists");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());

        userRepository.save(user);
    }
    
    @Override
    public void deleteUser(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getRoleName() == RoleName.ADMIN);

        if (isAdmin) {
            throw new BadRequestException(
                    "Admin user cannot be deleted");
        }

        user.setIsDeleted(true);

        userRepository.save(user);
    }
}