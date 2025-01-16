#!/bin/bash

echo "Testing empty input songs..."
curl -X POST \
  http://localhost:8080/api/songs/recommendations \
  -H 'Content-Type: application/json' \
  -d '{"inputSongs": []}' | json_pp

echo -e "\nTesting null input..."
curl -X POST \
  http://localhost:8080/api/songs/recommendations \
  -H 'Content-Type: application/json' \
  -d '{}' | json_pp

echo -e "\nTesting invalid song data..."
curl -X POST \
  http://localhost:8080/api/songs/recommendations \
  -H 'Content-Type: application/json' \
  -d '{
    "inputSongs": [{
      "title": "",
      "artist": null,
      "genre": ""
    }]
  }' | json_pp

echo -e "\nTesting valid request with limit..."
curl -X POST \
  'http://localhost:8080/api/songs/recommendations?limit=2' \
  -H 'Content-Type: application/json' \
  -d '{
    "inputSongs": [{
      "title": "Test Song",
      "artist": "Test Artist",
      "genre": "Rock"
    }]
  }' | json_pp

echo -e "\nTesting genre distribution with empty list..."
curl -X POST \
  http://localhost:8080/api/songs/genre-distribution \
  -H 'Content-Type: application/json' \
  -d '[]' | json_pp 