package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> implements MpaStorage {
    private static final String QUERY_GET_ALL = "SELECT * FROM mpa;";
    private static final String QUERY_GET_BY_ID = "SELECT * FROM mpa WHERE id = :id;";

    public MpaRepository(NamedParameterJdbcOperations jdbcOperations) {
        super(jdbcOperations);
    }

    @Override
    public Optional<Mpa> getById(Long id) {
        return findOne(QUERY_GET_BY_ID, new MapSqlParameterSource("id", id));
    }

    @Override
    public List<Mpa> getList() {
        return findAll(QUERY_GET_ALL);
    }

    @Override
    protected RowMapper<Mpa> getRowMapper() {
        return ((rs, rowNum) -> Mpa
                .builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build());
    }
}
