# Song Recommendation System Requirements

## Phase 1: Basic Recommendation System

### Input
- List of song objects with basic metadata:
  - Title
  - Genre
  - Artist
  - Album

### Output
- List of recommended songs sorted by similarity score
- Genre distribution analysis via /genre-distribution endpoint
- Artist distribution analysis via /artist-distribution endpoint
- Available songs listing via /available endpoint
- Configurable limit for number of recommendations returned (default: 10)

### Features
- Content-based recommendation system analyzing user's music profile
- Matches based on aggregated preferences from inputted songs:
  - Specifically, genre preferences (derived from frequency analysis)
- Genre diversity analysis and weighting:
  - Analyzes genre distribution in user's target songs
  - Identifies preferred genres (>30% representation)
  - Ensures recommendations reflect user's genre diversity
- Artist diversity analysis and weighting:
  - Identifies frequent artist (>1 time in input songs)
  - Prioritizes songs from frequent artists

### Scoring System
- Genre Match: 0.6 (60%)
  - Full weight (0.6) for songs matching preferred genres
- Artist Match: 0.4 (40%)
  - Full weight (0.4) for preferred artists (appearing multiple times in target songs)
  

## Phase 2: Advanced Weighted Scoring System

### Feature Weights
| Feature          | Weight |
|-----------------|---------|
| Genre Match     | 35%     |
| Artist Match    | 25%     |
| Album Match     | 20%     |
| Release Year    | 20%     |

### Core Requirements

#### Weight Configuration
- Store weights in recommendation.properties
- Ensure weights total 100%

#### Scoring Algorithm
1. Calculate individual feature similarity scores:
   - Genre: Exact match (1.0) or no match (0.0)
   - Artist: Exact match (1.0) or no match (0.0)
   - Album: Exact match (1.0) or no match (0.0)
   - Year: Scaled score based on release year proximity
2. Apply weights to each feature score
3. Sum weighted scores for final recommendation score

#### Output Requirements
- Return sorted list of recommendations
- Apply minimum score threshold (0.5)
- Configurable limit for number of recommendations (top N)
- Include similarity scores in results

### Technical Notes
- All weights should be configurable via recommendation.properties
- System should be extensible for additional features in future phases
- Implement proper error handling for missing metadata