package org.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.service.AuthService;
import org.example.web.dto.AuthDtos.LoginRequest;
import org.example.web.dto.AuthDtos.RegisterRequest;
import org.example.web.dto.AuthDtos.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PUBLIC_INTERFACE
 * Authentication endpoints for login and registration.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and JWT token issuance")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with username and password to receive a JWT token.")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = auth.login(request.username, request.password);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Create a new user and receive a JWT token on success.")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        String token = auth.register(request.username, request.email, request.password, request.role);
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
