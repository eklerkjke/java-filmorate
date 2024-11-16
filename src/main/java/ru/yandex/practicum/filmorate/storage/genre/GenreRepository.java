package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {
    private static final String QUERY_GET_ALL = "SELECT * FROM genres";
    private static final String QUERY_GET_BY_ID = "SELECT * FROM genres WHERE id = :genreId";
    private static final String QUERY_SELECT_GENRES_BY_FILM_ID = """
            SELECT g.id, g.name
            FROM genres g
            JOIN films_genres fg ON g.id = fg.genreId
            WHERE fg.filmId = :filmId
            ORDER BY g.id ASC
            """;

    public GenreRepository(NamedParameterJdbcOperations jdbcOperations) {
        super(jdbcOperations);
    }

    @Override
    public Optional<Genre> getById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource("genreId", id);
        return findOne(QUERY_GET_BY_ID, params);
    }

    @Override
    public List<Genre> getList() {
        return findAll(QUERY_GET_ALL);
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        SqlParameterSource params = new MapSqlParameterSource("filmId", filmId);
        return findMany(QUERY_SELECT_GENRES_BY_FILM_ID, params);
    }

    @Override
    protected RowMapper<Genre> getRowMapper() {
        return ((rs, rowNum) -> Genre
                .builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build());
    }
}
