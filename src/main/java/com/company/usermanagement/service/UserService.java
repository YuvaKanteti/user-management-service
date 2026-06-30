package com.company.usermanagement.service;

import java.util.List;

import com.company.usermanagement.request.CreateUserRequest;
import com.company.usermanagement.request.UpdateUserRequest;
import com.company.usermanagement.request.UpdateUserStatusRequest;
import com.company.usermanagement.response.DashboardResponse;
import com.company.usermanagement.response.UserResponse;

public interface UserService {

    Long createUser(CreateUserRequest request);
    
    List<UserResponse> getAllUsers();
    
    DashboardResponse getDashboard();
    
    void updateUserStatus(
            Long userId,
            UpdateUserStatusRequest request);
    
    UserResponse getUserById(Long id);
    
    void updateUser(
            Long userId,
            UpdateUserRequest request);
    
    void deleteUser(Long userId);

}