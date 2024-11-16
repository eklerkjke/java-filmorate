package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
abstract public class BaseRepository<T> {
    protected final NamedParameterJdbcOperations jdbcOperations;

    protected Optional<T> findOne(String query, SqlParameterSource source) {
        try {
            List<T> result = jdbcOperations.query(query, source, getRowMapper());
            if (result.isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(result.getFirst());
        } catch (NonTransientDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, SqlParameterSource source) {
        return jdbcOperations.query(query, source, getRowMapper());
    }

    protected List<T> findAll(String query) {
        return jdbcOperations.query(query, getRowMapper());
    }

    protected void update(String query, SqlParameterSource source) {
        int rows = jdbcOperations.update(query, source);
        if (rows == 0) {
            throw new NotFoundException("Не удалось обновить данные");
        }
    }

    protected void merge(String query, SqlParameterSource source) {
        jdbcOperations.update(query, source);
    }

    protected long insert(String query, SqlParameterSource source) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(query, source, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            return id;
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
    }

    abstract protected void modelMapping(MapSqlParameterSource params, T model);

    abstract protected RowMapper<T> getRowMapper();

    protected SqlParameterSource toMapSqlParameterSource(T model) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        modelMapping(params, model);
        return params;
    }
}
