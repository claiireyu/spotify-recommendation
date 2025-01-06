package com.example.demo.controller;

import com.example.demo.model.Song;
import com.example.demo.model.RecommendationRequest;
import com.example.demo.service.RecommendationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SongController.class)
public class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Song testSong;
    private List<Song> testSongs;

    @BeforeEach
    void setUp() {
        testSong = new Song();
        testSong.setTitle("Test Song");
        testSong.setArtist("Test Artist");
        testSong.setGenre("Test Genre");

        testSongs = Arrays.asList(testSong);
    }

    @Test
    void getRecommendations_ValidRequest_ReturnsOk() throws Exception {
        // Arrange
        RecommendationRequest request = new RecommendationRequest();
        request.setInputSongs(testSongs);
        when(recommendationService.getRecommendations(any())).thenReturn(testSongs);

        // Act & Assert
        mockMvc.perform(post("/api/songs/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testSongs)));
    }

    @Test
    void getRecommendations_NullRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/songs/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRecommendations_InvalidSong_ReturnsBadRequest() throws Exception {
        // Arrange
        Song invalidSong = new Song();  // Missing required fields
        RecommendationRequest request = new RecommendationRequest();
        request.setInputSongs(Collections.singletonList(invalidSong));

        // Act & Assert
        mockMvc.perform(post("/api/songs/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getGenreDistribution_ValidRequest_ReturnsOk() throws Exception {
        // Arrange
        Map<String, Double> distribution = new HashMap<>();
        distribution.put("Test Genre", 1.0);
        when(recommendationService.getGenreDistribution(any())).thenReturn(distribution);

        // Act & Assert
        mockMvc.perform(post("/api/songs/genre-distribution")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSongs)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(distribution)));
    }

    @Test
    void getArtistDistribution_ValidRequest_ReturnsOk() throws Exception {
        // Arrange
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("Test Artist", 1L);
        when(recommendationService.getArtistDistribution(any())).thenReturn(distribution);

        // Act & Assert
        mockMvc.perform(post("/api/songs/artist-distribution")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSongs)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(distribution)));
    }

    @Test
    void getAvailableSongs_ReturnsOk() throws Exception {
        // Arrange
        when(recommendationService.getAllSongs()).thenReturn(testSongs);

        // Act & Assert
        mockMvc.perform(get("/api/songs/available"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testSongs)));
    }

    @Test
    void getRecommendations_WithLimit_ReturnsLimitedResults() throws Exception {
        // Arrange
        RecommendationRequest request = new RecommendationRequest();
        request.setInputSongs(testSongs);
        List<Song> multipleSongs = Arrays.asList(
            testSong,
            testSong,
            testSong
        );
        when(recommendationService.getRecommendations(any())).thenReturn(multipleSongs);

        // Act & Assert
        mockMvc.perform(post("/api/songs/recommendations")
                .param("limit", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getRecommendations_EmptyInputSongs_ReturnsBadRequest() throws Exception {
        // Arrange
        RecommendationRequest request = new RecommendationRequest();
        request.setInputSongs(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(post("/api/songs/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Input songs are required"));
    }

    @Test
    void getRecommendations_ServiceThrowsException_ReturnsBadRequest() throws Exception {
        // Arrange
        RecommendationRequest request = new RecommendationRequest();
        request.setInputSongs(testSongs);
        when(recommendationService.getRecommendations(any()))
            .thenThrow(new RuntimeException("Test error"));

        // Act & Assert
        mockMvc.perform(post("/api/songs/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Test error"));
    }
} 