package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recommendations")
@Data
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "similarity_score")
    private double similarityScore;

    @Column(name = "recommendation_date")
    private java.time.LocalDateTime recommendationDate = java.time.LocalDateTime.now();
} 