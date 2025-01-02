package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String preferredGenre;
    private String preferredArtist;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPreferredGenre() {
        return preferredGenre;
    }

    public void setPreferredGenre(String preferredGenre) {
        this.preferredGenre = preferredGenre;
    }

    public String getPreferredArtist() {
        return preferredArtist;
    }

    public void setPreferredArtist(String preferredArtist) {
        this.preferredArtist = preferredArtist;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 