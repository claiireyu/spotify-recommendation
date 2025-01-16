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


        // Analyze preferences from input songs
        Map<String, Double> genrePreferences = analyzeGenrePreferences(inputSongs);
        Set<String> preferredGenres = getPreferredGenres(genrePreferences);
        
        // Get frequently appearing artists
        Set<String> frequentArtists = findFrequentArtists(inputSongs);
        
        // Add logging to debug
        logger.info("Preferred genres: {}", preferredGenres);
        logger.info("Frequent artists: {}", frequentArtists);

        // Get all available songs from database
        List<Song> availableSongs = songRepository.findAll();
        
        // Generate recommendations with detailed logging
        return availableSongs.stream()
            .filter(song -> !containsSimilarSong(inputSongs, song))
            .filter(song -> preferredGenres.contains(song.getGenre().toLowerCase()))
            .peek(song -> logger.debug("Calculating similarity for song: {} by {}", 
                song.getTitle(), song.getArtist()))
            .map(song -> {
                double similarity = calculateSimilarity(song, preferredGenres, frequentArtists);
                logger.debug("Similarity score for {} by {}: {}", 
                    song.getTitle(), song.getArtist(), similarity);
                return new SongSimilarity(song, similarity);
            })
            .sorted(Comparator.comparing(SongSimilarity::getSimilarity).reversed())
            .peek(songSim -> logger.debug("Final sorted position - Song: {} Score: {}", 
                songSim.getSong().getTitle(), songSim.getSimilarity()))
            .map(SongSimilarity::getSong)
            .collect(Collectors.toList());
    }

    private boolean containsSimilarSong(List<Song> inputSongs, Song song) {
        return inputSongs.stream()
            .anyMatch(inputSong -> 
                inputSong.getTitle().equals(song.getTitle()) && 
                inputSong.getArtist().equals(song.getArtist()));
    }

    private Set<String> findFrequentArtists(List<Song> songs) {
        Map<String, Long> artistCounts = songs.stream()
            .map(Song::getArtist)
            .collect(Collectors.groupingBy(
                artist -> artist,
                Collectors.counting()
            ));

        return artistCounts.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    private double calculateSimilarity(Song song, Set<String> preferredGenres, 
            Set<String> frequentArtists) {
        double similarity = 0.0;
        
        // Genre comparison (0.6 weight = 60%)
        if (preferredGenres.contains(song.getGenre().toLowerCase())) {
            similarity += 0.6;
        }
        
        // Artist comparison (0.4 weight = 40%)
        if (frequentArtists.contains(song.getArtist())) {
            similarity += 0.4;
        }
        
        return similarity;
    }

    private Map<String, Double> analyzeGenrePreferences(List<Song> songs) {
        Map<String, Long> genreCounts = songs.stream()
            .collect(Collectors.groupingBy(
                song -> song.getGenre().toLowerCase(),
                Collectors.counting()
            ));

        double totalSongs = songs.size();
        Map<String, Double> genrePercentages = new HashMap<>();
        
        genreCounts.forEach((genre, count) -> {
            double percentage = count / totalSongs;
            genrePercentages.put(genre, percentage);
        });

        return genrePercentages;
    }

    private Set<String> getPreferredGenres(Map<String, Double> genrePreferences) {
        return genrePreferences.entrySet().stream()
            .filter(entry -> entry.getValue() >= GENRE_PREFERENCE_THRESHOLD)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    public Map<String, Double> getGenreDistribution(List<Song> songs) {
        return analyzeGenrePreferences(songs);
    }

    public Map<String, Long> getArtistDistribution(List<Song> songs) {

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


    private static class SongSimilarity {
        private final Song song;
        private final double similarity;

        public SongSimilarity(Song song, double similarity) {
            this.song = song;
            this.similarity = similarity;
        }

        public Song getSong() {
            return song;
        }

        public double getSimilarity() {
            return similarity;
        }
    }

} 