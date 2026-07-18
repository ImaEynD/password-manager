package com.example.passwordmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.passwordmanager.service.AuthService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        String token = authService.register(request.getUsername(), request.getPassword(), request.getMasterKey());
        log.info("User registered: {}", request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        log.info("User logged in: {}", request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    public static class RegisterRequest {
        @NotBlank(message = "Username required")
        private String username;
        @NotBlank(message = "Password required")
        private String password;
        @NotBlank(message = "Master key required")
        private String masterKey;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getMasterKey() { return masterKey; }
        public void setMasterKey(String masterKey) { this.masterKey = masterKey; }
    }

    public static class LoginRequest {
        @NotBlank(message = "Username required")
        private String username;
        @NotBlank(message = "Password required")
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private final String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() { return token; }
    }
}