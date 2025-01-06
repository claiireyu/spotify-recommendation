package com.example.demo.service;

import com.example.demo.model.Song;
import com.example.demo.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private Song testSong1;
    private Song testSong2;

    @BeforeEach
    void setUp() {
        testSong1 = new Song();
        testSong1.setTitle("Test Song 1");
        testSong1.setArtist("Test Artist 1");
        testSong1.setGenre("Rock");

        testSong2 = new Song();
        testSong2.setTitle("Test Song 2");
        testSong2.setArtist("Test Artist 2");
        testSong2.setGenre("Pop");
    }

    @Test
    void getRecommendations_ReturnsMatchingSongs() {
        // Arrange
        // Create a third song with same genre as testSong1
        Song testSong3 = new Song();
        testSong3.setTitle("Test Song 3");
        testSong3.setArtist("Test Artist 3");
        testSong3.setGenre("Rock"); // Same genre as testSong1

        List<Song> inputSongs = Arrays.asList(testSong1);
        List<Song> allSongs = Arrays.asList(testSong1, testSong2, testSong3);
        when(songRepository.findAll()).thenReturn(allSongs);

        // Act
        List<Song> recommendations = recommendationService.getRecommendations(inputSongs);

        // Assert
        assertThat(recommendations).isNotEmpty();
        assertThat(recommendations).contains(testSong3); // Should recommend song with matching genre
        assertThat(recommendations).doesNotContain(testSong1); // Shouldn't recommend input song
    }

    @Test
    void getGenreDistribution_ReturnsCorrectDistribution() {
        // Arrange
        List<Song> songs = Arrays.asList(testSong1, testSong2);

        // Act
        Map<String, Double> distribution = recommendationService.getGenreDistribution(songs);

        // Assert
        assertThat(distribution).hasSize(2);
        assertThat(distribution.get("Rock")).isEqualTo(0.5);
        assertThat(distribution.get("Pop")).isEqualTo(0.5);
    }

    @Test
    void getArtistDistribution_ReturnsCorrectDistribution() {
        // Arrange
        List<Song> songs = Arrays.asList(testSong1, testSong2);

        // Act
        Map<String, Long> distribution = recommendationService.getArtistDistribution(songs);

        // Assert
        assertThat(distribution).hasSize(2);
        assertThat(distribution.get("Test Artist 1")).isEqualTo(1L);
        assertThat(distribution.get("Test Artist 2")).isEqualTo(1L);
    }

    @Test
    void getAllSongs_ReturnsAllSongs() {
        // Arrange
        List<Song> allSongs = Arrays.asList(testSong1, testSong2);
        when(songRepository.findAll()).thenReturn(allSongs);

        // Act
        List<Song> result = recommendationService.getAllSongs();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(testSong1, testSong2);
    }
} 