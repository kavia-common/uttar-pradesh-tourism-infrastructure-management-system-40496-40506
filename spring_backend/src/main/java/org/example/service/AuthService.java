package org.example.service;

import org.example.domain.Role;
import org.example.domain.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * PUBLIC_INTERFACE
 * Authentication service: register and login issuing JWT tokens.
 */
@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;
    private final UserRepository users;
    private final RoleRepository roles;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authManager, PasswordEncoder encoder, UserRepository users,
                       RoleRepository roles, CustomUserDetailsService userDetailsService, JwtService jwtService) {
        this.authManager = authManager;
        this.encoder = encoder;
        this.users = users;
        this.roles = roles;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public String register(String username, String email, String rawPassword, String roleName) {
        if (users.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        Role role = roles.findByName(roleName).orElseGet(() -> roles.save(Role.builder().name(roleName).build()));
        User u = users.save(User.builder()
            .username(username)
            .email(email)
            .password(encoder.encode(rawPassword))
            .roles(Set.of(role))
            .build());
        var ud = userDetailsService.loadUserByUsername(u.getUsername());
        return jwtService.generateToken(ud, Map.of("roles", ud.getAuthorities()));
    }

    public String login(String username, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        var ud = userDetailsService.loadUserByUsername(username);
        return jwtService.generateToken(ud, Map.of("roles", ud.getAuthorities()));
    }
}
