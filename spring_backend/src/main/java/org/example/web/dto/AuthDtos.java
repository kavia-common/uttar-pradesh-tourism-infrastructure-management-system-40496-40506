package org.example.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * PUBLIC_INTERFACE
 * DTOs for authentication requests and responses.
 */
public class AuthDtos {

    public static class LoginRequest {
        @Schema(description = "Username", example = "admin")
        @NotBlank
        public String username;

        @Schema(description = "Password", example = "Password@123")
        @NotBlank
        public String password;
    }

    public static class RegisterRequest {
        @NotBlank
        public String username;
        @Email
        public String email;
        @NotBlank
        public String password;
        @NotBlank
        public String role; // e.g., ADMIN or USER
    }

    public static class TokenResponse {
        public String token;
        public TokenResponse(String token) { this.token = token; }
    }
}
