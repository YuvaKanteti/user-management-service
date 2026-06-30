package com.company.usermanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.company.usermanagement.request.CreateUserRequest;
import com.company.usermanagement.request.UpdateUserRequest;
import com.company.usermanagement.request.UpdateUserStatusRequest;
import com.company.usermanagement.response.ApiResponse;
import com.company.usermanagement.response.DashboardResponse;
import com.company.usermanagement.response.UserResponse;
import com.company.usermanagement.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        Long userId = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Long>builder()
                        .success(true)
                        .message("User created successfully")
                        .data(userId)
                        .build());
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> users =
                userService.getAllUsers();

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .message("Users fetched successfully")
                        .data(users)
                        .build());
    }
    
    @GetMapping("dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>>
    getDashboard() {

        return ResponseEntity.ok(
                ApiResponse.<DashboardResponse>builder()
                        .success(true)
                        .message("Dashboard fetched successfully")
                        .data(userService.getDashboard())
                        .build());
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {

        userService.updateUserStatus(id, request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("User status updated successfully")
                        .data(null)
                        .build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable("id") Long id) {

        UserResponse user =
                userService.getUserById(id);

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User fetched successfully")
                        .data(user)
                        .build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        userService.updateUser(id, request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("User updated successfully")
                        .data(null)
                        .build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable("id") Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("User deleted successfully")
                        .data(null)
                        .build());
    }
}