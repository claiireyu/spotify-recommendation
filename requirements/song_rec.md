# Song Recommendation System Requirements

## Phase 1: Basic Recommendation System

### Input
- List of song objects with basic metadata:
  - Genre
  - Artist
  - Release year

### Output
- List of recommended songs sorted by similarity

### Features
- Simple content-based recommendation system
- Matches based on basic metadata
- Initial focus on easily obtainable data points

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