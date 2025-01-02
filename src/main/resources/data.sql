-- Insert sample songs
INSERT INTO songs (title, artist, album, genre) VALUES
('Bohemian Rhapsody', 'Queen', 'A Night at the Opera', 'Rock'),
('Billie Jean', 'Michael Jackson', 'Thriller', 'Pop'),
('Sweet Child O Mine', 'Guns N Roses', 'Appetite for Destruction', 'Rock'),
('Shape of You', 'Ed Sheeran', '÷ (Divide)', 'Pop'),
('Despacito', 'Luis Fonsi', 'VIDA', 'Latin Pop'),
('Rolling in the Deep', 'Adele', '21', 'Pop'),
('Stairway to Heaven', 'Led Zeppelin', 'Led Zeppelin IV', 'Rock'),
('Hotel California', 'Eagles', 'Hotel California', 'Rock'),
('Thriller', 'Michael Jackson', 'Thriller', 'Pop'),
('Someone Like You', 'Adele', '21', 'Pop');

-- Insert sample user preferences
INSERT INTO user_preferences (user_id, preferred_genre, preferred_artist) VALUES
('user1', 'Rock', 'Queen'),
('user2', 'Pop', 'Michael Jackson'),
('user3', 'Rock', 'Led Zeppelin'),
('user4', 'Pop', 'Adele'); 