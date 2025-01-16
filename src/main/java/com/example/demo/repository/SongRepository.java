package com.example.demo.repository;

import com.example.demo.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtist(String artist);
    List<Song> findByGenre(String genre);
    
    @Query("SELECT s FROM Song s WHERE " +
           "s.genre = :genre OR " +
           "s.artist = :artist")
    List<Song> findSimilarSongs(
        @Param("genre") String genre,
        @Param("artist") String artist
    );
} 