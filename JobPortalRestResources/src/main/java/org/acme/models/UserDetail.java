package org.acme.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="users")
public class UserDetail extends BaseEntity{
    @Column(name="username",nullable = false, unique = true)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="created_at",nullable = false)
    private LocalDate createdAt;


    @PrePersist
    public void setCreatedAtDefault() {
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now();
        }
    }

    public enum Role{
        seeker,provider,admin
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
