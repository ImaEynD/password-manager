package com.example.passwordmanager.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.passwordmanager.model.User;
import com.example.passwordmanager.repository.UserRepository;
import com.example.passwordmanager.util.JwtUtil;

@Service
public final class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = Objects.requireNonNull(userRepository, "UserRepository cannot be null");
        this.jwtUtil = Objects.requireNonNull(jwtUtil, "JwtUtil cannot be null");
        this.passwordEncoder = new BCryptPasswordEncoder(12);
        this.secureRandom = new SecureRandom();
    }

    public String register(String username, String rawPassword, String masterKey) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if (masterKey == null || masterKey.length() < 8) {
            throw new IllegalArgumentException("Master key must be at least 8 characters");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String passwordHash = passwordEncoder.encode(rawPassword);

        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        String salt = Base64.getEncoder().encodeToString(saltBytes);

        userRepository.save(new User(username, passwordHash, salt));
        return jwtUtil.generateToken(username);
    }

    public String login(String username, String rawPassword) {
        if (username == null || username.isBlank() || rawPassword == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtUtil.generateToken(username);
    }

    public String getMasterKeySalt(String username) {
        return userRepository.findByUsername(username)
                .map(User::getMasterKeySalt)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}