package com.example.passwordmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "passwords", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "service"}))
public class PasswordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String service;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String encryptedLogin;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String encryptedPassword;

    public PasswordEntity() {}

    public PasswordEntity(User user, String service, String encryptedLogin, String encryptedPassword) {
        this.user = user;
        this.service = service;
        this.encryptedLogin = encryptedLogin;
        this.encryptedPassword = encryptedPassword;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getEncryptedLogin() { return encryptedLogin; }
    public void setEncryptedLogin(String encryptedLogin) { this.encryptedLogin = encryptedLogin; }

    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }
}