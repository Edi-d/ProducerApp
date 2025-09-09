package com.example.icecreamshop.security.config;

import com.example.icecreamshop.security.entity.Role;
import com.example.icecreamshop.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.findByName(Role.ERole.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(Role.ERole.ROLE_USER));
        }

        if (roleRepository.findByName(Role.ERole.ROLE_MODERATOR).isEmpty()) {
            roleRepository.save(new Role(Role.ERole.ROLE_MODERATOR));
        }

        if (roleRepository.findByName(Role.ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(Role.ERole.ROLE_ADMIN));
        }

        System.out.println("Database initialized with default roles");
    }
}
