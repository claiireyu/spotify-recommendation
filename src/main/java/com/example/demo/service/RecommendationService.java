package com.example.demo.service;

import com.example.demo.model.Song;
import com.example.demo.repository.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    
    private static final double GENRE_PREFERENCE_THRESHOLD = 0.30;

    @Autowired
    private SongRepository songRepository;

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public List<Song> getRecommendations(List<Song> inputSongs) {
        if (inputSongs == null || inputSongs.isEmpty()) {
            return Collections.emptyList();
        }

        logger.info("Getting recommendations for {} input songs", inputSongs.size());

        // Get all songs from repository
        List<Song> allSongs = songRepository.findAll();
        
        // Get unique genres from input songs
        Set<String> inputGenres = inputSongs.stream()
            .map(Song::getGenre)
            .collect(Collectors.toSet());

        logger.info("Input genres: {}", inputGenres);

        // Filter songs that match genres but aren't in input
        return allSongs.stream()
            .filter(song -> inputGenres.contains(song.getGenre()))
            .filter(song -> !inputSongs.contains(song))
            .collect(Collectors.toList());
    }

    public Map<String, Double> getGenreDistribution(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return Collections.emptyMap();
        }

        // Count occurrences of each genre
        Map<String, Long> genreCounts = songs.stream()
            .map(Song::getGenre)
            .collect(Collectors.groupingBy(
                genre -> genre,
                Collectors.counting()
            ));

        // Calculate distribution percentages
        double totalSongs = songs.size();
        return genreCounts.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue() / totalSongs
            ));
    }

    public Map<String, Long> getArtistDistribution(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return Collections.emptyMap();
        }

        return songs.stream()
            .map(Song::getArtist)
            .collect(Collectors.groupingBy(
                artist -> artist,
                Collectors.counting()
            ));
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }
} 