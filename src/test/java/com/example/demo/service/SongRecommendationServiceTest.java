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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SongRecommendationServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongRecommendationService recommendationService;

    private Song testSong1;
    private Song testSong2;

    @BeforeEach
    void setUp() {
        testSong1 = new Song();
        testSong1.setId(1L);
        testSong1.setTitle("Test Song 1");
        testSong1.setArtist("Test Artist 1");
        testSong1.setGenre("Rock");

        testSong2 = new Song();
        testSong2.setId(2L);
        testSong2.setTitle("Test Song 2");
        testSong2.setArtist("Test Artist 2");
        testSong2.setGenre("Pop");
    }

    @Test
    void getRecommendations_ReturnsMatchingSongs() {
        // Arrange
        when(songRepository.findById(1L)).thenReturn(Optional.of(testSong1));
        when(songRepository.findSimilarSongs(testSong1.getGenre(), testSong1.getArtist()))
            .thenReturn(Arrays.asList(testSong1, testSong2));

        // Act
        List<Song> recommendations = recommendationService.getRecommendations(1L);

        // Assert
        assertThat(recommendations).hasSize(1);
        assertThat(recommendations).contains(testSong2);
        assertThat(recommendations).doesNotContain(testSong1); // shouldn't recommend input song
    }

    @Test
    void getRecommendations_SongNotFound_ThrowsException() {
        // Arrange
        when(songRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> recommendationService.getRecommendations(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Song not found");
    }

    @Test
    void saveSong_Success() {
        // Arrange
        when(songRepository.save(testSong1)).thenReturn(testSong1);

        // Act
        Song savedSong = recommendationService.saveSong(testSong1);

        // Assert
        assertThat(savedSong).isEqualTo(testSong1);
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