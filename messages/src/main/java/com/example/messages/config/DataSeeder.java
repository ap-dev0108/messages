package com.example.messages.config;

import com.example.messages.entity.Roles;
import com.example.messages.entity.User;
import com.example.messages.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String adminEmail = "admin@example.com";

            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            User admin = new User();

            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPassword(
                    passwordEncoder.encode("change-this-password")
            );
            admin.setUserRole(Roles.ADMIN);

            userRepository.save(admin);
        };
    }
}