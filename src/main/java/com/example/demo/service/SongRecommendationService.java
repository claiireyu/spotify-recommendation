package com.example.demo.service;

import com.example.demo.model.Song;
import com.example.demo.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongRecommendationService {

    @Autowired
    private SongRepository songRepository;

    public List<Song> getRecommendations(Long songId) {
        Song referenceSong = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found"));

        List<Song> similarSongs = songRepository.findSimilarSongs(
            referenceSong.getGenre(),
            referenceSong.getArtist()
        );

        // Remove the reference song from recommendations
        return similarSongs.stream()
            .filter(song -> !song.getId().equals(songId))
            .limit(5)
            .collect(Collectors.toList());
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }
} 