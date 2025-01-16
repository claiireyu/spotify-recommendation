package com.example.demo.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {
    private List<Song> inputSongs;
} 