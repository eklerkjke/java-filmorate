package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    private static final String QUERY_INSERT_FILM = """
            INSERT INTO films(name, description, releaseDate, duration, mpaId)
            VALUES (:name, :description, :releaseDate, :duration, :mpaId);
            """;

    private static final String QUERY_INSERT_FILMS_GENRES_BY_FILM_ID = """
            INSERT INTO films_genres(filmId, genreId)
            VALUES (:filmId, :genreId);
            """;

    private static final String QUERY_INSERT_LIKE_FILM = """
            INSERT INTO likes(filmId, userId)
            VALUES (:filmId, :userId)
            """;

    private static final String QUERY_DELETE_LIKE_FILM = "DELETE FROM likes WHERE filmId = :filmId AND userId = :userId";

    private static final String QUERY_GET_LIKES_BY_FILM = "SELECT userId FROM likes WHERE filmId = :filmId";

    private static final String QUERY_DELETE_FILM_GENRES_BY_FILM_ID = "DELETE FROM films_genres WHERE filmId = :filmId";

    private static final String QUERY_UPDATE_FILM = """
            UPDATE films
            SET name = :name, description = :description, releaseDate = :releaseDate, duration = :duration, mpaId = :mpaId
            WHERE id = :id;
            """;

    private static final String QUERY_GET_ALL = """
            SELECT f.*, m.name AS mpaName
            FROM films f
            JOIN mpa m ON f.mpaId = m.id;
            """;

    private static final String QUERY_GET_BY_ID = """
            SELECT f.*, m.name AS mpaName, g.id, g.name AS genreName
            FROM films f
            JOIN mpa m ON f.mpaId = m.id
            LEFT JOIN films_genres fg ON f.id = fg.filmId
            LEFT JOIN genres g ON fg.genreId = g.id
            WHERE f.id = :id;
            """;

    public FilmRepository(NamedParameterJdbcOperations jdbcOperations, GenreStorage genreStorage, MpaStorage mpaStorage) {
        super(jdbcOperations);
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film add(Film film) {
        mpaStorage.getById(film.getMpa().getId())
                .orElseThrow(() -> new BadRequestException("Ошибка, mpa не найден"));

        for (Genre genre : film.getGenres()) {
            genreStorage.getById(genre.getId()).orElseThrow(() -> new BadRequestException("Жанр не найден"));
        }

        long id = insert(QUERY_INSERT_FILM, toMapSqlParameterSource(film));
        film.setId(id);

        if (film.getGenres() != null) {
            jdbcOperations.batchUpdate(QUERY_INSERT_FILMS_GENRES_BY_FILM_ID, getFilmIdAndGenreIdsSqlParameters(film));
        }

        film.setGenres(new LinkedHashSet<>(genreStorage.getGenresByFilmId(id)));
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        update(QUERY_UPDATE_FILM, toMapSqlParameterSource(newFilm));

        jdbcOperations.update(
                QUERY_DELETE_FILM_GENRES_BY_FILM_ID,
                new MapSqlParameterSource("filmId", newFilm.getId())
        );

        if (newFilm.getGenres() != null) {
            jdbcOperations.batchUpdate(QUERY_INSERT_FILMS_GENRES_BY_FILM_ID, getFilmIdAndGenreIdsSqlParameters(newFilm));
        }

        return newFilm;
    }

    @Override
    public Optional<Film> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        Optional<Film> film = findOne(QUERY_GET_BY_ID, params);
        if (!film.isEmpty()) {
            film.get().setGenres(new HashSet<>(genreStorage.getGenresByFilmId(id)));
        }
        return film;
    }

    @Override
    public List<Film> getList() {
        List<Film> films = findAll(QUERY_GET_ALL);
        for (Film film : films) {
            film.setGenres(new HashSet<>(genreStorage.getGenresByFilmId(film.getId())));
            film.setLikes(getLikesFilm(film.getId()));
        }
        return films;
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId)
                .addValue("userId", userId);
        jdbcOperations.update(QUERY_INSERT_LIKE_FILM, params);
    }

    @Override
    public void unLikeFilm(Long filmId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId)
                .addValue("userId", userId);
        jdbcOperations.update(QUERY_DELETE_LIKE_FILM, params);
    }

    @Override
    public Set<Long> getLikesFilm(Long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId);

        Set<Long> likes = new HashSet<>();
        SqlRowSet likesRows = jdbcOperations.queryForRowSet(QUERY_GET_LIKES_BY_FILM, params);
        while (likesRows.next()) {
            likes.add(likesRows.getLong("userId"));
        }
        return likes;
    }

    @Override
    public List<Film> getTopFilms() {
        return List.of();
    }

    @Override
    protected void modelMapping(MapSqlParameterSource params, Film model) {
        if (model.getId() > 0) {
            params.addValue("id", model.getId());
        }

        params.addValue("name", model.getName());
        params.addValue("description", model.getDescription());
        params.addValue("releaseDate", model.getReleaseDate());
        params.addValue("duration", model.getDuration());

        if (model.getMpa() != null) {
            params.addValue("mpaId", model.getMpa().getId());
        } else {
            params.addValue("mpaId", null);
        }
    }

    @Override
    protected RowMapper<Film> getRowMapper() {
        return (rs, rowNum) -> Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(
                        Mpa.builder()
                                .id(rs.getInt("mpaId"))
                                .name(rs.getString("mpaName"))
                                .build()
                )
                .likes(new HashSet<>())
                .genres(new LinkedHashSet<>())
                .build();
    }

    private SqlParameterSource[] getFilmIdAndGenreIdsSqlParameters(Film film) {
        Set<Genre> genres = film.getGenres();

        return genres.stream()
                .map(genre -> new MapSqlParameterSource("filmId", film.getId())
                        .addValue("genreId", genre.getId()))
                .toArray(SqlParameterSource[]::new);
    }
}
