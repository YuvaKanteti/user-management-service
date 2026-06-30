package com.company.usermanagement.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.usermanagement.entity.UserEntity;
import com.company.usermanagement.exception.BadRequestException;
import com.company.usermanagement.repository.UserRepository;
import com.company.usermanagement.request.ChangePasswordRequest;
import com.company.usermanagement.request.LoginRequest;
import com.company.usermanagement.request.UpdateProfileRequest;
import com.company.usermanagement.response.LoggedInUserResponse;
import com.company.usermanagement.response.LoginResponse;
import com.company.usermanagement.security.CustomUserDetails;
import com.company.usermanagement.security.JwtService;
import com.company.usermanagement.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {

        System.out.println("===== STEP 1 =====");

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getMobile(),
                                request.getPassword()));

        System.out.println("===== STEP 2 =====");

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        System.out.println("===== STEP 3 =====");

        String token = jwtService.generateToken(user);

        System.out.println("===== STEP 4 =====");

        String role = user.getUser()
                .getRoles()
                .iterator()
                .next()
                .getRoleName()
                .name();

        System.out.println("===== STEP 5 =====");

        return new LoginResponse(
                token,
                role,
                user.getUser().getFirstName());
    }
    
    @Override
    public LoggedInUserResponse getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication);

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        System.out.println(userDetails);

        UserEntity user = userDetails.getUser();

        System.out.println(user);

        LoggedInUserResponse response =
                LoggedInUserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .mobile(user.getMobile())
                        .role(user.getRoles()
                                  .iterator()
                                  .next()
                                  .getRoleName()
                                  .name())
                        .build();

        System.out.println(response);

        return response;
    }
    
    @Override
    public void changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        UserEntity user = userDetails.getUser();

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
    
    @Override
    public void updateProfile(UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        UserEntity user = userDetails.getUser();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        userRepository.save(user);
    }
}