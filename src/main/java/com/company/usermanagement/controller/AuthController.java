package com.company.usermanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.company.usermanagement.request.ChangePasswordRequest;
import com.company.usermanagement.request.LoginRequest;
import com.company.usermanagement.request.UpdateProfileRequest;
import com.company.usermanagement.response.ApiResponse;
import com.company.usermanagement.response.LoggedInUserResponse;
import com.company.usermanagement.response.LoginResponse;
import com.company.usermanagement.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Validated
            @RequestBody LoginRequest request) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Login Successful")
                        .data(response)
                        .build());
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoggedInUserResponse>>
    getLoggedInUser() {

        return ResponseEntity.ok(
                ApiResponse.<LoggedInUserResponse>builder()
                        .success(true)
                        .message("User fetched successfully")
                        .data(authService.getLoggedInUser())
                        .build());
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        authService.changePassword(request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Password changed successfully")
                        .data(null)
                        .build());
    }
    
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<String>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        authService.updateProfile(request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Profile updated successfully")
                        .data(null)
                        .build());
    }
}