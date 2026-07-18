package com.example.passwordmanager.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repository.UserRepository;
import com.example.passwordmanager.service.PasswordService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/passwords")
public class PasswordController {

    private static final Logger log = LoggerFactory.getLogger(PasswordController.class);

    private final PasswordService passwordService;
    private final UserRepository userRepository;

    public PasswordController(PasswordService passwordService, UserRepository userRepository) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
    }

    @PostMapping("/unlock")
    public ResponseEntity<Void> unlock(
            @RequestAttribute("username") String username,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UnlockRequest request) {
        
        String jwt = authHeader.replace("Bearer ", "");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        passwordService.unlockForUser(jwt, request.getMasterKey(), user.getMasterKeySalt());
        log.info("Vault unlocked for user: {}", username);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPasswords(
            @RequestAttribute("username") String username,
            @RequestHeader("X-Session-Key") String sessionKey) throws Exception {
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<Map<String, Object>> passwords = passwordService.getPasswords(sessionKey, user);
        return ResponseEntity.ok(passwords);
    }

    @GetMapping("/{service}")
    public ResponseEntity<Map<String, Object>> getPassword(
            @RequestAttribute("username") String username,
            @RequestHeader("X-Session-Key") String sessionKey,
            @PathVariable String service) throws Exception {
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Map<String, Object> entry = passwordService.getDecryptedEntry(sessionKey, user, service);
        return ResponseEntity.ok(entry);
    }

    @PostMapping
    public ResponseEntity<Void> addPassword(
            @RequestAttribute("username") String username,
            @RequestHeader("X-Session-Key") String sessionKey,
            @Valid @RequestBody PasswordRequest request) throws Exception {
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        passwordService.addPassword(sessionKey, user, request.getService(), 
                request.getLogin(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{service}")
    public ResponseEntity<Void> updatePassword(
            @RequestAttribute("username") String username,
            @RequestHeader("X-Session-Key") String sessionKey,
            @PathVariable String service,
            @Valid @RequestBody PasswordUpdateRequest request) throws Exception {
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        passwordService.updatePassword(sessionKey, user, service, request.getNewService(), 
                request.getLogin(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{service}")
    public ResponseEntity<Void> deletePassword(
            @RequestAttribute("username") String username,
            @RequestHeader("X-Session-Key") String sessionKey,
            @PathVariable String service) {
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        passwordService.deletePassword(sessionKey, user, service);
        return ResponseEntity.noContent().build();
    }

    // Request DTOs
    public static class UnlockRequest {
        @NotBlank(message = "Master key required")
        private String masterKey;

        public String getMasterKey() { return masterKey; }
        public void setMasterKey(String masterKey) { this.masterKey = masterKey; }
    }

    public static class PasswordRequest {
        @NotBlank(message = "Service required")
        private String service;
        @NotBlank(message = "Login required")
        private String login;
        @NotBlank(message = "Password required")
        private String password;

        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class PasswordUpdateRequest {
        @NotBlank(message = "New service required")
        private String newService;
        @NotBlank(message = "Login required")
        private String login;
        @NotBlank(message = "Password required")
        private String password;

        public String getNewService() { return newService; }
        public void setNewService(String newService) { this.newService = newService; }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}