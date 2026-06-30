package com.company.usermanagement.service;

import com.company.usermanagement.request.ChangePasswordRequest;
import com.company.usermanagement.request.LoginRequest;
import com.company.usermanagement.request.UpdateProfileRequest;
import com.company.usermanagement.response.LoggedInUserResponse;
import com.company.usermanagement.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
    
    LoggedInUserResponse getLoggedInUser();
    
    void changePassword(ChangePasswordRequest request);
    
    void updateProfile(UpdateProfileRequest request);
}