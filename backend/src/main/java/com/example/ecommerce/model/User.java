package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100) private String name;
    @Column(nullable = false, unique = true, length = 150) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false, length = 20) private String role;
    @Column(nullable = false) private LocalDateTime createdAt;

    protected User() {}
    public User(String name, String email, String password, String role) {
        this.name = name; this.email = email.toLowerCase(); this.password = password;
        this.role = role; this.createdAt = LocalDateTime.now();
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
