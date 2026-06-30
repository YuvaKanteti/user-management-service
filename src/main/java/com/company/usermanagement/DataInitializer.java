package com.company.usermanagement;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.company.usermanagement.entity.RoleEntity;
import com.company.usermanagement.entity.UserEntity;
import com.company.usermanagement.enums.RoleName;
import com.company.usermanagement.enums.UserStatus;
import com.company.usermanagement.repository.RoleRepository;
import com.company.usermanagement.repository.UserRepository;
import com.company.usermanagement.utils.AppConstants;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createRoles();

        createDefaultAdmin();
    }

    private void createRoles() {

        if (!roleRepository.existsByRoleName(RoleName.ADMIN)) {

            RoleEntity adminRole = new RoleEntity();
            adminRole.setRoleName(RoleName.ADMIN);

            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByRoleName(RoleName.USER)) {

            RoleEntity userRole = new RoleEntity();
            userRole.setRoleName(RoleName.USER);

            roleRepository.save(userRole);
        }
    }

    private void createDefaultAdmin() {

        if (userRepository.existsByMobile("9999999999")) {
            return;
        }

        RoleEntity adminRole =
                roleRepository.findByRoleName(RoleName.ADMIN)
                        .orElseThrow();

        UserEntity admin = new UserEntity();

        admin.setFirstName("System");
        admin.setLastName("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(
        	    passwordEncoder.encode(AppConstants.DEFAULT_PASSWORD)
        		);
        admin.setMobile("9999999999");
        admin.setStatus(UserStatus.ACTIVE);

        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}