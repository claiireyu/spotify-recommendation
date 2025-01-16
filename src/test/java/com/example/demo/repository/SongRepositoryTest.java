package com.example.demo.repository;

import com.example.demo.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

    private Song testSong;

    @BeforeEach
    void setUp() {
        testSong = new Song();
        testSong.setTitle("Test Song");
        testSong.setArtist("Test Artist");
        testSong.setGenre("Test Genre");
    }

    @Test
    @Rollback(false)
    void saveSong_Success() {
        // Act
        Song savedSong = songRepository.save(testSong);

        // Assert
        assertThat(savedSong).isNotNull();
        assertThat(savedSong.getId()).isNotNull();
        assertThat(savedSong.getTitle()).isEqualTo(testSong.getTitle());
    }

    @Test
    void findByArtist_ReturnsMatchingSongs() {
        // Arrange
        songRepository.save(testSong);

        // Act
        List<Song> foundSongs = songRepository.findByArtist(testSong.getArtist());

        // Assert
        assertThat(foundSongs).isNotEmpty();
        assertThat(foundSongs.get(0).getArtist()).isEqualTo(testSong.getArtist());
    }

    @Test
    void findByGenre_ReturnsMatchingSongs() {
        // Arrange
        songRepository.save(testSong);

        // Act
        List<Song> foundSongs = songRepository.findByGenre(testSong.getGenre());

        // Assert
        assertThat(foundSongs).isNotEmpty();
        assertThat(foundSongs.get(0).getGenre()).isEqualTo(testSong.getGenre());
    }

    @Test
    void findSimilarSongs_ReturnsMatchingSongs() {
        // Arrange
        songRepository.save(testSong);

        // Act
        List<Song> foundSongs = songRepository.findSimilarSongs(testSong.getGenre(), testSong.getArtist());

        // Assert
        assertThat(foundSongs).isNotEmpty();
        assertThat(foundSongs.get(0).getGenre()).isEqualTo(testSong.getGenre());
        assertThat(foundSongs.get(0).getArtist()).isEqualTo(testSong.getArtist());
    }
} 