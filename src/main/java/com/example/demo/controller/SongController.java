package com.example.demo.controller;

import com.example.demo.model.Song;
import com.example.demo.model.RecommendationRequest;
import com.example.demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(
            @RequestBody RecommendationRequest request,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            logger.info("Received request: {}", request);
            
            if (request == null) {
                return ResponseEntity.badRequest().body("Request body cannot be null");
            }
            
            if (request.getInputSongs() == null || request.getInputSongs().isEmpty()) {
                return ResponseEntity.badRequest().body("Input songs are required");
            }
            
            // Validate individual songs
            for (Song song : request.getInputSongs()) {
                if (!isValidSong(song)) {
                    return ResponseEntity.badRequest().body("Invalid input song: " + song.getTitle());
                }
            }
            
            List<Song> recommendations = recommendationService.getRecommendations(
                request.getInputSongs()
            );
            
            // Limit the number of recommendations
            if (recommendations.size() > limit) {
                recommendations = recommendations.subList(0, limit);
            }
            
            logger.info("Generated {} recommendations", recommendations.size());
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            logger.error("Error processing recommendation request", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private boolean isValidSong(Song song) {
        return song != null &&
               song.getTitle() != null && !song.getTitle().isEmpty() &&
               song.getArtist() != null && !song.getArtist().isEmpty() &&
               song.getGenre() != null && !song.getGenre().isEmpty();
    }

    @PostMapping("/genre-distribution")
    public ResponseEntity<Map<String, Double>> getGenreDistribution(
            @RequestBody List<Song> songs) {
        return ResponseEntity.ok(recommendationService.getGenreDistribution(songs));
    }

    @PostMapping("/artist-distribution")
    public ResponseEntity<Map<String, Long>> getArtistDistribution(@RequestBody List<Song> songs) {
        return ResponseEntity.ok(recommendationService.getArtistDistribution(songs));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Song>> getAvailableSongs() {
        return ResponseEntity.ok(recommendationService.getAllSongs());
    }
} 