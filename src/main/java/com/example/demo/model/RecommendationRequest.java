package com.example.demo.model;

import java.util.List;
import lombok.Data;

@Data
public class RecommendationRequest {
    private List<Song> inputSongs;
} 