package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;


@Entity
@Table(name = "songs")
@Data

@JsonIgnoreProperties(ignoreUnknown = true)

public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String artist;
    
    @Column
    private String album;
    
    @Column(nullable = false)
    private String genre;
} 