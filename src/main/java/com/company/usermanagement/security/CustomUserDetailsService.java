package com.company.usermanagement.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.usermanagement.entity.UserEntity;
import com.company.usermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mobile)
            throws UsernameNotFoundException {

        UserEntity user = userRepository
                .findByMobile(mobile)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid mobile number"));

        return new CustomUserDetails(user);
    }
}