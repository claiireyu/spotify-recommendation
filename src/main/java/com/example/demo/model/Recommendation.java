package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    
    private LocalDateTime recommendationDate;

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

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public LocalDateTime getRecommendationDate() {
        return recommendationDate;
    }

    public void setRecommendationDate(LocalDateTime recommendationDate) {
        this.recommendationDate = recommendationDate;
    }
} 