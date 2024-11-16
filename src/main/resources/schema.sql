CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS genres
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    description VARCHAR(200),
    releaseDate DATE,
    duration INT,
    mpaId BIGINT REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS films_genres
(
    filmId BIGINT NOT NULL REFERENCES films(id),
    genreId BIGINT NOT NULL REFERENCES genres(id)
);

CREATE TABLE IF NOT EXISTS friends
(
    userId BIGINT NOT NULL REFERENCES users(id),
    friendId BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS likes
(
    filmId BIGINT NOT NULL REFERENCES films(id),
    userId BIGINT NOT NULL REFERENCES users(id)
);