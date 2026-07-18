package com.example.passwordmanager.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String masterKeySalt;

    @Column(nullable = false)
    private long createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordEntity> passwords = new ArrayList<>();

    public User() {}

    public User(String username, String passwordHash, String masterKeySalt) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be empty");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("Password hash required");
        if (masterKeySalt == null || masterKeySalt.isBlank()) throw new IllegalArgumentException("Salt required");
        this.username = username;
        this.passwordHash = passwordHash;
        this.masterKeySalt = masterKeySalt;
        this.createdAt = System.currentTimeMillis();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getMasterKeySalt() { return masterKeySalt; }
    public long getCreatedAt() { return createdAt; }
    public List<PasswordEntity> getPasswords() { return passwords; }
}