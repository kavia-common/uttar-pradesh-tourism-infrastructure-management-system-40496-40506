package org.example.config;

import org.example.domain.Role;
import org.example.domain.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository users, RoleRepository roles, PasswordEncoder encoder) {
        return args -> {
            if (users.count() == 0) {
                Role admin = roles.findByName("ADMIN").orElseGet(() -> roles.save(Role.builder().name("ADMIN").build()));
                users.save(User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(encoder.encode("admin123"))
                    .roles(Set.of(admin))
                    .build());
            }
        };
    }
}
